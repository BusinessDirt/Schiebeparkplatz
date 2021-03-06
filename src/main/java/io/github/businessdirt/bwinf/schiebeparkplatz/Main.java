package io.github.businessdirt.bwinf.schiebeparkplatz;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static final char EMPTY_SLOT_CHAR = '-';

    public static InputStream inputStream;
    public static List<String> inputLines;
    public static FileHandler fileHandler = FileHandler.get();

    public static List<Character> parkingLot = new ArrayList<>();
    public static List<CrossParker> crossParkers = new ArrayList<>();

    public static int parkingSpaces;

    public static void main(String[] args) {
        calculate("parkplatz0.txt");
        calculate("parkplatz1.txt");
        calculate("parkplatz2.txt");
        calculate("parkplatz3.txt");
        calculate("parkplatz4.txt");
        calculate("parkplatz5.txt");
    }

    /**
     * Calculates the moves required to un-park every car
     * @param fileName the name of the file, that has the information for the board
     */
    private static void calculate(String fileName) {
        System.out.println("\nCalculating " + fileName.substring(0, fileName.length() - 4) + ":");

        // Sets the start of the timer
        long startTime = System.nanoTime();

        // Parses the content of the file into a list
        // every String in the List is a separate line in the file
        inputStream = fileHandler.getFileFromResourceAsStream(fileName);
        inputLines = fileHandler.fileToList(inputStream);

        // Gets the first line of the file and removes it
        // Splits it at the regex " "
        // Gets the second value of it and puts it into lower case
        // Subtracts 96 from that character to get the length (see ASCII Table)
        parkingSpaces = inputLines.remove(0).trim().split(" ")[1].toLowerCase().charAt(0) - 96;
        inputLines.remove(0);

        // Gets all the data for the cross-parker
        crossParkers.clear();
        crossParkers.addAll(CrossParker.getAllFromData(inputLines));

        parkingLot.clear();
        // Adds an empty slot for every parking space
        for (int i = 0; i < parkingSpaces; i++) {
            parkingLot.add(EMPTY_SLOT_CHAR);
        }
        // Inserts all cross parker into the list
        for (CrossParker crossParker : crossParkers) {
            crossParker.putIntoList(parkingLot);
        }

        // loops through all the parking spaces
        for (int i = 0; i < parkingSpaces; i++) {
            if (parkingLot.get(i) == EMPTY_SLOT_CHAR) {
                // Prints out the capital letter (see ASCII table, 65 is 'A' in ASCII, 65 + 1 is 'B', ...)
                System.out.print((char) (i + 65) + ": -");
            } else {
                // maps for the moves
                Map<Character, Integer> leftMoves = generateMoves(Direction.LEFT, i);
                Map<Character, Integer> rightMoves = generateMoves(Direction.RIGHT, i);

                // Prints out the capital letter (see ASCII table, 65 is 'A' in ASCII, 65 + 1 is 'B', ...)
                System.out.print((char) (i + 65) + ": ");

                if (leftMoves == null && rightMoves == null) {
                    System.out.print("Car can't get out");
                } else if (rightMoves == null && leftMoves != null) {
                    printMap(leftMoves, Direction.LEFT);
                } else if (leftMoves == null && rightMoves != null) {
                    printMap(rightMoves, Direction.RIGHT);
                } else {
                    int tempLeft = 0;
                    for (Integer integer : leftMoves.values()) {
                        tempLeft += integer;
                    }

                    int tempRight = 0;
                    for (Integer integer : rightMoves.values()) {
                        tempRight += integer;
                    }

                    if (tempLeft < tempRight) {
                        printMap(leftMoves, Direction.LEFT);
                    } else {
                        printMap(rightMoves, Direction.RIGHT);
                    }
                }
            }
            System.out.print("\n");
        }
        long endTime = System.nanoTime();
        System.out.println("Took " + ((endTime - startTime) / 1000000) + "ms");
    }

    /**
     * @param dir the movement direction - 0 for left, 1 for right
     * @param slot the starting slot
     * @return a map of all the cross parked cars that need to be moved
     */
    public static Map<Character, Integer> generateMoves(Direction dir, int slot) {
        Map<Character, Integer> moves = new HashMap<>();
        List<Character> row = new ArrayList<>(parkingLot);
        char currentCharacter = row.get(slot);

        while (row.get(slot) != EMPTY_SLOT_CHAR) {
            if(row.indexOf(currentCharacter) == 0 && dir == Direction.LEFT || row.indexOf(currentCharacter) == parkingSpaces - 2 && dir == Direction.RIGHT) return null;
            if (check(row, dir, currentCharacter) == EMPTY_SLOT_CHAR) {

                // move the car
                if (dir == Direction.LEFT) {
                    row.set(row.lastIndexOf(currentCharacter), EMPTY_SLOT_CHAR);
                    row.set(row.indexOf(currentCharacter) - 1, currentCharacter);
                } else if (dir == Direction.RIGHT) {
                    row.set(row.indexOf(currentCharacter), EMPTY_SLOT_CHAR);
                    row.set(row.indexOf(currentCharacter) + 1, currentCharacter);
                }

                // update the map
                if (moves.containsKey(currentCharacter)){
                    moves.replace(currentCharacter, moves.get(currentCharacter) + 1);
                } else {
                    moves.put(currentCharacter, 1);
                }

                // update the character
                currentCharacter = row.get(slot);
            } else currentCharacter = check(row, dir, currentCharacter);
        }
        return moves;
    }

    /**
     * Checks the slot in the given direction
     * @param row the row with the cars
     * @param direction the direction to check
     * @param car the slot of the car
     * @return the char of the car next to the {@code car}
     */
    public static char check(List<Character> row, Direction direction, char car) {
        if (direction == Direction.LEFT) {
            if (row.get(row.indexOf(car)) == row.get(row.indexOf(car) - 1)) {
                return row.get(row.indexOf(car) - 2);
            } else return row.get(row.indexOf(car) - 1);
        } else {
            if (row.get(row.lastIndexOf(car)) == row.get(row.lastIndexOf(car) + 1)) {
                return row.get(row.lastIndexOf(car) + 2);
            } else return row.get(row.lastIndexOf(car) + 1);
        }
    }

    /**
     * Prints out the map with the direction and car character
     * @param moves the map to be printed
     * @param direction the direction
     */
    public static void printMap(Map<Character, Integer> moves, Direction direction) {
        for (Map.Entry<Character, Integer> move : moves.entrySet()) {
            System.out.print(move.getKey().toString().toUpperCase() + " " + move.getValue() + " " + direction.toString() + " ");
        }
    }
}
