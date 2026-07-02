INSERT IGNORE INTO persona (rut, primer_nombre, segundo_nombre, apellido_paterno, apellido_materno, email, telefono, edad) VALUES 
('11111111-1', 'Alejandro', 'Andrés', 'Sanz', 'García', 'alejandro.sanz@rednorte.cl', '+56911111111', 55),
('12345678-9', 'Pedro', 'José', 'Pascal', 'Balmaceda', 'pedro.pascal@correo.com', '+56912345678', 48),
('9876543-2', 'Norma', 'Monserrat', 'Bustamante', 'Laferte', 'mon.laferte@correo.com', '+56922223333', 40),
('11223344-5', 'Alexis', 'Alejandro', 'Sánchez', 'Sánchez', 'alexis.sanchez@correo.com', '+56944445555', 35),
('19283746-K', 'Francisca', 'Javiera', 'Valenzuela', 'Méndez', 'fran.valenzuela@correo.com', '+56966667777', 37),
('22222222-2', 'Camila', 'Ignacia', 'Rojas', 'Méndez', 'camila.rojas@rednorte.cl', '+56922222222', 32),
('33333333-3', 'Luis', 'Alberto', 'Torres', 'Salinas', 'luis.torres@rednorte.cl', '+56933333333', 45),
('44444444-4', 'Andrea', 'Paz', 'Gomez', 'Vargas', 'andrea.gomez@rednorte.cl', '+56944444444', 39),
('55555555-5', 'Juan', 'Carlos', 'Soto', 'Pérez', 'juan.soto@rednorte.cl', '+56955555555', 50);

INSERT IGNORE INTO paciente (rut_paciente, antecedentes_medicos, contacto_emergencia_nombre, contacto_emergencia_parentesco, contacto_emergencia_telefono, prevision, es_ges) VALUES 
('12345678-9', 'Diabetes Tipo 2', 'María Carmen', 'Esposa', '+56987654321', 'ISAPRE', true),
('9876543-2', 'Hipertensión arterial controlada', 'Juan Perez', 'Esposo', '+56988889999', 'FONASA_D', true),
('11223344-5', 'Lesión de tobillo recurrente', 'Mayte Rodriguez', 'Contacto', '+56955556666', 'FONASA_C', true),
('19283746-K', 'Sin antecedentes médicos relevantes', 'Carlos Valenzuela', 'Padre', '+56933334444', 'FONASA_B', false);

INSERT IGNORE INTO medico (rut_medico, especialidad, id_especialidad) VALUES 
('11111111-1', 'Cardiología', 2),
('22222222-2', 'Medicina General', 4),
('33333333-3', 'Traumatología', 3),
('44444444-4', 'Ginecología', 5),
('55555555-5', 'Kinesiología', 6);

INSERT IGNORE INTO documentos (id,rut_paciente, nombre_documento, emisor_y_fecha, url_descarga) VALUES 

(1,'12345678-9', 'Test de Alergias Múltiples', 'Dra. Camila Rojas - 10-01-2026', 'https://storage.rednorte.cl/docs/12345678-9_test_alergia.pdf'),
(2,'12345678-9', 'Receta Médica - Antihistamínicos', 'Dra. Camila Rojas - 15-01-2026', 'https://storage.rednorte.cl/docs/12345678-9_receta.pdf'),

(3,'9876543-2', 'Electrocardiograma de Reposo', 'Dr. Alejandro Sanz - 22-03-2026', 'https://storage.rednorte.cl/docs/9876543-2_electro.pdf'),
(4,'9876543-2', 'Receta - Tratamiento Hipertensión', 'Dr. Alejandro Sanz - 22-03-2026', 'https://storage.rednorte.cl/docs/9876543-2_receta_hip.pdf'),

(5,'11223344-5', 'Orden Resonancia Magnética - Tobillo', 'Dr. Luis Torres - 05-05-2026', 'https://storage.rednorte.cl/docs/11223344-5_orden_rm.pdf'),
(6,'11223344-5', 'Orden de Kinesiología (10 sesiones)', 'Dr. Luis Torres - 08-05-2026', 'https://storage.rednorte.cl/docs/11223344-5_orden_kine.pdf'),
(7,'11223344-5', 'Informe de Alta Kinesiológica', 'Dr. Juan Soto - 15-06-2026', 'https://storage.rednorte.cl/docs/11223344-5_alta_kine.pdf'),

(8,'19283746-K', 'Resultados Exámenes de Sangre (Rutina)', 'Dra. Andrea Gomez - 12-06-2026', 'https://storage.rednorte.cl/docs/19283746-K_examenes.pdf'),
(9,'19283746-K', 'Certificado Médico - Salud Compatible', 'Dra. Andrea Gomez - 15-06-2026', 'https://storage.rednorte.cl/docs/19283746-K_certificado.pdf');