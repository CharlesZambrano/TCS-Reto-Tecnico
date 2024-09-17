CREATE TABLE IF NOT EXISTS cuenta (
    cuenta_id SERIAL PRIMARY KEY,
    unique_id VARCHAR(16) NOT NULL,
    numero_cuenta VARCHAR(20) UNIQUE NOT NULL,
    tipo_cuenta VARCHAR(3) NOT NULL CHECK (tipo_cuenta IN ('COR', 'AHO')),
    saldo_inicial DECIMAL(17, 2) NOT NULL,
    estado BOOLEAN NOT NULL,
    cliente_id INT NOT NULL,
    CONSTRAINT uq_unique_id UNIQUE (unique_id)
);

COMMENT ON TABLE cuenta IS 'Tabla que almacena información sobre las cuentas bancarias de los clientes, incluyendo tipo de cuenta y saldo inicial';
COMMENT ON COLUMN cuenta.cuenta_id IS 'Clave primaria única e incremental para la cuenta';
COMMENT ON COLUMN cuenta.unique_id IS 'Identificador único generado externamente para la cuenta';
COMMENT ON COLUMN cuenta.numero_cuenta IS 'Número de cuenta bancario único';
COMMENT ON COLUMN cuenta.tipo_cuenta IS 'Tipo de cuenta: COR = Corriente, AHO = Ahorro';
COMMENT ON COLUMN cuenta.saldo_inicial IS 'Saldo inicial de la cuenta';
COMMENT ON COLUMN cuenta.estado IS 'Estado de la cuenta: activa o inactiva';
COMMENT ON COLUMN cuenta.cliente_id IS 'Relación con el cliente asociado a esta cuenta';

CREATE TABLE IF NOT EXISTS movimiento (
    movimiento_id SERIAL PRIMARY KEY,
    unique_id VARCHAR(16) NOT NULL,
    fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    tipo VARCHAR(3) NOT NULL CHECK (tipo IN ('RET', 'DEP')),
    valor DECIMAL(17, 2) NOT NULL,
    saldo_inicial DECIMAL(17, 2) NOT NULL,
    saldo_disponible DECIMAL(17, 2) NOT NULL,
    numero_cuenta VARCHAR(20) NOT NULL,
    CONSTRAINT fk_cuenta_numero FOREIGN KEY (numero_cuenta) REFERENCES cuenta(numero_cuenta),
    CONSTRAINT uq_mov_unique_id UNIQUE (unique_id)
);

COMMENT ON TABLE movimiento IS 'Tabla que almacena los movimientos bancarios realizados en las cuentas, incluyendo retiros y depósitos';
COMMENT ON COLUMN movimiento.movimiento_id IS 'Clave primaria única e incremental para el movimiento';
COMMENT ON COLUMN movimiento.unique_id IS 'Identificador único generado externamente para el movimiento';
COMMENT ON COLUMN movimiento.fecha IS 'Fecha en la que se realizó el movimiento';
COMMENT ON COLUMN movimiento.tipo IS 'Tipo de movimiento: RET = Retiro, DEP = Depósito';
COMMENT ON COLUMN movimiento.valor IS 'Valor del movimiento realizado';
COMMENT ON COLUMN movimiento.saldo_inicial IS 'Saldo en la cuenta antes de realizar el movimiento';
COMMENT ON COLUMN movimiento.saldo_disponible IS 'Saldo en la cuenta después de realizar el movimiento';
COMMENT ON COLUMN movimiento.numero_cuenta IS 'Número de cuenta en el cual se realizó el movimiento';

CREATE INDEX IF NOT EXISTS idx_cuenta_cliente_id ON cuenta (cliente_id);
CREATE INDEX IF NOT EXISTS idx_movimiento_fecha ON movimiento (fecha);
CREATE INDEX IF NOT EXISTS idx_movimiento_cuenta ON movimiento (numero_cuenta);