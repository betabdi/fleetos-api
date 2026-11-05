package com.fleetos.trip;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fleetos.common.ApiException;
import com.fleetos.driver.Driver;
import com.fleetos.driver.DriverRepository;
import com.fleetos.vehicle.Vehicle;
import com.fleetos.vehicle.VehicleRepository;
import com.fleetos.vehicle.VehicleStatus;
@Service
public class TripService {
 private final TripRepository trips; private final VehicleRepository vehicles; private final DriverRepository drivers;
 public TripService(TripRepository trips,VehicleRepository vehicles,DriverRepository drivers){this.trips=trips;this.vehicles=vehicles;this.drivers=drivers;}
 public List<Trip> list(){return trips.findAll();}
 @Transactional
 public Trip create(TripRequest request){
  if(!request.deliveryAt().isAfter(request.pickupAt())) throw new ApiException(HttpStatus.BAD_REQUEST,"Delivery must occur after pickup");
  Vehicle vehicle=vehicles.findById(request.vehicleId()).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"Vehicle not found"));
  Driver driver=drivers.findById(request.driverId()).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"Driver not found"));
  if(vehicle.getStatus()!=VehicleStatus.AVAILABLE) throw new ApiException(HttpStatus.CONFLICT,"Vehicle unavailable");
  if(!driver.isActive()) throw new ApiException(HttpStatus.CONFLICT,"Driver inactive");
  if(trips.hasOverlap(vehicle.getId(),driver.getId(),request.pickupAt(),request.deliveryAt(),List.of(TripStatus.PLANNED,TripStatus.IN_PROGRESS)))
   throw new ApiException(HttpStatus.CONFLICT,"Vehicle or driver already assigned during that time");
  return trips.save(new Trip(vehicle,driver,request.origin().trim(),request.destination().trim(),request.pickupAt(),request.deliveryAt(),request.freightRate()));
 }
}
