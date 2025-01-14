package com.javhlahm.acbmin_autoalmacen.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
    public List<Transaccion> obtenerTranacciones() {
        System.out.println("ingreso al controller");
        return transaccionServicio.obtenerTransacciones();
    }

    @GetMapping("/transact/{id}")
    public Optional<Transaccion> obtenerTransaccionID(@PathVariable("id") Long id) {
        return transaccionServicio.obtenerTransaccionID(id);
    }

    @PostMapping("/transact/salida")
    public Transaccion guardarTransaccionSalida(@RequestBody Transaccion transaccion) {
        return transaccionServicio.guardarTransaccionSalida(transaccion);
    }

    @PostMapping("/transact/entrada")
    public Transaccion guardarTransaccionEntrada(@RequestBody Transaccion transaccion) {
        return transaccionServicio.guardarTransaccionIngreso(transaccion);
    }

    @DeleteMapping("/transact/{id}")
    public void eliminarTransaccion(@PathVariable Long id) {
        transaccionServicio.eliminarTransaccion(id);
    }
}
