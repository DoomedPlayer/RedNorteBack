package com.rednorte.security.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.rednorte.security.dto.AuthResponse;
import com.rednorte.security.dto.LoginRequest;
import com.rednorte.security.dto.RegisterRequest;
import com.rednorte.security.entity.Rol;
import com.rednorte.security.entity.Usuario;
import com.rednorte.security.repository.UsuarioRepository;
import com.rednorte.security.services.JwtService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticación", description = "Endpoints para inicio de sesión y registro de usuarios en el sistema")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate = new RestTemplate();

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Autentica a un usuario y genera un token JWT.")
    @ApiResponse(responseCode = "200", description = "Autenticación exitosa, retorna el token")
    @ApiResponse(responseCode = "403", description = "Credenciales inválidas")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getRut(), request.getPassword())
        );

        Usuario usuario = usuarioRepository.findByRutPersona(request.getRut())
                .orElseThrow(); 

        String token = jwtService.generateToken(usuario);

        return ResponseEntity.ok(new AuthResponse(token, usuario.getRutPersona()));
    }
    @PostMapping("/register")
    @Operation(summary = "Registrar usuario", description = "Crea un nuevo usuario con rol PACIENTE y sincroniza el perfil con Patient Portal.")
    @ApiResponse(responseCode = "200", description = "Usuario registrado exitosamente")
    @ApiResponse(responseCode = "400", description = "El RUT ya se encuentra registrado")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {

        if (usuarioRepository.findByRutPersona(request.getRut()).isPresent()) {
            return ResponseEntity.badRequest().body("El RUT ya se encuentra registrado en el sistema de acceso.");
        }

        // 2. Guardar credenciales en db_auth
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setRutPersona(request.getRut());
        nuevoUsuario.setEmail(request.getCorreo());
        nuevoUsuario.setPassword(passwordEncoder.encode(request.getPassword()));
        nuevoUsuario.setRol(Rol.PACIENTE); // Usando tu Enum
        
        usuarioRepository.save(nuevoUsuario);

        try {
            String portalUrl = "http://patient-portal:8084/api/v1/portal/pacientes/registro-perfil";
            restTemplate.postForEntity(portalUrl, request, String.class);
        } catch (Exception e) {

            System.err.println("Advertencia: No se pudo crear el perfil en patient-portal: " + e.getMessage());
        }

        String token = jwtService.generateToken(nuevoUsuario);
        return ResponseEntity.ok(new AuthResponse(token, nuevoUsuario.getRutPersona()));
    
    }
}