DROP TABLE IF EXISTS student_courses;
DROP TABLE IF EXISTS lectures;
DROP TABLE IF EXISTS timetables;
DROP TABLE IF EXISTS teachers;
DROP TABLE IF EXISTS students;
DROP TABLE IF EXISTS courses;
DROP TABLE IF EXISTS groups;
DROP TABLE IF EXISTS faculties;
DROP TABLE IF EXISTS universities;
DROP TABLE IF EXISTS administrators;
DROP TABLE IF EXISTS roles;
DROP TABLE IF EXISTS my_users;

CREATE TABLE roles (
    role_id SERIAL PRIMARY KEY,
    role_name VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE my_users (
    user_id SERIAL PRIMARY KEY,
    role_id INT REFERENCES roles(role_id),
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

CREATE TABLE universities (
    university_id SERIAL PRIMARY KEY,
    university_name VARCHAR(255) NOT NULL,
    location VARCHAR(255) NOT NULL
);

CREATE TABLE faculties (
    faculty_id SERIAL PRIMARY KEY,
    faculty_name VARCHAR(255) NOT NULL,
    head VARCHAR(255),
    university_id INT REFERENCES universities(university_id)
);

CREATE TABLE groups (
    group_id SERIAL PRIMARY KEY,
    faculty_id INT REFERENCES faculties(faculty_id),
    group_name VARCHAR(255) NOT NULL
);

CREATE TABLE courses (
    course_id SERIAL PRIMARY KEY,
    faculty_id INT REFERENCES faculties(faculty_id),
    course_name VARCHAR(255) NOT NULL,
    course_description TEXT
);

CREATE TABLE students (
    student_id SERIAL PRIMARY KEY,
    group_id INT REFERENCES groups(group_id),
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    user_id INT REFERENCES my_users(user_id)
);

CREATE TABLE group_courses (
     group_id INT REFERENCES groups(group_id),
     course_id INT REFERENCES courses(course_id),
     PRIMARY KEY (group_id, course_id)
);

CREATE TABLE teachers (
    teacher_id SERIAL PRIMARY KEY,
    faculty_id INT REFERENCES faculties(faculty_id),
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    user_id INT REFERENCES my_users(user_id)
);

CREATE TABLE teacher_courses (
    teacher_id INT REFERENCES teachers(teacher_id),
    course_id INT REFERENCES courses(course_id),
    PRIMARY KEY (teacher_id, course_id)
);

CREATE TABLE timetables (
    timetable_id SERIAL PRIMARY KEY,
    timetable_name VARCHAR(255) NOT NULL,
    type VARCHAR(30) NOT NULL,
    start_date DATE NOT NULL
);

CREATE TABLE lectures (
    lecture_id SERIAL PRIMARY KEY,
    lecture_name VARCHAR(255) NOT NULL,
    course_id INT REFERENCES courses(course_id),
    timetable_id INT REFERENCES timetables(timetable_id),
    teacher_id INT REFERENCES teachers(teacher_id),
    date DATE NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL CHECK (end_time > start_time),
    room INT
);

CREATE TABLE group_lectures (
    group_id INT REFERENCES groups(group_id),
    lecture_id INT REFERENCES lectures(lecture_id),
    PRIMARY KEY (group_id, lecture_id)
);

CREATE TABLE administrators (
    admin_id SERIAL PRIMARY KEY,
    admin_name VARCHAR(255) NOT NULL
);




