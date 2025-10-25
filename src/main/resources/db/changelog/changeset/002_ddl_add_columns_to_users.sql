--liquibase formatted sql
--changeset ivanm:create_users_table
ALTER TABLE users
    ADD COLUMN first_arg INTEGER,
    ADD COLUMN second_arg INTEGER,
    ADD COLUMN result INTEGER;

