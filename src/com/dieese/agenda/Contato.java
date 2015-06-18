package com.dieese.agenda;

public class Contato {

	private int id;
	private String nome;
	private String nascimento;
	private String empresa;
	private String telefoneCelular;
	private String telefoneResidencial;
	private String telefoneComercial;
	
	// Setters
	
	public void setId (int id) { this.id = id; }
	public void setNome(String nome) { this.nome = nome; }
	public void setEmpresa (String empresa) { this.empresa = empresa; }
	public void setNascimento(String nascimento) { this.nascimento = nascimento; }
	public void setTelefoneCelular(String celular) { this.telefoneCelular = celular; }
	public void setTelefoneResidencial(String residencial) { this.telefoneResidencial = residencial; }
	public void setTelefoneComercial(String comercial) { this.telefoneComercial = comercial; }
	
	// Getters
	
	public int getId() { return id; }
	public String getNome() { return nome; }
	public String getEmpresa() { return empresa; }
	public String getNascimento() { return nascimento; }
	public String getTelefoneCelular() { return telefoneCelular; }
	public String getTelefoneResidencial() { return telefoneResidencial; }
	public String getTelefoneComercial() { return telefoneComercial; }
	

}
