package NMS;

import static java.lang.System.err;

import java.util.concurrent.ThreadLocalRandom;

import jdk.jfr.Unsigned;

public class game {
    @Unsigned
    private int mines;
    @Unsigned
    private int r;
    @Unsigned
    private int c;
    Cell[][] grid;

    static final String version = "Version 0.02";

    boolean game_in_progress, random_generated;

    final short[] dir = { -1, 0, 1 };

    private void place_mines(int mines) {
        while (mines > 0) {
            final int row_i = ThreadLocalRandom.current().nextInt(r), col_i = ThreadLocalRandom.current().nextInt(c);
            final int correc = ThreadLocalRandom.current().nextInt(-1, 2);
            if (grid[row_i][col_i].isMined()) {
                if (check_coord(row_i + correc, col_i + correc))
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
                            if (check_coord(i + d, j + c)) {
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
    }

    public game(final int rows, final int columns, final int mines) {
        r = rows;
        c = columns;
        grid = new Cell[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++)
                grid[i][j] = new Cell();
        }
        this.mines = mines;
        place_mines(mines);

        for (short i = 0; i < rows; i++) {
            for (short j = 0; j < columns; j++)
                grid[i][j].setCovered(true);
        }
        game_in_progress = true;
    }

    public String toString() {
        String grid_out = "";
        for (short i = 0; i < r; i++)
            grid_out += String.format("%-3d", i);
        grid_out += System.lineSeparator();
        short i, j;
        for (j = 0; j < c; j++) {
            for (i = 0; i < r; i++) {
                if (game_in_progress == true) {

                    if (grid[i][j].isCovered()) {
                        if (grid[i][j].isFlagged())
                            grid_out += "f";
                        else
                            grid_out += "*";
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
                grid_out += "  ";
            }
            grid_out += j + System.lineSeparator();
        }
        return grid_out;
    }

    public boolean check_coord(int row, int col) {
        return (row >= 0 && col >= 0 && row < r && col < c);
    }

    // XXX: unelegant uncovering algorithm.
    boolean isuncovering_success(int row, int col) {
        grid[row][col].setCovered(false);
        if (grid[row][col].isMined())
            return false;
        else {
            for (short i : dir) {
                for (short j : dir) {
                    if (check_coord(row + i, col + j) && (grid[row + i][col + j].isMined() == false)
                            && (grid[row + i][col + j].num_neighboring_mines == 0)) {
                        grid[row + i][col + j].setCovered(false);
                    }
                }
            }
        }
        return true;
    }

    boolean allmines_revealed() {
        short c = 0;
        for (Cell[] co : grid) {
            for (Cell d : co) {
                if (d.isCovered() == false) {
                    c++;
                }
            }
        }
        if (c == (grid.length * grid[0].length) - mines)
            return true;
        return false;
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
