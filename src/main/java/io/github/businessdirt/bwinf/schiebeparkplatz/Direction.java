package io.github.businessdirt.bwinf.schiebeparkplatz;

public enum Direction {
    LEFT, RIGHT;

    public String toString() {
        switch (this) {
            case LEFT:
                return "left";
            case RIGHT:
                return "right";
            default:
                return "null";
        }
    }
}
