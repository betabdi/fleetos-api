package com.fleetos.trip;
import com.fleetos.driver.Driver;
import com.fleetos.driver.DriverRepository;
import com.fleetos.vehicle.Vehicle;
import com.fleetos.vehicle.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest @AutoConfigureMockMvc class TripLifecycleApiTests {
 @Autowired MockMvc mvc; @Autowired TripRepository trips; @Autowired TripStatusHistoryRepository history;
 @Autowired VehicleRepository vehicles; @Autowired DriverRepository drivers;
 private Long tripId;
 @BeforeEach void setUp(){history.deleteAll();trips.deleteAll();vehicles.deleteAll();drivers.deleteAll();
  Vehicle vehicle=vehicles.save(new Vehicle("MN-LIFE-1","Freightliner","M2",2022,90000L));
  Driver driver=drivers.save(new Driver("Lifecycle Driver","555-0101"));
  Trip trip=trips.save(new Trip(vehicle,driver,"Minneapolis","Chicago",java.time.OffsetDateTime.parse("2027-01-05T08:00:00-06:00"),java.time.OffsetDateTime.parse("2027-01-05T16:00:00-06:00"),new java.math.BigDecimal("2100.00")));
  history.save(new TripStatusHistory(trip,null,TripStatus.PLANNED,"Trip scheduled"));tripId=trip.getId();}
 @Test void followsLifecycleAndRecordsAuditHistory() throws Exception {
  mvc.perform(patch("/api/v1/trips/{id}/status",tripId).contentType(MediaType.APPLICATION_JSON).content("{\"status\":\"IN_PROGRESS\",\"note\":\"Driver departed\"}"))
   .andExpect(status().isOk()).andExpect(jsonPath("$.status").value("IN_PROGRESS"));
  mvc.perform(patch("/api/v1/trips/{id}/status",tripId).contentType(MediaType.APPLICATION_JSON).content("{\"status\":\"COMPLETED\"}"))
   .andExpect(status().isOk()).andExpect(jsonPath("$.status").value("COMPLETED"));
  mvc.perform(get("/api/v1/trips/{id}/history",tripId)).andExpect(status().isOk())
   .andExpect(jsonPath("$[0].toStatus").value("PLANNED")).andExpect(jsonPath("$[1].toStatus").value("IN_PROGRESS"))
   .andExpect(jsonPath("$[1].note").value("Driver departed")).andExpect(jsonPath("$[2].toStatus").value("COMPLETED"));
 }
 @Test void rejectsInvalidLifecycleJump() throws Exception {
  mvc.perform(patch("/api/v1/trips/{id}/status",tripId).contentType(MediaType.APPLICATION_JSON).content("{\"status\":\"COMPLETED\"}"))
   .andExpect(status().isConflict()).andExpect(jsonPath("$.error").value("Cannot transition trip from PLANNED to COMPLETED"));
 }
 @Test void rejectsZeroFreightRate() throws Exception {
  mvc.perform(post("/api/v1/trips").contentType(MediaType.APPLICATION_JSON).content("""
   {"vehicleId":1,"driverId":1,"origin":"Minneapolis","destination":"Chicago","pickupAt":"2027-01-05T08:00:00-06:00","deliveryAt":"2027-01-05T16:00:00-06:00","freightRate":0}
   """)).andExpect(status().isBadRequest());
 }
}
