package com.javhlahm.acbmin_autoalmacen.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.javhlahm.acbmin_autoalmacen.entity.UsuariosAcbmin;
import com.javhlahm.acbmin_autoalmacen.security.Jwt;
import com.javhlahm.acbmin_autoalmacen.service.ServiceUsuariosAcbmin;

@RestController
@RequestMapping("/usuarios")
@CrossOrigin(origins = "*")
public class ControllerUsuariosAcbmin {

    @Autowired
    private ServiceUsuariosAcbmin usuarioService;

    @Autowired
    private Jwt jwt;

    @GetMapping
    public List<UsuariosAcbmin> getAllUsuarios() {
        return usuarioService.getAllUsuarios();
    }

    @GetMapping("/{email}")
    public ResponseEntity<UsuariosAcbmin> getUsuarioById(@PathVariable String email) {
        return usuarioService.getUsuarioByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<UsuariosAcbmin> createUsuario(@RequestBody UsuariosAcbmin usuario) {
        return ResponseEntity.ok(usuarioService.createUsuario(usuario));
    }

    @PutMapping("/{email}")
    public ResponseEntity<UsuariosAcbmin> updateUsuario(@PathVariable String email,
            @RequestBody UsuariosAcbmin usuario) {
        return ResponseEntity.ok(usuarioService.updateUsuario(email, usuario));
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void> deleteUsuario(@PathVariable String email) {
        usuarioService.deleteUsuario(email);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Map<String, String> credenciales) {
        String email = credenciales.get("email");
        String contrasena = credenciales.get("contrasena");
        Optional<UsuariosAcbmin> usuarioAutorizado = usuarioService.login(email, contrasena);
        if (usuarioAutorizado.isPresent()) {
            UsuariosAcbmin usuario = usuarioAutorizado.get();
            String token = jwt.generarToken(usuario.getEmail());
            return ResponseEntity.ok(token);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas");
        }
    }
}
