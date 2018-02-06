/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.ghost;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;


public class GhostActivity extends AppCompatActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();
    String wordFragment="",currentWord="";
    TextView text,label;
    


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);
//        AssetManager assetManager = getAssets();
        try{
            InputStream dictionaryWords=getAssets().open("words.txt");
            dictionary=new SimpleDictionary(dictionaryWords);
        }
        catch(IOException e){
            e.printStackTrace();
        }
        text=(TextView)findViewById(R.id.ghostText);
        label=(TextView) findViewById(R.id.gameStatus);
        onStart(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        text.setText("");
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }

    private void computerTurn() {
        //TextView label = (TextView) findViewById(R.id.gameStatus);
        // Do computer turn stuff then make it the user's turn again
        //wordFragment=text.getText().toString();
        Log.e("Word Fragment", wordFragment);
        if(wordFragment.length()>=4 && dictionary.isWord((wordFragment)))
        {
            label.setText("Computer Win !! :)");
            Log.e("Word in Dict","Comp win");

        }
        else
        {
            String wordFromDictionary;
            //wordFromDictionary= dictionary.getAnyWordStartingWith(wordFragment);
            wordFromDictionary=dictionary.getGoodWordStartingWith(wordFragment);
            if(wordFromDictionary!=null) {
                wordFragment = wordFromDictionary.substring(0, wordFragment.length() + 1);
                text.setText(wordFragment);
                currentWord = wordFragment;
                userTurn = true;
            }
            else
            {
                label.setText("No word can be formed from this prefix");
                text.setText("Match Draw...");
                //userTurn = true;
            }
        }

        //userTurn = true;
        //label.setText(USER_TURN);
    }

    /**
     * Handler for user key presses.
     * @param keyCode
     * @param event
     * @return whether the key stroke was handled.
     */
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        /**
         **
         **  YOUR CODE GOES HERE
         **
         **/
        if(keyCode>=29 && keyCode<=54)
        {
            char c=(char) event.getUnicodeChar();
            wordFragment=(String) text.getText();
            wordFragment=currentWord + Character.toString(c);
            text.setText(wordFragment);
            computerTurn();
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    //challenge is only pressed by user, not computerTurn()
    public void challenge(View view) {
        currentWord=text.getText().toString();
        if(dictionary.isWord(currentWord))
        {
            if(userTurn)
                label.setText("user win");
            else
                label.setText("comp win!!");
        }

        else
        {
            label.setText("compwin!!");
        }
    }

    public void reset(View view) {
        userTurn=random.nextBoolean();
        text.setText("");
        wordFragment="";
        currentWord="";

        if(userTurn)
        {
              label.setText(USER_TURN);
        }
        else
        {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
    }
}
