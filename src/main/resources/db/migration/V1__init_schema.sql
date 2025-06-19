-- Создание таблицы типов кошельков
CREATE TABLE IF NOT EXISTS wallet_types (
    uid UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    created_at TIMESTAMP DEFAULT NOW() NOT NULL,
    modified_at TIMESTAMP,
    name VARCHAR(32) NOT NULL,
    currency_code VARCHAR(3) NOT NULL,
    status VARCHAR(18) NOT NULL,
    user_type VARCHAR(15),
    creator VARCHAR(255),
    modifier VARCHAR(255)
);

-- Создание таблицы кошельков
CREATE TABLE IF NOT EXISTS wallets (
    uid UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    created_at TIMESTAMP DEFAULT NOW() NOT NULL,
    modified_at TIMESTAMP,
    name VARCHAR(32) NOT NULL,
    wallet_type_uid UUID NOT NULL REFERENCES wallet_types(uid),
    user_uid UUID NOT NULL,
    status VARCHAR(30) NOT NULL,
    balance DECIMAL DEFAULT 0.0 NOT NULL
);

-- Создание таблицы платежных запросов
CREATE TABLE IF NOT EXISTS payment_requests (
    uid UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    created_at TIMESTAMP DEFAULT NOW() NOT NULL,
    modified_at TIMESTAMP,
    user_uid UUID NOT NULL,
    wallet_uid UUID NOT NULL REFERENCES wallets(uid),
    amount DECIMAL DEFAULT 0.0 NOT NULL,
    status VARCHAR(30),
    comment VARCHAR(256),
    payment_method_id BIGINT
);

-- Создание таблицы транзакций
CREATE TABLE IF NOT EXISTS transactions (
    uid UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    created_at TIMESTAMP DEFAULT NOW() NOT NULL,
    modified_at TIMESTAMP,
    user_uid UUID NOT NULL,
    wallet_uid UUID NOT NULL REFERENCES wallets(uid),
    wallet_name VARCHAR(32) NOT NULL,
    amount DECIMAL DEFAULT 0.0 NOT NULL,
    type VARCHAR(32) NOT NULL,
    state VARCHAR(32) NOT NULL,
    payment_request_uid UUID NOT NULL REFERENCES payment_requests(uid) ON DELETE CASCADE
);

