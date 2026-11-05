package com.fleetos.driver;
import java.util.List;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/v1/drivers")
public class DriverController {
 private final DriverRepository repository;
 public DriverController(DriverRepository repository){this.repository=repository;}
 @GetMapping public List<Driver> list(){return repository.findAll();}
 @PostMapping @ResponseStatus(HttpStatus.CREATED) public Driver create(@Valid @RequestBody DriverRequest request){return repository.save(new Driver(request.fullName().trim(),request.phone()));}
 public record DriverRequest(@NotBlank String fullName,String phone){}
}
