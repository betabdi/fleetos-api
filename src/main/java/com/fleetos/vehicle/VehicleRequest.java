package com.fleetos.vehicle;
import jakarta.validation.constraints.*;
public record VehicleRequest(
 @NotBlank @Size(max=32) String plate,
 @NotBlank @Size(max=80) String make,
 @NotBlank @Size(max=80) String model,
 @NotNull @Min(1980) @Max(2100) Integer year,
 @NotNull @PositiveOrZero Long odometerMiles) {}
