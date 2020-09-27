package com.food.devtime.devtimefood;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.food.devtime.devtimefood.Database.ConnectionRemote;
import com.food.devtime.devtimefood.Database.database;
import com.food.devtime.devtimefood.Dominio.Entidade.Configuracao;
import com.food.devtime.devtimefood.Dominio.RepositorioDevtimeFood;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MesaActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, AdapterView.OnItemClickListener {

    protected String CodigoPedido = "";
    protected int QuantidadeMesa = 0;
    ConnectionRemote connectionRemote;
    private ArrayAdapter<Configuracao> adpConfiguracao;
    private com.food.devtime.devtimefood.Database.database database;
    private SQLiteDatabase connLocal;
    private RepositorioDevtimeFood repositorioDevtimeFood;
    private Configuracao configuracao;
    private long lastBackPressTime = 0;
    private Toast toast;
    private EditText txtMesa;
    private Button
            btnMesa1, btnMesa2, btnMesa3, btnMesa4, btnMesa5, btnMesa6, btnMesa7, btnMesa8, btnMesa9, btnMesa10,
            btnMesa11, btnMesa12, btnMesa13, btnMesa14, btnMesa15, btnMesa16, btnMesa17, btnMesa18, btnMesa19, btnMesa20,
            btnMesa21, btnMesa22, btnMesa23, btnMesa24, btnMesa25, btnMesa26, btnMesa27, btnMesa28, btnMesa29, btnMesa30,
            btnMesa31, btnMesa32, btnMesa33, btnMesa34, btnMesa35, btnMesa36, btnMesa37, btnMesa38, btnMesa39, btnMesa40,
            btnMesa41, btnMesa42, btnMesa43, btnMesa44, btnMesa45, btnMesa46, btnMesa47, btnMesa48, btnMesa49, btnMesa50,
            btnMesa51, btnMesa52, btnMesa53, btnMesa54, btnMesa55, btnMesa56, btnMesa57, btnMesa58, btnMesa59, btnMesa60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mesa);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Selecione a Mesa...");

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabSalvaMEsa);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //   .setAction("Action", null).show();
                SalvaMesaSelecionada();

                finish();
            }
        });
        //  getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        connectionRemote = new ConnectionRemote();

        //region CARREGA AS CONFIGURACOES PARA A CONEXAO
        try {

            database = new database(this);
            connLocal = database.getWritableDatabase();
            repositorioDevtimeFood = new RepositorioDevtimeFood(connLocal);
            adpConfiguracao = repositorioDevtimeFood.buscaConfiguracao(this);

        } catch (Exception e) {

            Toast.makeText(MesaActivity.this, "Erro na configuracao", Toast.LENGTH_SHORT).show();
        }

        txtMesa = (EditText) findViewById(R.id.txtMesa);

        //region Button
        btnMesa1 = (Button) findViewById(R.id.btnMesa1);
        btnMesa2 = (Button) findViewById(R.id.btnMesa2);
        btnMesa3 = (Button) findViewById(R.id.btnMesa3);
        btnMesa4 = (Button) findViewById(R.id.btnMesa4);
        btnMesa5 = (Button) findViewById(R.id.btnMesa5);
        btnMesa6 = (Button) findViewById(R.id.btnMesa6);
        btnMesa7 = (Button) findViewById(R.id.btnMesa7);
        btnMesa8 = (Button) findViewById(R.id.btnMesa8);
        btnMesa9 = (Button) findViewById(R.id.btnMesa9);
        btnMesa10 = (Button) findViewById(R.id.btnMesa10);

        btnMesa11 = (Button) findViewById(R.id.btnMesa11);
        btnMesa12 = (Button) findViewById(R.id.btnMesa12);
        btnMesa13 = (Button) findViewById(R.id.btnMesa13);
        btnMesa14 = (Button) findViewById(R.id.btnMesa14);
        btnMesa15 = (Button) findViewById(R.id.btnMesa15);
        btnMesa16 = (Button) findViewById(R.id.btnMesa16);
        btnMesa17 = (Button) findViewById(R.id.btnMesa17);
        btnMesa18 = (Button) findViewById(R.id.btnMesa18);
        btnMesa19 = (Button) findViewById(R.id.btnMesa19);
        btnMesa20 = (Button) findViewById(R.id.btnMesa20);

        btnMesa21 = (Button) findViewById(R.id.btnMesa21);
        btnMesa22 = (Button) findViewById(R.id.btnMesa22);
        btnMesa23 = (Button) findViewById(R.id.btnMesa23);
        btnMesa24 = (Button) findViewById(R.id.btnMesa24);
        btnMesa25 = (Button) findViewById(R.id.btnMesa25);
        btnMesa26 = (Button) findViewById(R.id.btnMesa26);
        btnMesa27 = (Button) findViewById(R.id.btnMesa27);
        btnMesa28 = (Button) findViewById(R.id.btnMesa28);
        btnMesa29 = (Button) findViewById(R.id.btnMesa29);
        btnMesa30 = (Button) findViewById(R.id.btnMesa30);

        btnMesa31 = (Button) findViewById(R.id.btnMesa31);
        btnMesa32 = (Button) findViewById(R.id.btnMesa32);
        btnMesa33 = (Button) findViewById(R.id.btnMesa33);
        btnMesa34 = (Button) findViewById(R.id.btnMesa34);
        btnMesa35 = (Button) findViewById(R.id.btnMesa35);
        btnMesa36 = (Button) findViewById(R.id.btnMesa36);
        btnMesa37 = (Button) findViewById(R.id.btnMesa37);
        btnMesa38 = (Button) findViewById(R.id.btnMesa38);
        btnMesa39 = (Button) findViewById(R.id.btnMesa39);
        btnMesa40 = (Button) findViewById(R.id.btnMesa40);

        btnMesa41 = (Button) findViewById(R.id.btnMesa41);
        btnMesa42 = (Button) findViewById(R.id.btnMesa42);
        btnMesa43 = (Button) findViewById(R.id.btnMesa43);
        btnMesa44 = (Button) findViewById(R.id.btnMesa44);
        btnMesa45 = (Button) findViewById(R.id.btnMesa45);
        btnMesa46 = (Button) findViewById(R.id.btnMesa46);
        btnMesa47 = (Button) findViewById(R.id.btnMesa47);
        btnMesa48 = (Button) findViewById(R.id.btnMesa48);
        btnMesa49 = (Button) findViewById(R.id.btnMesa49);
        btnMesa50 = (Button) findViewById(R.id.btnMesa50);

        btnMesa51 = (Button) findViewById(R.id.btnMesa51);
        btnMesa52 = (Button) findViewById(R.id.btnMesa52);
        btnMesa53 = (Button) findViewById(R.id.btnMesa53);
        btnMesa54 = (Button) findViewById(R.id.btnMesa54);
        btnMesa55 = (Button) findViewById(R.id.btnMesa55);
        btnMesa56 = (Button) findViewById(R.id.btnMesa56);
        btnMesa57 = (Button) findViewById(R.id.btnMesa57);
        btnMesa58 = (Button) findViewById(R.id.btnMesa58);
        btnMesa59 = (Button) findViewById(R.id.btnMesa59);
        btnMesa60 = (Button) findViewById(R.id.btnMesa60);

        txtMesa.setOnClickListener(this);

        btnMesa1.setOnClickListener(this);
        btnMesa2.setOnClickListener(this);
        btnMesa3.setOnClickListener(this);
        btnMesa4.setOnClickListener(this);
        btnMesa5.setOnClickListener(this);
        btnMesa6.setOnClickListener(this);
        btnMesa7.setOnClickListener(this);
        btnMesa8.setOnClickListener(this);
        btnMesa9.setOnClickListener(this);
        btnMesa10.setOnClickListener(this);

        btnMesa11.setOnClickListener(this);
        btnMesa12.setOnClickListener(this);
        btnMesa13.setOnClickListener(this);
        btnMesa14.setOnClickListener(this);
        btnMesa15.setOnClickListener(this);
        btnMesa16.setOnClickListener(this);
        btnMesa17.setOnClickListener(this);
        btnMesa18.setOnClickListener(this);
        btnMesa19.setOnClickListener(this);
        btnMesa20.setOnClickListener(this);

        btnMesa21.setOnClickListener(this);
        btnMesa22.setOnClickListener(this);
        btnMesa23.setOnClickListener(this);
        btnMesa24.setOnClickListener(this);
        btnMesa25.setOnClickListener(this);
        btnMesa26.setOnClickListener(this);
        btnMesa27.setOnClickListener(this);
        btnMesa28.setOnClickListener(this);
        btnMesa29.setOnClickListener(this);
        btnMesa30.setOnClickListener(this);

        btnMesa31.setOnClickListener(this);
        btnMesa32.setOnClickListener(this);
        btnMesa33.setOnClickListener(this);
        btnMesa34.setOnClickListener(this);
        btnMesa35.setOnClickListener(this);
        btnMesa36.setOnClickListener(this);
        btnMesa37.setOnClickListener(this);
        btnMesa38.setOnClickListener(this);
        btnMesa39.setOnClickListener(this);
        btnMesa40.setOnClickListener(this);

        btnMesa41.setOnClickListener(this);
        btnMesa42.setOnClickListener(this);
        btnMesa43.setOnClickListener(this);
        btnMesa44.setOnClickListener(this);
        btnMesa45.setOnClickListener(this);
        btnMesa46.setOnClickListener(this);
        btnMesa47.setOnClickListener(this);
        btnMesa48.setOnClickListener(this);
        btnMesa49.setOnClickListener(this);
        btnMesa50.setOnClickListener(this);

        btnMesa51.setOnClickListener(this);
        btnMesa52.setOnClickListener(this);
        btnMesa53.setOnClickListener(this);
        btnMesa54.setOnClickListener(this);
        btnMesa55.setOnClickListener(this);
        btnMesa56.setOnClickListener(this);
        btnMesa57.setOnClickListener(this);
        btnMesa58.setOnClickListener(this);
        btnMesa59.setOnClickListener(this);
        btnMesa60.setOnClickListener(this);
        //endregion

        VerificaQuantidadeMesa();

        VerificaStatusMesa verifica = new VerificaStatusMesa();
        verifica.execute("");

        //RCEBE O VALOR DA ACTIVITY PEDIDOS
        CodigoPedido = this.getIntent().getStringExtra("CodPedido");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                Toast.makeText(MesaActivity.this, "Selecione a Mesa...", Toast.LENGTH_SHORT).show();

                // Intent mIntent = //Crie a intent para chamar a activity anterior

                //         startActivity(mIntent);

                // finish(); // Finaliza a Activity atual

                break;

            default:
                break;
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        if (this.lastBackPressTime < System.currentTimeMillis() - 4000) {
            toast = Toast.makeText(this, "Pressione o Botão Voltar novamente para fechar o Aplicativo.", Toast.LENGTH_LONG);
            toast.show();
            this.lastBackPressTime = System.currentTimeMillis();
        } else {
            if (toast != null) {
                toast.cancel();
            }
            Toast.makeText(MesaActivity.this, "Selecione a Mesa...", Toast.LENGTH_SHORT).show();

            //super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnMesa1:

                LimpaButtonMesa();
                txtMesa.setText("1");
                PorterDuffColorFilter colorFilter1 = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                btnMesa1.getBackground().setColorFilter(colorFilter1);

                break;
            case R.id.btnMesa2:

                LimpaButtonMesa();
                txtMesa.setText("2");
                PorterDuffColorFilter colorFilter2 = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                btnMesa2.getBackground().setColorFilter(colorFilter2);

                break;
            case R.id.btnMesa3:

                LimpaButtonMesa();
                txtMesa.setText("3");
                PorterDuffColorFilter colorFilter3 = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                btnMesa3.getBackground().setColorFilter(colorFilter3);

                break;
            case R.id.btnMesa4:

                LimpaButtonMesa();
                txtMesa.setText("4");
                PorterDuffColorFilter colorFilter4 = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                btnMesa4.getBackground().setColorFilter(colorFilter4);

                break;
            case R.id.btnMesa5:

                LimpaButtonMesa();
                txtMesa.setText("5");
                PorterDuffColorFilter colorFilter5 = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                btnMesa5.getBackground().setColorFilter(colorFilter5);

                break;
            case R.id.btnMesa6:

                LimpaButtonMesa();
                txtMesa.setText("6");
                PorterDuffColorFilter colorFilter6 = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                btnMesa6.getBackground().setColorFilter(colorFilter6);

                break;
            case R.id.btnMesa7:

                LimpaButtonMesa();
                txtMesa.setText("7");
                PorterDuffColorFilter colorFilter7 = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                btnMesa7.getBackground().setColorFilter(colorFilter7);

                break;
            case R.id.btnMesa8:

                LimpaButtonMesa();
                txtMesa.setText("8");
                PorterDuffColorFilter colorFilter8 = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                btnMesa8.getBackground().setColorFilter(colorFilter8);

                break;
            case R.id.btnMesa9:

                LimpaButtonMesa();
                txtMesa.setText("9");
                PorterDuffColorFilter colorFilter9 = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                btnMesa9.getBackground().setColorFilter(colorFilter9);

                break;
            case R.id.btnMesa10:

                LimpaButtonMesa();
                txtMesa.setText("10");
                PorterDuffColorFilter colorFilter10 = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                btnMesa10.getBackground().setColorFilter(colorFilter10);

                break;
            case R.id.btnMesa11:

                LimpaButtonMesa();
                txtMesa.setText("11");
                PorterDuffColorFilter colorFilter11 = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                btnMesa11.getBackground().setColorFilter(colorFilter11);

                break;
            case R.id.btnMesa12:

                LimpaButtonMesa();
                txtMesa.setText("12");
                PorterDuffColorFilter colorFilter12 = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                btnMesa12.getBackground().setColorFilter(colorFilter12);

                break;
            case R.id.btnMesa13:

                LimpaButtonMesa();
                txtMesa.setText("13");
                PorterDuffColorFilter colorFilter13 = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                btnMesa13.getBackground().setColorFilter(colorFilter13);

                break;
            case R.id.btnMesa14:

                LimpaButtonMesa();
                txtMesa.setText("14");
                PorterDuffColorFilter colorFilter14 = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                btnMesa14.getBackground().setColorFilter(colorFilter14);

                break;
            case R.id.btnMesa15:

                LimpaButtonMesa();
                txtMesa.setText("15");
                PorterDuffColorFilter colorFilter15 = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                btnMesa15.getBackground().setColorFilter(colorFilter15);

                break;
            case R.id.btnMesa16:

                LimpaButtonMesa();
                txtMesa.setText("16");
                PorterDuffColorFilter colorFilter16 = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                btnMesa16.getBackground().setColorFilter(colorFilter16);

                break;
            case R.id.btnMesa17:

                LimpaButtonMesa();
                txtMesa.setText("17");
                PorterDuffColorFilter colorFilter17 = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                btnMesa17.getBackground().setColorFilter(colorFilter17);

                break;
            case R.id.btnMesa18:

                LimpaButtonMesa();
                txtMesa.setText("18");
                PorterDuffColorFilter colorFilter18 = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                btnMesa18.getBackground().setColorFilter(colorFilter18);

                break;
            case R.id.btnMesa19:

                LimpaButtonMesa();
                txtMesa.setText("19");
                PorterDuffColorFilter colorFilter19 = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                btnMesa19.getBackground().setColorFilter(colorFilter19);

                break;
            case R.id.btnMesa20:

                LimpaButtonMesa();
                txtMesa.setText("20");
                PorterDuffColorFilter colorFilter20 = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                btnMesa20.getBackground().setColorFilter(colorFilter20);

                break;
            case R.id.btnMesa21:

                LimpaButtonMesa();
                txtMesa.setText("21");
                PorterDuffColorFilter colorFilter21 = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                btnMesa21.getBackground().setColorFilter(colorFilter21);

                break;
            case R.id.btnMesa22:

                LimpaButtonMesa();
                txtMesa.setText("22");
                PorterDuffColorFilter colorFilter22 = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                btnMesa22.getBackground().setColorFilter(colorFilter22);

                break;
            case R.id.btnMesa23:

                LimpaButtonMesa();
                txtMesa.setText("23");
                PorterDuffColorFilter colorFilter23 = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                btnMesa23.getBackground().setColorFilter(colorFilter23);

                break;
            case R.id.btnMesa24:

                LimpaButtonMesa();
                txtMesa.setText("24");
                PorterDuffColorFilter colorFilter24 = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                btnMesa24.getBackground().setColorFilter(colorFilter24);

                break;
            case R.id.btnMesa25:

                LimpaButtonMesa();
                txtMesa.setText("25");
                PorterDuffColorFilter colorFilter25 = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                btnMesa25.getBackground().setColorFilter(colorFilter25);

                break;
            case R.id.btnMesa26:

                LimpaButtonMesa();
                txtMesa.setText("26");
                PorterDuffColorFilter colorFilter26 = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                btnMesa26.getBackground().setColorFilter(colorFilter26);

                break;
            case R.id.btnMesa27:

                LimpaButtonMesa();
                txtMesa.setText("27");
                PorterDuffColorFilter colorFilter27 = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                btnMesa27.getBackground().setColorFilter(colorFilter27);

                break;
            case R.id.btnMesa28:

                LimpaButtonMesa();
                txtMesa.setText("28");
                PorterDuffColorFilter colorFilter28 = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                btnMesa28.getBackground().setColorFilter(colorFilter28);

                break;
            case R.id.btnMesa29:

                LimpaButtonMesa();
                txtMesa.setText("29");
                PorterDuffColorFilter colorFilter29 = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                btnMesa29.getBackground().setColorFilter(colorFilter29);

                break;
            case R.id.btnMesa30:

                LimpaButtonMesa();
                txtMesa.setText("30");
                PorterDuffColorFilter colorFilter30 = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                btnMesa30.getBackground().setColorFilter(colorFilter30);

                break;
            case R.id.btnMesa31:

                LimpaButtonMesa();
                txtMesa.setText("31");
                PorterDuffColorFilter colorFilter31 = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                btnMesa31.getBackground().setColorFilter(colorFilter31);

                break;
            case R.id.btnMesa32:

                LimpaButtonMesa();
                txtMesa.setText("32");
                PorterDuffColorFilter colorFilter32 = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                btnMesa32.getBackground().setColorFilter(colorFilter32);

                break;
            case R.id.btnMesa33:

                LimpaButtonMesa();
                txtMesa.setText("33");
                PorterDuffColorFilter colorFilter33 = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                btnMesa33.getBackground().setColorFilter(colorFilter33);

                break;
            case R.id.btnMesa34:

                LimpaButtonMesa();
                txtMesa.setText("34");
                PorterDuffColorFilter colorFilter34 = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                btnMesa34.getBackground().setColorFilter(colorFilter34);

                break;
            case R.id.btnMesa35:

                LimpaButtonMesa();
                txtMesa.setText("35");
                PorterDuffColorFilter colorFilter35 = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                btnMesa35.getBackground().setColorFilter(colorFilter35);

                break;
            case R.id.btnMesa36:

                LimpaButtonMesa();
                txtMesa.setText("36");
                PorterDuffColorFilter colorFilter36 = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                btnMesa36.getBackground().setColorFilter(colorFilter36);

                break;
            case R.id.btnMesa37:

                LimpaButtonMesa();
                txtMesa.setText("37");
                PorterDuffColorFilter colorFilter37 = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                btnMesa37.getBackground().setColorFilter(colorFilter37);

                break;
            case R.id.btnMesa38:

                LimpaButtonMesa();
                txtMesa.setText("38");
                PorterDuffColorFilter colorFilter38 = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                btnMesa38.getBackground().setColorFilter(colorFilter38);

                break;
            case R.id.btnMesa39:

                LimpaButtonMesa();
                txtMesa.setText("39");
                PorterDuffColorFilter colorFilter39 = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                btnMesa39.getBackground().setColorFilter(colorFilter39);

                break;
            case R.id.btnMesa40:

                LimpaButtonMesa();
                txtMesa.setText("40");
                PorterDuffColorFilter colorFilter40 = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                btnMesa40.getBackground().setColorFilter(colorFilter40);

                break;
            case R.id.btnMesa41:

                LimpaButtonMesa();
                txtMesa.setText("41");
                PorterDuffColorFilter colorFilter41 = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                btnMesa41.getBackground().setColorFilter(colorFilter41);

                break;
            case R.id.btnMesa42:

                LimpaButtonMesa();
                txtMesa.setText("42");
                PorterDuffColorFilter colorFilter42 = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                btnMesa42.getBackground().setColorFilter(colorFilter42);

                break;
            case R.id.btnMesa43:

                LimpaButtonMesa();
                txtMesa.setText("43");
                PorterDuffColorFilter colorFilter43 = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                btnMesa43.getBackground().setColorFilter(colorFilter43);

                break;
            case R.id.btnMesa44:

                LimpaButtonMesa();
                txtMesa.setText("44");
                PorterDuffColorFilter colorFilter44 = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                btnMesa44.getBackground().setColorFilter(colorFilter44);

                break;
            case R.id.btnMesa45:

                LimpaButtonMesa();
                txtMesa.setText("45");
                PorterDuffColorFilter colorFilter45 = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                btnMesa45.getBackground().setColorFilter(colorFilter45);

                break;
            case R.id.btnMesa46:

                LimpaButtonMesa();
                txtMesa.setText("46");
                PorterDuffColorFilter colorFilter46 = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                btnMesa46.getBackground().setColorFilter(colorFilter46);

                break;
            case R.id.btnMesa47:

                LimpaButtonMesa();
                txtMesa.setText("47");
                PorterDuffColorFilter colorFilter47 = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                btnMesa47.getBackground().setColorFilter(colorFilter47);

                break;
            case R.id.btnMesa48:

                LimpaButtonMesa();
                txtMesa.setText("48");
                PorterDuffColorFilter colorFilter48 = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                btnMesa48.getBackground().setColorFilter(colorFilter48);

                break;
            case R.id.btnMesa49:

                LimpaButtonMesa();
                txtMesa.setText("49");
                PorterDuffColorFilter colorFilter49 = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                btnMesa49.getBackground().setColorFilter(colorFilter49);

                break;
            case R.id.btnMesa50:

                LimpaButtonMesa();
                txtMesa.setText("50");
                PorterDuffColorFilter colorFilter50 = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                btnMesa50.getBackground().setColorFilter(colorFilter50);

                break;
            case R.id.btnMesa51:

                LimpaButtonMesa();
                txtMesa.setText("51");
                PorterDuffColorFilter colorFilter51 = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                btnMesa51.getBackground().setColorFilter(colorFilter51);

                break;
            case R.id.btnMesa52:

                LimpaButtonMesa();
                txtMesa.setText("52");
                PorterDuffColorFilter colorFilter52 = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                btnMesa52.getBackground().setColorFilter(colorFilter52);

                break;
            case R.id.btnMesa53:

                LimpaButtonMesa();
                txtMesa.setText("53");
                PorterDuffColorFilter colorFilter53 = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                btnMesa53.getBackground().setColorFilter(colorFilter53);

                break;
            case R.id.btnMesa54:

                LimpaButtonMesa();
                txtMesa.setText("54");
                PorterDuffColorFilter colorFilter54 = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                btnMesa54.getBackground().setColorFilter(colorFilter54);

                break;
            case R.id.btnMesa55:

                LimpaButtonMesa();
                txtMesa.setText("55");
                PorterDuffColorFilter colorFilter55 = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                btnMesa55.getBackground().setColorFilter(colorFilter55);

                break;
            case R.id.btnMesa56:

                LimpaButtonMesa();
                txtMesa.setText("56");
                PorterDuffColorFilter colorFilter56 = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                btnMesa56.getBackground().setColorFilter(colorFilter56);

                break;
            case R.id.btnMesa57:

                LimpaButtonMesa();
                txtMesa.setText("57");
                PorterDuffColorFilter colorFilter57 = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                btnMesa57.getBackground().setColorFilter(colorFilter57);

                break;
            case R.id.btnMesa58:

                LimpaButtonMesa();
                txtMesa.setText("58");
                PorterDuffColorFilter colorFilter58 = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                btnMesa58.getBackground().setColorFilter(colorFilter58);

                break;
            case R.id.btnMesa59:

                LimpaButtonMesa();
                txtMesa.setText("59");
                PorterDuffColorFilter colorFilter59 = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                btnMesa59.getBackground().setColorFilter(colorFilter59);

                break;
            case R.id.btnMesa60:

                LimpaButtonMesa();
                txtMesa.setText("60");
                PorterDuffColorFilter colorFilter60 = new PorterDuffColorFilter(Color.GREEN, PorterDuff.Mode.MULTIPLY);
                btnMesa60.getBackground().setColorFilter(colorFilter60);

                break;

        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    private void LimpaButtonMesa() {

        if (btnMesa1.isEnabled() == true) {

            PorterDuffColorFilter colorFilter1 = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            btnMesa1.getBackground().setColorFilter(colorFilter1);
        }
        if (btnMesa2.isEnabled() == true) {

            PorterDuffColorFilter colorFilter2 = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            btnMesa2.getBackground().setColorFilter(colorFilter2);
        }
        if (btnMesa3.isEnabled() == true) {

            PorterDuffColorFilter colorFilter3 = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            btnMesa3.getBackground().setColorFilter(colorFilter3);
        }
        if (btnMesa4.isEnabled() == true) {

            PorterDuffColorFilter colorFilter4 = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            btnMesa4.getBackground().setColorFilter(colorFilter4);
        }
        if (btnMesa5.isEnabled() == true) {

            PorterDuffColorFilter colorFilter5 = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            btnMesa5.getBackground().setColorFilter(colorFilter5);
        }
        if (btnMesa6.isEnabled() == true) {

            PorterDuffColorFilter colorFilter6 = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            btnMesa6.getBackground().setColorFilter(colorFilter6);
        }
        if (btnMesa7.isEnabled() == true) {

            PorterDuffColorFilter colorFilter7 = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            btnMesa7.getBackground().setColorFilter(colorFilter7);
        }
        if (btnMesa8.isEnabled() == true) {

            PorterDuffColorFilter colorFilter8 = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            btnMesa8.getBackground().setColorFilter(colorFilter8);
        }
        if (btnMesa9.isEnabled() == true) {

            PorterDuffColorFilter colorFilter9 = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            btnMesa9.getBackground().setColorFilter(colorFilter9);
        }
        if (btnMesa10.isEnabled() == true) {

            PorterDuffColorFilter colorFilter0 = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            btnMesa10.getBackground().setColorFilter(colorFilter0);
        }
        if (btnMesa11.isEnabled() == true) {

            PorterDuffColorFilter colorFilter1 = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            btnMesa11.getBackground().setColorFilter(colorFilter1);
        }
        if (btnMesa12.isEnabled() == true) {

            PorterDuffColorFilter colorFilter2 = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            btnMesa12.getBackground().setColorFilter(colorFilter2);
        }
        if (btnMesa13.isEnabled() == true) {

            PorterDuffColorFilter colorFilter3 = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            btnMesa13.getBackground().setColorFilter(colorFilter3);
        }
        if (btnMesa14.isEnabled() == true) {

            PorterDuffColorFilter colorFilter4 = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            btnMesa14.getBackground().setColorFilter(colorFilter4);
        }
        if (btnMesa15.isEnabled() == true) {

            PorterDuffColorFilter colorFilter5 = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            btnMesa15.getBackground().setColorFilter(colorFilter5);
        }
        if (btnMesa16.isEnabled() == true) {

            PorterDuffColorFilter colorFilter6 = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            btnMesa16.getBackground().setColorFilter(colorFilter6);
        }
        if (btnMesa17.isEnabled() == true) {

            PorterDuffColorFilter colorFilter7 = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            btnMesa17.getBackground().setColorFilter(colorFilter7);
        }
        if (btnMesa18.isEnabled() == true) {

            PorterDuffColorFilter colorFilter8 = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            btnMesa18.getBackground().setColorFilter(colorFilter8);
        }
        if (btnMesa19.isEnabled() == true) {

            PorterDuffColorFilter colorFilter9 = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            btnMesa19.getBackground().setColorFilter(colorFilter9);
        }
        if (btnMesa20.isEnabled() == true) {

            PorterDuffColorFilter colorFilter0 = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            btnMesa20.getBackground().setColorFilter(colorFilter0);
        }
        if (btnMesa21.isEnabled() == true) {

            PorterDuffColorFilter colorFilter1 = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            btnMesa21.getBackground().setColorFilter(colorFilter1);
        }
        if (btnMesa22.isEnabled() == true) {

            PorterDuffColorFilter colorFilter2 = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            btnMesa22.getBackground().setColorFilter(colorFilter2);
        }
        if (btnMesa23.isEnabled() == true) {

            PorterDuffColorFilter colorFilter3 = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            btnMesa23.getBackground().setColorFilter(colorFilter3);
        }
        if (btnMesa24.isEnabled() == true) {

            PorterDuffColorFilter colorFilter4 = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            btnMesa24.getBackground().setColorFilter(colorFilter4);
        }
        if (btnMesa25.isEnabled() == true) {

            PorterDuffColorFilter colorFilter5 = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            btnMesa25.getBackground().setColorFilter(colorFilter5);
        }
        if (btnMesa26.isEnabled() == true) {

            PorterDuffColorFilter colorFilter6 = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            btnMesa26.getBackground().setColorFilter(colorFilter6);
        }
        if (btnMesa27.isEnabled() == true) {

            PorterDuffColorFilter colorFilter7 = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            btnMesa27.getBackground().setColorFilter(colorFilter7);
        }
        if (btnMesa28.isEnabled() == true) {

            PorterDuffColorFilter colorFilter8 = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            btnMesa28.getBackground().setColorFilter(colorFilter8);
        }
        if (btnMesa29.isEnabled() == true) {

            PorterDuffColorFilter colorFilter9 = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            btnMesa29.getBackground().setColorFilter(colorFilter9);
        }
        if (btnMesa30.isEnabled() == true) {

            PorterDuffColorFilter colorFilter0 = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            btnMesa30.getBackground().setColorFilter(colorFilter0);
        }
        if (btnMesa31.isEnabled() == true) {

            PorterDuffColorFilter colorFilter1 = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            btnMesa31.getBackground().setColorFilter(colorFilter1);
        }
        if (btnMesa32.isEnabled() == true) {

            PorterDuffColorFilter colorFilter2 = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            btnMesa32.getBackground().setColorFilter(colorFilter2);
        }
        if (btnMesa33.isEnabled() == true) {

            PorterDuffColorFilter colorFilter3 = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            btnMesa33.getBackground().setColorFilter(colorFilter3);
        }
        if (btnMesa34.isEnabled() == true) {

            PorterDuffColorFilter colorFilter4 = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            btnMesa34.getBackground().setColorFilter(colorFilter4);
        }
        if (btnMesa35.isEnabled() == true) {

            PorterDuffColorFilter colorFilter5 = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            btnMesa35.getBackground().setColorFilter(colorFilter5);
        }
        if (btnMesa36.isEnabled() == true) {

            PorterDuffColorFilter colorFilter6 = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            btnMesa36.getBackground().setColorFilter(colorFilter6);
        }
        if (btnMesa37.isEnabled() == true) {

            PorterDuffColorFilter colorFilter7 = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            btnMesa37.getBackground().setColorFilter(colorFilter7);
        }
        if (btnMesa38.isEnabled() == true) {

            PorterDuffColorFilter colorFilter8 = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            btnMesa38.getBackground().setColorFilter(colorFilter8);
        }
        if (btnMesa39.isEnabled() == true) {

            PorterDuffColorFilter colorFilter9 = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            btnMesa39.getBackground().setColorFilter(colorFilter9);
        }
        if (btnMesa40.isEnabled() == true) {

            PorterDuffColorFilter colorFilter0 = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            btnMesa40.getBackground().setColorFilter(colorFilter0);
        }
        if (btnMesa41.isEnabled() == true) {

            PorterDuffColorFilter colorFilter1 = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            btnMesa41.getBackground().setColorFilter(colorFilter1);
        }
        if (btnMesa42.isEnabled() == true) {

            PorterDuffColorFilter colorFilter2 = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            btnMesa42.getBackground().setColorFilter(colorFilter2);
        }
        if (btnMesa3.isEnabled() == true) {

            PorterDuffColorFilter colorFilter3 = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            btnMesa43.getBackground().setColorFilter(colorFilter3);
        }
        if (btnMesa44.isEnabled() == true) {

            PorterDuffColorFilter colorFilter4 = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            btnMesa44.getBackground().setColorFilter(colorFilter4);
        }
        if (btnMesa45.isEnabled() == true) {

            PorterDuffColorFilter colorFilter5 = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            btnMesa45.getBackground().setColorFilter(colorFilter5);
        }
        if (btnMesa46.isEnabled() == true) {

            PorterDuffColorFilter colorFilter6 = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            btnMesa46.getBackground().setColorFilter(colorFilter6);
        }
        if (btnMesa47.isEnabled() == true) {

            PorterDuffColorFilter colorFilter7 = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            btnMesa47.getBackground().setColorFilter(colorFilter7);
        }
        if (btnMesa48.isEnabled() == true) {

            PorterDuffColorFilter colorFilter8 = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            btnMesa48.getBackground().setColorFilter(colorFilter8);
        }
        if (btnMesa49.isEnabled() == true) {

            PorterDuffColorFilter colorFilter9 = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            btnMesa49.getBackground().setColorFilter(colorFilter9);
        }
        if (btnMesa50.isEnabled() == true) {

            PorterDuffColorFilter colorFilter0 = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            btnMesa50.getBackground().setColorFilter(colorFilter0);
        }
        if (btnMesa51.isEnabled() == true) {

            PorterDuffColorFilter colorFilter1 = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            btnMesa51.getBackground().setColorFilter(colorFilter1);
        }
        if (btnMesa52.isEnabled() == true) {

            PorterDuffColorFilter colorFilter2 = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            btnMesa52.getBackground().setColorFilter(colorFilter2);
        }
        if (btnMesa53.isEnabled() == true) {

            PorterDuffColorFilter colorFilter3 = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            btnMesa53.getBackground().setColorFilter(colorFilter3);
        }
        if (btnMesa54.isEnabled() == true) {

            PorterDuffColorFilter colorFilter4 = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            btnMesa54.getBackground().setColorFilter(colorFilter4);
        }
        if (btnMesa55.isEnabled() == true) {

            PorterDuffColorFilter colorFilter5 = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            btnMesa55.getBackground().setColorFilter(colorFilter5);
        }
        if (btnMesa56.isEnabled() == true) {

            PorterDuffColorFilter colorFilter6 = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            btnMesa56.getBackground().setColorFilter(colorFilter6);
        }
        if (btnMesa57.isEnabled() == true) {

            PorterDuffColorFilter colorFilter7 = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            btnMesa57.getBackground().setColorFilter(colorFilter7);
        }
        if (btnMesa58.isEnabled() == true) {

            PorterDuffColorFilter colorFilter8 = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            btnMesa58.getBackground().setColorFilter(colorFilter8);
        }
        if (btnMesa59.isEnabled() == true) {

            PorterDuffColorFilter colorFilter9 = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            btnMesa59.getBackground().setColorFilter(colorFilter9);
        }
        if (btnMesa60.isEnabled() == true) {

            PorterDuffColorFilter colorFilter0 = new PorterDuffColorFilter(Color.LTGRAY, PorterDuff.Mode.MULTIPLY);
            btnMesa60.getBackground().setColorFilter(colorFilter0);
        }
    }

    private void SalvaMesaSelecionada() {

        int mesa = Integer.parseInt(txtMesa.getText().toString());

        try {
            configuracao = adpConfiguracao.getItem(0);

            String ip = configuracao.getIp();
            String driver = configuracao.getDriver();
            String db = configuracao.getBancoDados();
            String usuario = configuracao.getUsuario();
            String senha = configuracao.getSenha();

            Connection con = connectionRemote.CONN(ip, driver, db, usuario, senha);

            if (con == null) {
                Toast.makeText(MesaActivity.this, "Erro ao Conectar o SQL server", Toast.LENGTH_SHORT).show();
            } else {

                String queryB = "Update Pedidos set Mesa='" + mesa + "' where Codigo =" + CodigoPedido;
                PreparedStatement preparedStatementB = con.prepareStatement(queryB);
                preparedStatementB.executeUpdate();

                con.close();
            }
        } catch (Exception ex) {

            Toast.makeText(MesaActivity.this, "erro " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void VerificaQuantidadeMesa() {

        btnMesa1.setVisibility(View.GONE);
        btnMesa2.setVisibility(View.GONE);
        btnMesa3.setVisibility(View.GONE);
        btnMesa4.setVisibility(View.GONE);
        btnMesa5.setVisibility(View.GONE);
        btnMesa6.setVisibility(View.GONE);
        btnMesa7.setVisibility(View.GONE);
        btnMesa8.setVisibility(View.GONE);
        btnMesa9.setVisibility(View.GONE);
        btnMesa10.setVisibility(View.GONE);
        btnMesa11.setVisibility(View.GONE);
        btnMesa12.setVisibility(View.GONE);
        btnMesa13.setVisibility(View.GONE);
        btnMesa14.setVisibility(View.GONE);
        btnMesa15.setVisibility(View.GONE);
        btnMesa16.setVisibility(View.GONE);
        btnMesa17.setVisibility(View.GONE);
        btnMesa18.setVisibility(View.GONE);
        btnMesa19.setVisibility(View.GONE);
        btnMesa20.setVisibility(View.GONE);
        btnMesa21.setVisibility(View.GONE);
        btnMesa22.setVisibility(View.GONE);
        btnMesa23.setVisibility(View.GONE);
        btnMesa24.setVisibility(View.GONE);
        btnMesa25.setVisibility(View.GONE);
        btnMesa26.setVisibility(View.GONE);
        btnMesa27.setVisibility(View.GONE);
        btnMesa28.setVisibility(View.GONE);
        btnMesa29.setVisibility(View.GONE);
        btnMesa30.setVisibility(View.GONE);
        btnMesa31.setVisibility(View.GONE);
        btnMesa32.setVisibility(View.GONE);
        btnMesa33.setVisibility(View.GONE);
        btnMesa34.setVisibility(View.GONE);
        btnMesa35.setVisibility(View.GONE);
        btnMesa36.setVisibility(View.GONE);
        btnMesa37.setVisibility(View.GONE);
        btnMesa38.setVisibility(View.GONE);
        btnMesa39.setVisibility(View.GONE);
        btnMesa40.setVisibility(View.GONE);
        btnMesa41.setVisibility(View.GONE);
        btnMesa42.setVisibility(View.GONE);
        btnMesa43.setVisibility(View.GONE);
        btnMesa44.setVisibility(View.GONE);
        btnMesa45.setVisibility(View.GONE);
        btnMesa46.setVisibility(View.GONE);
        btnMesa47.setVisibility(View.GONE);
        btnMesa48.setVisibility(View.GONE);
        btnMesa49.setVisibility(View.GONE);
        btnMesa50.setVisibility(View.GONE);
        btnMesa51.setVisibility(View.GONE);
        btnMesa52.setVisibility(View.GONE);
        btnMesa53.setVisibility(View.GONE);
        btnMesa54.setVisibility(View.GONE);
        btnMesa55.setVisibility(View.GONE);
        btnMesa56.setVisibility(View.GONE);
        btnMesa57.setVisibility(View.GONE);
        btnMesa58.setVisibility(View.GONE);
        btnMesa59.setVisibility(View.GONE);
        btnMesa60.setVisibility(View.GONE);

        PesquisaQtdMesa();

        if (QuantidadeMesa >= 1) {
            btnMesa1.setVisibility(View.VISIBLE);
        }
        if (QuantidadeMesa >= 2) {
            btnMesa2.setVisibility(View.VISIBLE);
        }
        if (QuantidadeMesa >= 3) {
            btnMesa3.setVisibility(View.VISIBLE);
        }
        if (QuantidadeMesa >= 4) {
            btnMesa4.setVisibility(View.VISIBLE);
        }
        if (QuantidadeMesa >= 5) {
            btnMesa5.setVisibility(View.VISIBLE);
        }
        if (QuantidadeMesa >= 6) {
            btnMesa6.setVisibility(View.VISIBLE);
        }
        if (QuantidadeMesa >= 7) {
            btnMesa7.setVisibility(View.VISIBLE);
        }
        if (QuantidadeMesa >= 8) {
            btnMesa8.setVisibility(View.VISIBLE);
        }
        if (QuantidadeMesa >= 9) {
            btnMesa9.setVisibility(View.VISIBLE);
        }
        if (QuantidadeMesa >= 10) {
            btnMesa10.setVisibility(View.VISIBLE);
        }
        if (QuantidadeMesa >= 11) {
            btnMesa11.setVisibility(View.VISIBLE);
        }
        if (QuantidadeMesa >= 12) {
            btnMesa12.setVisibility(View.VISIBLE);
        }
        if (QuantidadeMesa >= 13) {
            btnMesa13.setVisibility(View.VISIBLE);
        }
        if (QuantidadeMesa >= 14) {
            btnMesa14.setVisibility(View.VISIBLE);
        }
        if (QuantidadeMesa >= 15) {
            btnMesa15.setVisibility(View.VISIBLE);
        }
        if (QuantidadeMesa >= 16) {
            btnMesa16.setVisibility(View.VISIBLE);
        }
        if (QuantidadeMesa >= 17) {
            btnMesa17.setVisibility(View.VISIBLE);
        }
        if (QuantidadeMesa >= 18) {
            btnMesa18.setVisibility(View.VISIBLE);
        }
        if (QuantidadeMesa >= 19) {
            btnMesa19.setVisibility(View.VISIBLE);
        }
        if (QuantidadeMesa >= 20) {
            btnMesa20.setVisibility(View.VISIBLE);
        }
        if (QuantidadeMesa >= 21) {
            btnMesa21.setVisibility(View.VISIBLE);
        }
        if (QuantidadeMesa >= 22) {
            btnMesa22.setVisibility(View.VISIBLE);
        }
        if (QuantidadeMesa >= 23) {
            btnMesa23.setVisibility(View.VISIBLE);
        }
        if (QuantidadeMesa >= 24) {
            btnMesa24.setVisibility(View.VISIBLE);
        }
        if (QuantidadeMesa >= 25) {
            btnMesa25.setVisibility(View.VISIBLE);
        }
        if (QuantidadeMesa >= 26) {
            btnMesa26.setVisibility(View.VISIBLE);
        }
        if (QuantidadeMesa >= 27) {
            btnMesa27.setVisibility(View.VISIBLE);
        }
        if (QuantidadeMesa >= 28) {
            btnMesa28.setVisibility(View.VISIBLE);
        }
        if (QuantidadeMesa >= 29) {
            btnMesa29.setVisibility(View.VISIBLE);
        }
        if (QuantidadeMesa >= 30) {
            btnMesa30.setVisibility(View.VISIBLE);
        }
        if (QuantidadeMesa >= 31) {
            btnMesa31.setVisibility(View.VISIBLE);
        }
        if (QuantidadeMesa >= 32) {
            btnMesa32.setVisibility(View.VISIBLE);
        }
        if (QuantidadeMesa >= 33) {
            btnMesa33.setVisibility(View.VISIBLE);
        }
        if (QuantidadeMesa >= 34) {
            btnMesa34.setVisibility(View.VISIBLE);
        }
        if (QuantidadeMesa >= 35) {
            btnMesa35.setVisibility(View.VISIBLE);
        }
        if (QuantidadeMesa >= 36) {
            btnMesa36.setVisibility(View.VISIBLE);
        }
        if (QuantidadeMesa >= 37) {
            btnMesa37.setVisibility(View.VISIBLE);
        }
        if (QuantidadeMesa >= 38) {
            btnMesa38.setVisibility(View.VISIBLE);
        }
        if (QuantidadeMesa >= 39) {
            btnMesa39.setVisibility(View.VISIBLE);
        }
        if (QuantidadeMesa >= 40) {
            btnMesa40.setVisibility(View.VISIBLE);
        }
        if (QuantidadeMesa >= 41) {
            btnMesa41.setVisibility(View.VISIBLE);
        }
        if (QuantidadeMesa >= 42) {
            btnMesa42.setVisibility(View.VISIBLE);
        }
        if (QuantidadeMesa >= 43) {
            btnMesa43.setVisibility(View.VISIBLE);
        }
        if (QuantidadeMesa >= 44) {
            btnMesa44.setVisibility(View.VISIBLE);
        }
        if (QuantidadeMesa >= 45) {
            btnMesa45.setVisibility(View.VISIBLE);
        }
        if (QuantidadeMesa >= 46) {
            btnMesa46.setVisibility(View.VISIBLE);
        }
        if (QuantidadeMesa >= 47) {
            btnMesa47.setVisibility(View.VISIBLE);
        }
        if (QuantidadeMesa >= 48) {
            btnMesa48.setVisibility(View.VISIBLE);
        }
        if (QuantidadeMesa >= 49) {
            btnMesa49.setVisibility(View.VISIBLE);
        }
        if (QuantidadeMesa >= 50) {
            btnMesa50.setVisibility(View.VISIBLE);
        }
        if (QuantidadeMesa >= 51) {
            btnMesa51.setVisibility(View.VISIBLE);
        }
        if (QuantidadeMesa >= 52) {
            btnMesa52.setVisibility(View.VISIBLE);
        }
        if (QuantidadeMesa >= 53) {
            btnMesa53.setVisibility(View.VISIBLE);
        }
        if (QuantidadeMesa >= 54) {
            btnMesa54.setVisibility(View.VISIBLE);
        }
        if (QuantidadeMesa >= 55) {
            btnMesa55.setVisibility(View.VISIBLE);
        }
        if (QuantidadeMesa >= 56) {
            btnMesa56.setVisibility(View.VISIBLE);
        }
        if (QuantidadeMesa >= 57) {
            btnMesa57.setVisibility(View.VISIBLE);
        }
        if (QuantidadeMesa >= 58) {
            btnMesa58.setVisibility(View.VISIBLE);
        }
        if (QuantidadeMesa >= 59) {
            btnMesa59.setVisibility(View.VISIBLE);
        }
        if (QuantidadeMesa >= 60) {
            btnMesa60.setVisibility(View.VISIBLE);
        }
    }

    private void PesquisaQtdMesa() {

        try {
            configuracao = adpConfiguracao.getItem(0);

            String ip = configuracao.getIp();
            String driver = configuracao.getDriver();
            String db = configuracao.getBancoDados();
            String usuario = configuracao.getUsuario();
            String senha = configuracao.getSenha();

            Connection con = connectionRemote.CONN(ip, driver, db, usuario, senha);

            if (con == null) {
                Toast.makeText(MesaActivity.this, "Error in connection with SQL server", Toast.LENGTH_SHORT).show();
            } else {

                String query = "Select Mesa From Empresas";

                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {

                    QuantidadeMesa = rs.getInt("Mesa");
                }

                con.close();
            }
        } catch (Exception ex) {
            Toast.makeText(MesaActivity.this, "erro " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void finish() {

        super.finish();
    }

    public class VerificaStatusMesa extends AsyncTask<String, String, String> {
        String z = "";

        ArrayList<String> MesasPedido = new ArrayList<>();

        @Override
        protected void onPreExecute() {

            //  progressBarPedidosEdit.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {

            Toast.makeText(MesaActivity.this, r, Toast.LENGTH_SHORT).show();

            String mesa = "";

            for (String lista : MesasPedido) {

                mesa = lista;

                switch (mesa) {

                    case "1":

                        PorterDuffColorFilter colorFilter1 = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        btnMesa1.getBackground().setColorFilter(colorFilter1);
                        btnMesa1.setEnabled(false);

                        break;
                    case "2":

                        PorterDuffColorFilter colorFilter2 = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        btnMesa2.getBackground().setColorFilter(colorFilter2);
                        btnMesa2.setEnabled(false);

                        break;
                    case "3":

                        PorterDuffColorFilter colorFilter3 = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        btnMesa3.getBackground().setColorFilter(colorFilter3);
                        btnMesa3.setEnabled(false);

                        break;
                    case "4":

                        PorterDuffColorFilter colorFilter4 = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        btnMesa4.getBackground().setColorFilter(colorFilter4);
                        btnMesa4.setEnabled(false);

                        break;
                    case "5":

                        PorterDuffColorFilter colorFilter5 = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        btnMesa5.getBackground().setColorFilter(colorFilter5);
                        btnMesa5.setEnabled(false);

                        break;
                    case "6":

                        PorterDuffColorFilter colorFilter6 = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        btnMesa6.getBackground().setColorFilter(colorFilter6);
                        btnMesa6.setEnabled(false);

                        break;
                    case "7":

                        PorterDuffColorFilter colorFilter7 = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        btnMesa7.getBackground().setColorFilter(colorFilter7);
                        btnMesa7.setEnabled(false);

                        break;
                    case "8":

                        PorterDuffColorFilter colorFilter8 = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        btnMesa8.getBackground().setColorFilter(colorFilter8);
                        btnMesa8.setEnabled(false);

                        break;
                    case "9":

                        PorterDuffColorFilter colorFilter9 = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        btnMesa9.getBackground().setColorFilter(colorFilter9);
                        btnMesa9.setEnabled(false);

                        break;
                    case "10":

                        PorterDuffColorFilter colorFilter10 = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        btnMesa10.getBackground().setColorFilter(colorFilter10);
                        btnMesa10.setEnabled(false);

                        break;
                    case "11":

                        PorterDuffColorFilter colorFilter11 = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        btnMesa11.getBackground().setColorFilter(colorFilter11);
                        btnMesa11.setEnabled(false);

                        break;
                    case "12":

                        PorterDuffColorFilter colorFilter12 = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        btnMesa12.getBackground().setColorFilter(colorFilter12);
                        btnMesa12.setEnabled(false);

                        break;
                    case "13":

                        PorterDuffColorFilter colorFilter13 = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        btnMesa13.getBackground().setColorFilter(colorFilter13);
                        btnMesa13.setEnabled(false);

                        break;
                    case "14":

                        PorterDuffColorFilter colorFilter14 = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        btnMesa14.getBackground().setColorFilter(colorFilter14);
                        btnMesa14.setEnabled(false);

                        break;
                    case "15":

                        PorterDuffColorFilter colorFilter15 = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        btnMesa15.getBackground().setColorFilter(colorFilter15);
                        btnMesa15.setEnabled(false);

                        break;
                    case "16":

                        PorterDuffColorFilter colorFilter16 = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        btnMesa16.getBackground().setColorFilter(colorFilter16);
                        btnMesa16.setEnabled(false);

                        break;
                    case "17":

                        PorterDuffColorFilter colorFilter17 = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        btnMesa17.getBackground().setColorFilter(colorFilter17);
                        btnMesa17.setEnabled(false);

                        break;
                    case "18":

                        PorterDuffColorFilter colorFilter18 = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        btnMesa18.getBackground().setColorFilter(colorFilter18);
                        btnMesa18.setEnabled(false);

                        break;
                    case "19":

                        PorterDuffColorFilter colorFilter19 = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        btnMesa19.getBackground().setColorFilter(colorFilter19);
                        btnMesa19.setEnabled(false);

                        break;
                    case "20":

                        PorterDuffColorFilter colorFilter20 = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        btnMesa20.getBackground().setColorFilter(colorFilter20);
                        btnMesa20.setEnabled(false);

                        break;
                    case "21":

                        PorterDuffColorFilter colorFilter21 = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        btnMesa21.getBackground().setColorFilter(colorFilter21);
                        btnMesa21.setEnabled(false);

                        break;
                    case "22":

                        PorterDuffColorFilter colorFilter22 = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        btnMesa22.getBackground().setColorFilter(colorFilter22);
                        btnMesa22.setEnabled(false);

                        break;
                    case "23":

                        PorterDuffColorFilter colorFilter23 = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        btnMesa23.getBackground().setColorFilter(colorFilter23);
                        btnMesa23.setEnabled(false);

                        break;
                    case "24":

                        PorterDuffColorFilter colorFilter24 = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        btnMesa24.getBackground().setColorFilter(colorFilter24);
                        btnMesa24.setEnabled(false);

                        break;
                    case "25":

                        PorterDuffColorFilter colorFilter25 = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        btnMesa25.getBackground().setColorFilter(colorFilter25);
                        btnMesa25.setEnabled(false);

                        break;
                    case "26":

                        PorterDuffColorFilter colorFilter26 = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        btnMesa26.getBackground().setColorFilter(colorFilter26);
                        btnMesa26.setEnabled(false);

                        break;
                    case "27":

                        PorterDuffColorFilter colorFilter27 = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        btnMesa27.getBackground().setColorFilter(colorFilter27);
                        btnMesa27.setEnabled(false);

                        break;
                    case "28":

                        PorterDuffColorFilter colorFilter28 = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        btnMesa28.getBackground().setColorFilter(colorFilter28);
                        btnMesa28.setEnabled(false);

                        break;
                    case "29":

                        PorterDuffColorFilter colorFilter29 = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        btnMesa29.getBackground().setColorFilter(colorFilter29);
                        btnMesa29.setEnabled(false);

                        break;
                    case "30":

                        PorterDuffColorFilter colorFilter30 = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        btnMesa30.getBackground().setColorFilter(colorFilter30);
                        btnMesa30.setEnabled(false);

                        break;
                    case "31":

                        PorterDuffColorFilter colorFilter31 = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        btnMesa31.getBackground().setColorFilter(colorFilter31);
                        btnMesa31.setEnabled(false);

                        break;
                    case "32":

                        PorterDuffColorFilter colorFilter32 = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        btnMesa32.getBackground().setColorFilter(colorFilter32);
                        btnMesa32.setEnabled(false);

                        break;
                    case "33":

                        PorterDuffColorFilter colorFilter33 = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        btnMesa33.getBackground().setColorFilter(colorFilter33);
                        btnMesa33.setEnabled(false);

                        break;
                    case "34":

                        PorterDuffColorFilter colorFilter34 = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        btnMesa34.getBackground().setColorFilter(colorFilter34);
                        btnMesa34.setEnabled(false);

                        break;
                    case "35":

                        PorterDuffColorFilter colorFilter35 = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        btnMesa35.getBackground().setColorFilter(colorFilter35);
                        btnMesa35.setEnabled(false);

                        break;
                    case "36":

                        PorterDuffColorFilter colorFilter36 = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        btnMesa36.getBackground().setColorFilter(colorFilter36);
                        btnMesa36.setEnabled(false);

                        break;
                    case "37":

                        PorterDuffColorFilter colorFilter37 = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        btnMesa37.getBackground().setColorFilter(colorFilter37);
                        btnMesa37.setEnabled(false);

                        break;
                    case "38":

                        PorterDuffColorFilter colorFilter38 = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        btnMesa38.getBackground().setColorFilter(colorFilter38);
                        btnMesa38.setEnabled(false);

                        break;
                    case "39":

                        PorterDuffColorFilter colorFilter39 = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        btnMesa39.getBackground().setColorFilter(colorFilter39);
                        btnMesa39.setEnabled(false);

                        break;
                    case "40":

                        PorterDuffColorFilter colorFilter40 = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        btnMesa40.getBackground().setColorFilter(colorFilter40);
                        btnMesa40.setEnabled(false);

                        break;
                    case "41":

                        PorterDuffColorFilter colorFilter41 = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        btnMesa41.getBackground().setColorFilter(colorFilter41);
                        btnMesa41.setEnabled(false);

                        break;
                    case "42":

                        PorterDuffColorFilter colorFilter42 = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        btnMesa42.getBackground().setColorFilter(colorFilter42);
                        btnMesa42.setEnabled(false);

                        break;
                    case "43":

                        PorterDuffColorFilter colorFilter43 = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        btnMesa43.getBackground().setColorFilter(colorFilter43);
                        btnMesa43.setEnabled(false);

                        break;
                    case "44":

                        PorterDuffColorFilter colorFilter44 = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        btnMesa44.getBackground().setColorFilter(colorFilter44);
                        btnMesa44.setEnabled(false);

                        break;
                    case "45":

                        PorterDuffColorFilter colorFilter45 = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        btnMesa45.getBackground().setColorFilter(colorFilter45);
                        btnMesa45.setEnabled(false);

                        break;
                    case "46":

                        PorterDuffColorFilter colorFilter46 = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        btnMesa46.getBackground().setColorFilter(colorFilter46);
                        btnMesa46.setEnabled(false);

                        break;
                    case "47":

                        PorterDuffColorFilter colorFilter47 = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        btnMesa47.getBackground().setColorFilter(colorFilter47);
                        btnMesa47.setEnabled(false);

                        break;
                    case "48":

                        PorterDuffColorFilter colorFilter48 = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        btnMesa48.getBackground().setColorFilter(colorFilter48);
                        btnMesa48.setEnabled(false);

                        break;
                    case "49":

                        PorterDuffColorFilter colorFilter49 = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        btnMesa49.getBackground().setColorFilter(colorFilter49);
                        btnMesa49.setEnabled(false);

                        break;
                    case "50":

                        PorterDuffColorFilter colorFilter50 = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        btnMesa50.getBackground().setColorFilter(colorFilter50);
                        btnMesa50.setEnabled(false);

                        break;
                    case "51":

                        PorterDuffColorFilter colorFilter51 = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        btnMesa51.getBackground().setColorFilter(colorFilter51);
                        btnMesa51.setEnabled(false);

                        break;
                    case "52":

                        PorterDuffColorFilter colorFilter52 = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        btnMesa52.getBackground().setColorFilter(colorFilter52);
                        btnMesa52.setEnabled(false);

                        break;
                    case "53":

                        PorterDuffColorFilter colorFilter53 = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        btnMesa53.getBackground().setColorFilter(colorFilter53);
                        btnMesa53.setEnabled(false);

                        break;
                    case "54":

                        PorterDuffColorFilter colorFilter54 = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        btnMesa54.getBackground().setColorFilter(colorFilter54);
                        btnMesa54.setEnabled(false);

                        break;
                    case "55":

                        PorterDuffColorFilter colorFilter55 = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        btnMesa55.getBackground().setColorFilter(colorFilter55);
                        btnMesa55.setEnabled(false);

                        break;
                    case "56":

                        PorterDuffColorFilter colorFilter56 = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        btnMesa56.getBackground().setColorFilter(colorFilter56);
                        btnMesa56.setEnabled(false);

                        break;
                    case "57":

                        PorterDuffColorFilter colorFilter57 = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        btnMesa57.getBackground().setColorFilter(colorFilter57);
                        btnMesa57.setEnabled(false);

                        break;
                    case "58":

                        PorterDuffColorFilter colorFilter58 = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        btnMesa58.getBackground().setColorFilter(colorFilter58);
                        btnMesa58.setEnabled(false);

                        break;
                    case "59":

                        PorterDuffColorFilter colorFilter59 = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        btnMesa59.getBackground().setColorFilter(colorFilter59);
                        btnMesa59.setEnabled(false);

                        break;
                    case "60":

                        PorterDuffColorFilter colorFilter60 = new PorterDuffColorFilter(Color.RED, PorterDuff.Mode.MULTIPLY);
                        btnMesa60.getBackground().setColorFilter(colorFilter60);
                        btnMesa60.setEnabled(false);

                        break;
                }
            }
        }

        @Override
        protected String doInBackground(String... params) {

            try {

                configuracao = adpConfiguracao.getItem(0);

                String ip = configuracao.getIp();
                String driver = configuracao.getDriver();
                String db = configuracao.getBancoDados();
                String usuario = configuracao.getUsuario();
                String senha = configuracao.getSenha();

                Connection con = connectionRemote.CONN(ip, driver, db, usuario, senha);

                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {

                    String query = "Select Mesa From Pedidos Where FecharConta = 0";

                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {

                        MesasPedido.add(rs.getString("Mesa"));
                    }

                    con.close();

                    z = "Success";
                }
            } catch (Exception ex) {
                z = "Error retrieving data from table" + ex.getMessage();

            }
            return z;
        }
    }
}
