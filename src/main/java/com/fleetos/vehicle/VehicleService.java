package com.fleetos.vehicle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.fleetos.common.ApiException;
import com.fleetos.security.TenantContext;
@Service
public class VehicleService {
  private final VehicleRepository repository;
  public VehicleService(VehicleRepository repository){this.repository=repository;}
  public Page<Vehicle> list(Pageable pageable){return repository.findAllByOrganizationId(organizationId(),pageable);}
  public Vehicle get(Long id){return repository.findByIdAndOrganizationId(id,organizationId()).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"Vehicle not found"));}
  public Vehicle create(VehicleRequest request){
    if(repository.existsByOrganizationIdAndPlateIgnoreCase(organizationId(),request.plate())) throw new ApiException(HttpStatus.CONFLICT,"Plate already registered");
    return repository.save(new Vehicle(TenantContext.organization(),request.plate().trim(), request.make().trim(), request.model().trim(),request.year(),request.odometerMiles()));
  }
  public Vehicle updateStatus(Long id, VehicleStatus status){Vehicle vehicle=get(id);vehicle.setStatus(status);return repository.save(vehicle);}
  private Long organizationId(){return TenantContext.organization().getId();}
}
