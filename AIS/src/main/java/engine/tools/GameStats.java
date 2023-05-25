package engine.tools;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 *  Class, that controls the game progress.
 */
public class GameStats {
    private int roundNumber;
    private int allyTotalCount;
    private int enemyTotalCount;
    private int allyCount;
    private int enemyCount;
    private int movesLeft;
    private final AtomicBoolean isAllyPhase;

    public GameStats(int allyTotalCount, int enemyTotalCount) {
        this.roundNumber = 1;
        this.allyTotalCount = allyTotalCount;
        this.enemyTotalCount = enemyTotalCount;
        this.allyCount = allyTotalCount;
        this.enemyCount = enemyTotalCount;
        this.movesLeft = this.allyCount;
        isAllyPhase = new AtomicBoolean(true);
    }

    public void setIsAllyPhase(boolean state) {
        isAllyPhase.set(state);
    }

    public boolean isAllyPhase() {
        return isAllyPhase.get();
    }

    public int getRoundNumber() {
        return roundNumber;
    }

    public void nextRound() {
        roundNumber++;
        movesLeft = allyCount;
    }

    public void doMove() {
        movesLeft--;
    }

    public int getMovesCount() {
        return movesLeft;
    }

    public int getAllyTotalCount() {
        return allyTotalCount;
    }

    public void setAllyTotalCount(int allyTotalCount) {
        this.allyTotalCount = allyTotalCount;
    }

    public void decreaseAllyCount() {
        allyCount--;
    }

    public void decreaseEnemyCount() {
        enemyCount--;
    }

    public int getEnemyTotalCount() {
        return enemyTotalCount;
    }

    public void setEnemyTotalCount(int enemyTotalCount) {
        this.enemyTotalCount = enemyTotalCount;
    }

    public int getAllyCount() {
        return allyCount;
    }

    public void setAllyCount(int allyCount) {
        this.allyCount = allyCount;
    }

    public int getEnemyCount() {
        return enemyCount;
    }

    public void setEnemyCount(int enemyCount) {
        this.enemyCount = enemyCount;
    }
}
