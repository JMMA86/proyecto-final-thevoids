CREATE TABLE Clinic_Assignments
(
    start_time         DATE  NOT NULL ,
    end_time           DATE  NOT NULL ,
    id                 NUMBER  NOT NULL ,
    clinic_id          NUMBER  NOT NULL ,
    user_id            INTEGER  NOT NULL
)
;

ALTER TABLE Clinic_Assignments
    ADD CONSTRAINT Clinic_Assignments_PK PRIMARY KEY ( id, clinic_id, user_id ) ;

CREATE TABLE Appointments
(
    appointment_id                          INTEGER  NOT NULL ,
    patient_id                              INTEGER  NOT NULL ,
    doctor_id                               INTEGER  NOT NULL ,
    type_id                                 INTEGER  NOT NULL ,
    date_time                               DATE  NOT NULL ,
    status                                  VARCHAR2 (200) DEFAULT 'pending' ,
    clinic_assignment_id                    NUMBER  NOT NULL ,
    clinic_id                               NUMBER  NOT NULL ,
    user_id                                 INTEGER  NOT NULL
)
;
CREATE INDEX idx_appointments_date_time ON Appointments
    (
     date_time ASC
        )
;

ALTER TABLE Appointments
    ADD CONSTRAINT Appointments_PK PRIMARY KEY ( appointment_id ) ;

CREATE TABLE Clinics
(
    id           NUMBER  NOT NULL ,
    name         VARCHAR2 (20)  NOT NULL ,
    address      VARCHAR2 (20)  NOT NULL ,
    phone        VARCHAR2 (20) ,
    specialty    VARCHAR2 (20) ,
    capacity     NUMBER
)
;

ALTER TABLE Clinics
    ADD CONSTRAINT Clinics_PK PRIMARY KEY ( id ) ;

CREATE TABLE Specialties
(
    specialty_id     INTEGER  NOT NULL ,
    specialty_name   VARCHAR2 (100)  NOT NULL
)
;

ALTER TABLE Specialties
    ADD CONSTRAINT Specialties_PK PRIMARY KEY ( specialty_id ) ;

ALTER TABLE Specialties
    ADD CONSTRAINT INDEX_1 UNIQUE ( specialty_name ) ;

CREATE TABLE Medical_History
(
    history_id     INTEGER  NOT NULL ,
    patient_id     INTEGER  NOT NULL ,
    diagnosis      VARCHAR2 (500) ,
    treatment      VARCHAR2 (500) ,
    medications    VARCHAR2 (500) ,
    record_date    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
)
;
CREATE INDEX idx_medical_history_patient ON Medical_History
    (
     patient_id ASC
        )
;

ALTER TABLE Medical_History
    ADD CONSTRAINT Medical_History_PK PRIMARY KEY ( history_id ) ;

CREATE TABLE Schedules
(
    schedule_id  INTEGER  NOT NULL ,
    user_id      INTEGER  NOT NULL ,
    day_of_week  DATE ,
    start_time   DATE  NOT NULL ,
    end_time     DATE  NOT NULL
)
;

ALTER TABLE Schedules
    ADD CONSTRAINT Schedules_PK PRIMARY KEY ( schedule_id ) ;

CREATE TABLE Labs
(
    lab_id             INTEGER  NOT NULL ,
    patient_id         INTEGER  NOT NULL ,
    lab_technician_id  INTEGER  NOT NULL ,
    test_type          VARCHAR2 (100) ,
    request_date       DATE  NOT NULL ,
    completion_date    DATE ,
    result             VARCHAR2 (300) ,
    attachment         VARCHAR2 (255)
)
;
CREATE INDEX idx_labs_patient ON Labs
    (
     patient_id ASC
        )
;

ALTER TABLE Labs
    ADD CONSTRAINT Labs_PK PRIMARY KEY ( lab_id ) ;

CREATE TABLE Patients
(
    patient_id       INTEGER  NOT NULL ,
    user_id          INTEGER  NOT NULL ,
    blood_group      VARCHAR2 (10) ,
    allergies        VARCHAR2 (500) ,
    family_history   VARCHAR2 (500)
)
;

ALTER TABLE Patients
    ADD CONSTRAINT Patients_PK PRIMARY KEY ( patient_id ) ;

ALTER TABLE Patients
    ADD CONSTRAINT INDEX_1v1 UNIQUE ( user_id ) ;

CREATE TABLE Permissions
(
    permission_id     INTEGER  NOT NULL ,
    permission_name   VARCHAR2 (50)  NOT NULL
)
;

ALTER TABLE Permissions
    ADD CONSTRAINT Permissions_PK PRIMARY KEY ( permission_id ) ;

ALTER TABLE Permissions
    ADD CONSTRAINT INDEX_1v2 UNIQUE ( permission_name ) ;

CREATE TABLE User_Specialties
(
    user_id      INTEGER  NOT NULL ,
    specialty_id INTEGER  NOT NULL
)
;

ALTER TABLE User_Specialties
    ADD CONSTRAINT User_Specialties_PK PRIMARY KEY ( user_id, specialty_id ) ;

CREATE TABLE Roles
(
    role_id     INTEGER  NOT NULL ,
    role_name   VARCHAR2 (50)  NOT NULL
)
;

ALTER TABLE Roles
    ADD CONSTRAINT Roles_PK PRIMARY KEY ( role_id ) ;

ALTER TABLE Roles
    ADD CONSTRAINT INDEX_1v3 UNIQUE ( role_name ) ;

CREATE TABLE Assigned_Roles
(
    id                  NUMBER  NOT NULL ,
    user_id             INTEGER  NOT NULL ,
    role_id             INTEGER  NOT NULL
)
;

ALTER TABLE Assigned_Roles
    ADD CONSTRAINT Assigned_Roles_PK PRIMARY KEY ( id, user_id, role_id ) ;

CREATE TABLE Roles_Permissions
(
    role_id     INTEGER  NOT NULL ,
    permission_id INTEGER  NOT NULL
)
;

ALTER TABLE Roles_Permissions
    ADD CONSTRAINT Roles_Permissions_PK PRIMARY KEY ( role_id, permission_id ) ;

CREATE TABLE Tasks
(
    id            NUMBER  NOT NULL ,
    description   VARCHAR2 (500) ,
    completed     NUMBER  NOT NULL ,
    start_date    DATE  NOT NULL ,
    end_date      DATE  NOT NULL ,
    appointment_id INTEGER  NOT NULL
)
;

ALTER TABLE Tasks
    ADD CONSTRAINT Tasks_PK PRIMARY KEY ( id ) ;

CREATE TABLE Appointment_Types
(
    type_id          INTEGER  NOT NULL ,
    type_name        VARCHAR2 (100)  NOT NULL ,
    standard_duration INTEGER  NOT NULL
)
;

ALTER TABLE Appointment_Types
    ADD CONSTRAINT Appointment_Types_PK PRIMARY KEY ( type_id ) ;

ALTER TABLE Appointment_Types
    ADD CONSTRAINT INDEX_1v4 UNIQUE ( type_name ) ;

CREATE TABLE Users
(
    user_id              INTEGER  NOT NULL ,
    full_name            VARCHAR2 (255)  NOT NULL ,
    identification       VARCHAR2 (50)  NOT NULL ,
    birth_date           DATE ,
    gender               VARCHAR2 (20) ,
    address              VARCHAR2 (255) ,
    phone                VARCHAR2 (20) ,
    email                VARCHAR2 (255) ,
    role_id              INTEGER  NOT NULL ,
    status               VARCHAR2 (20) DEFAULT 'active' ,
    patient_id           INTEGER  NOT NULL ,
    password             VARCHAR2 (20)  NOT NULL
)
;
CREATE INDEX idx_user_identification ON Users
    (
     identification ASC
        )
;

ALTER TABLE Users
    ADD CONSTRAINT Users_PK PRIMARY KEY ( user_id ) ;

ALTER TABLE Users
    ADD CONSTRAINT INDEX_1v5 UNIQUE ( identification ) ;

ALTER TABLE Clinic_Assignments
    ADD CONSTRAINT Clinic_Assignments_Clinics_FK FOREIGN KEY
        (
         clinic_id
            )
        REFERENCES Clinics
            (
             id
                )
;

ALTER TABLE Clinic_Assignments
    ADD CONSTRAINT Clinic_Assignments_Users_FK FOREIGN KEY
        (
         user_id
            )
        REFERENCES Users
            (
             user_id
                )
;

ALTER TABLE Appointments
    ADD CONSTRAINT Appointments_Clinic_Assignments_FK FOREIGN KEY
        (
         clinic_assignment_id,
         clinic_id,
         user_id
            )
        REFERENCES Clinic_Assignments
            (
             id,
             clinic_id,
             user_id
                )
;

ALTER TABLE Appointments
    ADD CONSTRAINT Appointments_Patients_FK FOREIGN KEY
        (
         patient_id
            )
        REFERENCES Patients
            (
             patient_id
                )
;

ALTER TABLE Appointments
    ADD CONSTRAINT Appointments_Appointment_Types_FK FOREIGN KEY
        (
         type_id
            )
        REFERENCES Appointment_Types
            (
             type_id
                )
;

ALTER TABLE Medical_History
    ADD CONSTRAINT Medical_History_Patients_FK FOREIGN KEY
        (
         patient_id
            )
        REFERENCES Patients
            (
             patient_id
                )
;

ALTER TABLE Schedules
    ADD CONSTRAINT Schedules_Users_FK FOREIGN KEY
        (
         user_id
            )
        REFERENCES Users
            (
             user_id
                )
;

ALTER TABLE Labs
    ADD CONSTRAINT Labs_Patients_FK FOREIGN KEY
        (
         patient_id
            )
        REFERENCES Patients
            (
             patient_id
                )
;

ALTER TABLE Labs
    ADD CONSTRAINT Labs_Users_FK FOREIGN KEY
        (
         lab_technician_id
            )
        REFERENCES Users
            (
             user_id
                )
;

ALTER TABLE Patients
    ADD CONSTRAINT Patients_Users_FK FOREIGN KEY
        (
         user_id
            )
        REFERENCES Users
            (
             user_id
                )
;

ALTER TABLE User_Specialties
    ADD CONSTRAINT User_Specialties_Specialties_FK FOREIGN KEY
        (
         specialty_id
            )
        REFERENCES Specialties
            (
             specialty_id
                )
;

ALTER TABLE User_Specialties
    ADD CONSTRAINT User_Specialties_Users_FK FOREIGN KEY
        (
         user_id
            )
        REFERENCES Users
            (
             user_id
                )
;

ALTER TABLE Assigned_Roles
    ADD CONSTRAINT Assigned_Roles_Roles_FK FOREIGN KEY
        (
         role_id
            )
        REFERENCES Roles
            (
             role_id
                )
;

ALTER TABLE Assigned_Roles
    ADD CONSTRAINT Assigned_Roles_Users_FK FOREIGN KEY
        (
         user_id
            )
        REFERENCES Users
            (
             user_id
                )
;

ALTER TABLE Roles_Permissions
    ADD CONSTRAINT Roles_Permissions_Permissions_FK FOREIGN KEY
        (
         permission_id
            )
        REFERENCES Permissions
            (
             permission_id
                )
;

ALTER TABLE Roles_Permissions
    ADD CONSTRAINT Roles_Permissions_Roles_FK FOREIGN KEY
        (
         role_id
            )
        REFERENCES Roles
            (
             role_id
                )
;

ALTER TABLE Tasks
    ADD CONSTRAINT Tasks_Appointments_FK FOREIGN KEY
        (
         appointment_id
            )
        REFERENCES Appointments
            (
             appointment_id
                )
;