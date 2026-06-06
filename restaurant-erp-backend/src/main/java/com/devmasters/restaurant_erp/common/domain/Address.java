package com.devmasters.restaurant_erp.common.domain;

import jakarta.validation.constraints.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

    @NotBlank(message = "Address line is required")
    @Size(max = 255)
    private String addressLine;

    @Size(max = 100)
    private String city;

    @Size(max = 100)
    private String state;

    @Size(max = 100)
    private String country;

    @Pattern(regexp = "^[0-9]{4,10}$", message = "Invalid postal code")
    private String postalCode;

    private Double latitude;

    private Double longitude;
}
