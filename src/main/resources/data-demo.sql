-- Datos de demostración para HATEOAS
-- Roles
INSERT INTO roles (nombre) VALUES ('ROLE_USER');
INSERT INTO roles (nombre) VALUES ('ROLE_ADMIN');

-- Categorías
INSERT INTO categorias (nombre) VALUES ('Papelería');
INSERT INTO categorias (nombre) VALUES ('Libros');
INSERT INTO categorias (nombre) VALUES ('Oficina');

-- Proveedores
INSERT INTO proveedores (nombre, contacto) VALUES ('Proveedor Central', 'contacto@central.com');
INSERT INTO proveedores (nombre, contacto) VALUES ('Libros y Más', 'info@librosymas.com');

-- Sucursales
INSERT INTO sucursales (nombre, direccion, region) VALUES ('Sucursal Centro', 'Av. Principal 123', 'Metropolitana');
INSERT INTO sucursales (nombre, direccion, region) VALUES ('Sucursal Norte', 'Calle Norte 456', 'Norte');

-- Productos
INSERT INTO productos (nombre, descripcion, precio, stock, categoria_id, proveedor_id) VALUES 
('Cuaderno A4', 'Cuaderno universitario 100 hojas', 2.50, 50, 1, 1);
INSERT INTO productos (nombre, descripcion, precio, stock, categoria_id, proveedor_id) VALUES 
('Bolígrafo Azul', 'Bolígrafo de tinta azul', 0.75, 200, 1, 1);
INSERT INTO productos (nombre, descripcion, precio, stock, categoria_id, proveedor_id) VALUES 
('Libro Java Programming', 'Guía completa de programación Java', 45.00, 15, 2, 2);

-- Cliente de demostración (contraseña: "demo123")
INSERT INTO clientes (nombre, apellido, email, contrasena, puntos_fidelidad) VALUES 
('Demo', 'Usuario', 'demo@test.com', '$2a$10$N9qo8uLOickgx2ZMRZoMye$2D7N8ypUNBfUOV0AEsAq.oLf0xO6o4y', 100);

-- Asignar rol al cliente
INSERT INTO clientes_roles (cliente_id, role_nombre) VALUES (1, 'ROLE_USER');

-- Inventarios
INSERT INTO inventarios (producto_id, sucursal_id, stock_actual, umbral_minimo, cantidad, umbral) VALUES 
(1, 1, 30, 10, 30, 15);
INSERT INTO inventarios (producto_id, sucursal_id, stock_actual, umbral_minimo, cantidad, umbral) VALUES 
(2, 1, 150, 50, 150, 75);
INSERT INTO inventarios (producto_id, sucursal_id, stock_actual, umbral_minimo, cantidad, umbral) VALUES 
(3, 1, 10, 5, 10, 8);

-- Pedido de ejemplo
INSERT INTO pedidos (cliente_id, estado, fecha) VALUES (1, 'PENDIENTE', CURRENT_TIMESTAMP);

-- Productos del pedido
INSERT INTO pedidos_productos (pedido_id, producto_id) VALUES (1, 1);
INSERT INTO pedidos_productos (pedido_id, producto_id) VALUES (1, 2);

-- Reserva de ejemplo
INSERT INTO reservas (cliente_id, producto_id, cantidad, estado, fecha_reserva) VALUES 
(1, 3, 2, 'ACTIVA', CURRENT_TIMESTAMP);

-- Notificación
INSERT INTO notificaciones (cliente_id, mensaje, fecha, leida) VALUES 
(1, 'Su pedido ha sido procesado exitosamente', CURRENT_TIMESTAMP, false);
