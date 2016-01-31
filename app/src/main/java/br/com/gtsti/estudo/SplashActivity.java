package br.com.gtsti.estudo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * Created by Glauber on 25/01/2016.
 */
public class SplashActivity extends Activity {
    // Timer da splash screen
    private static int SPLASH_TIME_OUT = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_gts);

        new Handler().postDelayed(new Runnable()
        {
        /* * Exibindo splash com um timer. */
            @Override public void run()
            {
            // Esse método será executado sempre que o timer acabar
            // E inicia a activity principal
            Intent i = new Intent(SplashActivity.this, PrincipalActivity.class);
                startActivity(i);
                // Fecha esta activity
                finish();
            }
        }, SPLASH_TIME_OUT);

    }
}
