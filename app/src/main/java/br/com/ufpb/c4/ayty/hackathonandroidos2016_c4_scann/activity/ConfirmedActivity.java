package br.com.ufpb.c4.ayty.hackathonandroidos2016_c4_scann.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import br.com.ufpb.c4.ayty.hackathonandroidos2016_c4_scann.R;
import br.com.ufpb.c4.ayty.hackathonandroidos2016_c4_scann.ScannApplication;

public class ConfirmedActivity extends AppCompatActivity {

    private EnviarMail mAuthTask = null;

    private ImageView imageView;

    private RecyclerView recyclerView;

    private Button scanNovamente;
    private Button cancelar;
    private Button enviar;

    private View content;
    private View animSend;

    private ScannApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmed);

        application = (ScannApplication) getApplicationContext();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_confirmed);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViewById();

        // Coloca a imagem resultante da digitalização
        // o formato esta definido como Bitmap, se este nao for o formato
        // alterem para o formato compativel
        // Não esqueçam de alterar o método abaixo para exibir a imagem
        imageView.setImageBitmap(application.getBitmap());

        createRecyclerView();

        scanNovamente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConfirmedActivity.this, ScanActivity.class);
                startActivity(intent);
                finish();
            }
        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConfirmedActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attempSend();
            }
        });

    }

    private void attempSend(){
        content.setVisibility(View.GONE);
        animSend.setVisibility(View.VISIBLE);
        mAuthTask = new EnviarMail();
        mAuthTask.execute((Void) null);
    }

    private void findViewById(){
        imageView = (ImageView) findViewById(R.id.imageScan);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        scanNovamente = (Button) findViewById(R.id.digitalizarnovamente);
        cancelar = (Button) findViewById(R.id.cancel);
        enviar = (Button) findViewById(R.id.enviar);

        content = findViewById(R.id.content);
        animSend = findViewById(R.id.send_layout);
    }

    private void createRecyclerView(){
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(null);
    }

    private class EnviarMail extends AsyncTask<Void, Void, Boolean> {

        @Override
        protected Boolean doInBackground(Void... voids) {
            String[] emails = application.getEmails().split(";"); // Lista de emails a serem enviados

            // Fazer a parte de enviar aqui
            // Usar excessões, se enviar tudo sem problemas, retornar true
            // caso ocorra alguma falha ou de problema retorne falso

            return null;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            mAuthTask = null;

            if(success){
                new AlertDialog.Builder(ConfirmedActivity.this)
                        .setTitle("Concluído")
                        .setMessage("Todos os e-mails foram enviados")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(ConfirmedActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .create().show();
            } else{
                content.setVisibility(View.VISIBLE);
                animSend.setVisibility(View.GONE);
                new AlertDialog.Builder(ConfirmedActivity.this)
                        .setTitle("Falha ao enviar e-mail")
                        .setMessage("Ocorreu algum problema ao enviar os e-mails.\nTentar novamente?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                attempSend();
                            }
                        })
                        .setNegativeButton("Cancelar", null)
                        .create().show();
            }
        }

    }
}
