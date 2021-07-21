package DP;

import pathfinder.PathFinder;
import pathfinder.interfaz.Ventana;
import java.util.ArrayList;

public class PathFinderDP extends PathFinder {

    private int[][][] pathMatrix;
    private ArrayList<int[]> queue;
    private boolean startFound;
    
    public PathFinderDP() {
        setWindow(new Ventana(this));
        getWindow().setVisible(true);
    }

    // Returns the shortest path between the point A and B.
    @Override
    public ArrayList<int[]> findPath() {
        setBestPathLength(-1);
        startPathMatrix();
        
        queue = new ArrayList();
        queue.add(getTarget());
        startFound = false;
        recorrerLaberinto();
        
        return getPath();
    }
    
    public ArrayList<int[]> getPath() {
        ArrayList<int[]> ret = new ArrayList();
        ret.add(getStart());
        int[] currentPos = ret.get(ret.size() - 1);
        while (!targetFound(currentPos)) {
            int[] nextCoord = pathMatrix[currentPos[0]][currentPos[1]];
            ret.add(nextCoord);
            currentPos = nextCoord;
        }
        
        return ret;
    }
    
    public void recorrerLaberinto() {
        if (emptyQueue() || startFound) {
            return;
        }
        int[] currentPos = dequeue();
        int[][] directions = {{1,0},{-1,0},{0,1},{0,-1}};
        startFound = startFound(currentPos);
        
        for (int i = 0; i < directions.length; i++) {
            int[] nextPos = getNextPos(currentPos, directions[i]);
            
            if (inMapRange(nextPos[0], nextPos[1]) && isVisitable(nextPos)) {
                pathMatrix[nextPos[0]][nextPos[1]] = currentPos;
                queue.add(nextPos);
                setAsVisited(nextPos);
            }
        }
        
        recorrerLaberinto();
    }

    public void startPathMatrix() {
        int[][] map = getMap();
        pathMatrix = new int[map.length][map[0].length][2];
    }

    public int[] dequeue() {
        int[] coord = queue.get(0);
        queue.remove(0);
        return coord;
    }
    
    public boolean emptyQueue() {
        return queue.isEmpty();
    }
    
    public int[] getNextPos(int[] currentPos, int[] direction) {
        int[] ret = {currentPos[0] + direction[0], currentPos[1] + direction[1]};
        return ret;
    }
    
    // Returns true if currentPos are the same coords than start
    public boolean startFound(int[] currentPos) {
        return (currentPos[0] == getStart()[0]) && (currentPos[1] == getStart()[1]);
    }

}
