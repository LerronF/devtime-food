    package com.food.devtime.devtimefood;

    import android.app.AlertDialog;
    import android.content.DialogInterface;
    import android.content.Intent;
    import android.database.sqlite.SQLiteDatabase;
    import android.os.AsyncTask;
    import android.os.Bundle;
    import android.support.design.widget.NavigationView;
    import android.support.v4.view.GravityCompat;
    import android.support.v4.widget.DrawerLayout;
    import android.support.v7.app.ActionBarDrawerToggle;
    import android.support.v7.app.AppCompatActivity;
    import android.support.v7.widget.Toolbar;
    import android.view.ContextMenu;
    import android.view.Menu;
    import android.view.MenuInflater;
    import android.view.MenuItem;
    import android.view.View;
    import android.widget.AdapterView;
    import android.widget.ArrayAdapter;
    import android.widget.ListView;
    import android.widget.ProgressBar;
    import android.widget.SimpleAdapter;
    import android.widget.TextView;
    import android.widget.Toast;

    import com.food.devtime.devtimefood.Database.ConnectionRemote;
    import com.food.devtime.devtimefood.Database.database;
    import com.food.devtime.devtimefood.Dominio.Entidade.Configuracao;
    import com.food.devtime.devtimefood.Dominio.RepositorioDevtimeFood;
    import com.github.clans.fab.FloatingActionMenu;

    import java.sql.Connection;
    import java.sql.Date;
    import java.sql.PreparedStatement;
    import java.sql.ResultSet;
    import java.sql.Statement;
    import java.text.SimpleDateFormat;
    import java.util.ArrayList;
    import java.util.Calendar;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Map;
    import java.util.Timer;
    import java.util.TimerTask;

    public class MenuActivity extends AppCompatActivity
            implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, AdapterView.OnItemClickListener {

        ConnectionRemote connectionRemote;
        String proid;
        FloatingActionMenu materialDesignFAM;
        com.github.clans.fab.FloatingActionButton floatingActionButton1, floatingActionButton2;
        Timer timer;
        MyTimerTask myTimerTask;
        private TextView txtAtendente, lblCodigoPedido, lblObservacao;
        private ListView listViewPedidos;
        private ProgressBar progressBarPedidos;
        private ArrayAdapter<Configuracao> adpConfiguracao;
        private com.food.devtime.devtimefood.Database.database database;
        private SQLiteDatabase connLocal;
        private RepositorioDevtimeFood repositorioDevtimeFood;
        private Configuracao configuracao;
        private String codigoPedido = "";
        private Toast toast;
        private long lastBackPressTime = 0;
        private String codAtendente;
        private String codigoMax = "";
        private AlertDialog alerta;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_menu);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            connectionRemote = new ConnectionRemote();

            progressBarPedidos = (ProgressBar) findViewById(R.id.progressBarPedidos);
            progressBarPedidos.setVisibility(View.GONE);
            listViewPedidos = (ListView) findViewById(R.id.listViewPedidos);
            txtAtendente = (TextView) findViewById(R.id.txtAtendente);
            lblCodigoPedido = (TextView) findViewById(R.id.lblCodigoPedido);
            lblObservacao = (TextView) findViewById(R.id.lblObservacao);
            proid = "";

            listViewPedidos.setOnItemClickListener(this);

            registerForContextMenu(listViewPedidos);

            //region CARREGA AS CONFIGURACOES PARA A CONEXAO
            try {

                database = new database(this);
                connLocal = database.getWritableDatabase();
                repositorioDevtimeFood = new RepositorioDevtimeFood(connLocal);
                adpConfiguracao = repositorioDevtimeFood.buscaConfiguracao(this);

            } catch (Exception e) {

                Toast.makeText(MenuActivity.this, "Erro na configuracao", Toast.LENGTH_SHORT).show();
            }
            //endregion

            FillList fillList = new FillList();
            fillList.execute("");

            materialDesignFAM = (FloatingActionMenu) findViewById(R.id.botao_float_menu);
            floatingActionButton1 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.botao_float_menu_item1);
            floatingActionButton2 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.botao_float_menu_item2);

            floatingActionButton1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    alertDialogPedido();
                }
            });
            floatingActionButton2.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    FillList fillList = new FillList();
                    fillList.execute("");

                    Toast.makeText(MenuActivity.this, "Atualizado !", Toast.LENGTH_SHORT).show();

                    // Snackbar.make(MenuActivity.this, "Atualizado !", Snackbar.LENGTH_LONG)
                    //        .setAction("Action", null).show();

                }
            });

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.setDrawerListener(toggle);
            toggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            //region RETORNA OS VALORES DA ACTIVITY ANTERIOR E PREENCHER OS COMPONETES DA TELA
            try {
                String info = this.getIntent().getStringExtra("infos");
                txtAtendente.setText(info);
                // txtNroMesa.setText("0");
                // Toast.makeText(MenuActivity.this, "PASSOU m" + txtAtendente.getText(), Toast.LENGTH_SHORT).show();
                //codAtendente = txtAtendente.getText().toString().substring(0, 1);

            } catch (Exception e) {
            }
            //endregion

            timer = new Timer();

            myTimerTask = new MyTimerTask();

            timer.schedule(myTimerTask, 1000, 60000);
        }

        @Override
        protected void onActivityResult(int codigo, int resultado, Intent intent) {
            //if (resultado  == PedidosEdit.Codigo){  }
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
                    timer.purge();
                }

                alertDialogSairApp();

                timer.cancel();
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

            super.onCreateContextMenu(menu, v, menuInfo);
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_pedidos, menu);

            //MenuItem fecharconta = menu.add("Fechar Conta");
            //  MenuItem trocamesa = menu.add("Trocar Mesa");

            // fecharconta.setOnMenuItemClickListener(menu );
            //apagar.setOnMenuItemClickListener(/*o que fazer quando clicar no item apagar*/);
        }

        @Override
        public boolean onContextItemSelected(MenuItem item) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

            switch (item.getItemId()) {
                case R.id.action_fechar_pedido:

                    // Toast.makeText(MenuActivity.this, "AKI" + info.id, Toast.LENGTH_SHORT).show();
                    Toast.makeText(MenuActivity.this, "Disponível na Proxima Atualização !", Toast.LENGTH_SHORT).show();

                    return true;
                case R.id.action_imprimir_pedido:

                    Toast.makeText(MenuActivity.this, "Disponível na Proxima Atualização !", Toast.LENGTH_SHORT).show();

                    return true;
                default:
                    return super.onContextItemSelected(item);
            }
        }

        private void FecharPedido(String pedido) {

            try {
                configuracao = adpConfiguracao.getItem(0);

                String ip = configuracao.getIp();
                String driver = configuracao.getDriver();
                String db = configuracao.getBancoDados();
                String usuario = configuracao.getUsuario();
                String senha = configuracao.getSenha();

                Connection con = connectionRemote.CONN(ip, driver, db, usuario, senha);

                if (con == null) {
                    Toast.makeText(MenuActivity.this, "Erro ao Conectar o SQL server", Toast.LENGTH_SHORT).show();
                } else {

                    String queryB = "Update Pedidos set FecharMobile = 'S'  where Codigo =" + pedido;
                    PreparedStatement preparedStatementB = con.prepareStatement(queryB);
                    preparedStatementB.executeUpdate();

                    con.close();
                }
            } catch (Exception ex) {

                Toast.makeText(MenuActivity.this, "erro " + ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            // Handle action bar item clicks here. The action bar will
            // automatically handle clicks on the Home/Up button, so long
            // as you specify a parent activity in AndroidManifest.xml.
            int id = item.getItemId();

            //noinspection SimplifiableIfStatement
            if (id == R.id.action_incluir_pedido) {
                String z = "";

                try {
                    configuracao = adpConfiguracao.getItem(0);

                    String ip = configuracao.getIp();
                    String driver = configuracao.getDriver();
                    String db = configuracao.getBancoDados();
                    String usuario = configuracao.getUsuario();
                    String senha = configuracao.getSenha();

                    Connection conMax = connectionRemote.CONN(ip, driver, db, usuario, senha);

                    if (conMax == null) {
                        z = "Error in connection with SQL server";
                    } else {
                        // vamos obter um Statement para disparar instruções SQL no banco de dados
                        Statement stmt = conMax.createStatement();
                        // vamos disparar a consulta e obter os resultados em um ResultSet
                        ResultSet rs = stmt.executeQuery("Select Max(Codigo + 1) as Codigo From Pedidos");
                        // finalmente vamos percorrer os dados retornados
                        while (rs.next()) {
                            codigoMax = rs.getString("Codigo");
                        }

                        conMax.close();
                    }

                } catch (Exception ex) {
                    Toast.makeText(MenuActivity.this, "erro " + ex.getMessage() + " " + z, Toast.LENGTH_SHORT).show();
                }

                Intent intent = new Intent(MenuActivity.this, PedidosEdit.class);
                String parametros = "I" + codigoMax;
                intent.putExtra("infos", parametros);
                intent.putExtra("NomeAtend", txtAtendente.getText().toString());

                startActivity(intent);

            } else if (id == R.id.action_ordenar_nome) {
                //TODO: ADICIONAR O ORDER BY POR NOME
            } else if (id == R.id.action_ordenar_data) {
                //TODO: ADICIONAR O ORDER BY POR DATA
            }

            return super.onOptionsItemSelected(item);
        }

        @SuppressWarnings("StatementWithEmptyBody")
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            // Handle navigation view item clicks here.
            int id = item.getItemId();

            if (id == R.id.nav_pedidos) {
                Intent it = new Intent(this, MenuActivity.class);
                startActivity(it);
            } else if (id == R.id.nav_produtos) {
                Intent it = new Intent(this, ProdutosActivity.class);
                startActivity(it);
            } else if (id == R.id.nav_clientes) {
                Intent it = new Intent(this, ClientesActivity.class);
                startActivity(it);
            } else if (id == R.id.nav_delivery) {
                Intent it = new Intent(this, MonitorPreparo.class);
                startActivity(it);
            } else if (id == R.id.nav_Verifica) {
                // Intent it = new Intent(this, ProdutosActivity.class);
                // startActivity(it);
            }

            //TODO:ARRUMAR ESSAS OPCOES

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }

        @Override
        public void onClick(View view) {

            Intent it = new Intent(this, PedidosEdit.class);
            startActivityForResult(it, 0);
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        }

        private void alertDialogPedido() {
            //Cria o gerador do AlertDialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            //define o titulo
            builder.setTitle("Atenção");
            //define a mensagem
            builder.setMessage("Novo Pedido ?");
            //define um botão como positivo
            builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {

                    String z = "";

                    try {
                        configuracao = adpConfiguracao.getItem(0);

                        String ip = configuracao.getIp();
                        String driver = configuracao.getDriver();
                        String db = configuracao.getBancoDados();
                        String usuario = configuracao.getUsuario();
                        String senha = configuracao.getSenha();

                        Connection conMax = connectionRemote.CONN(ip, driver, db, usuario, senha);

                        if (conMax == null) {
                            z = "Error in connection with SQL server";
                        } else {
                            // vamos obter um Statement para disparar instruções SQL no banco de dados
                            Statement stmt = conMax.createStatement();
                            // vamos disparar a consulta e obter os resultados em um ResultSet
                            ResultSet rs = stmt.executeQuery("Select Max(Codigo + 1) as Codigo From Pedidos");
                            // finalmente vamos percorrer os dados retornados
                            while (rs.next()) {
                                codigoMax = rs.getString("Codigo");
                            }

                            conMax.close();
                        }

                    } catch (Exception ex) {
                        Toast.makeText(MenuActivity.this, "erro " + ex.getMessage() + " " + z, Toast.LENGTH_SHORT).show();
                    }

                    Intent intent = new Intent(MenuActivity.this, PedidosEdit.class);
                    String parametros = "I" + codigoMax;
                    intent.putExtra("infos", parametros);
                    intent.putExtra("NomeAtend", txtAtendente.getText().toString());
                    startActivity(intent);

                    timer.cancel();
                    timer.purge();

                    finish();
                }
            });
            //define um botão como negativo.
            builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {


                }
            });
            //cria o AlertDialog
            alerta = builder.create();
            //Exibe
            alerta.show();
        }

        private void alertDialogSairApp() {
            //Cria o gerador do AlertDialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            //define o titulo
            builder.setTitle("Atenção");
            //define a mensagem
            builder.setMessage("Sair do Aplicativo ?");
            //define um botão como positivo
            builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {

                    finish();

                }
            });
            //define um botão como negativo.
            builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {


                }
            });
            //cria o AlertDialog
            alerta = builder.create();
            //Exibe
            alerta.show();
        }

        public class FillList extends AsyncTask<String, String, String> {
            String z = "";
            String codPedido = "";

            List<Map<String, String>> prolist = new ArrayList<Map<String, String>>();

            @Override
            protected void onPreExecute() {

                progressBarPedidos.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String r) {

                String Observacao = "n";

                progressBarPedidos.setVisibility(View.GONE);
                Toast.makeText(MenuActivity.this, r, Toast.LENGTH_SHORT).show();

                //String[] from = { "A", "B", "C" };
                String[] from = {"A", "B", "C", "D", "E", "F", "G"};
                int[] views = {
                        // R.id.lblDataDia,
                        R.id.lblDataMes,
                        //   R.id.lblDataAno,
                        R.id.lblCliente,
                        R.id.lblValorTotal,
                        R.id.lblQtdProduto,
                        R.id.lblNroPedido,
                        R.id.lblCodigoPedido,
                        R.id.lblObservacao};
                final SimpleAdapter ADA = new SimpleAdapter(MenuActivity.this,
                        prolist, R.layout.activity_pedido_view, from,
                        views);
                listViewPedidos.setAdapter(ADA);

                listViewPedidos.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1,
                                            int arg2, long arg3) {
                        HashMap<String, Object> obj = (HashMap<String, Object>) ADA
                                .getItem(arg2);
                        codigoPedido = (String) obj.get("F");

                        //METODO PARA ENVIAR PARAMETROS PARA A OUTRA ACTIVITY
                        Intent intent = new Intent(MenuActivity.this, PedidosEdit.class);
                        String parametros = "C" + codigoPedido;
                        intent.putExtra("infos", parametros);
                        intent.putExtra("NomeAtend", txtAtendente.getText().toString());

                        startActivity(intent);

                        timer.cancel();
                        timer.purge();

                        finish();
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

                        String query = "Select  " +
                                "P.Codigo," +
                                "P.Data," +
                                "C.Nome," +
                                "P.FecharConta," +
                                "P.Mesa," +
                                "P.Observacao," +
                                "A.Nome as Atendente," +
                                "(Select  format(SUM(PIT.Quant * PIT.ValorUnit), 'N', 'pt-br') as Total " +
                                "From PedidosItens PIT " +
                                "Where PIT.Codigo = P.Codigo) as Total," +
                                "(Select Count(PIT.Codigo) as QtdProduto " +
                                "From PedidosItens PIT " +
                                "Where PIT.Codigo = P.Codigo) as QtdProduto " +
                                "From Pedidos P " +
                                "Inner Join Atendente A ON A.Codigo = P.Atendente " +
                                "Inner Join Clientes C ON C.Codigo = P.Cliente " +
                                "WHERE P.FecharConta = 0" +
                                "Order By P.Mesa Desc";

                        PreparedStatement ps = con.prepareStatement(query);
                        ResultSet rs = ps.executeQuery();

                        Calendar calendar = Calendar.getInstance();

                        while (rs.next()) {

                            Date gDia = rs.getDate("data");
                            SimpleDateFormat fDia = new SimpleDateFormat("dd");

                            Date gMes = rs.getDate("data");
                            SimpleDateFormat fMes = new SimpleDateFormat("MM");

                            Date gAno = rs.getDate("data");
                            SimpleDateFormat fAno = new SimpleDateFormat("yyyy");

                            Map<String, String> datanum = new HashMap<String, String>();
                            codigoPedido = rs.getString("Codigo");
                            //   datanum.put("A", "" +fDia.format(gDia));
                            datanum.put("A", fDia.format(gDia) + "/" + fMes.format(gMes) + "/" + fAno.format(gAno));
                            //   datanum.put("C", "" +fAno.format(gAno));
                            datanum.put("B", rs.getString("Nome"));
                            datanum.put("C", "R$ " + rs.getString("Total") + "                      " + rs.getString("Codigo"));
                            datanum.put("D", rs.getString("QtdProduto") + " PRODUTO(S)");
                            datanum.put("E", rs.getString("Mesa"));//PEDIDO
                            datanum.put("F", rs.getString("Codigo"));//PEDIDO
                            datanum.put("G", rs.getString("Observacao"));//PEDIDO
                            // datanum.put("F", rs.getString("Atendente"));

                            prolist.add(datanum);
                        }

                        con.close();

                        z = "Atualizado !";
                    }
                } catch (Exception ex) {
                    z = "Error retrieving data from table" + ex.getMessage();

                }
                return z;
            }
        }

        class MyTimerTask extends TimerTask {

            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        FillList fillList = new FillList();
                        fillList.execute("");
                    }
                });
            }
        }
    }
