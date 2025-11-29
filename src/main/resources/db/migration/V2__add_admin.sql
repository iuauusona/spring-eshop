INSERT INTO users (id, archived, email, username, password, role, bucket_id)
VALUES (1, false,'mail@gmail.com', 'admin', 'pass', 'ADMIN', null);

ALTER SEQUENCE users_seq RESTART WITH 2;