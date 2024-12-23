package com.javhlahm.acbmin_autoalmacen.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.javhlahm.acbmin_autoalmacen.entity.Item;
import com.javhlahm.acbmin_autoalmacen.service.ItemServicio;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/tallerautomotriz")
public class ItemController {
    @Autowired
    ItemServicio itemServicio;

    @GetMapping("/almacen")
    public List<Item> obtenerItems() {
        return itemServicio.obtenerItems();
    }

    @GetMapping("/almacen/{id}")
    public Optional<Item> obtenerItemID(@PathVariable("id") Long id) {
        return itemServicio.obtenerItemID(id);
    }

    @PostMapping("/almacen")
    public Item guardarItem(@RequestBody Item item) {
        return itemServicio.guardarActualizarItem(item);
    }

    @PutMapping("/almacen")
    public Item actualizarItem(@RequestBody Item item) {
        Item item2;
        item2 = item.clone();
        item2.setSerie(null);
        itemServicio.guardarActualizarItem(item2);
        return itemServicio.guardarActualizarItem(item);
    }

    @DeleteMapping("/almacen/{id}")
    public void postMethodName(@PathVariable Long id) {
        itemServicio.eliminarItem(id);
    }

}
