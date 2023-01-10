package assignment07;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PathFinder {

    static LinkedList<Node> queue_ = new LinkedList<>();
    static int height_ = 0, width_ = 0;
    static String outputFile_;
    static Node[][] maze_;
    static Node start_;
    static Node goal_;

    /**
     * Main method to read in a maze file, find the shortest path, and print out the maze showing the path
     * @param inputFile
     *              -- file to read in
     * @param outputFile
     *              -- file to write out to, create if it doesn't exist, overwrite if it already exists
     */
    public static void solveMaze(String inputFile, String outputFile) {

        outputFile_ = outputFile;

        readMaze(inputFile);

        setNeighbours();

        breadthFirstSearch();

        printMaze();

    }

    /**
     * Node class that creates nodes to populate a matrix of nodes
     */
    public static class Node {
        Integer i_ = null;
        Integer j_ = null;
        List<Node> neighbours;
        Node cameFrom = null;
        boolean path = false;
        boolean visited = false;

        public Node(int i, int j){
            i_ = i;
            j_ = j;

            neighbours = new ArrayList<>();
        }
    }

    /**
     * Reads in and parses a maze file
     * @param inputFile
     *          -- text file to be read in and parsed
     */
    private static void readMaze(String inputFile){

        try {
            Reader reader = new FileReader(inputFile);

            BufferedReader input = new BufferedReader(reader);

            String[] dimensions = input.readLine().split(" ");
            height_ = Integer.parseInt(dimensions[0]);
            width_ = Integer.parseInt(dimensions[1]);

            maze_ = new Node[width_][height_];

            int lineNum = 0;
            String line = input.readLine();
            while (line != null){
                for (int i=0; i<line.length(); i++){
                    char c = line.charAt(i);

                    if (c != 'X') {
                        maze_[i][lineNum] = new Node(i, lineNum);

                        if (c == 'S') {
                            start_ = maze_[i][lineNum];
                        } else if (c == 'G') {
                            goal_ = maze_[i][lineNum];
                        }
                    }
                }

                lineNum++;
                line = input.readLine();
            }

        } catch (IOException e){
            e.printStackTrace();
        }

    }

    /**
     * set the list of neighbour nodes that each node has
     */
    public static void setNeighbours(){
        for (int j=1; j<height_-1; j++){
            for (int i=1; i<width_-1; i++){
                if (maze_[i][j] != null) {
                    if (maze_[i - 1][j] != null) {
                        maze_[i][j].neighbours.add(maze_[i - 1][j]);
                    }
                    if (maze_[i][j - 1] != null) {
                        maze_[i][j].neighbours.add(maze_[i][j - 1]);
                    }
                    if (maze_[i + 1][j] != null) {
                        maze_[i][j].neighbours.add(maze_[i + 1][j]);
                    }
                    if (maze_[i][j + 1] != null) {
                        maze_[i][j].neighbours.add(maze_[i][j + 1]);
                    }
                }
            }
        }
    }

    /**
     * Does a Breadth First Search through the maze, finding the shortest path between the
     * start and the goal, if one exists
     */
    private static void breadthFirstSearch() {
        start_.visited = true;

        queue_.add(start_);

        while (!queue_.isEmpty()){
            Node curr = queue_.removeFirst();

            if (curr.equals(goal_)){
                Node n = goal_;
                while(n.cameFrom != start_){
                    n.cameFrom.path = true;
                    n = n.cameFrom;
                }
                return;
            }

            for (Node n : curr.neighbours){
                if (!n.visited){
                    n.visited = true;
                    n.cameFrom = curr;
                    queue_.add(n);
                }
            }

        }

    }

    /**
     * Print out the solved maze to text file
     */
    private static void printMaze() {
        try{
            PrintWriter output = new PrintWriter(new FileWriter(outputFile_));
            System.out.println(height_ + " " + width_);
            output.println(height_ + " " + width_);

            StringBuilder lineOut = new StringBuilder();
            for (int j=0; j<height_; j++){
                for (int i=0; i<width_; i++){
                    if (maze_[i][j] != null){
                        if (maze_[i][j] == start_){
                            lineOut.append("S");
                        }
                        else if (maze_[i][j] == goal_){
                            lineOut.append("G");
                        }
                        else if (maze_[i][j].path){
                            lineOut.append(".");
                        }
                        else {
                            lineOut.append(" ");
                        }
                    }
                    else {
                        lineOut.append("X");
                    }
                }
                System.out.println(lineOut);
                output.println(lineOut);
                output.flush();
                lineOut = new StringBuilder("");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
