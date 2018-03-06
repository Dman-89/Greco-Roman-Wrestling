package com.example.android.greco_romanwrestling;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private int zeroPointsFlag = 0; // if round ends with zero points for every wrestler,
                            // it blocks roundEnd for contestants to wrestle additional time
    private int roundEndEnableFlag = 0; // flag for "turning on" roundEnd function, needed to skip monkey clicks
    private int blockFlag = 0; // flag for all functions except roundEnd blocking, needed to skip monkey clicks
    private int roundNumber = 0;
    private int pointsBlue = 0; // round score for blue wrestler
    private int pointsRed = 0; // round score for red wrestler
    private int admonitionsBlue = 0; // contestation (not round!) admonitions for blue wrestler
    private int admonitionsRed = 0; // contestation (not round!) admonitions for red wrestler
    private int threePointsCounterBlue = 0; // if wrestler gets 2 times 3 points he wins round despite scores
    private int threePointsCounterRed = 0; // so it's counter of 3 scores times in round
    private int roundsWonBlue = 0;
    private int roundsWonRed = 0;
    private int lastScoreBlue = 0; // if score in round and admonitions are equal, winner is the one who got
    private int lastScoreRed = 0; // last point(s) in round

    // arrays needed to reset layout
    private Integer[] roundIdsBlue = {R.id.r1_left, R.id.r2_left, R.id.r3_left};
    private Integer[] roundIdsRed = {R.id.r1_right, R.id.r2_right, R.id.r3_right};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // resets everything - starts new contestation
    public void resetContestation(View view) {
        zeroPointsFlag = 0;
        roundEndEnableFlag = 0;
        blockFlag = 0;
        roundNumber = 0;
        pointsBlue = 0;
        pointsRed = 0;
        admonitionsBlue = 0;
        admonitionsRed = 0;
        threePointsCounterBlue = 0;
        threePointsCounterRed = 0;
        roundsWonBlue = 0;
        roundsWonRed = 0;
        lastScoreBlue = 0;
        lastScoreRed = 0;

        displayNumber(pointsBlue, (TextView) findViewById(R.id.points_count_blue));
        displayNumber(pointsRed, (TextView) findViewById(R.id.points_count_red));
        displayNumber(admonitionsBlue, (TextView) findViewById(R.id.admonitions_count_blue));
        displayNumber(admonitionsRed, (TextView) findViewById(R.id.admonitions_count_red));

        for (int i=0; i<3; i++){
            findViewById(roundIdsBlue[i]).setBackgroundResource(R.drawable.rounded_corners_white);
            findViewById(roundIdsRed[i]).setBackgroundResource(R.drawable.rounded_corners_white);
        }
    }

    // identifies round winner, resets variables that are targeted to track round (not contestation!) progress
    public void RoundEnd(View view) {
        if (roundEndEnableFlag == 0) {
            return;
        }
        if (admonitionsBlue == admonitionsRed & pointsBlue == 0 & pointsRed == 0) {
            zeroPointsFlag = 1;
            return;
        }
        roundNumber += 1;
        if (!(roundsWonBlue == 2 | roundsWonRed == 2 | roundNumber > 3 | admonitionsBlue == 3 | admonitionsRed == 3)) {

            blockFlag = 0;
            // conditions for proper identification of square to lighten in green as round winner
            if (pointsBlue > pointsRed) {
                roundColorChange((TextView) findViewById(roundIdsBlue[roundNumber - 1]));
                roundsWonBlue += 1;
            } else if (pointsBlue < pointsRed) {
                roundColorChange((TextView) findViewById(roundIdsRed[roundNumber - 1]));
                roundsWonRed += 1;
            } else if (admonitionsBlue > admonitionsRed) {
            roundColorChange((TextView) findViewById(roundIdsRed[roundNumber - 1]));
            roundsWonRed += 1;
            } else if (admonitionsBlue < admonitionsRed) {
            roundColorChange((TextView) findViewById(roundIdsBlue[roundNumber - 1]));
            roundsWonBlue += 1;
            } else if (lastScoreBlue == 1) {
                roundColorChange((TextView) findViewById(roundIdsBlue[roundNumber - 1]));
                roundsWonBlue += 1;
            } else if (lastScoreRed == 1) {
                roundColorChange((TextView) findViewById(roundIdsRed[roundNumber - 1]));
                roundsWonRed += 1;
            }
            // conditions above end
            pointsBlue = 0;
            pointsRed = 0;
            threePointsCounterBlue = 0;
            threePointsCounterRed = 0;
            lastScoreBlue = 0;
            lastScoreRed = 0;
            displayNumber(pointsBlue, (TextView) findViewById(R.id.points_count_blue));
            displayNumber(pointsRed, (TextView) findViewById(R.id.points_count_red));

            if (roundsWonBlue == 2 | roundsWonRed == 2 | admonitionsBlue == 3 | admonitionsRed == 3) {
                blockFlag = 1;
                if (roundsWonBlue == 2 | admonitionsRed == 3) {
                    contestationToaster ("Blue");
                } else if (roundsWonRed == 2 | admonitionsBlue == 3) {
                    contestationToaster ("Red");
                }
            }

        } else {
            blockFlag = 1;
            if (roundsWonBlue == 2 | admonitionsRed == 3) {
                contestationToaster ("Blue");
            } else if (roundsWonRed == 2 | admonitionsBlue == 3) {
                contestationToaster ("Red");
            }
        }
    }

    // changes every counter visible value
    private void displayNumber(int number, TextView textview) {
        textview.setText(String.valueOf(number));
    }

    // lightens square on top of round winner half
    private void roundColorChange (TextView textview) {
        textview.setBackgroundResource(R.drawable.rounded_corners_green);
    }

    // explicitly tells who's the winner of contestation
    private void contestationToaster (CharSequence wrestlerColor) {
        int toastContestationDuration = Toast.LENGTH_LONG;
        Context context = getApplicationContext();
        CharSequence text = wrestlerColor + " wrestler wins contestation";
        Toast toast = Toast.makeText(context, text, toastContestationDuration);
        toast.show();
    }

    // adds 5 points to score of BLUE contestant, auto-win round
    public void addFiveForBlue(View view) {
        roundEndEnableFlag = 1;
        if (blockFlag == 1) {
            return;
        }
        blockFlag = 1;
        pointsBlue += 5;
        displayNumber(pointsBlue, (TextView) findViewById(R.id.points_count_blue));
    }

    // adds 5 points to score of RED contestant, auto-win round
    public void addFiveForRed(View view) {
        roundEndEnableFlag = 1;
        if (blockFlag == 1) {
            return;
        }
        blockFlag = 1;
        pointsRed += 5;
        displayNumber(pointsRed, (TextView) findViewById(R.id.points_count_red));
    }

    // adds 3 points to score of BLUE contestant, auto-win round if he gets 3 points 2 times
    public void addThreeForBlue(View view) {
        roundEndEnableFlag = 1;
        if (blockFlag == 1) {
            return;
        }
        pointsBlue += 3;
        lastScoreBlue = 1;
        lastScoreRed = 0;
        threePointsCounterBlue += 1;
        displayNumber(pointsBlue, (TextView) findViewById(R.id.points_count_blue));
        if (threePointsCounterBlue == 2 | zeroPointsFlag == 1) {
            blockFlag = 1;
        }
    }

    // adds 3 points to score of RED contestant, auto-win round if he gets 3 points 2 times
    public void addThreeForRed(View view) {
        roundEndEnableFlag = 1;
        if (blockFlag == 1) {
            return;
        }
        pointsRed += 3;
        lastScoreRed = 1;
        lastScoreBlue = 0;
        threePointsCounterRed += 1;
        displayNumber(pointsRed, (TextView) findViewById(R.id.points_count_red));
        if (threePointsCounterRed == 2 | zeroPointsFlag == 1) {
            blockFlag = 1;
        }
    }

    // adds 2 points to score of BLUE contestant
    public void addTwoForBlue(View view) {
        roundEndEnableFlag = 1;
        if (blockFlag == 1) {
            return;
        }
        pointsBlue += 2;
        lastScoreBlue = 1;
        lastScoreRed = 0;
        displayNumber(pointsBlue, (TextView) findViewById(R.id.points_count_blue));
        if (zeroPointsFlag == 1) {
            blockFlag = 1;
        }
    }

    // adds 2 points to score of RED contestant
    public void addTwoForRed(View view) {
        roundEndEnableFlag = 1;
        if (blockFlag == 1) {
            return;
        }
        pointsRed += 2;
        lastScoreRed = 1;
        lastScoreBlue = 0;
        displayNumber(pointsRed, (TextView) findViewById(R.id.points_count_red));
        if (zeroPointsFlag == 1) {
            blockFlag = 1;
        }
    }

    // adds 1 point to score of BLUE contestant
    public void addOneForBlue(View view) {
        roundEndEnableFlag = 1;
        if (blockFlag == 1) {
            return;
        }
        pointsBlue += 1;
        lastScoreBlue = 1;
        lastScoreRed = 0;
        displayNumber(pointsBlue, (TextView) findViewById(R.id.points_count_blue));
        if (zeroPointsFlag == 1) {
            blockFlag = 1;
        }
    }

    // adds 1 point to score of RED contestant
    public void addOneForRed(View view) {
        roundEndEnableFlag = 1;
        if (blockFlag == 1) {
            return;
        }
        pointsRed += 1;
        lastScoreRed = 1;
        lastScoreBlue = 0;
        displayNumber(pointsRed, (TextView) findViewById(R.id.points_count_red));
        if (zeroPointsFlag == 1) {
            blockFlag = 1;
        }
    }

    // adds 1 admonition to admonitions counter of BLUE contestant
    public void admonitionBlue (View view) {
        roundEndEnableFlag = 1;
        if (blockFlag == 1) {
            return;
        }
        admonitionsBlue += 1;
        displayNumber(admonitionsBlue, (TextView) findViewById(R.id.admonitions_count_blue));
        if (admonitionsBlue == 3) {
            contestationToaster("Red");
            roundColorChange((TextView) findViewById(roundIdsRed[roundNumber]));
            blockFlag = 1;
        }

    }

    // adds 1 admonition to admonitions counter of RED contestant
    public void admonitionRed (View view) {
        roundEndEnableFlag = 1;
        if (blockFlag == 1) {
            return;
        }
        admonitionsRed += 1;
        displayNumber(admonitionsRed, (TextView) findViewById(R.id.admonitions_count_red));
        if (admonitionsRed == 3) {
            contestationToaster("Blue");
            roundColorChange((TextView) findViewById(roundIdsBlue[roundNumber]));
            blockFlag = 1;
        }
    }
}
