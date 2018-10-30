package gui;

import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import negocio.GerenciadorJBDC;

public class MainGUI extends JFrame {

	private JPanel contentPane;
	private JTextField txtNome;
	private JTextField txtCPF;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//copiar design de janela do SO
					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
					
					MainGUI frame = new MainGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	
	private static final JFileChooser selecionarImg = new JFileChooser();
	private static final File workingDirectory = new File(System.getProperty("user.dir"));
	private JPanel panelFoto;
	private JLabel fotoUsuario;
	private String arquivoFotoAtual;
	
	//sem jar
	private String currentPath=MainGUI.class.getProtectionDomain().getCodeSource().getLocation().getPath().substring(1).replace('/', '\\');
	
	
	public void atualizarImgUsuario(String arquivo) {		
		System.out.println(arquivo);
    	BufferedImage img = null;
		try {
		    img = ImageIO.read(new File(arquivo));
		} catch (IOException e) {
		    e.printStackTrace();
		}
    	Image dimg = img.getScaledInstance(panelFoto.getWidth(), panelFoto.getHeight(), Image.SCALE_SMOOTH);		
		//fotoUsuario = new JLabel(new ImageIcon(dimg));
		
        ImageIcon icon = new ImageIcon(dimg);
        fotoUsuario.setIcon(icon);
            
        this.setVisible(true);
        this.panelFoto.revalidate();
        this.repaint();
        arquivoFotoAtual = arquivo;
}

    //public static final String caminhoLocal = "C:\\Users\\rslgp\\Desktop\\eclipse\\0workspace\\gdi\\src\\";
    
    public static String selecionarArquivo(){
		
    	if(selecionarImg.showOpenDialog(null)==JFileChooser.APPROVE_OPTION){
        	
    		return selecionarImg.getSelectedFile().getAbsolutePath();
    	}
    	
    	return null;
    }
    
    public String buscarArquivoLocal(String arquivo){
    	System.out.println("buscar arquivo: "+arquivo);
    	return currentPath+arquivo;
    }
    
    
	public MainGUI() {
		//com jar
		//currentPath = currentPath.substring(0,currentPath.lastIndexOf("\\")+1);
		
		this.selecionarImg.setCurrentDirectory(this.workingDirectory);
		GerenciadorJBDC g = new GerenciadorJBDC();
				
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 461, 266);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel_usuarios = new JPanel();
		panel_usuarios.setBorder(new TitledBorder(null, "Cadastro Usuario", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_usuarios.setBounds(6, 11, 249, 134);
		contentPane.add(panel_usuarios);
		panel_usuarios.setLayout(null);
		
		JPanel panel_nome = new JPanel();
		panel_nome.setBounds(10, 22, 221, 20);
		panel_usuarios.add(panel_nome);
		panel_nome.setLayout(null);
				
		JLabel lblNome = new JLabel("Nome:");
		lblNome.setBounds(0, 3, 46, 14);
		panel_nome.add(lblNome);
		
		txtNome = new JTextField();
		txtNome.setBounds(37, 0, 184, 20);
		panel_nome.add(txtNome);
		txtNome.setColumns(10);
		
		JPanel panel_cpf = new JPanel();
		panel_cpf.setBounds(10, 64, 221, 20);
		panel_usuarios.add(panel_cpf);
		panel_cpf.setLayout(null);
		
		JLabel lblCpf = new JLabel("CPF:");
		lblCpf.setBounds(0, 3, 46, 14);
		panel_cpf.add(lblCpf);
		
		txtCPF = new JTextField();
		txtCPF.setBounds(37, 0, 184, 20);
		panel_cpf.add(txtCPF);
		txtCPF.setColumns(10);
		
		JButton button_1 = new JButton("buscar");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String cpf = "'"+txtCPF.getText()+"'";
								
				atualizarImgUsuario( g.lerValorArquivo("UsuarioFoto", cpf, buscarArquivoLocal("BAIXADO3x4.png")) );
				txtNome.setText(g.select("nome", "usuario", "where cpf="+cpf));
			}
		});
		button_1.setBounds(79, 100, 89, 23);
		panel_usuarios.add(button_1);
		
		panelFoto = new JPanel();
		panelFoto.setBounds(302, 11, 119, 144);
		

		fotoUsuario = new JLabel(); //nao pode criar um novo em atualizar img
        panelFoto.add(fotoUsuario); //nao pode add dnvo em atualizar img
        
		contentPane.add(panelFoto);		

				
		atualizarImgUsuario( buscarArquivoLocal("desconhecido.png") );
		
		JButton btnFoto = new JButton("+ FOTO");
		btnFoto.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				String arquivo = selecionarArquivo();
				System.out.println(arquivo);
				atualizarImgUsuario(arquivo);
				
			}
		});
		btnFoto.setBounds(332, 160, 89, 23);
		contentPane.add(btnFoto);
		
		JButton btnConfirmar = new JButton("confirmar Cadastro");
		btnConfirmar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String nome = txtNome.getText();
				String cpf = txtCPF.getText();
				String arquivo = arquivoFotoAtual;
				
				g.inserirValores("usuario", new String[]{"nome", "cpf"}, new String[]{"'"+nome+"'", "'"+cpf+"'"});
				System.out.println(arquivo);
				g.inserirValoresArquivo("usuariofoto", new String[]{"'"+cpf+"'", arquivo});
				//g.inserirValores("usuario", variaveis, valores);
			}
		});
		btnConfirmar.setBounds(155, 193, 136, 23);
		contentPane.add(btnConfirmar);
		

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
