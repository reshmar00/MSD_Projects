package assignment07;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Graph {
    public static class Node {
        private Node cameFrom_;
        // goal.cameFrom, .cameFrom -> make linkedList
        private char data_;
        ArrayList<Node> neighbors_ = new ArrayList<>();

        public char getData(){
            return data_;
        }
        private int row_, column_;
        private boolean visited_ = false;
        public boolean didVisit(){
            return visited_;
        }
        private boolean pathExists_ = false;
        public boolean doesPathExist(){
            return pathExists_;
        }

        Node(char data){
            data_ = data;
            neighbors_ = new ArrayList<>();
        }

        Node(char data, int row, int column){
            data_ = data;
            row_ = row;
            column_ = column;
            neighbors_ = new ArrayList<>();
        }
    }
    Node[][] nodes_;
    private static int width_, height_;

    public Node getNode(int height, int width){
        return nodes_[height][width];
    }
    public int getWidth(){
        return width_;
    }
    public int getHeight(){
        return height_;
    }

    static Node start_;
    public Node getStart(){
        return start_;
    }
    static Node goal_;
    public Node getGoal(){
        return goal_;
    }

    public Node getLeft(int row, int column) {
        if (column > 0 && nodes_[row][column - 1].data_ != 'X') {
            return nodes_[row][column - 1];
        }
        return null;
    }

    public Node getRight(int row, int column) {
        if (column < getWidth()-1 && nodes_[row][column + 1].data_ != 'X'){
            return nodes_[row][column + 1];
        }
        return null;
    }

    public Node getTop(int row, int column) {
        if (row > 0 && nodes_[row - 1][column].data_ != 'X') {
            return nodes_[row - 1][column];
        }
        return null;
    }

    public Node getBottom(int row, int column) {
        if (row < getHeight()-1 && nodes_[row + 1][column].data_ != 'X'){
            return nodes_[row + 1][column];
        }
        return null;
    }

    public static Graph createMaze(String inputFile) throws FileNotFoundException {
        // Read input file
        File fileName = new File(inputFile);
        Scanner fileScanner = new Scanner(fileName);
        if (!fileName.exists()) {
            throw new FileNotFoundException();
        }

        // Extract relevant data
        String[] dimensions = fileScanner.nextLine().split(" ");
        height_ = Integer.parseInt(dimensions[0]);
        width_ = Integer.parseInt(dimensions[1]);

        Graph maze = new Graph();
        maze.nodes_ = new Node[height_][width_];

        // Fill graph
        while(fileScanner.hasNext()) {
            for (int i = 0; i < height_; i++) {
                String line = fileScanner.nextLine();//read each row_/line in
                for (int j = 0; j < width_; j++) {
                    char c = line.charAt(j);

                    if (c == 'S') { //when 'S' is found, shove it into the start node
                        start_ = new Node(c, i, j);
                        maze.nodes_[i][j] = start_;
                    } else if (c == 'G') {//when 'G' is found, shove it into the goal node
                        goal_ = new Node(c, i, j);
                        maze.nodes_[i][j] = goal_;
                    }
                    else {
                        maze.nodes_[i][j] = new Node(c);
                    }
                }
            }
        }
        maze.findNeighbors();
        return maze;
    }

    public void breadthFirstSearch(){
        start_.visited_ = true;
        LinkedList<Node> queue = new LinkedList<>();
        Node currentNode;
        queue.add(start_);

        while(!queue.isEmpty()){
            currentNode = queue.removeFirst();
            if(currentNode == goal_){ // follow cameFrom links back to start
                while(currentNode.cameFrom_ != null){
                    currentNode.cameFrom_.pathExists_ = true; // used to replace ' '  with '.'
                    currentNode = currentNode.cameFrom_;
                }
                return;
            }
            for(Node neighbor: currentNode.neighbors_){
                if(!neighbor.visited_){
                    neighbor.visited_ = true;
                    neighbor.cameFrom_ = currentNode;
                    queue.addLast(neighbor);
                }
            }
        }
        // If program reaches this point, it returns no path
    }

    public void findNeighbors(){
        for (int i = 0; i < height_; i++) {
            for (int j = 0; j < width_; j++) {
                Node temp = nodes_[i][j];
                if(nodes_[i][j].data_ != 'X'){
                    Node left = getLeft(i,j);
                    if(left != null && left.data_ != 'X'){
                        temp.neighbors_.add(left);
                    }
                    Node right = getRight(i,j);
                    if(right != null && right.data_ != 'X'){
                        temp.neighbors_.add(right);
                    }
                    Node top = getTop(i,j);
                    if(top != null && top.data_ != 'X'){
                        temp.neighbors_.add(top);
                    }
                    Node bottom = getBottom(i,j);
                    if(bottom != null && bottom.data_ != 'X'){
                        temp.neighbors_.add(bottom);
                    }
                }
            }
        }
    }

    public void printGraph() {
        for (int i = 0; i < height_; i++) {
            for (int j = 0; j < width_; j++) {
                System.out.print(nodes_[i][j].data_);
            }
            System.out.println();
        }
    }
}

