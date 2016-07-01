package br.com.ufpb.c4.ayty.hackathonandroidos2016_c4_scann.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import br.com.ufpb.c4.ayty.hackathonandroidos2016_c4_scann.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new SplashAsyncTask().execute((Void) null);
    }

    private class SplashAsyncTask extends AsyncTask<Void, Void, Boolean> {

        private final int TIME = 1000;


        @Override
        protected Boolean doInBackground(Void... voids) {
            try{
                Thread.sleep(TIME);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
