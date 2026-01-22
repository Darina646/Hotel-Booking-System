tables
CREATE TABLE rooms (
                       id SERIAL PRIMARY KEY,
                       number VARCHAR(10) NOT NULL,
                       price_per_night DOUBLE PRECISION NOT NULL
);

CREATE TABLE guests (
                        id SERIAL PRIMARY KEY,
                        full_name VARCHAR(100) NOT NULL,
                        email VARCHAR(100),
                        phone VARCHAR(30)
);

CREATE TABLE bookings (
                          id SERIAL PRIMARY KEY,
                          guest_id INT REFERENCES guests(id),
                          room_id INT REFERENCES rooms(id),
                          arrival_date DATE NOT NULL,
                          departure_date DATE NOT NULL,
                          total_price DOUBLE PRECISION NOT NULL
);

insert
INSERT INTO rooms (number, price_per_night)
VALUES ('1', 1000), ('2', 4000);

INSERT INTO guests (full_name, email, phone)
VALUES ('Harry P.', 'harry@gmail.com', '+770534758');

INSERT INTO bookings (guest_id, room_id, arrival_date, departure_date, total_price)
VALUES (1, 1, '2026-01-13', '2026-01-18', 75000);



select and join



SELECT
    b.id AS booking_id,
    g.full_name AS guest_name,
    r.number AS room_number,
    b.arrival_date,
    b.departure_date,
    b.total_price
FROM bookings b
         JOIN guests g ON b.guest_id = g.id
         JOIN rooms r ON b.room_id = r.id;

