
CREATE TABLE Asignacion_Consultorio 
    ( 
     hora_inicio         DATE  NOT NULL , 
     hora_fin            DATE  NOT NULL , 
     id                  NUMBER  NOT NULL , 
     Consultorios_id     NUMBER  NOT NULL , 
     Usuarios_id_usuario INTEGER  NOT NULL 
    ) 
;

ALTER TABLE Asignacion_Consultorio 
    ADD CONSTRAINT Asignacion_Consultorio_PK PRIMARY KEY ( id, Consultorios_id, Usuarios_id_usuario ) ;

CREATE TABLE Citas 
    ( 
     id_cita                                    INTEGER  NOT NULL , 
     id_paciente                                INTEGER  NOT NULL , 
     id_medico                                  INTEGER  NOT NULL , 
     id_tipo_cita                               INTEGER  NOT NULL , 
     fecha_hora                                 DATE  NOT NULL , 
     estado                                     VARCHAR2 (200) DEFAULT 'pendiente' , 
     Asignacion_Consultorio_id                  NUMBER  NOT NULL , 
     Asignacion_Consultorio_Consultorios_id     NUMBER  NOT NULL , 
     Asignacion_Consultorio_Usuarios_id_usuario INTEGER  NOT NULL 
    ) 
;
CREATE INDEX idx_citas_fecha_hora ON Citas 
    ( 
     fecha_hora ASC 
    ) 
;

ALTER TABLE Citas 
    ADD CONSTRAINT Citas_PK PRIMARY KEY ( id_cita ) ;

CREATE TABLE Consultorios 
    ( 
     id           NUMBER  NOT NULL , 
     nombre       VARCHAR2 (20)  NOT NULL , 
     direccion    VARCHAR2 (20)  NOT NULL , 
     telefono     VARCHAR2 (20) , 
     especialidad VARCHAR2 (20) , 
     capacidad    NUMBER 
    ) 
;

ALTER TABLE Consultorios 
    ADD CONSTRAINT Consultorios_PK PRIMARY KEY ( id ) ;

CREATE TABLE Especialidades 
    ( 
     id_especialidad     INTEGER  NOT NULL , 
     nombre_especialidad VARCHAR2 (100)  NOT NULL 
    ) 
;

ALTER TABLE Especialidades 
    ADD CONSTRAINT Especialidades_PK PRIMARY KEY ( id_especialidad ) ;

ALTER TABLE Especialidades 
    ADD CONSTRAINT INDEX_1 UNIQUE ( nombre_especialidad ) ;

CREATE TABLE Historial_Medico 
    ( 
     id_historial   INTEGER  NOT NULL , 
     id_paciente    INTEGER  NOT NULL , 
     diagnostico    VARCHAR2 (500) , 
     tratamiento    VARCHAR2 (500) , 
     medicamentos   VARCHAR2 (500) , 
     fecha_registro TIMESTAMP DEFAULT CURRENT_TIMESTAMP 
    ) 
;
CREATE INDEX idx_historial_paciente ON Historial_Medico 
    ( 
     id_paciente ASC 
    ) 
;

ALTER TABLE Historial_Medico 
    ADD CONSTRAINT Historial_Medico_PK PRIMARY KEY ( id_historial ) ;

CREATE TABLE Horarios 
    ( 
     id_horario  INTEGER  NOT NULL , 
     id_usuario  INTEGER  NOT NULL , 
     dia_semana  DATE , 
     hora_inicio DATE  NOT NULL , 
     hora_fin    DATE  NOT NULL 
    ) 
;

ALTER TABLE Horarios 
    ADD CONSTRAINT Horarios_PK PRIMARY KEY ( id_horario ) ;

CREATE TABLE Laboratorios 
    ( 
     id_examen         INTEGER  NOT NULL , 
     id_paciente       INTEGER  NOT NULL , 
     id_laboratorista  INTEGER  NOT NULL , 
     tipo_examen       VARCHAR2 (100) , 
     fecha_solicitud   DATE  NOT NULL , 
     fecha_realizacion DATE , 
     resultado         VARCHAR2 (300) , 
     archivo_adjunto   VARCHAR2 (255) 
    ) 
;
CREATE INDEX idx_laboratorios_paciente ON Laboratorios 
    ( 
     id_paciente ASC 
    ) 
;

ALTER TABLE Laboratorios 
    ADD CONSTRAINT Laboratorios_PK PRIMARY KEY ( id_examen ) ;

CREATE TABLE Pacientes 
    ( 
     id_paciente       INTEGER  NOT NULL , 
     id_usuario        INTEGER  NOT NULL , 
     grupo_sanguineo   VARCHAR2 (10) , 
     alergias          VARCHAR2 (500) , 
     historia_familiar VARCHAR2 (500) 
    ) 
;

ALTER TABLE Pacientes 
    ADD CONSTRAINT Pacientes_PK PRIMARY KEY ( id_paciente ) ;

ALTER TABLE Pacientes 
    ADD CONSTRAINT INDEX_1v1 UNIQUE ( id_usuario ) ;

CREATE TABLE Permisos 
    ( 
     id_permiso     INTEGER  NOT NULL , 
     nombre_permiso VARCHAR2 (50)  NOT NULL 
    ) 
;

ALTER TABLE Permisos 
    ADD CONSTRAINT Permisos_PK PRIMARY KEY ( id_permiso ) ;

ALTER TABLE Permisos 
    ADD CONSTRAINT INDEX_1v2 UNIQUE ( nombre_permiso ) ;

CREATE TABLE Personal_Especialidades 
    ( 
     id_usuario      INTEGER  NOT NULL , 
     id_especialidad INTEGER  NOT NULL 
    ) 
;

ALTER TABLE Personal_Especialidades 
    ADD CONSTRAINT Personal_Especialidades_PK PRIMARY KEY ( id_usuario, id_especialidad ) ;

CREATE TABLE Roles 
    ( 
     id_rol     INTEGER  NOT NULL , 
     nombre_rol VARCHAR2 (50)  NOT NULL 
    ) 
;

ALTER TABLE Roles 
    ADD CONSTRAINT Roles_PK PRIMARY KEY ( id_rol ) ;

ALTER TABLE Roles 
    ADD CONSTRAINT INDEX_1v3 UNIQUE ( nombre_rol ) ;

CREATE TABLE Roles_Asignados 
    ( 
     id                  NUMBER  NOT NULL , 
     Usuarios_id_usuario INTEGER  NOT NULL , 
     Roles_id_rol        INTEGER  NOT NULL 
    ) 
;

ALTER TABLE Roles_Asignados 
    ADD CONSTRAINT Roles_Asignados_PK PRIMARY KEY ( id, Usuarios_id_usuario, Roles_id_rol ) ;

CREATE TABLE Roles_Permisos 
    ( 
     id_rol     INTEGER  NOT NULL , 
     id_permiso INTEGER  NOT NULL 
    ) 
;

ALTER TABLE Roles_Permisos 
    ADD CONSTRAINT Roles_Permisos_PK PRIMARY KEY ( id_rol, id_permiso ) ;

CREATE TABLE Tareas 
    ( 
     id            NUMBER  NOT NULL , 
     descripcion   VARCHAR2 (500) , 
     completado    NUMBER  NOT NULL , 
     fecha_inicio  DATE  NOT NULL , 
     fecha_final   DATE  NOT NULL , 
     Citas_id_cita INTEGER  NOT NULL 
    ) 
;

ALTER TABLE Tareas 
    ADD CONSTRAINT Tareas_PK PRIMARY KEY ( id ) ;

CREATE TABLE Tipos_Citas 
    ( 
     id_tipo_cita      INTEGER  NOT NULL , 
     nombre_tipo_cita  VARCHAR2 (100)  NOT NULL , 
     duracion_estandar INTEGER  NOT NULL 
    ) 
;

ALTER TABLE Tipos_Citas 
    ADD CONSTRAINT Tipos_Citas_PK PRIMARY KEY ( id_tipo_cita ) ;

ALTER TABLE Tipos_Citas 
    ADD CONSTRAINT INDEX_1v4 UNIQUE ( nombre_tipo_cita ) ;

CREATE TABLE Usuarios 
    ( 
     id_usuario            INTEGER  NOT NULL , 
     nombre_completo       VARCHAR2 (255)  NOT NULL , 
     identificacion        VARCHAR2 (50)  NOT NULL , 
     fecha_nacimiento      DATE , 
     genero                VARCHAR2 (20) , 
     direccion             VARCHAR2 (255) , 
     telefono              VARCHAR2 (20) , 
     correo_electronico    VARCHAR2 (255) , 
     rol_id                INTEGER  NOT NULL , 
     estado                VARCHAR2 (20) DEFAULT 'activo' , 
     Pacientes_id_paciente INTEGER  NOT NULL , 
     contraseña            VARCHAR2 (20)  NOT NULL 
    ) 
;
CREATE INDEX idx_usuario_identificacion ON Usuarios 
    ( 
     identificacion ASC 
    ) 
;

ALTER TABLE Usuarios 
    ADD CONSTRAINT Usuarios_PK PRIMARY KEY ( id_usuario ) ;

ALTER TABLE Usuarios 
    ADD CONSTRAINT INDEX_1v5 UNIQUE ( identificacion ) ;

ALTER TABLE Asignacion_Consultorio 
    ADD CONSTRAINT Asignacion_Consultorio_Consultorios_FK FOREIGN KEY 
    ( 
     Consultorios_id
    ) 
    REFERENCES Consultorios 
    ( 
     id
    ) 
;

ALTER TABLE Asignacion_Consultorio 
    ADD CONSTRAINT Asignacion_Consultorio_Usuarios_FK FOREIGN KEY 
    ( 
     Usuarios_id_usuario
    ) 
    REFERENCES Usuarios 
    ( 
     id_usuario
    ) 
;

ALTER TABLE Citas 
    ADD CONSTRAINT Citas_Asignacion_Consultorio_FK FOREIGN KEY 
    ( 
     Asignacion_Consultorio_id,
     Asignacion_Consultorio_Consultorios_id,
     Asignacion_Consultorio_Usuarios_id_usuario
    ) 
    REFERENCES Asignacion_Consultorio 
    ( 
     id,
     Consultorios_id,
     Usuarios_id_usuario
    ) 
;

ALTER TABLE Citas 
    ADD CONSTRAINT Citas_Pacientes_FK FOREIGN KEY 
    ( 
     id_paciente
    ) 
    REFERENCES Pacientes 
    ( 
     id_paciente
    ) 
;

ALTER TABLE Citas 
    ADD CONSTRAINT Citas_Tipos_Citas_FK FOREIGN KEY 
    ( 
     id_tipo_cita
    ) 
    REFERENCES Tipos_Citas 
    ( 
     id_tipo_cita
    ) 
;

ALTER TABLE Historial_Medico 
    ADD CONSTRAINT Historial_Medico_Pacientes_FK FOREIGN KEY 
    ( 
     id_paciente
    ) 
    REFERENCES Pacientes 
    ( 
     id_paciente
    ) 
;

ALTER TABLE Horarios 
    ADD CONSTRAINT Horarios_Usuarios_FK FOREIGN KEY 
    ( 
     id_usuario
    ) 
    REFERENCES Usuarios 
    ( 
     id_usuario
    ) 
;

ALTER TABLE Laboratorios 
    ADD CONSTRAINT Laboratorios_Pacientes_FK FOREIGN KEY 
    ( 
     id_paciente
    ) 
    REFERENCES Pacientes 
    ( 
     id_paciente
    ) 
;

ALTER TABLE Laboratorios 
    ADD CONSTRAINT Laboratorios_Usuarios_FK FOREIGN KEY 
    ( 
     id_laboratorista
    ) 
    REFERENCES Usuarios 
    ( 
     id_usuario
    ) 
;

ALTER TABLE Pacientes 
    ADD CONSTRAINT Pacientes_Usuarios_FK FOREIGN KEY 
    ( 
     id_usuario
    ) 
    REFERENCES Usuarios 
    ( 
     id_usuario
    ) 
;

ALTER TABLE Personal_Especialidades 
    ADD CONSTRAINT Personal_Especialidades_Especialidades_FK FOREIGN KEY 
    ( 
     id_especialidad
    ) 
    REFERENCES Especialidades 
    ( 
     id_especialidad
    ) 
;

ALTER TABLE Personal_Especialidades 
    ADD CONSTRAINT Personal_Especialidades_Usuarios_FK FOREIGN KEY 
    ( 
     id_usuario
    ) 
    REFERENCES Usuarios 
    ( 
     id_usuario
    ) 
;

ALTER TABLE Roles_Asignados 
    ADD CONSTRAINT Roles_Asignados_Roles_FK FOREIGN KEY 
    ( 
     Roles_id_rol
    ) 
    REFERENCES Roles 
    ( 
     id_rol
    ) 
;

ALTER TABLE Roles_Asignados 
    ADD CONSTRAINT Roles_Asignados_Usuarios_FK FOREIGN KEY 
    ( 
     Usuarios_id_usuario
    ) 
    REFERENCES Usuarios 
    ( 
     id_usuario
    ) 
;

ALTER TABLE Roles_Permisos 
    ADD CONSTRAINT Roles_Permisos_Permisos_FK FOREIGN KEY 
    ( 
     id_permiso
    ) 
    REFERENCES Permisos 
    ( 
     id_permiso
    ) 
;

ALTER TABLE Roles_Permisos 
    ADD CONSTRAINT Roles_Permisos_Roles_FK FOREIGN KEY 
    ( 
     id_rol
    ) 
    REFERENCES Roles 
    ( 
     id_rol
    ) 
;

ALTER TABLE Tareas 
    ADD CONSTRAINT Tareas_Citas_FK FOREIGN KEY 
    ( 
     Citas_id_cita
    ) 
    REFERENCES Citas 
    ( 
     id_cita
    ) 
;