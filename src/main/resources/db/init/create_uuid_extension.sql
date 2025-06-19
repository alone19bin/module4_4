-- Создание расширения uuid-ossp в первой базе
\c transaction_service_db1
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- Создание расширения uuid-ossp во второй базе
\c transaction_service_db2
CREATE EXTENSION IF NOT EXISTS "uuid-ossp"; 