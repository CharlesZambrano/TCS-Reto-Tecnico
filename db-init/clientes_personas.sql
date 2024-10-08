CREATE TABLE IF NOT EXISTS persona (
    persona_id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    genero VARCHAR(3) CHECK (genero IN ('MAS', 'FEM', 'OTR')),
    edad INT,
    identificacion VARCHAR(20) UNIQUE NOT NULL,
    direccion VARCHAR(255),
    telefono VARCHAR(15)
);

COMMENT ON TABLE persona IS 'Tabla que almacena los datos de una persona, incluyendo su género, identificación y datos de contacto';

COMMENT ON COLUMN persona.persona_id IS 'Clave primaria única para cada persona';
COMMENT ON COLUMN persona.nombre IS 'Nombre completo de la persona';
COMMENT ON COLUMN persona.genero IS 'Género de la persona: MAS = Masculino, FEM = Femenino, OTR = Otro';
COMMENT ON COLUMN persona.edad IS 'Edad de la persona';
COMMENT ON COLUMN persona.identificacion IS 'Identificación única de la persona';
COMMENT ON COLUMN persona.direccion IS 'Dirección de la residencia de la persona';
COMMENT ON COLUMN persona.telefono IS 'Número de teléfono de contacto';

CREATE TABLE IF NOT EXISTS cliente (
    cliente_id SERIAL PRIMARY KEY,
    persona_id INT REFERENCES persona(persona_id),
    contraseña VARCHAR(100) NOT NULL,
    estado BOOLEAN NOT NULL,
    UNIQUE (persona_id)
);

COMMENT ON TABLE cliente IS 'Tabla que almacena los datos de clientes vinculados a personas registradas en la tabla persona';

COMMENT ON COLUMN cliente.cliente_id IS 'Clave primaria única para cada cliente';
COMMENT ON COLUMN cliente.persona_id IS 'Referencia a la clave primaria de la tabla persona';
COMMENT ON COLUMN cliente.contraseña IS 'Contraseña de acceso del cliente';
COMMENT ON COLUMN cliente.estado IS 'Estado del cliente: activo o inactivo';

CREATE INDEX IF NOT EXISTS idx_persona_identificacion ON persona (identificacion);
CREATE INDEX IF NOT EXISTS idx_cliente_persona_id ON cliente (persona_id);



-- Inserción de datos en la tabla 'persona'
INSERT INTO persona (nombre, genero, edad, identificacion, direccion, telefono) 
VALUES 
('Juan Pérez', 'MAS', 30, '010123456789', 'Calle Falsa 123', '0912345678'),
('María Gómez', 'FEM', 25, '020987654321', 'Avenida Siempre Viva 742', '0987654321'),
('Alex Castro', 'OTR', 35, '030112233445', 'Calle Principal 456', '0923456789');

-- Inserción de datos en la tabla 'cliente'
INSERT INTO cliente (persona_id, contraseña, estado) 
VALUES 
(1, 'pass1234', true),
(2, 'pass5678', true),
(3, 'pass9876', true);
