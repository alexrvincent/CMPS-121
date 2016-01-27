package alexrvincent.tictactoe;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    /* Game Board (3x3 Matrix)
     | b0 | b1 | b2 |
     | b3 | b4 | b5 |
     | b6 | b7 | b8 |
    */

    /* Handle/References to ImageButton/Tiles on the game board */
    ImageButton b0;
    ImageButton b1;
    ImageButton b2;
    ImageButton b3;
    ImageButton b4;
    ImageButton b5;
    ImageButton b6;
    ImageButton b7;
    ImageButton b8;

    TextView t;
    Button ng;

    GameState game;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Grab a handle to each of the grid tiles on the board */
        b0 = (ImageButton) findViewById(R.id.tile0);
        b1 = (ImageButton) findViewById(R.id.tile1);
        b2 = (ImageButton) findViewById(R.id.tile2);
        b3 = (ImageButton) findViewById(R.id.tile3);
        b4 = (ImageButton) findViewById(R.id.tile4);
        b5 = (ImageButton) findViewById(R.id.tile5);
        b6 = (ImageButton) findViewById(R.id.tile6);
        b7 = (ImageButton) findViewById(R.id.tile7);
        b8 = (ImageButton) findViewById(R.id.tile8);

        /* Reset default button colors */
        resetButtonColors();

        /* Assign the handle to the text and the new game button */
        t = (TextView) findViewById(R.id.gameText);
        t.setText("Cross goes first!");
        ng = (Button) findViewById(R.id.newGame);

        /*Instantiate a new GameState object and clear it*/
        game = new GameState(this);
    }

    /* Generic click-handling for any tile on the board */
    public void tileClick(View v){
        /*As long as the game isn't over, update the game logic and it's graphics */
        if(!game.getGameStatus()) {
            /* Switch on the caught-tags of the individual buttons */
            switch (v.getTag().toString()) {
                case "b0":
                    if (game.getBoardPosition(0) == 0) updateDisplay(b0, 0);
                    break;
                case "b1":
                    if (game.getBoardPosition(1) == 0) updateDisplay(b1, 1);
                    break;
                case "b2":
                    if (game.getBoardPosition(2) == 0) updateDisplay(b2, 2);
                    break;
                case "b3":
                    if (game.getBoardPosition(3) == 0) updateDisplay(b3, 3);
                    break;
                case "b4":
                    if (game.getBoardPosition(4) == 0) updateDisplay(b4, 4);
                    break;
                case "b5":
                    if (game.getBoardPosition(5) == 0) updateDisplay(b5, 5);
                    break;
                case "b6":
                    if (game.getBoardPosition(6) == 0) updateDisplay(b6, 6);
                    break;
                case "b7":
                    if (game.getBoardPosition(7) == 0) updateDisplay(b7, 7);
                    break;
                case "b8":
                    if (game.getBoardPosition(8) == 0) updateDisplay(b8, 8);
                    break;
                default:
                    Toast.makeText(MainActivity.this, "Error: Please start a new game.", Toast.LENGTH_SHORT).show();
                    break;
            }

            /* Check the game logic for a game over */
            game.checkState();

            /* If there was a winner, or a tie, assign it */
            if (game.getGameStatus()) {
                if(game.getTie()){
                    Toast.makeText(MainActivity.this, "It's a tie!", Toast.LENGTH_SHORT).show();
                    t.setText("It's a tie!");
                }
                else if(game.getTurn() % 2 == 0) {
                    Toast.makeText(MainActivity.this, "Circle Wins!", Toast.LENGTH_SHORT).show();
                    t.setText(("Circle Wins!"));
                    highlightWinner(game.getWinSet());
                }
                else {
                    Toast.makeText(MainActivity.this, "Cross Wins!", Toast.LENGTH_SHORT).show();
                    t.setText("Cross Wins!");
                    highlightWinner(game.getWinSet());
                }
            }
        }
        /* Otherwise the game is over - prompt the user to make a new game */
        else {
            Toast.makeText(MainActivity.this, "Game Over. Please start a new game.", Toast.LENGTH_SHORT).show();
        }

    }

    /* Colors the winning set of tiles at the end of the game */
    public void highlightWinner(String winSet){
        //Recieve winSet string from GameState object
        switch(winSet){
            case "":
                break;
            case "012":
                b0.setBackgroundColor(getResources().getColor(R.color.highlightGreen));
                b1.setBackgroundColor(getResources().getColor(R.color.highlightGreen));
                b2.setBackgroundColor(getResources().getColor(R.color.highlightGreen));
                break;
            case "345":
                b3.setBackgroundColor(getResources().getColor(R.color.highlightGreen));
                b4.setBackgroundColor(getResources().getColor(R.color.highlightGreen));
                b5.setBackgroundColor(getResources().getColor(R.color.highlightGreen));
                break;
            case "678":
                b6.setBackgroundColor(getResources().getColor(R.color.highlightGreen));
                b7.setBackgroundColor(getResources().getColor(R.color.highlightGreen));
                b8.setBackgroundColor(getResources().getColor(R.color.highlightGreen));
                break;
            case "036":
                b0.setBackgroundColor(getResources().getColor(R.color.highlightGreen));
                b3.setBackgroundColor(getResources().getColor(R.color.highlightGreen));
                b6.setBackgroundColor(getResources().getColor(R.color.highlightGreen));
                break;
            case "147":
                b1.setBackgroundColor(getResources().getColor(R.color.highlightGreen));
                b4.setBackgroundColor(getResources().getColor(R.color.highlightGreen));
                b7.setBackgroundColor(getResources().getColor(R.color.highlightGreen));
                break;
            case "258":
                b2.setBackgroundColor(getResources().getColor(R.color.highlightGreen));
                b5.setBackgroundColor(getResources().getColor(R.color.highlightGreen));
                b8.setBackgroundColor(getResources().getColor(R.color.highlightGreen));
                break;
            case "048":
                b0.setBackgroundColor(getResources().getColor(R.color.highlightGreen));
                b4.setBackgroundColor(getResources().getColor(R.color.highlightGreen));
                b8.setBackgroundColor(getResources().getColor(R.color.highlightGreen));
                break;
            case "246":
                b2.setBackgroundColor(getResources().getColor(R.color.highlightGreen));
                b4.setBackgroundColor(getResources().getColor(R.color.highlightGreen));
                b6.setBackgroundColor(getResources().getColor(R.color.highlightGreen));
                break;
            default:
                break;
        }
    }

    /* Resets the game graphics and game logic */
    public void newGameClick(View v){

            /* Clear all current icons */
        b0.setImageResource(android.R.color.transparent);
        b1.setImageResource(android.R.color.transparent);
        b2.setImageResource(android.R.color.transparent);
        b3.setImageResource(android.R.color.transparent);
        b4.setImageResource(android.R.color.transparent);
        b5.setImageResource(android.R.color.transparent);
        b6.setImageResource(android.R.color.transparent);
        b7.setImageResource(android.R.color.transparent);
        b8.setImageResource(android.R.color.transparent);

            /*Reset all colors*/
        resetButtonColors();

            /*Reset game text*/
        t.setText("Cross goes first!");

            /*Reset game logic*/
        game.newGame();

    }

    /* Updates game logic and displays graphical changes */
    public void updateDisplay(ImageButton clicked, int position){

        /* Get the current turn and then increment it*/
        int turn = game.getTurn();
        game.updateTurn();

        /* Display either circle or cross depending on the turn number*/
        /* Cross goes on odd turns, Circles go on even turns */
        if(turn % 2 == 0) {
            clicked.setImageResource(R.drawable.ic_cross);
            game.markBoard(position, 1);
            t.setText("Circle goes!");
        }
        else {
            clicked.setImageResource(R.drawable.ic_circle);
            game.markBoard(position, 2);
            t.setText("Cross goes!");
        }


    }

    /* Sub-routine: Resets tile colors to specified default value */
    public void resetButtonColors(){
        b0.setBackgroundColor(getResources().getColor(R.color.defaultButtonColor));
        b1.setBackgroundColor(getResources().getColor(R.color.defaultButtonColor));
        b2.setBackgroundColor(getResources().getColor(R.color.defaultButtonColor));
        b3.setBackgroundColor(getResources().getColor(R.color.defaultButtonColor));
        b4.setBackgroundColor(getResources().getColor(R.color.defaultButtonColor));
        b5.setBackgroundColor(getResources().getColor(R.color.defaultButtonColor));
        b6.setBackgroundColor(getResources().getColor(R.color.defaultButtonColor));
        b7.setBackgroundColor(getResources().getColor(R.color.defaultButtonColor));
        b8.setBackgroundColor(getResources().getColor(R.color.defaultButtonColor));
    }
}
