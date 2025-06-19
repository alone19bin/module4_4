-- Проверка данных в transaction_service_db2
SELECT '=== Данные в transaction_service_db2 ===' as info;

SELECT 'Кошельки:' as table_name;
SELECT uid, name, user_uid, balance, status FROM wallets;

SELECT 'Платежные запросы:' as table_name;
SELECT uid, user_uid, amount, status FROM payment_requests;

SELECT 'Транзакции:' as table_name;
SELECT uid, user_uid, amount, type, state FROM transactions; 