
INSERT IGNORE INTO persona (rut, primer_nombre, segundo_nombre, apellido_paterno, apellido_materno, email, telefono) VALUES 
('11111111-1', 'Alejandro', 'Andrés', 'Sanz', 'García', 'alejandro.sanz@rednorte.cl', '+56911111111'),
('12345678-9', 'Pedro', 'José', 'Pascal', 'Balmaceda', 'pedro.pascal@correo.com', '+56912345678'),
('9876543-2', 'Norma', 'Monserrat', 'Bustamante', 'Laferte', 'mon.laferte@correo.com', '+56922223333'),
('11223344-5', 'Alexis', 'Alejandro', 'Sánchez', 'Sánchez', 'alexis.sanchez@correo.com', '+56944445555'),
('19283746-K', 'Francisca', 'Javiera', 'Valenzuela', 'Méndez', 'fran.valenzuela@correo.com', '+56966667777');

INSERT IGNORE INTO paciente (rut_paciente, antecedentes_medicos, contacto_emergencia_nombre, contacto_emergencia_parentesco, contacto_emergencia_telefono) VALUES 
('12345678-9', 'Alergia a la penicilina', 'María Carmen', 'Esposa', '+56987654321'),
('9876543-2', 'Hipertensión arterial controlada', 'Juan Perez', 'Esposo', '+56988889999'),
('11223344-5', 'Lesión de tobillo recurrente', 'Mayte Rodriguez', 'Contacto', '+56955556666'),
('19283746-K', 'Sin antecedentes médicos relevantes', 'Carlos Valenzuela', 'Padre', '+56933334444');