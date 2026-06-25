INSERT IGNORE INTO lista_espera (id_lista, rut_paciente, id_especialidad, tipo_atencion, nivel_prioridad, fecha_ingreso, ges_auge) VALUES 
(1, '12345678-9', 1, 'Cirugía', 2, '2026-04-14', true),
(2, '9876543-2', 3, 'Procedimiento', 3, '2026-05-01', false),
(3, '11223344-5', 2, 'Urgencia', 1, '2026-06-10', true),
(4, '19283746-K', 1, 'Consulta', 5, '2026-02-28', false),
(5, '12345678-9', 4, 'Procedimiento', 3, '2026-06-15', false),
(6, '19283746-K', 5, 'Consulta', 5, '2026-06-25', false);


INSERT IGNORE INTO registro_paciente (rut_paciente, estado, fecha_registro, prioridad, ges_auge) VALUES 
('12345678-9', 'EN_ESPERA', '2026-06-15', 'Nivel 2 - Cirugía', true),
('9876543-2', 'EN_ESPERA', '2026-05-01', 'Nivel 3 - Procedimiento', false),
('11223344-5', 'EN_ESPERA', '2026-06-10', 'Nivel 1 - Urgencia (Máxima)', true),
('19283746-K', 'EN_ESPERA', '2026-02-28', 'Nivel 5 - Consulta General', false),
('19283746-K', 'EN_ESPERA', '2026-06-25', 'Nivel 5 - Consulta General', false);