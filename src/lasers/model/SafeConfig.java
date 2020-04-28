package lasers.model;

import lasers.backtracking.Configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * The class represents a single configuration of a safe.  It is
 * used by the backtracker to generate successors, check for
 * validity, and eventually find the goal.
 *
 * This class is given to you here, but it will undoubtedly need to
 * communicate with the model.  You are free to move it into the lasers.model
 * package and/or incorporate it into another class.
 *
 * @author RIT CS
 * @author Promise Omiponle and Parth Paliwal
 */
public class SafeConfig implements Configuration {
    /** The number of rows of tiles on the floor of the Safe. */
    private int rows;

    /** The number of columns of tiles on the floor of the Safe. */
    private int cols;

    private int row;

    private int col;

    /** A 2-D array that represents the tiles of the Safe floor. */
    private String[][] floor;
    private BufferedReader reader;
    public SafeConfig(String filename) throws IOException {
        File safeFile = new File(filename);
        this.reader = new BufferedReader(new FileReader((safeFile)));
        String text = reader.readLine();
        String[] cord = text.split(" ");
        this.rows=Integer.parseInt(cord[0]);
        this.cols=Integer.parseInt(cord[1]);
        this.floor=new String[rows][cols];
        SafeTheBuilder();
        this.row=0;
        this.col=-1;
    }

    public SafeConfig(SafeConfig other, int row, int col) {
        this.row=row;
        this.col=col;
        this.rows=other.rows;
        this.cols=other.cols;
        this.floor=new String[other.rows][other.cols];
        for (int r=0; r<this.rows; r++) {
            this.floor=copy2DArray(other.floor);
        }
        this.floor[this.row][this.col]="L";
    }
    @Override
    public Collection<Configuration> getSuccessors() {
        List<Configuration> successors=new LinkedList<Configuration>();
        for (int row=0; row<this.rows; row++) {
            for (int col=0; col<this.cols; col++) {
                SafeConfig child = new SafeConfig(this, row, col);
                successors.add(child);
            }

        }
        return successors;
    }

    @Override
    public boolean isValid() {
        for(int r=row+1; r<this.rows; r++) {
            if (this.floor[r][col].equals("L")||this.floor[r][col].equals(".")) {
                return false;
            }
        }
        for (int c=col+1; c<this.cols; c++) {
            if (this.floor[row][c].equals("L")||this.floor[row][c].equals(".")) {
                return false;
            }
        }
        if(row!=0) {
            for(int r=row-1; r>=0; r--) {
                if (this.floor[r][col].equals("L")||this.floor[r][col].equals(".")) {
                    return false;
                }
            }
        }
        if (col!=0) {
            for (int c=col-1; c>=0; c--) {
                if (this.floor[row][c].equals("L")||this.floor[row][c].equals(".")) {
                    return false;
                }
            }
        }
        return true;

    }

    @Override
    public boolean isGoal() {
        if (isValid()) {
            for (int r=0; r<this.rows; r++) {
                for (int c=0; c<this.cols; c++) {
                    if (this.floor[r][c].equals("0")||this.floor[r][c].equals("1")||this.floor[r][c].equals("2")
                    ||this.floor[r][c].equals("3")||this.floor[r][c].equals("4")) {
                        int tile=Integer.parseInt(this.floor[row][col]);
                        int lasers=0;
                        if (c+1<this.cols && this.floor[r][c+1].equals("L")) {
                            lasers++;
                        }
                        if (c-1>=0 && this.floor[r][c-1].equals("L")) {
                            lasers++;
                        }
                        if (r+1<this.rows && this.floor[r+1][c].equals("L")) {
                            lasers++;
                        }
                        if ((r-1>=0) && (this.floor[r-1][c].equals("L"))) {
                            lasers++;
                        }
                        if (lasers!=tile) {
                            return false;
                        }
                    } else if (this.floor[r][c].equals(".")) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public void SafeTheBuilder() {
        String curLine;
        String[] splitter;
        try {
            for (int i = 0; i < rows; i++) {
                curLine = reader.readLine();
                splitter = curLine.split(" ");

                for (int j = 0; j < cols; j++) {
                    floor[i][j] = splitter[j];
                }
            }
        } catch (Exception e) {
            System.out.println("Error opening file");
        }
    }
    /**
     * Efficiently makes and returns a copy of the given array.
     *
     * @param array The array to copy.
     *
     * @return The copy of the original array.
     */
    static String[][] copy2DArray(String[][] array) {
        String[][] copy = new String[array.length][];
        for(int i=0; i<array.length; i++) {
            copy[i] = Arrays.copyOf(array[i], array[i].length);
        }
        return copy;
    }
}
