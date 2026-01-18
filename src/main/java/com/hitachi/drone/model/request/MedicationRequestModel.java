package com.hitachi.drone.model.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicationRequestModel {
    @NotNull(message = "Name is required")
    @Pattern(
            regexp = "^[A-Za-z0-9_-]+$",
            message = "Only letters, numbers, '-' and '_' are allowed (no spaces)"
    )
    private String name;

    @NotNull(message = "Weight is required")
    private Integer weight;

    @NotNull(message = "Code is required")
    @Pattern(
            regexp = "^[A-Z0-9_]+$",
            message = "Only upper letters, numbers, and '_' are allowed (no spaces)"
    )
    private String Code;

    @NotNull(message = "Image is required")
    private String image;

    @NotNull(message = "Drone Serial Number is required")
    private String droneSerialNumber;
}
