package com.fleetos.trip;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import jakarta.validation.constraints.*;
public record TripRequest(
 @NotNull Long vehicleId,
 @NotNull Long driverId,
 @NotBlank String origin,
 @NotBlank String destination,
 @NotNull OffsetDateTime pickupAt,
 @NotNull OffsetDateTime deliveryAt,
 @NotNull @DecimalMin("0.0") BigDecimal freightRate) {}
