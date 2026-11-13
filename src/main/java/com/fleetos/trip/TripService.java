package com.fleetos.trip;
import java.util.List;
import java.util.EnumSet;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
 private static final Map<TripStatus,EnumSet<TripStatus>> ALLOWED_TRANSITIONS=Map.of(
  TripStatus.PLANNED,EnumSet.of(TripStatus.IN_PROGRESS,TripStatus.CANCELED),
  TripStatus.IN_PROGRESS,EnumSet.of(TripStatus.COMPLETED,TripStatus.CANCELED),
  TripStatus.COMPLETED,EnumSet.noneOf(TripStatus.class),
  TripStatus.CANCELED,EnumSet.noneOf(TripStatus.class));
 private final TripRepository trips; private final VehicleRepository vehicles; private final DriverRepository drivers; private final TripStatusHistoryRepository history;
 public TripService(TripRepository trips,VehicleRepository vehicles,DriverRepository drivers,TripStatusHistoryRepository history){this.trips=trips;this.vehicles=vehicles;this.drivers=drivers;this.history=history;}
 public Page<Trip> list(Pageable pageable){return trips.findAll(pageable);}
 public Trip get(Long id){return trips.findById(id).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"Trip not found"));}
 @Transactional
 public Trip create(TripRequest request){
  if(!request.deliveryAt().isAfter(request.pickupAt())) throw new ApiException(HttpStatus.BAD_REQUEST,"Delivery must occur after pickup");
  Vehicle vehicle=vehicles.findById(request.vehicleId()).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"Vehicle not found"));
  Driver driver=drivers.findById(request.driverId()).orElseThrow(()->new ApiException(HttpStatus.NOT_FOUND,"Driver not found"));
  if(vehicle.getStatus()!=VehicleStatus.AVAILABLE) throw new ApiException(HttpStatus.CONFLICT,"Vehicle unavailable");
  if(!driver.isActive()) throw new ApiException(HttpStatus.CONFLICT,"Driver inactive");
  if(trips.hasOverlap(vehicle.getId(),driver.getId(),request.pickupAt(),request.deliveryAt(),List.of(TripStatus.PLANNED,TripStatus.IN_PROGRESS)))
   throw new ApiException(HttpStatus.CONFLICT,"Vehicle or driver already assigned during that time");
  Trip trip=trips.save(new Trip(vehicle,driver,request.origin().trim(),request.destination().trim(),request.pickupAt(),request.deliveryAt(),request.freightRate()));
  history.save(new TripStatusHistory(trip,null,TripStatus.PLANNED,"Trip scheduled"));
  return trip;
 }
 @Transactional
 public Trip transition(Long id,TripStatus target,String note){
  Trip trip=get(id); TripStatus current=trip.getStatus();
  if(current==target) throw new ApiException(HttpStatus.CONFLICT,"Trip is already "+target);
  if(!ALLOWED_TRANSITIONS.get(current).contains(target)) throw new ApiException(HttpStatus.CONFLICT,"Cannot transition trip from "+current+" to "+target);
  trip.transitionTo(target); history.save(new TripStatusHistory(trip,current,target,note)); return trip;
 }
 public List<TripStatusHistory> history(Long id){get(id);return history.findByTrip_IdOrderByChangedAtAsc(id);}
}
