package com.fleetos.driver;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fleetos.security.Organization;
@Entity @Table(name="drivers")
public class Driver {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @JsonIgnore @ManyToOne(optional=false,fetch=FetchType.LAZY) @JoinColumn(name="organization_id") private Organization organization;
 @Column(name="full_name",nullable=false,length=120) private String fullName;
 @Column(length=32) private String phone;
 @Column(nullable=false) private boolean active;
 protected Driver(){}
 public Driver(Organization organization,String fullName,String phone){this.organization=organization;this.fullName=fullName;this.phone=phone;this.active=true;}
 public Long getId(){return id;} public String getFullName(){return fullName;}
 public String getPhone(){return phone;} public boolean isActive(){return active;}
}
