package com.dieese.agenda;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import android.test.AndroidTestCase;
import android.test.RenamingDelegatingContext;

public class ContatoDatabaseTest extends AndroidTestCase{

	private DBAdapter adapter;
	
	@Override
	protected void setUp() throws Exception{
		super.setUp();
		System.out.println("SETTING UP");
		RenamingDelegatingContext context = new RenamingDelegatingContext(getContext(), "test_");

		// Cria as informações de contato
		
		String nome = "Walison";
		String nascimento = "16/06/1990";
		String empresa = "Dieese";
		String telcelular = "11962531990";
		String telresidencial = "xxxxxxx";
		String telcomercial = "xxxxxxx";
		
		// Cria a classe contato e atribue as informações
		
		Contato contato = new Contato();
		
		contato.setNome(nome);
		contato.setNascimento(nascimento);
		contato.setEmpresa(empresa);
		contato.setTelefoneCelular(telcelular);
		contato.setTelefoneComercial(telcomercial);
		contato.setTelefoneResidencial(telresidencial);
		
		DBAdapter adapter = new DBAdapter(context);
		adapter.open();
		adapter.cadastrar(contato);
		adapter.close();
	}


	protected void tearDown() throws Exception{
		super.tearDown();
		System.out.println("TEARING DOWN");
	}
}
