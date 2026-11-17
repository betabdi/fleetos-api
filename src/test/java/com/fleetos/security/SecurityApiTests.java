package com.fleetos.security;
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
@SpringBootTest @AutoConfigureMockMvc class SecurityApiTests {
 @Autowired MockMvc mvc; @Autowired OrganizationRepository organizations; @Autowired ApiCredentialRepository credentials; @Autowired VehicleRepository vehicles;
 private Organization secondOrganization;
 @BeforeEach void setUp(){
  Organization legacy=organizations.findBySlug("legacy").orElseThrow();
  secondOrganization=organizations.findBySlug("second-fleet").orElseGet(()->organizations.save(new Organization("second-fleet","Second Fleet")));
  addCredential(legacy,"viewer","viewer-key",FleetRole.VIEWER);
  addCredential(secondOrganization,"second-admin","second-admin-key",FleetRole.ADMIN);
 }
 private void addCredential(Organization organization,String name,String key,FleetRole role){String hash=ApiKeyHasher.hash(key);if(credentials.findByKeyHashAndActiveTrue(hash).isEmpty())credentials.save(new ApiCredential(organization,name,hash,role));}
 @Test void requiresAuthentication() throws Exception {mvc.perform(get("/api/v1/vehicles")).andExpect(status().isUnauthorized());}
 @Test void viewerCanReadButCannotWrite() throws Exception {
  mvc.perform(get("/api/v1/vehicles").header(ApiKeyAuthenticationFilter.HEADER,"viewer-key")).andExpect(status().isOk());
  mvc.perform(post("/api/v1/vehicles").header(ApiKeyAuthenticationFilter.HEADER,"viewer-key").contentType(MediaType.APPLICATION_JSON)
   .content("{\"plate\":\"VIEW-1\",\"make\":\"Ford\",\"model\":\"F-650\",\"year\":2022,\"odometerMiles\":1}"))
   .andExpect(status().isForbidden());
 }
 @Test void organizationCannotReadAnotherOrganizationsVehicle() throws Exception {
  Vehicle foreign=vehicles.save(new Vehicle(secondOrganization,"TENANT-ONLY","Ford","F-650",2022,100L));
  mvc.perform(get("/api/v1/vehicles/{id}",foreign.getId()).header(ApiKeyAuthenticationFilter.HEADER,"test-admin-key")).andExpect(status().isNotFound());
  mvc.perform(get("/api/v1/vehicles/{id}",foreign.getId()).header(ApiKeyAuthenticationFilter.HEADER,"second-admin-key"))
   .andExpect(status().isOk()).andExpect(jsonPath("$.plate").value("TENANT-ONLY"));
 }
 @Test void adminCanCreateCredentialButViewerCannot() throws Exception {
  String body="{\"name\":\"night-dispatch\",\"role\":\"DISPATCHER\"}";
  mvc.perform(post("/api/v1/admin/credentials").header(ApiKeyAuthenticationFilter.HEADER,"test-admin-key").contentType(MediaType.APPLICATION_JSON).content(body))
   .andExpect(status().isCreated()).andExpect(jsonPath("$.role").value("DISPATCHER")).andExpect(jsonPath("$.apiKey").isString());
  mvc.perform(post("/api/v1/admin/credentials").header(ApiKeyAuthenticationFilter.HEADER,"viewer-key").contentType(MediaType.APPLICATION_JSON).content(body))
   .andExpect(status().isForbidden());
 }
}
