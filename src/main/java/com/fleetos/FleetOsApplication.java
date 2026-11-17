package com.fleetos;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
@SpringBootApplication(exclude=UserDetailsServiceAutoConfiguration.class)
@EnableSpringDataWebSupport(pageSerializationMode=EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
public class FleetOsApplication {
  public static void main(String[] args) { SpringApplication.run(FleetOsApplication.class, args); }
}
