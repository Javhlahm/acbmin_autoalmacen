package com.javhlahm.acbmin_autoalmacen.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.javhlahm.acbmin_autoalmacen.entity.Item;

@Repository
public interface ItemRepo extends JpaRepository<Item,Long> {
    
}
