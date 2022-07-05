-- liquibase formatted sql

-- changeSet aorlova:1
CREATE INDEX student_name_index ON student (name);

-- changeSet aorlova:2
CREATE index faculty_name_color_index ON faculty (name, color);