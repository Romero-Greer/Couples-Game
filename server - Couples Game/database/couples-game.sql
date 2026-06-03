-- ============================================================
-- Couples Trivia Game — Database Setup Script
-- Database: PostgreSQL
-- Run this script in pgAdmin or via psql before starting the app
-- ============================================================

-- Drop tables if they exist (for clean setup)
DROP TABLE IF EXISTS players CASCADE;
DROP TABLE IF EXISTS scores CASCADE;
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
    card_id   SERIAL PRIMARY KEY,
    question  VARCHAR(500) NOT NULL,
    card_type VARCHAR(20)  NOT NULL DEFAULT 'standard'
);

-- ============================================================
-- SCORES TABLE
-- ============================================================
CREATE TABLE scores (
    score_id SERIAL PRIMARY KEY,
    team_id  INT NOT NULL UNIQUE REFERENCES teams(team_id) ON DELETE CASCADE,
    score    INT NOT NULL DEFAULT 0
);

-- ============================================================
-- QUESTION DATA (44 standard + 3 wildcard_self + 3 wildcard_teams = 50 total)
-- ============================================================
INSERT INTO question_cards (question, card_type) VALUES
    -- Standard questions
    ('What is your partner''s favorite food?', 'standard'),
    ('What is your partner''s dream vacation destination?', 'standard'),
    ('What is your partner''s biggest pet peeve?', 'standard'),
    ('What is your partner''s favorite movie?', 'standard'),
    ('What is your partner''s favorite hobby?', 'standard'),
    ('What would your partner order at a restaurant?', 'standard'),
    ('What is your partner''s favorite season?', 'standard'),
    ('What is your partner most afraid of?', 'standard'),
    ('What is your partner''s favorite childhood memory?', 'standard'),
    ('What is your partner''s go-to comfort food?', 'standard'),
    ('What song would your partner say is their anthem?', 'standard'),
    ('What is your partner''s favorite way to spend a Sunday?', 'standard'),
    ('What would your partner do with a surprise day off?', 'standard'),
    ('What is your partner''s most used emoji?', 'standard'),
    ('What is one thing your partner can''t live without?', 'standard'),
    ('What is your partner''s love language?', 'standard'),
    ('What would your partner''s dream job be?', 'standard'),
    ('What is your partner''s biggest guilty pleasure?', 'standard'),
    ('What sport does your partner wish they were better at?', 'standard'),
    ('What is your partner''s favorite TV show?', 'standard'),
    ('What is your partner''s most embarrassing moment?', 'standard'),
    ('What is your partner''s favorite book?', 'standard'),
    ('What is your partner''s go-to karaoke song?', 'standard'),
    ('What is one thing your partner would save in a fire (besides people and pets)?', 'standard'),
    ('What is your partner''s favorite type of music?', 'standard'),
    ('What is your partner''s worst habit?', 'standard'),
    ('What is your partner''s favorite board game?', 'standard'),
    ('What is your partner''s dream car?', 'standard'),
    ('Would your partner choose a beach vacation or a mountain vacation?', 'standard'),
    ('What is your partner''s hidden talent?', 'standard'),
    ('What is your partner''s least favorite household chore?', 'standard'),
    ('What is your partner''s favorite dessert?', 'standard'),
    ('What superpower would your partner choose?', 'standard'),
    ('What city would your partner move to if they could live anywhere?', 'standard'),
    ('What is your partner''s go-to fast food order?', 'standard'),
    ('What language would your partner most want to learn?', 'standard'),
    ('What is your partner''s most prized possession?', 'standard'),
    ('What was your partner''s first job?', 'standard'),
    ('What are your partner''s grandparents'' names?', 'standard'),
    ('What is your partner''s biggest fear?', 'standard'),
    ('What is your partner''s favorite childhood TV show?', 'standard'),
    ('What is your partner''s favorite way to exercise?', 'standard'),
    ('What is something your partner says all the time?', 'standard'),
    ('What is your partner''s morning routine like?', 'standard'),
    -- Wildcard Self: answerer secretly types their own question
    ('[WILDCARD] You choose the question!', 'wildcard_self'),
    ('[WILDCARD] You choose the question!', 'wildcard_self'),
    ('[WILDCARD] You choose the question!', 'wildcard_self'),
    -- Wildcard Teams: other teams secretly submit questions, one is randomly selected
    ('[WILDCARD] Other teams choose the question!', 'wildcard_teams'),
    ('[WILDCARD] Other teams choose the question!', 'wildcard_teams'),
    ('[WILDCARD] Other teams choose the question!', 'wildcard_teams');
