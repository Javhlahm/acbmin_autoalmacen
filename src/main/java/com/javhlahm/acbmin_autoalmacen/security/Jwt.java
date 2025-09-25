package com.javhlahm.acbmin_autoalmacen.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
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
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Collections;
import java.util.Date;

@Component
public class Jwt extends OncePerRequestFilter {

    // Clave secreta para firmar los tokens JWT
    @Value("${jwt.secret.key}")
    private String LLAVE_SECRETA;

    // función para obtener la clave de firmas
    private Key getLlaveParaFirmar() {
        byte[] keyBytes = LLAVE_SECRETA.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // funcion para generar un token
    public String generarToken(String email) {
        long tiempoDeExpiracion = 1000 * 60 * 60 * 8; // 8 horas
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + tiempoDeExpiracion))
                .signWith(getLlaveParaFirmar())
                .compact();
    }

    // funcion para validar un token
    public boolean validarToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getLlaveParaFirmar())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // funcion para extraer el email del token
    public String obtenerEmailDesdeToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getLlaveParaFirmar())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    // filtro para validar el token en cada solicitud
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String encabezadoDeAutorizacion = request.getHeader("Authorization");

        String email = null;
        String tokenJwt = null;

        if (encabezadoDeAutorizacion != null && encabezadoDeAutorizacion.startsWith("Bearer ")) {
            tokenJwt = encabezadoDeAutorizacion.substring(7);
            try {
                email = obtenerEmailDesdeToken(tokenJwt);
            } catch (Exception e) {
                System.err.println("Error al obtener email del token: " + e.getMessage());
            }

        }

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            if (validarToken(tokenJwt)) {
                UsernamePasswordAuthenticationToken autenticacion = new UsernamePasswordAuthenticationToken(
                        email, null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

                SecurityContextHolder.getContext().setAuthentication(autenticacion);
            }
        }
        filterChain.doFilter(request, response);
    }
}