package com.javhlahm.acbmin_autoalmacen.repository;

import com.javhlahm.acbmin_autoalmacen.entity.Resguardo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ResguardoRepo extends JpaRepository<Resguardo, Integer> {


}
