-- Insertar roles
INSERT INTO roles (name) VALUES
('ROLE_ADMIN'),
('ROLE_PROFESOR'),
('ROLE_ESTUDIANTE');

-- Insertar usuarios
INSERT INTO users (firstname, lastname, username, password, email, enabled, account_locked) VALUES
('Admin', 'User', 'admin', '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KsnmG', 'admin@example.com', true, false),
('Profesor', 'Uno', 'profesor1', '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KsnmG', 'profesor1@example.com', true, false),
('Estudiante', 'Uno', 'estudiante1', '$2a$10$xn3LI/AjqicFYZFruSwve.681477XaVNaUQbr1gioaWPn4t1KsnmG', 'estudiante1@example.com', true, false);

-- Asignar roles a usuarios
INSERT INTO users_roles (user_id, roles_id) VALUES
(1, 1), -- Admin tiene rol ADMIN
(2, 2), -- Profesor1 tiene rol PROFESOR
(3, 3); -- Estudiante1 tiene rol ESTUDIANTE

-- Insertar personas
INSERT INTO personas (nombre, apellido, tel, sexo, user_id) VALUES
('Admin', 'User', '123456789', 'MASCULINO', 1),
('Profesor', 'Uno', '987654321', 'MASCULINO', 2),
('Estudiante', 'Uno', '555555555', 'FEMENINO', 3);

-- Insertar colegio
INSERT INTO colegios (nombre, promedio_colegio) VALUES
('Colegio Ejemplo', 0.0);

-- Insertar profesor
INSERT INTO profesores (id, titulo, departamento, colegio_id) VALUES
(2, 'Licenciado en Educación', 'Matemáticas', 1);

-- Insertar estudiante
INSERT INTO estudiantes (id, card_id, promedio, rank_en_aula, rank_en_colegio, colegio_id) VALUES
(3, 12345, 0.0, 1, 1, 1);

-- Insertar aula
INSERT INTO aulas (grado, modalidad, capacidad_maxima, anio, promedio_clases, colegio_id, rank) VALUES
('PRIMERO', 'PRESENCIAL', 30, 1, 0.0, 1, 1);

-- Insertar clase
INSERT INTO clases (materia, promedio_clase, fecha_clase, hora_inicio, hora_fin, profesor_id, aula_id) VALUES
('Matemáticas', 0.0, '2023-01-01', '2023-01-01 08:00:00', '2023-01-01 09:30:00', 2, 1);

-- Asignar estudiante a clase
INSERT INTO clase_estudiante (clase_id, estudiante_id) VALUES
(1, 3);

-- Insertar examen
INSERT INTO examenes (dificultad, nota, fecha_examen, estudiante_id, clase_id, profesor_id) VALUES
('MEDIA', 0.0, '2023-01-15', 3, 1, 2);

-- Insertar ejercicio
INSERT INTO ejercicios (enunciado, tipo_ejercicio, puntaje_maximo, puntaje_obtenido, dificultad, examen_id) VALUES
('¿Cuánto es 2 + 2?', 'OPCION_MULTIPLE', 10.0, 0.0, 'FACIL', 1);

-- Insertar opciones para el ejercicio
INSERT INTO opciones (texto, es_correcta, ejercicio_id) VALUES
('3', false, 1),
('4', true, 1),
('5', false, 1),
('6', false, 1);