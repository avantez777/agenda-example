package com.dieese.agenda;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class ContatoListar extends Activity{
	
	int menu;
	int requestCode = 1;
	
	long[] listaIDs;
	ArrayList<String> listaNomes = new ArrayList<String>();
	ListView listView;
	ArrayAdapter<String> listAdapter;
	DBAdapter dbAdapter;

	Context context = this;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.listar);
		
		// Recebe o tipo de ação de menu e a listas (adicionada lista para ids diferentes da ordem);
		
		menu = getIntent().getIntExtra("menu",ContatoMenu.VISUALIZAR);
		listaIDs = getIntent().getLongArrayExtra("listaIDs");
		listaNomes = getIntent().getStringArrayListExtra("listaNomes");
		
		listAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, listaNomes);
		listView = (ListView) findViewById(R.id.list);
		listView.setAdapter(listAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
				long index = listaIDs[(int) id];
				executaContato(index, position);
				listAdapter.notifyDataSetChanged();
			}
		});
		
	}
	
	public void executaContato(long id, int position){

		// Baseado no tipo de ação de menu realiza a operação
		switch(menu){
			case ContatoMenu.VISUALIZAR:
				visualizarContato(id);
				break;
			case ContatoMenu.ALTERAR:
				alterarContato(id, position);
				break;
			case ContatoMenu.EXCLUIR:
				excluirContato(id, position);
				break;
		}
		
	}
	
	public void alterarContato(long id, int position){
		
		Intent alterar = new Intent(context, ContatoEditar.class);
		alterar.putExtra("menu", ContatoMenu.ALTERAR);
		alterar.putExtra("id", id);
		alterar.putExtra("position", position);
		startActivityForResult(alterar, requestCode);
		
	}
	
	public void visualizarContato(long id){
		
		Intent visualizar = new Intent(context, ContatoVisual.class);
		visualizar.putExtra("id", id);
		startActivity(visualizar);
		
	}
	
	public void excluirContato(final long id, final int position){
		
		AlertDialog.Builder dialog = new AlertDialog.Builder(context);
		dialog.setTitle("Excluir");
		dialog.setMessage("Tem certeza que deseja excluir ?");
		dialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				// Abre o banco e executa a operação de exclusão
				try{
					dbAdapter = new DBAdapter(context);
					dbAdapter.open();
					if(dbAdapter.excluir(id)){
						
						// Atualiza o adapter do listview
						listAdapter.remove(listAdapter.getItem(position));
						
						// Reset o banco e termina activity caso não haja mais nenhum contato
						if(dbAdapter.tamanho() <= 0){
							dbAdapter.reset();
							finish();
						}
					}
					dbAdapter.close();
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		dialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// Cancela a operação
				dialog.cancel();
			}
		});
		dialog.show();
	}
	
	

	public void onActivityResult(int request, int result, Intent data){
		if(request == requestCode){
			if(result == RESULT_OK){
				int position = data.getIntExtra("position", 0);
				finish();
			}
		}
	}



}
