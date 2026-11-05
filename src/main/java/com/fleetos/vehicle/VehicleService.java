package com.fleetos.vehicle;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.fleetos.common.ApiException;
@Service
public class VehicleService {
  private final VehicleRepository repository;
  public VehicleService(VehicleRepository repository){this.repository=repository;}
  public List<Vehicle> list(){return repository.findAll();}
  public Vehicle get(Long id){return repository.findById(id).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"Vehicle not found"));}
  public Vehicle create(VehicleRequest request){
    if(repository.existsByPlateIgnoreCase(request.plate())) throw new ApiException(HttpStatus.CONFLICT,"Plate already registered");
    return repository.save(new Vehicle(request.plate().trim(), request.make().trim(), request.model().trim(),request.year(),request.odometerMiles()));
  }
  public Vehicle updateStatus(Long id, VehicleStatus status){Vehicle vehicle=get(id);vehicle.setStatus(status);return repository.save(vehicle);}
}
