package lasers.model;

/**
 * Use this class to customize the data you wish to send from the model
 * to the view when the model changes state.
 *
 * @author RIT CS
 * @author PROMISE OMIPONLE, PARTH PALIWAL
 */
public class ModelData {
    private final int row;
    private final int col;
    private final String item;

    private final Status status;

    public ModelData(int row, int col, String item) {
        this.row=row;
        this.col=col;
        this.item=item;
        this.status=Status.OK;
    }

    /**
     * Get the row.
     *
     * @return the row
     */
    public int getRow() { return this.row; }

    /**
     * Get the column.
     *
     * @return the column
     */
    public int getCol() { return this.col; }

    /**
     * Get the value.
     *
     * @return the value
     */
    public String getItem() { return this.item; }

    /**
     * Is the card revealed?
     *
     * @return is the card revealed (if not it is hidden)
     */
    public Status getStatus() { return this.status; }
}
