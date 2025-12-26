package com.isagulova.spring_eshop.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private String username;
    private String password;
    private String email;
    private String matchingPassword;
    private boolean activated;
    private String phoneNumber;
    private String address;
}
