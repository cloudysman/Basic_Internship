package caro;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Setter
@Getter
public class CaroModel {
    private final Map<String, Integer> transpositionTable = new HashMap<>();
    private final int maxDepth = 6;
    private final int winningLength = 5;
    private final int sizeMatrix = 12;
    private final int[][] a = new int[sizeMatrix + 1][sizeMatrix + 1];
    private final int[][] directions = {{0,-1}, {-1,0}, {-1,-1}, {-1,1}};
    private static CaroModel caroModel = null;
    private CaroModel() {
        this.readTranspositionTable();
    }
    public static CaroModel getInstance() {
        if(caroModel == null) {
            caroModel = new CaroModel();
        }
        return caroModel;
    }
    /**
      Transposition
     */
    private void readTranspositionTable() {
        try {

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    private boolean isValidMove(int x, int y) {
        return 1 <= x && x <= sizeMatrix && 1 <= y && y <= sizeMatrix;
    }
    public long evaluateMove(int x, int y) {
        long totalScore = 0;
        for(int i = 0; i < directions.length; i++) {
            // Priority cover
            totalScore += evaluateAttack(x, y, directions[i]);
            totalScore += evaluateCover(x, y, directions[i]) * 2;
        }
        return totalScore;
    }
    /**
     Calculate point status attack
     */
    private long evaluateAttack(int x, int y, int[] directionModel) {
        int countPointSame = 0;
        int countPointEmpty, countPointEmptyV1 = 0, countPointEmptyV2 = 0;

        int directionX = directionModel[0];
        int directionY = directionModel[1];

        int idx = -1;
        while(true) {
            int X = x + directionX * idx;
            int Y = y + directionY * idx;
            if(isValidMove(X, Y)) {
                if(a[X][Y] == a[x][y] && countPointEmptyV1 == 0) {
                    countPointSame++;
                }
                else {
                    while(isValidMove(X, Y) && a[X][Y] == 0) {
                        countPointEmptyV1++;
                        idx--;
                        X = x + directionX * idx;
                        Y = y + directionY * idx;
                    }
                    break;
                }
            }
            else {
                break;
            }
            idx--;
        }

        idx = +1;
        while(true) {
            int X = x + directionX * idx;
            int Y = y + directionY * idx;
            if(isValidMove(X, Y)) {
                if(a[X][Y] == a[x][y] && countPointEmptyV2 == 0) {
                    countPointSame++;
                }
                else {
                    while(isValidMove(X, Y) && a[X][Y] == 0) {
                        countPointEmptyV2++;
                        idx++;
                        X = x + directionX * idx;
                        Y = y + directionY * idx;
                    }
                    break;
                }
            }
            else {
                break;
            }
            idx++;
        }

        countPointEmpty = countPointEmptyV1 + countPointEmptyV2;
        if((countPointSame >= winningLength - 1)) {
            return a[x][y] == 1 ? Integer.MAX_VALUE: Integer.MIN_VALUE;
        }
        else if (countPointSame == (winningLength - 2) && countPointEmpty >= 2) {
            return a[x][y] == 1 ? 1000 : -1000;
        }
        else {
            if(countPointSame + countPointEmpty >= winningLength - 1) {
                return a[x][y] == 1 ? countPointSame : -countPointSame;
            }
            return 0;
        }
    }
    /**
     Calculate point status cover
     */
    private long evaluateCover(int x, int y, int[] directionModel) {
        int countPointOther, countPointOtherV1 = 0, countPointOtherV2 = 0;
        int countPointEmpty, countPointEmptyV1 = 0, countPointEmptyV2 = 0;

        int directionX = directionModel[0];
        int directionY = directionModel[1];

        int idx = -1;
        while(true) {
            int X = x + directionX * idx;
            int Y = y + directionY * idx;
            if(isValidMove(X, Y)) {
                if(a[X][Y] != a[x][y] && a[X][Y] != 0 && countPointEmptyV1 == 0) {
                    countPointOtherV1++;
                }
                else {
                    while(isValidMove(X, Y) && a[X][Y] == 0) {
                        countPointEmptyV1++;
                        idx--;
                        X = x + directionX * idx;
                        Y = y + directionY * idx;
                    }
                    break;
                }
            }
            else {
                break;
            }
            idx--;
        }

        idx = 1;
        while(true) {
            int X = x + directionX * idx,
                Y = y + directionY * idx;
            if(isValidMove(X, Y)) {
                if(a[X][Y] != a[x][y] && a[X][Y] != 0 && countPointEmptyV2 == 0) {
                    countPointOtherV2++;
                }
                else {
                    while(isValidMove(X, Y) && a[X][Y] == 0) {
                        countPointEmptyV2++;
                        idx++;
                        X = x + directionX * idx;
                        Y = y + directionY * idx;
                    }
                    break;
                }
            }
            else {
                break;
            }
            idx++;
        }

        countPointOther = countPointOtherV1 + countPointOtherV2;
        countPointEmpty = countPointEmptyV1 + countPointEmptyV2;
        if(countPointOther >= 4) {
            return a[x][y] == 1 ? Integer.MAX_VALUE: Integer.MIN_VALUE;
        }
    /*
        | O O O . .  -> OK
        . | O O O .  -> OK
        . . | O O O  -> NOT OK
    */
        if(countPointOtherV1 >= 3) {
            if(countPointEmptyV1 >= 2 || (countPointEmptyV1 >= 1 && countPointEmptyV2 >= 1))
                return a[x][y] == 1 ? 100 : -100;
            else
                return 1;
        }
        if(countPointOtherV2 >= 3) {
            if(countPointEmptyV2 >= 2 || (countPointEmptyV1 >= 1 && countPointEmptyV2 >= 1))
                return a[x][y] == 1 ? 100 : -100;
            else
                return 1;
        }
        else {
            if(countPointOther + countPointEmpty >= winningLength) {
                return a[x][y] == 1 ? countPointOther : -countPointOther;
            }
            return 0;
        }
    }
    /**
     Status game
     */
    private int checkStatusGame(int x, int y, int[] directionModel) {
        if(!isValidMove(x, y)) {
            return 0;
        }
        int countPoint = 1;
        for(int i = -1; true; i--) {
            int X = x + directionModel[0] * i;
            int Y = y + directionModel[1] * i;
            if(isValidMove(X,Y) && a[X][Y] == a[x][y]) {
               countPoint++;
            }
            else {
                break;
            }
        }
        for(int i = +1; true; i++) {
            int X = x + directionModel[0] * i;
            int Y = y + directionModel[1] * i;
            if(isValidMove(X,Y) && a[X][Y] == a[x][y]) {
                countPoint++;
            }
            else {
                break;
            }
        }
        if(countPoint == 5) {
            return a[x][y] == 1 ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        }
        return 0;
    }
    public int checkStatusGame(int x,int y) {
        for(int i = 0; i < directions.length; i++) {
            int score = checkStatusGame(x, y, directions[i]);
            if(score == Integer.MIN_VALUE || score == Integer.MAX_VALUE)
                return score;
        }
        return 0;
    }
    private long alphaBeta(boolean isBot, int depth, long alpha, long beta, int preX,int preY) {
        int scoreEvaluate = checkStatusGame(preX, preY);
        if(scoreEvaluate == Integer.MIN_VALUE || scoreEvaluate == Integer.MAX_VALUE || depth >= maxDepth) {
            return scoreEvaluate;
        }
        long bestScore = isBot ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        for(int i = 1; i <= sizeMatrix; i++) {
            for(int j = 1; j <= sizeMatrix; j++) {
                if (a[i][j] == 0) {
                    a[i][j] = isBot ? 1 : -1;
                    long score = alphaBeta(!isBot, depth + 1, alpha, beta, i, j);
                    a[i][j] = 0;
                    if(isBot) {
                        bestScore = Math.max(score , bestScore);
                        alpha = Math.max(alpha, bestScore);
                    }
                    else {
                        bestScore = Math.min(score, bestScore);
                        beta = Math.min(beta, bestScore);
                    }
                    if(beta <= alpha) {
                        return bestScore;
                    }
                }
            }
        }
        return bestScore;
    }
    public int[] findBestMove() {
        long bestScore = Integer.MIN_VALUE;
        long alpha = Integer.MIN_VALUE;
        int x = -1, y = -1;
        for(int i = 1; i <= sizeMatrix; i++) {
            for(int j = 1; j <= sizeMatrix; j++) {
                if(a[i][j] == 0) {
                    a[i][j] = 1;
                    long scoreMove = evaluateMove(i, j);
                    long score = alphaBeta(false, 2, alpha, Integer.MAX_VALUE, i, j);
                    a[i][j] = 0;
                    if(bestScore < score + scoreMove) {
                        bestScore = score + scoreMove;
                        x = i;
                        y = j;
                    }
                    alpha = Math.max(alpha, bestScore);
                }
            }
        }
        return new int[]{x, y};
    }
    public void botMove(int x, int y) {
        if(isValidMove(x,y)) {
            a[x][y] = 1;
        }
    }
    public void playerMove(int x, int y) {
        if(isValidMove(x,y)) {
            a[x][y] = -1;
        }
    }
    public void restartGame() {
        for(int i = 1; i <= sizeMatrix; i++) {
            for(int j = 1; j <= sizeMatrix; j++) {
                a[i][j] = 0;
            }
        }
    }
}
