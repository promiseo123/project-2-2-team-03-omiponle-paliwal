package lasers.backtracking;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * This class represents the classic recursive backtracking algorithm.
 * It has a solver that can take a valid configuration and return a
 * solution, if one exists.
 *
 * This file comes from the backtracking lecture. It should be useful
 * in this project. A second method has been added that you should
 * implement.
 *
 * @author RIT CS
 * @author Promise Omiponle and Parth Paliwal
 */
public class Backtracker {

    private boolean debug;

    /**
     * Initialize a new backtracker.
     *
     * @param debug Is debugging output enabled?
     */
    public Backtracker(boolean debug) {
        this.debug = debug;
        if (this.debug) {
            System.out.println("Backtracker debugging enabled...");
        }
    }

    /**
     * A utility routine for printing out various debug messages.
     *
     * @param msg    The type of config being looked at (current, goal,
     *               successor, e.g.)
     * @param config The config to display
     */
    private void debugPrint(String msg, Configuration config) {
        if (this.debug) {
            System.out.println(msg + ":\n" + config);
        }
    }

    /**
     * Try find a solution, if one exists, for a given configuration.
     *
     * @param config A valid configuration
     * @return A solution config, or Optional.empty() if no solution
     */
    public Optional<Configuration> solve(Configuration config) {
        debugPrint("Current config", config);
        if (config.isGoal()) {
            debugPrint("\tGoal config", config);
            return Optional.of(config);
        } else {
            for (Configuration child : config.getSuccessors()) {
                if (child.isValid()) {
                    debugPrint("\tValid successor", child);
                    Optional<Configuration> sol = solve(child);
                    if (sol.isPresent()) {
                        return sol;
                    }
                } else {
                    debugPrint("\tInvalid successor", child);
                }
            }
            // implicit backtracking happens here
        }
        return Optional.empty();
    }

    /**
     * Find a goal configuration if it exists, and how to get there.
     *
     * @param current the starting configuration
     * @return a list of configurations to get to a goal configuration.
     * If there are none, return null.
     */
    public List<Configuration> solveWithPath(Configuration current) {
        List<Configuration> path= new ArrayList<>();
        debugPrint("Current config", current);
        if (current.isGoal()) {
            debugPrint("\tGoal config", current);
            path.add(current);
            return path;
        } else {
            for (Configuration child : current.getSuccessors()) {
                if (child.isValid()) {
                    debugPrint("\tValid successor", child);
                    path = solveWithPath(child);
                    if (path!= null) {
                        path.add(child);
                        return path;
                    }
                } else {
                    debugPrint("\tInvalid successor", child);
                }
            }
            // implicit backtracking happens here
        }
        return path;    }
}
