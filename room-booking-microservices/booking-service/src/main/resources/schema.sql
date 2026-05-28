CREATE TABLE IF NOT EXISTS bookings (
    id BIGSERIAL PRIMARY KEY,
    room_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    booking_date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    purpose VARCHAR(400) NOT NULL,
    status VARCHAR(30) NOT NULL,
    reject_reason VARCHAR(400),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    CONSTRAINT booking_time_check CHECK (end_time > start_time)
);

CREATE INDEX IF NOT EXISTS idx_bookings_student_id ON bookings (student_id);
CREATE INDEX IF NOT EXISTS idx_bookings_room_time ON bookings (room_id, booking_date, start_time, end_time);
