package br.com.ufpb.c4.ayty.hackathonandroidos2016_c4_scann.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.sec.android.ngen.common.lib.ssp.CapabilitiesExceededException;
import com.sec.android.ngen.common.lib.ssp.Result;
import com.sec.android.ngen.common.lib.ssp.scanner.ScanAttributes;
import com.sec.android.ngen.common.lib.ssp.scanner.ScanAttributesCaps;
import com.sec.android.ngen.common.lib.ssp.scanner.Scanlet;
import com.sec.android.ngen.common.lib.ssp.scanner.ScanletAttributes;
import com.sec.android.ngen.common.lib.ssp.scanner.ScannerService;

import net.xoaframework.ws.v1.device.scandev.scanner.Scanner;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;

import br.com.ufpb.c4.ayty.hackathonandroidos2016_c4_scann.R;
import br.com.ufpb.c4.ayty.hackathonandroidos2016_c4_scann.ScannApplication;

public class ScanActivity extends AppCompatActivity {

    private ScanAsyncTask mAuthTask = null;

    private View scan;
    private View error;
    private ScanObserver mScanObserver = null;
    private Button mScanAgain;
    private ScannApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        //Toast.makeText(getApplicationContext(), "Entrou Scan Activity !", Toast.LENGTH_LONG).show();

        application = (ScannApplication) getApplicationContext();
        mScanObserver = new ScanObserver(new Handler());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_scan);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViewById();

        mScanAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuthTask = new ScanAsyncTask(getApplicationContext(), mScanObserver);
                mAuthTask.execute();
            }
        });
        //Toast.makeText(getApplicationContext(), "Entrou Scan Activity", Toast.LENGTH_LONG).show();
        scan.setVisibility(View.VISIBLE);
        error.setVisibility(View.GONE);
        mAuthTask = new ScanAsyncTask(getApplicationContext(), mScanObserver);
        mAuthTask.execute();
    }

    private void findViewById(){
        scan = findViewById(R.id.scan);
        error = findViewById(R.id.falha);

        mScanAgain = (Button) findViewById(R.id.again_scan);
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

    /**
     *
     */
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
