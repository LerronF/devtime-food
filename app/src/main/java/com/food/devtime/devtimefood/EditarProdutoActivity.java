    package com.food.devtime.devtimefood;

    import android.app.AlertDialog;
    import android.content.DialogInterface;
    import android.content.Intent;
    import android.database.sqlite.SQLiteDatabase;
    import android.os.AsyncTask;
    import android.os.Bundle;
    import android.support.v7.app.AppCompatActivity;
    import android.support.v7.widget.Toolbar;
    import android.view.MenuItem;
    import android.view.View;
    import android.widget.AdapterView;
    import android.widget.ArrayAdapter;
    import android.widget.Button;
    import android.widget.EditText;
    import android.widget.LinearLayout;
    import android.widget.ListView;
    import android.widget.RadioButton;
    import android.widget.SimpleAdapter;
    import android.widget.Spinner;
    import android.widget.TextView;
    import android.widget.Toast;

    import com.food.devtime.devtimefood.Database.ConnectionRemote;
    import com.food.devtime.devtimefood.Database.database;
    import com.food.devtime.devtimefood.Dominio.Entidade.Configuracao;
    import com.food.devtime.devtimefood.Dominio.RepositorioDevtimeFood;

    import java.math.BigDecimal;
    import java.sql.Connection;
    import java.sql.PreparedStatement;
    import java.sql.ResultSet;
    import java.sql.Statement;
    import java.text.SimpleDateFormat;
    import java.util.ArrayList;
    import java.util.Calendar;
    import java.util.HashMap;
    import java.util.List;
    import java.util.Locale;
    import java.util.Map;

    public class EditarProdutoActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

        ConnectionRemote connectionRemote;
        String codigoPedido = "", nomeAtend = "", paramMesa = "", observacao = "";
        String observacaoPedItens = "";
        String descproduto = "", produto1 = "", produto2 = "", produto3 = "", produto4 = "", tamanho = "", categoria = "";
        private RadioButton radioButonInteira, radioButonMeia, radioButonMaisSabores;
        private EditText txtNroProdutoValor, txtQuantProd, txtNroPedido, txtQtdPizza, txtValorTotal, txtNroProduto, txtNroProduto1, txtNroProduto2, txtNroProduto3, txtNroProduto4, txtQtdProduto, txtValorUnitProduto;
        private Spinner spinnerCategoriaProd, spinnerProdutosEdit, spinnerProdutosTamanho, spinnerProdutosEdit1, spinnerProdutosEdit2, spinnerProdutosEdit3, spinnerProdutosEdit4;
        private LinearLayout LinearProdutoEspecial, LinearProdutoEspecialTamanho, LinearProdutoEspecial1, LinearProdutoEspecial2, LinearProdutoEspecial3, LinearProdutoEspecial4;
        private Button btnInserirProduto, btnConfirmaDetalhes, btnAddTipo, btnDelTipo, btnAddQtd, btnDelQtd, btnIncluirProd, btnDeletarProd;
        private ListView listViewAddProduto;
        private TextView lblIncluirProd, lblDeletarProd;
        private AlertDialog alerta;
        //region VARIAVEIS PARA A CONEXAO AO BANCO LOCAL PARA CARREGAR AS CONFIGURACOES
        private ArrayAdapter<Configuracao> adpConfiguracao;
        private com.food.devtime.devtimefood.Database.database database;
        private SQLiteDatabase connLocal;
        //endregion
        private RepositorioDevtimeFood repositorioDevtimeFood;
        private Configuracao configuracao;
        private Toast toast;
        private long lastBackPressTime = 0;
        private int quantPedProMov = 0;
        private int controleMov = 0;
        private int quantEstoqueMov = 0;
        private int movimentaEstoque =0;
        private double valorTotalPedido;
        private int codAtendente;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_editar_produto);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setTitle("Incluir Produtos...");

            connectionRemote = new ConnectionRemote();

            //region CARREGA AS CONFIGURACOES PARA A CONEXAO
            try {

                database = new database(this);
                connLocal = database.getWritableDatabase();
                repositorioDevtimeFood = new RepositorioDevtimeFood(connLocal);
                adpConfiguracao = repositorioDevtimeFood.buscaConfiguracao(this);

            } catch (Exception e) {

                Toast.makeText(EditarProdutoActivity.this, "Erro na configuracao", Toast.LENGTH_SHORT).show();
            }
            //endregion

            // FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            // fab.setOnClickListener(new View.OnClickListener() {
            //  @Override
            //  public void onClick(View view) {


            //  Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
            //          .setAction("Action", null).show();
            //     }
            // });

            // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            txtQtdProduto = (EditText) findViewById(R.id.txtQtdProduto);
            txtValorUnitProduto = (EditText) findViewById(R.id.txtValorUnitProduto);
            txtNroPedido = (EditText) findViewById(R.id.txtNroPedido);
            txtQuantProd = (EditText) findViewById(R.id.txtQuantProd);
            txtQtdPizza = (EditText) findViewById(R.id.txtQtdPizza);
            txtValorTotal = (EditText) findViewById(R.id.txtValorTotal);
            txtNroProduto = (EditText) findViewById(R.id.txtNroProduto);
            txtNroProduto1 = (EditText) findViewById(R.id.txtNroProduto1);
            txtNroProduto2 = (EditText) findViewById(R.id.txtNroProduto2);
            txtNroProduto3 = (EditText) findViewById(R.id.txtNroProduto3);
            txtNroProduto4 = (EditText) findViewById(R.id.txtNroProduto4);
            txtNroProdutoValor = (EditText) findViewById(R.id.txtNroProdutoValor);

            spinnerCategoriaProd = (Spinner) findViewById(R.id.spinnerCategoriaProd);
            spinnerProdutosEdit = (Spinner) findViewById(R.id.spinnerProdutoEdit);
            spinnerProdutosEdit1 = (Spinner) findViewById(R.id.spinnerProdutoEdit1);
            spinnerProdutosEdit2 = (Spinner) findViewById(R.id.spinnerProdutoEdit2);
            spinnerProdutosEdit3 = (Spinner) findViewById(R.id.spinnerProdutoEdit3);
            spinnerProdutosEdit4 = (Spinner) findViewById(R.id.spinnerProdutoEdit4);
            spinnerProdutosTamanho = (Spinner) findViewById(R.id.spinnerProdutoTamanho);

            LinearProdutoEspecial = (LinearLayout) findViewById(R.id.LinearProdutoEspecial);
            LinearProdutoEspecialTamanho = (LinearLayout) findViewById(R.id.LinearTamanhoProdutoEspecial);
            LinearProdutoEspecial1 = (LinearLayout) findViewById(R.id.LinearProdutoEspecial1);
            LinearProdutoEspecial2 = (LinearLayout) findViewById(R.id.LinearProdutoEspecial2);
            LinearProdutoEspecial3 = (LinearLayout) findViewById(R.id.LinearProdutoEspecial3);
            LinearProdutoEspecial4 = (LinearLayout) findViewById(R.id.LinearProdutoEspecial4);

            btnInserirProduto = (Button) findViewById(R.id.btnInserirProduto);
            btnConfirmaDetalhes = (Button) findViewById(R.id.btnConfirmaDetalhes);
            btnAddTipo = (Button) findViewById(R.id.btnAddTipo);
            btnDelTipo = (Button) findViewById(R.id.btnDelTipo);
            btnAddQtd = (Button) findViewById(R.id.btnAddQtd);
            btnDelQtd = (Button) findViewById(R.id.btnDelQtd);

            radioButonInteira = (RadioButton) findViewById(R.id.radioButtonInteira);
            radioButonMeia = (RadioButton) findViewById(R.id.radioButtonMeia);
            radioButonMaisSabores = (RadioButton) findViewById(R.id.radioButtonMaisSabores);

            lblIncluirProd = (TextView) findViewById(R.id.lblIncluirProd);
            lblDeletarProd = (TextView) findViewById(R.id.lblDeletarProd);
            btnIncluirProd = (Button) findViewById(R.id.btnIncluirProd);
            btnDeletarProd = (Button) findViewById(R.id.btnDeletarProd);

            listViewAddProduto = (ListView) findViewById(R.id.listViewAddProduto);
            listViewAddProduto.setOnItemClickListener(this);

            radioButonInteira.setOnClickListener(this);
            radioButonMeia.setOnClickListener(this);
            radioButonMaisSabores.setOnClickListener(this);

            btnInserirProduto.setOnClickListener(this);
            btnConfirmaDetalhes.setOnClickListener(this);
            btnAddTipo.setOnClickListener(this);
            btnDelTipo.setOnClickListener(this);
            btnAddQtd.setOnClickListener(this);
            btnDelQtd.setOnClickListener(this);

            lblIncluirProd.setOnClickListener(this);
            lblDeletarProd.setOnClickListener(this);
            btnIncluirProd.setOnClickListener(this);
            btnDeletarProd.setOnClickListener(this);

            LinearProdutoEspecial.setVisibility(View.GONE);

            PreencheCategoria();

            //region SPINNER DA CATEGORIA
            //Método do Spinner para capturar o item selecionado
            spinnerCategoriaProd.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View v, int posicao, long id) {
                    //pega nome pela posição
                    String nome = parent.getItemAtPosition(posicao).toString();

                    categoria = nome;

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
            spinnerProdutosEdit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View v, int posicao, long id) {
                    //pega nome pela posição
                    String nome = parent.getItemAtPosition(posicao).toString();
                    // observacaoPedItens = nome;

                    descproduto = nome + " - ";

                    PreencheNroProduto(nome);

                    PreencheProdutosTamanho(nome);

                    //imprime um Toast na tela com o nome que foi selecionado
                    // Toast.makeText(PedidosEdit.this, "Nome Selecionado: " + nome, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
            //endregion

            //region SPINNER DOS PRODUTOS1
            //Método do Spinner para capturar o item selecionado
            spinnerProdutosEdit1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View v, int posicao, long id) {
                    //pega nome pela posição
                    String nome = parent.getItemAtPosition(posicao).toString();
                    // observacaoPedItens = nome;
                    produto1 = nome + " - ";
                    PreencheNroProduto1(nome);

                    //imprime um Toast na tela com o nome que foi selecionado
                    // Toast.makeText(PedidosEdit.this, "Nome Selecionado: " + nome, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
            //endregion

            //region SPINNER DOS PRODUTOS2
            //Método do Spinner para capturar o item selecionado
            spinnerProdutosEdit2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View v, int posicao, long id) {
                    //pega nome pela posição
                    String nome = parent.getItemAtPosition(posicao).toString();
                    // observacaoPedItens = nome;

                    produto2 = nome + " - ";

                    PreencheNroProduto2(nome);

                    //imprime um Toast na tela com o nome que foi selecionado
                    // Toast.makeText(PedidosEdit.this, "Nome Selecionado: " + nome, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
            //endregion

            //region SPINNER DOS PRODUTOS3
            //Método do Spinner para capturar o item selecionado
            spinnerProdutosEdit3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View v, int posicao, long id) {
                    //pega nome pela posição
                    String nome = parent.getItemAtPosition(posicao).toString();
                    // observacaoPedItens = nome;

                    produto3 = nome + " - ";

                    PreencheNroProduto3(nome);

                    //imprime um Toast na tela com o nome que foi selecionado
                    // Toast.makeText(PedidosEdit.this, "Nome Selecionado: " + nome, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
            //endregion

            //region SPINNER DOS PRODUTOS4
            //Método do Spinner para capturar o item selecionado
            spinnerProdutosEdit4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View v, int posicao, long id) {
                    //pega nome pela posição
                    String nome = parent.getItemAtPosition(posicao).toString();
                    // observacaoPedItens = nome;

                    produto4 = nome + " - ";

                    PreencheNroProduto4(nome);

                    //imprime um Toast na tela com o nome que foi selecionado
                    // Toast.makeText(PedidosEdit.this, "Nome Selecionado: " + nome, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
            //endregion

            //region SPINNER DOS TAMANHOS
            //Método do Spinner para capturar o item selecionado
            spinnerProdutosTamanho.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View v, int posicao, long id) {
                    //pega nome pela posição
                    String nome = parent.getItemAtPosition(posicao).toString();

                    tamanho = nome;

                    //imprime um Toast na tela com o nome que foi selecionado
                    // Toast.makeText(PedidosEdit.this, "Nome Selecionado: " + nome, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });
            //endregion

            //region RETORNA OS VALORES DA ACTIVITY ANTERIOR E PREENCHER OS COMPONETES DA TELA
            try {
                observacao = this.getIntent().getStringExtra("Observacao");

                paramMesa = this.getIntent().getStringExtra("paramMesa");

                nomeAtend = this.getIntent().getStringExtra("NomeAtend");
                PreencheCodAtendente(nomeAtend);
                String info = this.getIntent().getStringExtra("infos");
                txtNroPedido.setText(info);

            } catch (Exception e) {


            }
            //endregion

            txtNroPedido.setEnabled(false);
            txtValorTotal.setEnabled(false);
            // txtQtdPizza.setEnabled(false);
            txtQtdProduto.setText("1");
            txtQtdPizza.setText("1");


            // searchEditText.setOnFocusChangeListener(new OnFocusChangeListener()
            //  {
            //      @Override
            ///      public void onFocusChange(View v, boolean hasFocus)
            //      {
            //           if (false == hasFocus) {
            //              ((InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
            //                      searchEditText.getWindowToken(), 0);
            // //          }
            //       }
            //    });

            FillList fillList = new FillList();
            fillList.execute("");

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
                    Toast.makeText(EditarProdutoActivity.this, "Error in connection with SQL server", Toast.LENGTH_SHORT).show();
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

                Toast.makeText(EditarProdutoActivity.this, "erro " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                //  z = "Error retrieving data from table" + ex.getMessage();

            }
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
        public void finish() {

            super.finish();
        }

        @Override
        public void onClick(View v) {

            switch (v.getId()) {

                case R.id.btnAddQtd:

                    int numQtd1 = Integer.parseInt(txtQtdProduto.getText().toString());

                    int resQtd1 = numQtd1 + 1;

                    txtQtdProduto.setText(String.valueOf(resQtd1));

                    spinnerCategoriaProd.setEnabled(true);
                    spinnerProdutosEdit.setEnabled(true);

                    break;
                case R.id.btnDelQtd:

                    int numQtd2 = Integer.parseInt(txtQtdProduto.getText().toString());

                    int resQtd2 = numQtd2 - 1;

                    if (resQtd2 != 0) {
                        txtQtdProduto.setText(String.valueOf(resQtd2));
                    }

                    break;
                case R.id.btnAddTipo:

                    int num1 = Integer.parseInt(txtQtdPizza.getText().toString());

                    int res1 = num1 + 1;

                    txtQtdPizza.setText(String.valueOf(res1));

                    break;
                case R.id.btnDelTipo:

                    int num2 = Integer.parseInt(txtQtdPizza.getText().toString());

                    int res2 = num2 - 1;

                    if (res2 != 0) {
                        txtQtdPizza.setText(String.valueOf(res2));
                    }

                    break;
                case R.id.btnDeletarProd:

                    PesquisaDelProduto pesquisaDelProduto = new PesquisaDelProduto();
                    pesquisaDelProduto.execute("");

                    break;

                case R.id.btnIncluirProd:

                    btnInserirProduto.setVisibility(View.INVISIBLE);

                    PesquisaAddProduto pesquisaProduto = new PesquisaAddProduto();
                    pesquisaProduto.execute("");

                    LinearProdutoEspecialTamanho.setVisibility(View.GONE);
                    LinearProdutoEspecial1.setVisibility(View.GONE);
                    LinearProdutoEspecial2.setVisibility(View.GONE);
                    LinearProdutoEspecial3.setVisibility(View.GONE);
                    LinearProdutoEspecial4.setVisibility(View.GONE);

                    btnInserirProduto.setVisibility(View.VISIBLE);

                    break;

                case R.id.btnConfirmaDetalhes:

                    try {

                        int Qtd = Integer.parseInt(txtQtdPizza.getText().toString());

                        if (Qtd >= 1) {

                            if (Qtd == 1) {

                                LinearProdutoEspecialTamanho.setVisibility(View.GONE);
                                LinearProdutoEspecial1.setVisibility(View.VISIBLE);

                            }

                            if (Qtd == 2) {

                                LinearProdutoEspecialTamanho.setVisibility(View.GONE);
                                LinearProdutoEspecial1.setVisibility(View.VISIBLE);
                                LinearProdutoEspecial2.setVisibility(View.VISIBLE);
                            }

                            if (Qtd == 3) {

                                LinearProdutoEspecialTamanho.setVisibility(View.GONE);
                                LinearProdutoEspecial1.setVisibility(View.VISIBLE);
                                LinearProdutoEspecial2.setVisibility(View.VISIBLE);
                                LinearProdutoEspecial3.setVisibility(View.VISIBLE);
                            }

                            if (Qtd == 4) {

                                LinearProdutoEspecialTamanho.setVisibility(View.GONE);
                                LinearProdutoEspecial1.setVisibility(View.VISIBLE);
                                LinearProdutoEspecial2.setVisibility(View.VISIBLE);
                                LinearProdutoEspecial3.setVisibility(View.VISIBLE);
                                LinearProdutoEspecial4.setVisibility(View.VISIBLE);
                            } else if (Qtd > 4) {

                                LinearProdutoEspecialTamanho.setVisibility(View.GONE);
                                LinearProdutoEspecial1.setVisibility(View.VISIBLE);
                                LinearProdutoEspecial2.setVisibility(View.VISIBLE);
                                LinearProdutoEspecial3.setVisibility(View.VISIBLE);
                                LinearProdutoEspecial4.setVisibility(View.VISIBLE);

                            }

                        }

                    } catch (Exception e) {

                    }

                    if (radioButonMeia.isChecked()) {

                        LinearProdutoEspecialTamanho.setVisibility(View.GONE);
                        LinearProdutoEspecial1.setVisibility(View.VISIBLE);

                    }

                    break;

                case R.id.radioButtonMaisSabores:

                    // txtQtdPizza.setEnabled(true);
                    // txtQtdPizza.requestFocus();

                    break;

                case R.id.btnInserirProduto:

                    //FillList fillList = new FillList();
                    //fillList.execute("");

                    SomaValorTotalPedido();
                    if (valorTotalPedido > 0) {

                        UpdatePedido updateP = new UpdatePedido();
                        updateP.execute("");

                        PedidosPrintMobile();

                        //METODO PARA ENVIAR PARAMETROS PARA A OUTRA ACTIVITY
                        Intent intent = new Intent(EditarProdutoActivity.this, MenuActivity.class);
                        String parametros = nomeAtend;
                        intent.putExtra("infos", parametros);
                        startActivity(intent);

                        finish();

                    } else {
                        alertDialogDeletePedido();

                        // super.onBackPressed();
                    }

                    break;
            }
        }

        private void SomaValorTotalPedido() {

            String Pedido = txtNroPedido.getText().toString();

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
                    Toast.makeText(EditarProdutoActivity.this, "Error in connection with SQL server", Toast.LENGTH_SHORT).show();
                } else {

                    //String query = "Select * From Produtos Order by Descricao";
                    String query = "Select Sum((PED.Quant * PED.ValorUnit)- PED.Desconto ) as Total  " +
                            "From PedidosItens PED  " +
                            "Where PED.Codigo = '" + Pedido + "'";

                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {

                        valorTotalPedido = rs.getDouble("Total");
                    }

                    con.close();
                    //   Toast.makeText(PedidosEdit.this, "PASSOU " +valorTotalPedido , Toast.LENGTH_SHORT).show();
                }
            } catch (Exception ex) {

                Toast.makeText(EditarProdutoActivity.this, "erro " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                //  z = "Error retrieving data from table" + ex.getMessage();

            }
        }

        private void PedidosPrintMobile() {

            String Pedido = txtNroPedido.getText().toString();

            try {
                configuracao = adpConfiguracao.getItem(0);

                String ip = configuracao.getIp();
                String driver = configuracao.getDriver();
                String db = configuracao.getBancoDados();
                String usuario = configuracao.getUsuario();
                String senha = configuracao.getSenha();

                Connection con = connectionRemote.CONN(ip, driver, db, usuario, senha);

                if (con == null) {
                    Toast.makeText(EditarProdutoActivity.this, "Erro ao Conectar o SQL server", Toast.LENGTH_SHORT).show();
                } else {

                    String queryB = "Update PedidosPrintMobile set Mesa='" + paramMesa + "', Status = 'A', Atendente ='" + codAtendente + "' where Pedido=" + Pedido;
                    PreparedStatement preparedStatementB = con.prepareStatement(queryB);
                    preparedStatementB.executeUpdate();

                    con.close();
                }
            } catch (Exception ex) {

                Toast.makeText(EditarProdutoActivity.this, "erro " + ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

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
                    Toast.makeText(EditarProdutoActivity.this, "Error in connection with SQL server", Toast.LENGTH_SHORT).show();
                } else {
                    String query = "Select * From ProdutoCategoria Order by Categoria";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    List<String> categoria = new ArrayList<String>();

                    while (rs.next()) {

                        //categoria.add("Código: "+rs.getString("Codigo"));
                        categoria.add(rs.getString("Categoria"));
                    }

                    con.close();

                   // ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, categoria);
                   // ArrayAdapter<String> spinnerArrayAdapter = arrayAdapter;

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, categoria);
                    ArrayAdapter<String> spinnerArrayAdapter = arrayAdapter;


                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCategoriaProd.setAdapter(spinnerArrayAdapter);
                }
            } catch (Exception ex) {

                Toast.makeText(EditarProdutoActivity.this, "erro " + ex.getMessage(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(EditarProdutoActivity.this, "Error in connection with SQL server", Toast.LENGTH_SHORT).show();
                } else {

                    String query = "Select *  " +
                            "From Produtos P " +
                            "Inner join ProdutoCategoria PC on PC.Codigo = P.CategoriaProduto  " +
                            "Inner join Estoque E on E.Produto = P.Codigo " +
                            "Where PC.Categoria = '" + descricao + "' Order By Descricao ";

                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    List<String> produto = new ArrayList<String>();

                    while (rs.next()) {

                        produto.add(rs.getString("Descricao"));// +"           "+  Qtd.:"+ rs.getString("Quant"));
                        // produto.add("R$ " + rs.getString("Quant"));
                       // produto.add(" ");
                    }

                    con.close();

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, produto);
                    ArrayAdapter<String> spinnerArrayAdapter = arrayAdapter;
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerProdutosEdit.setAdapter(spinnerArrayAdapter);
                    spinnerProdutosEdit1.setAdapter(spinnerArrayAdapter);
                    spinnerProdutosEdit2.setAdapter(spinnerArrayAdapter);
                    spinnerProdutosEdit3.setAdapter(spinnerArrayAdapter);
                    spinnerProdutosEdit4.setAdapter(spinnerArrayAdapter);
                }
            } catch (Exception ex) {

                Toast.makeText(EditarProdutoActivity.this, "erro " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                //  z = "Error retrieving data from table" + ex.getMessage();

            }
        }

        private void PreencheNroProduto1(String descricao) {

            try {

                configuracao = adpConfiguracao.getItem(0);

                String ip = configuracao.getIp();
                String driver = configuracao.getDriver();
                String db = configuracao.getBancoDados();
                String usuario = configuracao.getUsuario();
                String senha = configuracao.getSenha();

                Connection con = connectionRemote.CONN(ip, driver, db, usuario, senha);

                if (con == null) {
                    Toast.makeText(EditarProdutoActivity.this, "Error in connection with SQL server", Toast.LENGTH_SHORT).show();
                } else {

                    //String query = "Select * From Produtos Order by Descricao";
                    String query = "Select * From Produtos Where Descricao = '" + descricao + "'  Order By Descricao ";

                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {

                        txtNroProduto1.setText(Integer.toString(rs.getInt("Codigo")));

                        BigDecimal valorUnitP = rs.getBigDecimal("PrecoVenda");

                        BigDecimal txtValor;

                        txtValor = new BigDecimal(txtValorTotal.getText().toString());

                        if (txtValor.compareTo(valorUnitP) == -1) {

                            // Toast.makeText(EditarProdutoActivity.this,"Produto é maior", Toast.LENGTH_SHORT).show();
                            txtValorTotal.setText(valorUnitP.toString());
                            txtValorUnitProduto.setText(valorUnitP.toString());

                        } else if (txtValor.compareTo(valorUnitP) == 1) {

                            // Toast.makeText(EditarProdutoActivity.this, "txtValor é Maior", Toast.LENGTH_SHORT).show();
                            //txtValorTotal.setText(valorUnitP.toString());

                        }
                    }

                    con.close();

                }
            } catch (Exception ex) {

                Toast.makeText(EditarProdutoActivity.this, "erro " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                //  z = "Error retrieving data from table" + ex.getMessage();

            }
        }

        private void PreencheNroProduto2(String descricao) {

            try {

                configuracao = adpConfiguracao.getItem(0);

                String ip = configuracao.getIp();
                String driver = configuracao.getDriver();
                String db = configuracao.getBancoDados();
                String usuario = configuracao.getUsuario();
                String senha = configuracao.getSenha();

                Connection con = connectionRemote.CONN(ip, driver, db, usuario, senha);

                if (con == null) {
                    Toast.makeText(EditarProdutoActivity.this, "Error in connection with SQL server", Toast.LENGTH_SHORT).show();
                } else {

                    //String query = "Select * From Produtos Order by Descricao";
                    String query = "Select * From Produtos Where Descricao = '" + descricao + "' Order By Descricao ";

                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {

                        txtNroProduto2.setText(Integer.toString(rs.getInt("Codigo")));

                        BigDecimal valorUnitP = rs.getBigDecimal("PrecoVenda");

                        BigDecimal txtValor;

                        txtValor = new BigDecimal(txtValorTotal.getText().toString());

                        if (txtValor.compareTo(valorUnitP) == -1) {

                            txtValorUnitProduto.setText(valorUnitP.toString());
                            txtValorTotal.setText(valorUnitP.toString());

                        } else if (txtValor.compareTo(valorUnitP) == 1) {

                            //  Toast.makeText(EditarProdutoActivity.this, "txtValor é Maior", Toast.LENGTH_SHORT).show();
                            //txtValorTotal.setText(valorUnitP.toString());

                        }

                    }

                    con.close();

                }
            } catch (Exception ex) {

                Toast.makeText(EditarProdutoActivity.this, "erro " + ex.getMessage(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(EditarProdutoActivity.this, "Error in connection with SQL server", Toast.LENGTH_SHORT).show();
                } else {

                    //String query = "Select * From Produtos Order by Descricao";
                    String query = "Select * From Produtos P Inner join Estoque E on E.Produto = P.Codigo Where Descricao = '" + descricao + "' Order By P.Descricao";

                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {

                        txtNroProduto.setText(Integer.toString(rs.getInt("Codigo")));
                        BigDecimal aux = rs.getBigDecimal("Personalizado");
                        BigDecimal valorUnitP = rs.getBigDecimal("PrecoVenda");
                        BigDecimal quantProd = rs.getBigDecimal("Quant");

                        txtQuantProd.setText(quantProd.toString());
                        txtValorTotal.setText(valorUnitP.toString());
                        txtNroProdutoValor.setText(valorUnitP.toString());

                        txtValorUnitProduto.setText(valorUnitP.toString());

                        if (aux.compareTo(BigDecimal.ZERO) == 0) {

                            LinearProdutoEspecial.setVisibility(View.VISIBLE);
                            LinearProdutoEspecialTamanho.setVisibility(View.VISIBLE);
                            LinearProdutoEspecial1.setVisibility(View.GONE);
                            LinearProdutoEspecial2.setVisibility(View.GONE);
                            LinearProdutoEspecial3.setVisibility(View.GONE);
                            LinearProdutoEspecial4.setVisibility(View.GONE);

                        } else {

                            LinearProdutoEspecial.setVisibility(View.GONE);
                        }
                    }

                    con.close();

                }
            } catch (Exception ex) {

                Toast.makeText(EditarProdutoActivity.this, "erro " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                //  z = "Error retrieving data from table" + ex.getMessage();

            }
        }

        private void PreencheNroProduto3(String descricao) {

            try {

                configuracao = adpConfiguracao.getItem(0);

                String ip = configuracao.getIp();
                String driver = configuracao.getDriver();
                String db = configuracao.getBancoDados();
                String usuario = configuracao.getUsuario();
                String senha = configuracao.getSenha();

                Connection con = connectionRemote.CONN(ip, driver, db, usuario, senha);

                if (con == null) {
                    Toast.makeText(EditarProdutoActivity.this, "Error in connection with SQL server", Toast.LENGTH_SHORT).show();
                } else {

                    //String query = "Select * From Produtos Order by Descricao";
                    String query = "Select * From Produtos Where Descricao = '" + descricao + "' Order By Descricao ";

                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {

                        txtNroProduto3.setText(Integer.toString(rs.getInt("Codigo")));

                        BigDecimal valorUnitP = rs.getBigDecimal("PrecoVenda");

                        BigDecimal txtValor;

                        txtValor = new BigDecimal(txtValorTotal.getText().toString());

                        if (txtValor.compareTo(valorUnitP) == -1) {

                            txtValorUnitProduto.setText(valorUnitP.toString());
                            txtValorTotal.setText(valorUnitP.toString());

                        } else if (txtValor.compareTo(valorUnitP) == 1) {

                            //  Toast.makeText(EditarProdutoActivity.this, "txtValor é Maior", Toast.LENGTH_SHORT).show();
                            //txtValorTotal.setText(valorUnitP.toString());

                        }

                    }

                    con.close();

                }
            } catch (Exception ex) {

                Toast.makeText(EditarProdutoActivity.this, "erro " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                //  z = "Error retrieving data from table" + ex.getMessage();

            }
        }

        private void PreencheNroProduto4(String descricao) {

            try {

                configuracao = adpConfiguracao.getItem(0);

                String ip = configuracao.getIp();
                String driver = configuracao.getDriver();
                String db = configuracao.getBancoDados();
                String usuario = configuracao.getUsuario();
                String senha = configuracao.getSenha();

                Connection con = connectionRemote.CONN(ip, driver, db, usuario, senha);

                if (con == null) {
                    Toast.makeText(EditarProdutoActivity.this, "Error in connection with SQL server", Toast.LENGTH_SHORT).show();
                } else {

                    //String query = "Select * From Produtos Order by Descricao";
                    String query = "Select * From Produtos Where Descricao = '" + descricao + "' Order By Descricao ";

                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    while (rs.next()) {

                        txtNroProduto4.setText(Integer.toString(rs.getInt("Codigo")));

                        BigDecimal valorUnitP = rs.getBigDecimal("PrecoVenda");

                        BigDecimal txtValor;

                        txtValor = new BigDecimal(txtValorTotal.getText().toString());

                        if (txtValor.compareTo(valorUnitP) == -1) {

                            txtValorUnitProduto.setText(valorUnitP.toString());
                            txtValorTotal.setText(valorUnitP.toString());

                        } else if (txtValor.compareTo(valorUnitP) == 1) {

                            // Toast.makeText(EditarProdutoActivity.this, "txtValor é Maior", Toast.LENGTH_SHORT).show();
                            //txtValorTotal.setText(valorUnitP.toString());

                        }

                    }

                    con.close();

                }
            } catch (Exception ex) {

                Toast.makeText(EditarProdutoActivity.this, "erro " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                //  z = "Error retrieving data from table" + ex.getMessage();

            }
        }

        private void PreencheProdutosTamanho(String descricao) {

            try {

                configuracao = adpConfiguracao.getItem(0);

                String ip = configuracao.getIp();
                String driver = configuracao.getDriver();
                String db = configuracao.getBancoDados();
                String usuario = configuracao.getUsuario();
                String senha = configuracao.getSenha();

                Connection con = connectionRemote.CONN(ip, driver, db, usuario, senha);

                if (con == null) {
                    Toast.makeText(EditarProdutoActivity.this, "Error in connection with SQL server", Toast.LENGTH_SHORT).show();
                } else {

                    String querys =
                            "SELECT PPTamanho1," +
                                    "PPTamanho2," +
                                    "PPTamanho3," +
                                    "PPTamanho4," +
                                    "PPTamanho5 " +
                                    "FROM Empresas";

                    String query =
                            "SELECT Codigo, " +
                                    "Descricao," +
                                    "TamDisp1," +
                                    "PrecoVendaTam1," +
                                    "PrecoCustoTam1," +
                                    "TamDisp2," +
                                    "PrecoVendaTam2," +
                                    "PrecoCustoTam2," +
                                    "TamDisp3," +
                                    "PrecoVendaTam3," +
                                    "PrecoCustoTam3," +
                                    "TamDisp4," +
                                    "PrecoVendaTam4," +
                                    "PrecoCustoTam4," +
                                    "TamDisp5," +
                                    "PrecoVendaTam5," +
                                    "PrecoCustoTam5 " +
                                    "FROM Produtos " +
                                    "WHERE Personalizado = 0 AND Descricao = '" + descricao + "'";

                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    PreparedStatement ps2 = con.prepareStatement(querys);
                    ResultSet rs2 = ps2.executeQuery();

                    List<String> produto = new ArrayList<String>();

                    while (rs.next()) {

                        while (rs2.next()) {

                            int aux1 = rs.getInt("TamDisp1");
                            int aux2 = rs.getInt("TamDisp2");
                            int aux3 = rs.getInt("TamDisp3");
                            int aux4 = rs.getInt("TamDisp4");
                            int aux5 = rs.getInt("TamDisp5");

                            if (aux1 == 0) {
                                produto.add(rs2.getString("PPTamanho1"));
                            }
                            if (aux2 == 0) {
                                produto.add(rs2.getString("PPTamanho2"));
                            }
                            if (aux3 == 0) {
                                produto.add(rs2.getString("PPTamanho3"));
                            }
                            if (aux4 == 0) {
                                produto.add(rs2.getString("PPTamanho4"));
                            }
                            if (aux5 == 0) {
                                produto.add(rs2.getString("PPTamanho5"));
                            }
                        }
                    }

                    con.close();

                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, produto);
                    ArrayAdapter<String> spinnerArrayAdapter = arrayAdapter;
                    spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerProdutosTamanho.setAdapter(spinnerArrayAdapter);
                }
            } catch (Exception ex) {

                Toast.makeText(EditarProdutoActivity.this, "erro " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                //  z = "Error retrieving data from table" + ex.getMessage();

            }
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

                    Toast.makeText(EditarProdutoActivity.this, "Registro deletado !", Toast.LENGTH_SHORT).show();

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

        private void MovimentaEstoque() {

            controleMov = 0;
            int quantidade = 0;
            int calcQtd = 0;
            int qtdMov = 0;
            int pedidoMov = Integer.parseInt(txtNroPedido.getText().toString());
            int produtoMov = Integer.parseInt(txtNroProduto.getText().toString());
            qtdMov = Integer.parseInt(txtQtdProduto.getText().toString());

            try {
                configuracao = adpConfiguracao.getItem(0);

                String ip = configuracao.getIp();
                String driver = configuracao.getDriver();
                String db = configuracao.getBancoDados();
                String usuario = configuracao.getUsuario();
                String senha = configuracao.getSenha();

                Connection con = connectionRemote.CONN(ip, driver, db, usuario, senha);

                if (con == null) {
                    Toast.makeText(EditarProdutoActivity.this, "Error in connection with SQL server", Toast.LENGTH_SHORT).show();
                } else {

                    Statement stmtA = con.createStatement();
                    //VERIFICA SE O PRODUTO CONTROLA ESTOQUE
                    ResultSet rstA = stmtA.executeQuery("Select ControleES From  Produtos Where Codigo =" + produtoMov);
                    while (rstA.next()) {
                        controleMov = rstA.getInt("ControleES");
                    }
                    //VERIFICA SE AINDA TEM EM ESTOQUE
                    rstA = stmtA.executeQuery("Select Quant From  Estoque Where Produto =" + produtoMov);
                    while (rstA.next()) {
                        quantEstoqueMov = rstA.getInt("Quant");
                    }

                    if (controleMov > 0) { // 0 == Nao 1 == Sim

                        movimentaEstoque = 1;

                        if (quantEstoqueMov >= qtdMov) {

                            Statement stmt = con.createStatement();

                            ResultSet rst = stmt.executeQuery("Select Quant From PedidosItens " +
                                    "Where Codigo ='" + pedidoMov + "' and Produto =" + produtoMov);
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
                            String queryC = "Update Estoque set Quant =" + quantidade + " where Produto =" + produtoMov;
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

                Toast.makeText(EditarProdutoActivity.this, "erro " + ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

        //region CRUD DO PEDIDOS E PEDIDOSITENS

        private void alertDialogSalvarPedido() {
            //Cria o gerador do AlertDialog
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            //define o titulo
            builder.setTitle("Atenção");
            //define a mensagem
            builder.setMessage("Salvar Pedido ?");
            //define um botão como positivo
            builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface arg0, int arg1) {

                    UpdatePedido updateP = new UpdatePedido();
                    updateP.execute("");

                    PedidosPrintMobile();

                    //METODO PARA ENVIAR PARAMETROS PARA A OUTRA ACTIVITY
                    Intent intent = new Intent(EditarProdutoActivity.this, MenuActivity.class);
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

        private void DeletaPedido() {

            String Pedido = txtNroPedido.getText().toString();

            try {
                configuracao = adpConfiguracao.getItem(0);

                String ip = configuracao.getIp();
                String driver = configuracao.getDriver();
                String db = configuracao.getBancoDados();
                String usuario = configuracao.getUsuario();
                String senha = configuracao.getSenha();

                Connection con = connectionRemote.CONN(ip, driver, db, usuario, senha);

                if (con == null) {
                    Toast.makeText(EditarProdutoActivity.this, "Error in connection with SQL server", Toast.LENGTH_SHORT).show();
                } else {

                    String query = "Delete From Pedidos where Codigo ='" + Pedido + "'";
                    PreparedStatement preparedStatement = con.prepareStatement(query);
                    preparedStatement.executeUpdate();

                    con.close();
                }
            } catch (Exception ex) {

                Toast.makeText(EditarProdutoActivity.this, "erro " + ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
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

                    Toast.makeText(EditarProdutoActivity.this, "Pedido deletado !", Toast.LENGTH_SHORT).show();

                    //METODO PARA ENVIAR PARAMETROS PARA A OUTRA ACTIVITY
                    Intent intent = new Intent(EditarProdutoActivity.this, MenuActivity.class);
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

        public class UpdatePedido extends AsyncTask<String, String, String> {

            String z = "";
            String Pedido = txtNroPedido.getText().toString();
            Boolean isSuccess = false;

            @Override
            protected void onPreExecute() {

            }

            @Override
            protected void onPostExecute(String r) {
                //  progressBarPedidosEdit.setVisibility(View.GONE);
                Toast.makeText(EditarProdutoActivity.this, r, Toast.LENGTH_SHORT).show();
                if (isSuccess == true) {
                    //  FillList fillList = new FillList();
                    // fillList.execute("");
                }
            }

            @Override
            protected String doInBackground(String... params) {
                if (paramMesa.trim().equals(""))
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

                            //String query = "Update Pedidos set Mesa=" + mesa + " and Total='" + totalPedido + "' where Codigo=" + codigoPedido;
                            String query = "Update Pedidos " +
                                    "set Mesa ='" + paramMesa + "', " +
                                    "Observacao ='" + observacao + "', " +
                                    "Total ='" + valorTotalPedido + "' " +
                                    "where Codigo =" + Pedido;
                            PreparedStatement preparedStatement = con.prepareStatement(query);
                            preparedStatement.executeUpdate();
                            z = "Updated Successfully";

                            con.close();

                            isSuccess = true;
                        }
                    } catch (Exception ex) {
                        isSuccess = false;

                        z = "Exceptionsx " + ex.getMessage();
                    }
                }
                return z;
            }
        }

        public class FillList extends AsyncTask<String, String, String> {
            String z = "";

            String Pedido = txtNroPedido.getText().toString();

            List<Map<String, String>> prolist = new ArrayList<Map<String, String>>();

            @Override
            protected void onPreExecute() {

                //  progressBarPedidosEdit.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String r) {

                // Toast.makeText(EditarProdutoActivity.this, r, Toast.LENGTH_SHORT).show();

                String[] from = {"A", "B", "C", "D", "E"};
                int[] views = {
                        R.id.lblCodigoPro,
                        R.id.lblNomePro,
                        R.id.lblValorPro,
                        R.id.lblQtdPro,
                        R.id.lblTotalPro};

                final SimpleAdapter ADA = new SimpleAdapter(EditarProdutoActivity.this,
                        prolist, R.layout.activity_pedido_produto_listview, from,
                        views);
                listViewAddProduto.setAdapter(ADA);

                listViewAddProduto.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1,
                                            int arg2, long arg3) {
                        HashMap<String, Object> obj = (HashMap<String, Object>) ADA
                                .getItem(arg2);

                        String codProd = (String) obj.get("A");
                        String Quant = (String) obj.get("D");
                        String Valor = (String) obj.get("C");
                        txtNroProduto.setText(codProd);
                        txtQtdProduto.setText(Quant);
                        txtValorUnitProduto.setText(Valor);

                        spinnerCategoriaProd.setEnabled(false);
                        spinnerProdutosEdit.setEnabled(false);

                        //  lblAddProduto.setEnabled(true);
                        //  lblDeleteProduto.setEnabled(true);

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
                                        "PED.Observacao," +
                                        "format(PED.Quant, 'N0', 'pt-br') as Quant , " +
                                        "format(PED.ValorUnit, 'N', 'pt-br') as ValorUnit, " +
                                        "format(PED.Quant*PED.ValorUnit, 'N', 'pt-br') as Total " +
                                        "From PedidosItens PED " +
                                        "Inner join Produtos PRO on PRO.Codigo = PED.Produto " +
                                        "Where PED.Codigo = '" + Pedido + "'";

                        PreparedStatement ps = con.prepareStatement(query);
                        ResultSet rs = ps.executeQuery();

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

        public class AddProduto extends AsyncTask<String, String, String> {

            String z = "";
            Boolean isSuccess = false;

            //VALORES PARA O INSERT
            int codigoPedidosItens = Integer.parseInt(txtNroPedido.getText().toString());
            // String proname = txtCliente.getText().toString();
            int quantPedidosItens = Integer.parseInt(txtQtdProduto.getText().toString());
            //int quantPedidosItens = 1;
            int produto = Integer.parseInt(txtNroProduto.getText().toString());

            BigDecimal ValorFinal = new BigDecimal(txtValorTotal.getText().toString());

            String obsProduto = categoria.trim() + " " + tamanho.trim() + " " + descproduto + " " + produto1 + " " + produto2 + " " + produto3 + " " + produto4;

            @Override
            protected void onPreExecute() {
                //  progressBarPedidosEdit.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String r) {
                //   progressBarPedidosEdit.setVisibility(View.GONE);
                Toast.makeText(EditarProdutoActivity.this, r, Toast.LENGTH_SHORT).show();
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

                            MovimentaEstoque();

                            //VERIFICA SE ESSE PRODUTO ESTA MARCADO A OPCAO DE CONTROLE DE ESTOQUE , E SE ELE TEM A QUANTIDADE PARA O PEDIDO
                            if (movimentaEstoque > 0){
                                // the mysql insert statement
                                String query = "Insert Into PedidosItens (Codigo,Produto,Quant,ValorUnit,Observacao,Desconto,Sequencial)"
                                        + " values (?, ?, ?, ?, ?,?,?)";
                                PreparedStatement preparedStmt = con.prepareStatement(query);
                                preparedStmt.setInt(1, codigoPedidosItens);
                                preparedStmt.setInt(2, produto);
                                preparedStmt.setInt(3, quantPedidosItens);
                                preparedStmt.setBigDecimal(4, ValorFinal);
                                preparedStmt.setString(5, obsProduto);
                                preparedStmt.setInt(6, 0);
                                preparedStmt.setInt(7, produto);
                                // execute the preparedstatement
                                preparedStmt.execute();

                                //INSERT NA TABELA PEDIDOSPRINTMOBILE
                                Statement stmt = con.createStatement();

                                int codigoMax = 0;

                                ResultSet rst = stmt.executeQuery("Select Max(Numero + 1) as Codigo From PedidosPrintMobile");
                                while (rst.next()) {
                                    codigoMax = rst.getInt("Codigo");
                                }

                                String queryB = "Insert Into PedidosPrintMobile (Numero,Pedido,Status,Produto,Quant,Mesa,Atendente)"
                                        + " values (?, ?, ?,?,?,?,?)";
                                PreparedStatement preparedStmtB = con.prepareStatement(queryB);
                                preparedStmtB.setInt(1, codigoMax);
                                preparedStmtB.setInt(2, codigoPedidosItens);
                                preparedStmtB.setString(3, "E");//STATUS E = ESPERA ,A = AGUARDANDO, C = CONCLUIDO
                                preparedStmtB.setInt(4, produto);
                                preparedStmtB.setInt(5, quantPedidosItens);
                                preparedStmtB.setInt(6, 0);
                                preparedStmtB.setInt(7, 1);
                                // execute the preparedstatement
                                preparedStmtB.execute();
                                isSuccess = true;

                                z = "Salvo !";
                            }
                            else{
                                isSuccess = false;
                                z = "Produto Sem Estoque Disponível !";
                            }

                            con.close();
                        }
                    } catch (Exception ex) {
                        isSuccess = false;
                        z = "Exceptions " + ex.getMessage();
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

                // progressBarPedidosEdit.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String r) {
                // progressBarPedidosEdit.setVisibility(View.GONE);
                // Toast.makeText(PedidosEdit.this, r, Toast.LENGTH_SHORT).show();
                if (isSuccess == true) {

                    // Toast.makeText(EditarProdutoActivity.this, "Produto ja existe no Pedido !", Toast.LENGTH_SHORT).show();

                    UpdatePedidoProd updatePro = new UpdatePedidoProd();
                    updatePro.execute("");

                    // Toast.makeText(EditarProdutoActivity.this, "Success !", Toast.LENGTH_SHORT).show();
                } else {

                    AddProduto updatePro = new AddProduto();
                    updatePro.execute("");

                    // Toast.makeText(EditarProdutoActivity.this, "Success !", Toast.LENGTH_SHORT).show();
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

        public class PesquisaDelProduto extends AsyncTask<String, String, String> {

            String z = "";
            Boolean isSuccess = false;

            int pedido = Integer.parseInt(txtNroPedido.getText().toString());
            int produto = Integer.parseInt(txtNroProduto.getText().toString());

            @Override
            protected void onPreExecute() {

                // progressBarPedidosEdit.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String r) {
                //  progressBarPedidosEdit.setVisibility(View.GONE);
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

        public class UpdatePedidoProd extends AsyncTask<String, String, String> {

            String z = "";
            Boolean isSuccess = false;
            int quantPedPro = 0;
            int controle = 0;
            String proname = "TESTE 1";
            int pedido = Integer.parseInt(txtNroPedido.getText().toString());
            int produto = Integer.parseInt(txtNroProduto.getText().toString());
            int qtd = Integer.parseInt(txtQtdProduto.getText().toString());

            @Override
            protected void onPreExecute() {

                //progressBarPedidosEdit.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String r) {
                Toast.makeText(EditarProdutoActivity.this, z, Toast.LENGTH_LONG).show();
                if (isSuccess == true) {

                    FillList fillList = new FillList();
                    fillList.execute("");
                }
                else{
                   // Toast.makeText(EditarProdutoActivity.this, "Nome Selecionado: " + z, Toast.LENGTH_LONG).show();
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

                            MovimentaEstoque();
                            //VERIFICA SE ESSE PRODUTO ESTA MARCADO A OPCAO DE CONTROLE DE ESTOQUE , E SE ELE TEM A QUANTIDADE PARA O PEDIDO
                            if (movimentaEstoque > 0){
                                String query = "Update PedidosItens set Quant='" + qtd + "' where Codigo='" + pedido + "' and Produto =" + produto;
                                PreparedStatement preparedStatement = con.prepareStatement(query);
                                preparedStatement.executeUpdate();

                                String queryB = "Update PedidosPrintMobile set Quant='" + qtd + "', Status = 'E' where Pedido='" + pedido + "' and Produto =" + produto;
                                PreparedStatement preparedStatementB = con.prepareStatement(queryB);
                                preparedStatementB.executeUpdate();

                                isSuccess = true;

                                z = "Salvo !";
                            }
                            else{

                                z = "Produto sem Estoque !";
                                isSuccess = false;
                            }


                            con.close();

                        }
                    } catch (Exception ex) {
                        isSuccess = false;
                        z = "Exceptions" + ex.getMessage();
                    }
                }
                return z;
            }
        }

        //endregion

        public class UpdateProdAdicionar extends AsyncTask<String, String, String> {

            String z = "";
            Boolean isSuccess = false;

            String proname = "TESTE 1"; //txtCliente.getText().toString();
            // String mesa = txtNroMesa.getText().toString();
            int pedido = Integer.parseInt(txtNroPedido.getText().toString());
            int produto = Integer.parseInt(txtNroProduto.getText().toString());
            int qtd = Integer.parseInt(txtQtdPizza.getText().toString());

            int QuantProd = qtd + 1;

            @Override
            protected void onPreExecute() {

                //progressBarPedidosEdit.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String r) {
                // progressBarPedidosEdit.setVisibility(View.GONE);
                // Toast.makeText(PedidosEdit.this, r, Toast.LENGTH_SHORT).show();
                if (isSuccess == true) {

                    txtQtdPizza.setText("" + QuantProd);

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

                            String query = "Update PedidosItens set Quant='" + QuantProd + "' where Codigo='" + pedido + "' and Produto =" + produto;
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

            String proname = "TSTE 2";//txtCliente.getText().toString();
            //String mesa = txtNroMesa.getText().toString();
            int produto = Integer.parseInt(txtNroProduto.getText().toString());
            int qtd = Integer.parseInt(txtQtdPizza.getText().toString());

            int QuantProd = qtd - 1;

            @Override
            protected void onPreExecute() {

                //  progressBarPedidosEdit.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String r) {
                //progressBarPedidosEdit.setVisibility(View.GONE);
                // Toast.makeText(PedidosEdit.this, r, Toast.LENGTH_SHORT).show();
                if (isSuccess == true) {

                    txtQtdPizza.setText("" + QuantProd);

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

                // progressBarPedidosEdit.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String r) {
                //  progressBarPedidosEdit.setVisibility(View.GONE);
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

                // progressBarPedidosEdit.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String r) {
                //  progressBarPedidosEdit.setVisibility(View.GONE);
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
            int quantPedidosItens = Integer.parseInt(txtQtdProduto.getText().toString());


            @Override
            protected void onPreExecute() {
                //  progressBarPedidosEdit.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(String r) {
                //  progressBarPedidosEdit.setVisibility(View.GONE);
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

                            String queryB = "Delete From PedidosPrintMobile where Pedido='" + pedido + "' and Produto='" + produto + "'";
                            PreparedStatement preparedStatementB = con.prepareStatement(queryB);
                            preparedStatementB.executeUpdate();

                            //VERIFICA SE O PRODUTO CONTROLA ESTOQUE
                            Statement stmtA = con.createStatement();
                            int controle = 0;
                            ResultSet rstA = stmtA.executeQuery("Select ControleES From  Produtos Where Codigo =" + produto );
                            while (rstA.next()) {
                                controle = rstA.getInt("ControleES");
                            }

                            if (controle > 0){
                                //MOVIMENTA ESTOQUE
                                String queryC = "Update Estoque set Quant = Quant + '" + quantPedidosItens + "' where Produto =" + produto;
                                PreparedStatement preparedStatementC = con.prepareStatement(queryC);
                                preparedStatementC.executeUpdate();
                            }

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
    }
