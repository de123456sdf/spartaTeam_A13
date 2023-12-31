package com.example.sparta_a13.user;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UserPWModifyRequestDTO {
    private String beforePassword;
    @Pattern(regexp = "^[a-zA-Z0-9]{8,15}$")
    private String afterPassword;
}
