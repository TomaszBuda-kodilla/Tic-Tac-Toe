package com.kodilla.project.tictaktoe;

public class Players implements Score {
    String name;
    int score;

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public Players(String name, int score) {
        this.name = name;
        this.score = score;
    }
    public void restartScore(){
        score = 0;
    }
    public int setScore(){
        score += score;
        return score;
    }
}
