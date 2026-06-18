package com.rednorte.security.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.rednorte.security.entity.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    Optional<Usuario> findByRutPersona(String rutPersona);
}
