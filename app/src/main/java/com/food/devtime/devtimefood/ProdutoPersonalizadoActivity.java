package com.food.devtime.devtimefood;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TabHost;

public class ProdutoPersonalizadoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produto_personalizado);

        TabHost abas = (TabHost) findViewById(R.id.tabhost);
        abas.setup();

        TabHost.TabSpec descritor = abas.newTabSpec("aba1");
        // descritor.setContent(R.id.contatos);
        // descritor.setIndicator(getString(R.string.contatos));
        abas.addTab(descritor);

        descritor = abas.newTabSpec("aba2");
        //descritor.setContent(R.id.detalhes);
        //descritor.setIndicator(getString(R.string.detalhes));
        abas.addTab(descritor);
    }
}
