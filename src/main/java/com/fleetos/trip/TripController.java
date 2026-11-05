package com.fleetos.trip;
import java.util.List;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/api/v1/trips")
public class TripController {
 private final TripService service;
 public TripController(TripService service){this.service=service;}
 @GetMapping public List<Trip> list(){return service.list();}
 @PostMapping @ResponseStatus(HttpStatus.CREATED) public Trip create(@Valid @RequestBody TripRequest request){return service.create(request);}
}
