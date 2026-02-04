-- rooms table
CREATE TABLE rooms (
                       id SERIAL PRIMARY KEY,
                       room_number VARCHAR(50) NOT NULL,
                       capacity INT NOT NULL,
                       price DECIMAL(10, 2) NOT NULL,
                       category VARCHAR(50)  -- Added the category column
);

-- guests table
CREATE TABLE guests (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        email VARCHAR(255) UNIQUE NOT NULL,
                        role VARCHAR(50)  -- Added role column to distinguish admin/user
);

-- bookings table
CREATE TABLE bookings (
                          id SERIAL PRIMARY KEY,
                          guest_id INT NOT NULL,
                          room_id INT NOT NULL,
                          start_date DATE NOT NULL,
                          end_date DATE NOT NULL,
                          total_price DECIMAL(10, 2) NOT NULL,
                          FOREIGN KEY (guest_id) REFERENCES guests(id),
                          FOREIGN KEY (room_id) REFERENCES rooms(id),
                          CHECK (start_date < end_date)
);

-- check available rooms
SELECT * FROM rooms r
WHERE NOT EXISTS (
    SELECT 1 FROM bookings b
    WHERE b.room_id = r.id
      AND (
        (b.start_date BETWEEN '2026-10-01' AND '2026-10-05')
            OR (b.end_date BETWEEN '2026-10-01' AND '2026-10-05')
        )
);

-- get all bookings
SELECT * FROM bookings;
