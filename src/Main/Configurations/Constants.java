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
    public static final String UNVISITED = "#CFF1EF";
    public static final String SOURCE = "#F9D89C";
    public static final String TARGET = "#BC658D";
    public static final String VISITED = "#fbcffc";
    public static final String NEXT_VISIT = "#b4de87";
    public static final String SHORTEST = "#f1f5a2";
    // Algorithm related
    public static int THREAD_SLEEP_TIME = 40; // milisecs, TODO: use this to increase or decrease visualization's speed.
    public static final int NUM_OF_NEIGHBORS = 4;

    public static Algorithm currentThread = null;

}
