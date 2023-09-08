package co.edu.unipiloto.stopwatchapp;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Locale;

public class Stopwatch extends Activity {

    private  int seconds = 0;
    private boolean running;
    private int lapCount = 0; // Para contar las vueltas
    private long lapStartTime = 0; // Para registrar el tiempo de inicio de cada vuelta
    private long[] lapTimes; // Para almacenar los tiempos de cada vuelta
    private ArrayAdapter<String> lapAdapter; // El adaptador para mostrar las vueltas en un ListView





    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopwatch);
        runTimer();

        lapTimes = new long[5]; // Puedes ajustar esto si deseas un número diferente de vueltas
        lapAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        ListView lapListView = findViewById(R.id.lap_view);
        lapListView.setAdapter(lapAdapter);

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putInt("seconds", seconds);
        savedInstanceState.putBoolean("running", running);
        if (savedInstanceState != null){
            seconds = savedInstanceState.getInt("seconds");
            running = savedInstanceState.getBoolean("running");
        }
    }

    public void onClickStart (View view){
            running = true;
        }

    public void onClickStop (View view){
            running = false;
        }
    public void onClickReset (View view){
            running = false;
            seconds = 0;
            resetLaps();

        }
    public void onClickLap(View view) {
        if (running && lapCount < 5) { // Puedes cambiar 5 a la cantidad deseada de vueltas
            lapCount++;
            lapTimes[lapCount - 1] = System.currentTimeMillis() - lapStartTime;
            String lapTimeStr = formatTime(lapTimes[lapCount - 1]);
            String lapInfo = "Lap " + lapCount + ": " + lapTimeStr;
            lapAdapter.add(lapInfo);

            if (lapCount == 5) {
                // Detener el cronómetro después de 5 vueltas
                onClickStop(view);
            } else {
                // Iniciar la siguiente vuelta
                lapStartTime = System.currentTimeMillis();
            }
        }
    }

    private String formatTime(long timeInMillis) {
        // Implementa la lógica para formatear el tiempo en HH:MM:SS o el formato que prefieras
        int seconds = (int) (timeInMillis / 1000);
        int minutes = seconds / 60;
        seconds = seconds % 60;
        int hours = minutes / 60;
        minutes = minutes % 60;
        return String.format(Locale.getDefault(), "%d:%02d:%02d", hours, minutes, seconds);
    }
    private void resetLaps() {
        lapCount = 0;
        lapTimes = new long[5]; // O ajusta el tamaño según tu necesidad
        lapAdapter.clear(); // Limpia el adaptador de la lista de vueltas
    }



    //Sets the number of seconds on the timer
    private void runTimer(){
            final TextView timeView = findViewById(R.id.time_view);
            final Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {
                    int hours = seconds/3600;
                    int minutes = (seconds%3600)/60;
                    int secs = seconds%60;
                    String time = String.format(Locale.getDefault(),"%d:%02d:%02d" , hours, minutes, secs);
                    timeView.setText(time);
                    if(running){
                        seconds++;
                    }
                    handler.postDelayed(this, 1000);
                }
            });

    }

}
