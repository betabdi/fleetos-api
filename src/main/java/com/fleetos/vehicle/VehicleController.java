package com.fleetos.vehicle;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/v1/vehicles")
public class VehicleController {
 private final VehicleService service;
 public VehicleController(VehicleService service){this.service=service;}
 @GetMapping public Page<Vehicle> list(@PageableDefault(size=20,sort="plate") Pageable pageable){return service.list(pageable);}
 @GetMapping("/{id}") public Vehicle get(@PathVariable Long id){return service.get(id);}
 @PostMapping @ResponseStatus(HttpStatus.CREATED) public Vehicle create(@Valid @RequestBody VehicleRequest request){return service.create(request);}
 @PatchMapping("/{id}/status") public Vehicle status(@PathVariable Long id,@Valid @RequestBody StatusRequest request){return service.updateStatus(id,request.status());}
 public record StatusRequest(@NotNull VehicleStatus status) {}
}
