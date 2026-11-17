-- =============================================
-- Lost and Found System - MySQL Schema (Design Reference)
-- =============================================
-- 
-- This schema is designed for the USIU Africa Lost and Found System.
-- Tables are created automatically by the Java application on first run.
-- 
-- =============================================

-- CREATE DATABASE
CREATE DATABASE IF NOT EXISTS LOSTANDFOUNDSYSTEM DEFAULT CHARACTER SET utf8mb4 DEFAULT COLLATE utf8mb4_0900_ai_ci;

USE LOSTANDFOUNDSYSTEM;

-- ==============================================
-- 1. users - authentication and account management
-- ==============================================
CREATE TABLE IF NOT EXISTS users (
    user_id           INT AUTO_INCREMENT PRIMARY KEY,
    full_name         VARCHAR(100)     NOT NULL,
    email             VARCHAR(100)     UNIQUE NOT NULL,
    password_hash     VARCHAR(255)     NOT NULL,
    usiu_id           VARCHAR(20)      UNIQUE NOT NULL,
    phone             VARCHAR(15),
    role              ENUM('STUDENT', 'STAFF', 'ADMINISTRATOR') NOT NULL DEFAULT 'STUDENT',
    is_active         BOOLEAN          DEFAULT TRUE,
    created_at        TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,

    INDEX idx_usiu_id (usiu_id),
    INDEX idx_email (email),
    INDEX idx_role_active (role, is_active)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ==============================================
-- 2. categories - item classification
-- ==============================================
CREATE TABLE IF NOT EXISTS categories (
    category_id       INT AUTO_INCREMENT PRIMARY KEY,
    category_name     VARCHAR(50)      NOT NULL,
    description       VARCHAR(200),
    UNIQUE KEY uk_name (category_name),
    INDEX idx_name (category_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ==============================================
-- 3. locations - where items are lost/found
-- ==============================================
CREATE TABLE IF NOT EXISTS locations (
    location_id       INT AUTO_INCREMENT PRIMARY KEY,
    name              VARCHAR(50)      NOT NULL,
    building          VARCHAR(100),
    UNIQUE KEY uk_name_building (name, building),
    INDEX idx_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ==============================================
-- 4. items - polymorphic base for lost/found
-- ==============================================
CREATE TABLE IF NOT EXISTS items (
    item_id           INT AUTO_INCREMENT PRIMARY KEY,
    title             VARCHAR(100)     NOT NULL,
    description       TEXT,
    category_id       INT              NOT NULL,
    location_id       INT              NOT NULL,
    date_reported     DATETIME         NOT NULL,
    date_occured      DATETIME,
    photo_path        VARCHAR(255),
    status            ENUM('OPEN', 'MATCHED', 'CLAIM_PENDING', 'RETURNED', 'ARCHIVED') DEFAULT 'OPEN',
    report_type       ENUM('LOST', 'FOUND') NOT NULL,

    created_at        TIMESTAMP        DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (category_id)   REFERENCES categories(category_id) ON DELETE RESTRICT,
    FOREIGN KEY (location_id)   REFERENCES locations(location_id)  ON DELETE RESTRICT,

    INDEX idx_report_type_status (report_type, status),
    INDEX idx_category (category_id),
    INDEX idx_location (location_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ==============================================
-- 5. lost_items - lost item specific attributes
-- ==============================================
CREATE TABLE IF NOT EXISTS lost_items (
    item_id           INT PRIMARY KEY,
    reporter_id       INT              NOT NULL,
    distinguishing_features VARCHAR(255),
    reward_offered    DECIMAL(10,2)    DEFAULT 0.00,

    FOREIGN KEY (item_id)     REFERENCES items(item_id) ON DELETE CASCADE,
    FOREIGN KEY (reporter_id) REFERENCES users(user_id) ON DELETE RESTRICT,

    INDEX idx_reporter (reporter_id),
    INDEX idx_reward (reward_offered)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ==============================================
-- 6. found_items - found item specific attributes
-- ==============================================
CREATE TABLE IF NOT EXISTS found_items (
    item_id           INT PRIMARY KEY,
    finder_id         INT,
    storage_location  VARCHAR(100)     NOT NULL,
    verification_question VARCHAR(255)     NOT NULL,
    verification_answer   VARCHAR(255)     NOT NULL,

    FOREIGN KEY (item_id)     REFERENCES items(item_id) ON DELETE CASCADE,
    FOREIGN KEY (finder_id)   REFERENCES users(user_id) ON DELETE SET NULL,

    INDEX idx_finder (finder_id),
    INDEX idx_question (verification_question)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ==============================================
-- 7. claims - claim lifecycle
-- ==============================================
CREATE TABLE IF NOT EXISTS claims (
    claim_id          INT AUTO_INCREMENT PRIMARY KEY,
    claimant_id       INT              NOT NULL,
    found_item_id     INT              NOT NULL,
    answer_provided   VARCHAR(255),
    proof_description TEXT,
    status            ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
    reviewed_by       INT,
    decision_note     TEXT,
    claimed_at        DATETIME         DEFAULT CURRENT_TIMESTAMP,
    reviewed_at       DATETIME,

    FOREIGN KEY (claimant_id)     REFERENCES users(user_id) ON DELETE RESTRICT,
    FOREIGN KEY (found_item_id)   REFERENCES found_items(item_id) ON DELETE RESTRICT,
    FOREIGN KEY (reviewed_by)     REFERENCES users(user_id) ON DELETE SET NULL,

    UNIQUE KEY uk_active_claim (found_item_id, status),

    INDEX idx_claimant (claimant_id),
    INDEX idx_status (status),
    INDEX idx_reviewed_by (reviewed_by)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ==============================================
-- 8. matches - lost-found potential pairs
-- ==============================================
CREATE TABLE IF NOT EXISTS matches (
    match_id          INT AUTO_INCREMENT PRIMARY KEY,
    lost_item_id      INT              NOT NULL,
    found_item_id     INT              NOT NULL,
    score             DECIMAL(5,2)     NOT NULL,
    created_at        DATETIME         DEFAULT CURRENT_TIMESTAMP,
    is_dismissed      BOOLEAN          DEFAULT FALSE,

    FOREIGN KEY (lost_item_id)    REFERENCES lost_items(item_id) ON DELETE CASCADE,
    FOREIGN KEY (found_item_id)   REFERENCES found_items(item_id) ON DELETE CASCADE,

    UNIQUE KEY unique_match (lost_item_id, found_item_id),
    CHECK (score >= 0 AND score <= 100),
    CHECK (is_dismissed IN (TRUE, FALSE)),

    INDEX idx_score (score DESC),
    INDEX idx_dismissed (is_dismissed)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ==============================================
-- 9. notifications - system alerts
-- ==============================================
CREATE TABLE IF NOT EXISTS notifications (
    notification_id   INT AUTO_INCREMENT PRIMARY KEY,
    recipient_id      INT              NOT NULL,
    message           TEXT             NOT NULL,
    type              ENUM('CLAIM_APPROVED', 'CLAIM_REJECTED', 'NEW_MATCH', 'SYSTEM') NOT NULL,
    created_at        DATETIME         DEFAULT CURRENT_TIMESTAMP,
    is_read           BOOLEAN          DEFAULT FALSE,

    FOREIGN KEY (recipient_id) REFERENCES users(user_id) ON DELETE CASCADE,

    INDEX idx_recipient_unread (recipient_id, is_read),
    INDEX idx_type (type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- ==============================================
-- 10. Sample default categories (populated on first run)
-- ==============================================
INSERT IGNORE INTO categories (category_name, description) VALUES
('Electronics', 'Electronic devices and accessories'),
('Clothing', 'Apparel and wearable items'),
('Books', 'Textbooks and notebooks'),
('Keys', 'Keys and access cards'),
('Other', 'Miscellaneous items');