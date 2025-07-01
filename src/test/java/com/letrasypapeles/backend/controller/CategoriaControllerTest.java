package com.letrasypapeles.backend.controller;

import com.letrasypapeles.backend.entity.Categoria;
import com.letrasypapeles.backend.service.CategoriaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoriaControllerTest {

    @Mock
    private CategoriaService categoriaService;

    @InjectMocks
    private CategoriaController categoriaController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testObtenerTodasCategorias() {
        List<Categoria> categorias = Arrays.asList(
                new Categoria(1L, "Categoria 1"),
                new Categoria(2L, "Categoria 2")
        );

        when(categoriaService.obtenerTodas()).thenReturn(categorias);

        ResponseEntity<List<Categoria>> response = categoriaController.obtenerTodas();

        assertEquals(200, response.getStatusCode().value());
        List<Categoria> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(2, responseBody.size());
    }

    @Test
    void testObtenerTodasCategoriasVacia() {
        List<Categoria> categorias = new ArrayList<>();
        when(categoriaService.obtenerTodas()).thenReturn(categorias);

        ResponseEntity<List<Categoria>> response = categoriaController.obtenerTodas();

        assertEquals(200, response.getStatusCode().value());
        List<Categoria> responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(0, responseBody.size());
    }

    @Test
    void testObtenerCategoriaPorId() {
        Categoria categoria = new Categoria(1L, "Categoria 1");

        when(categoriaService.obtenerPorId(1L)).thenReturn(Optional.of(categoria));

        ResponseEntity<Categoria> response = categoriaController.obtenerPorId(1L);

        assertEquals(200, response.getStatusCode().value());
        Categoria responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Categoria 1", responseBody.getNombre());
    }

    @Test
    void testObtenerCategoriaPorIdNoEncontrada() {
        when(categoriaService.obtenerPorId(999L)).thenReturn(Optional.empty());

        ResponseEntity<Categoria> response = categoriaController.obtenerPorId(999L);

        assertEquals(404, response.getStatusCode().value());
        assertNull(response.getBody());
    }

    @Test
    void testCrearCategoria() {
        Categoria categoria = new Categoria(null, "Nueva Categoria");
        Categoria categoriaGuardada = new Categoria(1L, "Nueva Categoria");

        when(categoriaService.guardar(any(Categoria.class))).thenReturn(categoriaGuardada);

        ResponseEntity<Categoria> response = categoriaController.crearCategoria(categoria);

        assertEquals(200, response.getStatusCode().value());
        Categoria responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals(1L, responseBody.getId());
        assertEquals("Nueva Categoria", responseBody.getNombre());
    }

    @Test
    void testActualizarCategoria() {
        Categoria categoriaExistente = new Categoria(1L, "Categoria Original");
        Categoria categoriaActualizada = new Categoria(1L, "Categoria Actualizada");

        when(categoriaService.obtenerPorId(1L)).thenReturn(Optional.of(categoriaExistente));
        when(categoriaService.guardar(any(Categoria.class))).thenReturn(categoriaActualizada);

        ResponseEntity<Categoria> response = categoriaController.actualizarCategoria(1L, categoriaActualizada);

        assertEquals(200, response.getStatusCode().value());
        Categoria responseBody = response.getBody();
        assertNotNull(responseBody);
        assertEquals("Categoria Actualizada", responseBody.getNombre());
    }

    @Test
    void testActualizarCategoriaNoEncontrada() {
        Categoria categoria = new Categoria(1L, "Categoria Actualizada");
        when(categoriaService.obtenerPorId(999L)).thenReturn(Optional.empty());

        ResponseEntity<Categoria> response = categoriaController.actualizarCategoria(999L, categoria);

        assertEquals(404, response.getStatusCode().value());
        assertNull(response.getBody());
    }

    @Test
    void testEliminarCategoria() {
        Categoria categoriaExistente = new Categoria(1L, "Categoria a Eliminar");
        when(categoriaService.obtenerPorId(1L)).thenReturn(Optional.of(categoriaExistente));
        doNothing().when(categoriaService).eliminar(1L);

        ResponseEntity<Void> response = categoriaController.eliminarCategoria(1L);

        assertEquals(200, response.getStatusCode().value());
        verify(categoriaService).eliminar(1L);
    }

    @Test
    void testEliminarCategoriaNoEncontrada() {
        when(categoriaService.obtenerPorId(999L)).thenReturn(Optional.empty());

        ResponseEntity<Void> response = categoriaController.eliminarCategoria(999L);

        assertEquals(404, response.getStatusCode().value());
        verify(categoriaService, never()).eliminar(999L);
    }
}
