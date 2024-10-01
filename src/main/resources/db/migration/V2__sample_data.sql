INSERT INTO universities (university_name, location)
    VALUES ('UNI', 'US');

INSERT INTO faculties (faculty_name, head)
    VALUES ('Math', 'Math Head');

INSERT INTO roles(role_name)
VALUES ('ROLE_ADMIN');

INSERT INTO roles(role_name)
VALUES ('ROLE_TEACHER');

INSERT INTO roles(role_name)
VALUES ('ROLE_STAFF');

INSERT INTO roles(role_name)
VALUES ('ROLE_STUDENT');

INSERT INTO my_users(username, password, role_id)
VALUES ('user', 'password1', 1);

INSERT INTO my_users(username, password, role_id)
VALUES ('user1', 'password1', 2);

INSERT INTO my_users(username, password, role_id)
VALUES ('user2', 'password1', 4);

INSERT INTO courses (course_name, course_description)
    VALUES ('Math', 'Advanced Math');
INSERT INTO courses (course_name, course_description)
    VALUES ('History', 'Modern History');
INSERT INTO courses (course_name, course_description)
    VALUES ('Literature', 'Modern Literature');

INSERT INTO teachers (first_name, last_name, faculty_id, user_id)
    VALUES ('Mark', 'Doe', 1, 2);
INSERT INTO teachers (first_name, last_name, faculty_id)
    VALUES ('Ann', 'Banks', 1);
INSERT INTO teachers (first_name, last_name, faculty_id)
    VALUES ('Bob', 'Clark', 1);

INSERT INTO administrators (admin_name)
    VALUES ('Main admin');

INSERT INTO lectures (lecture_name, start_time, end_time, date, room, teacher_id, course_id)
    VALUES ('Test lecture', '08:00:00', '09:00:00', '2008-11-11', 22, 1, 1);

INSERT INTO groups (group_name) VALUES ('AA');
INSERT INTO groups (group_name) VALUES ('BB');

INSERT INTO students (first_name, last_name, group_id, user_id)
VALUES ('John', 'Doe', 1, 3);
INSERT INTO students (first_name, last_name, group_id)
VALUES ('Rob', 'Banks', 1);
INSERT INTO students (first_name, last_name, group_id)
VALUES ('Andrea', 'Clark', 2);

INSERT INTO group_lectures(group_id, lecture_id) VALUES (1, 1);

INSERT INTO group_courses(group_id, course_id) VALUES (1, 1);
INSERT INTO group_courses(group_id, course_id) VALUES (1, 2);
INSERT INTO group_courses(group_id, course_id) VALUES (2, 1);
INSERT INTO group_courses(group_id, course_id) VALUES (2, 2);

INSERT INTO teacher_courses(teacher_id, course_id) VALUES (1, 1);
INSERT INTO teacher_courses(teacher_id, course_id) VALUES (1, 2);
INSERT INTO teacher_courses(teacher_id, course_id) VALUES (2, 1);
INSERT INTO teacher_courses(teacher_id, course_id) VALUES (2, 2);



