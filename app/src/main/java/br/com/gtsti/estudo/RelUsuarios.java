package br.com.gtsti.estudo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import br.com.gtsti.estudo.Util.funcoes;

/**
 * Created by Glauber on 31/01/2016.
 */
public class RelUsuarios extends Activity {
    private ListView lvRelUsuarios;
    private funcoes funcao;
    private LinearLayout lRelusuarios;
    private DbFactory dbFactory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rel_usuarios);

        funcao = new funcoes();
        lRelusuarios = (LinearLayout)findViewById(R.id.lRelUsuarios);
        dbFactory = new DbFactory(this);
        lvRelUsuarios = (ListView)findViewById(R.id.lvRelUsuarios);


        ArrayAdapter<String>adapter_rel_usuarios = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,funcao.getAllDataTable("USUARIOS",1,dbFactory));
        adapter_rel_usuarios.setDropDownViewResource(android.R.layout.simple_list_item_1);
        lvRelUsuarios.setAdapter(adapter_rel_usuarios);

        lvRelUsuarios.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "Item Selecionado.: " + lvRelUsuarios.getItemAtPosition(position), Toast.LENGTH_LONG).show();
            }
        });



    }
}
