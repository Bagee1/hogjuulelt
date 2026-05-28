CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL
);

CREATE TABLE IF NOT EXISTS rooms (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    capacity INT NOT NULL,
    location VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS bookings (
    id SERIAL PRIMARY KEY,
    room_id INT NOT NULL REFERENCES rooms(id),
    user_id INT NOT NULL REFERENCES users(id),
    booking_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    purpose VARCHAR(255) NOT NULL,
    status VARCHAR(20) NOT NULL,
    reject_reason VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS audit_logs (
    id SERIAL PRIMARY KEY,
    booking_id INT REFERENCES bookings(id),
    message VARCHAR(255) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO users (username, password, role)
SELECT 'student', '123', 'STUDENT'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'student');

INSERT INTO users (username, password, role)
SELECT 'admin', '123', 'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'admin');

INSERT INTO rooms (name, capacity, location)
SELECT 'Lab 201', 30, 'Main Building'
WHERE NOT EXISTS (SELECT 1 FROM rooms WHERE name = 'Lab 201');

INSERT INTO rooms (name, capacity, location)
SELECT 'Seminar Room 105', 20, 'Library Building'
WHERE NOT EXISTS (SELECT 1 FROM rooms WHERE name = 'Seminar Room 105');

INSERT INTO rooms (name, capacity, location)
SELECT 'Conference Hall', 80, 'Administration Building'
WHERE NOT EXISTS (SELECT 1 FROM rooms WHERE name = 'Conference Hall');
