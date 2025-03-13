CREATE TABLE card (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    account_number INT NOT NULL UNIQUE
);

CREATE TABLE card_statement (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    status VARCHAR(50) NOT NULL,
    total_amount DOUBLE NOT NULL,
    started_at TIMESTAMP NOT NULL,
    ended_at TIMESTAMP NOT NULL,
    credit_card_id BIGINT NOT NULL,
    CONSTRAINT fk_card_statement_card FOREIGN KEY (credit_card_id) REFERENCES card (id) ON DELETE CASCADE,
    CONSTRAINT unique_open_statement UNIQUE (credit_card_id, status)
);

CREATE TABLE card_transaction (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    company_name VARCHAR(255) NOT NULL,
    installments_count INT NOT NULL,
    installments_amount INT NOT NULL,
    total_amount INT NOT NULL,
    date TIMESTAMP,
    card_statements_id BIGINT NOT NULL,
    CONSTRAINT fk_card_transaction_statement FOREIGN KEY (card_statements_id) REFERENCES card_statement (id) ON DELETE CASCADE
);

CREATE TABLE card_transaction_installment (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    amount DOUBLE,
    installment_number INT NOT NULL,
    card_statement_id BIGINT,
    card_transaction_id BIGINT,
    CONSTRAINT fk_installment_statement FOREIGN KEY (card_statement_id) REFERENCES card_statement (id) ON DELETE CASCADE,
    CONSTRAINT fk_installment_transaction FOREIGN KEY (card_transaction_id) REFERENCES card_transaction (id) ON DELETE CASCADE
);

CREATE UNIQUE INDEX idx_unique_open_statement ON card_statement (credit_card_id, status) WHERE status = 'OPEN';
