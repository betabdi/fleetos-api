package com.fleetos.vehicle;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fleetos.security.Organization;
@Entity @Table(name="vehicles")
public class Vehicle {
  @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
  @JsonIgnore @ManyToOne(optional=false,fetch=FetchType.LAZY) @JoinColumn(name="organization_id") private Organization organization;
  @Column(nullable=false,unique=true,length=32) private String plate;
  @Column(nullable=false,length=80) private String make;
  @Column(nullable=false,length=80) private String model;
  @Column(name="model_year",nullable=false) private Integer year;
  @Column(name="odometer_miles",nullable=false) private Long odometerMiles;
  @Enumerated(EnumType.STRING) @Column(nullable=false,length=24) private VehicleStatus status;
  protected Vehicle() {}
  public Vehicle(Organization organization,String plate,String make,String model,Integer year,Long odometerMiles) {
    this.organization=organization;this.plate=plate;this.make=make;this.model=model;this.year=year;this.odometerMiles=odometerMiles;this.status=VehicleStatus.AVAILABLE;
  }
  public Long getId(){return id;} public String getPlate(){return plate;} public String getMake(){return make;}
  public String getModel(){return model;} public Integer getYear(){return year;} public Long getOdometerMiles(){return odometerMiles;}
  public VehicleStatus getStatus(){return status;}
  public void setStatus(VehicleStatus status){this.status=status;}
}
