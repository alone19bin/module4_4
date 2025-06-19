#!/bin/bash

# Скрипт применяет все миграции из папки db/migration к двум базам PostgreSQL
# Перед запуском убедитесь, что переменные окружения PGPASSWORD, PGUSER, PGHOST заданы или используйте -U/-h/-W

MIGRATIONS_DIR="src/main/resources/db/migration"
DBS=(transaction_service_db1 transaction_service_db2)
USER="postgres"
HOST="localhost"

for DB in "${DBS[@]}"; do
  echo "\n=== Применение миграций к базе $DB ==="
  for FILE in $(ls $MIGRATIONS_DIR/V*.sql | sort); do
    echo "\n--- $FILE ---"
    psql -h "$HOST" -U "$USER" -d "$DB" -f "$FILE"
  done
done

echo "\nВсе миграции применены к обеим базам!" 