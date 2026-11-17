package com.fleetos.vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
 Page<Vehicle> findAllByOrganizationId(Long organizationId,Pageable pageable);
 Optional<Vehicle> findByIdAndOrganizationId(Long id,Long organizationId);
 boolean existsByOrganizationIdAndPlateIgnoreCase(Long organizationId,String plate);
}
