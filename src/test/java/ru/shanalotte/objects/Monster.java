package ru.shanalotte.objects;

public class Monster extends Creature{

    private int goldDrops = 100;

    public int getGoldDrops() {
        return goldDrops;
    }

    public void setGoldDrops(int goldDrops) {
        this.goldDrops = goldDrops;
    }

    @Override
    public String toString() {
        return super.toString() + " goldDrops = " + goldDrops;
    }
}
