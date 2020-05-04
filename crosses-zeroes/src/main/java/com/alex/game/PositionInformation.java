package com.alex.game;

public class PositionInformation {

    private int evaluation;
    private int alpha;
    private int beta;

    public PositionInformation(int evalution, int alpha, int beta) {
        this.evaluation = evalution;
        this.alpha = alpha;
        this.beta = beta;
    }

    public int getEvaluation() {
        return evaluation;
    }

    public int getAlpha() {
        return alpha;
    }

    public int getBeta() {
        return beta;
    }
}
