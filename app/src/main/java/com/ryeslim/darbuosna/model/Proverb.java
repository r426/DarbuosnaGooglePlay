package com.ryeslim.darbuosna.model;

public class Proverb {
    private short theID;
    private String proverb;

    public Proverb(short theID, String proverb) {
        this.theID = theID;
        this.proverb = proverb;
    }

    public short getTheID() {
        return theID;
    }

    public String getProverb() {
        return proverb;
    }
}
