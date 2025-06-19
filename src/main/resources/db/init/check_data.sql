-- Проверка данных в первой базе (transaction_service_db1)
\c transaction_service_db1

\echo '=== Данные в transaction_service_db1 ==='
\echo '\nКошельки:'
SELECT uid, name, user_uid, balance, status FROM wallets;

\echo '\nПлатежные запросы:'
SELECT uid, user_uid, amount, status FROM payment_requests;

\echo '\nТранзакции:'
SELECT uid, user_uid, amount, type, state FROM transactions;

-- Проверка данных во второй базе (transaction_service_db2)
\c transaction_service_db2

\echo '\n=== Данные в transaction_service_db2 ==='
\echo '\nКошельки:'
SELECT uid, name, user_uid, balance, status FROM wallets;

\echo '\nПлатежные запросы:'
SELECT uid, user_uid, amount, status FROM payment_requests;

\echo '\nТранзакции:'
SELECT uid, user_uid, amount, type, state FROM transactions;

SELECT * FROM wallet_types;
SELECT * FROM wallets;
SELECT * FROM payment_requests;