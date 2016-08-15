package de.spdns.safhome.optimalrunningspeed;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private double CalculatePace(double vo2max) {
        return vo2max / 17;
    }

    /**
     * The formula used in the calculation is taken from Daniels & Gilbert
     * VO2 Max=(-4.60 + 0.182258 * velocity + 0.000104 * velocity^2)/(0.8 + 0.1894393 * e^(-0.012778 * time) + 0.2989558 * e^(-0.1932605 * time))
     * velocity is in metres per minute; and time is in minutes
     */

    private double CalculateVO2max(double distance, int seconds) {
        double vO2max;
        double velocity;
        velocity = (distance / seconds) * 60000;
        double time = seconds / 60;
        vO2max = (-4.60 + 0.182258 * velocity + 0.000104 * velocity * velocity) / (0.8 + 0.1894393 * Math.exp(-0.012778 * time) + 0.2989558 * Math.exp(-0.1932605 * time));
        return vO2max;

    }

    private void ShowResults(String Results) {
        TextView ResultsView = (TextView) findViewById(R.id.evaluationTextView);
        ResultsView.setText(Results);
    }

    public void EvaluateSpeeds(View view) {
        int ownHours;
        String own_Hours;
        int ownMinutes;
        String own_Minutes;
        int ownSeconds;
        String own_Seconds;

        // Get distance in km from input
        EditText DistanceText = (EditText) findViewById(R.id.own_distance);
        String own_distance = DistanceText.getText().toString();
        if (own_distance == "") {
            Toast.makeText(MainActivity.this, getString(R.string.error), Toast.LENGTH_SHORT).show();

            // FEHLERMELDUNG einfügen!!!
            return;
        }
        double ownDistance = Double.parseDouble(own_distance);

        // Get time inputs (Hours, Minutes, Seconds)
        TextView HoursText = (TextView) findViewById(R.id.own_hours);
        own_Hours = HoursText.getText().toString();
        if (own_Hours == "") ownHours = 0;
        else ownHours = Integer.parseInt(own_Hours);

        TextView MinutesText = (TextView) findViewById(R.id.own_minutes);
        own_Minutes = MinutesText.getText().toString();

        if (own_Minutes == "") ownMinutes = 0;
        else ownMinutes = Integer.parseInt(own_Minutes);
        if (ownMinutes > 59) {
            // FEHLERMELDUNG
            return;
        }

        TextView SecondsText = (TextView) findViewById(R.id.own_seconds);
        own_Seconds = SecondsText.getText().toString();

        if (own_Seconds == "") ownSeconds = 0;
        else ownSeconds = Integer.parseInt(own_Seconds);

        // check for correct input
        if (ownSeconds > 59) {
            // FEHLERMELDUNG
            return;
        }




        // Sum up all seconds
        int overallSeconds = ownHours * 3600 + ownMinutes * 60 + ownSeconds;
        if (overallSeconds == 0) {
            //FEHLERMELDUNG
            return;
        }

        // Calculate VO2max and pace for half marathon
        // double vo2max = CalculateVO2max(distance, overallSeconds);
        // double pace = CalculatePace(vo2max);


        //display the results
        String ResultsMessage = "Hier sind die Ergebnisse\nGesamtsekunden: " + overallSeconds;
        ResultsMessage += "\nvO2max = " + CalculateVO2max(ownDistance, overallSeconds);
        // ResultsMessage = "VO2max: " + vo2max;

        ShowResults(ResultsMessage);


    }
}
