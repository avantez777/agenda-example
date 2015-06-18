package com.dieese.agenda;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapter {

	static final String KEY_ID = "id";
	static final String KEY_NOME = "nome";
	static final String KEY_NASCIMENTO = "nascimento";
	static final String KEY_EMPRESA = "empresa";
	static final String KEY_TELEFONE_CELULAR = "telefone_celular";
	static final String KEY_TELEFONE_RESIDENCIAL = "telefone_residencial";
	static final String KEY_TELEFONE_COMERCIAL = "telefone_comercial";
	static final String TAG = "DBAdapter";
	
	static final String[] columns = {
		KEY_ID,
		KEY_NOME,
		KEY_NASCIMENTO,
		KEY_EMPRESA,
		KEY_TELEFONE_CELULAR,
		KEY_TELEFONE_RESIDENCIAL,
		KEY_TELEFONE_COMERCIAL};
	
	static final String DATABASE_NAME = "agenda";
	static final String DATABASE_TABLE = "contatos";
	static final int DATABASE_VERSION = 1;
	static final String DATABASE_CREATE = "CREATE TABLE " + DATABASE_TABLE + " (" +
			KEY_ID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
			KEY_NOME + " TEXT NOT NULL," +
			KEY_NASCIMENTO + " TEXT NOT NULL," +
			KEY_EMPRESA + " TEXT NOT NULL," +
			KEY_TELEFONE_CELULAR + " TEXT NOT NULL," +
			KEY_TELEFONE_RESIDENCIAL + " TEXT NOT NULL," +
			KEY_TELEFONE_COMERCIAL + " TEXT NOT NULL);";
	
	Context context;
	DatabaseHelper dbHelper;
	SQLiteDatabase db;
	
	
	// Helper do banco de dados
	private class DatabaseHelper extends SQLiteOpenHelper{
		
		DatabaseHelper(Context c){
			super(c, DATABASE_NAME, null, DATABASE_VERSION);
		}
		
		@Override 
		public void onCreate(SQLiteDatabase db){
			try{
				db.execSQL(DATABASE_CREATE);
				System.out.println("database create");
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			System.out.println("database update");
			onCreate(db);
		}
		
	}

	
	// Constructor
	DBAdapter(Context c){
		context = c;
		dbHelper = new DatabaseHelper(context);
	}

	
	// Métodos Open & Close
	public DBAdapter open() throws SQLException{
		db = dbHelper.getWritableDatabase();
		return this;
	}
	
	public void close(){
		db.close();
	}
	
	public void reset(){
		db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
		db.execSQL(DATABASE_CREATE);
	}


	
	// Métodos auxiliares
	public ContentValues convertToValues(Contato contato){
		
		ContentValues cv = new ContentValues();
		cv.put(KEY_NOME, contato.getNome());
		cv.put(KEY_NASCIMENTO, contato.getNascimento());
		cv.put(KEY_EMPRESA, contato.getEmpresa());
		cv.put(KEY_TELEFONE_CELULAR, contato.getTelefoneCelular());
		cv.put(KEY_TELEFONE_RESIDENCIAL, contato.getTelefoneResidencial());
		cv.put(KEY_TELEFONE_COMERCIAL, contato.getTelefoneComercial());
		
		return cv;
	}
	
	public Contato convertToObject(Cursor cursor){
		Contato contato = new Contato();
		
		int indexId = cursor.getColumnIndex(KEY_ID);
		int indexNome = cursor.getColumnIndex(KEY_NOME);
		int indexNascimento = cursor.getColumnIndex(KEY_NASCIMENTO);
		int indexEmpresa = cursor.getColumnIndex(KEY_EMPRESA);
		int indexTelefoneCelular = cursor.getColumnIndex(KEY_TELEFONE_CELULAR);
		int indexTelefoneResidencial = cursor.getColumnIndex(KEY_TELEFONE_RESIDENCIAL);
		int indexTelefoneComercial = cursor.getColumnIndex(KEY_TELEFONE_COMERCIAL);
			
		contato.setId(cursor.getInt(indexId));
		contato.setNome(cursor.getString(indexNome));
		contato.setNascimento(cursor.getString(indexNascimento));
		contato.setEmpresa(cursor.getString(indexEmpresa));
		contato.setTelefoneCelular(cursor.getString(indexTelefoneCelular));
		contato.setTelefoneResidencial(cursor.getString(indexTelefoneResidencial));
		contato.setTelefoneComercial(cursor.getString(indexTelefoneComercial));
		
		return contato;
	}
	
	public int tamanho(){
		
		int t = 0;
		Cursor cursor = db.query(DATABASE_TABLE, columns, null, null, null, null, null);
		if(cursor != null){
			t = cursor.getCount();
		}
		return t;
	}
	
	
	// Métodos CRUD
	public boolean cadastrar(Contato contato){

		ContentValues cv = convertToValues(contato);
		return db.insert(DATABASE_TABLE, null, cv) > 0;
	}
	
	public boolean alterar(long id, Contato contato){
		
		ContentValues cv = convertToValues(contato);		
		return db.update(DATABASE_TABLE, cv, KEY_ID + "=" + id, null) > 0;
	}
	
	public boolean excluir(long id){
		return db.delete(DATABASE_TABLE, KEY_ID + "=" + id, null) > 0;
	}
	
	public Contato visualizar(long id){
		
		Contato contato = new Contato();
		Cursor cursor = db.query(DATABASE_TABLE, columns, KEY_ID + "=" + id, null, null, null, null);
		
		if(cursor != null){
			cursor.moveToFirst();
			contato = convertToObject(cursor);
		}
		
		return contato;
	}
	
	public List<Contato> listar(){
		
		List<Contato> contatos = new ArrayList<Contato>();
		Cursor cursor = db.query(DATABASE_TABLE, columns, null, null, null, null, null);
		
		if(cursor != null){
			if(cursor.getCount()>0){
				for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
					Contato contato = convertToObject(cursor);
					contatos.add(contato);
				}
			}
		}
		
		return contatos;
	}


}
