package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.Inventario;
import com.letrasypapeles.backend.repository.InventarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InventarioService {

    @Autowired
    private InventarioRepository inventarioRepository;

    public List<Inventario> obtenerTodos() {
        return inventarioRepository.findAll();
    }

    public Optional<Inventario> obtenerPorId(Long id) {
        return inventarioRepository.findById(id);
    }

    public Inventario guardar(Inventario inventario) {
        return inventarioRepository.save(inventario);
    }

    public void eliminar(Long id) {
        inventarioRepository.deleteById(id);
    }

    public List<Inventario> obtenerPorProductoId(Long productoId) {
        return inventarioRepository.findByProductoId(productoId);
    }

    public List<Inventario> obtenerPorSucursalId(Long sucursalId) {
        return inventarioRepository.findBySucursalId(sucursalId);
    }

    public List<Inventario> obtenerInventarioBajoUmbral(Integer umbral) {
        return inventarioRepository.findByCantidadLessThan(umbral);
    }
}
