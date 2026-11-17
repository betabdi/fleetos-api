package com.fleetos.driver;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import com.fleetos.security.TenantContext;
@RestController @RequestMapping("/api/v1/drivers")
public class DriverController {
 private final DriverRepository repository;
 public DriverController(DriverRepository repository){this.repository=repository;}
 @GetMapping public Page<Driver> list(@PageableDefault(size=20,sort="fullName") Pageable pageable){return repository.findAllByOrganizationId(TenantContext.organization().getId(),pageable);}
 @PostMapping @ResponseStatus(HttpStatus.CREATED) public Driver create(@Valid @RequestBody DriverRequest request){return repository.save(new Driver(TenantContext.organization(),request.fullName().trim(),request.phone()));}
 public record DriverRequest(@NotBlank @Size(max=120) String fullName,@Size(max=32) String phone){}
}
