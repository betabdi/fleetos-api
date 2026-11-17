package com.fleetos.trip;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest @AutoConfigureMockMvc class TripApiTests {
 @Autowired MockMvc mvc;
 @Test void missingVehicleRejected() throws Exception {
  mvc.perform(post("/api/v1/trips").header("X-FleetOS-Api-Key","test-admin-key").contentType(MediaType.APPLICATION_JSON).content("""
   {"vehicleId":999999,"driverId":999999,"origin":"Minneapolis","destination":"Chicago","pickupAt":"2027-01-05T08:00:00-06:00","deliveryAt":"2027-01-05T16:00:00-06:00","freightRate":2100}
   """)).andExpect(status().isNotFound());
 }
}
