package de.spdns.safhome.optimalrunningspeed;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    /**
     * The VO2 max is the body's maximum intake of Oxygen in mililitres per kg of body weight per minute
     * The VO2 max can be measured exactly by performing a lactate test. It can also be approximated with this function.
     * The formula used in the calculation is taken from Daniels & Gilbert 1979
     * VO2 Max=(-4.60 + 0.182258 * velocity + 0.000104 * velocity^2)/(0.8 + 0.1894393 * e^(-0.012778 * time) + 0.2989558 * e^(-0.1932605 * time))
     * velocity is in metres per minute; and time is in minutes
     *
     * @param distance
     * @param seconds
     */

    private double CalculateVO2max(double distance, int seconds) {
        double vO2max;
        double velocity;
        velocity = (distance / seconds) * 60000;
        double time = seconds / 60;
        vO2max = (-4.60 + 0.182258 * velocity + 0.000104 * velocity * velocity) / (0.8 + 0.1894393 * Math.exp(-0.012778 * time) + 0.2989558 * Math.exp(-0.1932605 * time));
        return vO2max;

    }

    private String CalculateHMTime(double vo2max) {
        return "1:57:30";
    }


    /**
     * Calculate the speed at which
     * vVO2 (metres per second) = 2.8859 + 0.0686 * (VO2-29)
     * vVO2 (km/h) = 10.38924 + 0.24696 * (VO2-29)
     *
     * @param vo2max
     * @return
     */
    private double CalculatevVO2max(double vo2max) {
        return 10.38924 + 0.24696 * (vo2max - 29);
    }

    private void ShowResults(String Results) {
        TextView ResultsView = (TextView) findViewById(R.id.evaluationTextView);
        ResultsView.setText(Results);
    }

    public void EvaluateSpeeds(View view) {
        // Hide keyboard
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        // Declare variables for hours, minutes and seconds, Integers are needed to check if the input is valid (eg. not 80 seconds) and Strings are needed to prevent parse errors.
        int ownHours;
        String own_Hours;
        int ownMinutes;
        String own_Minutes;
        int ownSeconds;
        String own_Seconds;

        // Get distance in km from input
        EditText DistanceText = (EditText) findViewById(R.id.own_distance);
        String own_distance = DistanceText.getText().toString().trim();
        if (own_distance.isEmpty()) {
            Toast toast = Toast.makeText(MainActivity.this, getString(R.string.error_distance), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }
        double ownDistance = Double.parseDouble(own_distance);

        // Get time inputs (Hours, Minutes, Seconds)
        // Check, if one of the fields is left empty. In that case assign value 0.
        // Also check if minutes or seconds are larger 59 and give a warning.

        // HOURS
        TextView HoursText = (TextView) findViewById(R.id.own_hours);
        own_Hours = HoursText.getText().toString().trim();
        if (own_Hours.isEmpty()) ownHours = 0;
        else ownHours = Integer.parseInt(own_Hours);

        //MINUTES
        TextView MinutesText = (TextView) findViewById(R.id.own_minutes);
        own_Minutes = MinutesText.getText().toString().trim();

        if (own_Minutes.isEmpty()) ownMinutes = 0;
        else ownMinutes = Integer.parseInt(own_Minutes);
        if (ownMinutes > 59) {
            Toast toast = Toast.makeText(MainActivity.this, getString(R.string.error_60_seconds_minutes), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }

        //SECONDS
        TextView SecondsText = (TextView) findViewById(R.id.own_seconds);
        own_Seconds = SecondsText.getText().toString().trim();

        if (own_Seconds.isEmpty()) ownSeconds = 0;
        else ownSeconds = Integer.parseInt(own_Seconds);

        if (ownSeconds > 59) {
            Toast toast = Toast.makeText(MainActivity.this, getString(R.string.error_60_seconds_minutes), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }

        // Sum up all seconds
        int overallSeconds = ownHours * 3600 + ownMinutes * 60 + ownSeconds;
        if (overallSeconds == 0) {
            Toast toast = Toast.makeText(MainActivity.this, getString(R.string.error_time_zero), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            return;
        }

        double vO2max = CalculateVO2max(ownDistance, overallSeconds);
        //display the results
        String ResultsMessage = getString(R.string.results);
        ResultsMessage += "\nVO2max = " + String.format(Locale.getDefault(), "%.1f", vO2max) + " ml/kg/min";
        ResultsMessage += "\nHM time = " + CalculateHMTime(vO2max);
        ResultsMessage += "\n" + getString(R.string.vVO2_explanation) + "\nvVO2max = " + String.format(Locale.getDefault(), "%.1f", CalculatevVO2max(vO2max)) + " km/h";

        ShowResults(ResultsMessage);


    }
}
