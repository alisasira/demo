CREATE TABLE users
(
    id   BIGINT(20) AUTO_INCREMENT,
    first_name VARCHAR(32) NOT NULL,
    last_name VARCHAR(32) NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE accounts
(
    id   BIGINT(20) AUTO_INCREMENT,
    number BIGINT(20) NOT NULL,
    user_id BIGINT(20) NOT NULL,
    balance BIGINT(20) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT `fk_accounts_users` FOREIGN KEY (user_id) REFERENCES users (id)
) ENGINE=InnoDB;

CREATE TABLE categories
(
    id   BIGINT(20) AUTO_INCREMENT,
    name VARCHAR(32) NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB;

CREATE TABLE transactions
(
    id   BIGINT(20) AUTO_INCREMENT,
    from_account_id BIGINT(20) NOT NULL,
    to_account_id BIGINT(20) NOT NULL,
    amount BIGINT(20) NOT NULL,
    category_id BIGINT(20) NOT NULL,
    date DATETIME NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT `fk_transactions_from_accounts` FOREIGN KEY (from_account_id) REFERENCES accounts (id),
    CONSTRAINT `fk_transactions_to_accounts` FOREIGN KEY (to_account_id) REFERENCES accounts (id),
    CONSTRAINT `fk_transactions_categories` FOREIGN KEY (category_id) REFERENCES categories (id)
) ENGINE=InnoDB;

INSERT INTO users(first_name, last_name) VALUES
('First', 'One'),
('Second', 'Two'),
('Third', 'Three'),
('Fourth', 'Four'),
('Fifth', 'Five');

INSERT INTO accounts(number, user_id, balance) VALUES
('11111111',1,10000),
('22222222',2,15000),
('33333333',3,20500),
('44444444',4,35000),
('55555555',5,5000);

INSERT INTO categories(name) VALUES
('salary'),
('moneyTransfer'),
('onlineShopping'),
('grocery');