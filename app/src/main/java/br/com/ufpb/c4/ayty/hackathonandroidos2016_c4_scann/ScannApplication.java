package br.com.ufpb.c4.ayty.hackathonandroidos2016_c4_scann;

import android.app.Application;
import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by raphaelsalviano on 29/06/16.
 */
public class ScannApplication extends Application {

    private List<String> funcoes;
    private String emails;
    private Bitmap bitmap;


    @Override
    public void onCreate() {
        super.onCreate();
        funcoes = new ArrayList<>();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public void addFuncao(String funcao){
        this.funcoes.add(funcao);
    }

    public void removerFuncao(){
        if(funcoes.size() >= 0) {
            this.funcoes.remove(funcoes.size() - 1);
        }
    }

    public void removerTudo(){
        for (int i = 0; i < funcoes.size(); i++){
            funcoes.remove(i);
        }
    }

    public List<String> getFuncoes() {
        return funcoes;
    }

    public boolean contains(String function){
        return this.funcoes.contains(function);
    }

    public String getEmails() {
        return emails;
    }

    public void setEmails(String emails) {
        this.emails = emails;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
