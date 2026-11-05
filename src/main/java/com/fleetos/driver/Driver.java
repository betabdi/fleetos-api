package com.fleetos.driver;
import jakarta.persistence.*;
@Entity @Table(name="drivers")
public class Driver {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(name="full_name",nullable=false,length=120) private String fullName;
 @Column(length=32) private String phone;
 @Column(nullable=false) private boolean active;
 protected Driver(){}
 public Driver(String fullName,String phone){this.fullName=fullName;this.phone=phone;this.active=true;}
 public Long getId(){return id;} public String getFullName(){return fullName;}
 public String getPhone(){return phone;} public boolean isActive(){return active;}
}
