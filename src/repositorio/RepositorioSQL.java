package repositorio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class RepositorioSQL {
	private Connection con;
	private Statement stmt;
	
	private final String espaco = " ", values= ") VALUES (", abreParenteses="(", fechaParenteses=")", quebraLinha="\r\n", espacamento = "\t";
	
	public RepositorioSQL(String url_driver, String user_bd, String password_bd) {
		try{			
			Class.forName("oracle.jdbc.driver.OracleDriver"); //jdbc connection (load and register driver) to drivermanager
			//https://www.codeproject.com/Questions/432728/How-to-connect-java-and-Oracle
			
			con = DriverManager.getConnection(url_driver, user_bd, password_bd);
			
			//falta fechar con em algum momento, no final do programa
			
			
		}catch(ClassNotFoundException e){
			System.err.println("erro jdbc connection");
			
		} catch(Exception e){
			System.err.println("erro gerenciador");
			e.printStackTrace();
		}
	}
	
	public void executarComando(String comandoSQL, String funcao){		
		try{
			stmt = con.createStatement();
			
			stmt.executeUpdate(comandoSQL);
			
			stmt.close();
			
			System.out.println("ACAO: "+funcao);
		}catch(Exception e){
			System.err.println("ERRO: "+funcao);
			e.printStackTrace();
		}
	}
	
	public void criarTabela(String nome, String[] variaveisTipo){ //nome: hospital, variaveisTipo: {"CGC NUMBER", "NOME VARCHAR2(40)"...} 
		String comandoSQL = "CREATE TABLE " + nome + espaco + abreParenteses + String.join(",", variaveisTipo) + fechaParenteses;
		System.out.println(comandoSQL);
		
		executarComando(comandoSQL, "criar tabela");
	}
	
	public void inserirValores(String nomeTabela, String[] variaveis, String[] valores){ //nome: hospital, variaveis: {"CGC", "NOME"}, valores:{1, 'restauracao', 1000}} 
		String comandoSQL = "INSERT INTO " + nomeTabela + espaco + abreParenteses + String.join(",", variaveis) + values + String.join(",", valores) + fechaParenteses;
		System.out.println(comandoSQL);
		executarComando(comandoSQL, "inserir Valores");
		//SE DER ERRO CONFERE SE STRING TA COM ASPAS SIMPLES
	}
	
	public void inserirValoresArquivo(String nomeTabela, String[] valores){ //nome: hospital, valores:{ID, CAMINHOARQUIVO}} 
		//String comandoSQL = "INSERT INTO " + nomeTabela + espaco + values + String.join(",", valores) + fechaParenteses;
		//https://www.javatpoint.com/storing-image-in-oracle-database
		//http://www.mysqltutorial.org/mysql-jdbc-blob
		
		String padraoVariaveisTabelaArquivo="(id, arquivo)";
		try {
			PreparedStatement ps=con.prepareStatement("insert into "+nomeTabela+espaco+padraoVariaveisTabelaArquivo+" values (?,?)");  
			ps.setString(1,valores[0]);  
			  
			FileInputStream fin=new FileInputStream(new File(valores[1]));  
			ps.setBinaryStream(2,fin);
			
			ps.executeUpdate();			
		}catch(Exception e) {
			System.err.println("erro arquivo");
			e.printStackTrace();
		}		
		System.out.println("inseridos");
	}
	
	public String lerValorArquivo(String nomeTabela, String id, String arquivoResultado){ //nome: hospital, valores:{ID, CAMINHOARQUIVO}} 
		//String comandoSQL = "INSERT INTO " + nomeTabela + espaco + values + String.join(",", valores) + fechaParenteses;
		//https://www.javatpoint.com/storing-image-in-oracle-database
		//http://www.mysqltutorial.org/mysql-jdbc-blob
		String colunaArquivo = "arquivo";
		String selectSQL = "SELECT "+colunaArquivo+" FROM "+nomeTabela+" WHERE id=?";
		ResultSet rs = null;
		try {
			PreparedStatement pstmt = con.prepareStatement(selectSQL);
			pstmt.setString(1, id);
			
			rs = pstmt.executeQuery();
			
			File file = new File(arquivoResultado);
			FileOutputStream output = new FileOutputStream(file);
			 
			System.out.println("Writing to file " + file.getAbsolutePath());
			while (rs.next()) {
			    InputStream input = rs.getBinaryStream(colunaArquivo);
			    byte[] buffer = new byte[1024];
			    while (input.read(buffer) > 0) {
			        output.write(buffer);
			    }
			    input.close();
			    output.close();
			}
			rs.close();
			
			return file.getAbsolutePath();
			
			
		}catch(Exception e) {
			System.err.println("erro arquivo");
			e.printStackTrace();
		}		
		System.out.println("lido");
		return null;
	}
	
	public void removerDados(String nomeTabela, String condicoes){ //nome: hospital, condicoes: "" ou "where ..."} 
		String comandoSQL = "DELETE FROM " + nomeTabela + espaco + condicoes;
		
		executarComando(comandoSQL, "remover Dados");
	}
	
	public void dropTabela(String nomeTabela){ //nome: hospital, condicoes: "" ou "where ..."} 
		String comandoSQL = "DROP TABLE " + nomeTabela;
		
		executarComando(comandoSQL, "drop table");
	}
	
	public void updateValores(String nomeTabela, String[] variaveisSET, String[] condicoes){ //nome: hospital, variaveis: {"CGC", "NOME"}, valores:{1, 'restauracao', 1000}} 
		String comandoSQL = "UPDATE " + nomeTabela + " SET " + String.join(",", variaveisSET) + " WHERE " + String.join(",", condicoes) + fechaParenteses;
		
		executarComando(comandoSQL, "update Valores");
	}
	
	public String select(String colunasSelect, String nomeTabela, String condicoes){ //nome: hospital, condicoes: "" ou "where ..."} 
		String comandoSQL = "SELECT " + colunasSelect + " FROM " + nomeTabela + espaco + condicoes;
		
		System.out.println(comandoSQL);
		//DUVIDA HARD, n tem como generalizar
		ResultSet rs = null;
		String resultado="";
		try{
			stmt = con.createStatement();
			rs = stmt.executeQuery(comandoSQL);
			
			boolean tudo = colunasSelect.contentEquals("*");
			
			//depende da tabela
			switch(nomeTabela.toLowerCase()){
				case "usuario":
					String nome, cpf;
						while(rs.next()){
							
							if(tudo){
								resultado += "nome" + espacamento + "cpf" + quebraLinha;
								nome = rs.getString("nome");
								cpf = rs.getString("cpf");
								resultado += nome + espacamento + cpf + quebraLinha;
							}else{
								if(colunasSelect.contentEquals("nome")) {
									nome = rs.getString("nome");
									resultado += nome + quebraLinha;
								}
								else if(colunasSelect.contentEquals("cpf")) {
									cpf = rs.getString("cpf");
									resultado += cpf + quebraLinha;
								}
							}
						}
					break;
		
			}
			rs.close();
			
			//while(rs.next()){ //precisa fazer a leitura e fechar fora do metodo
			//}
			//rs.close();
			
			stmt.close();
		}catch(Exception e){
			System.err.println("erro select");
			e.printStackTrace();
		}
		
		return resultado;
	}	
	
}
