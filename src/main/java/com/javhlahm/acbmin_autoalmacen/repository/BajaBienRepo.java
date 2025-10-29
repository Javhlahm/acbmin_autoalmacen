package com.javhlahm.acbmin_autoalmacen.repository;

import com.javhlahm.acbmin_autoalmacen.entity.BajaBien;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BajaBienRepo extends JpaRepository<BajaBien, Integer> {

    // Spring Data JPA genera automáticamente los métodos CRUD básicos (findAll,
    // findById, save, deleteById, etc.)

    // Puedes añadir métodos personalizados si los necesitas, por ejemplo:
    // Optional<BajaBien> findByNumeroInventario(String numeroInventario);
    // List<BajaBien> findByEstatus(String estatus);

}