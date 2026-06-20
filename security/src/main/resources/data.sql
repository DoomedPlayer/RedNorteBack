-- Contraseña para todos: password123
-- Hash BCrypt: $2a$10$8.UnVuG9AHBQAJTXwXyqbe2Y4S6Z8Jg8eJ9f8vQ5X4A/7q9qK8U2G

INSERT IGNORE INTO usuario (email, password, rol, rut_persona) VALUES
('alejandro.sanz@rednorte.cl', '$2a$10$8.UnVuG9AHBQAJTXwXyqbe2Y4S6Z8Jg8eJ9f8vQ5X4A/7q9qK8U2G', 'DOCTOR', '11111111-1'),
('pedro.pascal@correo.com', '$2a$10$8.UnVuG9AHBQAJTXwXyqbe2Y4S6Z8Jg8eJ9f8vQ5X4A/7q9qK8U2G', 'PACIENTE', '12345678-9'),
('mon.laferte@correo.com', '$2a$10$8.UnVuG9AHBQAJTXwXyqbe2Y4S6Z8Jg8eJ9f8vQ5X4A/7q9qK8U2G', 'PACIENTE', '9876543-2'),
('alexis.sanchez@correo.com', '$2a$10$8.UnVuG9AHBQAJTXwXyqbe2Y4S6Z8Jg8eJ9f8vQ5X4A/7q9qK8U2G', 'PACIENTE', '11223344-5'),
('fran.valenzuela@correo.com', '$2a$10$8.UnVuG9AHBQAJTXwXyqbe2Y4S6Z8Jg8eJ9f8vQ5X4A/7q9qK8U2G', 'PACIENTE', '19283746-K');