package BinarySeint.BFF.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.Key;
import java.util.List;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Value("${api.security.jwt.secret}")
    private String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("=== NUEVA PETICIÓN EN BFF ===");
        System.out.println("Ruta solicitada: " + request.getRequestURI());
        System.out.println("Método: " + request.getMethod());
        
        String authHeader = request.getHeader("Authorization");
        System.out.println("Cabecera Authorization recibida: " + authHeader);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            try {
                // CORRECCIÓN: Usamos .getBytes() exactamente igual que en el auth-service
                Key key = Keys.hmacShaKeyFor(secretKey.getBytes());
                
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                String rut = claims.getSubject();
                String rol = claims.get("rol", String.class);

                List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(rol));
                UsernamePasswordAuthenticationToken authentication = 
                        new UsernamePasswordAuthenticationToken(rut, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(authentication);
                
            } catch (Exception e) {
                System.err.println("Error procesando el JWT en el BFF: " + e.getMessage());
                e.printStackTrace(); 
                SecurityContextHolder.clearContext();
            }
        }
        filterChain.doFilter(request, response);
    }
}