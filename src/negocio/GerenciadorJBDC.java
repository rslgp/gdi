package negocio;

import repositorio.RepositorioSQL;

public class GerenciadorJBDC {
	private RepositorioSQL sql;
	
	public GerenciadorJBDC(){		
			
			String url_driver, user_bd, password_bd;			
			url_driver="jdbc:oracle:thin:@HOST:1521:INSTANCIA";
			user_bd="USER";
			password_bd="PASS";
			
			sql = new RepositorioSQL(url_driver, user_bd, password_bd);
	}
	
	public String select(String colunasSelect, String nomeTabela, String condicoes){ //nome: hospital, condicoes: "" ou "where ..."} 
		String resultado = sql.select(colunasSelect, nomeTabela, condicoes);
		
		return resultado;
	}
	
	public void criarTabela(String nome, String[] variaveisTipo){ //nome: hospital, variaveisTipo: {"CGC NUMBER", "NOME VARCHAR2(40)"...} 
		sql.criarTabela(nome, variaveisTipo);
	}
	
	public void dropTabela(String nome){ //nome: hospital, variaveisTipo: {"CGC NUMBER", "NOME VARCHAR2(40)"...} 
		sql.dropTabela(nome);
	}
	
	public void inserirValores(String nomeTabela, String[] variaveis, String[] valores){ //nome: hospital, variaveis: {"CGC", "NOME"}, valores:{1, 'restauracao', 1000}} 
		sql.inserirValores(nomeTabela, variaveis, valores);
	}
	
	public void inserirValoresArquivo(String nomeTabela, String[] valores) { //nome: hospital, valores:{ID, arquivo}}
		sql.inserirValoresArquivo(nomeTabela, valores);
	}
	
	public String lerValorArquivo(String nomeTabela, String id, String arquivoResultado){ //nome: hospital, valores:{ID, CAMINHOARQUIVO}}
		return sql.lerValorArquivo(nomeTabela, id, arquivoResultado);
	}
	
	public void removerDados(String nomeTabela, String condicoes){ //nome: hospital, condicoes: "" ou "where ..."} 
		sql.removerDados(nomeTabela, condicoes);
	}
	
	public void updateValores(String nomeTabela, String[] variaveisSET, String[] condicoes){ //nome: hospital, variaveis: {"CGC", "NOME"}, valores:{1, 'restauracao', 1000}} 
		sql.updateValores(nomeTabela, variaveisSET, condicoes);
	}		
	
	
}
