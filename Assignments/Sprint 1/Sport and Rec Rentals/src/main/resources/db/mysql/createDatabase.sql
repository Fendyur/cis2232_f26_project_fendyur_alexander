# For hccis.ca version of the database
# DROP DATABASE IF EXISTS bjmac_squash_skills_w26;
# CREATE DATABASE bjmac_squash_skills_w26;
# use bjmac_squash_skills_w26;

#For localhost
DROP DATABASE IF EXISTS cis2232_squash_scorer;
CREATE DATABASE cis2232_squash_scorer;
use cis2232_squash_scorer;

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

