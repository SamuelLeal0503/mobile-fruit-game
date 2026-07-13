package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

public class MainActivity2_Nivel7 extends AppCompatActivity {
    Toolbar myToolBar, myToolBar2;
    TextView tv_nombre, tv_score;
    ImageView ivAuno, ivAdos, iv_vidas;
    EditText et_respuesta;
    MediaPlayer mp, mp_great, mp_bad;
    int score, numAleatorio_uno, numAleatorio_dos, resultado, vidas = 3;
    String nombre_jugador, string_score, string_vidas;
    String numero[]= {"cero","uno","dos","tres","cuatro","cinco","seis","siete","ocho","nueve","diez"};
    ProgressBar progressBar;
    TextView tvSeconds;
    CountDownTimer countDownTimer;
    int timeLeft = 10;
    boolean isTimerRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity2_nivel7);

        Toast.makeText(this, "Nivel 7 - Dividir", Toast.LENGTH_SHORT).show();
        tv_nombre = findViewById(R.id.textView_Nombre);
        tv_score = findViewById(R.id.textView_Score);
        iv_vidas = findViewById(R.id.imageView_Manzanas);
        ivAuno = findViewById(R.id.imageView_NumeroUno);
        ivAdos = findViewById(R.id.imageView_NumeroDos);
        et_respuesta = findViewById(R.id.et_Resultado);
        progressBar = findViewById(R.id.barra);
        tvSeconds = findViewById(R.id.tiempo);

        progressBar.setMax(10);

        nombre_jugador = getIntent().getStringExtra("jugador");
        tv_nombre.setText("Jugador: " + nombre_jugador);

        string_score = getIntent().getStringExtra("score");
        score = Integer.parseInt(string_score);
        tv_score.setText("Score: " + score);

        string_vidas = getIntent().getStringExtra("vidas");
        vidas = Integer.parseInt(string_vidas);
        switch (vidas){
            case 3:
                iv_vidas.setImageResource(R.drawable.tresvidas);
                break;
            case 2:
                iv_vidas.setImageResource(R.drawable.dosvidas);
                break;
            case 1:
                iv_vidas.setImageResource(R.drawable.unavida);
                break;
        }

        setSupportActionBar(findViewById(R.id.myToolBar2Nivel1));
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        mp = MediaPlayer.create(this, R.raw.alphabet_song);
        mp.start();
        mp.setLooping(true);

        mp_great = MediaPlayer.create(this, R.raw.wonderful);
        mp_bad = MediaPlayer.create(this, R.raw.bad);

        numeroAleatorio();


        timeLeft = 10;
        updateProgressBar();
        updateTextView();

        startTimer();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mp_bad != null) {
            mp_bad.release();
            mp_bad = null;
        }
        stopTimer();
    }
    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeft * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = (int) (millisUntilFinished / 1000);
                updateProgressBar();
                updateTextView();
            }
            @Override
            public void onFinish() {
                loseLife();
            }
        };
        countDownTimer.start();
        isTimerRunning = true;
    }


    private void loseLife() {
        if (mp_bad != null) {
            mp_bad.start();
        }
        vidas--;
        switch (vidas) {
            case 2:
                iv_vidas.setImageResource(R.drawable.dosvidas);
                Toast.makeText(this, "Quedan dos manzanas", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                iv_vidas.setImageResource(R.drawable.unavida);
                Toast.makeText(this, "Queda una manzana", Toast.LENGTH_SHORT).show();
                break;
            case 0:
                iv_vidas.setImageResource(R.drawable.cero);
                Toast.makeText(this, "Has perdido todas las manzanas", Toast.LENGTH_SHORT).show();
                mp.stop();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }

        stopTimer();

        timeLeft = 10;
        updateProgressBar();
        updateTextView();

        startTimer();
    }

    private void updateProgressBar() {
        progressBar.setProgress(timeLeft);
    }

    private void updateTextView() {
        tvSeconds.setText(String.valueOf(timeLeft));
    }

    private void stopTimer() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
            countDownTimer = null;
        }
    }
    public void comparar(View vista) {
        String respuesta = et_respuesta.getText().toString();
        if (!respuesta.equals("")) {
            if (respuesta.length() < 3) {
                int respuestaJugador = Integer.parseInt(respuesta);
                if (resultado == respuestaJugador) {
                    mp_great.start();
                    score++;
                    tv_score.setText("Score: " + score);
                } else {
                    loseLife();
                }
            } else {
                loseLife();
            }
            baseDeDatos();
            et_respuesta.setText("");
            numeroAleatorio();

            stopTimer();
            timeLeft = 10;
            updateProgressBar();
            updateTextView();
            startTimer();
        } else {
            Toast.makeText(this, "Debes dar una respuesta", Toast.LENGTH_SHORT).show();
        }
    }

    private void numeroAleatorio() {
        if (score <= 69) {
            numAleatorio_uno = (int) (Math.random() * 10);
            do {
                numAleatorio_dos = (int) (Math.random() * 10);
            } while (numAleatorio_dos == 0);

            resultado = numAleatorio_uno / numAleatorio_dos;

            if (resultado >= 0) {
                for (int i = 0; i < numero.length; i++) {
                    int id = getResources().getIdentifier(numero[i], "drawable", getPackageName());
                    if (numAleatorio_uno == i) {
                        ivAuno.setImageResource(id);
                    }
                    if (numAleatorio_dos == i) {
                        ivAdos.setImageResource(id);
                    }
                }
            } else {
                numeroAleatorio();
            }
        } else {
            Intent intent = new Intent(this, MainActivity2_Nivel8.class);
            string_score = String.valueOf(score);
            string_vidas = String.valueOf(vidas);

            intent.putExtra("jugador", nombre_jugador);
            intent.putExtra("score", string_score);
            intent.putExtra("vidas", string_vidas);

            mp.stop();
            mp.release();
            startActivity(intent);
            finish();
        }
    }


    public void baseDeDatos() {
        FirestoreHelper firestoreHelper = new FirestoreHelper();

        User user = firestoreHelper.saveUserScore(nombre_jugador, score);
    }

}