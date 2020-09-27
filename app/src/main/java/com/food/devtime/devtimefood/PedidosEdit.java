package com.food.devtime.devtimefood;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.food.devtime.devtimefood.Database.ConnectionRemote;
import com.food.devtime.devtimefood.Database.database;
import com.food.devtime.devtimefood.Dominio.Entidade.Configuracao;
import com.food.devtime.devtimefood.Dominio.RepositorioDevtimeFood;
import com.github.clans.fab.FloatingActionMenu;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PedidosEdit extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    static final int CHAMADA = 0;
    public EditText dtData, txtCliente, txtNroPedido, txtNroProduto, txtNroMesa, txtQtdProd, txtObservacao;
    ConnectionRemote connectionRemote;
    String codigoPedido = "";
    //region VARIAVEIS PARA O INSERT E EDICAO DA TABELA PEDIDOSITENS
    BigDecimal valorUnitPedItens;
    String observacaoPedItens = "", nomeAtend = "";
    FloatingActionMenu materialDesignFAM;
    com.github.clans.fab.FloatingActionButton floatingActionButton1, floatingActionButton2, floatingActionButton3, floatingActionButton4, floatingActionButton5;
    //region VARIAVEIS DOS COMPONENTES DO LAYOUT
    private Spinner spinnerCategoria, spinnerProdutos, spinner_categoria;
    private ProgressBar progressBarPedidosEdit;
    private TextView lblQtdProd, lblAddProdutos, lblDeleteProduto, lblAddProduto;
    private ListView listViewAddProduto;
    //endregion
    private Button btnOK, btnExcluir;
    private AlertDialog alerta;
    //region VARIAVEIS PARA A CONEXAO AO BANCO LOCAL PARA CARREGAR AS CONFIGURACOES
    private ArrayAdapter<Configuracao> adpConfiguracao;
    private com.food.devtime.devtimefood.Database.database database;
    private SQLiteDatabase connLocal;
    //endregion
    private RepositorioDevtimeFood repositorioDevtimeFood;
    private Configuracao configuracao;
    //endregion
    private Toast toast;
    private long lastBackPressTime = 0;
    private int codAtendente = 0, controleMov = 0, quantEstoqueMov = 0, movimentaEstoque = 0, QuantidadeProduto = 0, quantPedProMov = 0;
    private double valorTotalPedido;

    private String CodigoProduto = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido_toolbar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Pedidos...");

        connectionRemote = new ConnectionRemote();

        //region CARREGA AS CONFIGURACOES PARA A CONEXAO
        try {

            database = new database(this);
            connLocal = database.getWritableDatabase();
            repositorioDevtimeFood = new RepositorioDevtimeFood(connLocal);
            adpConfiguracao = repositorioDevtimeFood.buscaConfiguracao(this);

        } catch (Exception e) {

            Toast.makeText(PedidosEdit.this, "Erro na configuracao", Toast.LENGTH_SHORT).show();
        }
        //endregion

        materialDesignFAM = (FloatingActionMenu) findViewById(R.id.botao_float_pedido);
        floatingActionButton1 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.botao_float_pedido_item1);
        floatingActionButton2 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.botao_float_pedido_item2);
        floatingActionButton3 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.botao_float_pedido_item3);
        floatingActionButton4 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.botao_float_pedido_item4);
        floatingActionButton5 = (com.github.clans.fab.FloatingActionButton) findViewById(R.id.botao_float_pedido_item5);

        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                alertDialogFecharPedido();
            }
        });
        floatingActionButton2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                alertDialogImprimirPedido();

            }
        });

        floatingActionButton4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if (txtNroMesa.getText().toString().isEmpty()) {
                    Toast.makeText(PedidosEdit.this, "Informe a Mesa !", Toast.LENGTH_SHORT).show();
                } else {
                    PesquisaMesaDisp updatePro = new PesquisaMesaDisp();
                    updatePro.execute("");
                }
                    }
        });

        floatingActionButton5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                FillList fillList = new FillList();
                fillList.execute("");

                AtualizaMesa();
            }
        });

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //region CARREGA OS COMPONENTES DO ACTIVITY
        dtData = (EditText) findViewById(R.id.dtData);
        txtCliente = (EditText) findViewById(R.id.txtCliente);
        txtNroPedido = (EditText) findViewById(R.id.txtNroPedido);
        txtNroMesa = (EditText) findViewById(R.id.txtNroMesa);
        lblQtdProd = (TextView) findViewById(R.id.lblQtdProd);
        txtQtdProd = (EditText) findViewById(R.id.txtQtdProd);
        txtNroProduto = (EditText) findViewById(R.id.txtNroProduto);
        txtObservacao = (EditText) findViewById(R.id.txtObservacao);

        spinner_categoria = (Spinner) findViewById(R.id.spinnerCategoria);
        //spinnerCategoria = (Spinner)findViewById(R.id.spinnerCategoria);
        spinnerProdutos = (Spinner) findViewById(R.id.spinnerProdutos);
        //BOTÃO
        btnOK = (Button) findViewById(R.id.btnOK);
        btnExcluir = (Button) findViewById(R.id.btnExcluir);

        lblAddProduto = (TextView) findViewById(R.id.lblAddProduto);
        lblAddProdutos = (TextView) findViewById(R.id.lblAddProdutos);
        lblDeleteProduto = (TextView) findViewById(R.id.lblDeleteProduto);


        //endregion

        listViewAddProduto = (ListView) findViewById(R.id.listViewAddProduto);
        listViewAddProduto.setOnItemClickListener(this);
        registerForContextMenu(listViewAddProduto);

        PreencheCategoria();

        //region SPINNER DA CATEGORIA
        //Método do Spinner para capturar o item selecionado
        spinner_categoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int posicao, long id) {
                //pega nome pela posição
                String nome = parent.getItemAtPosition(posicao).toString();

                PreencheProdutos(nome);

                //imprime um Toast na tela com o nome que foi selecionado
                // Toast.makeText(PedidosEdit.this, "Nome Selecionado: " + nome, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        //endregion

        //region SPINNER DOS PRODUTOS
        //Método do Spinner para capturar o item selecionado
        spinnerProdutos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int posicao, long id) {
                //pega nome pela posição
                String nome = parent.getItemAtPosition(posicao).toString();
                observacaoPedItens = nome;

                PreencheNroProduto(nome);

                //imprime um Toast na tela com o nome que foi selecionado
                // Toast.makeText(PedidosEdit.this, "Nome Selecionado: " + nome, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        //endregion

        progressBarPedidosEdit = (ProgressBar) findViewById(R.id.progressBarPedidosEdit);
        progressBarPedidosEdit.setVisibility(View.GONE);

        //region RETORNA OS VALORES DA ACTIVITY ANTERIOR E PREENCHER OS COMPONETES DA TELA
        try {
            nomeAtend = this.getIntent().getStringExtra("NomeAtend");
            PreencheCodAtendente(nomeAtend);

            String info = this.getIntent().getStringExtra("infos");
            txtCliente.setText(info);
            String tipo = txtCliente.getText().toString().substring(0, 1);

            codigoPedido = txtCliente.getText().toString().substring(1);

            txtNroPedido.setText(txtCliente.getText().toString().substring(1));
            txtCliente.setText("Venda Direta");
            txtQtdProd.setText("1");

            if (tipo.equals("I")) {

                floatingActionButton3.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {

                        txtNroMesa.setText("");
                        //METODO PARA ENVIAR PARAMETROS PARA A OUTRA ACTIVITY
                        Intent intent = new Intent(PedidosEdit.this, MesaActivity.class);
                        String parametros = txtNroPedido.getText().toString();
                        intent.putExtra("CodPedido", parametros);
                        intent.putExtra("NomeAtend", nomeAtend);
                        startActivity(intent);
                    }
                });

                AddPedido addPro = new AddPedido();
                addPro.execute("");

                txtObservacao.setText("Venda Mobile ");
                txtNroMesa.setText("");
                //METODO PARA ENVIAR PARAMETROS PARA A OUTRA ACTIVITY
                Intent intent = new Intent(PedidosEdit.this, MesaActivity.class);
                String parametros = txtNroPedido.getText().toString();
                intent.putExtra("CodPedido", parametros);
                startActivity(intent);

            } else {

                floatingActionButton3.setVisibility(View.GONE);

                PreencheDados();
            }

            String currentDateTimeString = DateFormat.getDateInstance().format(new Date());
            dtData.setText(currentDateTimeString);

            ExibeDataListener listener = new ExibeDataListener();

            dtData.setOnClickListener(listener);

        } catch (Exception e) {

            PreencheDados();

            FillList fillList = new FillList();
            fillList.execute("");
        }
        //endregion

        txtCliente.setOnClickListener(this);
        //lblAddProduto.setOnClickListener(this);

                FillList fillList = new FillList();
                fillList.execute("");

        spinner_categoria.requestFocus();

        btnOK.setOnClickListener(this);
        btnExcluir.setOnClickListener(this);
        lblAddProduto.setOnClickListener(this);
        lblDeleteProduto.setOnClickListener(this);

        lblAddProdutos.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                SomaValorTotalPedido();
                if (valorTotalPedido > 0) {

                    alertDialogSalvarPedido();

                } else {
                    alertDialogDeletePedido();

                    // super.onBackPressed();
                }

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

            SomaValorTotalPedido();
            if (valorTotalPedido > 0) {

                alertDialogSalvarPedido();

            } else {
                alertDialogDeletePedido();

                // super.onBackPressed();
            }
                }
            }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        //MenuItem excluirproduto = menu.add("Excluir Produto");
        // MenuItem trocamesa = menu.add("Trocar Mesa");

        // AddPedido addPro = new AddPedido();
        // addPro.execute("");

        //excluirproduto.setOnMenuItemClickListener();
        //excluirproduto.setOnMenuItemClickListener(/*o que fazer quando clicar no item apagar*/);

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        // Toast.makeText(PedidosEdit.this, "?", Toast.LENGTH_SHORT).show();
    }

    private void alertDialogDelete() {
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle("Atenção");
        //define a mensagem
        builder.setMessage("Deletar o Produto ?");
        //define um botão como positivo
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

                DeleteProduto deleteProduto = new DeleteProduto();
                deleteProduto.execute("");

                Toast.makeText(PedidosEdit.this, "Registro deletado !", Toast.LENGTH_SHORT).show();

                // finish();
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

    private void PreencheDados() {

        try {

            configuracao = adpConfiguracao.getItem(0);
            String ip = configuracao.getIp();
            String driver = configuracao.getDriver();
            String db = configuracao.getBancoDados();
            String usuario = configuracao.getUsuario();
            String senha = configuracao.getSenha();

            Connection con = connectionRemote.CONN(ip, driver, db, usuario, senha);

            if (con == null) {
                Toast.makeText(PedidosEdit.this, "Error in connection with SQL server", Toast.LENGTH_SHORT).show();

            } else {
                // vamos obter um Statement para disparar instruções SQL no banco de dados
                Statement stmt = con.createStatement();
                // vamos disparar a consulta e obter os resultados em um ResultSet

                ResultSet rs = stmt.executeQuery(
                        "Select P.Codigo, " +
                                "P.Cliente, " +
                                "C.Nome as NomeCliente, " +
                                "P.Mesa, " +
                                "P.Observacao " +
                                "From Pedidos P " +
                                "Inner Join Clientes C on C.Codigo = P.Cliente " +
                                "Where P.Codigo = " + codigoPedido);


                // finalmente vamos percorrer os dados retornados
                while (rs.next()) {
                    int id = rs.getInt("Codigo");
                    String nomeCliente = rs.getString("NomeCliente");
                    String observacao = rs.getString("Observacao");
                    int mesa = rs.getInt("Mesa");

                    txtNroPedido.setText(Integer.toString(id));
                    txtCliente.setText(nomeCliente);
                    txtNroMesa.setText(Integer.toString(mesa));
                    txtObservacao.setText(observacao);

                    //Toast.makeText(PedidosEdit.this, "Added Successfully " + codigoProduto , Toast.LENGTH_SHORT).show();
                }

                con.close();
            }
            //  Toast.makeText(PedidosEdit.this, "Added Successfully " + id , Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            Toast.makeText(PedidosEdit.this, "Exceptions " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                }

            }

    private void exibeData() {
        Calendar calendar = Calendar.getInstance();

        int ano = calendar.get(Calendar.YEAR);
        int mes = calendar.get(Calendar.MONTH);
        int dia = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dlg = new DatePickerDialog(this, new SelecionaDataListener(), ano, mes, dia);
        dlg.show();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.txtCliente:
                Intent it = new Intent(this, ClientesActivity.class);
                startActivityForResult(it, 0);

                break;
            case R.id.lblAddProdutos:

                AtualizaMesa();

                String CodigoPed = txtNroPedido.getText().toString();

                //METODO PARA ENVIAR PARAMETROS PARA A OUTRA ACTIVITY
                Intent intent = new Intent(PedidosEdit.this, EditarProdutoActivity.class);
                String parametros = CodigoPed;
                intent.putExtra("infos", parametros);
                intent.putExtra("paramMesa", txtNroMesa.getText().toString());
                intent.putExtra("NomeAtend", nomeAtend);
                intent.putExtra("Observacao", txtObservacao.getText().toString());

                startActivity(intent);

                finish();

                break;
            case R.id.btnExcluir:

                PesquisaDelProduto pesquisaDelProduto = new PesquisaDelProduto();
                pesquisaDelProduto.execute("");

                txtQtdProd.setText("0");

                break;
            case R.id.btnOK:

                PesquisaAddProduto pesquisaProduto = new PesquisaAddProduto();
                pesquisaProduto.execute("");

                txtQtdProd.setText("0");

                break;
            case R.id.lblAddProduto:
                //Toast.makeText(PedidosEdit.this, "PEGA !!!", Toast.LENGTH_SHORT).show();
                PesquisaProdAdicionar pesquisaProdAdicionar = new PesquisaProdAdicionar();
                pesquisaProdAdicionar.execute("");

                // lblAddProduto.setEnabled(false);
                //lblDeleteProduto.setEnabled(false);

                break;
            case R.id.lblDeleteProduto:

                // Toast.makeText(PedidosEdit.this, "PEGA 2 !!!", Toast.LENGTH_SHORT).show();
                PesquisaProdDiminuir updateProDiminuir = new PesquisaProdDiminuir();
                updateProDiminuir.execute("");

                //lblAddProduto.setEnabled(false);
                // lblDeleteProduto.setEnabled(false);

                break;
        }

            }

    private void PreencheCategoria() {

        try {

            configuracao = adpConfiguracao.getItem(0);

            String ip = configuracao.getIp();
            String driver = configuracao.getDriver();
            String db = configuracao.getBancoDados();
            String usuario = configuracao.getUsuario();
            String senha = configuracao.getSenha();

            Connection con = connectionRemote.CONN(ip, driver, db, usuario, senha);

            if (con == null) {
                Toast.makeText(PedidosEdit.this, "Error in connection with SQL server", Toast.LENGTH_SHORT).show();
            } else {
                String query = "Select * From ProdutoCategoria Order by Categoria";
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();

                List<String> categoria = new ArrayList<String>();

                while (rs.next()) {

                    //categoria.add("Código: "+rs.getString("Codigo"));
                    categoria.add(rs.getString("Categoria"));
                    categoria.add(" ");
                }

                con.close();

                // ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<String>(this,R.layout.spinner_categoria, categoria);
                // arrayAdapter2.setDropDownViewResource(R.layout.spinner_categoria);
                //  spinner_categoria.setAdapter(arrayAdapter2);


                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_categoria, categoria);
                spinner_categoria.setAdapter(arrayAdapter);


                //ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, categoria);
                //  ArrayAdapter<String> spinnerArrayAdapter = arrayAdapter;
                // spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                // spinnerCategoria.setAdapter(spinnerArrayAdapter);
            }
        } catch (Exception ex) {

            Toast.makeText(PedidosEdit.this, "erro " + ex.getMessage(), Toast.LENGTH_SHORT).show();
            //  z = "Error retrieving data from table" + ex.getMessage();

                }
    }

    private void PreencheProdutos(String descricao) {

        try {

            configuracao = adpConfiguracao.getItem(0);

            String ip = configuracao.getIp();
            String driver = configuracao.getDriver();
            String db = configuracao.getBancoDados();
            String usuario = configuracao.getUsuario();
            String senha = configuracao.getSenha();

            Connection con = connectionRemote.CONN(ip, driver, db, usuario, senha);

            if (con == null) {
                Toast.makeText(PedidosEdit.this, "Error in connection with SQL server", Toast.LENGTH_SHORT).show();
            } else {

                String query =
                        "Select * " +
                                "From Produtos P " +
                                "Inner join ProdutoCategoria PC on PC.Codigo = P.CategoriaProduto " +
                                "Where PC.Categoria = '" + descricao + "'";

                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();

                List<String> produto = new ArrayList<String>();

                while (rs.next()) {

                    //VARIAVEIS PARA O INSERT DE PEDIDOSITENS
                    valorUnitPedItens = rs.getBigDecimal("PrecoVenda");

                    produto.add(rs.getString("Descricao"));
                    produto.add("R$ " + rs.getString("PrecoVenda"));
                    produto.add(" ");
                }

                con.close();

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, produto);
                ArrayAdapter<String> spinnerArrayAdapter = arrayAdapter;
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
                spinnerProdutos.setAdapter(spinnerArrayAdapter);
                    }
        } catch (Exception ex) {

            Toast.makeText(PedidosEdit.this, "erro " + ex.getMessage(), Toast.LENGTH_SHORT).show();
            //  z = "Error retrieving data from table" + ex.getMessage();

                }
    }

    private void PreencheNroProduto(String descricao) {

        try {

            configuracao = adpConfiguracao.getItem(0);

            String ip = configuracao.getIp();
            String driver = configuracao.getDriver();
            String db = configuracao.getBancoDados();
            String usuario = configuracao.getUsuario();
            String senha = configuracao.getSenha();

            Connection con = connectionRemote.CONN(ip, driver, db, usuario, senha);

            if (con == null) {
                Toast.makeText(PedidosEdit.this, "Error in connection with SQL server", Toast.LENGTH_SHORT).show();
            } else {

                //String query = "Select * From Produtos Order by Descricao";
                String query = "Select * From Produtos Where Descricao = '" + descricao + "'";

                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {

                    txtNroProduto.setText(Integer.toString(rs.getInt("Codigo")));
                    valorUnitPedItens = rs.getBigDecimal("PrecoVenda");

                }

                con.close();

                    }
        } catch (Exception ex) {

            Toast.makeText(PedidosEdit.this, "erro " + ex.getMessage(), Toast.LENGTH_SHORT).show();
            //  z = "Error retrieving data from table" + ex.getMessage();

                }
    }

    private void PreencheCodAtendente(String descricao) {

        try {

            configuracao = adpConfiguracao.getItem(0);

            String ip = configuracao.getIp();
            String driver = configuracao.getDriver();
            String db = configuracao.getBancoDados();
            String usuario = configuracao.getUsuario();
            String senha = configuracao.getSenha();

            Connection con = connectionRemote.CONN(ip, driver, db, usuario, senha);

            if (con == null) {
                Toast.makeText(PedidosEdit.this, "Error in connection with SQL server", Toast.LENGTH_SHORT).show();
            } else {

                //String query = "Select * From Produtos Order by Descricao";
                String query = "Select * From Atendente Where Nome = '" + descricao + "'";

                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {

                    codAtendente = rs.getInt("Codigo");
                }

                con.close();

                    }
        } catch (Exception ex) {

            Toast.makeText(PedidosEdit.this, "erro " + ex.getMessage(), Toast.LENGTH_SHORT).show();
            //  z = "Error retrieving data from table" + ex.getMessage();

                }
            }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //Pronto, você tem o retorno para a sua Activity, ai se você quiser retornar algum valor da outra
        //Activity, isso tambem é possivel

        //region RETORNA OS VALORES DA ACTIVITY ANTERIOR E PREENCHER OS COMPONETES DA TELA

        try {
            String info = this.getIntent().getStringExtra("infos");
            txtCliente.setText(info);

            String tipo = txtCliente.getText().toString().substring(0, 1);

            codigoPedido = txtCliente.getText().toString().substring(1);

            txtNroPedido.setText(txtCliente.getText().toString().substring(1));
            txtCliente.setText("Venda Direta");
            txtQtdProd.setText("1");

            if (tipo.equals("I")) {
                AddPedido addPro = new AddPedido();
                addPro.execute("");
            } else {
                PreencheDados();
            }

            String currentDateTimeString = DateFormat.getDateInstance().format(new Date());
            dtData.setText(currentDateTimeString);

            ExibeDataListener listener = new ExibeDataListener();

            dtData.setOnClickListener(listener);

        } catch (Exception e) {

            PreencheDados();
        }
        //endregion
            }

    private void SomaValorTotalPedido() {

        try {
            // Toast.makeText(PedidosEdit.this, "PASSOUasd " + codigoPedido , Toast.LENGTH_SHORT).show();
            configuracao = adpConfiguracao.getItem(0);

            String ip = configuracao.getIp();
            String driver = configuracao.getDriver();
            String db = configuracao.getBancoDados();
            String usuario = configuracao.getUsuario();
            String senha = configuracao.getSenha();

            Connection con = connectionRemote.CONN(ip, driver, db, usuario, senha);

            if (con == null) {
                Toast.makeText(PedidosEdit.this, "Error in connection with SQL server", Toast.LENGTH_SHORT).show();
            } else {

                //String query = "Select * From Produtos Order by Descricao";
                String query = "Select Sum((PED.Quant * PED.ValorUnit)- PED.Desconto ) as Total  " +
                        "From PedidosItens PED  " +
                        "Where PED.Codigo = '" + codigoPedido + "'";

                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {

                    valorTotalPedido = rs.getDouble("Total");
                }

                con.close();
                //   Toast.makeText(PedidosEdit.this, "PASSOU " +valorTotalPedido , Toast.LENGTH_SHORT).show();
                    }
        } catch (Exception ex) {

            Toast.makeText(PedidosEdit.this, "erro " + ex.getMessage(), Toast.LENGTH_SHORT).show();
            //  z = "Error retrieving data from table" + ex.getMessage();

        }
            }

    private void PedidosPrintMobile() {

        int pedido = Integer.parseInt(txtNroPedido.getText().toString());
        int mesa = Integer.parseInt(txtNroMesa.getText().toString());

        try {
            configuracao = adpConfiguracao.getItem(0);

            String ip = configuracao.getIp();
            String driver = configuracao.getDriver();
            String db = configuracao.getBancoDados();
            String usuario = configuracao.getUsuario();
            String senha = configuracao.getSenha();

            Connection con = connectionRemote.CONN(ip, driver, db, usuario, senha);

            if (con == null) {
                Toast.makeText(PedidosEdit.this, "Erro ao Conectar o SQL server", Toast.LENGTH_SHORT).show();
            } else {

                String queryB = "Update PedidosPrintMobile set Mesa='" + mesa + "', Status = 'A', Atendente ='" + codAtendente + "' where Pedido=" + pedido;
                PreparedStatement preparedStatementB = con.prepareStatement(queryB);
                preparedStatementB.executeUpdate();

                con.close();
            }
        } catch (Exception ex) {

            Toast.makeText(PedidosEdit.this, "erro " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
            }

    @Override
    public void finish() {

        super.finish();
    }

    private void alertDialogDeletePedido() {
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle("Atenção");
        //define a mensagem
        builder.setMessage("Pedido sem Produtos !\n\nDeseja Excluir o Pedido ?");
        //define um botão como positivo
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

                DeletaPedido();

                Toast.makeText(PedidosEdit.this, "Pedido deletado !", Toast.LENGTH_SHORT).show();

                //METODO PARA ENVIAR PARAMETROS PARA A OUTRA ACTIVITY
                Intent intent = new Intent(PedidosEdit.this, MenuActivity.class);
                String parametros = nomeAtend;
                intent.putExtra("infos", parametros);
                startActivity(intent);

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
    //endregion

    private void DeletaPedido() {

        try {
            configuracao = adpConfiguracao.getItem(0);

            String ip = configuracao.getIp();
            String driver = configuracao.getDriver();
            String db = configuracao.getBancoDados();
            String usuario = configuracao.getUsuario();
            String senha = configuracao.getSenha();

            Connection con = connectionRemote.CONN(ip, driver, db, usuario, senha);

            if (con == null) {
                Toast.makeText(PedidosEdit.this, "Error in connection with SQL server", Toast.LENGTH_SHORT).show();
            } else {

                String query = "Delete From Pedidos where Codigo ='" + codigoPedido + "'";
                PreparedStatement preparedStatement = con.prepareStatement(query);
                preparedStatement.executeUpdate();

                con.close();
            }
        } catch (Exception ex) {

            Toast.makeText(PedidosEdit.this, "erro " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
            }

    private void AtualizaMesa() {

        try {
            configuracao = adpConfiguracao.getItem(0);

            String ip = configuracao.getIp();
            String driver = configuracao.getDriver();
            String db = configuracao.getBancoDados();
            String usuario = configuracao.getUsuario();
            String senha = configuracao.getSenha();

            Connection con = connectionRemote.CONN(ip, driver, db, usuario, senha);

            if (con == null) {
                Toast.makeText(PedidosEdit.this, "Error in connection with SQL server", Toast.LENGTH_SHORT).show();
            } else {

                String query = "Select Mesa From Pedidos Where Codigo ='" + codigoPedido + "'";

                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();

                while (rs.next()) {

                    txtNroMesa.setText(rs.getString("Mesa"));
                }

                con.close();
                    }
        } catch (Exception ex) {

            Toast.makeText(PedidosEdit.this, "erro " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

    private void FecharPedido() {


    }

    private void alertDialogFecharPedido() {
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle("Atenção");
        //define a mensagem
        builder.setMessage("Fechar Pedido Nº" + codigoPedido + " da Mesa " + txtNroMesa.getText() + " ?");
        //define um botão como positivo
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

                String z = "";
                alertDialogPedidoPercent();

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

    private void alertDialogPedidoPercent() {
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle("Atenção");
        //define a mensagem
        builder.setMessage("Deseja Incluir 10% no Pedido ?");
        //define um botão como positivo
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

                SomaValorTotalPedido();

                String z = "";

                try {
                    configuracao = adpConfiguracao.getItem(0);

                    String ip = configuracao.getIp();
                    String driver = configuracao.getDriver();
                    String db = configuracao.getBancoDados();
                    String usuario = configuracao.getUsuario();
                    String senha = configuracao.getSenha();

                    Connection con = connectionRemote.CONN(ip, driver, db, usuario, senha);

                    if (con == null) {
                        Toast.makeText(PedidosEdit.this, "Erro ao Conectar o SQL server", Toast.LENGTH_SHORT).show();
                    } else {

                        String queryB = "Update Pedidos " +
                                "Set FechaMobile = 'F' , Servico = (" + valorTotalPedido + " * 0.1) " +
                                "where Codigo =" + codigoPedido; // F = FECHAR
                        PreparedStatement preparedStatementB = con.prepareStatement(queryB);
                        preparedStatementB.executeUpdate();

                        con.close();
                    }
                } catch (Exception ex) {

                    Toast.makeText(PedidosEdit.this, "erro " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                //METODO PARA ENVIAR PARAMETROS PARA A OUTRA ACTIVITY
                Intent intent = new Intent(PedidosEdit.this, MenuActivity.class);
                String parametros = nomeAtend;
                intent.putExtra("infos", parametros);
                startActivity(intent);

                finish();
                    }
        });
        //define um botão como negativo.
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

                String z = "";

                try {
                    configuracao = adpConfiguracao.getItem(0);

                    String ip = configuracao.getIp();
                    String driver = configuracao.getDriver();
                    String db = configuracao.getBancoDados();
                    String usuario = configuracao.getUsuario();
                    String senha = configuracao.getSenha();

                    Connection con = connectionRemote.CONN(ip, driver, db, usuario, senha);

                    if (con == null) {
                        Toast.makeText(PedidosEdit.this, "Erro ao Conectar o SQL server", Toast.LENGTH_SHORT).show();
                    } else {

                        String queryB = "Update Pedidos " +
                                "Set FechaMobile = 'F' " +
                                "Where Codigo =" + codigoPedido; // F = FECHAR

                        PreparedStatement preparedStatementB = con.prepareStatement(queryB);
                        preparedStatementB.executeUpdate();

                        con.close();
                    }
                } catch (Exception ex) {

                    Toast.makeText(PedidosEdit.this, "erro " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                //METODO PARA ENVIAR PARAMETROS PARA A OUTRA ACTIVITY
                Intent intent = new Intent(PedidosEdit.this, MenuActivity.class);
                String parametros = nomeAtend;
                intent.putExtra("infos", parametros);
                startActivity(intent);

                finish();

                    }
        });
        //cria o AlertDialog
        alerta = builder.create();
        //Exibe
        alerta.show();
    }

    private void alertDialogImprimirPedido() {
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle("Atenção");
        //define a mensagem
        builder.setMessage("Imprimir Pedido Nº" + codigoPedido + " da Mesa " + txtNroMesa.getText() + " ?");
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

                    Connection con = connectionRemote.CONN(ip, driver, db, usuario, senha);

                    if (con == null) {
                        Toast.makeText(PedidosEdit.this, "Erro ao Conectar o SQL server", Toast.LENGTH_SHORT).show();
                    } else {

                        String queryB = "Update Pedidos set FechaMobile = 'I'  where Codigo =" + codigoPedido; // I = IMPRIMIR
                        PreparedStatement preparedStatementB = con.prepareStatement(queryB);
                        preparedStatementB.executeUpdate();

                        con.close();
                    }
                } catch (Exception ex) {

                    Toast.makeText(PedidosEdit.this, "erro " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }
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

    private void alertDialogEditarProdutos(String CodigoProd, String NomeProd) {
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle("Atenção");
        //define a mensagem
        builder.setMessage("Alterar Quantidade do Produto ? \n\nNº " + CodigoProd + "-" + NomeProd + "\n\n");
        //define um botão como positivo
        builder.setPositiveButton("Adicionar + 1 ?", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

                UpdateADDProd update = new UpdateADDProd();
                update.execute("");

            }
        });
        //define um botão como negativo.
        builder.setNegativeButton("Remover - 1 ?", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

                UpdateMOVProd update = new UpdateMOVProd();
                update.execute("");
            }
        });
        //cria o AlertDialog
        alerta = builder.create();
        //Exibe
        alerta.show();
    }

    private void MovimentaEstoqueADD(int quant) {

        controleMov = 0;
        int quantidade = 0;
        int calcQtd = 0;
        int qtdMov = 0;
        String Pedido = txtNroPedido.getText().toString();
        int produtoMov = Integer.parseInt(txtNroProduto.getText().toString());
        qtdMov = quant;

        try {
            configuracao = adpConfiguracao.getItem(0);

            String ip = configuracao.getIp();
            String driver = configuracao.getDriver();
            String db = configuracao.getBancoDados();
            String usuario = configuracao.getUsuario();
            String senha = configuracao.getSenha();

            Connection con = connectionRemote.CONN(ip, driver, db, usuario, senha);

            if (con == null) {
                Toast.makeText(PedidosEdit.this, "Error in connection with SQL server", Toast.LENGTH_SHORT).show();
            } else {

                Statement stmtA = con.createStatement();
                //VERIFICA SE O PRODUTO CONTROLA ESTOQUE
                ResultSet rstA = stmtA.executeQuery("Select ControleES From  Produtos Where Codigo =" + CodigoProduto);
                while (rstA.next()) {
                    controleMov = rstA.getInt("ControleES");
                }
                //VERIFICA SE AINDA TEM EM ESTOQUE
                rstA = stmtA.executeQuery("Select Quant From  Estoque Where Produto =" + CodigoProduto);
                while (rstA.next()) {
                    quantEstoqueMov = rstA.getInt("Quant");
                }

                if (controleMov > 0) { // 0 == Nao 1 == Sim

                    movimentaEstoque = 1;

                    if (quantEstoqueMov >= qtdMov) {

                        Statement stmt = con.createStatement();

                        ResultSet rst = stmt.executeQuery("Select Quant From PedidosItens " +
                                "Where Codigo ='" + Pedido + "' and Produto =" + CodigoProduto);
                        while (rst.next()) {
                            quantPedProMov = rst.getInt("Quant");
                        }

                        calcQtd = qtdMov - quantPedProMov;

                        if (calcQtd < 0) {

                            int auxQtd = calcQtd * (-1);
                            quantidade = quantEstoqueMov + auxQtd;

                        } else {
                            quantidade = quantEstoqueMov - calcQtd;
                        }
                        //MOVIMENTA ESTOQUE
                        String queryC = "Update Estoque set Quant =" + quantidade + " where Produto =" + CodigoProduto;
                        PreparedStatement preparedStatementC = con.prepareStatement(queryC);
                        preparedStatementC.executeUpdate();

                            } else {
                        movimentaEstoque = 0;
                            }
                        } else {
                    movimentaEstoque = 1;

                        }

                con.close();
                    }
        } catch (Exception ex) {

            Toast.makeText(PedidosEdit.this, "erro " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
    }

    private void alertDialogSalvarPedido() {
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle("Atenção");
        //define a mensagem
        builder.setMessage("Salvar Pedido Nº" + codigoPedido + " da Mesa " + txtNroMesa.getText() + " ?");
        //define um botão como positivo
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {

                if (txtNroMesa.getText().toString().isEmpty()) {
                    Toast.makeText(PedidosEdit.this, "Informe a Mesa !", Toast.LENGTH_SHORT).show();
                } else {
                    PesquisaMesaDisp updatePro = new PesquisaMesaDisp();
                    updatePro.execute("");
                }

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

    public class UpdateADDProd extends AsyncTask<String, String, String> {

        String z = "";
        Boolean isSuccess = false;
        String Pedido = txtNroPedido.getText().toString();

        @Override
        protected void onPreExecute() {

            //progressBarPedidosEdit.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {
            Toast.makeText(PedidosEdit.this, z, Toast.LENGTH_LONG).show();
            if (isSuccess == true) {

                FillList fillList = new FillList();
                fillList.execute("");
            } else {

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

                    int quant = QuantidadeProduto + 1;

                    MovimentaEstoqueADD(quant);

                    //VERIFICA SE ESSE PRODUTO ESTA MARCADO A OPCAO DE CONTROLE DE ESTOQUE , E SE ELE TEM A QUANTIDADE PARA O PEDIDO
                    if (movimentaEstoque > 0) {
                        String query = "Update PedidosItens set Quant='" + quant + "' where Codigo='" + Pedido + "' and Produto =" + CodigoProduto;
                        PreparedStatement preparedStatement = con.prepareStatement(query);
                        preparedStatement.executeUpdate();

                        String queryB = "Update PedidosPrintMobile set Quant='" + quant + "', Status = 'E' where Pedido='" + Pedido + "' and Produto =" + CodigoProduto;
                        PreparedStatement preparedStatementB = con.prepareStatement(queryB);
                        preparedStatementB.executeUpdate();

                        isSuccess = true;

                        z = "Alterado !";
                            } else {

                        z = "Produto sem Estoque !";
                        isSuccess = false;
                            }

                    con.close();
                        }
            } catch (Exception ex) {
                isSuccess = false;
                z = "Exceptions" + ex.getMessage();
                    }

            return z;
                }
            }

    public class UpdateMOVProd extends AsyncTask<String, String, String> {

        String z = "";
        Boolean isSuccess = false;
        String Pedido = txtNroPedido.getText().toString();

        @Override
        protected void onPreExecute() {

            //progressBarPedidosEdit.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {
            Toast.makeText(PedidosEdit.this, z, Toast.LENGTH_LONG).show();
            if (isSuccess == true) {

                FillList fillList = new FillList();
                fillList.execute("");
            } else {

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

                    int quant = QuantidadeProduto - 1;

                    if (quant >= 1) {

                        MovimentaEstoqueADD(quant);

                        //VERIFICA SE ESSE PRODUTO ESTA MARCADO A OPCAO DE CONTROLE DE ESTOQUE , E SE ELE TEM A QUANTIDADE PARA O PEDIDO
                        if (movimentaEstoque > 0) {
                            String query = "Update PedidosItens set Quant='" + quant + "' where Codigo='" + Pedido + "' and Produto =" + CodigoProduto;
                            PreparedStatement preparedStatement = con.prepareStatement(query);
                            preparedStatement.executeUpdate();

                            String queryB = "Update PedidosPrintMobile set Quant='" + quant + "', Status = 'E' where Pedido='" + Pedido + "' and Produto =" + CodigoProduto;
                            PreparedStatement preparedStatementB = con.prepareStatement(queryB);
                            preparedStatementB.executeUpdate();

                            isSuccess = true;
                            z = "Alterado !";
                        } else {

                            z = "Produto sem Estoque !";
                            isSuccess = false;
                        }
                    } else {
                        z = "Quantidade não pode ser menor que 1 !";
                        isSuccess = false;
                    }

                    con.close();
                }
            } catch (Exception ex) {
                isSuccess = false;
                z = "Exceptions" + ex.getMessage();
                    }

            return z;
        }
            }

    //region CRUD DO PEDIDOS E PEDIDOSITENS
    public class AddPedido extends AsyncTask<String, String, String> {

        String msgRetornoPedido = "";
        Boolean isSuccess = false;

        String proname = txtCliente.getText().toString();

        @Override
        protected void onPreExecute() {
            progressBarPedidosEdit.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {
            progressBarPedidosEdit.setVisibility(View.GONE);

            if (isSuccess == false) {

                Toast.makeText(PedidosEdit.this, msgRetornoPedido, Toast.LENGTH_SHORT).show();

                //METODO PARA ENVIAR PARAMETROS PARA A OUTRA ACTIVITY
                Intent intent = new Intent(PedidosEdit.this, MenuActivity.class);
                String parametros = nomeAtend;
                intent.putExtra("infos", parametros);
                startActivity(intent);

                finish();
            } else {
                Toast.makeText(PedidosEdit.this, r, Toast.LENGTH_SHORT).show();
            }
                }

        @Override
        protected String doInBackground(String... params) {

            if (proname.trim().equals(""))
                msgRetornoPedido = "Preencha os Campos !!!";
            else {
                try {
                    configuracao = adpConfiguracao.getItem(0);
                    String ip = configuracao.getIp();
                    String driver = configuracao.getDriver();
                    String db = configuracao.getBancoDados();
                    String usuario = configuracao.getUsuario();
                    String senha = configuracao.getSenha();
                    Connection con = connectionRemote.CONN(ip, driver, db, usuario, senha);

                    if (con == null) {
                        msgRetornoPedido = "Error in connection with SQL server";
                    } else {
                        // create a sql date object so we can use it in our INSERT statement
                        Calendar calendar = Calendar.getInstance();
                        java.sql.Date dataPedido = new java.sql.Date(calendar.getTime().getTime());
                        java.sql.Time tempo = new java.sql.Time(calendar.getTime().getTime());

                        //TODO PESQUISAR COMO ABRE FECHA UMA TRANSACAO COM ROLBACK

                        // the mysql insert statement
                        String query = "Insert Into Pedidos (Codigo,Cliente,Atendente,Tempo,Entrega,Servico," +
                                "Total,Observacao,FecharConta,Mesa,Data," +
                                "Usuario,[Status],TaxaEntrega,Delivery, NroCaixa, TrocoDelivery)"
                                + " values (?, ?, ?, ?, ?,?, ?, ?, ?, ?,?, ?, ?, ?,?,?,?)";
                        PreparedStatement preparedStmt = con.prepareStatement(query);
                        preparedStmt.setString(1, codigoPedido);
                        preparedStmt.setInt(2, 99999);
                        preparedStmt.setInt(3, codAtendente);
                        preparedStmt.setTime(4, tempo);
                        preparedStmt.setInt(5, 0);
                        preparedStmt.setInt(6, 0);
                        preparedStmt.setDouble(7, valorTotalPedido);
                        preparedStmt.setString(8, "Venda Mobile");
                        preparedStmt.setInt(9, 0);
                        preparedStmt.setInt(10, 0);
                        preparedStmt.setDate(11, dataPedido);
                        preparedStmt.setString(12, "Mobile");
                        preparedStmt.setString(13, "A");
                        preparedStmt.setInt(14, 0);
                        preparedStmt.setInt(15, 0);
                        preparedStmt.setInt(16, 0);
                        preparedStmt.setInt(17, 0);
                        // execute the preparedstatement
                        preparedStmt.execute();
                        con.close();
                    }
                    msgRetornoPedido = "Pedido Adicionado !";
                    isSuccess = true;
                } catch (Exception ex) {
                    isSuccess = false;
                    msgRetornoPedido = "Faça o Pedido Novamente !";
                }
            }
            return msgRetornoPedido;
        }
    }

    public class AddProduto extends AsyncTask<String, String, String> {

        String z = "";
        Boolean isSuccess = false;

        //VALORES PARA O INSERT
        int codigoPedidosItens = Integer.parseInt(txtNroPedido.getText().toString());
        String proname = txtCliente.getText().toString();
        // int quantPedidosItens = Integer.parseInt(txtQuantProd.getText().toString());
        int quantPedidosItens = 1;
        int produto = Integer.parseInt(txtNroProduto.getText().toString());

        @Override
        protected void onPreExecute() {
            progressBarPedidosEdit.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {
            progressBarPedidosEdit.setVisibility(View.GONE);
            Toast.makeText(PedidosEdit.this, r, Toast.LENGTH_SHORT).show();
            if (isSuccess == true) {
                FillList fillList = new FillList();
                fillList.execute("");
            }
        }

        @Override
        protected String doInBackground(String... params) {

            if (quantPedidosItens < 1)
                z = "Preencha os Campos !!!";
            else {
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
                        // create a sql date object so we can use it in our INSERT statement
                        Calendar calendar = Calendar.getInstance();
                        java.sql.Date dataPedido = new java.sql.Date(calendar.getTime().getTime());
                        java.sql.Time tempo = new java.sql.Time(calendar.getTime().getTime());

                        // the mysql insert statement
                        String query = "Insert Into PedidosItens (Codigo,Produto,Quant,ValorUnit,Observacao,Desconto, Sequencial)"
                                + " values (?, ?, ?, ?, ?,?,?)";
                        PreparedStatement preparedStmt = con.prepareStatement(query);
                        preparedStmt.setInt(1, codigoPedidosItens);
                        preparedStmt.setInt(2, produto);
                        preparedStmt.setInt(3, quantPedidosItens);
                        preparedStmt.setBigDecimal(4, valorUnitPedItens);
                        preparedStmt.setString(5, observacaoPedItens);
                        preparedStmt.setInt(6, 0);
                        preparedStmt.setInt(7, produto);
                        z = "PRODUTO SEQUENCIAL ADICIONADO";
                        // execute the preparedstatement
                        preparedStmt.execute();
                        con.close();
                    }
                    //  z = "Added Successfully";
                    isSuccess = true;
                } catch (Exception ex) {
                            isSuccess = false;
                    z = "Exceptions" + ex.getMessage();
                        }

                    }
            return z;
                }
            }

    public class PesquisaAddProduto extends AsyncTask<String, String, String> {

        String z = "";
        Boolean isSuccess = false;

        int pedido = Integer.parseInt(txtNroPedido.getText().toString());
        int produto = Integer.parseInt(txtNroProduto.getText().toString());

        @Override
        protected void onPreExecute() {

            progressBarPedidosEdit.setVisibility(View.VISIBLE);
                }

        @Override
        protected void onPostExecute(String r) {
            progressBarPedidosEdit.setVisibility(View.GONE);
            // Toast.makeText(PedidosEdit.this, r, Toast.LENGTH_SHORT).show();
            if (isSuccess == true) {


                Toast.makeText(PedidosEdit.this, "Produto ja existe no Pedido !", Toast.LENGTH_SHORT).show();
            } else {

                AddProduto updatePro = new AddProduto();
                updatePro.execute("");

                Toast.makeText(PedidosEdit.this, "Success !", Toast.LENGTH_SHORT).show();
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
                            z = "Não foi possível conectar ao SQL server";
                        } else {
                            String query = "Select * From PedidosItens Where Codigo = '" + pedido + "' and Produto = '" + produto + "'";
                            Statement stmt = con.createStatement();
                            ResultSet rs = stmt.executeQuery(query);

                            if (rs.next()) {
                                //z = "Login successfull";
                                isSuccess = true;
                            } else {
                                // z = "Invalid Credentials";
                                isSuccess = false;
                            }

                            con.close();
                        }
                    } catch (Exception ex) {
                        isSuccess = false;
                        z = "Exceptions" + ex.getMessage();
                    }

            return z;
                }
            }

    public class PesquisaDelProduto extends AsyncTask<String, String, String> {

        String z = "";
        Boolean isSuccess = false;

        int pedido = Integer.parseInt(txtNroPedido.getText().toString());
        int produto = Integer.parseInt(txtNroProduto.getText().toString());

        @Override
        protected void onPreExecute() {

            progressBarPedidosEdit.setVisibility(View.VISIBLE);
                }

        @Override
        protected void onPostExecute(String r) {
            progressBarPedidosEdit.setVisibility(View.GONE);
            // Toast.makeText(PedidosEdit.this, r, Toast.LENGTH_SHORT).show();
            if (isSuccess == true) {

                alertDialogDelete();

                // Toast.makeText(PedidosEdit.this, "Produto ja existe no Pedido !", Toast.LENGTH_SHORT).show();
            } else {


                // Toast.makeText(PedidosEdit.this, "Success !", Toast.LENGTH_SHORT).show();
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
                            z = "Não foi possível conectar ao SQL server";
                        } else {
                            String query = "Select * From PedidosItens Where Codigo = '" + pedido + "' and Produto = '" + produto + "'";
                            Statement stmt = con.createStatement();
                            ResultSet rs = stmt.executeQuery(query);

                            if (rs.next()) {
                                //z = "Login successfull";
                                isSuccess = true;
                            } else {
                                // z = "Invalid Credentials";
                                isSuccess = false;
                            }

                            con.close();
                        }
                    } catch (Exception ex) {
                        isSuccess = false;
                        z = "Exceptions" + ex.getMessage();
                    }

            return z;
                }
            }

    public class UpdatePedido extends AsyncTask<String, String, String> {

        String z = "";
        Boolean isSuccess = false;

        String mesa = txtNroMesa.getText().toString();
        String observacao = txtObservacao.getText().toString();

        @Override
        protected void onPreExecute() {

            progressBarPedidosEdit.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {
            progressBarPedidosEdit.setVisibility(View.GONE);
            Toast.makeText(PedidosEdit.this, r, Toast.LENGTH_SHORT).show();
            if (isSuccess == true) {

                //  FillList fillList = new FillList();
                // fillList.execute("");
            }
        }

        @Override
        protected String doInBackground(String... params) {
            if (mesa.trim().equals(""))
                z = "Preencha o numero da mesa !";
            else {
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

                        SomaValorTotalPedido();

                        String data = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());

                        //String query = "Update Pedidos set Mesa='" + mesa + "' and Total='" + totalPedido + "' where Codigo=" + codigoPedido;
                        String query = "Update Pedidos " +
                                "set Mesa='" + mesa + "'," +
                                " Observacao='" + observacao + "'," +
                                " Total ='" + valorTotalPedido + "' " +
                                "where Codigo =" + codigoPedido;
                        PreparedStatement preparedStatement = con.prepareStatement(query);
                        preparedStatement.executeUpdate();
                        z = "Updated Successfully";

                        con.close();

                        isSuccess = true;
                    }
                } catch (Exception ex) {
                            isSuccess = false;
                    z = "Exceptions" + ex.getMessage();
                        }
                    }
            return z;
                }
            }

    public class UpdateProduto extends AsyncTask<String, String, String> {

        String z = "";
        Boolean isSuccess = false;

        String proname = txtCliente.getText().toString();
        String mesa = txtNroMesa.getText().toString();
        int produto = Integer.parseInt(txtNroProduto.getText().toString());

        @Override
        protected void onPreExecute() {

            progressBarPedidosEdit.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {
            progressBarPedidosEdit.setVisibility(View.GONE);
            Toast.makeText(PedidosEdit.this, r, Toast.LENGTH_SHORT).show();
            if (isSuccess == true) {
                //  FillList fillList = new FillList();
                // fillList.execute("");
            }
        }

        @Override
        protected String doInBackground(String... params) {
            if (proname.trim().equals(""))
                z = "Preencha os Campos !";
            else {
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

                        String data = new SimpleDateFormat("dd/MM/yyyy").format(Calendar.getInstance().getTime());

                        String query = "Update PedidosItens set Observacao='" + proname + "',Mesa='" + mesa + "' where Codigo=" + codigoPedido;
                        PreparedStatement preparedStatement = con.prepareStatement(query);
                        preparedStatement.executeUpdate();
                        z = "Updated Successfully";
                        con.close();

                        isSuccess = true;
                    }
                } catch (Exception ex) {
                            isSuccess = false;
                    z = "Exceptions" + ex.getMessage();
                        }
                    }
            return z;
                }
            }

    public class UpdateProdAdicionar extends AsyncTask<String, String, String> {

        String z = "";
        Boolean isSuccess = false;

        String proname = txtCliente.getText().toString();
        String mesa = txtNroMesa.getText().toString();
        int produto = Integer.parseInt(txtNroProduto.getText().toString());
        int qtd = Integer.parseInt(txtQtdProd.getText().toString());

        int QuantProd = qtd + 1;

        @Override
        protected void onPreExecute() {

            progressBarPedidosEdit.setVisibility(View.VISIBLE);
                }

        @Override
        protected void onPostExecute(String r) {
            progressBarPedidosEdit.setVisibility(View.GONE);
            // Toast.makeText(PedidosEdit.this, r, Toast.LENGTH_SHORT).show();
            if (isSuccess == true) {

                txtQtdProd.setText("" + QuantProd);

                FillList fillList = new FillList();
                fillList.execute("");
            }
        }

        @Override
        protected String doInBackground(String... params) {
            if (proname.trim().equals(""))
                z = "Preencha os Campos !";
            else {
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

                        String query = "Update PedidosItens set Quant='" + QuantProd + "' where Codigo='" + codigoPedido + "' and Produto =" + produto;
                        PreparedStatement preparedStatement = con.prepareStatement(query);
                        preparedStatement.executeUpdate();
                        // z = "Updated Successfully";
                        con.close();

                        isSuccess = true;
                    }
                } catch (Exception ex) {
                    isSuccess = false;
                    z = "Exceptions" + ex.getMessage();
                        }
                    }
            return z;
                }
            }

    public class UpdateProdDiminuir extends AsyncTask<String, String, String> {

        String z = "";
        Boolean isSuccess = false;

        String proname = txtCliente.getText().toString();
        String mesa = txtNroMesa.getText().toString();
        int produto = Integer.parseInt(txtNroProduto.getText().toString());
        int qtd = Integer.parseInt(txtQtdProd.getText().toString());

        int QuantProd = qtd - 1;

        @Override
        protected void onPreExecute() {

            progressBarPedidosEdit.setVisibility(View.VISIBLE);
                }

        @Override
        protected void onPostExecute(String r) {
            progressBarPedidosEdit.setVisibility(View.GONE);
            // Toast.makeText(PedidosEdit.this, r, Toast.LENGTH_SHORT).show();
            if (isSuccess == true) {

                txtQtdProd.setText("" + QuantProd);

                FillList fillList = new FillList();
                fillList.execute("");
            }
        }

        @Override
        protected String doInBackground(String... params) {
            if (QuantProd <= 0)
                z = "Quantidade igual a zero !";
            else {
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

                        String query = "Update PedidosItens set Quant='" + QuantProd + "' where Codigo='" + codigoPedido + "' and Produto =" + produto;
                        PreparedStatement preparedStatement = con.prepareStatement(query);
                        preparedStatement.executeUpdate();
                        // z = "Updated Successfully";
                        con.close();

                        isSuccess = true;
                    }
                } catch (Exception ex) {
                    isSuccess = false;
                    z = "Exceptions" + ex.getMessage();
                        }
                    }
            return z;
                }
            }

    public class PesquisaProdAdicionar extends AsyncTask<String, String, String> {

        String z = "";
        Boolean isSuccess = false;

        int pedido = Integer.parseInt(txtNroPedido.getText().toString());
        int produto = Integer.parseInt(txtNroProduto.getText().toString());

        @Override
        protected void onPreExecute() {

            progressBarPedidosEdit.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {
            progressBarPedidosEdit.setVisibility(View.GONE);
            //  Toast.makeText(PedidosEdit.this, r, Toast.LENGTH_SHORT).show();
            if (isSuccess == true) {

                UpdateProdAdicionar updateProAdd = new UpdateProdAdicionar();
                updateProAdd.execute("");

                // Toast.makeText(PedidosEdit.this, "Success !", Toast.LENGTH_SHORT).show();
            } else {

                //Toast.makeText(PedidosEdit.this, "Success !", Toast.LENGTH_SHORT).show();
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
                            z = "Não foi possível conectar ao SQL server";
                        } else {
                            String query = "Select * From PedidosItens Where Codigo = '" + pedido + "' and Produto = '" + produto + "'";
                            Statement stmt = con.createStatement();
                            ResultSet rs = stmt.executeQuery(query);

                            if (rs.next()) {
                                //z = "Login successfull";
                                isSuccess = true;
                            } else {
                                // z = "Invalid Credentials";
                                isSuccess = false;
                            }

                            con.close();
                        }
                    } catch (Exception ex) {
                        isSuccess = false;
                        z = "Exceptions" + ex.getMessage();
                    }

            return z;
                }
            }

    public class PesquisaProdDiminuir extends AsyncTask<String, String, String> {

        String z = "";
        Boolean isSuccess = false;

        int pedido = Integer.parseInt(txtNroPedido.getText().toString());
        int produto = Integer.parseInt(txtNroProduto.getText().toString());

        @Override
        protected void onPreExecute() {

            progressBarPedidosEdit.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {
            progressBarPedidosEdit.setVisibility(View.GONE);
            // Toast.makeText(PedidosEdit.this, r, Toast.LENGTH_SHORT).show();
            if (isSuccess == true) {

                UpdateProdDiminuir updateProdDiminuir = new UpdateProdDiminuir();
                updateProdDiminuir.execute("");

                // Toast.makeText(PedidosEdit.this, "Success !", Toast.LENGTH_SHORT).show();
            } else {

                //Toast.makeText(PedidosEdit.this, "Success !", Toast.LENGTH_SHORT).show();
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
                            z = "Não foi possível conectar ao SQL server";
                        } else {
                            String query = "Select * From PedidosItens Where Codigo = '" + pedido + "' and Produto = '" + produto + "'";
                            Statement stmt = con.createStatement();
                            ResultSet rs = stmt.executeQuery(query);

                            if (rs.next()) {
                                //z = "Login successfull";
                                isSuccess = true;
                            } else {
                                // z = "Invalid Credentials";
                                isSuccess = false;
                            }

                            con.close();
                        }
                    } catch (Exception ex) {
                        isSuccess = false;
                        z = "Exceptions" + ex.getMessage();
                    }

            return z;
                }
            }

    public class DeleteProduto extends AsyncTask<String, String, String> {

        String z = "";
        Boolean isSuccess = false;

        String pedido = txtNroPedido.getText().toString();
        String produto = txtNroProduto.getText().toString();


        @Override
        protected void onPreExecute() {
            progressBarPedidosEdit.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {
            progressBarPedidosEdit.setVisibility(View.GONE);
            // Toast.makeText(PedidosEdit.this, r, Toast.LENGTH_SHORT).show();
            if (isSuccess == true) {
                FillList fillList = new FillList();
                fillList.execute("");
            }

                }

        @Override
        protected String doInBackground(String... params) {
            if (produto.trim().equals(""))
                z = "Informe o Código do Produto";
            else {
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

                        String dates = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH)
                                .format(Calendar.getInstance().getTime());

                        String query = "Delete From PedidosItens where Codigo='" + pedido + "' and Produto='" + produto + "'";
                        PreparedStatement preparedStatement = con.prepareStatement(query);
                        preparedStatement.executeUpdate();
                        z = "Deleted Successfully";
                        con.close();
                        isSuccess = true;
                    }
                } catch (Exception ex) {
                            isSuccess = false;
                    z = "Exceptions " + ex.getMessage();
                        }
                    }
            return z;
                }
            }

    public class PesquisaMesaDisp extends AsyncTask<String, String, String> {

        String z = "";
        Boolean isSuccess = false;

        int pedido = Integer.parseInt(txtNroPedido.getText().toString());
        int mesa = Integer.parseInt(txtNroMesa.getText().toString());

        @Override
        protected void onPreExecute() {

            progressBarPedidosEdit.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {
            progressBarPedidosEdit.setVisibility(View.GONE);

            if (isSuccess == false) {

                UpdatePedido updateP = new UpdatePedido();
                updateP.execute("");

                PedidosPrintMobile();

                //METODO PARA ENVIAR PARAMETROS PARA A OUTRA ACTIVITY
                Intent intent = new Intent(PedidosEdit.this, MenuActivity.class);
                String parametros = nomeAtend;
                intent.putExtra("infos", parametros);
                startActivity(intent);

                finish();
                    } else {
                Toast.makeText(PedidosEdit.this, "A Mesa está ocupada !", Toast.LENGTH_SHORT).show();
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
                            z = "Não foi possível conectar ao SQL server";
                        } else {
                            String query = "Select Codigo," +
                                    "Data," +
                                    "Mesa, " +
                                    "FecharConta " +
                                    "From Pedidos " +
                                    "Where FecharConta = 0 and Mesa = '" + mesa + "' and Codigo <> '" + pedido + "'";

                            Statement stmt = con.createStatement();
                            ResultSet rs = stmt.executeQuery(query);

                            if (rs.next()) {
                                isSuccess = true;
                            } else {
                                isSuccess = false;
                            }

                            con.close();
                        }
                    } catch (Exception ex) {
                        isSuccess = false;
                        z = "Exceptions" + ex.getMessage();
                    }

            return z;
                }
            }

    private class ExibeDataListener implements View.OnClickListener, View.OnFocusChangeListener {

        @Override
        public void onClick(View v) {
            exibeData();

        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                exibeData();
            }
        }
            }

    private class SelecionaDataListener implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            Calendar calendar = Calendar.getInstance();

            calendar.set(year, monthOfYear, dayOfMonth);
            Date dataAt = calendar.getTime();

            DateFormat format = DateFormat.getDateInstance(DateFormat.SHORT);
            String dt = format.format(dataAt);

            dtData.setText(dt);

            // contato.setDataAtual(dataAt);

                }
            }

    public class FillList extends AsyncTask<String, String, String> {
        String z = "";

        String Pedido = txtNroPedido.getText().toString();

        List<Map<String, String>> prolist = new ArrayList<Map<String, String>>();

        @Override
        protected void onPreExecute() {

            progressBarPedidosEdit.setVisibility(View.VISIBLE);
                }

        @Override
        protected void onPostExecute(String r) {

            progressBarPedidosEdit.setVisibility(View.GONE);
            // Toast.makeText(PedidosEdit.this, r, Toast.LENGTH_SHORT).show();

            // SalvaValorTotalPedido updateP = new SalvaValorTotalPedido();
            // updateP.execute("");

            //String[] from = { "A", "B", "C" };
            String[] from = {"A", "B", "C", "D", "E"};
            int[] views = {
                    R.id.lblCodigoPro,
                    R.id.lblNomePro,
                    R.id.lblValorPro,
                    R.id.lblQtdPro,
                    R.id.lblTotalPro};

            final SimpleAdapter ADA = new SimpleAdapter(PedidosEdit.this,
                    prolist, R.layout.activity_produto_view, from,
                    views);
            listViewAddProduto.setAdapter(ADA);

            listViewAddProduto.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
                    HashMap<String, Object> obj = (HashMap<String, Object>) ADA
                            .getItem(arg2);

                    String codProd = (String) obj.get("A");
                    String nomeProd = (String) obj.get("B");
                    String Quant = (String) obj.get("D");
                    txtNroProduto.setText(codProd);
                    txtQtdProd.setText(Quant);

                    lblAddProduto.setEnabled(true);
                    lblDeleteProduto.setEnabled(true);

                    CodigoProduto = codProd;
                    QuantidadeProduto = Integer.parseInt(Quant.toString());
                    alertDialogEditarProdutos(codProd, nomeProd);

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

                    String query =
                            "Select " +
                                    "PED.Codigo, " +
                                    "PED.Produto, " +
                                    "Pro.Descricao, " +
                                    "PED.Observacao," +
                                    "format(PED.Quant, 'N0', 'pt-br') as Quant , " +
                                    "format(PED.ValorUnit, 'N', 'pt-br') as ValorUnit, " +
                                    "format(PED.Quant*PED.ValorUnit, 'N', 'pt-br') as Total " +
                                    "From PedidosItens PED " +
                                    "Inner join Produtos PRO on PRO.Codigo = PED.Produto " +
                                    "Where PED.Codigo = '" + Pedido + "'";

                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    Calendar calendar = Calendar.getInstance();

                    while (rs.next()) {

                        Map<String, String> datanum = new HashMap<String, String>();

                        datanum.put("A", rs.getString("Produto"));
                        datanum.put("B", rs.getString("Observacao"));
                        datanum.put("C", rs.getString("ValorUnit"));
                        datanum.put("D", rs.getString("Quant"));
                        datanum.put("E", rs.getString("Total"));

                        prolist.add(datanum);
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
