-- Crear la tabla 'roles' (solo si no existe todavía)
CREATE TABLE IF NOT EXISTS roles (
  nombre VARCHAR(50) PRIMARY KEY
);

-- Insertar los cuatro roles necesarios
INSERT INTO roles(nombre) VALUES ('CLIENTE');
INSERT INTO roles(nombre) VALUES ('EMPLEADO');
INSERT INTO roles(nombre) VALUES ('GERENTE');
INSERT INTO roles(nombre) VALUES ('ADMIN');
