-- Recordar: Urgencia=1, Cirugía=2, Procedimiento=3, Consulta=5
INSERT IGNORE INTO registro_espera (rut_paciente, id_especialidad, tipo_atencion, nivel_prioridad, fecha_ingreso, estado, ges_auge) VALUES 
('12345678-9', 1, 'Cirugía', 2, '2026-04-14', 'En espera', true),
('9876543-2', 3, 'Procedimiento', 3, '2026-05-01', 'En espera', false),
('11223344-5', 2, 'Urgencia', 1, '2026-06-10', 'En espera', true),
('19283746-K', 1, 'Consulta', 5, '2026-02-28', 'Pendiente de asignación médica', false),
('12345678-9', 4, 'Procedimiento', 3, '2026-06-15', 'En espera', false);