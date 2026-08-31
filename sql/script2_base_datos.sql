CREATE DATABASE IF NOT EXISTS sistema_academico;
USE sistema_academico;

CREATE TABLE IF NOT EXISTS estudiante (
    codigo VARCHAR(20) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    correo VARCHAR(100) UNIQUE NOT NULL,
    fecha_ingreso DATE NOT NULL,
    fecha_nacimiento DATE NOT NULL,
    estado BOOLEAN DEFAULT TRUE,
    carrera VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS materia (
    codigo VARCHAR(20) NOT NULL,
    grupo VARCHAR(10) NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    creditos INT CHECK (creditos BETWEEN 3 AND 4),
    estado BOOLEAN DEFAULT TRUE,
    PRIMARY KEY (codigo, grupo)
);

CREATE TABLE IF NOT EXISTS inscripcion (
    id INT AUTO_INCREMENT PRIMARY KEY,
    codigo_estudiante VARCHAR(20) NOT NULL,
    codigo_materia VARCHAR(20) NOT NULL,
    grupo_materia VARCHAR(10) NOT NULL,
    fecha_inscripcion DATE NOT NULL,
    estado BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (codigo_estudiante) REFERENCES estudiante(codigo),
    FOREIGN KEY (codigo_materia, grupo_materia) REFERENCES materia(codigo, grupo)
);