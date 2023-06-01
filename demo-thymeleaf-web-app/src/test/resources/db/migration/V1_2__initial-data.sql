INSERT INTO users(email, password, role, created_date, updated_date, enabled) VALUES
    ('test.user@gmail.com', '$2a$10$6Md9dbrLlsz1KCUs.FC2NOSvfEwZIU9F.GHEuuVfDSYpr1kPuTZwG', 'ROLE_USER', now(), now(), true);

INSERT INTO bookings(from_date, to_date, status, created_date, updated_date, user_id)
VALUES ('2023-06-12 08:00:00', '2023-06-12 12:00:00', 'PENDING', now(), now(), 2);


