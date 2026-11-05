package com.fleetos.vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
public interface VehicleRepository extends JpaRepository<Vehicle, Long> { boolean existsByPlateIgnoreCase(String plate); }
