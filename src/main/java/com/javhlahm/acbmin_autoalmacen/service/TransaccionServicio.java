package com.javhlahm.acbmin_autoalmacen.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.javhlahm.acbmin_autoalmacen.entity.Item;
import com.javhlahm.acbmin_autoalmacen.entity.Transaccion;
import com.javhlahm.acbmin_autoalmacen.repository.TransacRepo;

@Service
public class TransaccionServicio {

    @Autowired
    TransacRepo transacRepo;
    @Autowired
    ItemServicio itemServicio;

    public List<Transaccion> obtenerTransacciones() {
        System.out.println("ingreso al servicio");
        return transacRepo.findAll();
    }

    public Optional<Transaccion> obtenerTransaccionID(long id) {
        return transacRepo.findById(id);
    }

    public Transaccion guardarTransaccionSalida(Transaccion transaccion) {
        Optional<Item> item = itemServicio.obtenerItemID(transaccion.getId_prod());
        if (item.isPresent()) {
            item.get().setCantidad(item.get().getCantidad() - transaccion.getCantidad());
            itemServicio.guardarActualizarItem(item.get());
        }
        return transacRepo.save(transaccion);
    }

    public Transaccion guardarTransaccionIngreso(Transaccion transaccion) {
        Optional<Item> item = itemServicio.obtenerItemID(transaccion.getId_prod());
        if (item.isPresent()) {
            item.get().setCantidad(item.get().getCantidad() + transaccion.getCantidad());
            item.get().setUltMovimiento(transaccion.getFecha());
            itemServicio.guardarActualizarItem(item.get());
        } else {
            Item item2 = new Item();
            item2.setNombre(transaccion.getNombre());
            item2.setCategoria(transaccion.getCategoria());
            item2.setMarca(transaccion.getMarca());
            item2.setModelo(transaccion.getModelo());
            item2.setSerie(transaccion.getSerie());
            item2.setDescripcion(transaccion.getDescripcion());
            item2.setCantidad(transaccion.getCantidad());
            item2.setModeloAuto(transaccion.getModeloAuto());
            item2.setUltMovimiento(transaccion.getFecha());
            item2.setNotas(transaccion.getRequerimientoONotas());
            itemServicio.guardarActualizarItem(item2);
        }
        return transacRepo.save(transaccion);
    }

    public void eliminarTransaccion(Long id) {
        transacRepo.deleteById(id);
    }
}
