package de.spdns.safhome.optimalrunningspeed;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    private double CalculatePace(double vo2max) {
        return vo2max / 17;
    }

    private double CalculateVO2max(double distance, int seconds) {
        return distance + seconds;
    }

    private void ShowResults(String Results) {
        TextView ResultsView = (TextView) findViewById(R.id.evaluationTextView);
        ResultsView.setText(Results);
    }

    public void EvaluateSpeeds(View view) {
        double distance = 1.0;

        // Get distance in km from input
        EditText DistanceText = (EditText) findViewById(R.id.own_distance);
        String own_distance = DistanceText.getText().toString();
        double ownDistance = Double.parseDouble(own_distance);

        // Get time inputs (Hours, Minutes, Seconds)
        TextView HoursText = (TextView) findViewById(R.id.own_hours);
        int ownHours = Integer.parseInt(HoursText.getText().toString());

        TextView MinutesText = (TextView) findViewById(R.id.own_minutes);
        int ownMinutes = Integer.parseInt(MinutesText.getText().toString());

        TextView SecondsText = (TextView) findViewById(R.id.own_seconds);
        int ownSeconds = Integer.parseInt(SecondsText.getText().toString());

        // Sum up all seconds
        int overallSeconds = ownHours * 3600 + ownMinutes * 60 + ownSeconds;

        // Calculate VO2max and pace for half marathon
        // double vo2max = CalculateVO2max(distance, overallSeconds);
        // double pace = CalculatePace(vo2max);


        //display the results
        String ResultsMessage = "Hier sind die Ergebnisse\nGesamtsekunden: " + overallSeconds;
        // ResultsMessage = "VO2max: " + vo2max;

        ShowResults(ResultsMessage);


    }
}
