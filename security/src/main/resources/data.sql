-- Contraseña para todos: password123
-- Hash BCrypt: $2a$10$8.UnVuG9AHBQAJTXwXyqbe2Y4S6Z8Jg8eJ9f8vQ5X4A/7q9qK8U2G

INSERT IGNORE INTO usuario (email, password, rol, rut_persona) VALUES
('alejandro.sanz@rednorte.cl', '$2a$12$5AKPZWFRyfXTpKETguee6.0RVxXZ5zE3aGR/8k72Sdap/edI2422a', 'DOCTOR', '11111111-1'),
('camila.rojas@rednorte.cl', '$2a$12$5AKPZWFRyfXTpKETguee6.0RVxXZ5zE3aGR/8k72Sdap/edI2422a', 'DOCTOR', '22222222-2'),
('luis.torres@rednorte.cl', '$2a$12$5AKPZWFRyfXTpKETguee6.0RVxXZ5zE3aGR/8k72Sdap/edI2422a', 'DOCTOR', '33333333-3'),
('andrea.gomez@rednorte.cl', '$2a$12$5AKPZWFRyfXTpKETguee6.0RVxXZ5zE3aGR/8k72Sdap/edI2422a', 'DOCTOR', '44444444-4'),
('juan.soto@rednorte.cl', '$2a$12$5AKPZWFRyfXTpKETguee6.0RVxXZ5zE3aGR/8k72Sdap/edI2422a', 'DOCTOR', '55555555-5'),

-- Pacientes
('pedro.pascal@correo.com', '$2a$12$5AKPZWFRyfXTpKETguee6.0RVxXZ5zE3aGR/8k72Sdap/edI2422a', 'PACIENTE', '12345678-9'),
('mon.laferte@correo.com', '$2a$12$5AKPZWFRyfXTpKETguee6.0RVxXZ5zE3aGR/8k72Sdap/edI2422a', 'PACIENTE', '9876543-2'),
('alexis.sanchez@correo.com', '$2a$12$5AKPZWFRyfXTpKETguee6.0RVxXZ5zE3aGR/8k72Sdap/edI2422a', 'PACIENTE', '11223344-5'),
('fran.valenzuela@correo.com', '$2a$12$5AKPZWFRyfXTpKETguee6.0RVxXZ5zE3aGR/8k72Sdap/edI2422a', 'PACIENTE', '19283746-K');