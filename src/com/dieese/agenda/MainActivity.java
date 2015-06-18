package com.dieese.agenda;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
	
	Context context = this;
	Button btnCadastrar, btnVisualizar, btnAlterar, btnExcluir;
	DBAdapter dbAdapter;
	List<Contato> contatos;	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		btnCadastrar = (Button) findViewById(R.id.btnCadastrar);
		btnVisualizar = (Button) findViewById(R.id.btnVisualizar);
		btnAlterar = (Button) findViewById(R.id.btnAlterar);
		btnExcluir = (Button) findViewById(R.id.btnExcluir);
		
		btnCadastrar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				Intent cadastrar = new Intent(context, ContatoEditar.class);
				startActivity(cadastrar);
			}
		});
		btnVisualizar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				carregaLista(ContatoMenu.VISUALIZAR);
			}
		});
		btnAlterar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				carregaLista(ContatoMenu.ALTERAR);
			}
		});
		btnExcluir.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				carregaLista(ContatoMenu.EXCLUIR);
			}
		});

	}
	
	
	
	// Carrrega a lista de contatos recebendo o tipo de ação de menu como parâmetro
	
	public void carregaLista(int menu){
		
		// Carrega os contatos do banco de dados
		
		try{
			dbAdapter = new DBAdapter(context);
			dbAdapter.open();
			contatos = dbAdapter.listar();
			
			if(contatos != null && contatos.size() > 0){
				
				// Itera sobre os contatos e os adiciona
				
				long[] listaIDs = new long[contatos.size()];
				ArrayList<String> listaNomes = new ArrayList<String>();
 				
				for(int i=0;i<contatos.size();i++){
					int id = contatos.get(i).getId();
					String nome = contatos.get(i).getNome();
					listaIDs[i] = id;
					listaNomes.add(nome);
				}
	
				// Passa a lista de contatos para uma outra Activity
				
				Intent listar = new Intent(context, ContatoListar.class);
				listar.putExtra("menu", menu);
				listar.putExtra("listaIDs", listaIDs);
				listar.putExtra("listaNomes", listaNomes);
				startActivity(listar);
			
			}else{
				ContatoUtil.showMessage(context,"Não existe nenhum contato ainda");
			}
			dbAdapter.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}
