package com.javhlahm.acbmin_autoalmacen.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.javhlahm.acbmin_autoalmacen.entity.Item;

@Repository
public interface ItemRepo extends JpaRepository<Item, Long> {

    Optional<Item> findBySerie(String serie);

    void deleteBySerie(String serie);

}
