CREATE TABLE IF NOT EXISTS students (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(80) NOT NULL UNIQUE,
    password_hash VARCHAR(160) NOT NULL,
    role VARCHAR(30) NOT NULL,
    first_name VARCHAR(120) NOT NULL,
    last_name VARCHAR(120) NOT NULL,
    email VARCHAR(160) NOT NULL UNIQUE,
    matriculation_number VARCHAR(80) NOT NULL UNIQUE
);
