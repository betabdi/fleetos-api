package com.fleetos.trip;
import java.time.OffsetDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
public interface TripRepository extends JpaRepository<Trip,Long> {
 @Query("select count(t)>0 from Trip t where t.status in :statuses and (t.vehicle.id=:vehicleId or t.driver.id=:driverId) and t.pickupAt < :endAt and t.deliveryAt > :startAt")
 boolean hasOverlap(@Param("vehicleId") Long vehicleId,@Param("driverId") Long driverId,
 @Param("startAt") OffsetDateTime startAt,@Param("endAt") OffsetDateTime endAt,
 @Param("statuses") java.util.List<TripStatus> statuses);
}
