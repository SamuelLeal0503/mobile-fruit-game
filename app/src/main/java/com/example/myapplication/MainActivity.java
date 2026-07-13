package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    
    EditText et_Nombre;
    ImageView iv_personaje;
    TextView tv_BestScore;
    Button btnPlay;
    MediaPlayer mp;
    private FirestoreHelper firestoreHelper;

    int numAlatorio = (int) (Math.random() * 10);

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setSupportActionBar(findViewById(R.id.myToolBar));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        et_Nombre = findViewById(R.id.editTextComoTellamas);
        iv_personaje = findViewById(R.id.imageView);
        tv_BestScore = findViewById(R.id.editTextEmpty);
        btnPlay = findViewById(R.id.buttonJugar);

        firestoreHelper = new FirestoreHelper();

        firestoreHelper.getHighestScore(new FirestoreHelper.GetHighestScoreCallback() {
            @Override
            public void onSuccess(User user) {
                // Handle the user object
                System.out.println("Received highest score: " + user.getScore());
                tv_BestScore.setText("Record actual: " + user.getScore() + " de " + user.getUsername());
            }

            @Override
            public void onFailure(String errorMessage) {
                // Handle the failure case
                System.out.println("Failed to retrieve highest score: " + errorMessage);
                tv_BestScore.setText(errorMessage);
            }
        });


        int id;
        switch (numAlatorio){
            case 0 : case 10:
                id = getResources().getIdentifier("mango", "drawable",getPackageName());
                iv_personaje.setImageResource(id);
                break;
            case 1 : case 9:
                id = getResources().getIdentifier("fresa", "drawable",getPackageName());
                iv_personaje.setImageResource(id);
                break;
            case 2 : case 8:
                id = getResources().getIdentifier("manzana", "drawable",getPackageName());
                iv_personaje.setImageResource(id);
                break;
            case 3 : case 7:
                id = getResources().getIdentifier("sandia", "drawable",getPackageName());
                iv_personaje.setImageResource(id);
                break;
            case 4 : case 5: case 6:
                id = getResources().getIdentifier("uva", "drawable",getPackageName());
                iv_personaje.setImageResource(id);
                break;

            default:
                id = getResources().getIdentifier("naranja", "drawable",getPackageName());
                iv_personaje.setImageResource(id);
                break;
        }
        mp = MediaPlayer.create(this, R.raw.alphabet_song);
        mp.start();
        mp.setLooping(true);
    }

    public void jugar(View vista){
        String nombre = et_Nombre.getText().toString();

        if (!nombre.equals("")){
            mp.stop();
            mp.release();

            Intent intent = new Intent(this, MainActivity2_Nivel1.class);
            intent.putExtra("jugador", nombre);
            startActivity(intent);
            finish();
        }else{
            Toast.makeText(this, "Debe escribir su nombre!", Toast.LENGTH_SHORT).show();

            et_Nombre.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
            imm.showSoftInput(et_Nombre, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    @Override
    public void onBackPressed(){

    }
}