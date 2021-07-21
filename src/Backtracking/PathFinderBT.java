package Backtracking;

import pathfinder.PathFinder;
import pathfinder.interfaz.Ventana;
import java.util.ArrayList;

public class PathFinderBT extends PathFinder {

    public PathFinderBT() {
        setWindow(new Ventana(this));
        getWindow().setVisible(true);
    }

    // Returns the shortest path between the point A and B.
    @Override
    public ArrayList<int[]> findPath() {
        setBestPathLength(-1);
        startDistanceMatrix();
        return findPath(getStart(), new ArrayList<>());
    }

    // Uses backtracking to find the shortest path. 
    public ArrayList<int[]> findPath(int[] currentPos, ArrayList<int[]> path) {
        path.add(currentPos);
        updateDistanceMatrix(currentPos, path.size());

        if (targetFound(currentPos)) {
            compareWithTheBestLength(path.size());
            return path;
        }
        
        ArrayList<int[]> ret = new ArrayList<>();
        setAsVisited(currentPos);

        if (isShortEnough(path.size())) {
            ret = continueSearch(currentPos, path);
        }
        
        setAsFreeSpace(currentPos);
        return ret;
    }

    // Compares the 4 possible paths and chooses the best
    public ArrayList<int[]> continueSearch(int[] currentPos, ArrayList<int[]> path) {
        ArrayList<int[]>[] possiblePaths = getPossiblePaths(currentPos, path);
        ArrayList<int[]> bestPath = possiblePaths[0];

        for (int i = 1; i < possiblePaths.length; i++) {
            bestPath = chooseShortestPath(bestPath, possiblePaths[i]);
        }

        return bestPath;
    }

    // Returns 4 paths, one for each direction. This function uses priority to know which path should be calculated first.
    public ArrayList<int[]>[] getPossiblePaths(int[] currentPos, ArrayList<int[]> path) {
        ArrayList<int[]>[] paths = new ArrayList[4];
        String[] priority = choosePriority(currentPos);
        for (int i = 0; i < paths.length; i++) {
            switch (priority[i]) {
                case "up":
                    paths[i] = tryToMoveTo(currentPos[0], currentPos[1] - 1, path);
                    break;
                case "right":
                    paths[i] = tryToMoveTo(currentPos[0] + 1, currentPos[1], path);
                    break;
                case "down":
                    paths[i] = tryToMoveTo(currentPos[0], currentPos[1] + 1, path);
                    break;
                case "left":
                    paths[i] = tryToMoveTo(currentPos[0] - 1, currentPos[1], path);
                    break;
                default:
                    break;
            }
        }
        
        return paths;
    }

    // If the coordinates are in the map, there is no wall and was not visited yet, calls findPath to that coordinates.
    public ArrayList<int[]> tryToMoveTo(int xCoord, int yCoord, ArrayList<int[]> path) {
        if (inMapRange(xCoord, yCoord)) {
            int[] nextCoord = {xCoord, yCoord};
            boolean visitable = isVisitable(nextCoord);
            boolean isAShorterPath = consultDistanceMatrix(nextCoord, path.size());
            if (visitable && isAShorterPath) { 
                return findPath(nextCoord, (ArrayList<int[]>) path.clone());
            }
        }
        return new ArrayList<>();
    }

    // Returns the shortest path between 2 paths
    public ArrayList<int[]> chooseShortestPath(ArrayList<int[]> pathA, ArrayList<int[]> pathB) {
        if (pathA.isEmpty()) {
            return pathB;
        }
        if (pathB.isEmpty()) {
            return pathA;
        }
        if (pathA.size() <= pathB.size()) {
            return pathA;
        }
        return pathB;
    }

}
