# Empire 23  
### A Civilization-Inspired Turn-Based Strategy Game Built Fully from Scratch

**Empire 23** is a turn-based strategy game developed from scratch as a team project in **Java** using **JavaFX** and **Maven**.  
Inspired by the Civilization series, the game allows players to build cities, manage resources, train units, research technologies, interact diplomatically, and compete for dominance on a tile-based world map.

This project was designed and implemented end-to-end by our team, including the **game logic, architecture, UI, data handling, save system, and testing infrastructure**.

---

## Why This Project Stands Out

This was not a template-based assignment or a small demo. We built the project ourselves from the ground up, including:

- A complete **turn-based strategy gameplay loop**
- A structured **MVC-style architecture**
- A **JavaFX graphical interface**
- Core systems for **cities, units, technologies, terrain, and resources**
- **Diplomacy and chat** features
- **Save / load / autosave** support
- Multiple controllers, screens, and test suites
- A large asset pipeline for buildings, units, terrain, and UI components

The result is a fairly large game project that combines **software engineering**, **game system design**, and **GUI development** in one codebase.

---

## Team

- **Reza Heidari**
- **Omid Daliran**

---

## Gameplay Overview

In Empire 23, players control a civilization and make strategic decisions turn by turn. The game includes systems for expansion, development, conflict, and progression.

### Core gameplay systems
- Found and manage cities
- Train and move military and civilian units
- Gather and utilize strategic and luxury resources
- Construct buildings and world wonders
- Progress through a technology tree
- Explore a tile-based map with different terrain types
- Engage in diplomacy with other players
- Compete through long-term strategic development

---

## Main Features

### Strategy & World Simulation
- Tile-based world map
- Different terrain and environmental tile types
- Resource-driven progression
- City growth and management
- Unit production and movement
- Improvement and building systems
- Technology research tree
- Military and civilian unit classes

### Player Systems
- User registration and login
- Profiles and score-related pages
- Save and load support
- Autosave page and save selection flow

### Social / Multiplayer-Oriented Features
- Chat system
- Private chat selection
- Room creation and invitations
- Diplomacy interface

### Interface & Presentation
- JavaFX-based GUI
- Multiple dedicated pages and menus
- Custom CSS styling
- Extensive game assets for:
  - Buildings
  - Units
  - Terrain
  - Technology tree
  - Backgrounds
  - UI icons and overlays

---

## Architecture

The project is structured around a clean multi-layered design with separation between models, controllers, and views.

### Main modules

```text
src/main/java/
├── controller/        # Core game and menu logic
├── model/             # Domain models: city, unit, tile, user, tech, etc.
├── database/          # Static game data and save-related data
├── enums/             # Shared command / enum definitions
├── view/              # Non-graphical or menu-related view classes
└── view_graphic/      # JavaFX GUI pages and visual components
```

### Resources

```text
src/main/resources/
├── css/               # Application styling
├── images/            # Game assets and interface images
└── saves/             # Saved game data
```

### Tests

```text
src/test/java/
├── TestCityController.java
├── TestCivilizationController.java
├── TestMainMenuController.java
├── TestProfileController.java
├── TestRegisterController.java
└── TestUnitController.java
```

---

## Tech Stack

- **Java 17**
- **JavaFX**
- **Maven**
- **JUnit 4 / JUnit 5**
- **Mockito**
- **Gson**

---

## Project Highlights from the Codebase

A quick look at the repository shows that the game includes dedicated support for many substantial mechanics and assets:

- Controllers for:
  - city management
  - civilization logic
  - game flow
  - main menu
  - profile handling
  - registration
  - units

- Models for:
  - cities
  - buildings
  - technologies
  - terrain and tiles
  - users
  - worker / settler / military units
  - world graph structures

- Databases for:
  - buildings
  - improvements
  - resources
  - technologies
  - terrains
  - units
  - save data

- GUI pages for:
  - login
  - profile
  - game menu
  - chat
  - diplomacy
  - room creation
  - save selection
  - score page
  - settings
  - map sizing
  - game screen

That breadth is one of the strongest parts of the project: it is not just a single mechanic, but a full ecosystem of interacting systems.

---

## Screenshots

Add screenshots here to make the repository look much stronger.

```markdown
![Main Menu](docs/screenshots/main-menu.png)
![Game Screen](docs/screenshots/game-screen.png)
![Technology Tree](docs/screenshots/technology-tree.png)
![Diplomacy](docs/screenshots/diplomacy.png)
```

Recommended screenshots:
1. Main menu
2. In-game map
3. City or unit interface
4. Technology tree or diplomacy page

Even 3–4 screenshots will make the repo look far more professional.

---

## Getting Started

### Prerequisites

Make sure you have installed:

- **Java 17**
- **Maven**

---

## Build the Project

```bash
mvn clean install
```

---

## Run the Project

```bash
mvn javafx:run
```

> Note: Depending on the local JavaFX/Maven setup, the JavaFX plugin main class configuration may need adjustment to match the actual application entry point.

---

## Run Tests

```bash
mvn test
```

---

## Repository Structure

```text
project-group-23-main/
├── ProjectUmls/               # UML diagrams for model/view/controller design
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── controller/
│   │   │   ├── database/
│   │   │   ├── enums/
│   │   │   ├── model/
│   │   │   ├── view/
│   │   │   └── view_graphic/
│   │   └── resources/
│   │       ├── css/
│   │       ├── images/
│   │       └── saves/
│   └── test/
│       └── java/
├── pom.xml
└── README.md
```

---

## What We Built Ourselves

A part we are especially proud of is that this project was built **from scratch** by our group.

That includes:
- application structure
- gameplay mechanics
- data models
- menus and GUI flow
- asset integration
- persistence and save logic
- test coverage for core controllers

This project gave us hands-on experience in turning a large idea into a working software system with many interacting components.

---

## What We Learned

Through this project, we gained practical experience in:

- designing large object-oriented systems
- structuring a medium-to-large Java codebase
- separating responsibilities across models, views, and controllers
- building GUI applications with JavaFX
- integrating assets into a real application
- writing tests for logic-heavy components
- collaborating as a team on a non-trivial software project

---

## Future Improvements

There are many directions this project could be expanded further:

- AI-controlled opponents
- stronger multiplayer synchronization
- improved balancing of civilizations and technologies
- better animations and transitions
- fog-of-war and map visibility improvements
- richer diplomacy mechanics
- more advanced combat logic
- polished deployment / packaging

---

## Academic Context

This repository contains a university group project developed as part of our coursework.  
While it was created in an academic setting, the implementation reflects serious effort in both **software engineering** and **system design**, and we are proud of how much was built from the ground up.

---

## License

This project is shared for educational and portfolio purposes.

If you plan to reuse or extend the project, please give appropriate credit to the original team.

---

## Final Note

This project represents a lot more than just a course submission: it reflects the time, design work, debugging, teamwork, and engineering effort required to build a complete strategy game system from scratch.
