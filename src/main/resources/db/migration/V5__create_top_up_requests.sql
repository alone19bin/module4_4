CREATE TABLE IF NOT EXISTS top_up_requests (
    uid UUID DEFAULT uuid_generate_v4() PRIMARY KEY,
    created_at TIMESTAMP DEFAULT NOW() NOT NULL,
    modified_at TIMESTAMP,
    provider VARCHAR(32) NOT NULL,
    payment_request_uid UUID NOT NULL,
    CONSTRAINT fk_top_up_requests_payment_requests FOREIGN KEY (payment_request_uid) REFERENCES payment_requests(uid) ON DELETE CASCADE
); 