package br.com.ufpb.c4.ayty.hackathonandroidos2016_c4_scann.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import br.com.ufpb.c4.ayty.hackathonandroidos2016_c4_scann.R;

public class ScanActivity extends AppCompatActivity {

    private ScanAsyncTask mAuthTask = null;

    private View scan;
    private View error;

    private Button mScanAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_scan);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViewById();

        mScanAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuthTask = new ScanAsyncTask();
                mAuthTask.execute((Void) null);
            }
        });

        scan.setVisibility(View.VISIBLE);
        error.setVisibility(View.GONE);
        mAuthTask = new ScanAsyncTask();
        mAuthTask.execute((Void) null);
    }

    private void findViewById(){
        scan = findViewById(R.id.scan);
        error = findViewById(R.id.falha);

        mScanAgain = (Button) findViewById(R.id.again_scan);
    }

    private class ScanAsyncTask extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {

            // Fazer toda a lógica de digitalização aqui
            // A imagem resultante (Bitmap) salve na Application
            // Se houver objeto resultando valido retorne true
            // se não retorne falso

            // Caso ocorra alguma excessão dentro do exception, chamem
            // o metodo onCancelled da classe

            return null;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            mAuthTask = null;

            if(success){
                Intent intent = new Intent(ScanActivity.this, null);
                startActivity(intent);
                finish();
            }else{
                onCancelled();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            scan.setVisibility(View.GONE);
            error.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == android.R.id.home){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
