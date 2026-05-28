INSERT INTO rooms (id, name, capacity, location)
VALUES
    (1, 'A-101 Lecture Room', 32, 'Main building, 1st floor'),
    (2, 'B-205 Lab Room', 24, 'Science building, 2nd floor'),
    (3, 'C-310 Seminar Room', 16, 'Library wing, 3rd floor')
ON CONFLICT (id) DO NOTHING;

SELECT setval('rooms_id_seq', GREATEST((SELECT MAX(id) FROM rooms), 1));
