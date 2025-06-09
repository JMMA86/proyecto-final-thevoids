-- Insert sample data into Roles
INSERT INTO Roles (role_name) VALUES ('ADMIN');
INSERT INTO Roles (role_name) VALUES ('RECEPCIONIST');
INSERT INTO Roles (role_name) VALUES ('DOCTOR');
INSERT INTO Roles (role_name) VALUES ('LAB-TECHNICIAN');
INSERT INTO Roles (role_name) VALUES ('PATIENT');

-- Add specific permissions for users
INSERT INTO Permissions (permission_name) VALUES ('VIEW_USERS');
INSERT INTO Permissions (permission_name) VALUES ('ADD_USERS');
INSERT INTO Permissions (permission_name) VALUES ('EDIT_USERS');
INSERT INTO Permissions (permission_name) VALUES ('DELETE_USERS');
INSERT INTO Permissions (permission_name) VALUES ('MANAGE_USER_ROLES');

-- Add specific permissions for roles
INSERT INTO Permissions (permission_name) VALUES ('VIEW_ROLES');
INSERT INTO Permissions (permission_name) VALUES ('ADD_ROLES');
INSERT INTO Permissions (permission_name) VALUES ('EDIT_ROLES');
INSERT INTO Permissions (permission_name) VALUES ('DELETE_ROLES');
INSERT INTO Permissions (permission_name) VALUES ('MANAGE_ROLE_PERMISSIONS');

-- Add specific permissions for permissions
INSERT INTO Permissions (permission_name) VALUES ('VIEW_PERMISSIONS');
INSERT INTO Permissions (permission_name) VALUES ('ADD_PERMISSIONS');
INSERT INTO Permissions (permission_name) VALUES ('EDIT_PERMISSIONS');
INSERT INTO Permissions (permission_name) VALUES ('DELETE_PERMISSIONS');

-- Add specific permissions for appointments
INSERT INTO Permissions (permission_name) VALUES ('VIEW_APPOINTMENTS');
INSERT INTO Permissions (permission_name) VALUES ('ADD_APPOINTMENTS');
INSERT INTO Permissions (permission_name) VALUES ('EDIT_APPOINTMENTS');
INSERT INTO Permissions (permission_name) VALUES ('DELETE_APPOINTMENTS');

-- Add specific permissions for appointment types
INSERT INTO Permissions (permission_name) VALUES ('VIEW_APPOINTMENT_TYPES');
INSERT INTO Permissions (permission_name) VALUES ('ADD_APPOINTMENT_TYPES');
INSERT INTO Permissions (permission_name) VALUES ('EDIT_APPOINTMENT_TYPES');
INSERT INTO Permissions (permission_name) VALUES ('DELETE_APPOINTMENT_TYPES');

-- Add specific permissions for clinics
INSERT INTO Permissions (permission_name) VALUES ('VIEW_CLINICS');
INSERT INTO Permissions (permission_name) VALUES ('ADD_CLINICS');
INSERT INTO Permissions (permission_name) VALUES ('EDIT_CLINICS');
INSERT INTO Permissions (permission_name) VALUES ('DELETE_CLINICS');

-- Add specific permissions for clinic assignments
INSERT INTO Permissions (permission_name) VALUES ('VIEW_CLINIC_ASSIGNMENTS');
INSERT INTO Permissions (permission_name) VALUES ('ADD_CLINIC_ASSIGNMENTS');
INSERT INTO Permissions (permission_name) VALUES ('EDIT_CLINIC_ASSIGNMENTS');
INSERT INTO Permissions (permission_name) VALUES ('DELETE_CLINIC_ASSIGNMENTS');

-- Add specific permissions for tasks
INSERT INTO Permissions (permission_name) VALUES ('VIEW_TASKS');
INSERT INTO Permissions (permission_name) VALUES ('ADD_TASKS');
INSERT INTO Permissions (permission_name) VALUES ('EDIT_TASKS');
INSERT INTO Permissions (permission_name) VALUES ('DELETE_TASKS');

-- Add specific permissions for medical history
INSERT INTO Permissions (permission_name) VALUES ('VIEW_MEDICAL_HISTORY');
INSERT INTO Permissions (permission_name) VALUES ('ADD_MEDICAL_HISTORY');
INSERT INTO Permissions (permission_name) VALUES ('EDIT_MEDICAL_HISTORY');
INSERT INTO Permissions (permission_name) VALUES ('DELETE_MEDICAL_HISTORY');

-- Add specific permissions for labs
INSERT INTO Permissions (permission_name) VALUES ('VIEW_LABS');
INSERT INTO Permissions (permission_name) VALUES ('ADD_LABS');
INSERT INTO Permissions (permission_name) VALUES ('EDIT_LABS');
INSERT INTO Permissions (permission_name) VALUES ('DELETE_LABS');

-- Add specific permissions for schedules
INSERT INTO Permissions (permission_name) VALUES ('VIEW_SCHEDULES');
INSERT INTO Permissions (permission_name) VALUES ('ADD_SCHEDULES');
INSERT INTO Permissions (permission_name) VALUES ('EDIT_SCHEDULES');
INSERT INTO Permissions (permission_name) VALUES ('DELETE_SCHEDULES');

-- Add specific permissions for specialties
INSERT INTO Permissions (permission_name) VALUES ('VIEW_SPECIALTIES');
INSERT INTO Permissions (permission_name) VALUES ('ADD_SPECIALTIES');
INSERT INTO Permissions (permission_name) VALUES ('EDIT_SPECIALTIES');
INSERT INTO Permissions (permission_name) VALUES ('DELETE_SPECIALTIES');

-- Add specific permissions for users specialties
INSERT INTO Permissions (permission_name) VALUES ('VIEW_USER_SPECIALTIES');
INSERT INTO Permissions (permission_name) VALUES ('ADD_USER_SPECIALTIES');
INSERT INTO Permissions (permission_name) VALUES ('EDIT_USER_SPECIALTIES');
INSERT INTO Permissions (permission_name) VALUES ('DELETE_USER_SPECIALTIES');

-- Add specific permissions for patients
INSERT INTO Permissions (permission_name) VALUES ('VIEW_PATIENTS');
INSERT INTO Permissions (permission_name) VALUES ('ADD_PATIENTS');
INSERT INTO Permissions (permission_name) VALUES ('EDIT_PATIENTS');
INSERT INTO Permissions (permission_name) VALUES ('DELETE_PATIENTS');

-- Add specific permissions for assigned roles
INSERT INTO Permissions (permission_name) VALUES ('VIEW_ASSIGNED_ROLES');
INSERT INTO Permissions (permission_name) VALUES ('ADD_ASSIGNED_ROLES');
INSERT INTO Permissions (permission_name) VALUES ('EDIT_ASSIGNED_ROLES');
INSERT INTO Permissions (permission_name) VALUES ('DELETE_ASSIGNED_ROLES');


-- Assign new permissions to the ADMIN role

-- Users
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 1);  -- VIEW_USERS
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 2);  -- ADD_USERS
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 3);  -- EDIT_USERS
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 4);  -- DELETE_USERS
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 5);  -- MANAGE_USER_ROLES

-- Roles
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 6);  -- VIEW_ROLES
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 7);  -- ADD_ROLES
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 8);  -- EDIT_ROLES
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 9);  -- DELETE_ROLES
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 10); -- MANAGE_ROLE_PERMISSIONS

-- Permissions
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 11); -- VIEW_PERMISSIONS
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 12); -- ADD_PERMISSIONS
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 13); -- EDIT_PERMISSIONS
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 14); -- DELETE_PERMISSIONS

-- Appointments
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 15); -- VIEW_APPOINTMENTS
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 16); -- ADD_APPOINTMENTS
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 17); -- EDIT_APPOINTMENTS
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 18); -- DELETE_APPOINTMENTS

-- Appointment Types
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 19); -- VIEW_APPOINTMENT_TYPES
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 20); -- ADD_APPOINTMENT_TYPES
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 21); -- EDIT_APPOINTMENT_TYPES
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 22); -- DELETE_APPOINTMENT_TYPES

-- Clinics
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 23); -- VIEW_CLINICS
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 24); -- ADD_CLINICS
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 25); -- EDIT_CLINICS
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 26); -- DELETE_CLINICS

-- Clinic Assignments
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 27); -- VIEW_CLINIC_ASSIGNMENTS
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 28); -- ADD_CLINIC_ASSIGNMENTS
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 29); -- EDIT_CLINIC_ASSIGNMENTS
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 30); -- DELETE_CLINIC_ASSIGNMENTS

-- Tasks
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 31); -- VIEW_TASKS
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 32); -- ADD_TASKS
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 33); -- EDIT_TASKS
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 34); -- DELETE_TASKS

-- Medical History
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 35); -- VIEW_MEDICAL_HISTORY
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 36); -- ADD_MEDICAL_HISTORY
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 37); -- EDIT_MEDICAL_HISTORY
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 38); -- DELETE_MEDICAL_HISTORY

-- Labs
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 39); -- VIEW_LABS
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 40); -- ADD_LABS
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 41); -- EDIT_LABS
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 42); -- DELETE_LABS

-- Schedules
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 43); -- VIEW_SCHEDULES
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 44); -- ADD_SCHEDULES
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 45); -- EDIT_SCHEDULES
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 46); -- DELETE_SCHEDULES

-- Specialties
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 47); -- VIEW_SPECIALTIES
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 48); -- ADD_SPECIALTIES
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 49); -- EDIT_SPECIALTIES
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 50); -- DELETE_SPECIALTIES

-- User Specialties
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 51); -- VIEW_USER_SPECIALTIES
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 52); -- ADD_USER_SPECIALTIES
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 53); -- EDIT_USER_SPECIALTIES
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 54); -- DELETE_USER_SPECIALTIES

-- Patients
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 55); -- VIEW_PATIENTS
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 56); -- ADD_PATIENTS
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 57); -- EDIT_PATIENTS
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 58); -- DELETE_PATIENTS

-- Assigned Roles
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 59); -- VIEW_ASSIGNED_ROLES
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 60); -- ADD_ASSIGNED_ROLES
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 61); -- EDIT_ASSIGNED_ROLES
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 62); -- DELETE_ASSIGNED_ROLES

-- Assign view, edit, and register user permissions to the RECEPCIONIST role
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (2, 1);  -- VIEW_USERS
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (2, 3);  -- EDIT_USERS
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (2, 2);  -- ADD_USERS

-- Assign view and register user permissions to the DOCTOR role
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (3, 1);  -- VIEW_USERS
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (3, 2);  -- ADD_USERS
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (3, 3);  -- EDIT_USERS

-- Assign view and register user permissions to the LAB-TECHNICIAN role
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (4, 1);  -- VIEW_USERS
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (4, 2);  -- ADD_USERS
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (4, 3);  -- EDIT_USERS

-- Insert sample data into Users
INSERT INTO Users (full_name, identification, birth_date, gender, address, phone, email, password, status)
VALUES ('Admin', '123456789', '1980-05-15', 'Male', '123 Main St', '555-1234', 'john.doe@example.com', '$2a$12$7Ni.4Byq96tNSgEOy1mtiufyDWTq4wveUP7bQnd1AhIgNwojcwT/2', 'active');

INSERT INTO Users (full_name, identification, birth_date, gender, address, phone, email, password, status)
VALUES ('Jane Smith', '111111111', '1990-08-25', 'Female', '456 Elm St', '555-5678', 'jane.smith@example.com', '$2a$12$7Ni.4Byq96tNSgEOy1mtiufyDWTq4wveUP7bQnd1AhIgNwojcwT/2', 'active');

INSERT INTO Users (full_name, identification, birth_date, gender, address, phone, email, password, status)
VALUES ('Johnny Sins', '456789123', '1985-03-10', 'Female', '789 Oak St', '555-9012', 'alice.johnson@example.com', '$2a$12$7Ni.4Byq96tNSgEOy1mtiufyDWTq4wveUP7bQnd1AhIgNwojcwT/2', 'active');

INSERT INTO Users (full_name, identification, birth_date, gender, address, phone, email, password, status)
VALUES ('Bob Wilson', '789123456', '1992-07-15', 'Male', '321 Pine St', '555-3456', 'bob.wilson@example.com', '$2a$12$7Ni.4Byq96tNSgEOy1mtiufyDWTq4wveUP7bQnd1AhIgNwojcwT/2', 'active');

INSERT INTO Users (full_name, identification, birth_date, gender, address, phone, email, password, status)
VALUES ('Maria Garcia', '654321789', '1988-12-03', 'Female', '456 Cedar Ave', '555-7890', 'maria.garcia@example.com', '$2a$12$7Ni.4Byq96tNSgEOy1mtiufyDWTq4wveUP7bQnd1AhIgNwojcwT/2', 'active');

INSERT INTO Users (full_name, identification, birth_date, gender, address, phone, email, password, status)
VALUES ('Sarah Connor', '987654321', '1975-05-20', 'Female', '789 Future St', '555-4567', 'sarah.connor@example.com', '$2a$12$7Ni.4Byq96tNSgEOy1mtiufyDWTq4wveUP7bQnd1AhIgNwojcwT/2', 'active');

-- Insert sample data into Patients
INSERT INTO Patients (user_id, blood_group, allergies, family_history)
VALUES (2, 'A+', 'Pollen', 'Diabetes');

INSERT INTO Patients (user_id, blood_group, allergies, family_history)
VALUES (4, 'O-', 'Penicillin', 'Hypertension');

INSERT INTO Patients (user_id, blood_group, allergies, family_history)
VALUES (5, 'B+', 'Nuts', 'Heart Disease');

INSERT INTO Patients (user_id, blood_group, allergies, family_history)
VALUES (6, 'AB+', 'None', 'Cancer');

-- Insert sample data into Clinics
INSERT INTO Clinics (name, address, phone, specialty, capacity)
VALUES ('City Clinic', '789 Clinic Rd', '555-3456', 'Oncology', 50);

-- Insert sample data into Specialty
INSERT INTO Specialties (specialty_name) VALUES ('Oncology');
INSERT INTO Specialties (specialty_name) VALUES ('Cardiology');

-- Insert sample data into UserSpecialty
INSERT INTO User_Specialties (user_id, specialty_id) VALUES (3, 1); -- John Doe is an Oncologist

-- Insert sample data into AppointmentType
INSERT INTO Appointment_Types (type_name, standard_duration) VALUES ('Consultation', 30);
INSERT INTO Appointment_Types (type_name, standard_duration) VALUES ('Follow-up', 15);

-- Insert sample data into ClinicAssignment
INSERT INTO Clinic_Assignments (start_time, end_time, clinic_id, user_id)
VALUES ('2023-10-01 08:00:00', '2023-10-01 16:00:00', 1, 1);

-- Insert sample data into Appointments
INSERT INTO Appointments (date_time, status, patient_id, doctor_id, type_id, clinic_assignment_id)
VALUES ('2023-10-02 10:00:00', 'pending', 1, 3, 1, 1);

-- Insert sample data into MedicalHistory
INSERT INTO Medical_History (patient_id, diagnosis, treatment, medications)
VALUES (2, 'Cancer', 'Chemotherapy', 'Medicine A, Medicine B');

-- Insert sample data into Labs
INSERT INTO Labs (patient_id, lab_technician_id, test_type, request_date, completion_date, result, attachment)
VALUES (1, 3, 'Blood Test', '2023-10-01 10:00:00', '2023-10-02 12:00:00', 'Normal', 'path/to/attachment.pdf');

-- Insert sample data into Tasks
INSERT INTO Tasks (description, completed, start_date, end_date, appointment_id)
VALUES ('Prepare patient report', TRUE, '2023-10-02 09:00:00', '2023-10-02 09:30:00', 1);

-- Insert sample data into Schedules
INSERT INTO Schedules (user_id, day_of_week, start_time, end_time)
VALUES (1, 'Monday', '2023-10-02 08:00:00', '2023-10-02 16:00:00');

-- Insert sample data into AssignedRole
INSERT INTO Assigned_Roles (user_id, role_id) VALUES (1, 1); -- Admin is assigned Admin role
INSERT INTO Assigned_Roles (user_id, role_id) VALUES (3, 3); -- Johnny is assigned as a Doctor
INSERT INTO Assigned_Roles (user_id, role_id) VALUES (2, 5); -- Jane Smith is assigned as a Patient
INSERT INTO Assigned_Roles (user_id, role_id) VALUES (3, 4); -- Alice Johnson is assigned as a Lab Technician
INSERT INTO Assigned_Roles (user_id, role_id) VALUES (4, 5); -- Bob Wilson is assigned as a Patient
INSERT INTO Assigned_Roles (user_id, role_id) VALUES (5, 5); -- Maria Garcia is assigned as a Patient
INSERT INTO Assigned_Roles (user_id, role_id) VALUES (6, 2); -- Sarah Connor is assigned as a Receptionist