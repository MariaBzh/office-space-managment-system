CREATE SCHEMA IF NOT EXISTS osms AUTHORIZATION CURRENT_USER;
SET SCHEMA 'osms';

CREATE TABLE IF NOT EXISTS branch (
    id SERIAL4 PRIMARY KEY,
    branch_uid VARCHAR(36) NOT NULL,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255) NULL
);

CREATE TABLE IF NOT EXISTS floor (
   id SERIAL4 PRIMARY KEY,
   floor_uid VARCHAR(36) NOT NULL,
   level VARCHAR(10) NOT NULL,
   name VARCHAR(255) NOT NULL,
   description VARCHAR(255) NULL
);

CREATE TABLE IF NOT EXISTS office (
    id SERIAL4 PRIMARY KEY,
    office_uid VARCHAR(36) NOT NULL,
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255) NULL
);

CREATE TABLE IF NOT EXISTS booking (
     id SERIAL4 PRIMARY KEY,
     booking_uid VARCHAR(36) NOT NULL,
     user_id INT4 NOT NULL,
     branch_id INT4 NOT NULL REFERENCES branch(id),
     floor_id INT4 NOT NULL REFERENCES floor(id),
     office_id INT4 NOT NULL REFERENCES office(id),
     workplace_id INT4 NOT NULL,
     description VARCHAR(255) NULL,
     startTime TIMESTAMP WITH TIME ZONE NOT NULL,
     endTime TIMESTAMP WITH TIME ZONE NOT NULL
);

INSERT INTO branch (branch_uid, name, description) VALUES ('branch-1', 'Branch', 'Test');
INSERT INTO floor (floor_uid, level, name, description) VALUES ('floor-1', '1A', 'Floor 1', 'Test');
INSERT INTO office (office_uid, name, description) VALUES ('office-1', 'Office', 'Test');
INSERT INTO booking
(booking_uid, user_id, branch_id, floor_id, office_id, workplace_id, description, startTime, endTime)
VALUES
    ('booking-1',
     1,
     (SELECT b.id FROM branch b WHERE b.branch_uid like 'branch-1'),
     (SELECT f.id FROM floor f WHERE f.floor_uid like 'floor-1'),
     (SELECT o.id FROM office o WHERE o.office_uid like 'office-1'),
     1,
     null,
     '2024-01-01T10:00:00',
     '2024-01-01T12:00:00'
    )
