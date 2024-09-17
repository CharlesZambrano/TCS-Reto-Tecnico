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
