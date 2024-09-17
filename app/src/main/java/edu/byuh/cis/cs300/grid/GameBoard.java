package edu.byuh.cis.cs300.grid;

public class GameBoard {

	private final int DIM = 5;
	private Player[][] grid;
	private Player whoseTurnIsIt;

	/**
	 * Constructs a 5x5 game board with all cells initially set to BLANK.
	 * X starts as the first player.
	 */
	public GameBoard() {
		//create a 5x5 gameboard of BLANK cells
		grid = new Player[DIM][DIM];
		for (int i=0; i<DIM; ++i) {
			for (int j=0; j<DIM; ++j) {
				grid[i][j] = Player.BLANK;
			}
		}

		//arbitrarily, we make X the first player.
		whoseTurnIsIt = Player.X;
	}

	/**
	 * Submits a move for the current player and updates the game board.
	 *
	 * @param move The move to be submitted ('1'-'5' for columns, 'A'-'E' for rows).
	 * @param p The player making the move (X or O).
	 */
	public void submitMove(char move, Player p) {
		if (move >= '1' && move <= '5') {
			//vertical move, move tokens down
			int col = (int)(move - '1');
			Player newVal = p;
			for (int i=0; i<DIM; ++i) {
				if (grid[i][col] == Player.BLANK) {
					grid[i][col] = newVal;
					break;
				} else {
					Player tmp = grid[i][col];
					grid[i][col] = newVal;
					newVal = tmp;
				}
			}
			
		} else { //A-E
			//horizontal move, move tokens right
			int row = move - 'A';
			Player newVal = p;
			for (int i=0; i<DIM; ++i) {
				if (grid[row][i] == Player.BLANK) {
					grid[row][i] = newVal;
					break;
				} else {
					Player tmp = grid[row][i];
					grid[row][i] = newVal;
					newVal = tmp;
				}
			}	
		}

		//change whose turn it is, switch players
		whoseTurnIsIt = (whoseTurnIsIt == Player.X) ? Player.O : Player.X;
	}

	/**
	 * Checks if a player has won the game.
	 *
	 * @return An integer representing the winning player (X or O), TIE, or BLANK if no winner yet.
	 */
	public int checkForWin() {
		int winner = Player.BLANK.ordinal();
		int winnerO = Player.BLANK.ordinal(); //new variable
		int winnerX = Player.BLANK.ordinal(); //new variable

		//check all rows
		for (int i=0; i<DIM; ++i) {
			if (grid[i][0] != Player.BLANK) {
				Player currentCell = grid[i][0]; //new code 
				boolean win = true; //new code assume win
				for (int j=0; j<DIM; ++j) {
					if (grid[i][j] != currentCell) {
						//winner = Player.BLANK; //old code
						win = false; //new code set win to false
						break;
					}
				}
				//old code
				/*if (winner != Player.BLANK) {
					return winner; //5 in a row!
				}*/

				//new code to check for tie
				if (win) {
					if (currentCell == Player.X) {
						winnerX = Player.X.ordinal();
					} else {
						winnerO = Player.O.ordinal();
					}
				}
			}
		}
		
		//check all columns
		for (int i=0; i<DIM; ++i) {
			if (grid[0][i] != Player.BLANK) {
				Player currentCell = grid[0][i];
				boolean win = true; //new code assume win
				for (int j=0; j<DIM; ++j) {
					if (grid[j][i] != currentCell) {
						//winner = Player.BLANK; //old code
						win = false; //new code set win to false
						break;
					}
				}
				//old code
				/*if (winner != Player.BLANK) {
					return winner; //5 in a column!
				}*/

				//new code to check for tie
				if (win) {
					if (currentCell == Player.X) {
						winnerX = Player.X.ordinal();
					} else {
						winnerO = Player.O.ordinal();
					}
				}
				
			}
		}
		
		//check top-left -> bottom-right diagonal
		if (grid[0][0] != Player.BLANK) {
			winner = grid[0][0].ordinal();
			for (int i=0; i<DIM; ++i) {
				if (grid[i][i].ordinal() != winner) {
					winner = Player.BLANK.ordinal();
					break;
				}
			}
			if (winner != Player.BLANK.ordinal()) {
				return winner; //5 in a diagonal!
			}
		}

		//check bottom-left -> top-right diagonal
		if (grid[DIM-1][0] != Player.BLANK) {
			winner = grid[DIM-1][0].ordinal();
			for (int i=0; i<DIM; ++i) {
				if (grid[DIM-1-i][i].ordinal() != winner) {
					winner = Player.BLANK.ordinal();
					break;
				}
			}
			if (winner != Player.BLANK.ordinal()) {
				return winner; //5 in a diagonal!
			}
		}
		//new code to check for tie
		if (winnerX == Player.X.ordinal() && winnerO == Player.O.ordinal()) {
			return Player.TIE.ordinal();
		} else if (winnerX == Player.X.ordinal()) {
			return Player.X.ordinal();
		} else if (winnerO == Player.O.ordinal()) {
			return Player.O.ordinal();
		} else {
			return winner;
		}
		//return winner; //old code
	}

	/**
	 * Draws the current state of the game board to the console.
	 */
	public void consoleDraw() {
		System.out.print("  ");
		for (int i=0; i<DIM; ++i) {
			System.out.print(i+1);
		}
		System.out.println();
		System.out.print(" /");
		for (int i=0; i<DIM; ++i) {
			System.out.print("-");
		}
		System.out.println("\\");
		for (int i=0; i<DIM; ++i) {
			System.out.print(((char)('A'+i)) + "|");
			for (int j=0; j<DIM; ++j) {
				if (grid[i][j] == Player.BLANK) {
					System.out.print(" ");
				} else {
					System.out.print(grid[i][j].toString());
				}
			}
			System.out.println("|");
		}
		System.out.print(" \\");
		for (int i=0; i<DIM; ++i) {
			System.out.print("-");
		}
		System.out.println("/");
		
	}

	/**
	 * Gets the current player whose turn it is.
	 *
	 * @return The ordinal value of the current player (X or O).
	 */
	public int getCurrentPlayer() {
		return whoseTurnIsIt.ordinal();
	}
}
