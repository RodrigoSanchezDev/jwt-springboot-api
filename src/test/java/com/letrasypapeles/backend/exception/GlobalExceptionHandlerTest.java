package com.letrasypapeles.backend.exception;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    @Test
    void testHandleValidationExceptions() {
        // Crear mocks
        MethodArgumentNotValidException ex = Mockito.mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        
        // Crear errores de campo
        FieldError fieldError1 = new FieldError("objectName", "email", "El email es obligatorio");
        FieldError fieldError2 = new FieldError("objectName", "nombre", "El nombre es obligatorio");
        
        // Configurar los mocks
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(Arrays.asList(fieldError1, fieldError2));
        
        // Ejecutar el método
        ResponseEntity<Map<String, String>> response = exceptionHandler.handleValidationExceptions(ex);
        
        // Verificar el resultado
        assertNotNull(response);
        assertEquals(400, response.getStatusCode().value());
        
        Map<String, String> errors = response.getBody();
        assertNotNull(errors);
        assertEquals(2, errors.size());
        assertEquals("El email es obligatorio", errors.get("email"));
        assertEquals("El nombre es obligatorio", errors.get("nombre"));
    }

    @Test
    void testHandleValidationExceptions_EmptyErrors() {
        // Crear mocks
        MethodArgumentNotValidException ex = Mockito.mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        
        // Configurar los mocks sin errores
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(Arrays.asList());
        
        // Ejecutar el método
        ResponseEntity<Map<String, String>> response = exceptionHandler.handleValidationExceptions(ex);
        
        // Verificar el resultado
        assertNotNull(response);
        assertEquals(400, response.getStatusCode().value());
        
        Map<String, String> errors = response.getBody();
        assertNotNull(errors);
        assertEquals(0, errors.size());
    }

    @Test
    void testHandleValidationExceptions_SingleError() {
        // Crear mocks
        MethodArgumentNotValidException ex = Mockito.mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = Mockito.mock(BindingResult.class);
        
        // Crear un solo error de campo
        FieldError fieldError = new FieldError("objectName", "password", "La contraseña es obligatoria");
        
        // Configurar los mocks
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(Arrays.asList(fieldError));
        
        // Ejecutar el método
        ResponseEntity<Map<String, String>> response = exceptionHandler.handleValidationExceptions(ex);
        
        // Verificar el resultado
        assertNotNull(response);
        assertEquals(400, response.getStatusCode().value());
        
        Map<String, String> errors = response.getBody();
        assertNotNull(errors);
        assertEquals(1, errors.size());
        assertEquals("La contraseña es obligatoria", errors.get("password"));
    }
}
