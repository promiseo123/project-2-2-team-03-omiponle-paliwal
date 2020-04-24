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
    private Object[][] floor;

    private BufferedReader reader;

    public String status;

    public LasersModel(String filename) throws IOException {
        this.observers = new LinkedList<>();
        File safeFile = new File(filename);
        this.reader = new BufferedReader(new FileReader((safeFile)));
        String text = reader.readLine();
        String[] cord = text.split(" ");
        this.rows=Integer.parseInt(cord[0]);
        this.cols=Integer.parseInt(cord[1]);
        this.floor=new Object[rows][cols];
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

    public void add(int row, int col) {
        if(row<0||row>this.rows||col<0||col>this.cols||this.floor[row][col].equals("L")||this.floor[row][col].equals("X")||
                this.floor[row][col] instanceof Integer) {
            notifyObservers(new ModelData(row, col,"Error adding laser at: ("+row+","+col+")"));
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
            notifyObservers(new ModelData(row, col,"Laser added at: ("+row+","+col+")"));
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
            notifyObservers(new ModelData(row, col,"Error removing laser at: ("+row+", "+col+")"));
        } else {
            this.floor[row][col]=".";
            for (int c=col+1; c<this.cols; c++) {
                if(this.floor[row][c].equals("*")) {
                    if((row-1>=0)|| (row+1<this.rows)) {
                        for (int d=row-1; d>=0; d--) {
                            if (this.floor[d][c].equals("L")||this.floor[d][c] instanceof Integer) {
                                break;
                            }
                        }
                        for (int e=row+1; e<this.rows; e++) {
                            if (this.floor[e][c].equals("L")||this.floor[e][c] instanceof Integer) {
                                break;
                            }
                        }
                    } else {
                        this.floor[row][c]=".";
                    }
                } else if (this.floor[row][c].equals("X")||this.floor[row][c] instanceof Integer) {
                    break;
                }
                else if (this.floor[row][c].equals("L")) {
                    for(int b=c-1; b>=col; b--) {
                        this.floor[row][b]="*";
                    }
                }
            }
            for (int r=row+1; r<this.rows; r++) {
                if(this.floor[r][col].equals("*")) {
                    if((col-1>=0)|| (col+1<this.cols)) {
                        for (int d=col-1; d>=0; d--) {
                            if (this.floor[row][d].equals("L")||this.floor[row][d] instanceof Integer) {
                                break;
                            }
                        }
                        for (int e=row+1; e<this.rows; e++) {
                            if (this.floor[row][e].equals("L")||this.floor[row][e] instanceof Integer) {
                                break;
                            }
                        }
                    } else {
                        this.floor[r][col]=".";
                    }
                } else if (this.floor[r][col].equals("X")||this.floor[r][col] instanceof Integer) {
                    break;
                } else if (this.floor[r][col].equals("L")) {
                    for(int b=r-1; b>=row; b--) {
                        this.floor[b][col]="*";
                    }
                }
            }
            if (col!=0) {
                for (int c=col-1; c>=0; c--) {
                    if(this.floor[row][c].equals("*")) {
                        if((row-1>=0)|| (row+1<this.rows)) {
                            for (int d=row-1; d>=0; d--) {
                                if (this.floor[d][c].equals("L")||this.floor[d][c] instanceof Integer) {
                                    break;
                                }
                            }
                            for (int e=row+1; e<this.rows; e++) {
                                if (this.floor[e][c].equals("L")||this.floor[e][c] instanceof Integer) {
                                    break;
                                }
                            }
                        } else {
                            this.floor[row][c]=".";
                        }
                    } else if (this.floor[row][c].equals("X")||this.floor[row][c] instanceof Integer) {
                        break;
                    } else if (this.floor[row][c].equals("L")) {
                        for(int b=c+1; b<=col; b++) {
                            this.floor[row][b]="*";
                        }
                    }
                }
            }
            if (row!=0) {
                for (int r=row-1; r>=0; r--) {
                    if(this.floor[r][col].equals("*")) {
                        if((col-1>=0)|| (col+1<this.cols)) {
                            for (int d=col-1; d>=0; d--) {
                                if (this.floor[row][d].equals("L")||this.floor[row][d] instanceof Integer) {
                                    break;
                                }
                            }
                            for (int e=row+1; e<this.rows; e++) {
                                if (this.floor[row][e].equals("L")||this.floor[row][e] instanceof Integer) {
                                    break;
                                }
                            }
                        } else {
                            this.floor[r][col]=".";
                        }

                    } else if (this.floor[r][col].equals("X")||this.floor[r][col] instanceof Integer) {
                        break;
                    } else if (this.floor[r][col].equals("L")) {
                        for(int b=r+1; b<=row; b++) {
                            this.floor[b][col]="*";
                        }
                    }
                }
            }
            notifyObservers(new ModelData(row, col,"Laser removed at: ("+row+","+col+")"));
        }
    }

    /**
     * Displays a status message indicating whether the
     * safe is valid or not.
     */
    public void verify() {
        int errors=0;
        int row = 0;
        outerloop:
        while ( row < this.rows ) {
            int col = 0;
            while ( col < this.cols ) {
                if(this.floor[row][col] instanceof Integer) {
                    int tile=(int) this.floor[row][col];
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
                        notifyObservers(new ModelData(row, col,"Error verifying at: ("+row+", "+col+")"));
//                        this.display();
                        break outerloop;
                    }
                }
                else if (this.floor[row][col].equals(".")) {
                    notifyObservers(new ModelData(row, col,"Error verifying at: ("+row+", "+col+")"));
//                    this.display();
                    break outerloop;
                } else if (this.floor[row][col].equals("L")) {
                    for (int c=col+1; c<this.cols; c++) {
                        if(this.floor[row][c].equals("L")) {
                            errors++;
                            notifyObservers(new ModelData(row, col,"Error verifying at: ("+row+", "+col+")"));
//                            this.display();
                            break outerloop;
                        } else if (this.floor[row][c] instanceof Integer||this.floor[row][c].equals("X")) {
                            break;
                        }
                    }
                    for (int r=row+1; r<this.rows; r++) {
                        if(this.floor[r][col].equals("L")) {
                            errors++;
                            notifyObservers(new ModelData(row, col,"Error verifying at: ("+row+", "+col+")"));
//                            this.display();
                            break outerloop;
                        } else if (this.floor[r][col] instanceof Integer||this.floor[r][col].equals("X")) {
                            break;
                        }
                    }
                    if (col!=0) {
                        for (int c=col-1; c>=0; c--) {
                            if(this.floor[row][c].equals("L")) {
                                errors++;
                                notifyObservers(new ModelData(row, col,"Error verifying at: ("+row+", "+col+")"));
//                                this.display();
                                break outerloop;
                            } else if (this.floor[row][c] instanceof Integer||this.floor[row][c].equals("X")) {
                                break;
                            }
                        }
                    }
                    if (row!=0) {
                        for (int r=row-1; r>=0; r--) {
                            if(this.floor[r][col].equals("L")) {
                                errors++;
                                notifyObservers(new ModelData(row, col,"Error verifying at: ("+row+", "+col+")"));
//                                this.display();
                                break outerloop;
                            } else if (this.floor[r][col] instanceof Integer||this.floor[r][col].equals("X")) {
                                break;
                            }
                        }
                    }
                } col++;
            } row++;
        }
        if (errors==0) {
            notifyObservers(new ModelData(row, col,this+" is fully verified!"));
        }
        this.display();
    }
}
