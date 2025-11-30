USE hotel_management;

-- ----------------------------------------------------------
-- ROLES
-- ----------------------------------------------------------
INSERT INTO roles (role_name, description) VALUES
('CUSTOMER','Regular customer role'),
('STAFF','Hotel staff role'),
('MANAGER','Hotel manager role'),
('SUPERADMIN','System administrator');

-- ----------------------------------------------------------
-- PERMISSIONS
-- ----------------------------------------------------------
INSERT INTO permissions (permission_name, description) VALUES
('manage_rooms','Can add/edit/remove rooms'),
('manage_bookings','Can view/modify bookings'),
('view_reports','Can view reports');

-- ----------------------------------------------------------
-- ROOM TYPES
-- ----------------------------------------------------------
INSERT INTO room_types(type_name, description) VALUES
('Single','Single bed room'),
('Double','Double bed room'),
('Suite','Luxury suite');

-- ----------------------------------------------------------
-- HOTEL BRANCHES
-- ----------------------------------------------------------
INSERT INTO hotel_branches (name, location, contact_number, manager_id, total_rooms, status) VALUES
('Grand Palace Hotel', 'Mumbai, India', '+91-9988776655', NULL, 50, 'active'),
('Ocean View Resort', 'Goa, India', '+91-9977665544', NULL, 30, 'active'),
('Hilltop Retreat', 'Manali, India', '+91-8899776655', NULL, 20, 'inactive');

-- ----------------------------------------------------------
-- MEDIA
-- ----------------------------------------------------------
INSERT INTO media (owner_type, owner_id, file_name, file_type, file_size, file_path, uploaded_by) VALUES
('ROOM', 1, 'room1.jpg', 'image/jpeg', 210000, '/media/rooms/room1.jpg', 1),
('FACILITY', 1, 'spa.jpg', 'image/jpeg', 350000, '/media/facilities/spa.jpg', 2),
('USER', 1, 'profile1.png', 'image/png', 120000, '/media/users/profile1.png', 1),
('BRANCH', 1, 'branch1.png', 'image/png', 150000, '/media/branches/branch1.png', 3);

-- ----------------------------------------------------------
-- ROOMS
-- ----------------------------------------------------------
INSERT INTO rooms (branch_id, type_id, room_number, price_per_night, capacity, status, room_primary_image, description, floor_number) VALUES
(1, 1, '101', 2500.00, 1, 'AVAILABLE', '/media/rooms/r101.jpg', 'Cozy single room', 1),
(1, 2, '102', 3500.00, 2, 'OCCUPIED', '/media/rooms/r102.jpg', 'Double room with balcony', 1),
(1, 3, '201', 7000.00, 4, 'AVAILABLE', '/media/rooms/suite201.jpg', 'Luxury suite', 2),
(2, 1, 'A1', 2200.00, 1, 'RESERVED', '/media/rooms/a1.jpg', 'Basic single room', 1),
(2, 2, 'A2', 3300.00, 2, 'AVAILABLE', '/media/rooms/a2.jpg', 'Double room garden view', 1);

-- ----------------------------------------------------------
-- FACILITIES
-- ----------------------------------------------------------
INSERT INTO facilities (name, type, price, capacity, event_start, event_end, status, facility_primary_image, brochure_document, description, location) VALUES
('Luxury Spa', 'SPA', 1500.00, 20, NULL, NULL, 'AVAILABLE', '/media/facility/spa1.jpg', '/docs/spa.pdf', 'Relaxing spa services', 'Ground Floor'),
('Fitness Center', 'GYM', 0.00, 40, NULL, NULL, 'AVAILABLE', '/media/facility/gym1.jpg', NULL, '24/7 gym access', 'Basement'),
('Infinity Pool', 'POOL', 500.00, 30, NULL, NULL, 'UNAVAILABLE', '/media/facility/pool1.jpg', NULL, 'Outdoor pool', 'Terrace');

-- ----------------------------------------------------------
-- ROLE PERMISSIONS
-- ----------------------------------------------------------
INSERT INTO role_permissions (role_id, permission_id) VALUES
(2, 1),
(2, 2),
(3, 3),
(4, 1), (4, 2), (4, 3);

-- ----------------------------------------------------------
-- USERS
-- ----------------------------------------------------------
INSERT INTO users (role_id, name, email, password_hash, password_salt, phone, profile_image, id_document, notes) VALUES
(1, 'Amit Sharma', 'amit@example.com',
 'd5579c46dfcc7d0d1aab1e8fa6f3b9c0d66ed1f3dbf0a1d4bb2e4d9ad1f8a123', 'S4LT123456', '+91-9991112233',
 '/media/users/u1.jpg', '/media/docs/amit_id.pdf', 'Frequent visitor'),

(2, 'Riya Singh', 'riya.staff@example.com',
 'c84b1d9ee120ac8ff47bdf23445e1bc67af1c82a06bdff233bd718fa91a0bd55', 'H0TELSALT9', '+91-9988776655',
 '/media/users/u2.jpg', NULL, 'Front desk staff'),

(3, 'Manoj Verma', 'manoj.manager@example.com',
 'ab12bc34d45ef6789012ffab33445566abcdeff11223344556677889900aabbc', 'SALT202410', '+91-8877665544',
 NULL, NULL, 'Branch manager'),

(4, 'System Admin', 'admin@example.com',
 '9911aa22bb33cc44dd55ee66ff7788990011aabbccddeeff2233445566778899', 'ADM1N10000', '+91-1234567890',
 NULL, NULL, 'Root administrator');

-- ----------------------------------------------------------
-- BOOKINGS
-- ----------------------------------------------------------
INSERT INTO bookings (customer_id, room_id, branch_id, check_in_date, check_out_date, total_price, payment_status, booking_status, notes) VALUES
(1, 1, 1, '2025-01-05 12:00:00', '2025-01-07 11:00:00', 5000.00, 'PAID', 'CONFIRMED', 'No special requests'),
(1, 3, 1, '2025-02-10 14:00:00', '2025-02-12 11:00:00', 14000.00, 'PENDING', 'CONFIRMED', NULL);

-- ----------------------------------------------------------
-- FACILITY BOOKINGS
-- ----------------------------------------------------------
INSERT INTO facility_bookings (customer_id, facility_id, booking_date, start_time, end_time, time_slot, quantity, total_price, payment_status) VALUES
(1, 1, '2025-01-05', '10:00:00', '11:00:00', 'Morning Slot', 1, 1500.00, 'PAID'),
(1, 2, '2025-01-06', NULL, NULL, 'Full Day', 1, 0.00, 'PENDING');

-- ----------------------------------------------------------
-- PAYMENTS
-- ----------------------------------------------------------
INSERT INTO payments (booking_id, facility_booking_id, customer_id, payment_method, amount_paid, transaction_id, status, payment_receipt) VALUES
(1, NULL, 1, 'UPI', 5000.00, 'TXN123BOOK', 'SUCCESS', '/receipts/r1.pdf'),
(NULL, 1, 1, 'CREDIT_CARD', 1500.00, 'TXN456FAC', 'SUCCESS', '/receipts/r2.pdf');

-- ----------------------------------------------------------
-- FEEDBACK
-- ----------------------------------------------------------
INSERT INTO feedback (customer_id, booking_id, facility_booking_id, rating, comments, feedback_image) VALUES
(1, 1, NULL, 5, 'Excellent stay!', '/media/feedback/f1.jpg'),
(1, NULL, 1, 4, 'Spa was relaxing.', NULL);

-- ----------------------------------------------------------
-- NOTIFICATIONS
-- ----------------------------------------------------------
INSERT INTO notifications (user_id, notification_type, message, sent_via, status) VALUES
(1, 'BOOKING', 'Your booking is confirmed.', 'EMAIL', 'SENT'),
(1, 'FACILITY', 'Spa booking confirmed.', 'SMS', 'SENT');

-- ----------------------------------------------------------
-- SUPPORT TICKETS
-- ----------------------------------------------------------
INSERT INTO support_tickets (customer_id, assigned_staff_id, booking_id, subject, category, status, details) VALUES
(1, 2, 1, 'Room AC Issue', 'MAINTENANCE', 'IN_PROGRESS', 'AC not cooling properly');

-- ----------------------------------------------------------
-- STAFF TASKS
-- ----------------------------------------------------------
INSERT INTO staff_tasks (staff_id, room_id, task_type, status, remarks) VALUES
(2, 2, 'CLEANING', 'IN_PROGRESS', 'Started cleaning work'),
(2, 1, 'MAINTENANCE_CHECK', 'PENDING', NULL);

-- ----------------------------------------------------------
-- ACTIVITY LOG
-- ----------------------------------------------------------
INSERT INTO activity_log (user_id, action, entity_type, entity_id, description, ip_address, device_info) VALUES
(1, 'Created Booking', 'BOOKING', 1, 'User booked room 101', '192.168.1.10', 'Chrome Browser'),
(2, 'Updated Room Status', 'ROOM', 2, 'Staff changed room to OCCUPIED', '192.168.1.22', 'Firefox');

-- ----------------------------------------------------------
-- REPORTS
-- ----------------------------------------------------------
INSERT INTO reports (report_type, generated_by, period, data_summary, report_path) VALUES
('Monthly Revenue', 3, 'Jan-2025', 'Revenue data summary...', '/reports/rev_jan2025.pdf'),
('Customer Feedback', 3, 'Q1-2025', 'Feedback stats...', '/reports/feedback_q1.pdf');

-- ----------type_id------------------------------------------------
-- ROOM FACILITIES
-- ----------------------------------------------------------
INSERT INTO room_facilities (room_id, facility_name, description) VALUES
(1, 'Free WiFi', 'High speed internet'),
(1, 'TV', 'LED Television'),
(2, 'Mini Bar', 'Stocked mini bar'),
(3, 'Jacuzzi', 'Private Jacuzzi');