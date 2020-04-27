package lasers.model;

import lasers.backtracking.Configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;

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
    }

    public SafeConfig(SafeConfig other, int rows, int cols) {
        this.rows=rows;
        this.cols=cols;
        this.floor=new String[other.rows][other.cols];
    }
    @Override
    public Collection<Configuration> getSuccessors() {
        // TODO
        return null;
    }

    @Override
    public boolean isValid() {
        // TODO
        return false;
    }

    @Override
    public boolean isGoal() {
        // TODO
        return false;
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
}
