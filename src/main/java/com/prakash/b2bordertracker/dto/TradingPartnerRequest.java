package com.prakash.b2bordertracker.dto;

import com.prakash.b2bordertracker.enums.PartnerType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TradingPartnerRequest {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "EDI ID is required")
    private String ediId;

    @NotNull(message = "Partner type is required")
    private PartnerType type;

    @Email(message = "Must be a valid email")
    @NotBlank(message = "Email is required")
    private String contactEmail;
}
