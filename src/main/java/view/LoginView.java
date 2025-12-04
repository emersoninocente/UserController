package view;

import controller.UsuarioController;
import model.Usuario;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Tela de login do sistema.
 * Permite que usuários façam autenticação para acessar o sistema.
 * 
 * @author Emerson Meneses Inocente <emerson_inocente@hotmail.com>
 * @version 1.0
 */
public class LoginView extends JFrame {
    
    // Componentes da interface
    private JTextField txtEmail;
    private JPasswordField txtSenha;
    private JButton btnLogin;
    private JButton btnLimpar;
    private JCheckBox chkMostrarSenha;
    private JLabel lblStatus;
    
    // Controller
    private UsuarioController controller;
    
    /**
     * Construtor da tela de login.
     * Inicializa todos os componentes da interface.
     */
    public LoginView() {
        controller = new UsuarioController();
        inicializarComponentes();
        configurarEventos();
    }
    
    /**
     * Inicializa e configura todos os componentes da interface.
     */
    private void inicializarComponentes() {
        // Configurações da janela
        setTitle("Sistema de Gerenciamento de Usuários - Login");
        setSize(450, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Painel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        mainPanel.setBackground(new Color(245, 245, 245));
        
        // ========== PAINEL SUPERIOR - LOGO E TÍTULO ==========
        JPanel topPanel = criarPainelSuperior();
        
        // ========== PAINEL CENTRAL - FORMULÁRIO ==========
        JPanel centerPanel = criarPainelFormulario();
        
        // ========== PAINEL INFERIOR - BOTÕES ==========
        JPanel bottomPanel = criarPainelBotoes();
        
        // ========== PAINEL DE STATUS ==========
        JPanel statusPanel = criarPainelStatus();
        
        // Adiciona painéis ao frame
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);
        
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.add(mainPanel, BorderLayout.CENTER);
        wrapper.add(statusPanel, BorderLayout.SOUTH);
        
        add(wrapper);
    }
    
    /**
     * Cria o painel superior com logo e título.
     */
    private JPanel criarPainelSuperior() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        
        // Ícone/Logo
        JLabel lblIcone = new JLabel("🔐");
        lblIcone.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 60));
        lblIcone.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Título principal
        JLabel lblTitulo = new JLabel("BEM-VINDO");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setForeground(new Color(51, 51, 51));
        lblTitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Subtítulo
        JLabel lblSubtitulo = new JLabel("Sistema de Gerenciamento de Usuários");
        lblSubtitulo.setFont(new Font("Arial", Font.PLAIN, 14));
        lblSubtitulo.setForeground(new Color(102, 102, 102));
        lblSubtitulo.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        panel.add(lblIcone);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(lblTitulo);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(lblSubtitulo);
        
        return panel;
    }
    
    /**
     * Cria o painel do formulário de login.
     */
    private JPanel criarPainelFormulario() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 8, 8, 8);
        
        // ========== EMAIL ==========
        JLabel lblEmail = new JLabel("Email:");
        lblEmail.setFont(new Font("Arial", Font.BOLD, 14));
        lblEmail.setForeground(new Color(51, 51, 51));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(lblEmail, gbc);
        
        txtEmail = new JTextField(25);
        txtEmail.setFont(new Font("Arial", Font.PLAIN, 14));
        txtEmail.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        gbc.gridy = 1;
        panel.add(txtEmail, gbc);
        
        // ========== SENHA ==========
        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setFont(new Font("Arial", Font.BOLD, 14));
        lblSenha.setForeground(new Color(51, 51, 51));
        gbc.gridy = 2;
        gbc.insets = new Insets(20, 8, 8, 8);
        panel.add(lblSenha, gbc);
        
        txtSenha = new JPasswordField(25);
        txtSenha.setFont(new Font("Arial", Font.PLAIN, 14));
        txtSenha.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        gbc.gridy = 3;
        gbc.insets = new Insets(8, 8, 8, 8);
        panel.add(txtSenha, gbc);
        
        // ========== CHECKBOX MOSTRAR SENHA ==========
        chkMostrarSenha = new JCheckBox("Mostrar senha");
        chkMostrarSenha.setFont(new Font("Arial", Font.PLAIN, 12));
        chkMostrarSenha.setBackground(Color.WHITE);
        chkMostrarSenha.setFocusPainted(false);
        gbc.gridy = 4;
        gbc.insets = new Insets(5, 8, 8, 8);
        panel.add(chkMostrarSenha, gbc);
        
        return panel;
    }
    
    /**
     * Cria o painel com os botões de ação.
     */
    private JPanel criarPainelBotoes() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 20));
        panel.setBackground(new Color(245, 245, 245));
        
        // Botão Login
        btnLogin = new JButton("Entrar");
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogin.setBackground(new Color(66, 139, 202));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLogin.setPreferredSize(new Dimension(150, 40));
        btnLogin.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Efeito hover no botão Login
        btnLogin.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLogin.setBackground(new Color(51, 122, 183));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnLogin.setBackground(new Color(66, 139, 202));
            }
        });
        
        // Botão Limpar
        btnLimpar = new JButton("Limpar");
        btnLimpar.setFont(new Font("Arial", Font.PLAIN, 14));
        btnLimpar.setBackground(new Color(150, 150, 150));
        btnLimpar.setForeground(Color.WHITE);
        btnLimpar.setFocusPainted(false);
        btnLimpar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnLimpar.setPreferredSize(new Dimension(150, 40));
        btnLimpar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        // Efeito hover no botão Limpar
        btnLimpar.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnLimpar.setBackground(new Color(120, 120, 120));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnLimpar.setBackground(new Color(150, 150, 150));
            }
        });
        
        panel.add(btnLogin);
        panel.add(btnLimpar);
        
        return panel;
    }
    
    /**
     * Cria o painel de status na parte inferior.
     */
    private JPanel criarPainelStatus() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(240, 240, 240));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 15, 8, 15)
        ));
        
        lblStatus = new JLabel("Digite suas credenciais para acessar o sistema");
        lblStatus.setFont(new Font("Arial", Font.ITALIC, 11));
        lblStatus.setForeground(new Color(102, 102, 102));
        
        panel.add(lblStatus, BorderLayout.WEST);
        
        // Informação de versão
        JLabel lblVersao = new JLabel("v1.0");
        lblVersao.setFont(new Font("Arial", Font.PLAIN, 10));
        lblVersao.setForeground(new Color(150, 150, 150));
        panel.add(lblVersao, BorderLayout.EAST);
        
        return panel;
    }
    
    /**
     * Configura os eventos dos componentes.
     */
    private void configurarEventos() {
        // Evento do botão Login
        btnLogin.addActionListener(e -> realizarLogin());
        
        // Evento do botão Limpar
        btnLimpar.addActionListener(e -> limparCampos());
        
        // Evento do checkbox mostrar senha
        chkMostrarSenha.addActionListener(e -> {
            if (chkMostrarSenha.isSelected()) {
                txtSenha.setEchoChar((char) 0);
            } else {
                txtSenha.setEchoChar('•');
            }
        });
        
        // Enter no campo de senha para fazer login
        txtSenha.addActionListener(e -> realizarLogin());
        
        // Enter no campo de email move para senha
        txtEmail.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    txtSenha.requestFocus();
                }
            }
        });
        
        // ESC para limpar campos
        KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0);
        getRootPane().registerKeyboardAction(
            e -> limparCampos(),
            escapeKeyStroke,
            JComponent.WHEN_IN_FOCUSED_WINDOW
        );
    }
    
    /**
     * Realiza o processo de login.
     */
    private void realizarLogin() {
        // Desabilita botão para evitar múltiplos cliques
        btnLogin.setEnabled(false);
        lblStatus.setText("Autenticando...");
        lblStatus.setForeground(new Color(66, 139, 202));
        
        // Executa login em thread separada para não travar a interface
        SwingWorker<Usuario, Void> worker = new SwingWorker<Usuario, Void>() {
            @Override
            protected Usuario doInBackground() throws Exception {
                String email = txtEmail.getText();
                String senha = new String(txtSenha.getPassword());
                return controller.autenticar(email, senha);
            }
            
            @Override
            protected void done() {
                try {
                    Usuario usuario = get();
                    
                    if (usuario != null) {
                        // Login bem-sucedido
                        lblStatus.setText("Login realizado com sucesso! Abrindo sistema...");
                        lblStatus.setForeground(new Color(92, 184, 92));
                        
                        // Aguarda um momento para o usuário ver a mensagem
                        Timer timer = new Timer(500, evt -> {
                            abrirTelaPrincipal(usuario);
                        });
                        timer.setRepeats(false);
                        timer.start();
                    } else {
                        // Login falhou
                        lblStatus.setText("Falha na autenticação. Verifique suas credenciais.");
                        lblStatus.setForeground(new Color(217, 83, 79));
                        txtSenha.setText("");
                        txtSenha.requestFocus();
                        btnLogin.setEnabled(true);
                    }
                } catch (Exception e) {
                    lblStatus.setText("Erro ao autenticar: " + e.getMessage());
                    lblStatus.setForeground(new Color(217, 83, 79));
                    e.printStackTrace();
                    btnLogin.setEnabled(true);
                }
            }
        };
        
        worker.execute();
    }
    
    /**
     * Limpa os campos de email e senha.
     */
    private void limparCampos() {
        txtEmail.setText("");
        txtSenha.setText("");
        chkMostrarSenha.setSelected(false);
        txtSenha.setEchoChar('•');
        lblStatus.setText("Campos limpos. Digite suas credenciais.");
        lblStatus.setForeground(new Color(102, 102, 102));
        txtEmail.requestFocus();
    }
    
    /**
     * Abre a tela principal do sistema após login bem-sucedido.
     * 
     * @param usuario Usuario autenticado
     */
    private void abrirTelaPrincipal(Usuario usuario) {
        // Exibe mensagem de boas-vindas
        JOptionPane.showMessageDialog(
            this,
            "Bem-vindo(a), " + usuario.getNome() + "!\n" +
            "Perfil: " + usuario.getPerfil().getDescricao(),
            "Login Realizado",
            JOptionPane.INFORMATION_MESSAGE
        );
        
        // Abre tela principal
        MainView mainView = new MainView(usuario);
        mainView.setVisible(true);
        
        // Fecha tela de login
        dispose();
    }
    
    /**
     * Método main para executar a aplicação.
     */
    public static void main(String[] args) {
        // Define o Look and Feel do sistema operacional
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        // Executa a interface na Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            LoginView loginView = new LoginView();
            loginView.setVisible(true);
            
            // Foco inicial no campo de email
            loginView.txtEmail.requestFocus();
            
            // Exibe dica de credenciais padrão (apenas para desenvolvimento)
            System.out.println("=".repeat(60));
            System.out.println("SISTEMA INICIADO");
            System.out.println("=".repeat(60));
            System.out.println("Credenciais padrão:");
            System.out.println("  Email: admin@sistema.com");
            System.out.println("  Senha: admin123");
            System.out.println("=".repeat(60));
        });
    }
}