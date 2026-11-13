package com.fleetos.trip;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/v1/trips")
public class TripController {
 private final TripService service;
 public TripController(TripService service){this.service=service;}
 @GetMapping public Page<Trip> list(@PageableDefault(size=20,sort="pickupAt") Pageable pageable){return service.list(pageable);}
 @GetMapping("/{id}") public Trip get(@PathVariable Long id){return service.get(id);}
 @PostMapping @ResponseStatus(HttpStatus.CREATED) public Trip create(@Valid @RequestBody TripRequest request){return service.create(request);}
 @PatchMapping("/{id}/status") public Trip transition(@PathVariable Long id,@Valid @RequestBody TransitionRequest request){return service.transition(id,request.status(),request.note());}
 @GetMapping("/{id}/history") public java.util.List<TripStatusHistory> history(@PathVariable Long id){return service.history(id);}
 public record TransitionRequest(@jakarta.validation.constraints.NotNull TripStatus status,@Size(max=240) String note){}
}
