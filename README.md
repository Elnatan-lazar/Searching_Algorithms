# AI Search Algorithms – 3x3 Circular Board Puzzle

This project implements search algorithms to solve a 3×3 circular board puzzle involving colored balls, blocked cells, and empty spaces.

The goal is to transform a **start configuration** into a **goal configuration** with minimal cost, according to predefined movement rules and tile costs.

---

## ✔ Implemented Algorithms

- ✅ **BFS (Breadth-First Search)**
- ✅ **IDA\*** (Iterative Deepening A*)
- ✅ **DFID** 
- ✅ **A\*** 
- ✅ **DFBnB**  

---

# Problem Description

The board is a 3×3 grid containing:

- 2 Red balls (`R`)
- 2 Green balls (`G`)
- 2 Blue balls (`B`)
- Blocked cells (`X`)
- Empty cell (`_`)

The objective is to reach the goal board configuration with minimum total movement cost.

---

# Movement Rules

- Movement is allowed **up / down / left / right** only.
- The board is **circular (wrap-around)**:
  - Moving up from row 1 goes to row 3.
  - Moving left from column 1 goes to column 3.
- A ball can move only into an empty cell (`_`).
- Movement into a blocked cell (`X`) is not allowed.

---

# Movement Cost

| Color | Cost |
|-------|------|
| Blue (`B`)  | 1 |
| Green (`G`) | 3 |
| Red (`R`)   | 10 |

The total solution cost is the sum of the moved ball costs.

---

# Project Structure

```
.
├── Ex1.java        # Main class – input reading, algorithm selection, output writing
├── State.java      # State representation and board operations
├── input.txt       # Input file (must be in same directory)
└── output.txt      # Output file (generated after execution)
```

---

# How to Run

Make sure `input.txt` is in the same directory.

Compile and run:

```bash
javac *.java
java Ex1
```

The program will create:

```
output.txt
```

---

# Input Format (input.txt)

Structure:

```
<Algorithm Name>
<with time / no time>
<with open / no open>
<Start Board Row 1>
<Start Board Row 2>
<Start Board Row 3>
Goal state:
<Goal Board Row 1>
<Goal Board Row 2>
<Goal Board Row 3>
```

Example:

```
DFID
with time
with open
X,R,X
X,B,_
X,G,X
Goal state:
X,R,X
X,G,_
X,B,X
```

---

# Output Format (output.txt)

Structure:

```
<path OR "no path">
Num: <number of generated nodes>
Cost: <cost OR inf>
<time in seconds> (only if "with time")
```

Example:

```
no path
Num: 61
Cost: inf
0.149 seconds
```

---

# Action Format

Each move is printed as:

```
(rowFrom,colFrom):Color:(rowTo,colTo)
```

Example:

```
(2,2):B:(2,3)--(2,3):B:(1,3)
```

- Indexing is **1-based** (1–3).
- Moves are separated by `--`.

---

# Code Overview

## Ex1.java

Responsible for:

- Reading `input.txt`
- Parsing start and goal boards
- Selecting the requested algorithm
- Measuring runtime (if required)
- Writing `output.txt`

Main components:

- `main()` – Controls execution flow
- `BFS()` – Breadth-First Search implementation
- `IDA_STAR()` – Iterative Deepening A*
- `hurstic()` – Heuristic function (misplaced tile cost)

---

## State.java

Represents a search node:

- `char[][] board`
- `int cost`
- `String actions`

Important methods:

- `copyBoardWithSwap()` – Creates new state by swapping positions
- `equals()` / `hashCode()` – Used for state comparison
- `boardToString()` – String representation for hashing
- `toString()` – Debug printing

---

# Heuristic Function (for IDA*)

The heuristic:

- Iterates over the board.
- If a tile is not `_` or `X` and is not in the correct goal position,
  it adds the movement cost of that tile.

This heuristic is admissible because it never overestimates the true cost.

---

# Known Limitations

- Program expects fixed file names:
  - `input.txt`
  - `output.txt`

---

# Academic Context

This project was developed as part of an Artificial Intelligence Search assignment.
