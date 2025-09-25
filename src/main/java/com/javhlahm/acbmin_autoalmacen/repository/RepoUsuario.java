package com.javhlahm.acbmin_autoalmacen.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.javhlahm.acbmin_autoalmacen.entity.UsuariosAcbmin;

@Repository
public interface RepoUsuario extends JpaRepository<UsuariosAcbmin, Long> {
    Optional<UsuariosAcbmin> findByEmail(String email);

}
