package com.javhlahm.acbmin_autoalmacen.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javhlahm.acbmin_autoalmacen.entity.Item;
import com.javhlahm.acbmin_autoalmacen.repository.ItemRepo;

@Service
public class ItemServicio {
    @Autowired
    ItemRepo itemRepo;

    public List<Item> obtenerItems() {
        return itemRepo.findAll();
    }

    public Optional<Item> obtenerItemSerie(String serie) {
        return itemRepo.findBySerie(serie);
    }

    public Item guardarActualizarItem(Item item) {
        return itemRepo.save(item);
    }

    public void eliminarItem(String serie) {
        itemRepo.deleteBySerie(serie);
    }

}
