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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import br.com.gtsti.estudo.Util.Mask;
import br.com.gtsti.estudo.Util.funcoes;
//import br.com.jansenfelipe.androidmask.MaskEditTextChangedListener;

/**
 * Created by Glauber on 24/01/2016.
 */
public class CadProdutos extends Activity {
    private DbFactory dbFactory;
    private EditText nome,qtd,valor,ncm,ultcompra,codbarra;
    private Spinner sp_medida;
    private Button btnAddProduto;
    private Resources rs;
    private long idMedida;
    private LinearLayout lproduto;
    private funcoes funcao;
    private Mask mask_val_prod;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cad_produto);

        //Instancia para o banco de dados
        dbFactory = new DbFactory(this);
        rs = getResources();


        funcao =new funcoes();
        //Recuperando os valores do objetos
        lproduto = (LinearLayout)findViewById(R.id.lproduto);

        nome = (EditText)findViewById(R.id.nome_produto);
        qtd =(EditText)findViewById(R.id.quantidade);
        valor = (EditText)findViewById(R.id.valor_produto);
        ncm = (EditText)findViewById(R.id.ncm_produto);
        ultcompra = (EditText)findViewById(R.id.uCompra_produto);
        codbarra = (EditText)findViewById(R.id.cod_barra);

        valor.addTextChangedListener(Mask.insert("###.###,###", valor));
      //  valor.addTextChangedListener(mask_val_prod);

        sp_medida = (Spinner)findViewById(R.id.sp_medida);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,rs.getStringArray(R.array.MEDIDAS));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_medida.setAdapter(adapter);

        sp_medida.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //Pego o id da medida que o usuario selecionou
                idMedida = sp_medida.getSelectedItemId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    public void AddProduto(View view) {
        try {
            //Recupera instancia do banco para inserir dados
            SQLiteDatabase db = dbFactory.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("NOME", nome.getText().toString());
            values.put("QTD", qtd.getText().toString());
            values.put("VALOR", valor.getText().toString());

          /*  if(ncm.getText().toString()!= "" && ultcompra.getText().toString()!="" && codbarra.getText().toString() !="") {
                values.put("NCM", ncm.getText().toString());
                values.put("ULTCOMPRA", ultcompra.getText().toString());
                values.put("CODBARRA", codbarra.getText().toString());
            }
*/
            values.put("COD_UD", idMedida);

            long retorno = db.insert("PRODUTOS", null, values);

            if (retorno != -1) {
                Toast.makeText(this, "Produto inserido com sucesso!", Toast.LENGTH_SHORT).show();
                funcao.ClearCampos(lproduto);
            } else {
                Toast.makeText(this, "Ocorreu um erro ao inserir as informações no banco de dados!", Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex) {
            Toast.makeText(this, "Ocorreu um erro desconhecido " + ex.getMessage(), Toast.LENGTH_LONG).show();
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
        getMenuInflater().inflate(R.menu.menu_produtos, menu);
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
