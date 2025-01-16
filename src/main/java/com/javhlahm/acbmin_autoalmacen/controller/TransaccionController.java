package com.javhlahm.acbmin_autoalmacen.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.javhlahm.acbmin_autoalmacen.entity.Transaccion;
import com.javhlahm.acbmin_autoalmacen.service.TransaccionServicio;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/tallerautomotriz/almacen")
@CrossOrigin(origins = "*")
public class TransaccionController {

    @Autowired
    TransaccionServicio transaccionServicio;

    @GetMapping("/transact")
    public ResponseEntity<List<Transaccion>> obtenerTranacciones() {
        List<Transaccion> lista = transaccionServicio.obtenerTransacciones();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/transact/{id}")
    public ResponseEntity<Optional<Transaccion>> obtenerTransaccionID(@PathVariable("id") Long id) {
        Optional<Transaccion> transaccion = transaccionServicio.obtenerTransaccionID(id);
        return transaccion.isPresent() ? ResponseEntity.ok(transaccion) : ResponseEntity.noContent().build();
    }

    @PostMapping("/transact/salida")
    public ResponseEntity<Optional<Transaccion>> guardarTransaccionSalida(@RequestBody Transaccion transaccion) {
        Optional<Transaccion> transaccion2 = Optional.of(transaccionServicio.guardarTransaccionSalida(transaccion));
        return transaccion2.isPresent() ? ResponseEntity.ok(transaccion2) : ResponseEntity.noContent().build();
    }

    @PostMapping("/transact/entrada")
    public ResponseEntity<Optional<Transaccion>> guardarTransaccionEntrada(@RequestBody Transaccion transaccion) {
        Optional<Transaccion> transaccion2 = Optional.of(transaccionServicio.guardarTransaccionIngreso(transaccion));
        return transaccion2.isPresent() ? ResponseEntity.ok(transaccion2) : ResponseEntity.noContent().build();
    }

    @DeleteMapping("/transact/{id}")
    public ResponseEntity<Void> eliminarTransaccion(@PathVariable Long id) {
        transaccionServicio.eliminarTransaccion(id);
        return ResponseEntity.ok().build();
    }
}
