package br.com.ufpb.c4.ayty.hackathonandroidos2016_c4_scann.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import br.com.ufpb.c4.ayty.hackathonandroidos2016_c4_scann.R;
import br.com.ufpb.c4.ayty.hackathonandroidos2016_c4_scann.ScannApplication;
import br.com.ufpb.c4.ayty.hackathonandroidos2016_c4_scann.fragment.ItemMain;

public class MainActivity extends AppCompatActivity {

    private LinearLayout container1;
    private LinearLayout container2;
    private LinearLayout container3;

    private Button buttonDelete;
    private Button buttonDeleteAll;
    private Button buttonStart;

    private View buttonScan;
    private View buttonPrint;
    private View buttonMail;

    private ScannApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        application = (ScannApplication) getApplicationContext();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        findViewById();

        clickButtonFuncions();

    }

    private void clickButtonFuncions(){


        buttonScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                application.addFuncao("scan");
                Fragment fragment = new ItemMain();
                Bundle bundle = new Bundle();
                bundle.putInt("img", R.drawable.ic_scanner);
                bundle.putString("desc", "Digitalizar");
                fragment.setArguments(bundle);
                container1.removeAllViews();
                getSupportFragmentManager().beginTransaction().replace(R.id.container1, fragment).addToBackStack("Scan").commit();
                if(application.getFuncoes().size() > 0){
                    buttonDelete.setEnabled(true);
                    buttonDeleteAll.setEnabled(true);
                    buttonStart.setEnabled(true);
                }
            }
        });

        buttonPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                application.addFuncao("print");
                Fragment fragment = new ItemMain();
                Bundle bundle = new Bundle();
                bundle.putInt("img", R.drawable.ic_printer);
                bundle.putString("desc", "Imprimir");
                fragment.setArguments(bundle);
                container2.removeAllViews();
                getSupportFragmentManager().beginTransaction().replace(R.id.container2, fragment).addToBackStack("Printer").commit();
                if(application.getFuncoes().size() > 0){
                    buttonDelete.setEnabled(true);
                    buttonDeleteAll.setEnabled(true);
                    buttonStart.setEnabled(true);
                }
            }
        });

        buttonMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                application.addFuncao("mail");
                Fragment fragment = new ItemMain();
                Bundle bundle = new Bundle();
                bundle.putInt("img", R.drawable.ic_mail);
                bundle.putString("desc", "Enviar por e-mail");
                fragment.setArguments(bundle);
                container3.removeAllViews();
                getSupportFragmentManager().beginTransaction().replace(R.id.container3, fragment).addToBackStack("Mail").commit();
                if(application.getFuncoes().size() > 0){
                    buttonDelete.setEnabled(true);
                    buttonDeleteAll.setEnabled(true);
                    buttonStart.setEnabled(true);
                }
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSupportFragmentManager().popBackStack();
                application.removerFuncao();
            }
        });

        buttonDeleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                container1.removeAllViews();
                container2.removeAllViews();
                container3.removeAllViews();
                application.removerTudo();
            }
        });

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(application.contains("mail")){
                    createDialogMail();
                }else{
                    return;
                }
            }
        });
    }

    private void createDialogMail(){
        final View view = LayoutInflater.from(this).inflate(R.layout.content_alert_mail, null, false);

        new AlertDialog.Builder(this)
                .setTitle("Vamos começar!")
                .setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        TextInputEditText text = (TextInputEditText) view.findViewById(R.id.insert_mail);
                        application.setEmails(text.getText().toString());
                        createDialogScan();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .create().show();
    }

    private void createDialogScan(){
        final View view = LayoutInflater.from(this).inflate(R.layout.content_alert_scan, null, false);

        new AlertDialog.Builder(this)
                .setTitle("Atenção!")
                .setView(view)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(MainActivity.this, ScanActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .create().show();
    }

    private void findViewById(){
        container1 = (LinearLayout) findViewById(R.id.container1);
        container2 = (LinearLayout) findViewById(R.id.container2);
        container3 = (LinearLayout) findViewById(R.id.container3);

        buttonDeleteAll = (Button) findViewById(R.id.button_delete_all);
        buttonDelete = (Button) findViewById(R.id.button_delete);
        buttonStart = (Button) findViewById(R.id.button_start);

        buttonScan = findViewById(R.id.btn_scan);
        buttonPrint = findViewById(R.id.btn_print);
        buttonMail = findViewById(R.id.btn_mail);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if(id == R.id.action_about){
            View view = LayoutInflater.from(this).inflate(R.layout.content_alert_sobre, null, false);
            new AlertDialog.Builder(this)
                    .setTitle("Sobre")
                    .setView(view)
                    .setPositiveButton("OK", null)
                    .create().show();
        }

        return super.onOptionsItemSelected(item);
    }
}
