package com.fleetos.security;
import jakarta.persistence.*;
@Entity @Table(name="api_credentials")
public class ApiCredential {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(optional=false,fetch=FetchType.EAGER) @JoinColumn(name="organization_id") private Organization organization;
 @Column(name="credential_name",nullable=false,length=120) private String name;
 @Column(name="key_hash",nullable=false,unique=true,length=64) private String keyHash;
 @Enumerated(EnumType.STRING) @Column(nullable=false,length=24) private FleetRole role;
 @Column(nullable=false) private boolean active;
 protected ApiCredential(){}
 public ApiCredential(Organization organization,String name,String keyHash,FleetRole role){this.organization=organization;this.name=name;this.keyHash=keyHash;this.role=role;this.active=true;}
 public Long getId(){return id;} public Organization getOrganization(){return organization;} public String getName(){return name;} public FleetRole getRole(){return role;} public boolean isActive(){return active;}
 public void revoke(){this.active=false;}
}
