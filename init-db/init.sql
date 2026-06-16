-- Crear base de datos para Portal Paciente si no existe
CREATE DATABASE IF NOT EXISTS db_patient_portal CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Crear base de datos para Lista de Espera si no existe
CREATE DATABASE IF NOT EXISTS db_waitlist CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Crear base de datos para Auto-Reasignación si no existe
CREATE DATABASE IF NOT EXISTS db_reasignacion CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;