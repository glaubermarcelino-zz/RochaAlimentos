package br.com.gtsti.estudo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Glauber on 26/01/2016.
 */
public class DbFactory extends SQLiteOpenHelper {
    private static int BD_VERSAO = 1;
    private static final String BD_NAME = "RochaAlimentos";

    public DbFactory(Context context)
    {
        super(context,BD_NAME,null,BD_VERSAO);
    }
    @Override
    public void onCreate(SQLiteDatabase db)
    {

        //UF
        db.execSQL("CREATE TABLE UF(_id integer PRIMARY KEY AUTOINCREMENT,NOME VARCHAR(100)NOT NULL,SIGLA CHAR(2)NOT NULL); ");

        //Cidades
        db.execSQL("CREATE TABLE CIDADES(_id INTEGER PRIMARY KEY AUTOINCREMENT,NOME VARCHAR(100) NOT NULL,COD_UD INTEGER REFERENCES UF (_id),FOREIGN KEY (COD_UD)REFERENCES UF(_id));");

        //Clientes
        db.execSQL("CREATE TABLE CLIENTES (_id INTEGER PRIMARY KEY AUTOINCREMENT,NOME VARCHAR(100) NOT NULL,ENDERECO VARCHAR(200) NOT NULL,CIDADE INT NOT NULL REFERENCES CIDADES (_id),UF INT NOT NULL REFERENCES UF(_id),TELEFONE VARCHAR(50),BAIRRO VARCHAR(50),OBS TEXT,REFERENCIA VARCHAR(150));");

        //Unidade de medida
        db.execSQL("CREATE TABLE MEDIDA(_id integer PRIMARY KEY AUTOINCREMENT,DESCRICAO VARCHAR(50)NOT NULL,SIGLA CHAR(4)NOT NULL); ");

        //Produtos
        db.execSQL("CREATE TABLE PRODUTOS(_id INTEGER PRIMARY KEY AUTOINCREMENT,NOME VARCHAR(100) NOT NULL,VALOR DECIMAL(10,2),QTD INTEGER DEFAULT 1,NCM CHAR(20) DEFAULT '99999999',DATACOMPRA DATETIME DEFAULT CURRENT_TIMESTAMP,CODBARRA VARCHAR(50)DEFAULT '000000000000000000',ULTCOMPRA DATETIME DEFAULT CURRENT_TIMESTAMP,COD_UD INTEGER REFERENCES MEDIDA (_id),FOREIGN KEY (COD_UD)REFERENCES MEDIDA(_ID));");

        //PEDIDOS
        db.execSQL("CREATE TABLE PEDIDOS (_id INTEGER PRIMARY KEY AUTOINCREMENT,idcliente INTEGER  REFERENCES CLIENTES (_id),valor DECIMAL(18, 2),qtd INTEGER,datapedido DATETIME DEFAULT CURRENT_TIMESTAMP,datacobranca DATETIME,idprod INTEGER REFERENCES PRODUTOS(_id),obs Text DEFAULT '');");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
