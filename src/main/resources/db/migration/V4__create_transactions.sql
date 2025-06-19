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