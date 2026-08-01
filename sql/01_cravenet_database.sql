-- ============================================================
-- CRAVENET - Database 1: cravenet_database
-- Purpose : User authentication (Login / Registration)
-- Run this file FIRST in MySQL Workbench / command line
-- ============================================================

CREATE DATABASE IF NOT EXISTS cravenet_database;
USE cravenet_database;

-- Drop existing table to allow clean re-run
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id       INT AUTO_INCREMENT PRIMARY KEY,
    User_ID  VARCHAR(50)  NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email    VARCHAR(100) NOT NULL UNIQUE
);

-- ============================================================
-- Uncomment to add a test user (password: test123)
-- ============================================================
-- INSERT INTO users (User_ID, password, email)
-- VALUES ('testuser', 'test123', 'test@example.com');

SELECT 'cravenet_database setup complete.' AS status;
