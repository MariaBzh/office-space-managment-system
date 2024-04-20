CREATE SCHEMA IF NOT EXISTS osms AUTHORIZATION CURRENT_USER;
SET SCHEMA 'osms';

CREATE TABLE IF NOT EXISTS branch (
    branch_uid VARCHAR(36) PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255) NULL
);

CREATE TABLE IF NOT EXISTS floor (
   floor_uid VARCHAR(36) PRIMARY KEY DEFAULT gen_random_uuid(),
   level VARCHAR(10) NOT NULL,
   description VARCHAR(255) NULL
);

CREATE TABLE IF NOT EXISTS office (
    office_uid VARCHAR(36) PRIMARY KEY DEFAULT gen_random_uuid(),
    name VARCHAR(255) NOT NULL,
    description VARCHAR(255) NULL
);

CREATE TABLE IF NOT EXISTS users (
    user_uid VARCHAR(36) PRIMARY KEY DEFAULT gen_random_uuid()
);

CREATE TABLE IF NOT EXISTS permission (
    id SERIAL4 PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS booking (
     booking_uid VARCHAR(36) PRIMARY KEY DEFAULT gen_random_uuid(),
     user_uid VARCHAR(36) NOT NULL REFERENCES users(user_uid),
     branch_uid VARCHAR(36) NOT NULL REFERENCES branch(branch_uid) ,
     floor_uid VARCHAR(36) NOT NULL REFERENCES floor(floor_uid),
     office_uid VARCHAR(36) NOT NULL REFERENCES office(office_uid),
     workplace_uid VARCHAR(36) NOT NULL,
     description VARCHAR(255) NULL,
     startTime TIMESTAMP NOT NULL,
     endTime TIMESTAMP NOT NULL,
     lock VARCHAR(36) NULL
);

CREATE TABLE IF NOT EXISTS ref_booking_user_permission (
    ref_uid VARCHAR(36) PRIMARY KEY DEFAULT gen_random_uuid(),
    booking_uid VARCHAR(36) REFERENCES booking(booking_uid),
    permission_id INT4 REFERENCES permission(id),
    user_uid VARCHAR(36) REFERENCES users(user_uid)
);

INSERT INTO branch (branch_uid, name, description) VALUES ('branch-1', 'Branch', 'Test');
INSERT INTO floor (floor_uid, level, description) VALUES ('floor-1', '1A', 'Test');
INSERT INTO office (office_uid, name, description) VALUES ('office-1', 'Office', 'Test');

INSERT INTO permission (name) VALUES ('READ');
INSERT INTO permission (name) VALUES ('UPDATE');
INSERT INTO permission (name) VALUES ('DELETE');

INSERT INTO users (user_uid) VALUES ('user-1');

INSERT INTO booking
    (booking_uid, user_uid, branch_uid, floor_uid, office_uid, workplace_uid, description, startTime, endTime, lock)
VALUES
    ('booking-1',
     'user-1',
     (SELECT b.branch_uid FROM branch b WHERE b.branch_uid like 'branch-1'),
     (SELECT f.floor_uid FROM floor f WHERE f.floor_uid like 'floor-1'),
     (SELECT o.office_uid FROM office o WHERE o.office_uid like 'office-1'),
     'workspace-1',
     null,
     '2024-01-01T10:00:00',
     '2024-01-01T12:00:00',
     'lock-1'
    );

INSERT INTO ref_booking_user_permission (booking_uid, permission_id, user_uid) VALUES ('booking-1', 1, 'user-1');
INSERT INTO ref_booking_user_permission (booking_uid, permission_id, user_uid) VALUES ('booking-1', 2, 'user-1');
INSERT INTO ref_booking_user_permission (booking_uid, permission_id, user_uid) VALUES ('booking-1', 3, 'user-1');
