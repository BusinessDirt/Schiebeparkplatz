package io.github.businessdirt.bwinf.schiebeparkplatz;

import java.util.ArrayList;
import java.util.List;

public class CrossParker {

    private int x;
    private Character character;

    public CrossParker(int x, Character character) {
        this.x = x;
        this.character = character;
    }

    public static List<CrossParker> getAllFromData(List<String> data) {
        List<CrossParker> result = new ArrayList<>();
        for (String str : data) {
            String[] temp = str.split(" ");
            int x = Integer.parseInt(temp[1]);
            Character character = temp[0].charAt(0);
            result.add(new CrossParker(x, character));
        }
        return result;
    }

    public void putIntoList(List<Character> parkingLot) {
        parkingLot.set(this.x, this.character);
        parkingLot.set(this.x + 1, this.character);
    }

    public static CrossParker getByCharacter(List<CrossParker> crossParkers, char character) {
        CrossParker result = null;
        for (CrossParker crossParker : crossParkers) {
            if (crossParker.getCharacter() == character) result = crossParker;
        }
        return result;
    }

    public static CrossParker getByX(List<CrossParker> crossParkers, int x) {
        CrossParker result = null;
        for (CrossParker crossParker : crossParkers) {
            if (crossParker.getX() == x) result = crossParker;
        }
        return result;
    }

    public int getX() {
        return x;
    }

    public Character getCharacter() {
        return character;
    }
}
