package Main.Configurations;

import Main.Algorithms.Algorithm;

// Class contains all the constant used through out the project
public class Constants
{
    // Design preference
    public static final int ROW = 25, COL = 40;

    //Colors
    public static final String BORDER = "black";
    public static final String WALL = "#202040";
    public static final String UNVISITED = "#fffff0";
    public static final String SOURCE = "#BC658D";
    public static final String TARGET = "#ff6363";
    public static final String VISITED = "#fbcffc";
    public static final String NEXT_VISIT = "#b4de87";
    public static final String SHORTEST = "#f1f5a2";
    // Algorithm related
    public static int THREAD_SLEEP_TIME = 35; // milisecs, TODO: use this to increase or decrease visualization's speed.
    public static int THREAD_PAUSE_TIME = 1000;
    public static final int NUM_OF_NEIGHBORS = 4;
    public static final int UNVISITED_WEIGHT = Integer.MAX_VALUE;
    public static final int WALL_WEIGHT = -1;

    public static Algorithm currentThread = null;
    public static boolean isPause = false;
}
