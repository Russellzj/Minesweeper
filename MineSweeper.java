import java.util.*;
import java.util.Arrays;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Deque;
import java.util.ArrayDeque;

class MineSweeper {
    private final int xAxis; //size of x axis
    private final int yAxis; //size of y axis
    private int bombs; //number of bombs to be created on the field
    private int[] bombLocations; //bomb locations as int values
    private String[][] field; //play field
    private String[][] playField;//= new String[xAxis][yAxis]; //play field
    ArrayList<Integer> mineMarks = new ArrayList<>(); // Location of player specified mine markers
    MineSweeper(int xAxis, int yAxis){
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        field = new String[xAxis][yAxis];
        playField = new String[xAxis][yAxis];
        for (String[] row : playField) {
            Arrays.fill(row, ".");
        }
    }

    //Retrieves desired number of bombs from the player
    public void setBombCount(){
        Scanner sc = new Scanner(System.in);
        boolean unacceptableBombCount = true;
        int bombs;

        do {
            System.out.println("How many mines do you want on the field?");
            bombs = sc.nextInt();
            //Makes sure the number of bombs is less than the size of the map minus one so that the game can be played
            if (bombs <= xAxis * yAxis) {
                unacceptableBombCount = false;
            } else {
                System.out.println("Too many bombs have been selected. Please choose a value equal or less than " +
                        (xAxis * yAxis - 1) + ".");
            }
        } while (unacceptableBombCount);
        this.bombs = bombs;
        //sc.close();
    }

    //Randomly assigns location of bombs
    public void createBombCoordinates(int x, int y){
        ArrayList<Integer> coordinates = new ArrayList<>(); // Array for possible bomb locations
        //Fill Array will all possible coordinates
        for (int i = 0; i < xAxis * yAxis; i++) {
            if (i != coordinatesToValue(x, y)) {
                coordinates.add(i);
            }
        }
        Random random = new Random();
        //Array to keep bomb locations
        bombLocations = new int[bombs];
        for (int i = 0; i < bombs; i++) {
            //gets random location of next bomb
            int bombPlant = random.nextInt(coordinates.size());
            //Adds the bomb's coordinates to the array
            bombLocations[i]  = coordinates.get(bombPlant);
            coordinates.remove(bombPlant);
        }
    }

    //Translates coordinates of x and y into an int value
    private int coordinatesToValue(int x, int y) {
        return x * field[x].length + y;
    }

    //Creates field with bombs on field
    public void createMinefield() {
        for (int x = 0; x < xAxis; x++) {
            for (int y = 0; y < yAxis; y++) {
                if(isBombLocation(coordinatesToValue(x, y))){
                    field[x][y] = "X";
                } else {
                    int bombCount = detectBomb(x, y);
                    //System.out.println("x: " + x + "y:" + y);
                    field[x][y] = bombCount != 0 ? String.valueOf(bombCount) : "/";
                }
            }
        }
    }

    //Adds mines to the playField if the player loses
    public void gameOverMines(){
            for (int bomb : bombLocations) {
                playField[bomb/yAxis][bomb%yAxis] = "X";
            }
        }

    //Returns int of number of bombs around a location x, y
    public int detectBomb(int x, int y) {
        int bombCount = 0;
        //System.out.println("TESTING LOCATION: " + x + " " + y);
        for(int i = Math.max(x - 1, 0); i <= x + 1  && i < playField.length ; i++) {
            for(int j = Math.max(y - 1, 0); j < y + 2 && j < field[i].length; j++) {
                //Checks to see if location is in bombLocations
                if (isBombLocation(coordinatesToValue(i, j))) {
                    //System.out.println("DBV2 Location: " + i + " " + j);
                    bombCount++;
                }
            }
        }
        return bombCount;
    }

    //returns true if location is a location with a bomb
    public boolean isBombLocation(int location) {
        Arrays.sort(bombLocations);
        int index = Arrays.binarySearch(bombLocations, location);
        return index >= 0;
    }

    //Prints map in new format
    public void printPlayerMap(){
        //Prints coordinate map ex: |123|
        System.out.print(" |");
        for (int i = 0; i < playField[0].length; i++) {
            System.out.print(i + 1);
        }
        System.out.println("|");
        //Prints an empty field with dashes "-"
        System.out.print("-|");
        for (int i = 0; i < playField[0].length; i++) {
            System.out.print("-");
        }
        System.out.println("|");

        for (int i = 0; i < playField.length; i++) {
            System.out.print(i + 1 + "|");
            for (int j = 0; j < playField[i].length; j++) {
                System.out.print(playField[i][j]);
            }
            System.out.println("|");
        }
        System.out.print("-|");
        for (int i = 0; i < playField[0].length; i++) {
            System.out.print("-");
        }
        System.out.println("|");
    }

    //Prints map in new format
    public void printHiddenMap(){
        //Prints coordinate map ex: |123|
        System.out.print(" |");
        for (int i = 0; i < field[0].length; i++) {
            System.out.print(i + 1);
        }
        System.out.println("|");
        //Prints an empty field with dashes "-"
        System.out.print("-|");
        for (int i = 0; i < field[0].length; i++) {
            System.out.print("-");
        }
        System.out.println("|");

        for (int i = 0; i < field.length; i++) {
            System.out.print(i + 1 + "|");
            for (int j = 0; j < field[i].length; j++) {
                System.out.print(field[i][j]);
            }
            System.out.println("|");
        }
        System.out.print("-|");
        for (int i = 0; i < field[0].length; i++) {
            System.out.print("-");
        }
        System.out.println("|");
    }

    //ClearSpace without using recursion
    public void clearSpace(int x, int y) {
        playField[x][y] = field[x][y];
        System.out.println("Start Area: " + x + " " + y);
        Deque<Integer[]> deque = new ArrayDeque<>();
        deque.push(new Integer[]{x, y});
        do {
            assert deque.peek() != null;
            x = deque.peek()[0];
            y = deque.pop()[1];
            for(int i = Math.max(x - 1, 0); i <= x + 1 && i < playField.length; i++) {
                for(int j = Math.max(y - 1, 0); j < y + 2 && j < field[i].length; j++) {
                    if (playField[i][j].equals(".") || playField[i][j].equals("*")){
                        playField[i][j] = field[i][j];
                        if (field[i][j].equals("/")) {
                            deque.push(new Integer[] {i, j});
                        }
                    }
                }
            }
        } while (deque.peek() != null);
    }

    //ClearSpace using Recursion
    public void clearSpaceRecursion(int x, int y) {
        playField[x][y] = field[x][y];
        //System.out.println("Start Area: " + x + " " + y);
        for(int i = Math.max(x - 1, 0); i <= x + 1 && i < playField.length; i++) {
            for(int j = Math.max(y - 1, 0); j < y + 2 && j < field[i].length; j++) {
                if (playField[i][j].equals(".")) {
                    playField[i][j] = field[i][j];
                    if (field[i][j].equals("/")) {
                        clearSpaceRecursion(i, j);
                    }
                } else if (playField[i][j].equals("*")) {
                    playField[i][j] = field[i][j];
                    mineMarks.remove(mineMarks.indexOf(coordinatesToValue(i, j)));
                }
            }
        }
    }
/*
    public void firstRound() {
        boolean firstRoundEnd = false;
        do {
            Scanner sc = new Scanner(System.in);
            System.out.println("Set/unset mines marks or claim a cell as free:");
            int x= sc.nextInt();
            int y = sc.nextInt();
            String choice = sc.next();
            x--;
            y--;
            if(x >= field.length || y >= field[0].length || y < 0 || x < 0) {
                System.out.println("Coordinates outside of game field!");
            } else if (!choice.equals("mine")  && !choice.equals("free")){
                System.out.println(choice + " is not an acceptable input!");
                System.out.println("Please enter \"mine\" or \"free\" as your choices.");
            } else {
                switch (choice) {
                    case "mine":
                        if (playField[x][y].equals(".")) {
                            playField[x][y] = "*";
                            mineMarks.add(coordinatesToValue(x, y));
                        } else if (playField[x][y].equals("*")) {
                            playField[x][y] = ".";
                            mineMarks.remove(mineMarks.indexOf(coordinatesToValue(x, y)));
                        }
                        break;
                    case "free":
                        createBombCoordinates(x, y);
                        createMinefield();
                        if (field[x][y].equals("/")) {
                            clearSpaceRecursion(x, y);
                        } else {
                            playField[x][y] = field[x][y];
                        }
                        firstRoundEnd = true;
                }
                printPlayerMap();
            }
        } while (!firstRoundEnd);
    }

 */

    public void play() {
        Scanner sc = new Scanner(System.in);
        int moveCount = 0;
        ArrayList<Integer> bombs = new ArrayList<>();

        boolean continueGame = true;
        boolean win = true;
        while (continueGame) {
            System.out.println("Set/unset mines marks or claim a cell as free:");
            int x = sc.nextInt();
            int y = sc.nextInt();
            x--;
            y--;
            String choice = sc.next();
            if(x >= field.length || y >= field[0].length || y < 0 || x < 0) {
                System.out.println("Coordinates outside of game field!");
            } else if (!choice.equals("mine")  && !choice.equals("free")){
                System.out.println(choice + " is not an acceptable input!");
                System.out.println("Please enter \"mine\" or \"free\" as your choices.");
            } else {
                switch (choice) {
                    case "mine":
                        switch (playField[x][y]) {
                            case ".":
                                playField[x][y] = "*";
                                System.out.println("Adding Coordinate: " + "X: " + x + "Y:" + y + "Value:" + coordinatesToValue(x, y));
                                mineMarks.add(coordinatesToValue(x, y));
                                break;
                            case "*":
                                playField[x][y] = ".";
                                mineMarks.remove(mineMarks.indexOf(coordinatesToValue(x, y)));
                                break;
                            default:
                                System.out.println("This spot has already been revealed!");
                        }
                        printPlayerMap();
                        if (moveCount != 0) {
                            Collections.sort(mineMarks);
                            if(mineMarks.equals(bombs)) {
                                win = mineMarks.equals(bombs);
                                continueGame = false;
                            }
                        }

                        break;
                    case "free":
                        if (moveCount == 0) {
                            createBombCoordinates(x, y);
                            createMinefield();
                            System.out.println("Hidden Map");
                            printHiddenMap();
                            for(int bomb : bombLocations) {
                                bombs.add(bomb);
                            }
                            Collections.sort(bombs);
                        }
                        if (playField[x][y].equals(".")) {
                            switch (field[x][y]) {
                                case "X":
                                    win = false;
                                    continueGame = false;
                                    gameOverMines();
                                    break;
                                case "/":
                                    clearSpaceRecursion(x, y);
                                    break;
                                default:
                                    playField[x][y] = field[x][y];
                                    }
                            }
                        moveCount++;
                        }
                        printPlayerMap();
                }
            }
        if (win) {
            System.out.println("Congratulations! You found all the mines!");
        } else {
            System.out.println("You stepped on a mine and failed!");
        }
    }
}