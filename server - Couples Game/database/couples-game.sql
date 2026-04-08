-- ============================================================
-- Couples Trivia Game — Database Setup Script
-- Database: PostgreSQL
-- Run this script in pgAdmin or via psql before starting the app
-- ============================================================

-- Drop tables if they exist (for clean setup)
DROP TABLE IF EXISTS players CASCADE;
DROP TABLE IF EXISTS question_cards CASCADE;
DROP TABLE IF EXISTS teams CASCADE;

-- ============================================================
-- TEAMS TABLE
-- ============================================================
CREATE TABLE teams (
    team_id SERIAL PRIMARY KEY,
    team_name VARCHAR(100) NOT NULL
);

-- ============================================================
-- PLAYERS TABLE
-- ============================================================
CREATE TABLE players (
    player_id SERIAL PRIMARY KEY,
    name      VARCHAR(100) NOT NULL,
    team_id   INT NOT NULL,
    CONSTRAINT fk_team
        FOREIGN KEY (team_id)
        REFERENCES teams (team_id)
        ON DELETE CASCADE
);

-- ============================================================
-- QUESTION_CARDS TABLE
-- ============================================================
CREATE TABLE question_cards (
    card_id  SERIAL PRIMARY KEY,
    question VARCHAR(500) NOT NULL
);

-- ============================================================
-- SAMPLE QUESTION DATA
-- ============================================================
INSERT INTO question_cards (question) VALUES
    ('What is your partner''s favorite food?'),
    ('What is your partner''s dream vacation destination?'),
    ('What is your partner''s biggest pet peeve?'),
    ('What is your partner''s favorite movie?'),
    ('What is your partner''s favorite hobby?'),
    ('What would your partner order at a restaurant?'),
    ('What is your partner''s favorite season?'),
    ('What is your partner most afraid of?'),
    ('What is your partner''s favorite childhood memory?'),
    ('What is your partner''s go-to comfort food?'),
    ('What song would your partner say is their anthem?'),
    ('What is your partner''s favorite way to spend a Sunday?'),
    ('What would your partner do with a surprise day off?'),
    ('What is your partner''s most used emoji?'),
    ('What is one thing your partner can''t live without?');
