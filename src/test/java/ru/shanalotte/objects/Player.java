package ru.shanalotte.objects;

import java.util.Objects;

public class Player extends Creature{

    private int killed = 10;

    public int getKilled() {
        return killed;
    }

    public void setKilled(int killed) {
        this.killed = killed;
    }


}
