# Football Betting App ⚽

Web application for managing football matches and placing bets.
Built with Java + Spring Boot (MVC) + Thymeleaf.

## Why this project
This project demonstrates a typical Spring MVC application with:
- clean separation (Controller → Service → Repository)
- validation and domain exceptions
- role-based admin flow (create/finish matches) and automatic bet resolution

## Features

### User
- Register / Login
- View upcoming matches
- Place a bet (stake + bet type)
- View “My Bets” with status: **PENDING / WON / LOST**

### Admin
- Create matches
- Finish matches (set result) via admin screen
- Automatically resolve all related bets when a match is finished

## Tech Stack
- Java <version> (recommended: 17)
- Spring Boot, Spring MVC, Spring Data JPA ,Spring Security 
- Thymeleaf, HTML/CSS
- MySQL 
- Maven

## Screenshots
Add UI screenshots here (recommended 5 images):

| Page | Preview |
|------|---------|
| Matches | ![](docs/images/01-matches.png) |
| Place Bet | ![](docs/images/02-place-bet.png) |
| My Bets | ![](docs/images/03-my-bets.png) |
| Admin: Create Match | ![](docs/images/04-admin-create.png) |
| Admin: Finish Match | ![](docs/images/05-admin-finish.png) |

## Demo Flow (2 minutes)
1. Register + login as User
2. Open Matches → select match → place bet
3. Login as Admin → finish the match
4. Back as User → “My Bets” shows updated status (WON/LOST)

## Domain Highlights (what matters)
- **Bet validation**: stake must be positive, user must be allowed to bet, match must be OPEN, etc.
- **Match lifecycle**: OPEN → FINISHED (finished matches cannot be bet on)
- **Bet resolution** happens when a match is finished (service layer logic)

## Getting Started (Local)

### Prerequisites
- Java 21
- MySQL running locally (or Docker)
- Maven

### 1) Clone
```bash
git clone <repo-url>
cd <repo-folder>
