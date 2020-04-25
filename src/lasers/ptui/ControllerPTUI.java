package lasers.ptui;

import lasers.Lasers;
import lasers.model.LasersModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * This class represents the controller portion of the plain text UI.
 * It takes the model from the view (LasersPTUI) so that it can perform
 * the operations that are input in the run method.
 *
 * @author RIT CS
 * @author Parth Paliwal, Promise Omiponle
 */
public class ControllerPTUI  {
    /** The UI's connection to the lasers.lasers.model */
    private LasersModel model;

    /**
     * Construct the PTUI.  Create the model and initialize the view.
     * @param model The laser model
     */
    public ControllerPTUI(LasersModel model) {
        this.model = model;
    }

    /**
     * Run the main loop.  This is the entry point for the controller
     * @param inputFile The name of the input command file, if specified
     */
    public void run(String inputFile) {

        if (inputFile != null) {
            try {
                Scanner in = new Scanner(new File(inputFile));
                while (in.hasNextLine()) {
                    String line = in.nextLine();
                    System.out.println("> " + line);
                    String[] cmd = line.split("");
                    cmdPtui(cmd);
                }
                in.close();
            } catch (FileNotFoundException e) {
                System.out.println(e.getMessage());
            }
        }
        while (true){
            Scanner newIn = new Scanner(System.in);
            System.out.println("> ");
            if(!newIn.hasNextLine()){
                break;
            }
            String cmdString = newIn.nextLine();
            String[] cmd = cmdString.split(" ");

            cmdPtui(cmd);
        }
    }

    /**
     * Method to handle different commands
     * @param cmdArray command
     */
    private void cmdPtui(String[] cmdArray) {

        String cmd = Character.toString(cmdArray[0].charAt(0));
        if (cmd.toLowerCase().charAt(0) == 'a') {
            String[] adder = cmd.split(" ");

            if (adder.length == 3) {
                model.add(Integer.parseInt(adder[1]), Integer.parseInt(adder[2]));
            } else {
                System.out.println("Incorrect coordinates");
            }
        } else if (cmd.toLowerCase().charAt(0) == 'd') {
            model.display();
        } else if (cmd.toLowerCase().charAt(0) == 'h') {
            model.help();
        } else if (cmd.toLowerCase().charAt(0) == 'v') {
            model.verify();
        } else if (cmd.toLowerCase().charAt(0) == 'r') {
            String[] remover = cmd.split(" ");
            if (remover.length == 3) {
                model.remove(Integer.parseInt(remover[1]), Integer.parseInt(remover[2]));
            } else {
                System.out.println("Incorrect coordinates");
            }
        } else if (cmd.toLowerCase().charAt(0) == 'q') {
            model.quit();
        } else {

            System.out.println("Unrecognized command " + cmd);
        }

        System.out.println(cmd);
    }
}
