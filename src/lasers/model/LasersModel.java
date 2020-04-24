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
    }
}
