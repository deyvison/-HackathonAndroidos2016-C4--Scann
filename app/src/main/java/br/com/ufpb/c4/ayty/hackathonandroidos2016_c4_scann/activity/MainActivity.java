package br.com.ufpb.c4.ayty.hackathonandroidos2016_c4_scann.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sec.android.ngen.common.lib.ssp.CapabilitiesExceededException;
import com.sec.android.ngen.common.lib.ssp.Result;
import com.sec.android.ngen.common.lib.ssp.scanner.ScanAttributes;
import com.sec.android.ngen.common.lib.ssp.scanner.ScanAttributesCaps;
import com.sec.android.ngen.common.lib.ssp.scanner.ScanletAttributes;
import com.sec.android.ngen.common.lib.ssp.scanner.ScannerService;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;

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
    private ScanObserver mScanObserver = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mScanObserver = new ScanObserver(new Handler());
        application = (ScannApplication) getApplicationContext();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        findViewById();

        clickButtonFunctions();

    }

    private void clickButtonFunctions(){


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
                container1.removeAllViews();
                getSupportFragmentManager().beginTransaction().replace(R.id.container2, fragment).addToBackStack("Printer").commit();
                if(application.getFuncoes().size() > 0){
                    buttonDelete.setEnabled(true);
                    buttonDeleteAll.setEnabled(true);
                    buttonStart.setEnabled(true);
                }
                Toast.makeText(getApplicationContext(), "Em breve!", Toast.LENGTH_SHORT).show();
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
                container1.removeAllViews();
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
                        TextInputEditText text1 = (TextInputEditText) view.findViewById(R.id.insert_your_mail);
                        application.setEmails(text.getText().toString());
                        application.setUserMail(text1.getText().toString());
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
                        Toast.makeText(getApplicationContext(), "Mudando para o Scan", Toast.LENGTH_SHORT).show();
                        new ScanAsyncTask(getApplicationContext(), mScanObserver).execute();

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
    protected void onResume() {
        super.onResume();
        mScanObserver.register(getApplicationContext());
    }

    @Override
    protected void onPause() {
        super.onPause();
        mScanObserver.unregister(getApplicationContext());
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


    private class ScanAsyncTask extends AsyncTask<Void, Void, Boolean> {
        String LOG_TAG = ScanAsyncTask.class.getSimpleName();
        final Context mContext;
        final WeakReference<ScanObserver> mObserver;

        public ScanAsyncTask(final Context context, final ScanObserver observer) {
            mContext = context;
            mObserver = new WeakReference<ScanObserver>(observer);

        }

        @Override
        protected Boolean doInBackground(Void... voids) {

            // Fazer toda a lógica de digitalização aqui
            // A imagem resultante (Bitmap) salve na Application
            // Se houver objeto resultando valido retorne true
            // se não retorne falso

            // Caso ocorra alguma excessão dentro do exception, chamem
            // o metodo onCancelled da classe

            final Result result = new Result();
            final ScanAttributesCaps caps = ScannerService.getCapabilities(mContext,result);

            if(caps == null){
                return null;
            }
            try{
                // Obtém os e-mails
                String body = null;
                String from = application.getUserMail();
                final String subj;
                String[] to = null;
                String emails = application.getEmails();
                if(!emails.contains(" ")){
                    to = emails.split(";");
                } else if(emails.contains("; ")){
                    to = emails.split("; ");
                }

                final ScanAttributes.EmailBuilder emailBuilder = new ScanAttributes.EmailBuilder(from, new ArrayList<String>(Arrays.asList(to)));
                // Formato do documento
                ScanAttributes.DocumentFormat doc = ScanAttributes.DocumentFormat.JPEG;
                emailBuilder.setDocumentFormat(doc);
                final ScanAttributes attributes = emailBuilder.build(caps);

                final ScanletAttributes scanletAttributes = new ScanletAttributes.Builder().build();

                final String rid = ScannerService.submit(mContext, attributes, scanletAttributes);

                if(mObserver.get() != null){
                    mObserver.get().setRid(rid);
                }

            } catch (CapabilitiesExceededException cee){
                Log.d(LOG_TAG, "Capacidades excedidas! " + cee.getMessage());
            }

            return true;
        }

    }

    private static class ScanObserver extends ScannerService.AbstractScanletObserver{
        private String LOG_TAG = ScanObserver.class.getSimpleName();
        private String mRid = "";

        public ScanObserver(Handler handler) {
            super(handler);
        }

        public void setRid(final String rid){
            if(rid != null){
                this.mRid = rid;
            } else {
                this.mRid = "";
            }
        }

        @Override
        public void onProgress(String rid, Bundle bundle) {

        }

        @Override
        public void onComplete(String rid, Bundle bundle) {
            if(!mRid.equals(rid)){
                Log.w(LOG_TAG, "ID esperado: "+ mRid + ", ID recebido: " + rid);
                return;
            }
            Log.d(LOG_TAG, "Digitalização completa!");
//            final ArrayList<String> files = bundle.getStringArrayList(Scanlet.Keys.KEY_FILENAME_LIST);


        }

        @Override
        public void onFail(String s, Result result) {

        }

        @Override
        public void onCancel(String s) {

        }
    }

}
