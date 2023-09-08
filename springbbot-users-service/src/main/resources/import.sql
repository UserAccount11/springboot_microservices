INSERT INTO `users` (username, password, enabled, firstname, lastname, email) VALUES ('marco', '12345', 1, 'Marco', 'Cubas', 'marcocubas@gmail.com');
INSERT INTO `users` (username, password, enabled, firstname, lastname, email) VALUES ('admin', '12345', 1, 'Jhon ', 'Doe', 'jhon.doe@gmail.com');

INSERT INTO `roles` (name) VALUES ('ROLE_USER');
INSERT INTO `roles` (name) VALUES ('ROLE_ADMIN');

INSERT INTO `users_roles` (user_id, role_id) VALUES (1, 1);
INSERT INTO `users_roles` (user_id, role_id) VALUES (2, 1);
INSERT INTO `users_roles` (user_id, role_id) VALUES (2, 2);