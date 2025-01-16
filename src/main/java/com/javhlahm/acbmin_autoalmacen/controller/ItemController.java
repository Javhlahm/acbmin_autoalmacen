package com.javhlahm.acbmin_autoalmacen.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.javhlahm.acbmin_autoalmacen.entity.Item;
import com.javhlahm.acbmin_autoalmacen.service.ItemServicio;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/tallerautomotriz")
@CrossOrigin(origins = "*")
public class ItemController {
    @Autowired
    ItemServicio itemServicio;

    @GetMapping("/almacen")
    public ResponseEntity<List<Item>> obtenerItems() {
        List<Item> lista = itemServicio.obtenerItems();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/almacen/{serie}")
    public ResponseEntity<Optional<Item>> obtenerItemSerie(@PathVariable("serie") String serie) {
        Optional<Item> item = itemServicio.obtenerItemSerie(serie);
        return item.isPresent() ? ResponseEntity.ok(item) : ResponseEntity.noContent().build();
    }

    @PostMapping("/almacen")
    public ResponseEntity<Optional<Item>> guardarItem(@RequestBody Item item) {
        Optional<Item> item2 = Optional.of(itemServicio.guardarActualizarItem(item));
        return item2.isPresent() ? ResponseEntity.ok(item2) : ResponseEntity.noContent().build();
    }

    @PutMapping("/almacen")
    public ResponseEntity<Optional<Item>> actualizarItem(@RequestBody Item item) {
        Item item2;
        item2 = item.clone();
        item2.setSerie(null);
        itemServicio.guardarActualizarItem(item2);
        Optional<Item> itemopt = Optional.of(itemServicio.guardarActualizarItem(item));
        return itemopt.isPresent() ? ResponseEntity.ok(itemopt) : ResponseEntity.noContent().build();
    }

    @DeleteMapping("/almacen/{serie}")
    public ResponseEntity<Void> eliminarItem(@PathVariable String serie) {
        itemServicio.eliminarItem(serie);
        return ResponseEntity.ok().build();
    }

}
