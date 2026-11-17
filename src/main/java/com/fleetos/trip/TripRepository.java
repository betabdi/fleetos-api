package com.fleetos.trip;
import java.time.OffsetDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
public interface TripRepository extends JpaRepository<Trip,Long> {
 Page<Trip> findAllByOrganizationId(Long organizationId,Pageable pageable);
 Optional<Trip> findByIdAndOrganizationId(Long id,Long organizationId);
 @Query("select count(t)>0 from Trip t where t.organization.id=:organizationId and t.status in :statuses and (t.vehicle.id=:vehicleId or t.driver.id=:driverId) and t.pickupAt < :endAt and t.deliveryAt > :startAt")
 boolean hasOverlap(@Param("organizationId") Long organizationId,@Param("vehicleId") Long vehicleId,@Param("driverId") Long driverId,
 @Param("startAt") OffsetDateTime startAt,@Param("endAt") OffsetDateTime endAt,
 @Param("statuses") java.util.List<TripStatus> statuses);
}
