-- Insert sample data into Roles
INSERT INTO Roles (role_name) VALUES ('Admin');
INSERT INTO Roles (role_name) VALUES ('Doctor');
INSERT INTO Roles (role_name) VALUES ('Patient');
INSERT INTO Roles (role_name) VALUES ('Lab Technician');

-- Insert sample data into Permissions
INSERT INTO Permissions (permission_name) VALUES ('VIEW_APPOINTMENTS');
INSERT INTO Permissions (permission_name) VALUES ('EDIT_MEDICAL_HISTORY');
INSERT INTO Permissions (permission_name) VALUES ('MANAGE_LABS');

-- Insert sample data into RolePermission
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (1, 1); -- Admin can VIEW_APPOINTMENTS
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (2, 2); -- Doctor can EDIT_MEDICAL_HISTORY
INSERT INTO Roles_Permissions (role_id, permission_id) VALUES (4, 3); -- Lab Technician can MANAGE_LABS

-- Insert sample data into Users
INSERT INTO Users (full_name, identification, birth_date, gender, address, phone, email, role_id, password, status)
VALUES ('John Doe', '123456789', '1980-05-15', 'Male', '123 Main St', '555-1234', 'john.doe@example.com', 2, 'password123', 'active');

INSERT INTO Users (full_name, identification, birth_date, gender, address, phone, email, role_id, password, status)
VALUES ('Jane Smith', '987654321', '1990-08-25', 'Female', '456 Elm St', '555-5678', 'jane.smith@example.com', 3, 'password456', 'active');

INSERT INTO Users (full_name, identification, birth_date, gender, address, phone, email, role_id, password, status)
VALUES ('Alice Johnson', '456789123', '1985-03-10', 'Female', '789 Oak St', '555-9012', 'alice.johnson@example.com', 4, 'password789', 'active');

-- Insert sample data into Patients
INSERT INTO Patients (user_id, blood_group, allergies, family_history)
VALUES (2, 'A+', 'Pollen', 'Diabetes');

-- Insert sample data into Clinics
INSERT INTO Clinics (name, address, phone, specialty, capacity)
VALUES ('City Clinic', '789 Clinic Rd', '555-3456', 'Oncology', 50);

-- Insert sample data into Specialty
INSERT INTO Specialties (specialty_name) VALUES ('Oncology');
INSERT INTO Specialties (specialty_name) VALUES ('Cardiology');

-- Insert sample data into UserSpecialty
INSERT INTO User_Specialties (user_id, specialty_id) VALUES (1, 1); -- John Doe is an Oncologist

-- Insert sample data into AppointmentType
INSERT INTO Appointment_Types (type_name, standard_duration) VALUES ('Consultation', 30);
INSERT INTO Appointment_Types (type_name, standard_duration) VALUES ('Follow-up', 15);

-- Insert sample data into ClinicAssignment
INSERT INTO Clinic_Assignments (start_time, end_time, clinic_id, user_id)
VALUES ('2023-10-01 08:00:00', '2023-10-01 16:00:00', 1, 1);

-- Insert sample data into Appointments
INSERT INTO Appointments (date_time, status, patient_id, doctor_id, type_id, clinic_assignment_id)
VALUES ('2023-10-02 10:00:00', 'pending', 1, 1, 1, 1);

-- Insert sample data into MedicalHistory
INSERT INTO Medical_History (patient_id, diagnosis, treatment, medications)
VALUES (1, 'Cancer', 'Chemotherapy', 'Medicine A, Medicine B');

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
INSERT INTO Assigned_Roles (user_id, role_id) VALUES (1, 2); -- John Doe is assigned as a Doctor
INSERT INTO Assigned_Roles (user_id, role_id) VALUES (2, 3); -- Jane Smith is assigned as a Patient
INSERT INTO Assigned_Roles (user_id, role_id) VALUES (3, 4); -- Alice Johnson is assigned as a Lab Technician