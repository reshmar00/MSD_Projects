package assignment07;

import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GraphTest {

    @Test
    void TestAll() throws FileNotFoundException, IOException {
        TestCreateMaze();
        TestBreadthFirstSearch();
        TestFindNeighbors();
        TestMazeOutputs();
        System.out.println("All tests passed!");
    }

    @Test
    void TestCreateMaze() throws FileNotFoundException {
        // throws exception(s) - FileNotFound
        Graph testGraph = null;
        FileNotFoundException thrown =
                assertThrows(FileNotFoundException.class,
                        () -> {
                            testGraph.createMaze("src/assignment07/Mazes/filename.txt");
                        });
        assertEquals("src/assignment07/Mazes/filename.txt (No such file or directory)", thrown.getMessage());

        // Check if height and width are as expected
        Graph tinyGraph = new Graph();
        tinyGraph.createMaze("src/assignment07/Mazes/tinyMaze.txt");
        assertEquals(7, tinyGraph.getHeight());
        assertEquals(9, tinyGraph.getWidth());

        Graph mediumGraph = new Graph();
        mediumGraph.createMaze("src/assignment07/Mazes/mediumMaze.txt");
        assertEquals(18, mediumGraph.getHeight());
        assertEquals(36, mediumGraph.getWidth());

        Graph bigGraph = new Graph();
        bigGraph.createMaze("src/assignment07/Mazes/bigMaze.txt");
        assertEquals(37, bigGraph.getHeight());
        assertEquals(37, bigGraph.getWidth());

        // Check that the correct nodes are saved as 'start' and 'goal'
        assertEquals('S', tinyGraph.getStart().getData());
        assertEquals('G', tinyGraph.getGoal().getData());

        assertEquals('S', mediumGraph.getStart().getData());
        assertEquals('G', mediumGraph.getGoal().getData());

        assertEquals('S', bigGraph.getStart().getData());
        assertEquals('G', bigGraph.getGoal().getData());

        // Checking neighbor arraylist
        int actualNeighbors = tinyGraph.getGoal().neighbors_.size();
        assertEquals(1, actualNeighbors);
        actualNeighbors = tinyGraph.getStart().neighbors_.size();
        assertEquals(2, actualNeighbors);

    }

    @Test
    void TestBreadthFirstSearch() throws FileNotFoundException {
        Graph tinyGraph = new Graph();
        tinyGraph.createMaze("src/assignment07/Mazes/tinyMaze.txt");
        // Checking that .visited_ works
        assertFalse(tinyGraph.getStart().doesPathExist());
        tinyGraph.breadthFirstSearch();
        assertTrue(tinyGraph.getStart().doesPathExist());
        assertFalse(tinyGraph.getGoal().doesPathExist());

        Graph mediumGraph = new Graph();
        mediumGraph.createMaze("src/assignment07/Mazes/mediumMaze.txt");
        assertFalse(mediumGraph.getStart().doesPathExist());
        mediumGraph.breadthFirstSearch();
        assertTrue(mediumGraph.getStart().doesPathExist());
        assertFalse(mediumGraph.getGoal().doesPathExist());

        Graph bigGraph = new Graph();
        bigGraph.createMaze("src/assignment07/Mazes/bigMaze.txt");
        assertFalse(bigGraph.getStart().doesPathExist());
        bigGraph.breadthFirstSearch();
        assertTrue(bigGraph.getStart().doesPathExist());
        assertFalse(bigGraph.getGoal().doesPathExist());
    }

    @Test
    void TestFindNeighbors() throws FileNotFoundException {

        // Assert that 'X' is never included in neighbors_
        Graph tinyGraph = new Graph();
        tinyGraph.createMaze("src/assignment07/Mazes/tinyMaze.txt");
        Graph.Node xChar = new Graph.Node('X');
        assertFalse(tinyGraph.getStart().neighbors_.contains(xChar));
        assertFalse(tinyGraph.getGoal().neighbors_.contains(xChar));

        Graph mediumGraph = new Graph();
        mediumGraph.createMaze("src/assignment07/Mazes/mediumMaze.txt");
        assertFalse(mediumGraph.getStart().neighbors_.contains(xChar));
        assertFalse(mediumGraph.getGoal().neighbors_.contains(xChar));

        Graph bigGraph = new Graph();
        bigGraph.createMaze("src/assignment07/Mazes/bigMaze.txt");
        assertFalse(mediumGraph.getStart().neighbors_.contains(xChar));
        assertFalse(mediumGraph.getGoal().neighbors_.contains(xChar));
    }

    @Test
    void TestMazeOutputs() throws IOException {
        /* How the following tests work: the program reads two .txt files,
        * it then goes line by line and compares the files
        * if the lines are not equal, it returns '1'. If they are,
        * it returns -1 */

        /* Tiny Maze */
        long lineNumber = 1;
        PathFinder.solveMaze("src/assignment07/Mazes/tinyMaze.txt", "src/assignment07/Mazes/tinyMazeOutput.txt");
        Path path1 = Paths.get("src/assignment07/Mazes/tinyMazeSol.txt");
        Path path2 = Paths.get("src/assignment07/Mazes/tinyMazeOutput.txt");
        try (BufferedReader bf1 = Files.newBufferedReader(path1);
             BufferedReader bf2 = Files.newBufferedReader(path2)) {

            String line1 = "", line2 = "";
            while ((line1 = bf1.readLine()) != null) {
                line2 = bf2.readLine();
                if (line2 == null || !line1.equals(line2)) {
                    return;
                }
                lineNumber++;
            }
            if (bf2.readLine() == null) {
                lineNumber = -1;
            }
            else{
                lineNumber = 1;
            }
        }

        assertEquals(-1, lineNumber);

        /* Big Maze*/
        long lineNumber2 = 1;
        PathFinder.solveMaze("src/assignment07/Mazes/bigMaze.txt", "src/assignment07/Mazes/bigMazeOutput.txt");
        Path path3 = Paths.get("src/assignment07/Mazes/bigMazeSol.txt");
        Path path4 = Paths.get("src/assignment07/Mazes/bigMazeOutput.txt");
        try (BufferedReader bf3 = Files.newBufferedReader(path3);
             BufferedReader bf4 = Files.newBufferedReader(path4)) {

            String line1 = "", line2 = "";
            while ((line1 = bf3.readLine()) != null) {
                line2 = bf4.readLine();
                if (line2 == null || !line1.equals(line2)) {
                    return;
                }
                lineNumber2++;
            }
            if (bf4.readLine() == null) {
                lineNumber2 = -1;
            }
            else{
                lineNumber2 = 1;
            }
        }

        assertEquals(-1, lineNumber2);

        /* Classic Maze */
        long lineNumber3 = 1;
        PathFinder.solveMaze("src/assignment07/Mazes/classic.txt", "src/assignment07/Mazes/classicOutput.txt");
        Path path5 = Paths.get("src/assignment07/Mazes/classicSol.txt");
        Path path6 = Paths.get("src/assignment07/Mazes/classicOutput.txt");
        try (BufferedReader bf5 = Files.newBufferedReader(path5);
             BufferedReader bf6 = Files.newBufferedReader(path6)) {

            String line1 = "", line2 = "";
            while ((line1 = bf5.readLine()) != null) {
                line2 = bf6.readLine();
                if (line2 == null || !line1.equals(line2)) {
                    return;
                }
                lineNumber3++;
            }
            if (bf6.readLine() == null) {
                lineNumber3 = -1;
            }
            else{
                lineNumber3 = 1;
            }
        }

        assertEquals(-1, lineNumber3);

        /* Demo Maze */
        PathFinder.solveMaze("src/assignment07/Mazes/demoMaze.txt", "src/assignment07/Mazes/demoMazeOutput.txt");
        List<String> s1 = Files.readAllLines(Path.of("src/assignment07/Mazes/demoMazeOutput.txt"));
        int count = 0;
        for (String s: s1) {
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) == '.') {
                    count++;
                }
            }
        }
        List<String> s2 = Files.readAllLines(Path.of("src/assignment07/Mazes/demoMazeSol.txt"));
        int count2 = 0;
        for (String s: s2) {
            for (int i = 0; i < s.length(); i++) {
                if (s.charAt(i) == '.') {
                    count2++;
                }
            }
        }
        assertEquals(count, count2);

        /* Medium Maze */
        long lineNumber5 = 1;
        PathFinder.solveMaze("src/assignment07/Mazes/mediumMaze.txt", "src/assignment07/Mazes/mediumMazeOutput.txt");
        Path path9 = Paths.get("src/assignment07/Mazes/mediumMazeSol.txt");
        Path path10 = Paths.get("src/assignment07/Mazes/mediumMazeOutput.txt");
        try (BufferedReader bf9 = Files.newBufferedReader(path9);
             BufferedReader bf10 = Files.newBufferedReader(path10)) {

            String line1 = "", line2 = "";
            while ((line1 = bf9.readLine()) != null) {
                line2 = bf10.readLine();
                if (line2 == null || !line1.equals(line2)) {
                    return;
                }
                lineNumber5++;
            }
            if (bf10.readLine() == null) {
                lineNumber5 = -1;
            }
            else{
                lineNumber5 = 1;
            }
        }

        assertEquals(-1, lineNumber5);

        /* Random Maze */
        long lineNumber6 = 1;
        PathFinder.solveMaze("src/assignment07/Mazes/randomMaze.txt", "src/assignment07/Mazes/randomMazeOutput.txt");
        Path path11 = Paths.get("src/assignment07/Mazes/randomMazeSol.txt");
        Path path12 = Paths.get("src/assignment07/Mazes/randomMazeOutput.txt");
        try (BufferedReader bf11 = Files.newBufferedReader(path11);
             BufferedReader bf12 = Files.newBufferedReader(path12)) {

            String line1 = "", line2 = "";
            while ((line1 = bf11.readLine()) != null) {
                line2 = bf12.readLine();
                if (line2 == null || !line1.equals(line2)) {
                    return;
                }
                lineNumber6++;
            }
            if (bf12.readLine() == null) {
                lineNumber6 = -1;
            }
            else{
                lineNumber6 = 1;
            }
        }

        assertEquals(-1, lineNumber6);

        /* Straight Maze */
        long lineNumber7 = 1;
        PathFinder.solveMaze("src/assignment07/Mazes/straight.txt", "src/assignment07/Mazes/straightOutput.txt");
        Path path13 = Paths.get("src/assignment07/Mazes/straightSol.txt");
        Path path14 = Paths.get("src/assignment07/Mazes/straightOutput.txt");
        try (BufferedReader bf13 = Files.newBufferedReader(path13);
             BufferedReader bf14 = Files.newBufferedReader(path14)) {

            String line1 = "", line2 = "";
            while ((line1 = bf13.readLine()) != null) {
                line2 = bf14.readLine();
                if (line2 == null || !line1.equals(line2)) {
                    return;
                }
                lineNumber7++;
            }
            if (bf14.readLine() == null) {
                lineNumber7 = -1;
            }
            else{
                lineNumber7 = 1;
            }
        }

        assertEquals(-1, lineNumber7);

        /* Tiny Open Maze */
        long lineNumber8 = 1;
        PathFinder.solveMaze("src/assignment07/Mazes/tinyOpen.txt", "src/assignment07/Mazes/tinyOpenOutput.txt");
        Path path15 = Paths.get("src/assignment07/Mazes/tinyOpenSol.txt");
        Path path16 = Paths.get("src/assignment07/Mazes/tinyOpenOutput.txt");
        try (BufferedReader bf15 = Files.newBufferedReader(path15);
             BufferedReader bf16 = Files.newBufferedReader(path16)) {

            String line1 = "", line2 = "";
            while ((line1 = bf15.readLine()) != null) {
                line2 = bf16.readLine();
                if (line2 == null || !line1.equals(line2)) {
                    return;
                }
                lineNumber8++;
            }
            if (bf16.readLine() == null) {
                lineNumber8 = -1;
            }
            else{
                lineNumber8 = 1;
            }
        }

        assertEquals(-1, lineNumber8);

        /* Turn Maze */
        long lineNumber9 = 1;
        PathFinder.solveMaze("src/assignment07/Mazes/turn.txt", "src/assignment07/Mazes/turnOutput.txt");
        Path path17 = Paths.get("src/assignment07/Mazes/turnSol.txt");
        Path path18 = Paths.get("src/assignment07/Mazes/turnOutput.txt");
        try (BufferedReader bf17 = Files.newBufferedReader(path17);
             BufferedReader bf18 = Files.newBufferedReader(path18)) {

            String line1 = "", line2 = "";
            while ((line1 = bf17.readLine()) != null) {
                line2 = bf18.readLine();
                if (line2 == null || !line1.equals(line2)) {
                    return;
                }
                lineNumber9++;
            }
            if (bf18.readLine() == null) {
                lineNumber9 = -1;
            }
            else{
                lineNumber9 = 1;
            }
        }

        assertEquals(-1, lineNumber9);

        /* Unsolvable Maze */
        long lineNumber10 = 1;
        PathFinder.solveMaze("src/assignment07/Mazes/turn.txt", "src/assignment07/Mazes/turnOutput.txt");
        Path path19 = Paths.get("src/assignment07/Mazes/turnSol.txt");
        Path path20 = Paths.get("src/assignment07/Mazes/turnOutput.txt");
        try (BufferedReader bf19 = Files.newBufferedReader(path19);
             BufferedReader bf20 = Files.newBufferedReader(path20)) {

            String line1 = "", line2 = "";
            while ((line1 = bf19.readLine()) != null) {
                line2 = bf20.readLine();
                if (line2 == null || !line1.equals(line2)) {
                    return;
                }
                lineNumber10++;
            }
            if (bf20.readLine() == null) {
                lineNumber10 = -1;
            }
            else{
                lineNumber10 = 1;
            }
        }

        assertEquals(-1, lineNumber10);

    }
}