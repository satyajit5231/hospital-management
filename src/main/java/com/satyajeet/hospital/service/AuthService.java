package com.satyajeet.hospital.service;
import com.satyajeet.hospital.dto.*;
import com.satyajeet.hospital.entity.User;
import com.satyajeet.hospital.exception.ConflictException;
import com.satyajeet.hospital.repository.UserRepository;
import com.satyajeet.hospital.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service @RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest req) {
        if (userRepository.existsByUsername(req.getUsername()))
            throw new ConflictException("Username already exists");
        if (userRepository.existsByEmail(req.getEmail()))
            throw new ConflictException("Email already registered");
        User user = User.builder()
                .username(req.getUsername()).email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .role(req.getRole() != null ? req.getRole() : User.Role.STAFF)
                .build();
        userRepository.save(user);
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
        return AuthResponse.builder().token(token).username(user.getUsername())
                .email(user.getEmail()).role(user.getRole().name()).build();
    }

    public AuthResponse login(AuthRequest req) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
        User user = userRepository.findByUsername(req.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
        return AuthResponse.builder().token(token).username(user.getUsername())
                .email(user.getEmail()).role(user.getRole().name()).build();
    }
}
