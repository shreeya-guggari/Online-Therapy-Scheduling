-- Create the database
CREATE DATABASE therapy_scheduler;
USE therapy_scheduler;

-- Users table (stores user credentials and roles)
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    role ENUM('PATIENT', 'THERAPIST') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Profiles table (stores user profile information)
CREATE TABLE profiles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT UNIQUE,
    full_name VARCHAR(100),
    phone VARCHAR(15),
    address TEXT,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Appointments table (stores appointment details)
CREATE TABLE appointments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    patient_id BIGINT,
    counsellor_id BIGINT,
    start_time DATETIME NOT NULL,
    end_time DATETIME NOT NULL,
    status ENUM('PENDING', 'CONFIRMED', 'CANCELLED') DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (patient_id) REFERENCES users(id),
    FOREIGN KEY (counsellor_id) REFERENCES users(id)
);

-- Sessions table (stores session details, including notes for therapists/admins)
CREATE TABLE sessions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    appointment_id BIGINT UNIQUE,
    session_type ENUM('TEXT', 'VIDEO') NOT NULL,
    status ENUM('SCHEDULED', 'ONGOING', 'COMPLETED') DEFAULT 'SCHEDULED',
    session_details TEXT,
    notes TEXT,
    started_at TIMESTAMP,
    ended_at TIMESTAMP,
    FOREIGN KEY (appointment_id) REFERENCES appointments(id)
);

-- Prescriptions table (stores prescription details, visible to admins, therapists, patients)
CREATE TABLE prescriptions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    session_id BIGINT,
    details TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (session_id) REFERENCES sessions(id)
);

-- Payments table (stores payment details)
CREATE TABLE payments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    appointment_id BIGINT,
    amount DECIMAL(10, 2) NOT NULL,
    payment_method ENUM('CREDIT_CARD', 'UPI') NOT NULL,
    status ENUM('PENDING', 'COMPLETED', 'FAILED') DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (appointment_id) REFERENCES appointments(id)
);

-- Feedback table (stores feedback from patients)
CREATE TABLE feedback (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    appointment_id BIGINT,
    patient_id BIGINT,
    rating INT CHECK (rating >= 1 AND rating <= 5),
    comments TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (appointment_id) REFERENCES appointments(id),
    FOREIGN KEY (patient_id) REFERENCES users(id)
);

-- Notifications table (stores notifications for users)
CREATE TABLE notifications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT,
    message TEXT NOT NULL,
    type ENUM('APPOINTMENT', 'SESSION', 'PAYMENT') NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Session History table (stores session summaries)
CREATE TABLE session_history (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    session_id BIGINT,
    summary TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (session_id) REFERENCES sessions(id)
);