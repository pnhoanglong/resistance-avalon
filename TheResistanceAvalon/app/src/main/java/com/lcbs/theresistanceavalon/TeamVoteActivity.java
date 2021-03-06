package com.lcbs.theresistanceavalon;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;


public class TeamVoteActivity extends ActionBarActivity {

    TextView proposedTeamTextView;
    private String[] selectedPlayers;
    private Player[] players;
    private String team;
    private int TEAM_SIZE;
    private int NUM_PLAYERS;
    LinearLayout containerLayout;

    CheckBox c1;
    CheckBox c2;
    CheckBox c3;
    CheckBox c4;
    CheckBox c5;
    CheckBox c6;
    CheckBox c7;
    CheckBox c8;
    CheckBox c9;
    CheckBox c10;
    CheckBox[] cCheckBoxes = {c1,c2,c3,c4,c5,c6,c7,c8,c9,c10};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team_vote);
        TEAM_SIZE = GameState.getInstance().getNumPlayersThisRound();
        containerLayout = (LinearLayout)findViewById(R.id.linear3);
        players = GameState.getInstance().getPlayers();
        NUM_PLAYERS = GameState.getInstance().getNumPlayers();
        selectedPlayers = GameState.getInstance().getTeamThisRound();
        team = "";
        for (int i = 0; i < TEAM_SIZE; i++) {
            team += selectedPlayers[i] + "   ";
        }
        proposedTeamTextView = (TextView) findViewById(R.id.team_textview);
        proposedTeamTextView.setText(team);
        listPlayersToVote();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_team_vote, menu);
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

    public void listPlayersToVote() {

        // and either do an alert and decide

        for (int i = 0; i < NUM_PLAYERS; i++) {
            cCheckBoxes[i] = new CheckBox(this);
            containerLayout.addView(cCheckBoxes[i]);
            cCheckBoxes[i].setGravity(Gravity.CENTER_HORIZONTAL);
            cCheckBoxes[i].setText("   " + players[i].getName());
            cCheckBoxes[i].setTextColor(Color.parseColor("#c5a495"));
            cCheckBoxes[i].setId(i);

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) cCheckBoxes[i].getLayoutParams();
            layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
            layoutParams.setMargins(250, 30, 0, 0);
        }

        final Button submitButton = new Button(this);
        submitButton.setText("Vote");
        submitButton.setBackgroundColor(Color.parseColor("#f3d28f"));
        submitButton.setTextColor(Color.parseColor("#433022"));
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(250,20,250,0); // left, top, right, bottom
        submitButton.setGravity(Gravity.CENTER_HORIZONTAL);
        containerLayout.addView(submitButton, lp);
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                checkVote();
            }
        });
    }

    public void checkVote() {
        int passed = 0; // people who voted to pass
        for (int i = 0; i < NUM_PLAYERS; i++) {
            final CheckBox checkBox = (CheckBox) findViewById(i);
            if (checkBox.isChecked()) {
                passed++;
            }
        }

        if (passed > (NUM_PLAYERS/2)) { // vote passed
            GameState.getInstance().setTeamThisRound(selectedPlayers);
            openGood(proposedTeamTextView);
        } else { // vote failed
            GameState.getInstance().newRejectedRound();
            openFail(proposedTeamTextView);
        }

    }

    public void openFail(View view){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(R.string.failed_message);
        alertDialogBuilder.setTitle(R.string.failed_title);
        alertDialogBuilder.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        if (GameState.getInstance().gameOver()) {
                            Intent intent = new Intent(getApplicationContext(), com.lcbs.theresistanceavalon.GameOverActivity.class);
                            startActivity(intent);
                        } else {
                            Intent intent2 = new Intent(getApplicationContext(), com.lcbs.theresistanceavalon.AssembleTeamActivity.class);
                            startActivity(intent2);
                        }
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void openGood(View view){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(R.string.passed_message);
        alertDialogBuilder.setTitle(R.string.passed_title);
        alertDialogBuilder.setPositiveButton(R.string.ok,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(getApplicationContext(), com.lcbs.theresistanceavalon.QuestVoteActivity.class);
                        startActivity(intent);
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}
