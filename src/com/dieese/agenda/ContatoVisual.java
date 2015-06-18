package com.dieese.agenda;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

public class ContatoVisual extends Activity{
	
	long id;
	Context context = this;
	DBAdapter dbAdapter;
	TextView txtNome, txtNascimento, txtEmpresa, txtTelCelular, txtTelResidencial, txtTelComercial;
	
	@Override
	protected void onCreate(Bundle savedinstanceState){
		super.onCreate(savedinstanceState);
		setContentView(R.layout.visualizar);
		
		
		txtNome = (TextView) findViewById(R.id.txtNome);
		txtNascimento = (TextView) findViewById(R.id.txtNascimento);
		txtEmpresa = (TextView) findViewById(R.id.txtEmpresa);
		txtTelCelular = (TextView) findViewById(R.id.txtTelCelular);
		txtTelResidencial = (TextView) findViewById(R.id.txtTelResidencial);
		txtTelComercial = (TextView) findViewById(R.id.txtTelComercial);
		
		// Cria um contato vazio
		Contato contato = null;
		
		// Pega o id do contato passado pela lista e carrega do banco de dados
		try{
			
			id = getIntent().getLongExtra("id", 1);
			dbAdapter = new DBAdapter(context);
			dbAdapter.open();
			contato = dbAdapter.visualizar(id);
			dbAdapter.close();
			
			// Atribue contato aos TextViews
			if(contato != null){
				txtNome.setText(contato.getNome());
				txtNascimento.setText(contato.getNascimento());
				txtEmpresa.setText(contato.getEmpresa());
				txtTelCelular.setText(contato.getTelefoneCelular());
				txtTelResidencial.setText(contato.getTelefoneResidencial());
				txtTelComercial.setText(contato.getTelefoneComercial());
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		

			
		
	}


}
