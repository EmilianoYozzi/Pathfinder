package pathfinder;

import java.util.ArrayList;
import pathfinder.interfaz.Ventana;


public abstract class PathFinder {
    
    private int[][] map;
    private Ventana window;
    private int bestPathLength;
    private int[][] distanceMatrix;
    private int[] start;
    private int[] target;
    
    public abstract ArrayList<int[]> findPath(); 
    
    // Generates a new map based on a string with 0, 1, 2, 3 and 4 chars.
    public void generateMap(String code) {
        int mapSize = (int) Math.sqrt(code.length());
        map = new int[mapSize][mapSize];
        int cont = 0;

        for (int y = 0; y < mapSize; y++) {
            for (int x = 0; x < mapSize; x++) {
                if (cont < code.length()) {
                    map[x][y] = Integer.parseInt("" + code.charAt(cont));
                    if (map[x][y] == 3) {
                        int[] startCoord = {x, y};
                        setStart(startCoord);
                    } else if (map[x][y] == 4) {
                        int[] targetCoord = {x, y};
                        setTarget(targetCoord);
                    }
                } else {
                    map[x][y] = 0;
                }
                cont++;
            }
        }
    }
    
    // Returns a vector with the order to calculate paths sorted by priority 
    public String[] choosePriority(int[] currentPos) {
        int deltaX = target[0] - currentPos[0];
        int deltaY = target[1] - currentPos[1];

        if (Math.abs(deltaX) >= Math.abs(deltaY)) { // Chooses the main layer of priority
            return choosePriority2("right", "left", "down", "up", deltaX, deltaY); // Main: right-left
        } else {
            return choosePriority2("down", "up", "right", "left", deltaY, deltaX); // Main: down-up
        }
    }

    // Sets the priorities based on the parameters.
    public String[] choosePriority2(String positiveMainDir, String negativeMainDir, String positiveSecondDir, String negativeSecondDir, int firstDelta, int secondDelta) {
        String[] priorities = new String[4];
        priorities[0] = positiveMainDir;
        priorities[3] = negativeMainDir;
        if (firstDelta < 0) {
            priorities[0] = negativeMainDir;
            priorities[3] = positiveMainDir;
        }
        priorities[1] = positiveSecondDir;
        priorities[2] = negativeSecondDir;
        if (secondDelta < 0) {
            priorities[1] = negativeSecondDir;
            priorities[2] = positiveSecondDir;
        }
        
        return priorities;
    }
    
    // Generates the distance matrix full of Integer.MAX_VALUE
    public void startDistanceMatrix() {
        distanceMatrix = new int[map.length][map[0].length];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                distanceMatrix[i][j] = Integer.MAX_VALUE;
            }
        }
    }
    
    // Returns true if currentPos are the same coords than target
    public boolean targetFound(int[] currentPos) {
        return (currentPos[0] == target[0]) && (currentPos[1] == target[1]);
    }

    // Updates bestPathLength if a better length was found
    public void compareWithTheBestLength(int pathLength) {
        if (isShortEnough(pathLength)) {
            bestPathLength = pathLength;
        }
    }

    // Returns true if pathLength is shorter than the best length
    public boolean isShortEnough(int pathLength) {
        return bestPathLength < 0 || pathLength < bestPathLength;
    }

    public void updateDistanceMatrix(int[] coord, int value) {
        distanceMatrix[coord[0]][coord[1]] = value;
    }
    
    public boolean inMapRange(int xCoord, int yCoord){
        return (xCoord >= 0 && xCoord < map.length) && (yCoord >= 0 && yCoord < map[0].length);
    }

    public void setAsFreeSpace(int[] coord) {
        map[coord[0]][coord[1]] = 0;
    }

    public void setAsWall(int[] coord) {
        map[coord[0]][coord[1]] = 1;
    }

    public void setAsVisited(int[] coord) {
        map[coord[0]][coord[1]] = 2;
    }

    public void setAsStart(int[] coord) {
        map[coord[0]][coord[1]] = 3;
    }

    public void setAsTarget(int[] coord) {
        map[coord[0]][coord[1]] = 4;
    }
    
    public boolean isAWall(int[] coord) {
        return map[coord[0]][coord[1]] == 1;
    }
    
    public boolean isVisited(int[] coord) {
        return map[coord[0]][coord[1]] == 2;
    }
    
    public boolean isVisitable(int[] coord) {
        return !(isAWall(coord) || isVisited(coord));
    }
    
    public boolean consultDistanceMatrix(int[] coord, int pathLength) {
        return pathLength + 1 < distanceMatrix[coord[0]][coord[1]];
    }
    
    // Getters & Setters:
    public int[] getStart() {
        if (start == null) {
            return new int[2];
        }
        return start;
    }

    public void setStart(int[] start) {
        this.start = start;
    }

    public int[] getTarget() {
        if (target == null) {
            return new int[2];
        }
        return target;
    }

    public void setTarget(int[] target) {
        this.target = target;
    }

    public Ventana getWindow() {
        return window;
    }

    public void setWindow(Ventana window) {
        this.window = window;
    }

    public int getBestPathLength() {
        return bestPathLength;
    }

    public void setBestPathLength(int bestPathLength) {
        this.bestPathLength = bestPathLength;
    }

    public int[][] getMap() {
        return map;
    }

    public void setMap(int[][] map) {
        this.map = map;
    }

    public int[][] getDistanceMatrix() {
        return distanceMatrix;
    }

    public void setDistanceMatrix(int[][] distanceMatrix) {
        this.distanceMatrix = distanceMatrix;
    }
}
