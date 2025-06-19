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