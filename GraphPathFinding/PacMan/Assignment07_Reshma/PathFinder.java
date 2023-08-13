package assignment07;

import java.io.*;
import java.util.ArrayList;

public class PathFinder {
    public static void solveMaze(String inputFile, String outputFile){
        Graph graph = null;
        try {
            graph = Graph.createMaze(inputFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        // do breadth first search
        graph.breadthFirstSearch();
        // if part of the path, replace space with '.' in output file
        PrintWriter printWriter = null;
        File file;
        try{
            file = new File(outputFile);
            printWriter = new PrintWriter(file);
            printWriter.write(graph.getHeight() + " " + graph.getWidth() + "\n");
          //  System.out.println("Height: " + graph.getHeight() + " Width: " + graph.getWidth());
            for(int i = 0; i < graph.getHeight(); i++) {
                for (int j = 0; j < graph.getWidth(); j++) {
                    if(graph.nodes_[i][j].getData() == 'X'){
                        printWriter.print('X');
                    }
                    else if(graph.nodes_[i][j].getData() == 'S'){
                        printWriter.print('S');
                    }
                    else if(graph.nodes_[i][j].getData() == 'G'){
                        printWriter.print('G');
                    }
                    else if (graph.nodes_[i][j].getData() == ' ' && graph.nodes_[i][j].doesPathExist()) {
                        printWriter.print('.');
                    }
                    else if (graph.nodes_[i][j].getData() == ' ' && !graph.nodes_[i][j].doesPathExist()) {
                        printWriter.print(' ');
                    }
                }
                printWriter.print("\n");
            }
            printWriter.flush();
            printWriter.close();
        } catch(FileNotFoundException e){
            e.printStackTrace();
        }
    }
}
