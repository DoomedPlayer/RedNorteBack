
INSERT IGNORE INTO persona (rut, primer_nombre, segundo_nombre, apellido_paterno, apellido_materno, email, telefono) VALUES 
('11111111-1', 'Alejandro', 'Andrés', 'Sanz', 'García', 'alejandro.sanz@rednorte.cl', '+56911111111'),
('12345678-9', 'Pedro', 'José', 'Pascal', 'Balmaceda', 'pedro.pascal@correo.com', '+56912345678'),
('9876543-2', 'Norma', 'Monserrat', 'Bustamante', 'Laferte', 'mon.laferte@correo.com', '+56922223333'),
('11223344-5', 'Alexis', 'Alejandro', 'Sánchez', 'Sánchez', 'alexis.sanchez@correo.com', '+56944445555'),
('19283746-K', 'Francisca', 'Javiera', 'Valenzuela', 'Méndez', 'fran.valenzuela@correo.com', '+56966667777'),
('22222222-2', 'Camila', 'Ignacia', 'Rojas', 'Méndez', 'camila.rojas@rednorte.cl', '+56922222222'),
('33333333-3', 'Luis', 'Alberto', 'Torres', 'Salinas', 'luis.torres@rednorte.cl', '+56933333333'),
('44444444-4', 'Andrea', 'Paz', 'Gomez', 'Vargas', 'andrea.gomez@rednorte.cl', '+56944444444'),
('55555555-5', 'Juan', 'Carlos', 'Soto', 'Pérez', 'juan.soto@rednorte.cl', '+56955555555');

INSERT IGNORE INTO paciente (rut_paciente, antecedentes_medicos, contacto_emergencia_nombre, contacto_emergencia_parentesco, contacto_emergencia_telefono) VALUES 
('12345678-9', 'Alergia a la penicilina', 'María Carmen', 'Esposa', '+56987654321'),
('9876543-2', 'Hipertensión arterial controlada', 'Juan Perez', 'Esposo', '+56988889999'),
('11223344-5', 'Lesión de tobillo recurrente', 'Mayte Rodriguez', 'Contacto', '+56955556666'),
('19283746-K', 'Sin antecedentes médicos relevantes', 'Carlos Valenzuela', 'Padre', '+56933334444');


INSERT IGNORE INTO medico (rut_medico, especialidad, id_especialidad) VALUES 
('11111111-1', 'Cardiología', 2),
('22222222-2', 'Medicina General', 4),
('33333333-3', 'Traumatología', 3),
('44444444-4', 'Ginecología', 5),
('55555555-5', 'Kinesiología', 6);