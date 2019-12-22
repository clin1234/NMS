package NMS;

import static java.lang.System.err;

import java.util.concurrent.ThreadLocalRandom;

import jdk.jfr.Unsigned;

public class game {
    @Unsigned
    private short mines;
    @Unsigned
    private int r;
    @Unsigned
    private int c;
    Cell[][] grid;

    static final String version = "0.01";

    boolean game_in_progress, random_generated;

    final short[] dir = { -1, 0, 1 };

    private short place_mines(int mines) {
        if (mines < grid.length / 2) {
            err.println("Too few mines. Try again");
            return 12;
        }

        /*
         * XXX: pseudo-random placement. All mines are split as equally as possible
         * between each row, and then placed randomly within each one.
         */

        while (mines > 0) {
            final int row_i = ThreadLocalRandom.current().nextInt(r), col_i = ThreadLocalRandom.current().nextInt(c);
            final int correc = ThreadLocalRandom.current().nextInt(-1, 2);
            if (grid[row_i][col_i].isMined()) {
                if (row_i + correc >= 0 && col_i + correc >= 0 && row_i + correc < r && col_i + correc < c)
                    grid[row_i + correc][col_i + correc].setMined(true);
            } else
                grid[row_i][col_i].setMined(true);
            mines--;
        }

        

        for (short i = 0; i < r; i++) {
            for (short j = 0; j < c; j++) {
                if (grid[i][j].isMined()) {
                    for (short d : dir) {
                        for (short c : dir) {
                            if (check_coord(i+d, j+c)) {
                                if (grid[i + d][j + c].isMined())
                                    ;
                                else
                                    grid[i + d][j + c].num_neighboring_mines += 1;
                            }
                        }
                    }
                }
            }
        }

        return 0;
    }

    public game(final int rows, final int columns, final int mines) {
        r = rows;
        c = columns;
        grid = new Cell[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++)
                grid[i][j] = new Cell();
        }
        place_mines(mines);

        for (short i = 0; i < rows; i++) {
            for (short j = 0; j < columns; j++)
                grid[i][j].setCovered(true);
        }
        game_in_progress = true;
    }

    public String toString() {
        String grid_out = "";
        for (short i = 0; i < r; i++) {
            for (short j = 0; j < c; j++) {
                if (game_in_progress == true) {
                    if (grid[i][j].isCovered()) {
                        grid_out += "*";
                    } else if (grid[i][j].isFlagged()) {
                        grid_out += "f";
                    } else if (grid[i][j].num_neighboring_mines == 0)
                        grid_out += ".";
                    else
                        grid_out += grid[i][j].num_neighboring_mines;
                } else {
                    if (grid[i][j].isMined()) {
                        grid_out += "B";
                    } else if (grid[i][j].num_neighboring_mines == 0)
                        grid_out += ".";
                    else
                        grid_out += grid[i][j].num_neighboring_mines;

                }
                grid_out += " ";
            }
            grid_out += System.lineSeparator();
        }
        return grid_out;
    }

    public boolean check_coord(int row, int col) {
        return (row >= 0 && col >= 0 && row < r && col < c);
    }

    boolean uncover(int row, int col)
    {
        grid[row][col].setCovered(false);
        for(short i:dir)
        {
            for(short j:dir)
            {
                if(check_coord(row+i, col+j))
                {
                    
                }
            }
        }
    }
}

class Cell {
    protected short num_neighboring_mines = 0;
    private boolean covered, mined = false, flagged;

    Cell() {
        covered = false;
        flagged = false;
    }

    public boolean isCovered() {
        return covered;
    }

    public void setCovered(final boolean covered) {
        this.covered = covered;
    }

    public boolean isMined() {
        return mined;
    }

    public void setMined(final boolean mined) {
        this.mined = mined;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }
}
