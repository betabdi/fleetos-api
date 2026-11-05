package com.fleetos.vehicle;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest @AutoConfigureMockMvc class VehicleApiTests {
 @Autowired MockMvc mvc;
 @Test void createAndRejectDuplicatePlate() throws Exception {
  String body=""" 
    {"plate":"MN-TEST-431","make":"Isuzu","model":"NPR","year":2021,"odometerMiles":120000}
    """;
  mvc.perform(post("/api/v1/vehicles").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isCreated());
  mvc.perform(post("/api/v1/vehicles").contentType(MediaType.APPLICATION_JSON).content(body)).andExpect(status().isConflict());
 }
}
