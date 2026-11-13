package com.fleetos.trip;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
public interface TripStatusHistoryRepository extends JpaRepository<TripStatusHistory,Long>{
 List<TripStatusHistory> findByTrip_IdOrderByChangedAtAsc(Long tripId);
}
