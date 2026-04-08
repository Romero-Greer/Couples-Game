# Couples Trivia Game 🎲

A multiplayer CLI trivia game built with Java and Spring Boot where couples compete to prove how well they know each other. Teams roll dice to determine turn order, then race to match answers on questions about their partner. Built as a personal side project to practice Java fundamentals and explore concepts beyond the bootcamp curriculum — including custom Enum-based state management and JDBC data access patterns.

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Java |
| Framework | Spring Boot |
| Database | PostgreSQL |
| Data Access | JDBC / JdbcTemplate |
| Build Tool | Maven |
| Interface | Command Line (CLI) |

---

## Features

- **Team Management** — Add, update, remove, and view up to 4 teams (2 players each) with full CRUD operations persisted to PostgreSQL
- **Dice Roll-Off** — All teams roll dice at game start; a custom sorting algorithm determines turn order based on results
- **Question Engine** — Questions are randomly selected from the `question_cards` table each round
- **Turn-Based Gameplay** — Player 2 turns away while Player 1 answers, then Player 2 submits their guess — answers are compared for a match
- **Type-Safe Game State** — A custom `GamePhase` enum manages 8 distinct application states from `STARTUP` through `GAME_OVER`
- **Referential Integrity** — Cascading delete logic ensures players are removed before their team, maintaining database consistency

---

## Project Architecture

```
src/main/java/game/
├── CLI/           # CommandLineRunner — user interaction and menu navigation
├── logic/         # GameEngine + GamePhase enum — core game logic and state management
├── dao/           # JDBC DAOs — database access for Players, Teams, and QuestionCards
└── model/         # Domain models — Players, Teams, QuestionCard
```

This 4-layer structure enforces clean separation of concerns — the CLI layer handles I/O, the logic layer owns game state, the DAO layer owns persistence, and the model layer defines domain objects.

---

## Database Setup

### Prerequisites
- PostgreSQL installed and running
- pgAdmin or `psql` CLI access

### Steps

1. Open pgAdmin (or your preferred PostgreSQL client)
2. Create a new database called `couples_game` (or your preferred name)
3. Run the provided setup script:

```sql
-- Run database.sql in your PostgreSQL client
```

The script will:
- Create the `teams`, `players`, and `question_cards` tables
- Seed the database with 15 sample trivia questions

### Schema Overview

```
teams
├── team_id   SERIAL PRIMARY KEY
└── team_name VARCHAR(100)

players
├── player_id SERIAL PRIMARY KEY
├── name      VARCHAR(100)
└── team_id   INT → teams(team_id) ON DELETE CASCADE

question_cards
├── card_id  SERIAL PRIMARY KEY
└── question VARCHAR(500)
```

---

## Configuration

Update `src/main/resources/application.properties` with your PostgreSQL credentials:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/couples_game
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.datasource.driver-class-name=org.postgresql.Driver
```

---

## Running the Application

```bash
# Clone the repository
git clone https://github.com/Romero-Greer/Couples-Game.git
cd Couples-Game

# Run with Maven
mvn spring-boot:run
```

The CLI will launch in your terminal automatically.

---

## Gameplay

```
Welcome to the newest Couples Game! Time to find out which couple knows each other the best...

Main Menu
(0): Start Game
(1): Add Team
(2): Update Team
(3): Remove Team
(4): View Teams
(5): Quit Game
```

**Setup:** Add up to 4 teams (each with 2 players) from the main menu before starting.

**Roll-Off:** When the game starts, each team rolls a die. Results are stored in a `HashMap<Integer, Integer>` and sorted using a `Comparator` lambda to determine turn order — highest roll goes first.

**Taking a Turn:**
1. Player 2 turns away from the screen
2. Player 1 reads the question and submits their answer
3. Player 2 reads the same question and submits their guess
4. Answers are compared — a match is celebrated, a miss is called out!

---

## What I Learned

This project was built independently to practice concepts beyond the bootcamp curriculum:

- **Java Enum classes** — `GamePhase` was my first implementation of a custom Enum for type-safe state management across 8 game states
- **HashMap and Comparator** — Used a `HashMap<Integer, Integer>` with a custom `Comparator` lambda to implement the roll-off sorting algorithm
- **Spring Boot CommandLineRunner** — Learned how to wire a CLI application into the Spring Boot lifecycle
- **Cascading deletes** — Implemented the correct SQL and DAO-layer logic to maintain referential integrity when removing teams and their associated players

---

## Future Improvements

- [ ] Add a scoring system to track points across rounds
- [ ] Add a win condition (first team to X points wins)
- [ ] Migrate from CLI to a REST API + React frontend
- [ ] Add more question categories
- [ ] Support for tiebreaker roll-offs

---

## Author

**Romero Greer**
[LinkedIn](https://www.linkedin.com/in/romero-greer/) · [GitHub](https://github.com/Romero-Greer)
