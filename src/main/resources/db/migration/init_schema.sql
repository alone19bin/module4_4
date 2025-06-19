-- Создание расширения для UUID (только для PostgreSQL)
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- V1__create_wallet_types.sql
CREATE TABLE IF NOT EXISTS wallet_types (
    uid UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    created_at TIMESTAMP DEFAULT NOW() NOT NULL,
    modified_at TIMESTAMP,
    name VARCHAR(32) NOT NULL,
    currency_code VARCHAR(3) NOT NULL,
    status VARCHAR(18) NOT NULL,
    archived_at TIMESTAMP,
    user_type VARCHAR(15),
    creator VARCHAR(255),
    modifier VARCHAR(255)
);

-- V2__create_wallets.sql
CREATE TABLE IF NOT EXISTS wallets (
    uid UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    created_at TIMESTAMP DEFAULT NOW() NOT NULL,
    modified_at TIMESTAMP,
    name VARCHAR(32) NOT NULL,
    wallet_type_uid UUID NOT NULL,
    user_uid UUID NOT NULL,
    status VARCHAR(30) NOT NULL,
    balance DECIMAL DEFAULT 0.0 NOT NULL,
    archived_at TIMESTAMP,
    CONSTRAINT fk_wallets_wallet_types FOREIGN KEY (wallet_type_uid) REFERENCES wallet_types(uid)
);

-- V3__create_payment_requests.sql
CREATE TABLE IF NOT EXISTS payment_requests (
    uid UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    created_at TIMESTAMP DEFAULT NOW() NOT NULL,
    modified_at TIMESTAMP,
    user_uid UUID NOT NULL,
    wallet_uid UUID NOT NULL,
    amount DECIMAL DEFAULT 0.0 NOT NULL,
    status VARCHAR(32),
    comment VARCHAR(256),
    payment_method_id BIGINT,
    CONSTRAINT fk_payment_requests_wallets FOREIGN KEY (wallet_uid) REFERENCES wallets(uid)
);

-- V4__create_transactions.sql
CREATE TABLE IF NOT EXISTS transactions (
    uid UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    created_at TIMESTAMP DEFAULT NOW() NOT NULL,
    modified_at TIMESTAMP,
    user_uid UUID NOT NULL,
    wallet_uid UUID NOT NULL,
    wallet_name VARCHAR(32) NOT NULL,
    amount DECIMAL DEFAULT 0.0 NOT NULL,
    type VARCHAR(32) NOT NULL,
    state VARCHAR(32) NOT NULL,
    payment_request_uid UUID NOT NULL,
    CONSTRAINT fk_transactions_wallets FOREIGN KEY (wallet_uid) REFERENCES wallets(uid),
    CONSTRAINT fk_transactions_payment_requests FOREIGN KEY (payment_request_uid) REFERENCES payment_requests(uid) ON DELETE CASCADE
);

-- V5__create_top_up_requests.sql
CREATE TABLE IF NOT EXISTS top_up_requests (
    uid UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    created_at TIMESTAMP DEFAULT NOW() NOT NULL,
    modified_at TIMESTAMP,
    provider VARCHAR(32) NOT NULL,
    payment_request_uid UUID NOT NULL,
    CONSTRAINT fk_top_up_requests_payment_requests FOREIGN KEY (payment_request_uid) REFERENCES payment_requests(uid) ON DELETE CASCADE
);

-- V6__create_withdrawal_requests.sql
CREATE TABLE IF NOT EXISTS withdrawal_requests (
    uid UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    created_at TIMESTAMP DEFAULT NOW() NOT NULL,
    modified_at TIMESTAMP,
    payment_request_uid UUID NOT NULL,
    CONSTRAINT fk_withdrawal_requests_payment_requests FOREIGN KEY (payment_request_uid) REFERENCES payment_requests(uid) ON DELETE CASCADE
);

-- V7__create_transfer_requests.sql
CREATE TABLE IF NOT EXISTS transfer_requests (
    uid UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    created_at TIMESTAMP DEFAULT NOW() NOT NULL,
    modified_at TIMESTAMP,
    system_rate VARCHAR(32) NOT NULL,
    payment_request_uid_from UUID NOT NULL,
    payment_request_uid_to UUID NOT NULL,
    CONSTRAINT fk_transfer_requests_payment_requests_from FOREIGN KEY (payment_request_uid_from) REFERENCES payment_requests(uid) ON DELETE CASCADE,
    CONSTRAINT fk_transfer_requests_payment_requests_to FOREIGN KEY (payment_request_uid_to) REFERENCES payment_requests(uid) ON DELETE CASCADE
); 