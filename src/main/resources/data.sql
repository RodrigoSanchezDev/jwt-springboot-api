-- Crear la tabla 'roles' (solo si no existe todavía)
CREATE TABLE IF NOT EXISTS roles (
  nombre VARCHAR(50) PRIMARY KEY
);

-- Insertar los cuatro roles necesarios (H2-compatible syntax)
INSERT INTO roles(nombre) SELECT 'CLIENTE' WHERE NOT EXISTS (SELECT 1 FROM roles WHERE nombre = 'CLIENTE');
INSERT INTO roles(nombre) SELECT 'EMPLEADO' WHERE NOT EXISTS (SELECT 1 FROM roles WHERE nombre = 'EMPLEADO');
INSERT INTO roles(nombre) SELECT 'GERENTE' WHERE NOT EXISTS (SELECT 1 FROM roles WHERE nombre = 'GERENTE');
INSERT INTO roles(nombre) SELECT 'ADMIN' WHERE NOT EXISTS (SELECT 1 FROM roles WHERE nombre = 'ADMIN');
