-- ============================================================================
-- SISTEMA DE GESTIÓN DE BICICLETERÍA - SCRIPT COMPLETO DE BASE DE DATOS
-- Materia: Programación III - Programación Orientada a Objetos
-- Motor: MySQL 8.0+
-- ============================================================================

CREATE DATABASE IF NOT EXISTS bicicleteria_db
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE bicicleteria_db;

-- ----------------------------------------------------------------------------
-- 1. LIMPIEZA PREVENTIVA (Para ejecución reproducible)
-- ----------------------------------------------------------------------------
SET FOREIGN_KEY_CHECKS = 0;
DROP PROCEDURE IF EXISTS sp_registrar_producto;
DROP TABLE IF EXISTS producto;
DROP TABLE IF EXISTS categoria_producto;
DROP TABLE IF EXISTS tipo_producto;
DROP TABLE IF EXISTS usuario;
DROP TABLE IF EXISTS persona;
DROP TABLE IF EXISTS rol;
SET FOREIGN_KEY_CHECKS = 1;

-- ----------------------------------------------------------------------------
-- 2. CREACIÓN DE TABLAS NORMALIZADAS (3FN) CON RESTRICCIONES DE INTEGRIDAD
-- ----------------------------------------------------------------------------

CREATE TABLE rol (
    id_rol INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(30) NOT NULL UNIQUE
) ENGINE=InnoDB;

CREATE TABLE persona (
    id_persona INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(80) NOT NULL,
    apellido VARCHAR(80) NOT NULL,
    dni VARCHAR(20) NOT NULL UNIQUE,
    telefono VARCHAR(30),
    email VARCHAR(120)
) ENGINE=InnoDB;

CREATE TABLE usuario (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre_usuario VARCHAR(50) NOT NULL UNIQUE,
    clave_hash VARCHAR(64) NOT NULL,
    activo TINYINT(1) NOT NULL DEFAULT 1,
    id_rol INT NOT NULL,
    id_persona INT NOT NULL UNIQUE,
    CONSTRAINT fk_usuario_rol FOREIGN KEY (id_rol)
        REFERENCES rol(id_rol)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_usuario_persona FOREIGN KEY (id_persona)
        REFERENCES persona(id_persona)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE TABLE tipo_producto (
    id_tipo INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(80) NOT NULL UNIQUE
) ENGINE=InnoDB;

CREATE TABLE categoria_producto (
    id_categoria INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(80) NOT NULL,
    id_tipo INT NOT NULL,
    CONSTRAINT uq_categoria_por_tipo UNIQUE (nombre, id_tipo),
    CONSTRAINT fk_categoria_tipo FOREIGN KEY (id_tipo)
        REFERENCES tipo_producto(id_tipo)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE TABLE producto (
    id_producto INT AUTO_INCREMENT PRIMARY KEY,
    codigo VARCHAR(40) NOT NULL UNIQUE,
    nombre VARCHAR(120) NOT NULL,
    precio DECIMAL(12, 2) NOT NULL,
    stock_actual INT NOT NULL DEFAULT 0,
    activo TINYINT(1) NOT NULL DEFAULT 1,
    id_categoria INT NOT NULL,
    CONSTRAINT chk_producto_precio CHECK (precio >= 0),
    CONSTRAINT chk_producto_stock CHECK (stock_actual >= 0),
    CONSTRAINT fk_producto_categoria FOREIGN KEY (id_categoria)
        REFERENCES categoria_producto(id_categoria)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE=InnoDB;

-- ----------------------------------------------------------------------------
-- 3. PROCEDIMIENTOS ALMACENADOS
-- ----------------------------------------------------------------------------
DELIMITER //

CREATE PROCEDURE sp_registrar_producto(
    IN p_codigo VARCHAR(40),
    IN p_nombre VARCHAR(120),
    IN p_precio DECIMAL(12, 2),
    IN p_stock INT,
    IN p_id_categoria INT
)
BEGIN
    IF p_codigo IS NULL OR TRIM(p_codigo) = '' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El código del producto es obligatorio';
    END IF;

    IF p_nombre IS NULL OR TRIM(p_nombre) = '' THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El nombre del producto es obligatorio';
    END IF;

    IF p_precio < 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El precio no puede ser negativo';
    END IF;

    IF p_stock < 0 THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'El stock no puede ser negativo';
    END IF;

    INSERT INTO producto (codigo, nombre, precio, stock_actual, activo, id_categoria)
    VALUES (TRIM(p_codigo), TRIM(p_nombre), p_precio, p_stock, 1, p_id_categoria);
END //

DELIMITER ;

-- ----------------------------------------------------------------------------
-- 4. DATOS DE PRUEBA FICTICIOS
-- ----------------------------------------------------------------------------

-- Roles del sistema
INSERT INTO rol (id_rol, nombre) VALUES
(1, 'ADMINISTRADOR'),
(2, 'VENDEDOR');

-- Personas
INSERT INTO persona (id_persona, nombre, apellido, dni, telefono, email) VALUES
(1, 'Javier', 'Soloaga', '30111222', '3624112233', 'jsoloaga@bicicleteria.com'),
(2, 'Macarena', 'Rico', '36444555', '3624223344', 'mrico@bicicleteria.com'),
(3, 'Juan Pablo', 'Katona', '35777888', '3624334455', 'jpkatona@bicicleteria.com'),
(4, 'Carlos', 'Gómez', '33999000', '3624445566', 'cgomez@bicicleteria.com'),
(5, 'Lucía', 'Fernández', '38222111', '3624556677', 'lfernandez@bicicleteria.com');

-- Usuarios (Contraseñas encriptadas con SHA-256)
-- Credenciales:
--   admin    / admin123    (ADMINISTRADOR activo)
--   maca     / maca123     (ADMINISTRADOR activo)
--   kato     / kato123     (ADMINISTRADOR activo)
--   comun    / comun123    (VENDEDOR activo)
--   inactivo / inactivo123 (VENDEDOR desactivado para probar AC-08)
INSERT INTO usuario (id_usuario, nombre_usuario, clave_hash, activo, id_rol, id_persona) VALUES
(1, 'admin', SHA2('admin123', 256), 1, 1, 1),
(2, 'maca', SHA2('maca123', 256), 1, 1, 2),
(3, 'kato', SHA2('kato123', 256), 1, 1, 3),
(4, 'comun', SHA2('comun123', 256), 1, 2, 4),
(5, 'inactivo', SHA2('inactivo123', 256), 0, 2, 5);

-- Tipos de Producto
INSERT INTO tipo_producto (id_tipo, nombre) VALUES
(1, 'Bicicletas'),
(2, 'Repuestos y Transmisión'),
(3, 'Cubiertas y Cámaras'),
(4, 'Accesorios e Indumentaria'),
(5, 'Herramientas y Mantenimiento');

-- Categorías de Producto
INSERT INTO categoria_producto (id_categoria, nombre, id_tipo) VALUES
(1, 'Mountain Bike (MTB)', 1),
(2, 'Ruta y Gravel', 1),
(3, 'Urbanas y Paseo', 1),
(4, 'Infantiles', 1),
(5, 'Cadenas, Piñones y Platos', 2),
(6, 'Frenos y Discos', 2),
(7, 'Cambios y Descarriladores', 2),
(8, 'Cubiertas Rodado 29', 3),
(9, 'Cámaras y Parches', 3),
(10, 'Cascos y Protección', 4),
(11, 'Luces y Seguridad', 4),
(12, 'Infladores y Llaves', 5),
(13, 'Lubricantes y Limpieza', 5);

-- Productos (Incluye activos, inactivos y stock bajo <= 5 para reportes)
INSERT INTO producto (codigo, nombre, precio, stock_actual, activo, id_categoria) VALUES
('BIC-MTB-01', 'Bicicleta SLP 50 Pro R29 24v Shimano', 485000.00, 6, 1, 1),
('BIC-MTB-02', 'Bicicleta Venzo Primal EX R29 Deore', 820000.00, 3, 1, 1),
('BIC-RUT-01', 'Bicicleta Ruta Venzo Phoenix Aluminio', 950000.00, 2, 1, 2),
('BIC-URB-01', 'Bicicleta Paseo Dama R26 Canasto', 310000.00, 8, 1, 3),
('BIC-INF-01', 'Bicicleta Infantil R16 con Rueditas', 185000.00, 10, 1, 4),
('BIC-OLD-01', 'Bicicleta MTB R26 Modelo 2022 (Discontinuada)', 290000.00, 0, 0, 1),
('REP-CAD-01', 'Cadena Shimano HG40 6/7/8 Velocidades', 28500.00, 18, 1, 5),
('REP-CAD-02', 'Cadena KMC X9 9 Velocidades', 36000.00, 4, 1, 5),
('REP-PIN-01', 'Piñón Cassette Shimano 8v 11-32T', 44000.00, 9, 1, 5),
('REP-FRE-01', 'Juego Frenos Hidráulicos Shimano MT200', 115000.00, 5, 1, 6),
('REP-FRE-02', 'Pastillas de Freno Disco Resina B05S', 14500.00, 25, 1, 6),
('REP-CAM-01', 'Pata de Cambio Shimano Tourney TX800', 39000.00, 7, 1, 7),
('REP-CAM-02', 'Descarrilador Delantero Shimano Altus', 31000.00, 2, 0, 7),
('CUB-29-01', 'Cubierta Maxxis Ikon 29x2.20 Kevlar', 78000.00, 12, 1, 8),
('CUB-29-02', 'Cubierta Imperial Cord R29x2.10', 29500.00, 16, 1, 8),
('CAM-29-01', 'Cámara Imperial Cord R29 Válvula Auto', 9800.00, 35, 1, 9),
('CAM-26-01', 'Cámara R26 Válvula Presta', 8900.00, 3, 1, 9),
('ACC-CAS-01', 'Casco Venzo Raptor Regulable con Visera', 54000.00, 11, 1, 10),
('ACC-CAS-02', 'Casco Infantil Diseño Deportivo', 32000.00, 7, 1, 10),
('ACC-LUZ-01', 'Kit Luces LED Recargables USB Del/Tras', 18000.00, 20, 1, 11),
('ACC-LIN-01', 'Lingas de Seguridad con Combinación 120cm', 16500.00, 14, 1, 11),
('ACC-OLD-01', 'Caramañola Plástica Modelo Anterior', 5500.00, 4, 0, 11),
('HER-INF-01', 'Inflador de Pie con Manómetro Metal', 42000.00, 6, 1, 12),
('HER-MUL-01', 'Multiherramienta Plegable 16 Funciones', 24000.00, 9, 1, 12),
('LUB-CAD-01', 'Lubricante Seco Teflón Cadena 120ml', 13500.00, 22, 1, 13),
('LUB-DES-01', 'Desengrasante Biodegradable Transmisión 500ml', 17000.00, 4, 1, 13);
