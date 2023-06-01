CREATE TABLE users
(
    id   BIGINT(20) AUTO_INCREMENT,
    email VARCHAR(32) NOT NULL,
    password VARCHAR(64) NOT NULL,
    role VARCHAR(32) NOT NULL,
    enabled BOOLEAN NOT NULL,
    created_date DATETIME NOT NULL,
    updated_date DATETIME NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY (email)
) ENGINE=InnoDB;

CREATE TABLE bookings
(
    id   BIGINT(20) AUTO_INCREMENT,
    from_date DATETIME NOT NULL,
    to_date DATETIME NOT NULL,
    status VARCHAR(32) NOT NULL,
    created_date DATETIME NOT NULL,
    updated_date DATETIME NOT NULL,
    user_id BIGINT(20) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT `fk_bookings_users` FOREIGN KEY (user_id) REFERENCES users (id)

) ENGINE=InnoDB;

INSERT INTO users(email, password, role, created_date, updated_date, enabled) VALUES
('alisa.sira@gmail.com', '$2a$10$AbWrzrUYfAb/8WG1om43beRg3hifRK4EfpB.69RAyU6oWSVoEO5V.', 'ROLE_ADMIN', now(), now(), true)



