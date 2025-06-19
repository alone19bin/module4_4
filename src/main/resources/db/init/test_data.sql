-- Получаем ID типа кошелька для использования в тестовых данных
DO $$
DECLARE
    wallet_type_id uuid;
BEGIN
    -- Получаем ID основного типа кошелька
    SELECT uid INTO wallet_type_id FROM wallet_types WHERE name = 'Основной' LIMIT 1;

    -- Создаем тестовые кошельки для разных пользователей
    -- Пользователь 1 (четный ID) - должен попасть в db1
    INSERT INTO wallets (uid, created_at, name, wallet_type_uid, user_uid, status, balance)
    VALUES 
        (gen_random_uuid(), CURRENT_TIMESTAMP, 'Кошелек 1-1', wallet_type_id, '11111111-1111-1111-1111-111111111111', 'ACTIVE', 1000.00),
        (gen_random_uuid(), CURRENT_TIMESTAMP, 'Кошелек 1-2', wallet_type_id, '11111111-1111-1111-1111-111111111111', 'ACTIVE', 2000.00);

    -- Пользователь 2 (нечетный ID) - должен попасть в db2
    INSERT INTO wallets (uid, created_at, name, wallet_type_uid, user_uid, status, balance)
    VALUES 
        (gen_random_uuid(), CURRENT_TIMESTAMP, 'Кошелек 2-1', wallet_type_id, '22222222-2222-2222-2222-222222222222', 'ACTIVE', 3000.00),
        (gen_random_uuid(), CURRENT_TIMESTAMP, 'Кошелек 2-2', wallet_type_id, '22222222-2222-2222-2222-222222222222', 'ACTIVE', 4000.00);

    -- Создаем тестовые платежные запросы
    -- Для пользователя 1
    INSERT INTO payment_requests (uid, created_at, user_uid, wallet_uid, amount, status)
    SELECT 
        gen_random_uuid(),
        CURRENT_TIMESTAMP,
        '11111111-1111-1111-1111-111111111111',
        uid,
        100.00,
        'PENDING'
    FROM wallets 
    WHERE user_uid = '11111111-1111-1111-1111-111111111111';

    -- Для пользователя 2
    INSERT INTO payment_requests (uid, created_at, user_uid, wallet_uid, amount, status)
    SELECT 
        gen_random_uuid(),
        CURRENT_TIMESTAMP,
        '22222222-2222-2222-2222-222222222222',
        uid,
        200.00,
        'PENDING'
    FROM wallets 
    WHERE user_uid = '22222222-2222-2222-2222-222222222222';

    -- Создаем тестовые транзакции
    -- Для пользователя 1
    INSERT INTO transactions (uid, created_at, user_uid, wallet_uid, wallet_name, amount, type, state, payment_request_uid)
    SELECT 
        gen_random_uuid(),
        CURRENT_TIMESTAMP,
        '11111111-1111-1111-1111-111111111111',
        w.uid,
        w.name,
        100.00,
        'TOPUP',
        'COMPLETED',
        pr.uid
    FROM wallets w
    JOIN payment_requests pr ON pr.wallet_uid = w.uid
    WHERE w.user_uid = '11111111-1111-1111-1111-111111111111';

    -- Для пользователя 2
    INSERT INTO transactions (uid, created_at, user_uid, wallet_uid, wallet_name, amount, type, state, payment_request_uid)
    SELECT 
        gen_random_uuid(),
        CURRENT_TIMESTAMP,
        '22222222-2222-2222-2222-222222222222',
        w.uid,
        w.name,
        200.00,
        'TOPUP',
        'COMPLETED',
        pr.uid
    FROM wallets w
    JOIN payment_requests pr ON pr.wallet_uid = w.uid
    WHERE w.user_uid = '22222222-2222-2222-2222-222222222222';

END $$;

-- Создание тестовых данных для первой базы (transaction_service_db1)
INSERT INTO wallet_types (uid, name, currency_code, status) VALUES
('11111111-1111-1111-1111-111111111111', 'Основной', 'RUB', 'ACTIVE'),
('22222222-2222-2222-2222-222222222222', 'Бонусный', 'RUB', 'ACTIVE');

-- Данные для пользователя с user_uid = 1 (пойдет в первую базу)
INSERT INTO wallets (uid, name, user_uid, wallet_type_uid, balance, status) VALUES
('33333333-3333-3333-3333-333333333333', 'Основной кошелек', '11111111-1111-1111-1111-111111111111', '11111111-1111-1111-1111-111111111111', 1000.00, 'ACTIVE'),
('44444444-4444-4444-4444-444444444444', 'Бонусный кошелек', '11111111-1111-1111-1111-111111111111', '22222222-2222-2222-2222-222222222222', 500.00, 'ACTIVE');

INSERT INTO payment_requests (uid, user_uid, wallet_uid, amount, status) VALUES
('55555555-5555-5555-5555-555555555555', '11111111-1111-1111-1111-111111111111', '33333333-3333-3333-3333-333333333333', 100.00, 'PENDING'),
('66666666-6666-6666-6666-666666666666', '11111111-1111-1111-1111-111111111111', '44444444-4444-4444-4444-444444444444', 200.00, 'COMPLETED');

INSERT INTO transactions (uid, user_uid, wallet_uid, wallet_name, amount, type, state, payment_request_uid) VALUES
('77777777-7777-7777-7777-777777777777', '11111111-1111-1111-1111-111111111111', '33333333-3333-3333-3333-333333333333', 'Основной кошелек', 100.00, 'CREDIT', 'COMPLETED', '55555555-5555-5555-5555-555555555555'),
('88888888-8888-8888-8888-888888888888', '11111111-1111-1111-1111-111111111111', '44444444-4444-4444-4444-444444444444', 'Бонусный кошелек', 200.00, 'DEBIT', 'COMPLETED', '66666666-6666-6666-6666-666666666666');

-- Данные для пользователя с user_uid = 2 (пойдет во вторую базу)
INSERT INTO wallets (uid, name, user_uid, wallet_type_uid, balance, status) VALUES
('99999999-9999-9999-9999-999999999999', 'Основной кошелек', '22222222-2222-2222-2222-222222222222', '11111111-1111-1111-1111-111111111111', 2000.00, 'ACTIVE'),
('aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'Бонусный кошелек', '22222222-2222-2222-2222-222222222222', '22222222-2222-2222-2222-222222222222', 1000.00, 'ACTIVE');

INSERT INTO payment_requests (uid, user_uid, wallet_uid, amount, status) VALUES
('bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb', '22222222-2222-2222-2222-222222222222', '99999999-9999-9999-9999-999999999999', 300.00, 'PENDING'),
('cccccccc-cccc-cccc-cccc-cccccccccccc', '22222222-2222-2222-2222-222222222222', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 400.00, 'COMPLETED');

INSERT INTO transactions (uid, user_uid, wallet_uid, wallet_name, amount, type, state, payment_request_uid) VALUES
('dddddddd-dddd-dddd-dddd-dddddddddddd', '22222222-2222-2222-2222-222222222222', '99999999-9999-9999-9999-999999999999', 'Основной кошелек', 300.00, 'CREDIT', 'COMPLETED', 'bbbbbbbb-bbbb-bbbb-bbbb-bbbbbbbbbbbb'),
('eeeeeeee-eeee-eeee-eeee-eeeeeeeeeeee', '22222222-2222-2222-2222-222222222222', 'aaaaaaaa-aaaa-aaaa-aaaa-aaaaaaaaaaaa', 'Бонусный кошелек', 400.00, 'DEBIT', 'COMPLETED', 'cccccccc-cccc-cccc-cccc-cccccccccccc'); 