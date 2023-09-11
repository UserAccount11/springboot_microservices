INSERT INTO `users` (username, password, enabled, firstname, lastname, email) VALUES ('marco', '$2a$10$YvXIGEu6DhzSxnW0lS8m7uY97jNUKKe5hSD.QgdXx6TLvKdVm5kXS', 1, 'Marco', 'Cubas', 'marcocubas@gmail.com');
INSERT INTO `users` (username, password, enabled, firstname, lastname, email) VALUES ('admin', '$2a$10$YvXIGEu6DhzSxnW0lS8m7uY97jNUKKe5hSD.QgdXx6TLvKdVm5kXS', 1, 'Jhon ', 'Doe', 'jhon.doe@gmail.com');

INSERT INTO `roles` (name) VALUES ('ROLE_USER');
INSERT INTO `roles` (name) VALUES ('ROLE_ADMIN');

INSERT INTO `users_roles` (user_id, role_id) VALUES (1, 1);
INSERT INTO `users_roles` (user_id, role_id) VALUES (2, 1);
INSERT INTO `users_roles` (user_id, role_id) VALUES (2, 2);