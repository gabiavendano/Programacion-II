CREATE DATABASE IF NOT EXISTS inmobiliaria;
USE inmobiliaria;

-- Tabla de usuarios del sistema
CREATE TABLE IF NOT EXISTS usuarios (
    idUsuario INT AUTO_INCREMENT PRIMARY KEY,
    usuario VARCHAR(100) NOT NULL,
    contraseña VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL
);

-- Insertar usuario por defecto
INSERT INTO usuarios (usuario, contraseña, email) VALUES
('admin', 'admin123', 'admin@inmobiliaria.com');

-- Clientes
CREATE TABLE IF NOT EXISTS clientes (
    idCliente INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    dni VARCHAR(20) NOT NULL UNIQUE,
    telefono VARCHAR(20),
    email VARCHAR(100)
);

-- Propiedades
CREATE TABLE IF NOT EXISTS propiedades (
    idPropiedad INT AUTO_INCREMENT PRIMARY KEY,
    direccion VARCHAR(255) NOT NULL,
    tipo ENUM('Casa', 'Departamento', 'Terreno', 'Local', 'Otro') NOT NULL,
    metros_lote DECIMAL(10,2),
    metros_cubiertos DECIMAL(10,2),
    antiguedad INT,
    dormitorios INT,
    banos INT,
    pileta BOOLEAN DEFAULT FALSE,
    cochera BOOLEAN DEFAULT FALSE,
    expensas DECIMAL(10,2) DEFAULT 0,
    descripcion TEXT,
    precio DECIMAL(15,2) NOT NULL,
    moneda ENUM('USD', 'ARS', 'EUR', 'BRL') NOT NULL,
    otros TEXT,
    foto VARCHAR(255),
    estado ENUM('Disponible', 'Reservada', 'Vendida', 'Alquilada', 'Inactiva') DEFAULT 'Disponible',
    idUsuario INT NOT NULL DEFAULT 1,
    FOREIGN KEY (idUsuario) REFERENCES usuarios(idUsuario)
);

-- Contratos (ACTUALIZADA con campo activo)
CREATE TABLE IF NOT EXISTS contratos (
    idContrato INT AUTO_INCREMENT PRIMARY KEY,
    tipo ENUM('Venta', 'Alquiler', 'Permuta') NOT NULL,
    fechaInicio DATE NOT NULL,
    fechaFin DATE,
    monto DECIMAL(15,2) NOT NULL,
    idCliente INT NOT NULL,
    idPropiedad INT NOT NULL,
    porcentaje_aumento DECIMAL(5,2) DEFAULT 0,
    frecuencia_aumento INT DEFAULT 12,
    indice_aumento ENUM('IPC', 'Contractual', 'Otro') DEFAULT 'IPC',
    servicios_incluidos TEXT,
    tipo_alquiler ENUM('Vivienda Familiar', 'Comercio', 'Otro') DEFAULT 'Vivienda Familiar',
    activo BOOLEAN DEFAULT TRUE, -- NUEVO CAMPO PARA GESTIÓN DE ESTADO
    FOREIGN KEY (idCliente) REFERENCES clientes(idCliente),
    FOREIGN KEY (idPropiedad) REFERENCES propiedades(idPropiedad)
);

-- Recibos
CREATE TABLE IF NOT EXISTS recibos (
    idRecibo INT AUTO_INCREMENT PRIMARY KEY,
    idContrato INT NOT NULL,
    fecha_emision DATE NOT NULL,
    mes_referencia VARCHAR(20) NOT NULL,
    monto_alquiler DECIMAL(15,2) NOT NULL,
    servicios DECIMAL(15,2) DEFAULT 0,
    total DECIMAL(15,2) NOT NULL,
    fecha_vencimiento DATE NOT NULL,
    pagado BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (idContrato) REFERENCES contratos(idContrato) ON DELETE CASCADE
);

-- Reservas
CREATE TABLE IF NOT EXISTS reservas (
    idReserva INT AUTO_INCREMENT PRIMARY KEY,
    fechaReserva DATE NOT NULL,
    estado ENUM('Pendiente', 'Confirmada', 'Cancelada') DEFAULT 'Pendiente',
    idCliente INT NOT NULL,
    idPropiedad INT NOT NULL,
    FOREIGN KEY (idCliente) REFERENCES clientes(idCliente),
    FOREIGN KEY (idPropiedad) REFERENCES propiedades(idPropiedad)
);

-- Tabla de roles de cliente
CREATE TABLE IF NOT EXISTS tipos_cliente (
    idTipo INT AUTO_INCREMENT PRIMARY KEY,
    tipo ENUM('Propietario', 'Inquilino', 'Comprador') NOT NULL UNIQUE
);

-- Relación muchos a muchos: un cliente puede ser varios tipos
CREATE TABLE IF NOT EXISTS roles_cliente (
    idRolCliente INT AUTO_INCREMENT PRIMARY KEY,
    idCliente INT NOT NULL,
    idTipo INT NOT NULL,
    FOREIGN KEY (idCliente) REFERENCES clientes(idCliente) ON DELETE CASCADE,
    FOREIGN KEY (idTipo) REFERENCES tipos_cliente(idTipo) ON DELETE CASCADE,
    UNIQUE (idCliente, idTipo)
);

-- Insertar tipos de cliente
INSERT INTO tipos_cliente (tipo) VALUES
('Propietario'),
('Inquilino'),
('Comprador');

-- Insertar datos de ejemplo para pruebas
INSERT INTO clientes (nombre, apellido, dni, telefono, email) VALUES
('Juan', 'Pérez', '12345678', '1122334455', 'juan.perez@email.com'),
('María', 'Gómez', '87654321', '1155667788', 'maria.gomez@email.com'),
('Carlos', 'López', '11223344', '1199887766', 'carlos.lopez@email.com');

INSERT INTO propiedades (direccion, tipo, metros_lote, metros_cubiertos, antiguedad, dormitorios, banos, pileta, cochera, expensas, descripcion, precio, moneda, otros, estado) VALUES
('Av. Siempre Viva 742', 'Casa', 300.00, 150.00, 10, 3, 2, TRUE, TRUE, 0.00, 'Casa familiar con amplio jardín', 150000.00, 'USD', 'Cochera para 2 autos', 'Disponible'),
('Calle Falsa 123', 'Departamento', 0.00, 80.00, 5, 2, 1, FALSE, TRUE, 5000.00, 'Departamento luminoso', 80000.00, 'USD', 'Amoblado', 'Disponible'),
('Av. Libertador 1500', 'Local', 200.00, 120.00, 2, 0, 1, FALSE, FALSE, 8000.00, 'Local comercial en zona céntrica', 120000.00, 'USD', 'Apto comercial', 'Disponible');

-- Insertar algunos contratos de ejemplo
INSERT INTO contratos (tipo, fechaInicio, fechaFin, monto, idCliente, idPropiedad, porcentaje_aumento, frecuencia_aumento, indice_aumento, servicios_incluidos, tipo_alquiler) VALUES
('Alquiler', '2024-01-01', '2024-12-31', 50000.00, 1, 1, 20.00, 6, 'IPC', 'Luz, agua, gas', 'Vivienda Familiar'),
('Venta', '2024-02-01', NULL, 150000.00, 2, 2, 0.00, 12, 'IPC', NULL, 'Vivienda Familiar');

-- Insertar recibos de ejemplo
INSERT INTO recibos (idContrato, fecha_emision, mes_referencia, monto_alquiler, servicios, total, fecha_vencimiento, pagado) VALUES
(1, '2024-01-05', 'Enero 2024', 50000.00, 5000.00, 55000.00, '2024-01-10', TRUE),
(1, '2024-02-05', 'Febrero 2024', 50000.00, 5500.00, 55500.00, '2024-02-10', FALSE);

-- Crear índices para mejorar el rendimiento
CREATE INDEX idx_propiedades_estado ON propiedades(estado);
CREATE INDEX idx_contratos_activo ON contratos(activo);
CREATE INDEX idx_contratos_cliente ON contratos(idCliente);
CREATE INDEX idx_contratos_propiedad ON contratos(idPropiedad);
CREATE INDEX idx_recibos_contrato ON recibos(idContrato);

-- Vistas útiles para reportes
CREATE VIEW vista_contratos_activos AS
SELECT
    c.idContrato,
    c.tipo,
    c.fechaInicio,
    c.fechaFin,
    c.monto,
    cl.nombre AS cliente_nombre,
    cl.apellido AS cliente_apellido,
    p.direccion AS propiedad_direccion,
    p.tipo AS propiedad_tipo
FROM contratos c
JOIN clientes cl ON c.idCliente = cl.idCliente
JOIN propiedades p ON c.idPropiedad = p.idPropiedad
WHERE c.activo = TRUE;

CREATE VIEW vista_propiedades_disponibles AS
SELECT
    p.idPropiedad,
    p.direccion,
    p.tipo,
    p.precio,
    p.moneda,
    p.estado,
    p.metros_cubiertos,
    p.dormitorios,
    p.banos
FROM propiedades p
WHERE p.estado = 'Disponible';

-- Procedimientos almacenados útiles
DELIMITER //

CREATE PROCEDURE sp_obtener_contratos_por_cliente(IN cliente_id INT)
BEGIN
    SELECT
        c.*,
        p.direccion,
        p.tipo AS propiedad_tipo
    FROM contratos c
    JOIN propiedades p ON c.idPropiedad = p.idPropiedad
    WHERE c.idCliente = cliente_id AND c.activo = TRUE;
END //

CREATE PROCEDURE sp_obtener_recibos_pendientes(IN contrato_id INT)
BEGIN
    SELECT *
    FROM recibos
    WHERE idContrato = contrato_id AND pagado = FALSE
    ORDER BY fecha_vencimiento;
END //

DELIMITER ;

-- Triggers para mantener la integridad de datos
DELIMITER //

CREATE TRIGGER before_contrato_insert
BEFORE INSERT ON contratos
FOR EACH ROW
BEGIN
    -- Verificar que la propiedad esté disponible para alquiler/venta
    DECLARE prop_estado VARCHAR(20);
    SELECT estado INTO prop_estado FROM propiedades WHERE idPropiedad = NEW.idPropiedad;

    IF prop_estado != 'Disponible' AND NEW.tipo IN ('Alquiler', 'Venta') THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'La propiedad no está disponible';
    END IF;
END //

CREATE TRIGGER after_contrato_insert
AFTER INSERT ON contratos
FOR EACH ROW
BEGIN
    -- Actualizar el estado de la propiedad cuando se crea un contrato
    IF NEW.tipo = 'Alquiler' THEN
        UPDATE propiedades SET estado = 'Alquilada' WHERE idPropiedad = NEW.idPropiedad;
    ELSEIF NEW.tipo = 'Venta' THEN
        UPDATE propiedades SET estado = 'Vendida' WHERE idPropiedad = NEW.idPropiedad;
    END IF;
END //

CREATE TRIGGER after_contrato_update
AFTER UPDATE ON contratos
FOR EACH ROW
BEGIN
    -- Si se desactiva un contrato, liberar la propiedad
    IF OLD.activo = TRUE AND NEW.activo = FALSE THEN
        IF OLD.tipo = 'Alquiler' THEN
            UPDATE propiedades SET estado = 'Disponible' WHERE idPropiedad = OLD.idPropiedad;
        END IF;
    END IF;
END //

DELIMITER ;

-- Mostrar información de las tablas creadas
SELECT 'Base de datos inmobiliaria creada exitosamente' AS Mensaje;
SELECT COUNT(*) AS Total_Tipos_Cliente FROM tipos_cliente;
SELECT COUNT(*) AS Total_Usuarios FROM usuarios;
SELECT COUNT(*) AS Total_Clientes FROM clientes;
SELECT COUNT(*) AS Total_Propiedades FROM propiedades;