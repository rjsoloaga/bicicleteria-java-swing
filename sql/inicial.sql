CREATE DATABASE IF NOT EXISTS taller_bicicletas_parcial CHARACTER SET utf8mb4;
USE taller_bicicletas_parcial;
CREATE TABLE IF NOT EXISTS reparacion (
  idReparacion INT AUTO_INCREMENT PRIMARY KEY,
  codigo VARCHAR(20) NOT NULL UNIQUE,
  descripcion VARCHAR(100) NOT NULL,
  bicicleta VARCHAR(80) NOT NULL,
  costo INT NOT NULL,
  cliente VARCHAR(80) NOT NULL,
  fecha_ingreso DATE NOT NULL,
  estado VARCHAR(40) NOT NULL
);
CREATE TABLE IF NOT EXISTS rol (
  id_rol INT AUTO_INCREMENT PRIMARY KEY,
  nombre VARCHAR(30) NOT NULL UNIQUE
);
CREATE TABLE IF NOT EXISTS usuario (
  id_usuario INT AUTO_INCREMENT PRIMARY KEY,
  nombre_usuario VARCHAR(40) NOT NULL UNIQUE,
  clave_hash VARCHAR(64) NOT NULL,
  activo BOOLEAN NOT NULL DEFAULT TRUE,
  id_rol INT NOT NULL,
  CONSTRAINT fk_usuario_rol FOREIGN KEY (id_rol) REFERENCES rol(id_rol)
);
INSERT INTO rol(id_rol,nombre) VALUES(1,'ADMINISTRADOR'),(2,'COMUN')
ON DUPLICATE KEY UPDATE nombre=VALUES(nombre);
INSERT INTO usuario(id_usuario,nombre_usuario,clave_hash,activo,id_rol)
VALUES(1,'admin',SHA2('admin123',256),TRUE,1),(2,'comun',SHA2('comun123',256),TRUE,2)
ON DUPLICATE KEY UPDATE clave_hash=VALUES(clave_hash),activo=VALUES(activo),id_rol=VALUES(id_rol);
INSERT INTO reparacion(codigo,descripcion,bicicleta,costo,cliente,fecha_ingreso,estado)
VALUES('REP-01','Cambio de cadena','Mountain bike',18500,'Luis','2026-10-01','Pendiente'),
      ('REP-02','Ajuste de frenos','Urbana',6500,'María','2026-10-02','Finalizado')
ON DUPLICATE KEY UPDATE descripcion=VALUES(descripcion),bicicleta=VALUES(bicicleta),costo=VALUES(costo),cliente=VALUES(cliente),fecha_ingreso=VALUES(fecha_ingreso),estado=VALUES(estado);
