package br.com.gtsti.estudo;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import br.com.gtsti.estudo.Util.Mask;
import br.com.gtsti.estudo.Util.funcoes;
//import br.com.jansenfelipe.androidmask.MaskEditTextChangedListener;

/**
 * Created by Glauber on 24/01/2016.
 */
public class CadPedidos extends Activity {

    private DbFactory dbFactory;
    private Spinner sp_cliente;
    private Spinner sp_produto;
    private EditText qtd,valor,obs;
    private Button datacobranca;
    private funcoes funcao;
    private LinearLayout lPedido;
    private AutoCompleteTextView ac_endereco;
    private Mask mask_pedido_valor;
    private int dia,ano,mes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cad_pedido);

        //Instancia do banco de dados
        dbFactory = new DbFactory(this);
        funcao = new funcoes();


        //Recuperando os instancia dos objetos
        qtd =(EditText)findViewById(R.id.ped_qtd);
        valor = (EditText)findViewById(R.id.ped_valor);
        datacobranca = (Button)findViewById(R.id.ped_datacobranca);
        obs = (EditText)findViewById(R.id.ped_obs);
        lPedido = (LinearLayout)findViewById(R.id.lPedido);
        sp_cliente = (Spinner)findViewById(R.id.sp_cliente);
        sp_produto = (Spinner)findViewById(R.id.sp_produto);
        ac_endereco = (AutoCompleteTextView)findViewById(R.id.ac_cliente);
    //    mask_pedido_valor = new MaskEditTextChangedListener("###.##",valor);
        valor.addTextChangedListener(Mask.insert("###.###,###",valor));

        //Seletor de Data
        Calendar calendar = Calendar.getInstance();
        ano = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH);
        dia = calendar.get(Calendar.DAY_OF_MONTH);

        datacobranca.setText(dia + "/" + (mes + 1) + "/" + ano);


        //Popula combo de clientes
        ArrayAdapter<String>adapterCliente = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,getAllDataTable("CLIENTES",1));
        adapterCliente.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_cliente.setAdapter(adapterCliente);

        //Popula combo de produtos
        ArrayAdapter<String>adapterProduto = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,getAllDataTable("PRODUTOS",1));
        adapterProduto.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_produto.setAdapter(adapterProduto);

        //AutoComplete para pesquisa por endereço de clientes
        ArrayAdapter<String>auto_endereco = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line,getAllDataTable("CLIENTES",2));
        auto_endereco.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        ac_endereco.setAdapter(auto_endereco);


        ac_endereco.setTextColor(Color.RED);

        //Pega o item pesquisado e seta no Spinner Cliente(sp_cliente)
        ac_endereco.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                sp_cliente.setSelection(position);
                //Toast.makeText(getApplicationContext(), "Posição: " + (String.valueOf(position)), Toast.LENGTH_LONG).show();

                //Popula combo de clientes

                ArrayAdapter<String> adapterCliente2 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, FindClienteAndress(ac_endereco.getText().toString()));
                //adapterCliente2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                adapterCliente2.setDropDownViewResource(R.layout.spinner_layout_theme);
                sp_cliente.setAdapter(adapterCliente2);
                sp_cliente.setBackgroundColor(Color.LTGRAY);

            }
        });
        sp_produto.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {

                int tam_lista = getAllDataTable("PRODUTOS", 2).size();
                double[] valores = new double[tam_lista];
                long qtd_prod = Integer.parseInt(qtd.getText().toString());
                for(int i = 0; i < tam_lista; i++)
                    valores[i] = Float.parseFloat(getAllDataTable("PRODUTOS", 2).get(i));

                double val = qtd_prod*valores[position];

                valor.setText(String.valueOf(val));
                //valor.setText("Jaca", TextView.BufferType.EDITABLE);
                //valor.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }
    /**
     * Pega todos os clientes
     * retorna lista de clientes
     * */
    public List<String> getAllDataTable(String tabela,int indCampo){
        List<String> clientes = new ArrayList<String>();

        // Select todos os dados
        String selectQuery = "SELECT  * FROM "+tabela;

        SQLiteDatabase db = dbFactory.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Pega todos os dados da tabela de uma determinada coluna "indCampo"
        if (cursor.moveToFirst()) {
            do {
                clientes.add(cursor.getString(indCampo));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return clientes;
    }
    public void AddPedido(View view)
    {
        try{
        SQLiteDatabase db = dbFactory.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("qtd",qtd.getText().toString());
        values.put("valor",valor.getText().toString());
        values.put("datapedido",datacobranca.getText().toString().replace("//",""));
        values.put("obs",obs.getText().toString());
        values.put("idcliente",sp_cliente.getSelectedItemId());
        values.put("idprod",sp_produto.getSelectedItemId());

        long retorno = db.insert("PEDIDOS",null,values);

        if (retorno != -1) {
            Toast.makeText(this, "Pedido efetuado com sucesso!", Toast.LENGTH_SHORT).show();
            funcao.ClearCampos(lPedido);
        } else {
            Toast.makeText(this, "Ocorreu um erro ao inserir as informações no banco de dados!", Toast.LENGTH_LONG).show();
        }
    } catch (Exception ex) {
        Toast.makeText(this, "Erro desconhecido " + ex.getStackTrace(), Toast.LENGTH_LONG).show();
    }
    }
    public List<String>FindClienteAndress(String adress)
    {
        List<String> clientes = new ArrayList<String>();

        // Select todos os dados
        String selectQuery = "SELECT  * FROM CLIENTES WHERE ENDERECO LIKE '%"+adress+"%'";

        SQLiteDatabase db = dbFactory.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Pega todos os dados da tabela de uma determinada coluna "indCampo"
        if (cursor.moveToFirst()) {
            do {
                clientes.add(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return clientes;
    }
    //Criação de Menu para activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_pedidos, menu);
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
    @Override
    protected void onDestroy()
    {
        dbFactory.close();
        super.onDestroy();
    }
    public void selecionarData(View v)
    {
        showDialog(v.getId());
    }
    @Override
    protected Dialog onCreateDialog(int id)
    {
        if(R.id.ped_datacobranca ==id)
        {
            return new DatePickerDialog(this,listener,ano,mes,dia);
        }
        return null;
    }
    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            ano =year;
            mes = monthOfYear;
            dia = dayOfMonth;
            datacobranca.setText(dia+ "/"+(mes+1)+"/"+ano);
        }
    };

}

