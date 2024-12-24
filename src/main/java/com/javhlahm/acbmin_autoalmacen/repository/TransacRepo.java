package com.javhlahm.acbmin_autoalmacen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.javhlahm.acbmin_autoalmacen.entity.Transaccion;

@Repository
public interface TransacRepo extends JpaRepository<Transaccion, Long> {

}
