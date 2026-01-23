-- Create tables for Hotel Booking System
CREATE TABLE rooms (
                       id SERIAL PRIMARY KEY,
                       room_number VARCHAR(50) NOT NULL,
                       capacity INT NOT NULL,
                       price DECIMAL(10, 2) NOT NULL
);

CREATE TABLE guests (
                        id SERIAL PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        email VARCHAR(255) UNIQUE NOT NULL
);

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
