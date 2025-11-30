    
-- ==========================================================
-- HOTEL MANAGEMENT SYSTEM - FULL DATABASE SCHEMA
-- Custom Salt + SHA-256 Password Hashing Version
-- Database: hotel_management
-- ==========================================================

DROP DATABASE IF EXISTS hotel_management;
CREATE DATABASE hotel_management CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci;
USE hotel_management;

-- ==========================================================
-- 1) ROLES
-- ==========================================================
CREATE TABLE roles (
    role_id INT AUTO_INCREMENT PRIMARY KEY,
    role_name ENUM('CUSTOMER','STAFF','MANAGER','SUPERADMIN') NOT NULL,
    description TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- ==========================================================
-- 2) PERMISSIONS
-- ==========================================================
CREATE TABLE permissions (
    permission_id INT AUTO_INCREMENT PRIMARY KEY,
    permission_name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT
) ENGINE=InnoDB;

-- ==========================================================
-- 3) ROOM TYPES
-- ==========================================================
CREATE TABLE room_types (
    type_id INT AUTO_INCREMENT PRIMARY KEY,
    type_name VARCHAR(100) NOT NULL,
    description TINYTEXT
) ENGINE=InnoDB;

-- ==========================================================
-- 4) HOTEL BRANCHES
-- ==========================================================
CREATE TABLE hotel_branches (
    branch_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    location VARCHAR(255),
    contact_number VARCHAR(30),
    manager_id INT DEFAULT NULL,
    total_rooms INT DEFAULT 0 CHECK (total_rooms >= 0),
    status ENUM('active','inactive') DEFAULT 'active',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE INDEX idx_hotel_branches_location ON hotel_branches(location);

-- ==========================================================
-- 5) MEDIA
-- ==========================================================
CREATE TABLE media (
    media_id INT AUTO_INCREMENT PRIMARY KEY,
    owner_type VARCHAR(50) NULL,
    owner_id INT NULL,
    file_name VARCHAR(255) NOT NULL,
    file_type VARCHAR(100),
    file_size BIGINT NULL CHECK (file_size >= 0),
    file_path VARCHAR(1024),
    file_blob LONGBLOB NULL,
    uploaded_by INT NULL,
    uploaded_at DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE INDEX idx_media_owner ON media(owner_type, owner_id);

-- ==========================================================
-- 6) ROOMS
-- ==========================================================
CREATE TABLE rooms (
    room_id INT AUTO_INCREMENT PRIMARY KEY,
    branch_id INT NULL,
    type_id INT NOT NULL,
    room_number VARCHAR(20) NOT NULL,
    price_per_night DOUBLE(10,2) DEFAULT 0.00 CHECK(price_per_night >= 0),
    capacity INT DEFAULT 1 CHECK(capacity > 0),
    status ENUM('AVAILABLE','RESERVED','OCCUPIED','MAINTENANCE') DEFAULT 'AVAILABLE',
    room_primary_image VARCHAR(1024) NULL,
    description TINYTEXT,
    floor_number INT NULL,
    last_cleaned DATETIME NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (branch_id) REFERENCES hotel_branches(branch_id) ON DELETE SET NULL,
    FOREIGN KEY (type_id) REFERENCES room_types(type_id) ON DELETE RESTRICT,
    UNIQUE KEY uq_room_branch_number (branch_id, room_number)
) ENGINE=InnoDB;

CREATE INDEX idx_rooms_branch ON rooms(branch_id);
CREATE INDEX idx_rooms_type ON rooms(type_id);

-- ==========================================================
-- 7) FACILITIES
-- ==========================================================
CREATE TABLE facilities (
    facility_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(150) NOT NULL,
    type ENUM('SPA','GYM','POOL','BANQUET','MEETING_HALL','RESTAURANT','OTHER') NOT NULL,
    price DOUBLE(10,2) DEFAULT 0.00 CHECK (price >= 0),
    capacity INT DEFAULT 1 CHECK(capacity > 0),
    event_start DATETIME NULL,
    event_end DATETIME NULL,
    status ENUM('AVAILABLE','UNAVAILABLE') DEFAULT 'AVAILABLE',
    facility_primary_image VARCHAR(1024) NULL,
    brochure_document VARCHAR(1024) NULL,
    description TINYTEXT,
    location VARCHAR(255),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

CREATE INDEX idx_facilities_type ON facilities(type);

-- ==========================================================
-- 8) ROLE PERMISSIONS
-- ==========================================================
CREATE TABLE role_permissions (
    rp_id INT AUTO_INCREMENT PRIMARY KEY,
    role_id INT NOT NULL,
    permission_id INT NOT NULL,
    FOREIGN KEY (role_id) REFERENCES roles(role_id) ON DELETE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES permissions(permission_id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ==========================================================
-- 9) USERS (SHA-256 SALT)
-- ==========================================================
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    role_id INT NOT NULL,
    name VARCHAR(150) NOT NULL,
    email VARCHAR(70) NOT NULL UNIQUE,
    password_hash CHAR(64) NOT NULL,
    password_salt CHAR(10) NOT NULL,
    phone VARCHAR(30),
    profile_image VARCHAR(1024) NULL,
    id_document VARCHAR(1024) NULL,
    notes TINYTEXT,
    status ENUM('active','inactive','banned') DEFAULT 'active',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (role_id) REFERENCES roles(role_id)
) ENGINE=InnoDB;

CREATE INDEX idx_users_role ON users(role_id);
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_status ON users(status);

-- ==========================================================
-- 10) BOOKINGS
-- ==========================================================
CREATE TABLE bookings (
    booking_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    room_id INT NOT NULL,
    branch_id INT NULL,
    check_in_date DATETIME NOT NULL,
    check_out_date DATETIME NOT NULL,
    total_price DOUBLE(10,2) DEFAULT 0.00 CHECK(total_price >= 0),
    payment_status ENUM('PENDING','PAID','FAILED') DEFAULT 'PENDING',
    booking_status ENUM('CONFIRMED','CANCELLED','COMPLETED') DEFAULT 'CONFIRMED',
    notes TINYTEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (customer_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (room_id) REFERENCES rooms(room_id) ON DELETE RESTRICT,
    FOREIGN KEY (branch_id) REFERENCES hotel_branches(branch_id) ON DELETE SET NULL
) ENGINE=InnoDB;

CREATE INDEX idx_booking_customer ON bookings(customer_id);
CREATE INDEX idx_booking_room ON bookings(room_id);

-- ==========================================================
-- 11) FACILITY BOOKINGS
-- ==========================================================
CREATE TABLE facility_bookings (
    facility_booking_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    facility_id INT NOT NULL,
    booking_date DATE NOT NULL,
    start_time TIME NULL,
    end_time TIME NULL,
    time_slot VARCHAR(100) NULL,
    quantity INT DEFAULT 1 CHECK(quantity > 0),
    total_price DOUBLE(10,2) DEFAULT 0.00 CHECK(total_price >= 0),
    payment_status ENUM('PENDING','PAID','FAILED') DEFAULT 'PENDING',
    booking_status ENUM('CONFIRMED','CANCELLED','COMPLETED') DEFAULT 'CONFIRMED',
    notes TINYTEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,

    FOREIGN KEY (customer_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (facility_id) REFERENCES facilities(facility_id) ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE INDEX idx_fac_booking_customer ON facility_bookings(customer_id);

-- ==========================================================
-- 12) PAYMENTS
-- ==========================================================
CREATE TABLE payments (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    booking_id INT NULL,
    facility_booking_id INT NULL,
    customer_id INT NOT NULL,
    payment_method ENUM(
        'CREDIT_CARD','DEBIT_CARD','UPI','PAYPAL','STRIPE',
        'BANK_TRANSFER','CASH','OTHER'
    ) DEFAULT 'CREDIT_CARD',
    amount_paid DOUBLE(10,2) CHECK(amount_paid >= 0),
    payment_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    transaction_id VARCHAR(255),
    status ENUM('SUCCESS','PENDING','FAILED') DEFAULT 'SUCCESS',
    payment_receipt VARCHAR(1024),
    notes TINYTEXT,
    FOREIGN KEY (booking_id) REFERENCES bookings(booking_id) ON DELETE SET NULL,
    FOREIGN KEY (facility_booking_id) REFERENCES facility_bookings(facility_booking_id) ON DELETE SET NULL,
    FOREIGN KEY (customer_id) REFERENCES users(user_id) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE INDEX idx_pay_customer ON payments(customer_id);

-- ==========================================================
-- 13) FEEDBACK
-- ==========================================================
CREATE TABLE feedback (
    feedback_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    booking_id INT NULL,
    facility_booking_id INT NULL,
    rating TINYINT UNSIGNED CHECK (rating BETWEEN 1 AND 5),
    comments TINYTEXT,
    feedback_image VARCHAR(1024),
    submission_date DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (booking_id) REFERENCES bookings(booking_id) ON DELETE SET NULL,
    FOREIGN KEY (facility_booking_id) REFERENCES facility_bookings(facility_booking_id) ON DELETE SET NULL
) ENGINE=InnoDB;

-- ==========================================================
-- 14) NOTIFICATIONS
-- ==========================================================
CREATE TABLE notifications (
    notification_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    notification_type VARCHAR(50),
    message TINYTEXT,
    sent_via ENUM('EMAIL','SMS','PUSH') DEFAULT 'EMAIL',
    status ENUM('PENDING','SENT','FAILED') DEFAULT 'PENDING',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ==========================================================
-- 15) SUPPORT TICKETS
-- ==========================================================
CREATE TABLE support_tickets (
    ticket_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    assigned_staff_id INT NULL,
    booking_id INT NULL,
    facility_booking_id INT NULL,
    subject VARCHAR(255),
    category VARCHAR(100),
    status ENUM('OPEN','IN_PROGRESS','RESOLVED','CLOSED') DEFAULT 'OPEN',
    details TINYTEXT,
    attachment VARCHAR(1024),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (assigned_staff_id) REFERENCES users(user_id) ON DELETE SET NULL,
    FOREIGN KEY (booking_id) REFERENCES bookings(booking_id) ON DELETE SET NULL,
    FOREIGN KEY (facility_booking_id) REFERENCES facility_bookings(facility_booking_id) ON DELETE SET NULL
) ENGINE=InnoDB;

-- ==========================================================
-- 16) STAFF TASKS
-- ==========================================================
CREATE TABLE staff_tasks (
    task_id INT AUTO_INCREMENT PRIMARY KEY,
    staff_id INT NOT NULL,
    room_id INT NULL,
    task_type VARCHAR(100),
    status ENUM('PENDING','IN_PROGRESS','COMPLETED') DEFAULT 'PENDING',
    assigned_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    completed_at DATETIME,
    remarks TINYTEXT,
    FOREIGN KEY (staff_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (room_id) REFERENCES rooms(room_id) ON DELETE SET NULL
) ENGINE=InnoDB;

-- ==========================================================
-- 17) ACTIVITY LOG
-- ==========================================================
CREATE TABLE activity_log (
    log_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NULL,
    action VARCHAR(200),
    entity_type VARCHAR(100),
    entity_id INT NULL,
    description TINYTEXT,
    ip_address VARCHAR(50),
    device_info VARCHAR(255),
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE SET NULL
) ENGINE=InnoDB;

-- ==========================================================
-- 18) REPORTS
-- ==========================================================
CREATE TABLE reports (
    report_id INT AUTO_INCREMENT PRIMARY KEY,
    report_type VARCHAR(100),
    generated_by INT NULL,
    generated_on DATETIME DEFAULT CURRENT_TIMESTAMP,
    period VARCHAR(100),
    data_summary TINYTEXT,
    report_path VARCHAR(1024),
    FOREIGN KEY (generated_by) REFERENCES users(user_id) ON DELETE SET NULL
) ENGINE=InnoDB;

-- ==========================================================
-- 19) ROOM FACILITIES
-- ==========================================================
CREATE TABLE room_facilities (
    rf_id INT AUTO_INCREMENT PRIMARY KEY,
    room_id INT NOT NULL,
    facility_name VARCHAR(150),
    description VARCHAR(255),
    FOREIGN KEY (room_id) REFERENCES rooms(room_id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ==========================================================
-- 20) ADDITIONAL INDEXESusersrolesroleshotel_brancheshotel_branchesroomsroom_typestype_id
-- ==========================================================
CREATE INDEX idx_bookings_dates ON bookings(check_in_date, check_out_date);
CREATE INDEX idx_facilities_status ON facilities(status);