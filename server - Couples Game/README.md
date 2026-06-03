# Couples Game

A CLI-based party game for couples to test how well they know each other. Teams take turns answering personal questions while their partner tries to guess the answer — first team to 3 points wins.

---

## Features

- 1–4 teams, each with 2 players
- Dice roll-off to determine turn order (with tie-breaker handling)
- 44 standard questions + wildcard cards
- Fuzzy answer matching (Levenshtein distance, 60% similarity threshold)
- Scoreboard tracking with win condition
- Full team management (add, update, remove, view)

### Card Types

| Type | Description |
|---|---|
| `standard` | Pre-written question about the answerer |
| `wildcard_self` | Answerer creates their own question |
| `wildcard_teams` | Other teams secretly submit a question |

---

## Tech Stack

- **Java** with **Spring Boot 3.4.1**
- **PostgreSQL** (via Spring JDBC / JdbcTemplate)
- **Gradle 9** build system

---

## Prerequisites

- Java JDK 17+
- PostgreSQL running on `localhost:5432`

---

## Setup

**1. Create the database**

```sql
CREATE DATABASE "couples-game";
```

**2. Run the schema and seed script**

```bash
psql -U postgres -d couples-game -f database/couples-game.sql
```

**3. Configure credentials**

Update `src/main/resources/application.properties` with your PostgreSQL credentials:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/couples-game
spring.datasource.username=postgres
spring.datasource.password=${DB_PASSWORD}
```

Then set the environment variable before running:

```bash
# macOS/Linux
export DB_PASSWORD=your_password

# Windows (PowerShell)
$env:DB_PASSWORD="your_password"
```

---

## Build & Run

```bash
# Build
./gradlew build          # macOS/Linux
gradlew.bat build        # Windows

# Run
./gradlew bootRun        # macOS/Linux
gradlew.bat bootRun      # Windows
```

---

## How to Play

1. **Add teams** — each team needs a name and 2 player names
2. **Start the game** — teams roll dice to determine turn order
3. **Take turns** — one player answers a question about themselves while their partner looks away, then the partner guesses
4. **Score points** — a correct guess (within 60% string similarity) earns 1 point
5. **Win** — first team to reach 3 points wins

---

## Project Structure

```
src/main/java/game/
├── Main.java                   # Spring Boot entry point
├── CLI/
│   └── GameCLI.java            # CLI menus and user interaction
├── model/
│   ├── Teams.java
│   ├── Players.java
│   ├── QuestionCard.java
│   └── Scores.java
├── dao/                        # JDBC data access layer
│   ├── JdbcTeamsDao.java
│   ├── JdbcPlayersDao.java
│   ├── JdbcQuestionCardDao.java
│   └── JdbcScoresDao.java
└── logic/
    ├── GameEngine.java         # Core game loop and state management
    ├── GamePhase.java          # Game state enum
    └── StringSimilarity.java   # Fuzzy answer matching

database/
└── couples-game.sql            # Schema + seed data (50 question cards)
```

---

## Database Schema

```
teams ──────────────── players
  team_id (PK)           player_id (PK)
  team_name              name
                         team_id (FK → teams, CASCADE)

question_cards             scores
  card_id (PK)               score_id (PK)
  question                   team_id (FK → teams, CASCADE, UNIQUE)
  card_type                  score (default 0)
```
