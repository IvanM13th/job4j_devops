--liquibase formatted sql
--changeset ivanm:create_users_table
ALTER TABLE users ADD COLUMN create_date TIMESTAMP WITHOUT TIME ZONE DEFAULT now();

--rollback ALTER TABLE users DROP COLUMN create_date;

