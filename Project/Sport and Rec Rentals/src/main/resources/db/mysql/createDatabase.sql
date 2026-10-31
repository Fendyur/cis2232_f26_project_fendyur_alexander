# For hccis.ca version of the database
# DROP DATABASE IF EXISTS bjmac_squash_skills_w26;
# CREATE DATABASE bjmac_squash_skills_w26;
# use bjmac_squash_skills_w26;

#For localhost
DROP DATABASE IF EXISTS cis2232_sport_and_rec_rentals;
CREATE DATABASE cis2232_sport_and_rec_rentals;
use cis2232_sport_and_rec_rentals;

-- ------------------------------------------------------------------------------
-- Note the table below to hold data associated with your project.  Expect one
-- table with 7-9 fields.
-- Booking table for the Sport and Rec Rentals facility.
-- ------------------------------------------------------------------------------

CREATE TABLE booking (
                         id                  INT             NOT NULL AUTO_INCREMENT COMMENT 'Unique identifier',
                         groupNum            INT             NOT NULL COMMENT 'How many people are attending',
                         isBirthday          BOOLEAN         NOT NULL DEFAULT FALSE COMMENT 'Birthday decoration and cake',
                         equipmentNeeded     BOOLEAN         NOT NULL DEFAULT FALSE COMMENT 'Do they need equipment',
                         roomType            VARCHAR(50)     NOT NULL COMMENT 'Party room, gym, small rec room',
                         bookingDate         VARCHAR(10)     NOT NULL COMMENT 'yyyy-mm-dd',
                         startTime           VARCHAR(10)     NOT NULL COMMENT 'Booking start time',
                         endTime             VARCHAR(10)     NOT NULL COMMENT 'Booking end time',
                         bookingName         VARCHAR(100)    NOT NULL COMMENT 'Name for the booking',
                         phone               VARCHAR(20)     NOT NULL COMMENT 'Phone number for the booking',
                         email               VARCHAR(100)    NOT NULL COMMENT 'Email for the booking',
                         basePrice           DOUBLE          NOT NULL DEFAULT 0 COMMENT 'Cost of the room',
                         tax                 DOUBLE          NOT NULL DEFAULT 0 COMMENT 'Standard tax fee (15%)',
                         totalPrice          DOUBLE          NOT NULL DEFAULT 0 COMMENT 'Price for what has been booked',
                         birthdayCost        DOUBLE          NOT NULL DEFAULT 0 COMMENT 'Birthday cost',
                         PRIMARY KEY (id)
);

-- Sample data. tax = 15% of (basePrice + birthdayCost), totalPrice = basePrice + birthdayCost + tax
INSERT INTO booking
(groupNum, isBirthday, equipmentNeeded, roomType, bookingDate, startTime, endTime,
 bookingName, phone, email, basePrice, tax, totalPrice, birthdayCost)
VALUES
(12, TRUE,  FALSE, 'Party Room',     '2026-10-03', '13:00', '16:00', 'Jane Doe',      '902-555-1234', 'jane.doe@example.com',   150.00, 33.75, 258.75, 75.00),
(20, FALSE, TRUE,  'Gym',            '2026-10-05', '18:00', '20:00', 'Rovers Hockey', '902-555-4321', 'coach@rovers.example',   200.00, 30.00, 230.00,  0.00),
(6,  FALSE, FALSE, 'Small Rec Room', '2026-10-08', '10:00', '12:00', 'Book Club',     '902-555-9876', 'books@example.com',       60.00,  9.00,  69.00,  0.00),
(15, TRUE,  TRUE,  'Gym',            '2026-10-10', '14:00', '17:00', 'Sam Lee',       '902-555-2468', 'sam.lee@example.com',    200.00, 41.25, 316.25, 75.00);

# Claude (AI) 2026-10-29 Added sample bookings for testing the monthly room bookings report.
# October 2026 (with the rows above) has 12 bookings over 8 of the 12 room/add-on variations;
# Party Room + Equipment, Gym + Birthday, Small Rec Room + Equipment and
# Small Rec Room + Equipment + Birthday have 0 bookings. September and November 2026 rows
# check that other months are not counted (2026-11-01 is right after the end of October).
INSERT INTO booking
(groupNum, isBirthday, equipmentNeeded, roomType, bookingDate, startTime, endTime,
 bookingName, phone, email, basePrice, tax, totalPrice, birthdayCost)
VALUES
(18, TRUE,  FALSE, 'Party Room',     '2026-10-12', '12:00', '15:00', 'Maria Chen',    '902-555-1111', 'maria.chen@example.com', 150.00, 33.75, 258.75, 75.00),
(25, TRUE,  TRUE,  'Party Room',     '2026-10-14', '15:00', '18:00', 'Liam Walsh',    '902-555-2222', 'liam.walsh@example.com', 150.00, 33.75, 258.75, 75.00),
(30, FALSE, FALSE, 'Party Room',     '2026-10-17', '19:00', '22:00', 'Staff Social',  '902-555-3333', 'social@example.com',     150.00, 22.50, 172.50,  0.00),
(16, FALSE, TRUE,  'Gym',            '2026-10-19', '17:00', '19:00', 'Island Hoops',  '902-555-4444', 'hoops@example.com',      200.00, 30.00, 230.00,  0.00),
(22, FALSE, TRUE,  'Gym',            '2026-10-24', '09:00', '11:00', 'Rovers Hockey', '902-555-4321', 'coach@rovers.example',   200.00, 30.00, 230.00,  0.00),
(10, FALSE, FALSE, 'Gym',            '2026-10-26', '20:00', '21:00', 'Pickup Soccer', '902-555-5555', 'pickup@example.com',     200.00, 30.00, 230.00,  0.00),
(8,  FALSE, FALSE, 'Small Rec Room', '2026-10-28', '18:00', '20:00', 'Chess Club',    '902-555-6666', 'chess@example.com',       60.00,  9.00,  69.00,  0.00),
(9,  TRUE,  FALSE, 'Small Rec Room', '2026-10-31', '13:00', '15:00', 'Ava Brown',     '902-555-7777', 'ava.brown@example.com',   60.00, 20.25, 155.25, 75.00),
(14, FALSE, TRUE,  'Gym',            '2026-09-30', '18:00', '20:00', 'Island Hoops',  '902-555-4444', 'hoops@example.com',      200.00, 30.00, 230.00,  0.00),
(20, TRUE,  FALSE, 'Party Room',     '2026-11-01', '13:00', '16:00', 'Noah Gallant',  '902-555-8888', 'noah.g@example.com',     150.00, 33.75, 258.75, 75.00),
(6,  FALSE, TRUE,  'Small Rec Room', '2026-11-07', '10:00', '12:00', 'Yoga Group',    '902-555-9999', 'yoga@example.com',        60.00,  9.00,  69.00,  0.00);

-- Claude (AI) 2026-10-31 Added sample bookings for 2026-11 to 2028-09 (at least 6 per month)
-- for report testing. Same pricing as above: Party Room 150.00, Gym 200.00, Small Rec Room 60.00,
-- birthdayCost 75.00 when isBirthday, equipment has no charge,
-- tax = 15% of (basePrice + birthdayCost), totalPrice = basePrice + birthdayCost + tax.
-- 2026-11
INSERT INTO booking
(groupNum, isBirthday, equipmentNeeded, roomType, bookingDate, startTime, endTime,
 bookingName, phone, email, basePrice, tax, totalPrice, birthdayCost)
VALUES
(19, TRUE,  TRUE,  'Gym',            '2026-11-08', '21:00', '22:00', 'Ella Pineau',        '902-555-3244', 'ella.pineau@example.org',           200.00, 41.25, 316.25, 75.00),
(10, TRUE,  TRUE,  'Small Rec Room', '2026-11-13', '12:00', '15:00', 'Ben Morrison',       '902-555-9989', 'ben.morrison@example.com',           60.00, 20.25, 155.25, 75.00),
(24, FALSE, FALSE, 'Gym',            '2026-11-17', '14:00', '16:00', 'Lucas Doucette',     '902-555-5384', 'lucas.doucette@example.org',        200.00, 30.00, 230.00,  0.00),
(22, TRUE,  FALSE, 'Gym',            '2026-11-21', '13:00', '14:00', 'Ella Pineau',        '902-555-3244', 'ella.pineau@example.org',           200.00, 41.25, 316.25, 75.00),
(25, TRUE,  FALSE, 'Party Room',     '2026-11-26', '14:00', '17:00', 'Lucas Doucette',     '902-555-5384', 'lucas.doucette@example.org',        150.00, 33.75, 258.75, 75.00),
(16, FALSE, TRUE,  'Gym',            '2026-11-30', '10:00', '12:00', 'Badminton Club',     '902-555-1438', 'badmintonclub@example.com',         200.00, 30.00, 230.00,  0.00);

-- 2026-12
INSERT INTO booking
(groupNum, isBirthday, equipmentNeeded, roomType, bookingDate, startTime, endTime,
 bookingName, phone, email, basePrice, tax, totalPrice, birthdayCost)
VALUES
(7,  FALSE, TRUE,  'Small Rec Room', '2026-12-03', '20:00', '22:00', 'Yoga Group',         '902-555-2459', 'yogagroup@example.org',              60.00,  9.00,  69.00,  0.00),
(20, FALSE, FALSE, 'Party Room',     '2026-12-05', '12:00', '14:00', 'Holiday Mixer',      '902-555-7427', 'holidaymixer@example.org',          150.00, 22.50, 172.50,  0.00),
(35, TRUE,  TRUE,  'Party Room',     '2026-12-07', '14:00', '18:00', 'Ethan MacLeod',      '902-555-7942', 'ethan.macleod@example.com',         150.00, 33.75, 258.75, 75.00),
(11, FALSE, TRUE,  'Party Room',     '2026-12-10', '17:00', '21:00', 'Holiday Mixer',      '902-555-7427', 'holidaymixer@example.org',          150.00, 22.50, 172.50,  0.00),
(10, FALSE, FALSE, 'Small Rec Room', '2026-12-13', '16:00', '19:00', 'Knitting Circle',    '902-555-7965', 'knittingcircle@example.org',         60.00,  9.00,  69.00,  0.00),
(5,  TRUE,  FALSE, 'Small Rec Room', '2026-12-18', '15:00', '18:00', 'Ben Morrison',       '902-555-9989', 'ben.morrison@example.com',           60.00, 20.25, 155.25, 75.00),
(8,  TRUE,  FALSE, 'Small Rec Room', '2026-12-20', '14:00', '16:00', 'Owen Campbell',      '902-555-4819', 'owen.campbell@example.org',          60.00, 20.25, 155.25, 75.00),
(9,  FALSE, TRUE,  'Small Rec Room', '2026-12-22', '17:00', '19:00', 'Emma Richard',       '902-555-4635', 'emma.richard@example.org',           60.00,  9.00,  69.00,  0.00),
(18, FALSE, TRUE,  'Party Room',     '2026-12-26', '08:00', '10:00', 'Family Reunion',     '902-555-4919', 'familyreunion@example.org',         150.00, 22.50, 172.50,  0.00),
(23, TRUE,  TRUE,  'Gym',            '2026-12-29', '09:00', '12:00', 'Grace Hennessey',    '902-555-3815', 'grace.hennessey@example.com',       200.00, 41.25, 316.25, 75.00);

-- 2027-01
INSERT INTO booking
(groupNum, isBirthday, equipmentNeeded, roomType, bookingDate, startTime, endTime,
 bookingName, phone, email, basePrice, tax, totalPrice, birthdayCost)
VALUES
(27, FALSE, TRUE,  'Gym',            '2027-01-01', '14:00', '16:00', 'Volleyball League',  '902-555-2019', 'volleyballleague@example.com',      200.00, 30.00, 230.00,  0.00),
(12, FALSE, FALSE, 'Small Rec Room', '2027-01-10', '18:00', '20:00', 'Knitting Circle',    '902-555-7965', 'knittingcircle@example.org',         60.00,  9.00,  69.00,  0.00),
(21, FALSE, FALSE, 'Gym',            '2027-01-15', '09:00', '11:00', 'Volleyball League',  '902-555-2019', 'volleyballleague@example.com',      200.00, 30.00, 230.00,  0.00),
(27, TRUE,  TRUE,  'Party Room',     '2027-01-19', '17:00', '20:00', 'Chloe Gaudet',       '902-555-7246', 'chloe.gaudet@example.org',          150.00, 33.75, 258.75, 75.00),
(24, TRUE,  FALSE, 'Gym',            '2027-01-23', '15:00', '18:00', 'Henry Wedge',        '902-555-0900', 'henry.wedge@example.com',           200.00, 41.25, 316.25, 75.00),
(8,  TRUE,  TRUE,  'Small Rec Room', '2027-01-26', '19:00', '21:00', 'Dylan Arsenault',    '902-555-6512', 'dylan.arsenault@example.com',        60.00, 20.25, 155.25, 75.00);

-- 2027-02
INSERT INTO booking
(groupNum, isBirthday, equipmentNeeded, roomType, bookingDate, startTime, endTime,
 bookingName, phone, email, basePrice, tax, totalPrice, birthdayCost)
VALUES
(34, TRUE,  FALSE, 'Party Room',     '2027-02-02', '09:00', '12:00', 'Ruby McInnis',       '902-555-3363', 'ruby.mcinnis@example.org',          150.00, 33.75, 258.75, 75.00),
(10, FALSE, FALSE, 'Party Room',     '2027-02-05', '17:00', '21:00', 'Family Reunion',     '902-555-4919', 'familyreunion@example.org',         150.00, 22.50, 172.50,  0.00),
(22, TRUE,  FALSE, 'Gym',            '2027-02-07', '13:00', '16:00', 'Carter Murphy',      '902-555-5715', 'carter.murphy@example.org',         200.00, 41.25, 316.25, 75.00),
(8,  TRUE,  TRUE,  'Small Rec Room', '2027-02-12', '18:00', '19:00', 'Claire Robichaud',   '902-555-8973', 'claire.robichaud@example.com',       60.00, 20.25, 155.25, 75.00),
(8,  FALSE, TRUE,  'Small Rec Room', '2027-02-13', '11:00', '12:00', 'Book Club',          '902-555-7883', 'bookclub@example.org',               60.00,  9.00,  69.00,  0.00),
(28, TRUE,  FALSE, 'Party Room',     '2027-02-18', '10:00', '13:00', 'Lucas Doucette',     '902-555-5384', 'lucas.doucette@example.org',        150.00, 33.75, 258.75, 75.00),
(22, FALSE, TRUE,  'Gym',            '2027-02-20', '14:00', '16:00', 'Volleyball League',  '902-555-2019', 'volleyballleague@example.com',      200.00, 30.00, 230.00,  0.00),
(23, FALSE, FALSE, 'Gym',            '2027-02-23', '17:00', '20:00', 'Chloe Gaudet',       '902-555-7246', 'chloe.gaudet@example.org',          200.00, 30.00, 230.00,  0.00),
(12, FALSE, FALSE, 'Small Rec Room', '2027-02-25', '11:00', '12:00', 'Photography Club',   '902-555-1450', 'photographyclub@example.org',        60.00,  9.00,  69.00,  0.00);

-- 2027-03
INSERT INTO booking
(groupNum, isBirthday, equipmentNeeded, roomType, bookingDate, startTime, endTime,
 bookingName, phone, email, basePrice, tax, totalPrice, birthdayCost)
VALUES
(21, TRUE,  TRUE,  'Party Room',     '2027-03-01', '09:00', '13:00', 'Abby Doyle',         '902-555-1134', 'abby.doyle@example.org',            150.00, 33.75, 258.75, 75.00),
(11, TRUE,  FALSE, 'Small Rec Room', '2027-03-06', '14:00', '17:00', 'Sophie MacDonald',   '902-555-3423', 'sophie.macdonald@example.org',       60.00, 20.25, 155.25, 75.00),
(10, TRUE,  TRUE,  'Gym',            '2027-03-08', '18:00', '19:00', 'Ruby McInnis',       '902-555-3363', 'ruby.mcinnis@example.org',          200.00, 41.25, 316.25, 75.00),
(22, FALSE, TRUE,  'Party Room',     '2027-03-12', '17:00', '20:00', 'Holiday Mixer',      '902-555-7427', 'holidaymixer@example.org',          150.00, 22.50, 172.50,  0.00),
(34, FALSE, FALSE, 'Party Room',     '2027-03-14', '13:00', '17:00', 'Olivia Arsenault',   '902-555-6284', 'olivia.arsenault@example.org',      150.00, 22.50, 172.50,  0.00),
(30, TRUE,  TRUE,  'Gym',            '2027-03-17', '15:00', '17:00', 'Ella Pineau',        '902-555-3244', 'ella.pineau@example.org',           200.00, 41.25, 316.25, 75.00),
(10, TRUE,  TRUE,  'Small Rec Room', '2027-03-20', '12:00', '15:00', 'Carter Murphy',      '902-555-5715', 'carter.murphy@example.org',          60.00, 20.25, 155.25, 75.00),
(11, FALSE, FALSE, 'Gym',            '2027-03-23', '08:00', '09:00', 'Pickup Soccer',      '902-555-1899', 'pickupsoccer@example.org',          200.00, 30.00, 230.00,  0.00),
(33, TRUE,  FALSE, 'Party Room',     '2027-03-25', '09:00', '11:00', 'Dylan Arsenault',    '902-555-6512', 'dylan.arsenault@example.com',       150.00, 33.75, 258.75, 75.00),
(26, TRUE,  TRUE,  'Party Room',     '2027-03-28', '08:00', '11:00', 'Grace Hennessey',    '902-555-3815', 'grace.hennessey@example.com',       150.00, 33.75, 258.75, 75.00);

-- 2027-04
INSERT INTO booking
(groupNum, isBirthday, equipmentNeeded, roomType, bookingDate, startTime, endTime,
 bookingName, phone, email, basePrice, tax, totalPrice, birthdayCost)
VALUES
(29, FALSE, TRUE,  'Gym',            '2027-04-03', '16:00', '18:00', 'Rovers Hockey',      '902-555-4446', 'rovershockey@example.com',          200.00, 30.00, 230.00,  0.00),
(6,  FALSE, TRUE,  'Small Rec Room', '2027-04-07', '10:00', '12:00', 'Knitting Circle',    '902-555-7965', 'knittingcircle@example.org',         60.00,  9.00,  69.00,  0.00),
(8,  FALSE, FALSE, 'Small Rec Room', '2027-04-14', '10:00', '11:00', 'Chloe Gaudet',       '902-555-7246', 'chloe.gaudet@example.org',           60.00,  9.00,  69.00,  0.00),
(12, TRUE,  FALSE, 'Small Rec Room', '2027-04-16', '12:00', '13:00', 'Isaac Peters',       '902-555-0497', 'isaac.peters@example.com',           60.00, 20.25, 155.25, 75.00),
(28, FALSE, FALSE, 'Party Room',     '2027-04-25', '17:00', '20:00', 'Holiday Mixer',      '902-555-7427', 'holidaymixer@example.org',          150.00, 22.50, 172.50,  0.00),
(14, TRUE,  FALSE, 'Gym',            '2027-04-27', '08:00', '09:00', 'Mia Fraser',         '902-555-1633', 'mia.fraser@example.com',            200.00, 41.25, 316.25, 75.00);

-- 2027-05
INSERT INTO booking
(groupNum, isBirthday, equipmentNeeded, roomType, bookingDate, startTime, endTime,
 bookingName, phone, email, basePrice, tax, totalPrice, birthdayCost)
VALUES
(14, FALSE, TRUE,  'Party Room',     '2027-05-04', '16:00', '18:00', 'Olivia Arsenault',   '902-555-6284', 'olivia.arsenault@example.org',      150.00, 22.50, 172.50,  0.00),
(8,  FALSE, TRUE,  'Small Rec Room', '2027-05-08', '09:00', '10:00', 'Knitting Circle',    '902-555-7965', 'knittingcircle@example.org',         60.00,  9.00,  69.00,  0.00),
(23, TRUE,  FALSE, 'Party Room',     '2027-05-11', '08:00', '12:00', 'Lucas Doucette',     '902-555-5384', 'lucas.doucette@example.org',        150.00, 33.75, 258.75, 75.00),
(23, FALSE, TRUE,  'Party Room',     '2027-05-16', '16:00', '19:00', 'Emma Richard',       '902-555-4635', 'emma.richard@example.org',          150.00, 22.50, 172.50,  0.00),
(11, TRUE,  FALSE, 'Small Rec Room', '2027-05-24', '10:00', '12:00', 'Theo Ramsay',        '902-555-0229', 'theo.ramsay@example.com',            60.00, 20.25, 155.25, 75.00),
(9,  TRUE,  TRUE,  'Small Rec Room', '2027-05-26', '15:00', '16:00', 'Chloe Gaudet',       '902-555-7246', 'chloe.gaudet@example.org',           60.00, 20.25, 155.25, 75.00);

-- 2027-06
INSERT INTO booking
(groupNum, isBirthday, equipmentNeeded, roomType, bookingDate, startTime, endTime,
 bookingName, phone, email, basePrice, tax, totalPrice, birthdayCost)
VALUES
(12, FALSE, FALSE, 'Gym',            '2027-06-04', '08:00', '11:00', 'Badminton Club',     '902-555-1438', 'badmintonclub@example.com',         200.00, 30.00, 230.00,  0.00),
(21, TRUE,  TRUE,  'Party Room',     '2027-06-09', '19:00', '22:00', 'Carter Murphy',      '902-555-5715', 'carter.murphy@example.org',         150.00, 33.75, 258.75, 75.00),
(11, FALSE, FALSE, 'Small Rec Room', '2027-06-11', '10:00', '12:00', 'Yoga Group',         '902-555-2459', 'yogagroup@example.org',              60.00,  9.00,  69.00,  0.00),
(21, TRUE,  FALSE, 'Gym',            '2027-06-16', '15:00', '17:00', 'Emma Richard',       '902-555-4635', 'emma.richard@example.org',          200.00, 41.25, 316.25, 75.00),
(15, TRUE,  TRUE,  'Gym',            '2027-06-21', '11:00', '13:00', 'Olivia Arsenault',   '902-555-6284', 'olivia.arsenault@example.org',      200.00, 41.25, 316.25, 75.00),
(17, FALSE, TRUE,  'Gym',            '2027-06-26', '10:00', '13:00', 'Dodgeball Crew',     '902-555-7044', 'dodgeballcrew@example.com',         200.00, 30.00, 230.00,  0.00);

-- 2027-07
INSERT INTO booking
(groupNum, isBirthday, equipmentNeeded, roomType, bookingDate, startTime, endTime,
 bookingName, phone, email, basePrice, tax, totalPrice, birthdayCost)
VALUES
(32, FALSE, FALSE, 'Party Room',     '2027-07-03', '18:00', '20:00', 'Chloe Gaudet',       '902-555-7246', 'chloe.gaudet@example.org',          150.00, 22.50, 172.50,  0.00),
(16, FALSE, FALSE, 'Party Room',     '2027-07-06', '12:00', '16:00', 'Baby Shower',        '902-555-5999', 'babyshower@example.com',            150.00, 22.50, 172.50,  0.00),
(22, TRUE,  TRUE,  'Party Room',     '2027-07-07', '15:00', '18:00', 'Sophie MacDonald',   '902-555-3423', 'sophie.macdonald@example.org',      150.00, 33.75, 258.75, 75.00),
(22, FALSE, TRUE,  'Gym',            '2027-07-13', '11:00', '14:00', 'Lucas Doucette',     '902-555-5384', 'lucas.doucette@example.org',        200.00, 30.00, 230.00,  0.00),
(30, TRUE,  FALSE, 'Gym',            '2027-07-17', '09:00', '12:00', 'Isaac Peters',       '902-555-0497', 'isaac.peters@example.com',          200.00, 41.25, 316.25, 75.00),
(28, TRUE,  TRUE,  'Gym',            '2027-07-19', '14:00', '15:00', 'Maya Sutherland',    '902-555-3906', 'maya.sutherland@example.org',       200.00, 41.25, 316.25, 75.00),
(7,  TRUE,  FALSE, 'Small Rec Room', '2027-07-24', '16:00', '17:00', 'Zoe Matheson',       '902-555-3879', 'zoe.matheson@example.org',           60.00, 20.25, 155.25, 75.00),
(12, FALSE, FALSE, 'Small Rec Room', '2027-07-25', '14:00', '16:00', 'Book Club',          '902-555-7883', 'bookclub@example.org',               60.00,  9.00,  69.00,  0.00),
(14, TRUE,  FALSE, 'Party Room',     '2027-07-28', '18:00', '20:00', 'Ben Morrison',       '902-555-9989', 'ben.morrison@example.com',          150.00, 33.75, 258.75, 75.00);

-- 2027-08
INSERT INTO booking
(groupNum, isBirthday, equipmentNeeded, roomType, bookingDate, startTime, endTime,
 bookingName, phone, email, basePrice, tax, totalPrice, birthdayCost)
VALUES
(15, FALSE, FALSE, 'Gym',            '2027-08-02', '19:00', '22:00', 'Futsal Friends',     '902-555-1247', 'futsalfriends@example.com',         200.00, 30.00, 230.00,  0.00),
(31, FALSE, TRUE,  'Party Room',     '2027-08-06', '15:00', '17:00', 'Team Banquet',       '902-555-1993', 'teambanquet@example.com',           150.00, 22.50, 172.50,  0.00),
(4,  FALSE, TRUE,  'Small Rec Room', '2027-08-07', '20:00', '21:00', 'Board Game Night',   '902-555-7354', 'boardgamenight@example.com',         60.00,  9.00,  69.00,  0.00),
(4,  TRUE,  TRUE,  'Small Rec Room', '2027-08-11', '14:00', '15:00', 'Sophie MacDonald',   '902-555-3423', 'sophie.macdonald@example.org',       60.00, 20.25, 155.25, 75.00),
(6,  TRUE,  TRUE,  'Small Rec Room', '2027-08-13', '17:00', '18:00', 'Nathan Bernard',     '902-555-5590', 'nathan.bernard@example.com',         60.00, 20.25, 155.25, 75.00),
(15, TRUE,  TRUE,  'Party Room',     '2027-08-17', '20:00', '22:00', 'Ruby McInnis',       '902-555-3363', 'ruby.mcinnis@example.org',          150.00, 33.75, 258.75, 75.00),
(12, TRUE,  FALSE, 'Party Room',     '2027-08-21', '13:00', '16:00', 'Zoe Matheson',       '902-555-3879', 'zoe.matheson@example.org',          150.00, 33.75, 258.75, 75.00),
(12, FALSE, TRUE,  'Small Rec Room', '2027-08-23', '19:00', '21:00', 'Olivia Arsenault',   '902-555-6284', 'olivia.arsenault@example.org',       60.00,  9.00,  69.00,  0.00),
(23, TRUE,  FALSE, 'Gym',            '2027-08-27', '09:00', '10:00', 'Dylan Arsenault',    '902-555-6512', 'dylan.arsenault@example.com',       200.00, 41.25, 316.25, 75.00),
(28, TRUE,  TRUE,  'Gym',            '2027-08-31', '15:00', '16:00', 'Jack Poirier',       '902-555-2680', 'jack.poirier@example.com',          200.00, 41.25, 316.25, 75.00);

-- 2027-09
INSERT INTO booking
(groupNum, isBirthday, equipmentNeeded, roomType, bookingDate, startTime, endTime,
 bookingName, phone, email, basePrice, tax, totalPrice, birthdayCost)
VALUES
(15, FALSE, FALSE, 'Party Room',     '2027-09-03', '11:00', '14:00', 'Staff Social',       '902-555-1263', 'staffsocial@example.com',           150.00, 22.50, 172.50,  0.00),
(10, TRUE,  FALSE, 'Small Rec Room', '2027-09-05', '17:00', '18:00', 'Isaac Peters',       '902-555-0497', 'isaac.peters@example.com',           60.00, 20.25, 155.25, 75.00),
(30, FALSE, TRUE,  'Gym',            '2027-09-09', '11:00', '12:00', 'Futsal Friends',     '902-555-1247', 'futsalfriends@example.com',         200.00, 30.00, 230.00,  0.00),
(11, FALSE, FALSE, 'Gym',            '2027-09-15', '17:00', '20:00', 'Volleyball League',  '902-555-2019', 'volleyballleague@example.com',      200.00, 30.00, 230.00,  0.00),
(6,  FALSE, FALSE, 'Small Rec Room', '2027-09-19', '19:00', '22:00', 'Book Club',          '902-555-7883', 'bookclub@example.org',               60.00,  9.00,  69.00,  0.00),
(15, FALSE, TRUE,  'Party Room',     '2027-09-25', '11:00', '14:00', 'Lucas Doucette',     '902-555-5384', 'lucas.doucette@example.org',        150.00, 22.50, 172.50,  0.00),
(16, FALSE, FALSE, 'Gym',            '2027-09-27', '15:00', '17:00', 'Rovers Hockey',      '902-555-4446', 'rovershockey@example.com',          200.00, 30.00, 230.00,  0.00);

-- 2027-10
INSERT INTO booking
(groupNum, isBirthday, equipmentNeeded, roomType, bookingDate, startTime, endTime,
 bookingName, phone, email, basePrice, tax, totalPrice, birthdayCost)
VALUES
(20, FALSE, TRUE,  'Gym',            '2027-10-01', '08:00', '10:00', 'Lucas Doucette',     '902-555-5384', 'lucas.doucette@example.org',        200.00, 30.00, 230.00,  0.00),
(28, TRUE,  TRUE,  'Gym',            '2027-10-06', '11:00', '13:00', 'Olivia Arsenault',   '902-555-6284', 'olivia.arsenault@example.org',      200.00, 41.25, 316.25, 75.00),
(16, TRUE,  FALSE, 'Party Room',     '2027-10-10', '09:00', '12:00', 'Chloe Gaudet',       '902-555-7246', 'chloe.gaudet@example.org',          150.00, 33.75, 258.75, 75.00),
(27, FALSE, FALSE, 'Party Room',     '2027-10-12', '14:00', '18:00', 'Staff Social',       '902-555-1263', 'staffsocial@example.com',           150.00, 22.50, 172.50,  0.00),
(8,  TRUE,  FALSE, 'Gym',            '2027-10-19', '18:00', '20:00', 'Lily Cormier',       '902-555-6738', 'lily.cormier@example.org',          200.00, 41.25, 316.25, 75.00),
(8,  FALSE, FALSE, 'Small Rec Room', '2027-10-21', '14:00', '15:00', 'Photography Club',   '902-555-1450', 'photographyclub@example.org',        60.00,  9.00,  69.00,  0.00),
(11, FALSE, TRUE,  'Small Rec Room', '2027-10-27', '14:00', '16:00', 'Lucas Doucette',     '902-555-5384', 'lucas.doucette@example.org',         60.00,  9.00,  69.00,  0.00),
(5,  TRUE,  TRUE,  'Small Rec Room', '2027-10-28', '09:00', '10:00', 'Abby Doyle',         '902-555-1134', 'abby.doyle@example.org',             60.00, 20.25, 155.25, 75.00);

-- 2027-11
INSERT INTO booking
(groupNum, isBirthday, equipmentNeeded, roomType, bookingDate, startTime, endTime,
 bookingName, phone, email, basePrice, tax, totalPrice, birthdayCost)
VALUES
(10, TRUE,  TRUE,  'Party Room',     '2027-11-03', '11:00', '14:00', 'Hannah Gillis',      '902-555-3971', 'hannah.gillis@example.org',         150.00, 33.75, 258.75, 75.00),
(6,  TRUE,  FALSE, 'Small Rec Room', '2027-11-06', '21:00', '22:00', 'Ethan MacLeod',      '902-555-7942', 'ethan.macleod@example.com',          60.00, 20.25, 155.25, 75.00),
(24, FALSE, TRUE,  'Party Room',     '2027-11-07', '13:00', '15:00', 'Emma Richard',       '902-555-4635', 'emma.richard@example.org',          150.00, 22.50, 172.50,  0.00),
(15, TRUE,  FALSE, 'Gym',            '2027-11-11', '12:00', '14:00', 'Isaac Peters',       '902-555-0497', 'isaac.peters@example.com',          200.00, 41.25, 316.25, 75.00),
(7,  FALSE, FALSE, 'Small Rec Room', '2027-11-15', '10:00', '12:00', 'Book Club',          '902-555-7883', 'bookclub@example.org',               60.00,  9.00,  69.00,  0.00),
(19, FALSE, TRUE,  'Gym',            '2027-11-20', '10:00', '12:00', 'Rovers Hockey',      '902-555-4446', 'rovershockey@example.com',          200.00, 30.00, 230.00,  0.00),
(24, TRUE,  TRUE,  'Gym',            '2027-11-23', '10:00', '12:00', 'Hannah Gillis',      '902-555-3971', 'hannah.gillis@example.org',         200.00, 41.25, 316.25, 75.00),
(9,  FALSE, TRUE,  'Small Rec Room', '2027-11-26', '15:00', '17:00', 'Ethan MacLeod',      '902-555-7942', 'ethan.macleod@example.com',          60.00,  9.00,  69.00,  0.00),
(27, FALSE, FALSE, 'Gym',            '2027-11-27', '16:00', '19:00', 'Chloe Gaudet',       '902-555-7246', 'chloe.gaudet@example.org',          200.00, 30.00, 230.00,  0.00);

-- 2027-12
INSERT INTO booking
(groupNum, isBirthday, equipmentNeeded, roomType, bookingDate, startTime, endTime,
 bookingName, phone, email, basePrice, tax, totalPrice, birthdayCost)
VALUES
(33, FALSE, TRUE,  'Party Room',     '2027-12-02', '11:00', '15:00', 'Staff Social',       '902-555-1263', 'staffsocial@example.com',           150.00, 22.50, 172.50,  0.00),
(17, TRUE,  FALSE, 'Party Room',     '2027-12-06', '11:00', '14:00', 'Sophie MacDonald',   '902-555-3423', 'sophie.macdonald@example.org',      150.00, 33.75, 258.75, 75.00),
(6,  TRUE,  TRUE,  'Small Rec Room', '2027-12-11', '20:00', '22:00', 'Mia Fraser',         '902-555-1633', 'mia.fraser@example.com',             60.00, 20.25, 155.25, 75.00),
(33, FALSE, FALSE, 'Party Room',     '2027-12-15', '13:00', '15:00', 'Emma Richard',       '902-555-4635', 'emma.richard@example.org',          150.00, 22.50, 172.50,  0.00),
(4,  TRUE,  FALSE, 'Small Rec Room', '2027-12-21', '15:00', '18:00', 'Sophie MacDonald',   '902-555-3423', 'sophie.macdonald@example.org',       60.00, 20.25, 155.25, 75.00),
(28, TRUE,  TRUE,  'Party Room',     '2027-12-23', '18:00', '21:00', 'Chloe Gaudet',       '902-555-7246', 'chloe.gaudet@example.org',          150.00, 33.75, 258.75, 75.00),
(35, FALSE, FALSE, 'Party Room',     '2027-12-31', '17:00', '21:00', 'Ethan MacLeod',      '902-555-7942', 'ethan.macleod@example.com',         150.00, 22.50, 172.50,  0.00);

-- 2028-01
INSERT INTO booking
(groupNum, isBirthday, equipmentNeeded, roomType, bookingDate, startTime, endTime,
 bookingName, phone, email, basePrice, tax, totalPrice, birthdayCost)
VALUES
(18, FALSE, TRUE,  'Party Room',     '2028-01-04', '09:00', '12:00', 'Baby Shower',        '902-555-5999', 'babyshower@example.com',            150.00, 22.50, 172.50,  0.00),
(18, FALSE, FALSE, 'Gym',            '2028-01-06', '08:00', '10:00', 'Ethan MacLeod',      '902-555-7942', 'ethan.macleod@example.com',         200.00, 30.00, 230.00,  0.00),
(29, TRUE,  FALSE, 'Party Room',     '2028-01-10', '18:00', '20:00', 'Finn Gallant',       '902-555-6446', 'finn.gallant@example.com',          150.00, 33.75, 258.75, 75.00),
(26, FALSE, TRUE,  'Gym',            '2028-01-15', '19:00', '20:00', 'Badminton Club',     '902-555-1438', 'badmintonclub@example.com',         200.00, 30.00, 230.00,  0.00),
(9,  TRUE,  TRUE,  'Small Rec Room', '2028-01-19', '11:00', '13:00', 'Lucas Doucette',     '902-555-5384', 'lucas.doucette@example.org',         60.00, 20.25, 155.25, 75.00),
(25, TRUE,  TRUE,  'Gym',            '2028-01-26', '10:00', '11:00', 'Jack Poirier',       '902-555-2680', 'jack.poirier@example.com',          200.00, 41.25, 316.25, 75.00),
(10, FALSE, FALSE, 'Small Rec Room', '2028-01-30', '20:00', '21:00', 'Book Club',          '902-555-7883', 'bookclub@example.org',               60.00,  9.00,  69.00,  0.00);

-- 2028-02
INSERT INTO booking
(groupNum, isBirthday, equipmentNeeded, roomType, bookingDate, startTime, endTime,
 bookingName, phone, email, basePrice, tax, totalPrice, birthdayCost)
VALUES
(11, FALSE, TRUE,  'Small Rec Room', '2028-02-01', '12:00', '15:00', 'Study Group',        '902-555-4561', 'studygroup@example.com',             60.00,  9.00,  69.00,  0.00),
(15, TRUE,  TRUE,  'Party Room',     '2028-02-06', '17:00', '21:00', 'Ruby McInnis',       '902-555-3363', 'ruby.mcinnis@example.org',          150.00, 33.75, 258.75, 75.00),
(7,  TRUE,  FALSE, 'Small Rec Room', '2028-02-11', '18:00', '20:00', 'Zoe Matheson',       '902-555-3879', 'zoe.matheson@example.org',           60.00, 20.25, 155.25, 75.00),
(11, TRUE,  FALSE, 'Gym',            '2028-02-14', '09:00', '11:00', 'Sophie MacDonald',   '902-555-3423', 'sophie.macdonald@example.org',      200.00, 41.25, 316.25, 75.00),
(5,  FALSE, TRUE,  'Small Rec Room', '2028-02-17', '18:00', '19:00', 'Ethan MacLeod',      '902-555-7942', 'ethan.macleod@example.com',          60.00,  9.00,  69.00,  0.00),
(32, TRUE,  FALSE, 'Party Room',     '2028-02-23', '13:00', '15:00', 'Ben Morrison',       '902-555-9989', 'ben.morrison@example.com',          150.00, 33.75, 258.75, 75.00),
(4,  TRUE,  FALSE, 'Small Rec Room', '2028-02-29', '19:00', '22:00', 'Chloe Gaudet',       '902-555-7246', 'chloe.gaudet@example.org',           60.00, 20.25, 155.25, 75.00);

-- 2028-03
INSERT INTO booking
(groupNum, isBirthday, equipmentNeeded, roomType, bookingDate, startTime, endTime,
 bookingName, phone, email, basePrice, tax, totalPrice, birthdayCost)
VALUES
(17, FALSE, TRUE,  'Gym',            '2028-03-02', '13:00', '15:00', 'Emma Richard',       '902-555-4635', 'emma.richard@example.org',          200.00, 30.00, 230.00,  0.00),
(12, TRUE,  FALSE, 'Gym',            '2028-03-05', '16:00', '18:00', 'Nora Chiasson',      '902-555-9726', 'nora.chiasson@example.org',         200.00, 41.25, 316.25, 75.00),
(15, FALSE, FALSE, 'Party Room',     '2028-03-08', '09:00', '11:00', 'Staff Social',       '902-555-1263', 'staffsocial@example.com',           150.00, 22.50, 172.50,  0.00),
(7,  TRUE,  TRUE,  'Small Rec Room', '2028-03-12', '12:00', '14:00', 'Ben Morrison',       '902-555-9989', 'ben.morrison@example.com',           60.00, 20.25, 155.25, 75.00),
(30, TRUE,  TRUE,  'Party Room',     '2028-03-18', '16:00', '18:00', 'Ella Pineau',        '902-555-3244', 'ella.pineau@example.org',           150.00, 33.75, 258.75, 75.00),
(7,  FALSE, FALSE, 'Small Rec Room', '2028-03-20', '19:00', '21:00', 'Board Game Night',   '902-555-7354', 'boardgamenight@example.com',         60.00,  9.00,  69.00,  0.00),
(21, FALSE, TRUE,  'Party Room',     '2028-03-24', '14:00', '17:00', 'Grad Celebration',   '902-555-2211', 'gradcelebration@example.com',       150.00, 22.50, 172.50,  0.00),
(12, FALSE, FALSE, 'Gym',            '2028-03-29', '11:00', '12:00', 'Lucas Doucette',     '902-555-5384', 'lucas.doucette@example.org',        200.00, 30.00, 230.00,  0.00);

-- 2028-04
INSERT INTO booking
(groupNum, isBirthday, equipmentNeeded, roomType, bookingDate, startTime, endTime,
 bookingName, phone, email, basePrice, tax, totalPrice, birthdayCost)
VALUES
(28, TRUE,  TRUE,  'Gym',            '2028-04-05', '16:00', '17:00', 'Owen Campbell',      '902-555-4819', 'owen.campbell@example.org',         200.00, 41.25, 316.25, 75.00),
(11, TRUE,  FALSE, 'Party Room',     '2028-04-10', '08:00', '10:00', 'Eli Crosby',         '902-555-7320', 'eli.crosby@example.com',            150.00, 33.75, 258.75, 75.00),
(23, FALSE, TRUE,  'Gym',            '2028-04-13', '17:00', '19:00', 'Pickup Soccer',      '902-555-1899', 'pickupsoccer@example.org',          200.00, 30.00, 230.00,  0.00),
(6,  FALSE, TRUE,  'Small Rec Room', '2028-04-17', '11:00', '14:00', 'Chloe Gaudet',       '902-555-7246', 'chloe.gaudet@example.org',           60.00,  9.00,  69.00,  0.00),
(8,  FALSE, FALSE, 'Small Rec Room', '2028-04-21', '17:00', '18:00', 'Yoga Group',         '902-555-2459', 'yogagroup@example.org',              60.00,  9.00,  69.00,  0.00),
(20, FALSE, FALSE, 'Party Room',     '2028-04-28', '10:00', '13:00', 'Team Banquet',       '902-555-1993', 'teambanquet@example.com',           150.00, 22.50, 172.50,  0.00);

-- 2028-05
INSERT INTO booking
(groupNum, isBirthday, equipmentNeeded, roomType, bookingDate, startTime, endTime,
 bookingName, phone, email, basePrice, tax, totalPrice, birthdayCost)
VALUES
(24, FALSE, TRUE,  'Party Room',     '2028-05-03', '17:00', '21:00', 'Ethan MacLeod',      '902-555-7942', 'ethan.macleod@example.com',         150.00, 22.50, 172.50,  0.00),
(11, TRUE,  TRUE,  'Gym',            '2028-05-05', '10:00', '12:00', 'Carter Murphy',      '902-555-5715', 'carter.murphy@example.org',         200.00, 41.25, 316.25, 75.00),
(6,  TRUE,  TRUE,  'Small Rec Room', '2028-05-08', '15:00', '17:00', 'Zoe Matheson',       '902-555-3879', 'zoe.matheson@example.org',           60.00, 20.25, 155.25, 75.00),
(15, TRUE,  FALSE, 'Gym',            '2028-05-12', '13:00', '15:00', 'Olivia Arsenault',   '902-555-6284', 'olivia.arsenault@example.org',      200.00, 41.25, 316.25, 75.00),
(22, FALSE, FALSE, 'Gym',            '2028-05-14', '15:00', '16:00', 'Chloe Gaudet',       '902-555-7246', 'chloe.gaudet@example.org',          200.00, 30.00, 230.00,  0.00),
(29, TRUE,  TRUE,  'Party Room',     '2028-05-17', '08:00', '11:00', 'Maya Sutherland',    '902-555-3906', 'maya.sutherland@example.org',       150.00, 33.75, 258.75, 75.00),
(10, TRUE,  FALSE, 'Small Rec Room', '2028-05-19', '13:00', '16:00', 'Grace Hennessey',    '902-555-3815', 'grace.hennessey@example.com',        60.00, 20.25, 155.25, 75.00),
(11, FALSE, FALSE, 'Gym',            '2028-05-24', '13:00', '16:00', 'Pickup Soccer',      '902-555-1899', 'pickupsoccer@example.org',          200.00, 30.00, 230.00,  0.00),
(10, FALSE, TRUE,  'Gym',            '2028-05-25', '14:00', '17:00', 'Futsal Friends',     '902-555-1247', 'futsalfriends@example.com',         200.00, 30.00, 230.00,  0.00),
(9,  TRUE,  FALSE, 'Small Rec Room', '2028-05-29', '18:00', '19:00', 'Carter Murphy',      '902-555-5715', 'carter.murphy@example.org',          60.00, 20.25, 155.25, 75.00);

-- 2028-06
INSERT INTO booking
(groupNum, isBirthday, equipmentNeeded, roomType, bookingDate, startTime, endTime,
 bookingName, phone, email, basePrice, tax, totalPrice, birthdayCost)
VALUES
(6,  FALSE, FALSE, 'Small Rec Room', '2028-06-03', '10:00', '11:00', 'Olivia Arsenault',   '902-555-6284', 'olivia.arsenault@example.org',       60.00,  9.00,  69.00,  0.00),
(12, TRUE,  TRUE,  'Small Rec Room', '2028-06-05', '18:00', '21:00', 'Leo Blanchard',      '902-555-3877', 'leo.blanchard@example.com',          60.00, 20.25, 155.25, 75.00),
(24, TRUE,  FALSE, 'Gym',            '2028-06-08', '09:00', '10:00', 'Emma Richard',       '902-555-4635', 'emma.richard@example.org',          200.00, 41.25, 316.25, 75.00),
(8,  FALSE, TRUE,  'Small Rec Room', '2028-06-14', '08:00', '11:00', 'Book Club',          '902-555-7883', 'bookclub@example.org',               60.00,  9.00,  69.00,  0.00),
(35, FALSE, FALSE, 'Party Room',     '2028-06-17', '18:00', '22:00', 'Lucas Doucette',     '902-555-5384', 'lucas.doucette@example.org',        150.00, 22.50, 172.50,  0.00),
(25, TRUE,  TRUE,  'Party Room',     '2028-06-19', '17:00', '19:00', 'Abby Doyle',         '902-555-1134', 'abby.doyle@example.org',            150.00, 33.75, 258.75, 75.00),
(24, FALSE, TRUE,  'Party Room',     '2028-06-23', '19:00', '22:00', 'Retirement Party',   '902-555-4659', 'retirementparty@example.org',       150.00, 22.50, 172.50,  0.00),
(28, TRUE,  TRUE,  'Gym',            '2028-06-29', '10:00', '11:00', 'Ethan MacLeod',      '902-555-7942', 'ethan.macleod@example.com',         200.00, 41.25, 316.25, 75.00);

-- 2028-07
INSERT INTO booking
(groupNum, isBirthday, equipmentNeeded, roomType, bookingDate, startTime, endTime,
 bookingName, phone, email, basePrice, tax, totalPrice, birthdayCost)
VALUES
(30, TRUE,  FALSE, 'Party Room',     '2028-07-02', '18:00', '22:00', 'Abby Doyle',         '902-555-1134', 'abby.doyle@example.org',            150.00, 33.75, 258.75, 75.00),
(6,  TRUE,  FALSE, 'Small Rec Room', '2028-07-08', '21:00', '22:00', 'Dylan Arsenault',    '902-555-6512', 'dylan.arsenault@example.com',        60.00, 20.25, 155.25, 75.00),
(26, FALSE, TRUE,  'Party Room',     '2028-07-14', '13:00', '17:00', 'Community Potluck',  '902-555-6821', 'communitypotluck@example.org',      150.00, 22.50, 172.50,  0.00),
(14, FALSE, TRUE,  'Gym',            '2028-07-20', '16:00', '19:00', 'Island Hoops',       '902-555-9546', 'islandhoops@example.com',           200.00, 30.00, 230.00,  0.00),
(25, TRUE,  TRUE,  'Gym',            '2028-07-25', '16:00', '19:00', 'Ruby McInnis',       '902-555-3363', 'ruby.mcinnis@example.org',          200.00, 41.25, 316.25, 75.00),
(18, TRUE,  FALSE, 'Gym',            '2028-07-31', '14:00', '16:00', 'Henry Wedge',        '902-555-0900', 'henry.wedge@example.com',           200.00, 41.25, 316.25, 75.00);

-- 2028-08
INSERT INTO booking
(groupNum, isBirthday, equipmentNeeded, roomType, bookingDate, startTime, endTime,
 bookingName, phone, email, basePrice, tax, totalPrice, birthdayCost)
VALUES
(7,  FALSE, TRUE,  'Small Rec Room', '2028-08-02', '09:00', '12:00', 'Photography Club',   '902-555-1450', 'photographyclub@example.org',        60.00,  9.00,  69.00,  0.00),
(26, TRUE,  TRUE,  'Party Room',     '2028-08-05', '09:00', '12:00', 'Finn Gallant',       '902-555-6446', 'finn.gallant@example.com',          150.00, 33.75, 258.75, 75.00),
(9,  FALSE, FALSE, 'Gym',            '2028-08-07', '09:00', '12:00', 'Ball Hockey Night',  '902-555-4943', 'ballhockeynight@example.com',       200.00, 30.00, 230.00,  0.00),
(12, FALSE, FALSE, 'Small Rec Room', '2028-08-12', '16:00', '18:00', 'Chess Club',         '902-555-5235', 'chessclub@example.com',              60.00,  9.00,  69.00,  0.00),
(19, FALSE, FALSE, 'Party Room',     '2028-08-16', '19:00', '21:00', 'Staff Social',       '902-555-1263', 'staffsocial@example.com',           150.00, 22.50, 172.50,  0.00),
(12, TRUE,  TRUE,  'Small Rec Room', '2028-08-19', '13:00', '16:00', 'Lucas Doucette',     '902-555-5384', 'lucas.doucette@example.org',         60.00, 20.25, 155.25, 75.00),
(25, TRUE,  FALSE, 'Party Room',     '2028-08-21', '13:00', '16:00', 'Mia Fraser',         '902-555-1633', 'mia.fraser@example.com',            150.00, 33.75, 258.75, 75.00),
(9,  FALSE, FALSE, 'Gym',            '2028-08-26', '14:00', '16:00', 'Volleyball League',  '902-555-2019', 'volleyballleague@example.com',      200.00, 30.00, 230.00,  0.00),
(20, FALSE, TRUE,  'Gym',            '2028-08-28', '13:00', '14:00', 'Rovers Hockey',      '902-555-4446', 'rovershockey@example.com',          200.00, 30.00, 230.00,  0.00);

-- 2028-09
INSERT INTO booking
(groupNum, isBirthday, equipmentNeeded, roomType, bookingDate, startTime, endTime,
 bookingName, phone, email, basePrice, tax, totalPrice, birthdayCost)
VALUES
(33, FALSE, FALSE, 'Party Room',     '2028-09-03', '11:00', '15:00', 'Staff Social',       '902-555-1263', 'staffsocial@example.com',           150.00, 22.50, 172.50,  0.00),
(7,  FALSE, FALSE, 'Small Rec Room', '2028-09-06', '14:00', '17:00', 'Ethan MacLeod',      '902-555-7942', 'ethan.macleod@example.com',          60.00,  9.00,  69.00,  0.00),
(24, TRUE,  TRUE,  'Party Room',     '2028-09-08', '16:00', '19:00', 'Ruby McInnis',       '902-555-3363', 'ruby.mcinnis@example.org',          150.00, 33.75, 258.75, 75.00),
(4,  TRUE,  FALSE, 'Small Rec Room', '2028-09-10', '13:00', '14:00', 'Nora Chiasson',      '902-555-9726', 'nora.chiasson@example.org',          60.00, 20.25, 155.25, 75.00),
(35, FALSE, TRUE,  'Party Room',     '2028-09-13', '14:00', '18:00', 'Baby Shower',        '902-555-5999', 'babyshower@example.com',            150.00, 22.50, 172.50,  0.00),
(24, TRUE,  TRUE,  'Gym',            '2028-09-18', '15:00', '18:00', 'Isaac Peters',       '902-555-0497', 'isaac.peters@example.com',          200.00, 41.25, 316.25, 75.00),
(19, TRUE,  FALSE, 'Party Room',     '2028-09-21', '19:00', '22:00', 'Olivia Arsenault',   '902-555-6284', 'olivia.arsenault@example.org',      150.00, 33.75, 258.75, 75.00),
(9,  FALSE, TRUE,  'Small Rec Room', '2028-09-23', '10:00', '12:00', 'Craft Corner',       '902-555-4887', 'craftcorner@example.org',            60.00,  9.00,  69.00,  0.00),
(13, TRUE,  FALSE, 'Gym',            '2028-09-25', '13:00', '14:00', 'Adam Keough',        '902-555-1719', 'adam.keough@example.com',           200.00, 41.25, 316.25, 75.00),
(5,  TRUE,  TRUE,  'Small Rec Room', '2028-09-30', '18:00', '21:00', 'Claire Robichaud',   '902-555-8973', 'claire.robichaud@example.com',       60.00, 20.25, 155.25, 75.00);

# ALTER TABLE SkillsAssessmentSquashTechnical
#     ADD PRIMARY KEY (id);
# ALTER TABLE SkillsAssessmentSquashTechnical
#     MODIFY id int(4) NOT NULL AUTO_INCREMENT COMMENT 'This is the primary key',
#     AUTO_INCREMENT = 1;


# CREATE TABLE CodeType (codeTypeId int(3) COMMENT 'This is the primary key for code types',
#                        englishDescription varchar(100) NOT NULL COMMENT 'English description',
#                        frenchDescription varchar(100) DEFAULT NULL COMMENT 'French description',
#                        createdDateTime datetime DEFAULT NULL,
#                        createdUserId varchar(20) DEFAULT NULL,
#                        updatedDateTime datetime DEFAULT NULL,
#                        updatedUserId varchar(20) DEFAULT NULL
# ) COMMENT 'This tables holds the code types that are available for the application';
#
# ALTER TABLE CodeType
#     ADD PRIMARY KEY (CodeTypeId);
#
# INSERT INTO CodeType (CodeTypeId, englishDescription, frenchDescription, createdDateTime, createdUserId, updatedDateTime, updatedUserId) VALUES
#     (1, 'User Types', 'User Types FR', sysdate(), '', CURRENT_TIMESTAMP, '');
# INSERT INTO CodeType (CodeTypeId, englishDescription, frenchDescription, createdDateTime, createdUserId, updatedDateTime, updatedUserId) VALUES
#     (2, 'Squash Technical Types', 'Squash Technical Types FR', sysdate(), '', CURRENT_TIMESTAMP, '');
#
#
#
# CREATE TABLE CodeValue (
#                            codeTypeId int(3) NOT NULL COMMENT 'see code_type table',
#                            codeValueSequence int(3) NOT NULL,
#                            englishDescription varchar(100) NOT NULL COMMENT 'English description',
#                            englishDescriptionShort varchar(20) NOT NULL COMMENT 'English abbreviation for description',
#                            frenchDescription varchar(100) DEFAULT NULL COMMENT 'French description',
#                            frenchDescriptionShort varchar(20) DEFAULT NULL COMMENT 'French abbreviation for description',
#                            sortOrder int(3) DEFAULT NULL COMMENT 'Sort order if applicable',
#                            createdDateTime datetime DEFAULT NULL,
#                            createdUserId varchar(20) DEFAULT NULL,
#                            updatedDateTime datetime DEFAULT NULL,
#                            updatedUserId varchar(20) DEFAULT NULL
# ) COMMENT='This will hold code values for the application.';
#
# ALTER TABLE CodeValue
#     ADD PRIMARY KEY (CodeTypeId, codeValueSequence);
#
# INSERT INTO CodeValue (codeTypeId, codeValueSequence, englishDescription, englishDescriptionShort, frenchDescription, frenchDescriptionShort, createdDateTime, createdUserId, updatedDateTime, updatedUserId) VALUES
#     (1, 1, 'General', 'General', 'GeneralFR', 'GeneralFR', '2015-10-25 18:44:37', 'admin', '2015-10-25 18:44:37', 'admin');
# INSERT INTO CodeValue (codeTypeId, codeValueSequence, englishDescription, englishDescriptionShort, frenchDescription, frenchDescriptionShort, createdDateTime, createdUserId, updatedDateTime, updatedUserId) VALUES
#     (1, 2, 'Admin', 'Admin', 'Admin', 'Admin', '2015-10-25 18:44:37', 'admin', '2015-10-25 18:44:37', 'admin');
# INSERT INTO CodeValue (codeTypeId, codeValueSequence, englishDescription, englishDescriptionShort, frenchDescription, frenchDescriptionShort, createdDateTime, createdUserId, updatedDateTime, updatedUserId) VALUES
#     (2, 1, 'Forehand Drives', 'FH Drives', 'Forehand DrivesFR', 'FH DrivesFR', '2024-09-13 18:44:37', 'admin', '2024-09-13 18:44:37', 'admin');
# INSERT INTO CodeValue (codeTypeId, codeValueSequence, englishDescription, englishDescriptionShort, frenchDescription, frenchDescriptionShort, createdDateTime, createdUserId, updatedDateTime, updatedUserId) VALUES
#     (2, 2, 'Backhand Drives', 'BH Drives', 'Backhand DrivesFR', 'BH DrivesFR', '2024-09-13 18:44:37', 'admin', '2024-09-13 18:44:37', 'admin');
#

