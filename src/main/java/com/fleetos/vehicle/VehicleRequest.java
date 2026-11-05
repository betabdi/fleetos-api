package com.fleetos.vehicle;
import jakarta.validation.constraints.*;
public record VehicleRequest(
 @NotBlank String plate,
 @NotBlank String make,
 @NotBlank String model,
 @NotNull @Min(1980) @Max(2100) Integer year,
 @NotNull @PositiveOrZero Long odometerMiles) {}
