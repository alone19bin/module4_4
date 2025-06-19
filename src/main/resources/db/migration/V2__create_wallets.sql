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