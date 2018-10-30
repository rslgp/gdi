package gui;

import negocio.GerenciadorJBDC;

public class MainUI {
	public static void main(String[] args) {
		GerenciadorJBDC g = new GerenciadorJBDC();
		
		//apagar tabela
		//g.dropTabela("UsuarioFoto");
		//g.dropTabela("Usuario");
		
		//GERAR TABELAS e INSERIR
		//g.criarTabela("Usuario", new String[]{"nome VARCHAR2(40)", "cpf VARCHAR2(15)"});
		//g.inserirValores("usuario", new String[]{"nome", "cpf"}, new String[]{"'Rafael'", "'123456789'"});
		
		
		//g.criarTabela("UsuarioFoto", new String[]{"id VARCHAR2(15)", "arquivo BLOB"});		
		//g.inserirValoresArquivo("UsuarioFoto", new String[]{"'123456789'", "D:\\eclipse\\0workspace\\gdi\\bin\\3x4.png"});
		//FIM GERAR
		
		//imprimir todos
		//System.out.println( g.select("*", "Usuario", "") );
		
		//baixar foto
		//g.lerValorArquivo("UsuarioFoto", "'987654321'", "C:\\Users\\rslgp\\Desktop\\work\\gdi\\src\\BAIXADO3x4.png");
	}
}
