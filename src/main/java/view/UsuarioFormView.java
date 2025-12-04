package view;

import controller.UsuarioController;
import model.Usuario;
import util.PasswordUtil;

import javax.swing.*;
import java.awt.*;

/**
 * Formulário para cadastro e edição de usuários.
 * Pode ser usado tanto para criar novos usuários quanto para editar existentes.
 * 
 * @author Emerson Meneses Inocente <emerson_inocente@hotmail.com>
 * @version 1.0
 */
public class UsuarioFormView extends JDialog {
    
    // Referências
    private Usuario usuario;
    private Usuario usuarioLogado;
    private UsuarioController controller;
    private MainView parentView;
    
    // Componentes do formulário - Dados básicos
    private JTextField txtNome;
    private JTextField txtEmail;
    private JPasswordField txtSenha;
    private JPasswordField txtConfirmarSenha;
    private JTextField txtTelefone;
    private JComboBox<Usuario.Perfil> cbPerfil;
    
    // Componentes do formulário - Endereço
    private JTextField txtEndereco;
    private JTextField txtCidade;
    private JTextField txtEstado;
    private JTextField txtPais;
    private JTextField txtCodigoPostal;
    
    // Componentes de ação
    private JButton btnSalvar;
    private JButton btnCancelar;
    private JButton btnAlterarSenha;
    private JCheckBox chkMostrarSenha;
    
    // Indicadores de validação
    private JLabel lblValidacaoNome;
    private JLabel lblValidacaoEmail;
    private JLabel lblValidacaoSenha;
    private JLabel lblForcaSenha;
    
    /**
     * Construtor do formulário.
     * 
     * @param parent Tela principal (MainView)
     * @param usuario Usuario a ser editado (null para novo cadastro)
     * @param usuarioLogado Usuario que está logado no sistema
     */
    public UsuarioFormView(MainView parent, Usuario usuario, Usuario usuarioLogado) {
        super(parent, usuario == null ? "Novo Usuário" : "Editar Usuário", true);
        this.parentView = parent;
        this.usuario = usuario;
        this.usuarioLogado = usuarioLogado;
        this.controller = new UsuarioController();
        
        inicializarComponentes();
        
        if (usuario != null) {
            preencherCampos();
        }
    }
    
    /**
     * Inicializa e configura todos os componentes da interface.
     */
    private void inicializarComponentes() {
        setSize(700, 750);
        setLocationRelativeTo(parentView);
        setResizable(false);
        
        // Painel principal com scroll
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(245, 245, 245));
        
        // ========== PAINEL DO FORMULÁRIO ==========
        JPanel formPanel = criarPainelFormulario();
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        
        // ========== PAINEL DE BOTÕES ==========
        JPanel buttonPanel = criarPainelBotoes();
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    /**
     * Cria o painel do formulário.
     */
    private JPanel criarPainelFormulario() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // ========== SEÇÃO: DADOS PESSOAIS ==========
        panel.add(criarTituloSecao("👤 Dados Pessoais"));
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Nome
        panel.add(criarLabel("Nome *"));
        JPanel nomePanel = new JPanel(new BorderLayout(5, 0));
        nomePanel.setBackground(Color.WHITE);
        txtNome = criarCampoTexto();
        lblValidacaoNome = criarLabelValidacao();
        nomePanel.add(txtNome, BorderLayout.CENTER);
        nomePanel.add(lblValidacaoNome, BorderLayout.EAST);
        panel.add(nomePanel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(criarDica("Mínimo 3 caracteres"));
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Email
        panel.add(criarLabel("Email *"));
        JPanel emailPanel = new JPanel(new BorderLayout(5, 0));
        emailPanel.setBackground(Color.WHITE);
        txtEmail = criarCampoTexto();
        lblValidacaoEmail = criarLabelValidacao();
        emailPanel.add(txtEmail, BorderLayout.CENTER);
        emailPanel.add(lblValidacaoEmail, BorderLayout.EAST);
        panel.add(emailPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(criarDica("exemplo@dominio.com"));
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Telefone
        panel.add(criarLabel("Telefone"));
        txtTelefone = criarCampoTexto();
        panel.add(txtTelefone);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(criarDica("Formato internacional: +55 51 99999-9999"));
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Perfil
        panel.add(criarLabel("Perfil *"));
        cbPerfil = new JComboBox<>(Usuario.Perfil.values());
        cbPerfil.setFont(new Font("Arial", Font.PLAIN, 14));
        cbPerfil.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        cbPerfil.setBackground(Color.WHITE);
        
        // Apenas admin pode alterar perfil para admin
        if (!usuarioLogado.isAdmin()) {
            cbPerfil.setEnabled(false);
            cbPerfil.setSelectedItem(Usuario.Perfil.user);
        }
        
        panel.add(cbPerfil);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        // ========== SEÇÃO: SENHA (apenas para novo usuário) ==========
        if (usuario == null) {
            panel.add(criarTituloSecao("🔒 Senha"));
            panel.add(Box.createRigidArea(new Dimension(0, 15)));
            
            // Senha
            panel.add(criarLabel("Senha *"));
            JPanel senhaPanel = new JPanel(new BorderLayout(5, 0));
            senhaPanel.setBackground(Color.WHITE);
            txtSenha = new JPasswordField();
            txtSenha.setFont(new Font("Arial", Font.PLAIN, 14));
            txtSenha.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
            ));
            txtSenha.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
            
            lblValidacaoSenha = criarLabelValidacao();
            senhaPanel.add(txtSenha, BorderLayout.CENTER);
            senhaPanel.add(lblValidacaoSenha, BorderLayout.EAST);
            panel.add(senhaPanel);
            panel.add(Box.createRigidArea(new Dimension(0, 10)));
            
            // Força da senha
            lblForcaSenha = new JLabel();
            lblForcaSenha.setFont(new Font("Arial", Font.ITALIC, 11));
            lblForcaSenha.setAlignmentX(Component.LEFT_ALIGNMENT);
            panel.add(lblForcaSenha);
            panel.add(Box.createRigidArea(new Dimension(0, 10)));
            
            // Confirmar senha
            panel.add(criarLabel("Confirmar Senha *"));
            txtConfirmarSenha = new JPasswordField();
            txtConfirmarSenha.setFont(new Font("Arial", Font.PLAIN, 14));
            txtConfirmarSenha.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
            ));
            txtConfirmarSenha.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
            panel.add(txtConfirmarSenha);
            panel.add(Box.createRigidArea(new Dimension(0, 10)));
            
            // Mostrar senha
            chkMostrarSenha = new JCheckBox("Mostrar senhas");
            chkMostrarSenha.setFont(new Font("Arial", Font.PLAIN, 12));
            chkMostrarSenha.setBackground(Color.WHITE);
            chkMostrarSenha.setAlignmentX(Component.LEFT_ALIGNMENT);
            chkMostrarSenha.addActionListener(e -> {
                if (chkMostrarSenha.isSelected()) {
                    txtSenha.setEchoChar((char) 0);
                    txtConfirmarSenha.setEchoChar((char) 0);
                } else {
                    txtSenha.setEchoChar('•');
                    txtConfirmarSenha.setEchoChar('•');
                }
            });
            panel.add(chkMostrarSenha);
            panel.add(Box.createRigidArea(new Dimension(0, 5)));
            panel.add(criarDica("Mínimo 6 caracteres"));
            panel.add(Box.createRigidArea(new Dimension(0, 20)));
            
            // Listener para avaliar força da senha em tempo real
            txtSenha.addKeyListener(new java.awt.event.KeyAdapter() {
                public void keyReleased(java.awt.event.KeyEvent evt) {
                    avaliarForcaSenha();
                }
            });
        } else {
            // Botão para alterar senha (modo edição)
            panel.add(criarTituloSecao("🔒 Senha"));
            panel.add(Box.createRigidArea(new Dimension(0, 15)));
            
            btnAlterarSenha = new JButton("Alterar Senha");
            btnAlterarSenha.setFont(new Font("Arial", Font.PLAIN, 13));
            btnAlterarSenha.setBackground(new Color(240, 173, 78));
            btnAlterarSenha.setForeground(Color.WHITE);
            btnAlterarSenha.setFocusPainted(false);
            btnAlterarSenha.setCursor(new Cursor(Cursor.HAND_CURSOR));
            btnAlterarSenha.setAlignmentX(Component.LEFT_ALIGNMENT);
            btnAlterarSenha.setMaximumSize(new Dimension(200, 35));
            btnAlterarSenha.addActionListener(e -> abrirDialogoAlterarSenha());
            panel.add(btnAlterarSenha);
            panel.add(Box.createRigidArea(new Dimension(0, 20)));
        }
        
        // ========== SEÇÃO: ENDEREÇO ==========
        panel.add(criarTituloSecao("📍 Endereço"));
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Endereço
        panel.add(criarLabel("Endereço"));
        txtEndereco = criarCampoTexto();
        panel.add(txtEndereco);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Cidade
        panel.add(criarLabel("Cidade"));
        txtCidade = criarCampoTexto();
        panel.add(txtCidade);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Estado
        panel.add(criarLabel("Estado"));
        txtEstado = criarCampoTexto();
        panel.add(txtEstado);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // País
        panel.add(criarLabel("País"));
        txtPais = criarCampoTexto();
        panel.add(txtPais);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        
        // Código Postal
        panel.add(criarLabel("Código Postal / CEP"));
        txtCodigoPostal = criarCampoTexto();
        panel.add(txtCodigoPostal);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));
        
        return panel;
    }
    
    /**
     * Cria o painel de botões.
     */
    private JPanel criarPainelBotoes() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(245, 245, 245));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        
        // Label de campos obrigatórios
        JLabel lblObrigatorio = new JLabel("* Campos obrigatórios");
        lblObrigatorio.setFont(new Font("Arial", Font.ITALIC, 11));
        lblObrigatorio.setForeground(new Color(217, 83, 79));
        
        // Painel de botões de ação
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        actionPanel.setBackground(new Color(245, 245, 245));
        
        btnSalvar = new JButton(usuario == null ? "💾 Cadastrar" : "💾 Salvar");
        btnSalvar.setFont(new Font("Arial", Font.BOLD, 14));
        btnSalvar.setBackground(new Color(92, 184, 92));
        btnSalvar.setForeground(Color.WHITE);
        btnSalvar.setFocusPainted(false);
        btnSalvar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSalvar.setPreferredSize(new Dimension(150, 40));
        btnSalvar.addActionListener(e -> salvar());
        
        btnCancelar = new JButton("❌ Cancelar");
        btnCancelar.setFont(new Font("Arial", Font.PLAIN, 14));
        btnCancelar.setBackground(new Color(150, 150, 150));
        btnCancelar.setForeground(Color.WHITE);
        btnCancelar.setFocusPainted(false);
        btnCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnCancelar.setPreferredSize(new Dimension(150, 40));
        btnCancelar.addActionListener(e -> cancelar());
        
        actionPanel.add(btnSalvar);
        actionPanel.add(btnCancelar);
        
        panel.add(lblObrigatorio, BorderLayout.WEST);
        panel.add(actionPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Cria um título de seção.
     */
    private JLabel criarTituloSecao(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(new Color(66, 139, 202));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }
    
    /**
     * Cria um label padrão.
     */
    private JLabel criarLabel(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.BOLD, 13));
        label.setForeground(new Color(51, 51, 51));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }
    
    /**
     * Cria um campo de texto padrão.
     */
    private JTextField criarCampoTexto() {
        JTextField campo = new JTextField();
        campo.setFont(new Font("Arial", Font.PLAIN, 14));
        campo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        campo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        return campo;
    }
    
    /**
     * Cria uma dica informativa.
     */
    private JLabel criarDica(String texto) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.ITALIC, 11));
        label.setForeground(new Color(120, 120, 120));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }
    
    /**
     * Cria um label de validação (ícone de status).
     */
    private JLabel criarLabelValidacao() {
        JLabel label = new JLabel();
        label.setPreferredSize(new Dimension(20, 20));
        return label;
    }
    
    /**
     * Preenche os campos com os dados do usuário (modo edição).
     */
    private void preencherCampos() {
        txtNome.setText(usuario.getNome());
        txtEmail.setText(usuario.getEmail());
        txtTelefone.setText(usuario.getTelefone() != null ? usuario.getTelefone() : "");
        cbPerfil.setSelectedItem(usuario.getPerfil());
        txtEndereco.setText(usuario.getEndereco() != null ? usuario.getEndereco() : "");
        txtCidade.setText(usuario.getCidade() != null ? usuario.getCidade() : "");
        txtEstado.setText(usuario.getEstado() != null ? usuario.getEstado() : "");
        txtPais.setText(usuario.getPais() != null ? usuario.getPais() : "");
        txtCodigoPostal.setText(usuario.getCodigoPostal() != null ? usuario.getCodigoPostal() : "");
    }
    
    /**
     * Avalia a força da senha em tempo real.
     */
    private void avaliarForcaSenha() {
        String senha = new String(txtSenha.getPassword());
        
        if (senha.isEmpty()) {
            lblForcaSenha.setText("");
            return;
        }
        
        String forca = PasswordUtil.avaliarForcaSenha(senha);
        lblForcaSenha.setText("Força: " + forca);
        
        if (forca.contains("fraca")) {
            lblForcaSenha.setForeground(new Color(217, 83, 79));
        } else if (forca.contains("média")) {
            lblForcaSenha.setForeground(new Color(240, 173, 78));
        } else {
            lblForcaSenha.setForeground(new Color(92, 184, 92));
        }
    }
    
    /**
     * Abre diálogo para alterar senha (modo edição).
     */
    private void abrirDialogoAlterarSenha() {
        AlterarSenhaDialog dialog = new AlterarSenhaDialog(this, usuario.getId(), controller);
        dialog.setVisible(true);
    }
    
    /**
     * Salva o usuário (cadastro ou edição).
     */
    private void salvar() {
        Usuario usuarioSalvar = usuario != null ? usuario : new Usuario();
        
        // Preenche dados básicos
        usuarioSalvar.setNome(txtNome.getText().trim());
        usuarioSalvar.setEmail(txtEmail.getText().trim());
        usuarioSalvar.setTelefone(txtTelefone.getText().trim());
        usuarioSalvar.setPerfil((Usuario.Perfil) cbPerfil.getSelectedItem());
        
        // Preenche endereço
        usuarioSalvar.setEndereco(txtEndereco.getText().trim());
        usuarioSalvar.setCidade(txtCidade.getText().trim());
        usuarioSalvar.setEstado(txtEstado.getText().trim());
        usuarioSalvar.setPais(txtPais.getText().trim());
        usuarioSalvar.setCodigoPostal(txtCodigoPostal.getText().trim());
        
        // Para novos usuários, valida e define senha
        if (usuario == null) {
            String senha = new String(txtSenha.getPassword());
            String confirmarSenha = new String(txtConfirmarSenha.getPassword());
            
            if (senha.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Senha é obrigatória!", 
                    "Erro", 
                    JOptionPane.ERROR_MESSAGE);
                txtSenha.requestFocus();
                return;
            }
            
            if (!senha.equals(confirmarSenha)) {
                JOptionPane.showMessageDialog(this, 
                    "As senhas não coincidem!", 
                    "Erro", 
                    JOptionPane.ERROR_MESSAGE);
                txtConfirmarSenha.requestFocus();
                return;
            }
            
            usuarioSalvar.setSenha(senha);
        }
        
        // Salva no banco
        boolean sucesso;
        if (usuario == null) {
            sucesso = controller.cadastrar(usuarioSalvar);
        } else {
            sucesso = controller.atualizar(usuarioSalvar);
        }
        
        if (sucesso) {
            parentView.carregarUsuarios();
            parentView.atualizarEstatisticas();
            dispose();
        }
    }
    
    /**
     * Cancela a operação e fecha o formulário.
     */
    private void cancelar() {
        int confirmacao = JOptionPane.showConfirmDialog(
            this,
            "Deseja realmente cancelar?\nAs alterações não serão salvas.",
            "Confirmar Cancelamento",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (confirmacao == JOptionPane.YES_OPTION) {
            dispose();
        }
    }
    
    /**
     * Diálogo interno para alterar senha.
     */
    private class AlterarSenhaDialog extends JDialog {
        private JPasswordField txtSenhaAtual;
        private JPasswordField txtNovaSenha;
        private JPasswordField txtConfirmarNovaSenha;
        private JCheckBox chkMostrar;
        private int usuarioId;
        private UsuarioController ctrl;
        
        public AlterarSenhaDialog(JDialog parent, int usuarioId, UsuarioController controller) {
            super(parent, "Alterar Senha", true);
            this.usuarioId = usuarioId;
            this.ctrl = controller;
            
            setSize(450, 350);
            setLocationRelativeTo(parent);
            setResizable(false);
            
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
            panel.setBackground(Color.WHITE);
            
            // Senha atual
            panel.add(criarLabel("Senha Atual"));
            txtSenhaAtual = new JPasswordField();
            txtSenhaAtual.setFont(new Font("Arial", Font.PLAIN, 14));
            txtSenhaAtual.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
            panel.add(txtSenhaAtual);
            panel.add(Box.createRigidArea(new Dimension(0, 15)));
            
            // Nova senha
            panel.add(criarLabel("Nova Senha"));
            txtNovaSenha = new JPasswordField();
            txtNovaSenha.setFont(new Font("Arial", Font.PLAIN, 14));
            txtNovaSenha.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
            panel.add(txtNovaSenha);
            panel.add(Box.createRigidArea(new Dimension(0, 15)));
            
            // Confirmar nova senha
            panel.add(criarLabel("Confirmar Nova Senha"));
            txtConfirmarNovaSenha = new JPasswordField();
            txtConfirmarNovaSenha.setFont(new Font("Arial", Font.PLAIN, 14));
            txtConfirmarNovaSenha.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
            panel.add(txtConfirmarNovaSenha);
            panel.add(Box.createRigidArea(new Dimension(0, 10)));
            
            // Mostrar senhas
            chkMostrar = new JCheckBox("Mostrar senhas");
            chkMostrar.setBackground(Color.WHITE);
            chkMostrar.setAlignmentX(Component.LEFT_ALIGNMENT);
            chkMostrar.addActionListener(e -> {
                char echo = chkMostrar.isSelected() ? (char) 0 : '•';
                txtSenhaAtual.setEchoChar(echo);
                txtNovaSenha.setEchoChar(echo);
                txtConfirmarNovaSenha.setEchoChar(echo);
            });
            panel.add(chkMostrar);
            panel.add(Box.createRigidArea(new Dimension(0, 20)));
            
            // Botões
            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
            btnPanel.setBackground(Color.WHITE);
            
            JButton btnAlterar = new JButton("Alterar");
            btnAlterar.setBackground(new Color(92, 184, 92));
            btnAlterar.setForeground(Color.WHITE);
            btnAlterar.setFocusPainted(false);
            btnAlterar.addActionListener(e -> alterarSenha());
            
            JButton btnCancelar = new JButton("Cancelar");
            btnCancelar.setBackground(new Color(150, 150, 150));
            btnCancelar.setForeground(Color.WHITE);
            btnCancelar.setFocusPainted(false);
            btnCancelar.addActionListener(e -> dispose());
            
            btnPanel.add(btnAlterar);
            btnPanel.add(btnCancelar);
            panel.add(btnPanel);
            
            add(panel);
        }
        
        private void alterarSenha() {
            String senhaAtual = new String(txtSenhaAtual.getPassword());
            String novaSenha = new String(txtNovaSenha.getPassword());
            String confirmacao = new String(txtConfirmarNovaSenha.getPassword());
            
            if (ctrl.alterarSenha(usuarioId, senhaAtual, novaSenha, confirmacao)) {
                dispose();
            }
        }
    }
}