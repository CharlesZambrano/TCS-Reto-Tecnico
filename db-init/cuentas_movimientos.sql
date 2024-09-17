-- Crear la tabla cuenta
CREATE TABLE IF NOT EXISTS cuenta (
    cuenta_id SERIAL PRIMARY KEY, -- Identificador numérico incremental
    unique_id VARCHAR(16) NOT NULL, -- Identificador único generado externamente
    numero_cuenta VARCHAR(20) UNIQUE NOT NULL, -- Número de cuenta único pero no PK
    tipo_cuenta VARCHAR(20) NOT NULL,
    saldo_inicial DECIMAL(17, 2) NOT NULL,
    estado BOOLEAN NOT NULL,
    cliente_id INT NOT NULL, -- Relación con cliente (sin clave foránea explícita)
    CONSTRAINT uq_unique_id UNIQUE (unique_id) -- Restricción de unicidad para el unique_id
);

-- Crear la tabla movimiento
CREATE TABLE IF NOT EXISTS movimiento (
    movimiento_id SERIAL PRIMARY KEY, -- Identificador numérico incremental
    unique_id VARCHAR(16) NOT NULL, -- Identificador único generado externamente
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- Fecha de movimiento
    tipo_movimiento VARCHAR(20) NOT NULL,
    valor DECIMAL(17, 2) NOT NULL,
    saldo DECIMAL(17, 2) NOT NULL, -- Saldo después del movimiento
    numero_cuenta VARCHAR(20) NOT NULL, -- Relación con cuenta (a través de numero_cuenta)
    CONSTRAINT fk_cuenta_numero FOREIGN KEY (numero_cuenta) REFERENCES cuenta(numero_cuenta),
    CONSTRAINT uq_mov_unique_id UNIQUE (unique_id) -- Restricción de unicidad para el unique_id
);

-- Índices para optimizar consultas
CREATE INDEX IF NOT EXISTS idx_cuenta_cliente_id ON cuenta (cliente_id); -- Índice para consultas rápidas por cliente
CREATE INDEX IF NOT EXISTS idx_movimiento_fecha ON movimiento (fecha); -- Índice para consultas rápidas por fecha
CREATE INDEX IF NOT EXISTS idx_movimiento_cuenta ON movimiento (numero_cuenta); -- Índice para consultas rápidas por cuenta
