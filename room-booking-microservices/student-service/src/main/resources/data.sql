INSERT INTO students (id, username, password_hash, role, first_name, last_name, email, matriculation_number)
VALUES
    (1, 'student', '123', 'STUDENT', 'Demo', 'Student', 'student@university.edu', 'S-1001'),
    (2, 'admin', '123', 'ADMIN', 'Demo', 'Admin', 'admin@university.edu', 'A-0001')
ON CONFLICT (id) DO NOTHING;

SELECT setval('students_id_seq', GREATEST((SELECT MAX(id) FROM students), 1));
