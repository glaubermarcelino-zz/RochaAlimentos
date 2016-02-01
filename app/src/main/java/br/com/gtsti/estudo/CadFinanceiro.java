package br.com.gtsti.estudo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import br.com.gtsti.estudo.Util.funcoes;

/**
 * Created by Glauber on 24/01/2016.
 */
public class CadFinanceiro extends Activity {
    private ListView lvVendasDia;
    private funcoes funcao;
    private DbFactory dbFactory;
    private LinearLayout lFinanceiro;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mov_financeiro);
        dbFactory = new DbFactory(this);
        funcao = new funcoes();

        lvVendasDia = (ListView)findViewById(R.id.lv_vendas_dia);
        lFinanceiro = (LinearLayout)findViewById(R.id.lMonFinanceiro);

        ArrayAdapter<String>adapter_vendas_dia = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,funcao.getVendasDia(dbFactory));
        adapter_vendas_dia.setDropDownViewResource(android.R.layout.simple_list_item_1);
        lvVendasDia.setAdapter(adapter_vendas_dia);

        lvVendasDia.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(),"Item Selecionado.: "+lvVendasDia.getSelectedItem(),Toast.LENGTH_LONG).show();
            }
        });
    }
    //Criação de Menu para activity
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_financeiro, menu);
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
