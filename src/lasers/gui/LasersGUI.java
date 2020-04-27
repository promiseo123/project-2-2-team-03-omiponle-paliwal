package lasers.gui;

import javafx.application.Application;
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
        this.message.setAlignment(Pos.CENTER);
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
            Stage fileStage=new Stage();
            File selectedFile = chooser.showOpenDialog(fileStage);
            ;});
        options.getChildren().addAll(check, hint, solve, restart, load);
        window.setBottom(options);
        GridPane safe=new GridPane();
        for (int row=0; row<this.model.getRows(); row++) {
            for (int col=0; col<this.model.getCols(); col++) {
                if (this.model.getFloor()[row][col].equals("X")) {
                    Button pillar=new Button();
                    pillar.setGraphic(new ImageView("resources/pillarX.png"));
                    safe.add(pillar, col, row);
                } else if(this.model.getFloor()[row][col].equals("0")||this.model.getFloor()[row][col].equals("1")
                ||this.model.getFloor()[row][col].equals("2")||this.model.getFloor()[row][col].equals("3")
                                                                ||this.model.getFloor()[row][col].equals("4")) {
                    String thing= this.model.getFloor()[row][col];
                    Button pillar=new Button();
                    pillar.setGraphic(new ImageView("resources/pillar"+thing+".png"));
                    safe.add(pillar, col,row);
                } else {
                    Button tile=new Button();
                    tile.setGraphic(new ImageView("resources/white.png"));
                    safe.add(tile, col, row);
                }
            }
        }
        window.setCenter(safe);
        Scene scene=new Scene(window);
        stage.setScene(scene);
//        buttonDemo(stage);  // this can be removed/altered
    }

    @Override
    public void start(Stage stage) throws Exception {
        // TODO
        init(stage);  // do all your UI initialization here

        stage.setTitle("Lasers GUI");
        stage.show();
        this.started=true;
    }

    @Override
    public void update(LasersModel model, ModelData data) {
        // TODO
    }
}
