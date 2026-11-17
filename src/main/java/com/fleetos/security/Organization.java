package com.fleetos.security;
import jakarta.persistence.*;
@Entity @Table(name="organizations")
public class Organization {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,unique=true,length=64) private String slug;
 @Column(nullable=false,length=120) private String name;
 protected Organization(){}
 public Organization(String slug,String name){this.slug=slug;this.name=name;}
 public Long getId(){return id;} public String getSlug(){return slug;} public String getName(){return name;}
}
