package com.dieese.agenda;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ContatoEditar extends Activity{
	
	int menu;
	int position;
	long id;
	Context context = this;
	DBAdapter dbAdapter;
	Button btnEditar;
	TextView txtTitle;
	EditText edtNome, 
			edtNascimento, 
			edtEmpresa,
			edtTelefoneCelular, 
			edtTelefoneResidencial, 
			edtTelefoneComercial;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editar);
		
		dbAdapter = new DBAdapter(context);
		
		txtTitle = (TextView) findViewById(R.id.txtTitleEdition);
		edtNome = (EditText) findViewById(R.id.edtNome);
		edtNascimento = (EditText) findViewById(R.id.edtNascimento);
		edtEmpresa = (EditText) findViewById(R.id.edtEmpresa);
		edtTelefoneCelular = (EditText) findViewById(R.id.edtTelefoneCelular);
		edtTelefoneResidencial = (EditText) findViewById(R.id.edtTelefoneResidencial);
		edtTelefoneComercial = (EditText) findViewById(R.id.edtTelefoneComercial);
		
		// Baseado no tipo de edição altera a view
		
		menu = getIntent().getIntExtra("menu", ContatoMenu.CADASTRAR);
		id = getIntent().getLongExtra("id", 1);
		position = getIntent().getIntExtra("position",0);
		
		switch(menu){
			case ContatoMenu.CADASTRAR:
				txtTitle.setText("Cadastrar");
				break;
			case ContatoMenu.ALTERAR:
				txtTitle.setText("Alterar");
				carregaContato(id);
				break;
		}
		
		// Seta o listener para preencher nascimento
		edtNascimento.addTextChangedListener(inputTextWatcher);
		edtNascimento.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {

				int length = edtNascimento.getText().length();
				boolean typing = true;
				
				if(keyCode == KeyEvent.KEYCODE_DEL)
					typing = false;
				
				if(typing == true){
					if(length == 2 || length == 5){
						String newText = edtNascimento.getText().toString() + "/";
						edtNascimento.setText(newText);
						edtNascimento.setSelection(edtNascimento.getText().length());
					}
				}
				return false;
			}
		});
		
		// Seta o listener para salvar o contato
		btnEditar = (Button) findViewById(R.id.btnEditar);
		btnEditar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				salvaContato(id);
			}
		});
	}
	
	
	private void carregaContato(long id){
		
		// Carrega informações do contato do banco de dados para a caixa de texto
		try{
			
			Contato contato = null;
			dbAdapter.open();
			contato = dbAdapter.visualizar(id);
			dbAdapter.close();
			
			if(contato != null){
				edtNome.setText(contato.getNome());
				edtNascimento.setText(contato.getNascimento());
				edtEmpresa.setText(contato.getEmpresa());
				edtTelefoneCelular.setText(contato.getTelefoneCelular());
				edtTelefoneResidencial.setText(contato.getTelefoneResidencial());
				edtTelefoneComercial.setText(contato.getTelefoneComercial());
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}

	}
	
	private void salvaContato(final long id){

		// Atribui ao Contato os dados digitados caso não estejam vazios
		
		if(!isAnyoneEmpty()){
						
			// Cadastra o contato no banco de dados SQlite se confirmar
			
			AlertDialog.Builder dialog = new AlertDialog.Builder(context);
			dialog.setTitle("Confirmar");
			dialog.setMessage("Deseja salvar esse contato ?");
			dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					try{
						dbAdapter.open();
						if(menu == ContatoMenu.CADASTRAR){
							
							Contato contato = new Contato();
							contato.setNome(edtNome.getText().toString());
							contato.setNascimento(edtNascimento.getText().toString());
							contato.setEmpresa(edtEmpresa.getText().toString());
							contato.setTelefoneCelular(edtTelefoneCelular.getText().toString());
							contato.setTelefoneResidencial(edtTelefoneResidencial.getText().toString());
							contato.setTelefoneComercial(edtTelefoneComercial.getText().toString());
						
							if(dbAdapter.cadastrar(contato))
								ContatoUtil.showMessage(context,"Criado com sucesso");
							
						}else if(menu == ContatoMenu.ALTERAR){
							
							Contato contato = dbAdapter.visualizar(id);
							contato.setNome(edtNome.getText().toString());
							contato.setNascimento(edtNascimento.getText().toString());
							contato.setEmpresa(edtEmpresa.getText().toString());
							contato.setTelefoneCelular(edtTelefoneCelular.getText().toString());
							contato.setTelefoneResidencial(edtTelefoneResidencial.getText().toString());
							contato.setTelefoneComercial(edtTelefoneComercial.getText().toString());
							
							if(dbAdapter.alterar(id, contato))
								ContatoUtil.showMessage(context,"Alterado com sucesso");
						}

						dbAdapter.close();
						
					}catch(Exception e){
						e.printStackTrace();
					}finally{
						if(menu == ContatoMenu.CADASTRAR){
							finish();
						}else if(menu == ContatoMenu.ALTERAR){
							Intent data = new Intent();
							data.putExtra("position", position);
							setResult(RESULT_OK, data);
							finish();
						}
					}
				}
			});
			dialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					dialog.cancel();
				}
			});
			dialog.show();

		}else{
			ContatoUtil.showMessage(context,"Não deixe nenhum campo vazio");
		}
		
	}
	
	private boolean isAnyoneEmpty(){
		
		EditText[] edtAll = {
				edtNome, 
				edtNascimento, 
				edtTelefoneCelular, 
				edtTelefoneResidencial, 
				edtTelefoneComercial}; 
		
		for(EditText e : edtAll){
			if(e.getText().toString().trim().length() == 0)
				return true;
		}
		return false;
	}

	private TextWatcher inputTextWatcher = new TextWatcher() {
		 public void afterTextChanged(Editable s) { }
		 public void beforeTextChanged(CharSequence s, int start, int count, int after){ }
		 public void onTextChanged(CharSequence s, int start, int before, int count) {}
		};
}
