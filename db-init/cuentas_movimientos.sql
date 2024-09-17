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
