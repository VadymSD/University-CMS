INSERT INTO teachers (teacher_id, first_name, last_name)
VALUES (1, 'Mark', 'Doe');
INSERT INTO teachers (first_name, last_name)
VALUES ('Ann', 'Banks');
INSERT INTO teachers (first_name, last_name)
VALUES ('Bob', 'Clark');

INSERT INTO courses (course_id, course_name, course_description)
VALUES (1, 'Math', 'Advanced Math');
INSERT INTO courses (course_name, course_description)
VALUES ('History', 'Modern History');
INSERT INTO courses (course_name, course_description)
VALUES ('Literature', 'Modern Literature');

INSERT INTO groups (group_id, group_name) VALUES (1, 'AA');
INSERT INTO groups (group_name) VALUES ('BB');

INSERT INTO students (student_id, first_name, last_name, group_id)
VALUES (1, 'John', 'Doe', 1);
INSERT INTO students (first_name, last_name)
VALUES ('Rob', 'Banks');
INSERT INTO students (first_name, last_name)
VALUES ('Andrea', 'Clark');

INSERT INTO lectures (lecture_id, lecture_name, start_time, end_time, date, room, teacher_id, course_id)
VALUES (1, 'Test lecture', '08:00:00', '09:00:00', '2008-11-11', 22, 1, 1);

INSERT INTO group_lectures(group_id, lecture_id) VALUES (1, 1);
