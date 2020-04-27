package lasers.model;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * The model of the lasers safe.  You are free to change this class however
 * you wish, but it should still follow the MVC architecture.
 *
 * @author RIT CS
 * @author PROMISE OMIPONLE, PARTH PALIWAL
 */
public class LasersModel {
    /** the observers who are registered with this model */
    private List<Observer<LasersModel, ModelData>> observers;

    /** The number of rows of tiles on the floor of the Safe. */
    private int rows;

    /** The number of columns of tiles on the floor of the Safe. */
    private int cols;

    /** A 2-D array that represents the tiles of the Safe floor. */
    private String[][] floor;

    private BufferedReader reader;

    private String name;

    public LasersModel(String filename) throws IOException {
        this.name=filename;
        this.observers = new LinkedList<>();
        File safeFile = new File(filename);
        this.reader = new BufferedReader(new FileReader((safeFile)));
        String text = reader.readLine();
        String[] cord = text.split(" ");
        this.rows=Integer.parseInt(cord[0]);
        this.cols=Integer.parseInt(cord[1]);
        this.floor=new String[rows][cols];
        SafeTheBuilder();
    }

    /**
     * Add a new observer.
     *
     * @param observer the new observer
     */
    public void addObserver(Observer<LasersModel, ModelData > observer) {
        this.observers.add(observer);
    }

    /**
     * Notify observers the model has changed.
     *
     * @param data optional data the model can send to the view
     */
    private void notifyObservers(ModelData data){
        for (Observer<LasersModel, ModelData> observer: observers) {
            observer.update(this, data);
        }
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
        notifyObservers(null);
    }

    /**
     * Get the number of rows.
     *
     * @return number of the rows
     */
    public int getRows() { return this.rows; }

    /**
     * Get the columns.
     *
     * @return the number of columns
     */
    public int getCols() { return this.cols; }

    public String[][] getFloor() {
        return this.floor;
    }

    public void add(int row, int col) {
        if(row<0||row>this.rows||col<0||col>this.cols||this.floor[row][col].equals("L")||this.floor[row][col].equals("X")||
                this.floor[row][col].equals("0")||this.floor[row][col].equals("1")||this.floor[row][col].equals("2")
                ||this.floor[row][col].equals("3")||this.floor[row][col].equals("4")) {
            notifyObservers(new ModelData(row, col,"Error adding laser at: ("+row+","+col+")", Status.ERROR));
        } else {
            this.floor[row][col]="L";
            for (int c=col+1; c<this.cols; c++) {
                if(this.floor[row][c].equals(".")) {
                    this.floor[row][c]="*";
                } else {
                    break;
                }
            }
            for (int r=row+1; r<this.rows; r++) {
                if(this.floor[r][col].equals(".")) {
                    this.floor[r][col]="*";
                } else {
                    break;
                }
            }
            if (col!=0) {
                for (int c=col-1; c>=0; c--) {
                    if(this.floor[row][c].equals(".")) {
                        this.floor[row][c]="*";
                    } else {
                        break;
                    }
                }
            }
            if (row!=0) {
                for (int r=row-1; r>=0; r--) {
                    if(this.floor[r][col].equals(".")) {
                        this.floor[r][col]="*";
                    } else {
                        break;
                    }
                }
            }
            notifyObservers(new ModelData(row, col,"Laser added at: ("+row+","+col+")", Status.OK));
        }

    }

    /**
     * Close the program.
     */
    public void quit() {
        System.exit(0);
    }

    /**
     * Removes a laser from the specified coordinates in the Safe.
     *
     * @param row the row to add the laser to
     * @param col the col to add the laser to
     */
    public void remove(int row, int col) {
        if (row<0||row>this.rows||col<0||col>this.cols||!this.floor[row][col].equals("L")) {
            notifyObservers(new ModelData(row, col,"Error removing laser at: ("+row+", "+col+")", Status.ERROR));
        } else {
            this.floor[row][col]=".";
            label:
            for (int c = col+1; c<this.cols; c++) {
                switch (this.floor[row][c]) {
                    case "*":
                        if ((row - 1 >= 0) || (row + 1 < this.rows)) {
                            for (int d = row - 1; d >= 0; d--) {
                                if (this.floor[d][c].equals("L")) {
                                    for (int f=d+1; f<=rows; f++) {
                                        if (this.floor[f][c].equals(".")) {
                                        this.floor[f][c]="*";
                                    } else if (!this.floor[f][c].equals("L")) {
                                            break;
                                        }
                                    }
                                    break;
                                } else {
                                    this.floor[row][c] = ".";
                                }
                            }
                            for (int e = row + 1; e < this.rows; e++) {
                                if (this.floor[e][c].equals("L")) {
                                    for (int f=e-1; f>=0; f--) {
                                        if (this.floor[f][c].equals(".")) {
                                        this.floor[f][c]="*";
                                    } else if (!this.floor[f][c].equals("L")) {
                                            break;
                                        }
                                    }
                                    break;
                                } else {
                                    this.floor[row][c] = ".";
                                }
                            }
                        }
                        break;
                    case "X":
                    case "0":
                    case "1":
                    case "2":
                    case "3":
                    case "4":
                        break label;
                    case "L":
                        for (int b = c - 1; b >= col; b--) {
                            this.floor[row][b] = "*";
                        }
                        break;
                }
            }
            label1:
            for (int r = row+1; r<this.rows; r++) {
                switch (this.floor[r][col]) {
                    case "*":
                        if ((col - 1 >= 0) || (col + 1 < this.cols)) {
                            for (int d = col - 1; d >= 0; d--) {
                                if (this.floor[r][d].equals("L")) {
                                    for (int f=d+1; f<=cols; f++) {
                                        if (this.floor[r][f].equals(".")) {
                                        this.floor[r][f]="*";
                                    } else if (!this.floor[r][f].equals("L")) {
                                            break;
                                        }
                                    }
                                    break;
                                } else {
                                    this.floor[r][col] = ".";
                                }
                            }
                            for (int e = row + 1; e < this.rows; e++) {
                                if (this.floor[e][col].equals("L")) {
                                    for (int f=e-1; f>=0; f--) {
                                        if (this.floor[r][f].equals(".")) {
                                        this.floor[r][f]="*";
                                    } else if (!this.floor[r][f].equals("L")) {
                                            break;
                                        }
                                    }
                                    break;
                                } else {
                                    this.floor[r][col] = ".";
                                }
                            }
                        }
                        break;
                    case "X":
                    case "0":
                    case "1":
                    case "2":
                    case "3":
                    case "4":
                        break label1;
                    case "L":
                        for (int b = r - 1; b >= row; b--) {
                            this.floor[b][col] = "*";
                        }
                        break;
                }
            }
            if (col!=0) {
                label2:
                for (int c = col-1; c>=0; c--) {
                    switch (this.floor[row][c]) {
                        case "*":
                            if ((row - 1 >= 0) || (row + 1 < this.rows)) {
                                for (int d = row - 1; d >= 0; d--) {
                                    if (this.floor[d][c].equals("L")) {
                                        for (int f=d+1; f<rows; f++) {
                                            if (!this.floor[f][c].equals("L")) {
                                                break;
                                            } else if (this.floor[f][c].equals(".")) {
                                                this.floor[f][c]="*";
                                            }
                                        }
                                        break;
                                    } else {
                                        this.floor[row][c] = ".";
                                    }
                                }
                                for (int e = row + 1; e < this.rows; e++) {
                                    if (this.floor[e][c].equals("L")) {
                                        for (int f=e-1; f>=0; f--) {
                                            if (this.floor[f][c].equals(".")) {
                                            this.floor[f][c]="*";
                                        } else if (!this.floor[f][c].equals("L")) {
                                                break;
                                            }
                                        }
                                        break;
                                    } else {
                                        this.floor[row][c] = ".";
                                    }
                                }
                            }
                            break;
                        case "X":
                        case "0":
                        case "1":
                        case "2":
                        case "3":
                        case "4":
                            break label2;
                        case "L":
                            for (int b = c + 1; b <= col; b++) {
                                this.floor[row][b] = "*";
                            }
                            break;
                    }
                }
            }
            if (row!=0) {
                label3:
                for (int r = row-1; r>=0; r--) {
                    switch (this.floor[r][col]) {
                        case "*":
                            if ((col - 1 >= 0) || (col + 1 < this.cols)) {
                                for (int d = col - 1; d >= 0; d--) {
                                    if (this.floor[r][d].equals("L")) {
                                        for (int f=d+1; f<cols; f++) {
                                            if (!this.floor[r][f].equals("L")) {
                                                break;
                                            } else if (this.floor[r][f].equals(".")) {
                                                this.floor[r][f]="*";
                                            }
                                        }
                                        break;
                                    } else {
                                        this.floor[r][col] = ".";
                                    }
                                }
                                for (int e = col + 1; e < this.cols; e++) {
                                    if (this.floor[e][col].equals("L")) {
                                        for (int f=e-1; f>=0; f--) {
                                            if (this.floor[r][f].equals(".")) {
                                            this.floor[r][f]="*";
                                        } else if (!this.floor[r][f].equals("L")) {
                                                break;
                                            }
                                        }
                                        break;
                                    } else {
                                        this.floor[r][col] = ".";
                                    }
                                }
                            }

                            break;
                        case "X":
                        case "0":
                        case "1":
                        case "2":
                        case "3":
                        case "4":
                            break label3;
                        case "L":
                            for (int b = r + 1; b <= row; b++) {
                                this.floor[b][col] = "*";
                            }
                            break;
                    }
                }
            }
            notifyObservers(new ModelData(row, col,"Laser removed at: ("+row+","+col+")", Status.OK));
        }
    }

    /**
     * Displays a status message indicating whether the
     * safe is valid or not.
     */
    public void verify() {
        int errors=0;
        int row = 0;
        int col = 0;
        outerloop:
        while ( row < this.rows ) {
            col=0;
            while ( col < this.cols ) {
                if(this.floor[row][col].equals("X")||this.floor[row][col].equals("0")||this.floor[row][col].equals("1")
                        ||this.floor[row][col].equals("2")||this.floor[row][col].equals("3")
                        ||this.floor[row][col].equals("4")) {
                    int tile=Integer.parseInt(this.floor[row][col]);
                    int lasers=0;
                    if (col+1<this.cols && this.floor[row][col+1].equals("L")) {
                        lasers++;
                    }
                    if (col-1>=0 && this.floor[row][col-1].equals("L")) {
                        lasers++;
                    }
                    if (row+1<this.rows && this.floor[row+1][col].equals("L")) {
                        lasers++;
                    }
                    if ((row-1>=0) && (this.floor[row-1][col].equals("L"))) {
                        lasers++;
                    }
                    if (lasers!=tile) {
                        errors++;
                        notifyObservers(new ModelData(row, col,"Error verifying at: ("+row+", "+col+")", Status.ERROR));
                        break outerloop;
                    }
                }
                else if (this.floor[row][col].equals(".")) {
                    notifyObservers(new ModelData(row, col,"Error verifying at: ("+row+", "+col+")", Status.ERROR));
//                    this.display();
                    break outerloop;
                } else if (this.floor[row][col].equals("L")) {
                    for (int c=col+1; c<this.cols; c++) {
                        if(this.floor[row][c].equals("L")) {
                            errors++;
                            notifyObservers(new ModelData(row, col,"Error verifying at: ("+row+", "+col+")", Status.ERROR));
//                            this.display();
                            break outerloop;
                        } else if (this.floor[row][c].equals("0")||this.floor[row][c].equals("1")||this.floor[row][c].equals("2")
                                ||this.floor[row][c].equals("3")||this.floor[row][c].equals("4")||this.floor[row][c].equals("X")) {
                            break;
                        }
                    }
                    for (int r=row+1; r<this.rows; r++) {
                        if(this.floor[r][col].equals("L")) {
                            errors++;
                            notifyObservers(new ModelData(row, col,"Error verifying at: ("+row+", "+col+")", Status.ERROR));
//                            this.display();
                            break outerloop;
                        } else if (this.floor[r][col].equals("0")||this.floor[r][col].equals("1")||this.floor[r][col].equals("2")
                                ||this.floor[r][col].equals("3")||this.floor[r][col].equals("4")||this.floor[r][col].equals("X")) {
                            break;
                        }
                    }
                    if (col!=0) {
                        for (int c=col-1; c>=0; c--) {
                            if(this.floor[row][c].equals("L")) {
                                errors++;
                                notifyObservers(new ModelData(row, col,"Error verifying at: ("+row+", "+col+")", Status.ERROR));
//                                this.display();
                                break outerloop;
                            } else if (this.floor[row][c].equals("0")||this.floor[row][c].equals("1")||this.floor[row][c].equals("2")
                                    ||this.floor[row][c].equals("3")||this.floor[row][c].equals("4")||this.floor[row][c].equals("X")) {
                                break;
                            }
                        }
                    }
                    if (row!=0) {
                        for (int r=row-1; r>=0; r--) {
                            if(this.floor[r][col].equals("L")) {
                                errors++;
                                notifyObservers(new ModelData(row, col,"Error verifying at: ("+row+", "+col+")", Status.ERROR));
//                                this.display();
                                break outerloop;
                            } else if (this.floor[r][col].equals("0")||this.floor[r][col].equals("1")||this.floor[r][col].equals("2")
                                    ||this.floor[r][col].equals("3")||this.floor[r][col].equals("4")||this.floor[r][col].equals("X")) {
                                break;
                            }
                        }
                    }
                } col++;
            } row++;
        }
        if (errors==0) {
            notifyObservers(new ModelData(row, col,this.name+" is fully verified!", Status.OK));
        }
    }

    /**
     * Displays the safe to standard output.
     */
    public void display() {
        System.out.print("  ");
        for (int col = 0; col < this.cols; col++) {
            if (String.valueOf(col).length() == 1) {
                System.out.print(col + " ");
            } else {
                System.out.print(String.valueOf(col).charAt(String.valueOf(col).length() - 1) + " ");
            }
        }
        System.out.println();
        for (int row = 0; row < this.rows; row++) {
            System.out.print(row + "|");

            for (int col = 0; col < this.cols; col++) {
                System.out.print(this.floor[row][col] + " ");
            }
            System.out.println();
        }
    }

    /**
     * Method to display the help message to standard output, with no status message.
     */
    public void help(){
        System.out.println(
                "a|add r c: Add laser to (r,c)\n" +
                        "d|display: Display safe\n" +
                        "h|help: Print this help message\n" +
                        "q|quit: Exit program\n" +
                        "r|remove r c: Remove laser from (r,c)\n" +
                        "v|verify: Verify safe correctness");
    }
    public String getName() {
        return this.name;
    }
}
