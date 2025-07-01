package com.letrasypapeles.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class BackendApplicationTest {

    @Test
    void contextLoads() {
        // Test para verificar que el contexto de Spring se carga correctamente
        // Si este test pasa, significa que la aplicación se inicializa sin errores
    }

    @Test
    void mainMethodStartsApplication() {
        // Test para cubrir el método main
        try (MockedStatic<SpringApplication> mockedSpringApplication = Mockito.mockStatic(SpringApplication.class)) {
            // Configurar el mock
            mockedSpringApplication.when(() -> SpringApplication.run(BackendApplication.class, new String[]{}))
                    .thenReturn(null);
            
            // Ejecutar el método main
            BackendApplication.main(new String[]{});
            
            // Verificar que SpringApplication.run fue llamado con los parámetros correctos
            mockedSpringApplication.verify(() -> SpringApplication.run(BackendApplication.class, new String[]{}));
        }
    }
}
