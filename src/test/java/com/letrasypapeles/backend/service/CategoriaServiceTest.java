package com.letrasypapeles.backend.service;

import com.letrasypapeles.backend.entity.Categoria;
import com.letrasypapeles.backend.repository.CategoriaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testObtenerTodas() {
        List<Categoria> categorias = Arrays.asList(
                Categoria.builder().id(1L).nombre("Categoría 1").build(),
                Categoria.builder().id(2L).nombre("Categoría 2").build()
        );

        when(categoriaRepository.findAll()).thenReturn(categorias);

        List<Categoria> resultado = categoriaService.obtenerTodas();

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        verify(categoriaRepository).findAll();
    }

    @Test
    void testObtenerPorId() {
        Categoria categoria = Categoria.builder()
                .id(1L)
                .nombre("Categoría 1")
                .build();

        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));

        Optional<Categoria> resultado = categoriaService.obtenerPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals("Categoría 1", resultado.get().getNombre());
        verify(categoriaRepository).findById(1L);
    }

    @Test
    void testGuardar() {
        Categoria categoria = Categoria.builder()
                .nombre("Nueva Categoría")
                .build();

        Categoria categoriaGuardada = Categoria.builder()
                .id(1L)
                .nombre("Nueva Categoría")
                .build();

        when(categoriaRepository.save(categoria)).thenReturn(categoriaGuardada);

        Categoria resultado = categoriaService.guardar(categoria);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals("Nueva Categoría", resultado.getNombre());
        verify(categoriaRepository).save(categoria);
    }

    @Test
    void testEliminar() {
        doNothing().when(categoriaRepository).deleteById(1L);

        categoriaService.eliminar(1L);

        verify(categoriaRepository).deleteById(1L);
    }
}
