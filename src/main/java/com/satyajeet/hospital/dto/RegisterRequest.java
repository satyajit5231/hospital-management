package com.satyajeet.hospital.dto;
import com.satyajeet.hospital.entity.User;
import lombok.Data;
@Data
public class RegisterRequest {
    private String username;
    private String email;
    private String password;
    private User.Role role;
}
