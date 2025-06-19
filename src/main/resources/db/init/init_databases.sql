-- Создание баз данных
CREATE DATABASE transaction_service_db1;
CREATE DATABASE transaction_service_db2;

-- Подключение к первой базе данных
\c transaction_service_db1

-- Создание расширения для UUID
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Создание таблиц в первой базе данных
CREATE TABLE wallet_types (
    uid uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    created_at timestamp DEFAULT now() NOT NULL,
    modified_at timestamp,
    name varchar(32) NOT NULL,
    currency_code varchar(3) NOT NULL,
    status varchar(18) NOT NULL,
    archived_at timestamp,
    user_type varchar(15),
    creator varchar(255),
    modifier varchar(255)
);

CREATE TABLE wallets (
    uid uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    created_at timestamp DEFAULT now() NOT NULL,
    modified_at timestamp,
    name varchar(32) NOT NULL,
    wallet_type_uid uuid NOT NULL REFERENCES wallet_types(uid),
    user_uid uuid NOT NULL,
    status varchar(30) NOT NULL,
    balance decimal DEFAULT 0.0 NOT NULL,
    archived_at timestamp
);

CREATE TABLE payment_requests (
    uid uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    created_at timestamp DEFAULT now() NOT NULL,
    modified_at timestamp,
    user_uid uuid NOT NULL,
    wallet_uid uuid NOT NULL REFERENCES wallets(uid),
    amount decimal DEFAULT 0.0 NOT NULL,
    status varchar(30),
    comment varchar(256),
    payment_method_id bigint
);

CREATE TABLE transactions (
    uid uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    created_at timestamp DEFAULT now() NOT NULL,
    modified_at timestamp,
    user_uid uuid NOT NULL,
    wallet_uid uuid NOT NULL REFERENCES wallets(uid),
    wallet_name varchar(32) NOT NULL,
    amount decimal DEFAULT 0.0 NOT NULL,
    type varchar(32) NOT NULL,
    state varchar(32) NOT NULL,
    payment_request_uid uuid NOT NULL REFERENCES payment_requests(uid) ON DELETE CASCADE
);

CREATE TABLE top_up_requests (
    uid uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    created_at timestamp DEFAULT now() NOT NULL,
    provider varchar NOT NULL,
    payment_request_uid uuid NOT NULL REFERENCES payment_requests(uid) ON DELETE CASCADE
);

CREATE TABLE withdrawal_requests (
    uid uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    created_at timestamp DEFAULT now() NOT NULL,
    payment_request_uid uuid NOT NULL REFERENCES payment_requests(uid) ON DELETE CASCADE
);

CREATE TABLE transfer_requests (
    uid uuid PRIMARY KEY DEFAULT uuid_generate_v4() NOT NULL,
    created_at timestamp DEFAULT now() NOT NULL,
    system_rate varchar NOT NULL,
    payment_request_uid_from uuid NOT NULL REFERENCES payment_requests(uid) ON DELETE CASCADE,
    payment_request_uid_to uuid NOT NULL REFERENCES payment_requests(uid) ON DELETE CASCADE
);

-- Подключение ко второй базе данных
\c transaction_service_db2

-- Создание расширения для UUID
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Создание тех же таблиц во второй базе данных
CREATE TABLE wallet_types (
    uid uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    created_at timestamp DEFAULT now() NOT NULL,
    modified_at timestamp,
    name varchar(32) NOT NULL,
    currency_code varchar(3) NOT NULL,
    status varchar(18) NOT NULL,
    archived_at timestamp,
    user_type varchar(15),
    creator varchar(255),
    modifier varchar(255)
);

CREATE TABLE wallets (
    uid uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    created_at timestamp DEFAULT now() NOT NULL,
    modified_at timestamp,
    name varchar(32) NOT NULL,
    wallet_type_uid uuid NOT NULL REFERENCES wallet_types(uid),
    user_uid uuid NOT NULL,
    status varchar(30) NOT NULL,
    balance decimal DEFAULT 0.0 NOT NULL,
    archived_at timestamp
);

CREATE TABLE payment_requests (
    uid uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    created_at timestamp DEFAULT now() NOT NULL,
    modified_at timestamp,
    user_uid uuid NOT NULL,
    wallet_uid uuid NOT NULL REFERENCES wallets(uid),
    amount decimal DEFAULT 0.0 NOT NULL,
    status varchar(30),
    comment varchar(256),
    payment_method_id bigint
);

CREATE TABLE transactions (
    uid uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    created_at timestamp DEFAULT now() NOT NULL,
    modified_at timestamp,
    user_uid uuid NOT NULL,
    wallet_uid uuid NOT NULL REFERENCES wallets(uid),
    wallet_name varchar(32) NOT NULL,
    amount decimal DEFAULT 0.0 NOT NULL,
    type varchar(32) NOT NULL,
    state varchar(32) NOT NULL,
    payment_request_uid uuid NOT NULL REFERENCES payment_requests(uid) ON DELETE CASCADE
);

CREATE TABLE top_up_requests (
    uid uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    created_at timestamp DEFAULT now() NOT NULL,
    provider varchar NOT NULL,
    payment_request_uid uuid NOT NULL REFERENCES payment_requests(uid) ON DELETE CASCADE
);

CREATE TABLE withdrawal_requests (
    uid uuid PRIMARY KEY DEFAULT uuid_generate_v4(),
    created_at timestamp DEFAULT now() NOT NULL,
    payment_request_uid uuid NOT NULL REFERENCES payment_requests(uid) ON DELETE CASCADE
);

CREATE TABLE transfer_requests (
    uid uuid PRIMARY KEY DEFAULT uuid_generate_v4() NOT NULL,
    created_at timestamp DEFAULT now() NOT NULL,
    system_rate varchar NOT NULL,
    payment_request_uid_from uuid NOT NULL REFERENCES payment_requests(uid) ON DELETE CASCADE,
    payment_request_uid_to uuid NOT NULL REFERENCES payment_requests(uid) ON DELETE CASCADE
); 