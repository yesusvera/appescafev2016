package br.org.unesco.appesca.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

import br.org.unesco.appesca.R;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class TelaInicialUnescoSplashActivity extends Activity {

    private int TEMPO_INICIAL_MS = 1500;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_inicial_unesco_splash);

        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                finish();

                Intent intent = new Intent();
                intent.setClass(TelaInicialUnescoSplashActivity.this, LoginUnescoActivity.class);
                startActivity(intent);
            }
        }, TEMPO_INICIAL_MS);
    }
}
