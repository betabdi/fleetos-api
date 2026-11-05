package com.fleetos.trip;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import com.fleetos.vehicle.Vehicle;
import com.fleetos.driver.Driver;
import jakarta.persistence.*;
@Entity @Table(name="trips")
public class Trip {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(optional=false,fetch=FetchType.EAGER) @JoinColumn(name="vehicle_id") private Vehicle vehicle;
 @ManyToOne(optional=false,fetch=FetchType.EAGER) @JoinColumn(name="driver_id") private Driver driver;
 @Column(nullable=false,length=160) private String origin;
 @Column(nullable=false,length=160) private String destination;
 @Column(name="pickup_at",nullable=false) private OffsetDateTime pickupAt;
 @Column(name="delivery_at",nullable=false) private OffsetDateTime deliveryAt;
 @Column(name="freight_rate",nullable=false,precision=12,scale=2) private BigDecimal freightRate;
 @Enumerated(EnumType.STRING) @Column(nullable=false,length=24) private TripStatus status;
 protected Trip(){}
 public Trip(Vehicle vehicle,Driver driver,String origin,String destination,OffsetDateTime pickupAt,OffsetDateTime deliveryAt,BigDecimal freightRate){
  this.vehicle=vehicle;this.driver=driver;this.origin=origin;this.destination=destination;this.pickupAt=pickupAt;
  this.deliveryAt=deliveryAt;this.freightRate=freightRate;this.status=TripStatus.PLANNED;
 }
 public Long getId(){return id;} public Long getVehicleId(){return vehicle.getId();} public Long getDriverId(){return driver.getId();}
 public String getOrigin(){return origin;} public String getDestination(){return destination;}
 public OffsetDateTime getPickupAt(){return pickupAt;} public OffsetDateTime getDeliveryAt(){return deliveryAt;}
 public BigDecimal getFreightRate(){return freightRate;} public TripStatus getStatus(){return status;}
}
