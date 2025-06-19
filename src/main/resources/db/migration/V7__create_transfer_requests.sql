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