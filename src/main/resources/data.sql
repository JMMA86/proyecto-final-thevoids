-- Insertar datos en la tabla Roles
INSERT INTO Roles (nombre_rol) VALUES ('Administrador');
INSERT INTO Roles (nombre_rol) VALUES ('Medico');
INSERT INTO Roles (nombre_rol) VALUES ('Paciente');
INSERT INTO Roles (nombre_rol) VALUES ('Laboratorista');

-- Insertar datos en la tabla Permisos
INSERT INTO Permisos (nombre_permiso) VALUES ('Crear_Cita');
INSERT INTO Permisos (nombre_permiso) VALUES ('Editar_Cita');
INSERT INTO Permisos (nombre_permiso) VALUES ('Ver_Historial');
INSERT INTO Permisos (nombre_permiso) VALUES ('Gestionar_Usuarios');

-- Asignar permisos a roles en la tabla Roles_Permisos
INSERT INTO Roles_Permisos (id_rol, id_permiso) VALUES (1, 1); -- Administrador puede Crear_Cita
INSERT INTO Roles_Permisos (id_rol, id_permiso) VALUES (1, 2); -- Administrador puede Editar_Cita
INSERT INTO Roles_Permisos (id_rol, id_permiso) VALUES (2, 3); -- Medico puede Ver_Historial
INSERT INTO Roles_Permisos (id_rol, id_permiso) VALUES (4, 4); -- Laboratorista puede Gestionar_Usuarios

-- Insertar datos en la tabla Usuarios
INSERT INTO Usuarios (nombre_completo, identificacion, fecha_nacimiento, genero, direccion, telefono, correo_electronico, rol_id, estado, contraseña)
VALUES ('Juan Perez', '123456789', '1980-05-15', 'Masculino', 'Calle 123', '555-1234', 'juan@example.com', 2, 'activo', 'contraseña123');

INSERT INTO Usuarios (nombre_completo, identificacion, fecha_nacimiento, genero, direccion, telefono, correo_electronico, rol_id, estado, contraseña)
VALUES ('Maria Lopez', '987654321', '1990-08-20', 'Femenino', 'Avenida 456', '555-5678', 'maria@example.com', 3, 'activo', 'contraseña456');

INSERT INTO Usuarios (nombre_completo, identificacion, fecha_nacimiento, genero, direccion, telefono, correo_electronico, rol_id, estado, contraseña)
VALUES ('Carlos Ramirez', '456789123', '1975-03-10', 'Masculino', 'Calle 789', '555-9876', 'carlos@example.com', 4, 'activo', 'contraseña789');

-- Insertar datos en la tabla Pacientes
INSERT INTO Pacientes (id_usuario, grupo_sanguineo, alergias, historia_familiar)
VALUES (2, 'O+', 'Polen', 'Diabetes');

-- Insertar datos en la tabla Consultorios
INSERT INTO Consultorios (nombre, direccion, telefono, especialidad, capacidad)
VALUES ('Consultorio A', 'Hospital Central', '555-1111', 'Oncología', 10);

INSERT INTO Consultorios (nombre, direccion, telefono, especialidad, capacidad)
VALUES ('Consultorio B', 'Clinica Norte', '555-2222', 'Cardiología', 5);

-- Insertar datos en la tabla Asignacion_Consultorio
INSERT INTO Asignacion_Consultorio (hora_inicio, hora_fin, Consultorios_id, Usuarios_id_usuario)
VALUES ('2023-10-01 08:00:00', '2023-10-01 16:00:00', 1, 1);

-- Insertar datos en la tabla Tipos_Citas
INSERT INTO Tipos_Citas (nombre_tipo_cita, duracion_estandar)
VALUES ('Consulta General', 30);

INSERT INTO Tipos_Citas (nombre_tipo_cita, duracion_estandar)
VALUES ('Examen de Laboratorio', 60);

-- Insertar datos en la tabla Citas
INSERT INTO Citas (fecha_hora, estado, id_paciente, id_medico, id_tipo_cita, Asignacion_Consultorio_id, Asignacion_Consultorio_Consultorios_id, Asignacion_Consultorio_Usuarios_id_usuario)
VALUES ('2023-10-05 10:00:00', 'pendiente', 1, 1, 1, 1, 1, 1);

-- Insertar datos en la tabla Historial_Medico
INSERT INTO Historial_Medico (id_paciente, diagnostico, tratamiento, medicamentos)
VALUES (1, 'Hipertensión', 'Dieta baja en sal', 'Enalapril');

-- Insertar datos en la tabla Laboratorios
INSERT INTO Laboratorios (id_paciente, id_laboratorista, tipo_examen, fecha_solicitud, fecha_realizacion, resultado, archivo_adjunto)
VALUES (1, 3, 'Hemograma', '2023-10-01', '2023-10-03', 'Normal', 'archivo.pdf');

-- Insertar datos en la tabla Horarios
INSERT INTO Horarios (id_usuario, dia_semana, hora_inicio, hora_fin)
VALUES (1, '2023-10-02', '2023-10-01 08:00:00', '2023-10-01 16:00:00');

-- Insertar datos en la tabla Especialidades
INSERT INTO Especialidades (nombre_especialidad)
VALUES ('Oncología');

INSERT INTO Especialidades (nombre_especialidad)
VALUES ('Cardiología');

-- Insertar datos en la tabla Personal_Especialidad
INSERT INTO Personal_Especialidad (id_usuario, id_especialidad)
VALUES (1, 1);

-- Insertar datos en la tabla Roles_Asignados
INSERT INTO Roles_Asignados (Usuarios_id_usuario, Roles_id_rol)
VALUES (1, 2);

-- Insertar datos en la tabla Tareas
INSERT INTO Tareas (descripcion, completado, fecha_inicio, fecha_final, cita)
VALUES ('Realizar análisis de sangre', FALSE, '2023-10-05 10:00:00', '2023-10-05 11:00:00', 1);
