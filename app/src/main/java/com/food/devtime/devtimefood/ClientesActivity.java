package com.food.devtime.devtimefood;

import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
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

public class ClientesActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener {

    ConnectionRemote connectionRemote;
    String proid;
    private ListView listViewClientes;
    private ProgressBar progressBarClientes;
    private ArrayAdapter<Configuracao> adpConfiguracao;
    private com.food.devtime.devtimefood.Database.database database;
    private SQLiteDatabase connLocal;
    private RepositorioDevtimeFood repositorioDevtimeFood;
    private Configuracao configuracao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente_toolbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FillList fillList = new FillList();
                fillList.execute("");

                Snackbar.make(view, "Atualizado !", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        connectionRemote = new ConnectionRemote();

        progressBarClientes = (ProgressBar) findViewById(R.id.progressBarClientes);
        progressBarClientes.setVisibility(View.GONE);
        listViewClientes = (ListView) findViewById(R.id.listViewClientes);
        // listViewClientes.setOnClickListener(this);TESTE

        proid = "";

        database = new database(this);
        connLocal = database.getWritableDatabase();

        repositorioDevtimeFood = new RepositorioDevtimeFood(connLocal);

        adpConfiguracao = repositorioDevtimeFood.buscaConfiguracao(this);

        FillList fillList = new FillList();
        fillList.execute("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cliente, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_ordenar_nome) {
            return true;
        } else if (id == R.id.action_ordenar_codigo) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onClick(View view) {

    }

    public class FillList extends AsyncTask<String, String, String> {
        String z = "";

        List<Map<String, String>> prolist = new ArrayList<Map<String, String>>();

        @Override
        protected void onPreExecute() {

            progressBarClientes.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {

            progressBarClientes.setVisibility(View.GONE);
            Toast.makeText(ClientesActivity.this, r, Toast.LENGTH_SHORT).show();

            //String[] from = { "A", "B", "C" };
            String[] from = {"A", "B", "C", "D"};
            int[] views = {R.id.lblCodigoCli, R.id.lblNomeCli, R.id.lblEnderecoCli, R.id.lblCPFCli};
            final SimpleAdapter ADA = new SimpleAdapter(ClientesActivity.this,
                    prolist, R.layout.activity_cliente_view, from,
                    views);
            listViewClientes.setAdapter(ADA);


            listViewClientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
                    HashMap<String, Object> obj = (HashMap<String, Object>) ADA
                            .getItem(arg2);
                    proid = (String) obj.get("A");
                    String proname = (String) obj.get("B");
                    //String prodesc = (String) obj.get("C");
                    //  edtprodesc.setText(prodesc);
                    // edtproname.setText(proname);
                    //qty.setText(qtys);
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
                    String query = "Select * From Clientes";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    ArrayList<String> data1 = new ArrayList<String>();
                    while (rs.next()) {
                        Map<String, String> datanum = new HashMap<String, String>();
                        datanum.put("A", "Codigo: " + rs.getString("Codigo"));
                        datanum.put("B", "Nome: " + rs.getString("Nome"));
                        datanum.put("C", "Endereço: " + rs.getString("Endereco"));
                        datanum.put("D", "CPF: " + rs.getString("NroInsc"));
                        //  datanum.put("C", rs.getString("ProDesc"));
                        prolist.add(datanum);
                    }


                    z = "Success";
                }
            } catch (Exception ex) {
                z = "Error retrieving data from table" + ex.getMessage();

            }
            return z;
        }
    }

}
