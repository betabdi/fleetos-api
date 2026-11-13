package com.fleetos.trip;
import java.time.Instant;
import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
@Entity @Table(name="trip_status_history")
public class TripStatusHistory {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @JsonIgnore @ManyToOne(optional=false,fetch=FetchType.LAZY) @JoinColumn(name="trip_id") private Trip trip;
 @Enumerated(EnumType.STRING) @Column(name="from_status",length=24) private TripStatus fromStatus;
 @Enumerated(EnumType.STRING) @Column(name="to_status",nullable=false,length=24) private TripStatus toStatus;
 @Column(name="changed_at",nullable=false,updatable=false) private Instant changedAt;
 @Column(length=240) private String note;
 protected TripStatusHistory(){}
 public TripStatusHistory(Trip trip,TripStatus fromStatus,TripStatus toStatus,String note){this.trip=trip;this.fromStatus=fromStatus;this.toStatus=toStatus;this.note=normalize(note);this.changedAt=Instant.now();}
 private String normalize(String value){return value==null||value.isBlank()?null:value.trim();}
 public Long getId(){return id;} public Long getTripId(){return trip.getId();} public TripStatus getFromStatus(){return fromStatus;}
 public TripStatus getToStatus(){return toStatus;} public Instant getChangedAt(){return changedAt;} public String getNote(){return note;}
}
