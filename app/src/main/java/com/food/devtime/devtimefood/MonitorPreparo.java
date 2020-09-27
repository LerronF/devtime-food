package com.food.devtime.devtimefood;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.ColorRes;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class MonitorPreparo extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, AdapterView.OnItemClickListener {

    protected int QuantidadeMesa = 0;
    ConnectionRemote connectionRemote;
    Timer timer;
    MyTimerTask myTimerTask;
    private ArrayAdapter<Configuracao> adpConfiguracao;
    private com.food.devtime.devtimefood.Database.database database;
    private SQLiteDatabase connLocal;
    private RepositorioDevtimeFood repositorioDevtimeFood;
    private Configuracao configuracao;
    private long lastBackPressTime = 0;
    private Toast toast;
    private ListView listViewPreparoA, listViewPreparoB, listViewPreparoC;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitor_preparo);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        connectionRemote = new ConnectionRemote();

        //region CARREGA AS CONFIGURACOES PARA A CONEXAO
        try {

            database = new database(this);
            connLocal = database.getWritableDatabase();
            repositorioDevtimeFood = new RepositorioDevtimeFood(connLocal);
            adpConfiguracao = repositorioDevtimeFood.buscaConfiguracao(this);

        } catch (Exception e) {

            Toast.makeText(MonitorPreparo.this, "Erro na configuracao", Toast.LENGTH_SHORT).show();
        }

        listViewPreparoA = (ListView) findViewById(R.id.listViewPreparoA);
        listViewPreparoB = (ListView) findViewById(R.id.listViewPreparoB);
        listViewPreparoC = (ListView) findViewById(R.id.listViewPreparoC);

        listViewPreparoA.setOnItemClickListener(this);

        timer = new Timer();

        myTimerTask = new MyTimerTask();

        timer.schedule(myTimerTask, 1000, 3000);


        FillList fillList = new FillList();
        fillList.execute("");

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

            timer.cancel();
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }

    @Override
    public void finish() {

        super.finish();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            //  case R.id.btnIniciar:

            //  Toast.makeText(MonitorPreparo.this, "Iniciar", Toast.LENGTH_SHORT).show();

            // break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    private void AtualizaMonitorPreparo() {

        try {
            configuracao = adpConfiguracao.getItem(0);

            String ip = configuracao.getIp();
            String driver = configuracao.getDriver();
            String db = configuracao.getBancoDados();
            String usuario = configuracao.getUsuario();
            String senha = configuracao.getSenha();

            Connection con = connectionRemote.CONN(ip, driver, db, usuario, senha);

            if (con == null) {
                Toast.makeText(MonitorPreparo.this, "Error in connection with SQL server", Toast.LENGTH_SHORT).show();
            } else {

                String query = "SELECT Codigo, " +
                        "Pedido, " +
                        "Mesa, " +
                        "Atendente, " +
                        "Status " +
                        "FROM MonitorPreparo " +
                        "WHERE Status = 'A'"; //A - ABERTO

                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {

                    QuantidadeMesa = rs.getInt("Mesa");



                }

                con.close();
            }
        } catch (Exception ex) {

            Toast.makeText(MonitorPreparo.this, "erro " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    class MyTimerTask extends TimerTask {

        @Override
        public void run() {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    AtualizaMonitorPreparo();
                }
            });
        }
    }

    public class FillList extends AsyncTask<String, String, String> {
        String z = "";

        List<Map<String, String>> prolistA = new ArrayList<Map<String, String>>();
        List<Map<String, String>> prolistB = new ArrayList<Map<String, String>>();
        List<Map<String, String>> prolistC = new ArrayList<Map<String, String>>();

        @Override
        protected void onPreExecute() {

            // progressBarClientes.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {

            Toast.makeText(MonitorPreparo.this, r, Toast.LENGTH_SHORT).show();

            String[] from = {"A", "B", "C", "D", "E", "F"};
            int[] viewsA = {R.id.lblCodigo, R.id.lblPedido, R.id.lblMesa, R.id.lblAtendente, R.id.lblObservacao, R.id.lblTempo};
            final SimpleAdapter ADA = new SimpleAdapter(MonitorPreparo.this,
                    prolistA, R.layout.monitor_preparo_listview, from,
                    viewsA);
            listViewPreparoA.setAdapter(ADA);

            int[] viewsB = {R.id.lblCodigo, R.id.lblPedido, R.id.lblMesa, R.id.lblAtendente, R.id.lblObservacao, R.id.lblTempo};
            final SimpleAdapter ADAB = new SimpleAdapter(MonitorPreparo.this,
                    prolistB, R.layout.monitor_preparo_listview, from,
                    viewsB);
            listViewPreparoB.setAdapter(ADAB);

            int[] viewsC = {R.id.lblCodigo, R.id.lblPedido, R.id.lblMesa, R.id.lblAtendente, R.id.lblObservacao, R.id.lblTempo};
            final SimpleAdapter ADAC = new SimpleAdapter(MonitorPreparo.this,
                    prolistC, R.layout.monitor_preparo_listview, from,
                    viewsC);
            listViewPreparoC.setAdapter(ADAC);

            listViewPreparoA.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
                    HashMap<String, Object> obj = (HashMap<String, Object>) ADA
                            .getItem(arg2);
                    // proid = (String) obj.get("A");
                    String produto = (String) obj.get("B");
                    //String prodesc = (String) obj.get("C");
                    //  edtprodesc.setText(prodesc);
                    // edtproname.setText(proname);
                    //qty.setText(qtys);

                    Toast.makeText(MonitorPreparo.this, "Pedido " + produto, Toast.LENGTH_SHORT).show();
                }
            });

            listViewPreparoB.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
                    HashMap<String, Object> obj = (HashMap<String, Object>) ADAB
                            .getItem(arg2);

                    String produto = (String) obj.get("B");

                    Toast.makeText(MonitorPreparo.this, "Pedido " + produto, Toast.LENGTH_SHORT).show();
                }
            });

            listViewPreparoC.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
                    HashMap<String, Object> obj = (HashMap<String, Object>) ADAC
                            .getItem(arg2);

                    String produto = (String) obj.get("B");

                    Toast.makeText(MonitorPreparo.this, "Pedido " + produto, Toast.LENGTH_SHORT).show();

                    // listViewPreparoC.setBackground();
                }
            });

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

                    boolean list = true;
                    boolean listC = true;

                    String query = "SELECT MP.Codigo," +
                            "MP.Pedido, " +
                            "MP.Mesa, " +
                            "A.Nome as Atendente," +
                            "MP.Status, " +
                            "P.Observacao " +
                            "FROM MonitorPreparo MP " +
                            "INNER JOIN Pedidos P on P.codigo = MP.Pedido " +
                            "INNER JOIN Atendente A on A.Codigo = MP.Atendente " +
                            "WHERE MP.Status = 'A'"; //P - PREPARO

                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {
                        Map<String, String> datanum = new HashMap<String, String>();
                        datanum.put("A", "Codigo: " + rs.getString("Codigo"));
                        datanum.put("B", "Pedido: " + rs.getString("Pedido"));
                        datanum.put("C", "Mesa: " + rs.getString("Mesa"));
                        datanum.put("D", "Atendente: " + rs.getString("Atendente"));
                        datanum.put("E", "Observação: " + rs.getString("Observacao"));
                        datanum.put("F", "15:00");


                        if (list && listC) {

                            prolistA.add(datanum);

                            list = false;
                            listC = false;

                        } else if (!list && !listC) {

                            list = true;

                            prolistB.add(datanum);
                        } else {

                            list = true;
                            listC = true;

                            prolistC.add(datanum);
                        }
                    }


                    //z = "Success";
                }
            } catch (Exception ex) {
                z = "Error retrieving data from table" + ex.getMessage();

            }
            return z;
        }
    }
}
