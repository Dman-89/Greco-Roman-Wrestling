package com.example.android.greco_romanwrestling;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    int zeroPointsFlag = 0;
    int roundEndEnableFlag = 0;
    int blockFlag = 0;
    int roundNumber = 0;
    int pointsBlue = 0;
    int pointsRed = 0;
    int admonitionsBlue = 0;
    int admonitionsRed = 0;
    int threePointsCounterBlue = 0;
    int threePointsCounterRed = 0;
    int roundsWonBlue = 0;
    int roundsWonRed = 0;
    int lastScoreBlue = 0;
    int lastScoreRed = 0;

    private Integer[] roundIdsBlue = {R.id.R1Left, R.id.R2Left, R.id.R3Left};
    private Integer[] roundIdsRed = {R.id.R1Right, R.id.R2Right, R.id.R3Right};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

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

        displayNumber(pointsBlue, (TextView) findViewById(R.id.pointsCountBlue));
        displayNumber(pointsRed, (TextView) findViewById(R.id.pointsCountRed));
        displayNumber(admonitionsBlue, (TextView) findViewById(R.id.admonitionsCountBlue));
        displayNumber(admonitionsRed, (TextView) findViewById(R.id.admonitionsCountRed));

        for (int i=0; i<3; i++){
            findViewById(roundIdsBlue[i]).setBackgroundResource(R.drawable.rounded_corners_white);
            findViewById(roundIdsRed[i]).setBackgroundResource(R.drawable.rounded_corners_white);
        }
    }

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

            pointsBlue = 0;
            pointsRed = 0;
            threePointsCounterBlue = 0;
            threePointsCounterRed = 0;
            lastScoreBlue = 0;
            lastScoreRed = 0;
            displayNumber(pointsBlue, (TextView) findViewById(R.id.pointsCountBlue));
            displayNumber(pointsRed, (TextView) findViewById(R.id.pointsCountRed));

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

    private void displayNumber(int number, TextView textview) {
        textview.setText(String.valueOf(number));
    }

    private void roundColorChange (TextView textview) {
        textview.setBackgroundResource(R.drawable.rounded_corners_green);
    }

    private void contestationToaster (CharSequence wrestlerColor) {
        int toastContestationDuration = Toast.LENGTH_LONG;
        Context context = getApplicationContext();
        CharSequence text = wrestlerColor + " wrestler wins contestation";
        Toast toast = Toast.makeText(context, text, toastContestationDuration);
        toast.show();
    }

    public void addFiveForBlue(View view) {
        roundEndEnableFlag = 1;
        if (blockFlag == 1) {
            return;
        }
        blockFlag = 1;
        pointsBlue += 5;
        displayNumber(pointsBlue, (TextView) findViewById(R.id.pointsCountBlue));
    }

    public void addFiveForRed(View view) {
        roundEndEnableFlag = 1;
        if (blockFlag == 1) {
            return;
        }
        blockFlag = 1;
        pointsRed += 5;
        displayNumber(pointsRed, (TextView) findViewById(R.id.pointsCountRed));
    }

    public void addThreeForBlue(View view) {
        roundEndEnableFlag = 1;
        if (blockFlag == 1) {
            return;
        }
        pointsBlue += 3;
        lastScoreBlue = 1;
        lastScoreRed = 0;
        threePointsCounterBlue += 1;
        displayNumber(pointsBlue, (TextView) findViewById(R.id.pointsCountBlue));
        if (threePointsCounterBlue == 2 | zeroPointsFlag == 1) {
            blockFlag = 1;
        }
    }

    public void addThreeForRed(View view) {
        roundEndEnableFlag = 1;
        if (blockFlag == 1) {
            return;
        }
        pointsRed += 3;
        lastScoreRed = 1;
        lastScoreBlue = 0;
        threePointsCounterRed += 1;
        displayNumber(pointsRed, (TextView) findViewById(R.id.pointsCountRed));
        if (threePointsCounterRed == 2 | zeroPointsFlag == 1) {
            blockFlag = 1;
        }
    }

    public void addTwoForBlue(View view) {
        roundEndEnableFlag = 1;
        if (blockFlag == 1) {
            return;
        }
        pointsBlue += 2;
        lastScoreBlue = 1;
        lastScoreRed = 0;
        displayNumber(pointsBlue, (TextView) findViewById(R.id.pointsCountBlue));
        if (zeroPointsFlag == 1) {
            blockFlag = 1;
        }
    }

    public void addTwoForRed(View view) {
        roundEndEnableFlag = 1;
        if (blockFlag == 1) {
            return;
        }
        pointsRed += 2;
        lastScoreRed = 1;
        lastScoreBlue = 0;
        displayNumber(pointsRed, (TextView) findViewById(R.id.pointsCountRed));
        if (zeroPointsFlag == 1) {
            blockFlag = 1;
        }
    }

    public void addOneForBlue(View view) {
        roundEndEnableFlag = 1;
        if (blockFlag == 1) {
            return;
        }
        pointsBlue += 1;
        lastScoreBlue = 1;
        lastScoreRed = 0;
        displayNumber(pointsBlue, (TextView) findViewById(R.id.pointsCountBlue));
        if (zeroPointsFlag == 1) {
            blockFlag = 1;
        }
    }

    public void addOneForRed(View view) {
        roundEndEnableFlag = 1;
        if (blockFlag == 1) {
            return;
        }
        pointsRed += 1;
        lastScoreRed = 1;
        lastScoreBlue = 0;
        displayNumber(pointsRed, (TextView) findViewById(R.id.pointsCountRed));
        if (zeroPointsFlag == 1) {
            blockFlag = 1;
        }
    }

    public void admonitionBlue (View view) {
        roundEndEnableFlag = 1;
        if (blockFlag == 1) {
            return;
        }
        admonitionsBlue += 1;
        displayNumber(admonitionsBlue, (TextView) findViewById(R.id.admonitionsCountBlue));
        if (admonitionsBlue == 3) {
            contestationToaster("Red");
            blockFlag = 1;
        }

    }

    public void admonitionRed (View view) {
        roundEndEnableFlag = 1;
        if (blockFlag == 1) {
            return;
        }
        admonitionsRed += 1;
        displayNumber(admonitionsRed, (TextView) findViewById(R.id.admonitionsCountRed));
        if (admonitionsRed == 3) {
            contestationToaster("Blue");
            blockFlag = 1;
        }
    }
}
