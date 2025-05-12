# Minesweeper CLI Application

This is a command-line interface (CLI) application that simulates the classic game of Minesweeper. Users can define the grid size and the number of mines, then attempt to clear the board by uncovering squares without detonating any mines. The game provides feedback after each move and indicates win/loss conditions.

# Assumptions

1.  **Maximum Mine Density**: The number of mines cannot exceed 35% of the total squares on the grid. This is implicitly configured and enforced.
2.  **Minimum Grid Size**: The grid size must be at least 2x2.
3.  **Input Format**: Cell coordinates are expected in an alphanumeric format (e.g., "A1", "B3"), case-insensitive.
4.  **Safe First Move**: The first square selected by the user is guaranteed not to be a mine. If a mine is initially at the chosen location, the board is re-generated to ensure the first click is safe.
5.  **"Play Again" Prompt**: After a game concludes, pressing any key (and Enter) will start a new game.
6.  **Square Grid**: The game is played on a square grid (N x N).

# Retrospective

Developing this Minesweeper application was a valuable exercise in applying core software engineering principles.

-   **Learnings**:
    -   Reinforced understanding of Object-Oriented Programming (OOP) principles, such as encapsulation (e.g., `Cell` state) and Single Responsibility Principle (e.g., `BoardPrinter` for display, `InputValidator` for validation).
    -   The benefits of Test-Driven Development (TDD) or at least writing comprehensive unit tests became evident. Using JUnit and Mockito helped ensure the correctness of critical game logic, including mine placement, cell revealing, cascading (flood-fill) behavior, and win/loss conditions.
    -   Designing with modularity by separating concerns into distinct classes (models, controller, services, view) led to a more maintainable and understandable codebase.

-   **Challenges & Design Decisions**:
    -   Implementing the "safe first move" guarantee required careful consideration. The chosen approach—regenerating the board if the randomly placed mines conflict with the first click—ensures fairness to the player without overly complicating the initial mine placement algorithm or requiring knowledge of the first click before generation.
    -   The cascading reveal for cells with zero adjacent mines (flood fill) needed a robust recursive implementation within the `Board` class to correctly uncover all connected empty areas and their numbered neighbors.
    -   Managing game state (e.g., `revealedCount`, `firstMove`) and coordinating interactions between the `GameController`, `Board`, and `GameView` were key to the application's flow.

Further improvements could involve exploring more advanced features or having a GUI, but the current CLI version provides a solid foundation and a complete game experience as per the requirements.

# Application Details

## Overview

This is a Java-based command-line application that implements the Minesweeper game. It uses Maven for project management and dependencies. The application logic is structured into model, view, controller, and service packages to promote separation of concerns.

---

## Features

-   **Customizable Grid**: Play on a square grid of user-defined size.
-   **Adjustable Difficulty**: Specify the number of mines to be randomly placed on the grid (up to 35% of total squares).
-   **Random Mine Placement**: Mines are randomly distributed for each new game.
-   **Guaranteed Safe First Move**: The first square selected by the user will never contain a mine.
-   **Interactive Gameplay**:
    -   Users select squares to uncover using coordinates (e.g., A1).
    -   If a selected square contains a mine, the game is over (loss).
    -   If a safe square is uncovered, it reveals a number indicating how many of its 8 adjacent squares contain mines.
    -   If an uncovered square has no adjacent mines (shows '0'), the program automatically uncovers all adjacent squares until it reaches squares that do have adjacent mines (cascade/flood-fill).
-   **Win Condition**: The game is won when all non-mine squares have been uncovered.
-   **CLI Display**: The game grid and game status are displayed in the console, updating after each user input.
-   **Play Again**: Option to start a new game after one concludes.

---

## Tech Stack

-   **Language:** Java (Targeting JDK 21 as per `pom.xml`)
-   **Build Tool:** Apache Maven
-   **Environment:** Windows
-   **Testing Libraries:**
    -   JUnit 5 (Jupiter)
    -   Mockito

---

## Prerequisites

-   Java Development Kit (JDK): Version 21 or later.
-   Apache Maven: Version 3.9.x or later (for building from source and running tests).

---

## Installation & Setup

1.  **Clone/Download the repository:**
    Obtain the project source code. If it's a git repository:
    ```bash
    git clone GIC-Minesweeper.git
    cd GIC-Minesweeper
    ```
    Otherwise, extract the provided source code zip file.

2.  **Install dependencies & Build:**
    Navigate to the root directory of the project (where `pom.xml` is located) and run:
    ```bash
    mvn clean package
    ```
    This command will:
    - Clean previous builds.
    - Compile the source code.
    - Run unit tests.
    - Package the application into an executable JAR file (e.g., `GIC-Minesweeper-1.0-SNAPSHOT.jar`) in the `target/` directory.

3.  **Environment Variables:**
    This application does not require any specific environment variables to be set for its core functionality.

---

## Running the Application

1.  **Using the executable JAR (Recommended):**
    After successfully building the project (see Installation & Setup):
    - Navigate to the `target/` directory:
      ```bash
      cd target
      ```
    - Execute the JAR file:
      ```bash
      java -jar GIC-Minesweeper-1.0-SNAPSHOT.jar
      ```
      (Replace `GIC-Minesweeper-1.0-SNAPSHOT.jar` with the actual name of the JAR file generated if it differs).

2.  **Using Maven Exec Plugin:**
    Alternatively, you can run the application directly using Maven from the project's root directory:
    ```bash
    mvn exec:java -D exec.mainClass="com.filbertgoh.minesweeper.Main"
    ```

The game will then start in your console, prompting you for grid size and number of mines.

---

## Running Tests

To run the unit tests included with the project:
1.  Navigate to the root directory of the project (where `pom.xml` is located).
2.  Execute the Maven test command:
    ```bash
    mvn test
    ```
Test results will be displayed in the console, and detailed reports are typically generated in the `target/surefire-reports/` directory.