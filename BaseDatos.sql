-- ============================
-- Script para la base de datos `clientes_personas`
-- ============================

-- Crear la tabla persona
CREATE TABLE IF NOT EXISTS persona (
    id SERIAL PRIMARY KEY, -- Clave primaria
    nombre VARCHAR(100) NOT NULL,
    genero VARCHAR(10),
    edad INT,
    identificacion VARCHAR(20) UNIQUE NOT NULL, -- Identificación única
    direccion VARCHAR(255),
    telefono VARCHAR(15)
);

-- Crear la tabla cliente que extiende de persona
CREATE TABLE IF NOT EXISTS cliente (
    cliente_id SERIAL PRIMARY KEY, -- Clave primaria única para cliente
    persona_id INT REFERENCES persona(id), -- Relación con persona
    contraseña VARCHAR(100) NOT NULL,
    estado BOOLEAN NOT NULL,
    UNIQUE (persona_id) -- Asegura que cada cliente está vinculado a una única persona
);

-- Índices para mejorar las consultas en la base `clientes_personas`
CREATE INDEX IF NOT EXISTS idx_persona_identificacion ON persona (identificacion); -- Índice en la identificación
CREATE INDEX IF NOT EXISTS idx_cliente_persona_id ON cliente (persona_id); -- Índice en la relación con persona

-- ============================
-- Script para la base de datos `cuentas_movimientos`
-- ============================

-- Crear la tabla cuenta
CREATE TABLE IF NOT EXISTS cuenta (
    numero_cuenta SERIAL PRIMARY KEY, -- Clave primaria única para número de cuenta
    tipo_cuenta VARCHAR(20) NOT NULL,
    saldo_inicial DECIMAL(10, 2) NOT NULL,
    estado BOOLEAN NOT NULL,
    cliente_id INT NOT NULL -- Relación con cliente (pero sin clave foránea)
);

-- Crear la tabla movimiento
CREATE TABLE IF NOT EXISTS movimiento (
    id SERIAL PRIMARY KEY, -- Clave primaria única para movimientos
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Fecha de movimiento
    tipo_movimiento VARCHAR(20) NOT NULL,
    valor DECIMAL(10, 2) NOT NULL,
    saldo DECIMAL(10, 2) NOT NULL, -- Saldo después del movimiento
    numero_cuenta INT REFERENCES cuenta(numero_cuenta) -- Relación con cuenta
);

-- Índices para optimizar consultas en la base `cuentas_movimientos`
CREATE INDEX IF NOT EXISTS idx_cuenta_cliente_id ON cuenta (cliente_id); -- Índice para consultas rápidas por cliente
CREATE INDEX IF NOT EXISTS idx_movimiento_fecha ON movimiento (fecha); -- Índice para consultas rápidas por fecha
CREATE INDEX IF NOT EXISTS idx_movimiento_cuenta ON movimiento (numero_cuenta); -- Índice para consultas rápidas por cuenta

