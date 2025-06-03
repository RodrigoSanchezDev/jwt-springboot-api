package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.Proveedor;
import com.letrasypapeles.backend.repository.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProveedorService {

    @Autowired
    private ProveedorRepository proveedorRepository;

    public List<Proveedor> obtenerTodos() {
        return proveedorRepository.findAll();
    }

    public Optional<Proveedor> obtenerPorId(Long id) {
        return proveedorRepository.findById(id);
    }

    public Proveedor guardar(Proveedor proveedor) {
        return proveedorRepository.save(proveedor);
    }

    public void eliminar(Long id) {
        proveedorRepository.deleteById(id);
    }
}
