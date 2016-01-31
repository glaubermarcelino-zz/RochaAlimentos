package br.com.gtsti.estudo;

import android.app.Activity;
import android.content.ContentValues;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import br.com.gtsti.estudo.Util.Mask;
import br.com.gtsti.estudo.Util.funcoes;

/**
 * Created by Glauber on 24/01/2016.
 */
public class CadClientes extends Activity {
    private DbFactory dbFactory;
    private EditText cliente,endereco,telefone,bairro,obs,referencia;
    private Spinner spcidade,spuf;
    private long getCidade,getUF;
    private LinearLayout Layout;
    private funcoes funcao;
    private Resources rs;
    private String[] cidades;
    private String[]ufs;
    private Mask mask_fone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cad_clientes);

        //Instancia do Banco de dados
        dbFactory = new DbFactory(this);

        rs =getResources();
        //Recuperando os valores do array do Resources
        cidades = rs.getStringArray(R.array.CIDADES);
        ufs = rs.getStringArray(R.array.UF);
        funcao = new funcoes();

        //Recupera os objetos do XML
        spcidade = (Spinner)findViewById(R.id.cli_cidade);
        spuf = (Spinner)findViewById(R.id.cli_uf);
        cliente =(EditText)findViewById(R.id.nome_cliente);
        endereco =(EditText)findViewById(R.id.cli_endereco);
        telefone =(EditText)findViewById(R.id.cli_telefone);
        bairro =(EditText)findViewById(R.id.cli_bairro);
        obs =(EditText)findViewById(R.id.cli_obs);
        referencia =(EditText)findViewById(R.id.cli_ref);

        //Aplica a mascara para telefone
        telefone.addTextChangedListener(mask_fone.insert("(##)0####-####",telefone));

        //Recuperação do layout principal
        Layout = (LinearLayout)findViewById(R.id.cliLayout);

        //Adapter para o Spinner
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,cidades);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spcidade.setAdapter(adapter);

        ArrayAdapter<String>adapterUF = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,ufs);
        adapterUF.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spuf.setAdapter(adapterUF);

        spcidade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getCidade = spcidade.getSelectedItemId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spuf.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getUF = spuf.getSelectedItemId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    public void NovoCliente(View view)
    {
        try {
            SQLiteDatabase db = dbFactory.getWritableDatabase();

            //Cria um ContentValues para armazenar os valores mediante chave->valor
            ContentValues contentValues = new ContentValues();
            contentValues.put("NOME", cliente.getText().toString());
            contentValues.put("ENDERECO", endereco.getText().toString());
            contentValues.put("TELEFONE", telefone.getText().toString());
            contentValues.put("BAIRRO", bairro.getText().toString());
            contentValues.put("CIDADE", getCidade);
            contentValues.put("UF", getUF);
            contentValues.put("OBS", obs.getText().toString());
            contentValues.put("REFERENCIA", referencia.getText().toString());

            //Inserindo diretamente no banco de dados sem verificação do conteudo

            //Parametro 1: Tabela,Parametro 2:CursorFactory(para valores nulos),Parametro 3:ContentValues
            long retorno = db.insert("CLIENTES", null, contentValues);

            if (retorno != -1) {
                Toast.makeText(this, "Cadastro efetuado com sucesso!", Toast.LENGTH_SHORT).show();
                //LimparCampos(Layout);
                funcao.ClearCampos(Layout);
            } else {
                Toast.makeText(this, "Ocorreu um erro ao inserir as informações no banco de dados!", Toast.LENGTH_LONG).show();
            }
        }catch(Exception e)
        {
            Toast.makeText(this,"Erro ao inserir cliente "+e.getMessage()+", verifique o preenchimento dos campos",Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onDestroy()
    {
        dbFactory.close();
        super.onDestroy();
    }
    //Criação de Menu para activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_clientes, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
