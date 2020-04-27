package lasers.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import lasers.model.*;

/**
 * The main class that implements the JavaFX UI.   This class represents
 * the view/controller portion of the UI.  It is connected to the lasers.lasers.model
 * and receives updates from it.
 *
 * @author RIT CS
 */
public class LasersGUI extends Application implements Observer<LasersModel, ModelData> {
    /** The UI's connection to the lasers.lasers.model */
    private LasersModel model;

    private Label message;
    private Button check;
    private Button hint;
    private Button solve;
    private Button restart;
    private Button load;
    private boolean started;

    /** 2-D array of the buttons on the board. */
    private Button[][] buttons;

    /** this can be removed - it is used to demonstrates the button toggle */
    private static boolean status = true;

    @Override
    public void init() throws Exception {
        // the init method is run before start.  the file name is extracted
        // here and then the model is created.
        try {
            Parameters params = getParameters();
            String filename = params.getRaw().get(0);
            this.model = new LasersModel(filename);
        } catch (FileNotFoundException fnfe) {
            System.out.println(fnfe.getMessage());
            System.exit(-1);
        }
        this.model.addObserver(this);
    }

    /**
     * A private utility function for setting the background of a button to
     * an image in the resources subdirectory.
     *
     * @param button the button control
     * @param bgImgName the name of the image file
     */
    private void setButtonBackground(Button button, String bgImgName) {
        BackgroundImage backgroundImage = new BackgroundImage(
                new Image( getClass().getResource("resources/" + bgImgName).toExternalForm()),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT);
        Background background = new Background(backgroundImage);
        button.setBackground(background);
    }

    /**
     * This is a private demo method that shows how to create a button
     * and attach a foreground image with a background image that
     * toggles from yellow to red each time it is pressed.
     *
     * @param stage the stage to add components into
     */
    private void buttonDemo(Stage stage) {
        // this demonstrates how to create a button and attach a foreground and
        // background image to it.
        Button button = new Button();
        Image laserImg = new Image(getClass().getResourceAsStream("resources/laser.png"));
        ImageView laserIcon = new ImageView(laserImg);
        button.setGraphic(laserIcon);
        setButtonBackground(button, "yellow.png");
        button.setOnAction(e -> {
            // toggles background between yellow and red
            if (!status) {
                setButtonBackground(button, "yellow.png");
            } else {
                setButtonBackground(button, "red.png");
            }
            status = !status;
        });

        Scene scene = new Scene(button);
        stage.setScene(scene);
    }

    /**
     * The initialization of all GUI component happens here.
     *
     * @param stage the stage to add UI components into
     */
    private void init(Stage stage) {
        BorderPane window=new BorderPane();
        this.message=new Label("");
        this.message.setAlignment(Pos.TOP_CENTER);
        window.setTop(this.message);
        HBox options=new HBox();
        this.check=new Button("Check");
        this.check.setOnAction(event -> this.model.verify());
        this.hint=new Button("Hint");
        this.solve=new Button("Solve");
        this.restart=new Button("Restart");
        this.restart.setOnAction(event -> {
            try {
                start(stage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        this.load=new Button("Load");
        this.load.setOnAction(event -> {FileChooser chooser=new FileChooser();
            chooser.setInitialDirectory(new File("tests"));
            File selectedFile = chooser.showOpenDialog(stage);
            if (selectedFile != null) {
                try {
                    this.model=new LasersModel(selectedFile.getAbsolutePath());
                    start(stage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            });
        options.getChildren().addAll(check, hint, solve, restart, load);
        window.setBottom(options);
        GridPane safe=new GridPane();
        buttons=new Button[model.getRows()][model.getCols()];
        for (int row=0; row<this.model.getRows(); row++) {
            for (int col=0; col<this.model.getCols(); col++) {
                if (this.model.getFloor()[row][col].equals("X")) {
                    Button pillar=new Button();
                    pillar.setGraphic(new ImageView(
                            new Image(getClass().getResourceAsStream("resources/pillarX.png"))));
                    buttons[row][col]=pillar;
                    safe.add(pillar, col, row);
                } else if(this.model.getFloor()[row][col].equals("0")||this.model.getFloor()[row][col].equals("1")
                ||this.model.getFloor()[row][col].equals("2")||this.model.getFloor()[row][col].equals("3")
                                                                ||this.model.getFloor()[row][col].equals("4")) {
                    String thing= this.model.getFloor()[row][col];
                    Button pillar=new Button();
                    pillar.setGraphic(new ImageView(
                            new Image(getClass().getResourceAsStream("resources/pillar"+thing+".png"))));
                    buttons[row][col]=pillar;
                    safe.add(pillar, col,row);
                } else {
                    Button tile=new Button();
                    tile.setGraphic(new ImageView(
                            new Image(getClass().getResourceAsStream("resources/white.png"))));
                    int finalRow = row;
                    int finalCol = col;
                    tile.setOnAction(event -> model.add(finalRow, finalCol));
                    buttons[row][col]=tile;
                    safe.add(tile, col, row);
                }
            }
        }
        window.setCenter(safe);
        Scene scene=new Scene(window);
        stage.setScene(scene);
//        buttonDemo(stage);  // this can be removed/altered
    }

    public void updateButton(ModelData data){
        Button button=buttons[data.getRow()][data.getCol()];
        if (data.getStatus()==Status.ERROR) {
            setButtonBackground(button, "red.png");
        } else if (data.getItem().equals(model.getName()+" is fully verified!")) {
            for (int row=0; row<model.getRows(); row++) {
                for(int col=0; col<model.getCols(); col++) {
                    setButtonBackground(buttons[row][col], null);
                }
            }
        }
            else if (data.getItem().equals("Laser added at: ("+data.getRow()+","+data.getCol()+")")) {
            button.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("resources/laser.png"))));
            setButtonBackground(button, "yellow.png");
            button.setOnAction(event -> model.remove(data.getRow(), data.getCol()));
            for (int c=data.getCol()+1; c<model.getCols(); c++) {
                if(model.getFloor()[data.getRow()][c].equals("*")) {
                    Button beam=buttons[data.getRow()][c];
                    beam.setGraphic(new ImageView(
                            new Image(getClass().getResourceAsStream("resources/beam.png"))));
                    setButtonBackground(beam, "yellow.png");
                    int finalC = c;
                    beam.setOnAction(event -> model.add(data.getRow(), finalC));
                } else {
                    break;
                }
            }
            for (int r=data.getRow()+1; r<model.getCols(); r++) {
                if(model.getFloor()[r][data.getCol()].equals("*")) {
                    Button beam=buttons[r][data.getCol()];
                    beam.setGraphic(new ImageView(
                            new Image(getClass().getResourceAsStream("resources/beam.png"))));
                    setButtonBackground(beam, "yellow.png");
                    int finalR = r;
                    beam.setOnAction(event -> model.add(finalR, data.getCol()));
                } else {
                    break;
                }
            }
            if (data.getCol()!=0) {
                for (int c=data.getCol()-1; c>=0; c--) {
                    if(model.getFloor()[data.getRow()][c].equals("*")) {
                        Button beam=buttons[data.getRow()][c];
                        beam.setGraphic(new ImageView(
                                new Image(getClass().getResourceAsStream("resources/beam.png"))));
                        setButtonBackground(beam, "yellow.png");
                        int finalC = c;
                        beam.setOnAction(event -> model.add(data.getRow(), finalC));
                    } else {
                        break;
                    }
                }
            }
            if (data.getRow()!=0) {
                for (int r=data.getRow()-1; r>=0; r--) {
                    if(model.getFloor()[r][data.getCol()].equals("*")) {
                        Button beam=buttons[r][data.getCol()];
                        beam.setGraphic(new ImageView(
                                new Image(getClass().getResourceAsStream("resources/beam.png"))));
                        setButtonBackground(beam, "yellow.png");
                        int finalR = r;
                        beam.setOnAction(event -> model.add(finalR, data.getCol()));
                    } else {
                        break;
                    }
                }
            }
        } else if (data.getItem().equals("Laser removed at: ("+data.getRow()+","+data.getCol()+")")) {
            if (this.model.getFloor()[data.getRow()][data.getCol()].equals(".")) {
                button.setGraphic(new ImageView(
                        new Image(getClass().getResourceAsStream("resources/white.png"))));
                button.setOnAction(event -> model.add(data.getRow(), data.getCol()));
            }
            for (int c = data.getCol() + 1; c < model.getCols(); c++) {
                if (this.model.getFloor()[data.getRow()][c].equals(".")) {
                    Button tile=buttons[data.getRow()][c];
                    tile.setGraphic(new ImageView(
                            new Image(getClass().getResourceAsStream("resources/white.png"))));
                    int finalC = c;
                    tile.setOnAction(event -> model.add(data.getRow(), finalC));
                } else if (!this.model.getFloor()[data.getRow()][c].equals("*")) {
                    break;
                }
            }
            for (int r = data.getRow() + 1; r < model.getRows(); r++) {
                if (this.model.getFloor()[r][data.getCol()].equals(".")) {
                    Button tile=buttons[r][data.getCol()];
                    tile.setGraphic(new ImageView(
                            new Image(getClass().getResourceAsStream("resources/white.png"))));
                    int finalR = r;
                    tile.setOnAction(event -> model.add(finalR, data.getCol()));
                } else if (!this.model.getFloor()[r][data.getCol()].equals("*")) {
                    break;
                }
            }
            if (data.getCol()!=0) {
                for (int c = data.getCol() - 1; c >= 0; c--) {
                    if (this.model.getFloor()[data.getRow()][c].equals(".")) {
                        Button tile=buttons[data.getRow()][c];
                        tile.setGraphic(new ImageView(
                                new Image(getClass().getResourceAsStream("resources/white.png"))));
                        int finalC = c;
                        tile.setOnAction(event -> model.add(data.getRow(), finalC));
                    } else if (!this.model.getFloor()[data.getRow()][c].equals("*")) {
                        break;
                    }
                }
            }
            if (data.getRow()!=0) {
                for (int r = data.getRow() - 1; r >= 0; r--) {
                    if (this.model.getFloor()[r][data.getCol()].equals(".")) {
                        Button tile=buttons[r][data.getCol()];
                        tile.setGraphic(new ImageView(
                                new Image(getClass().getResourceAsStream("resources/white.png"))));
                        int finalR = r;
                        tile.setOnAction(event -> model.add(finalR, data.getCol()));
                    } else if (!this.model.getFloor()[r][data.getCol()].equals("*")) {
                        break;
                    }
                }

            }
        }this.message.setText(data.getItem());
    }

    /**
     * UpdateThread represents a Thread for updating a tile
     * in the view.
     */
    public class UpdateThread implements Runnable {
        private ModelData data;

        /**
         * Initializes the card field in the thread.
         *
         * @param data the card to be updated
         */
        public UpdateThread(ModelData data) {
            this.data=data;
        }

        /**
         * Sets the tile to a button and updates it according to what
         * happens in the safe.
         */
        @Override
        public void run() {
            if(data!=null) {
                if(started) {
                    updateButton(data);
                }
            }
            assert data != null;
            message.setText(data.getItem());

        }
    }

    @Override
    public void start(Stage stage) throws Exception {
//        init();
        init(stage);  // do all your UI initialization here

        stage.setTitle("Lasers GUI");
        stage.show();
        this.message.setText(model.getName()+" loaded.");
        this.started=true;
    }

    @Override
    public void update(LasersModel model, ModelData data) {
        if(started) {
            Platform.runLater(new UpdateThread(data));
        }
    }
}
