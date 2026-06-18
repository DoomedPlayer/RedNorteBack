package com.rednorte.security.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import com.rednorte.security.dto.AuthResponse;
import com.rednorte.security.dto.LoginRequest;
import com.rednorte.security.entity.Usuario;
import com.rednorte.security.repository.UsuarioRepository;
import com.rednorte.security.services.JwtService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getRut(), request.getPassword())
        );

        Usuario usuario = usuarioRepository.findByRutPersona(request.getRut())
                .orElseThrow(); 

        String token = jwtService.generateToken(usuario);

        return ResponseEntity.ok(new AuthResponse(token));
    }
}