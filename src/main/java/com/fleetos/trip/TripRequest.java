package com.fleetos.trip;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import jakarta.validation.constraints.*;
public record TripRequest(
 @NotNull Long vehicleId,
 @NotNull Long driverId,
 @NotBlank @Size(max=160) String origin,
 @NotBlank @Size(max=160) String destination,
 @NotNull OffsetDateTime pickupAt,
 @NotNull OffsetDateTime deliveryAt,
 @NotNull @DecimalMin(value="0.0",inclusive=false) BigDecimal freightRate) {}
