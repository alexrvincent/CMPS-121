package alexrvincent.tictactoe;

import android.app.Application;
import android.content.Context;

/**
 * Created by Alex on 1/13/2016.
 */
public class GameState {

    private int[] gameState;
    private int tiles = 9;
    private int turn;
    private boolean gameOver;
    private boolean tie;
    private String winSet;
    private Context gameContext;

    /* Constructor */
    public GameState(Context context){

        /* Default Member Fields */
        gameContext = context;
        turn = 0;
        gameOver = false;
        tie = false;

        /* Set up a new tiled board and clear it with 0s */
        gameState = new int[tiles];
        for (int i = 0; i < tiles; ++i){ gameState[i] = 0; }
    }

    /* Resets all game logic variables and board */
    public void newGame() {
        turn = 0;
        gameOver = false;
        tie = false;

        for (int i = 0; i < tiles; ++i) { gameState[i] = 0; }
    }

    /* Checks the current array-board for a win-combination,
       updates GameState object if it finds one */
    public void checkState(){

        /* Winning sets:
        Row win: {0, 1, 2}, {3,4,5}, {6,7,8}
        Column win: {0,3,6} {1,4,7}, {2,5,8}
        Diagonal Win: {0, 4, 8}, {2,4,6}
        */

        /* Row Victory */
        if (  (gameState[0] == gameState[1]) && (gameState[1]) == gameState[2] && gameState[0] != 0 ){
            gameOver = true;
            winSet = "012";
            return;
        }
        if (  (gameState[3] == gameState[4]) && (gameState[4]) == gameState[5] && gameState[3] != 0 ){
            gameOver = true;
            winSet = "345";
            return;
        }
        if (  (gameState[6] == gameState[7]) && (gameState[7]) == gameState[8] && gameState[6] != 0 ){
            gameOver = true;
            winSet = "678";
            return;
        }

        /* Column Victory */
        if (  (gameState[0] == gameState[3]) && (gameState[3]) == gameState[6] && gameState[0] != 0 ){
            gameOver = true;
            winSet = "036";
            return;
        }
        if (  (gameState[1] == gameState[4]) && (gameState[4]) == gameState[7] && gameState[1] != 0 ){
            gameOver = true;
            winSet = "147";
            return;
        }
        if (  (gameState[2] == gameState[5]) && (gameState[5]) == gameState[8] && gameState[2] != 0 ){
            gameOver = true;
            winSet = "258";
            return;
        }

        /* Diagonal Victory */
        if (  (gameState[0] == gameState[4]) && (gameState[4]) == gameState[8] && gameState[0] != 0 ){
            gameOver = true;
            winSet = "048";
            return;
        }
        if (  (gameState[2] == gameState[4]) && (gameState[4]) == gameState[6] && gameState[2] != 0 ){
            gameOver = true;
            winSet = "246";
            return;
        }

        if (turn > 8) {
            gameOver = true;
            tie = true;
        }
    }

    /* Ticks an empty array tile at a given position */
    public void markBoard(int position, int icon){
        /* Check if the position is empty (contains 0) */
        if(gameState[position] == 0){
            /* Otherwise Write In A Cross (1) */
            if (icon == 1) { gameState[position] = icon; }
            /* Otherwise Write In a Circle (2) */
            else if(icon == 2){ gameState[position] = icon;
            }
        }
    }


    /* Accessors */
    public int getTurn(){ return turn; }

    public int getBoardPosition(int position){ return gameState[position]; }

    public boolean getGameStatus() { return gameOver; }

    public boolean getTie() { return tie; }

    public String getWinSet() {return winSet; }


    /* Mutators */
    public void updateTurn(){ turn++; }

}
