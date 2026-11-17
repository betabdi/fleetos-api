package com.fleetos.driver;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
public interface DriverRepository extends JpaRepository<Driver,Long>{
 Page<Driver> findAllByOrganizationId(Long organizationId,Pageable pageable);
 Optional<Driver> findByIdAndOrganizationId(Long id,Long organizationId);
}
