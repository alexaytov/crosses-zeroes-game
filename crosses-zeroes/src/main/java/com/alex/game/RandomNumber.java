package com.alex.game;

import java.util.Random;

public class RandomNumber {

    private int[][] ai;
    private int[][] human;

    RandomNumber(int size) {
        // TODO: 20/05/03 not null not zero
        initA(size);
        initB(size);
    }

    private void initA(int size) {
        ai = new int[size][size];
        for (int i = 0; i < ai.length; i++) {
            for (int j = 0; j < ai[i].length; j++) {
                ai[i][j] = new Random().nextInt();
            }
        }
    }

    private void initB(int size) {
        human = new int[size][size];
        for (int i = 0; i < human.length; i++) {
            for (int j = 0; j < human[i].length; j++) {
                human[i][j] = new Random().nextInt();
            }
        }
    }

    public int getAi(int row, int col) {
        return ai[row][col];
    }

    public int getHuman(int row, int col) {
        return human[row][col];
    }
}

