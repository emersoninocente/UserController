package view;

import controller.UsuarioController;
import model.Usuario;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.util.List;

/**
 * Tela principal do sistema com listagem de usuários.
 * Permite visualizar, criar, editar e excluir usuários.
 * 
 * @author Emerson Meneses Inocente <emerson_inocente@hotmail.com>
 * @version 1.0
 */
public class MainView extends JFrame {
    
    // Usuário logado
    private Usuario usuarioLogado;
    
    // Controller
    private UsuarioController controller;
    
    // Componentes da tabela
    private JTable tabelaUsuarios;
    private DefaultTableModel tableModel;
    
    // Componentes de interface
    private JButton btnNovo, btnEditar, btnExcluir, btnAtualizar, btnSair;
    private JTextField txtBusca;
    private JButton btnBuscar, btnLimparBusca;
    private JLabel lblTotal, lblAdmin, lblUser;
    private JLabel lblUsuarioLogado;
    
    /**
     * Construtor da tela principal.
     * 
     * @param usuarioLogado Usuario que está logado no sistema
     */
    public MainView(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
        this.controller = new UsuarioController();
        inicializarComponentes();
        carregarUsuarios();
        atualizarEstatisticas();
    }
    
    /**
     * Inicializa e configura todos os componentes da interface.
     */
    private void inicializarComponentes() {
        // Configurações da janela
        setTitle("Sistema de Gerenciamento de Usuários");
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Painel principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        mainPanel.setBackground(new Color(245, 245, 245));
        
        // ========== PAINEL SUPERIOR ==========
        JPanel topPanel = criarPainelSuperior();
        
        // ========== PAINEL DE BUSCA ==========
        JPanel searchPanel = criarPainelBusca();
        
        // ========== PAINEL DA TABELA ==========
        JPanel tablePanel = criarPainelTabela();
        
        // ========== PAINEL DE BOTÕES ==========
        JPanel buttonPanel = criarPainelBotoes();
        
        // ========== PAINEL DE ESTATÍSTICAS ==========
        JPanel statsPanel = criarPainelEstatisticas();
        
        // Organiza os painéis
        JPanel northPanel = new JPanel(new BorderLayout(5, 5));
        northPanel.setBackground(new Color(245, 245, 245));
        northPanel.add(topPanel, BorderLayout.NORTH);
        northPanel.add(searchPanel, BorderLayout.CENTER);
        
        mainPanel.add(northPanel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);
        
        JPanel southPanel = new JPanel(new BorderLayout(5, 5));
        southPanel.setBackground(new Color(245, 245, 245));
        southPanel.add(buttonPanel, BorderLayout.CENTER);
        southPanel.add(statsPanel, BorderLayout.SOUTH);
        
        mainPanel.add(southPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    /**
     * Cria o painel superior com informações do usuário logado.
     */
    private JPanel criarPainelSuperior() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(66, 139, 202));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
        // Informações do usuário logado
        String icone = usuarioLogado.isAdmin() ? "👤⚙️" : "👤";
        lblUsuarioLogado = new JLabel(icone + " Logado como: " + usuarioLogado.getNome() + 
                                       " (" + usuarioLogado.getPerfil().getDescricao() + ")");
        lblUsuarioLogado.setFont(new Font("Arial", Font.BOLD, 16));
        lblUsuarioLogado.setForeground(Color.WHITE);
        
        // Botão Sair
        btnSair = new JButton("Sair");
        btnSair.setFont(new Font("Arial", Font.BOLD, 13));
        btnSair.setBackground(new Color(217, 83, 79));
        btnSair.setForeground(Color.WHITE);
        btnSair.setFocusPainted(false);
        btnSair.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnSair.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btnSair.addActionListener(e -> sair());
        
        // Efeito hover
        btnSair.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSair.setBackground(new Color(192, 57, 43));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSair.setBackground(new Color(217, 83, 79));
            }
        });
        
        panel.add(lblUsuarioLogado, BorderLayout.WEST);
        panel.add(btnSair, BorderLayout.EAST);
        
        return panel;
    }
    
    /**
     * Cria o painel de busca.
     */
    private JPanel criarPainelBusca() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        
        JLabel lblBusca = new JLabel("🔍 Buscar:");
        lblBusca.setFont(new Font("Arial", Font.BOLD, 13));
        
        txtBusca = new JTextField(30);
        txtBusca.setFont(new Font("Arial", Font.PLAIN, 13));
        txtBusca.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        btnBuscar = criarBotaoPequeno("Buscar", new Color(92, 184, 92));
        btnBuscar.addActionListener(e -> buscarUsuarios());
        
        btnLimparBusca = criarBotaoPequeno("Limpar", new Color(150, 150, 150));
        btnLimparBusca.addActionListener(e -> {
            txtBusca.setText("");
            carregarUsuarios();
        });
        
        // Enter para buscar
        txtBusca.addActionListener(e -> buscarUsuarios());
        
        panel.add(lblBusca);
        panel.add(txtBusca);
        panel.add(btnBuscar);
        panel.add(btnLimparBusca);
        
        return panel;
    }
    
    /**
     * Cria o painel com a tabela de usuários.
     */
    private JPanel criarPainelTabela() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
        
        // Título da tabela
        JLabel lblTitulo = new JLabel("  📋 Lista de Usuários Cadastrados");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 14));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        lblTitulo.setBackground(new Color(240, 240, 240));
        lblTitulo.setOpaque(true);
        
        // Colunas da tabela
        String[] colunas = {"ID", "Nome", "Email", "Telefone", "Perfil", "Cidade", "Estado", "País"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        // Configurações da tabela
        tabelaUsuarios = new JTable(tableModel);
        tabelaUsuarios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabelaUsuarios.setRowHeight(30);
        tabelaUsuarios.setFont(new Font("Arial", Font.PLAIN, 13));
        tabelaUsuarios.setSelectionBackground(new Color(184, 207, 229));
        tabelaUsuarios.setSelectionForeground(Color.BLACK);
        tabelaUsuarios.setGridColor(new Color(220, 220, 220));
        
        // Configuração do cabeçalho
        JTableHeader header = tabelaUsuarios.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 13));
        header.setBackground(new Color(66, 139, 202));
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);
        
        // Largura das colunas
        tabelaUsuarios.getColumnModel().getColumn(0).setPreferredWidth(50);   // ID
        tabelaUsuarios.getColumnModel().getColumn(1).setPreferredWidth(150);  // Nome
        tabelaUsuarios.getColumnModel().getColumn(2).setPreferredWidth(200);  // Email
        tabelaUsuarios.getColumnModel().getColumn(3).setPreferredWidth(130);  // Telefone
        tabelaUsuarios.getColumnModel().getColumn(4).setPreferredWidth(80);   // Perfil
        tabelaUsuarios.getColumnModel().getColumn(5).setPreferredWidth(120);  // Cidade
        tabelaUsuarios.getColumnModel().getColumn(6).setPreferredWidth(100);  // Estado
        tabelaUsuarios.getColumnModel().getColumn(7).setPreferredWidth(100);  // País
        
        // Centraliza algumas colunas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        tabelaUsuarios.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // ID
        tabelaUsuarios.getColumnModel().getColumn(4).setCellRenderer(centerRenderer); // Perfil
        
        // Renderizador customizado para perfil
        tabelaUsuarios.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                         boolean isSelected, boolean hasFocus,
                                                         int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(JLabel.CENTER);
                
                if (!isSelected) {
                    if (value != null && value.toString().equals("admin")) {
                        setBackground(new Color(255, 235, 205));
                        setForeground(new Color(204, 102, 0));
                        setFont(new Font("Arial", Font.BOLD, 12));
                    } else {
                        setBackground(Color.WHITE);
                        setForeground(Color.BLACK);
                        setFont(new Font("Arial", Font.PLAIN, 12));
                    }
                }
                
                return c;
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(tabelaUsuarios);
        scrollPane.setBorder(null);
        
        panel.add(lblTitulo, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    /**
     * Cria o painel com os botões de ação.
     */
    private JPanel criarPainelBotoes() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 15));
        panel.setBackground(new Color(245, 245, 245));
        
        btnNovo = criarBotao("➕ Novo Usuário", new Color(92, 184, 92));
        btnEditar = criarBotao("✏️ Editar", new Color(240, 173, 78));
        btnExcluir = criarBotao("🗑️ Excluir", new Color(217, 83, 79));
        btnAtualizar = criarBotao("🔄 Atualizar Lista", new Color(66, 139, 202));
        
        btnNovo.addActionListener(e -> novoUsuario());
        btnEditar.addActionListener(e -> editarUsuario());
        btnExcluir.addActionListener(e -> excluirUsuario());
        btnAtualizar.addActionListener(e -> {
            carregarUsuarios();
            atualizarEstatisticas();
        });
        
        // Desabilita exclusão se não for admin
        if (!usuarioLogado.isAdmin()) {
            btnExcluir.setEnabled(false);
            btnExcluir.setToolTipText("Apenas administradores podem excluir usuários");
        }
        
        panel.add(btnNovo);
        panel.add(btnEditar);
        panel.add(btnExcluir);
        panel.add(btnAtualizar);
        
        return panel;
    }
    
    /**
     * Cria o painel de estatísticas.
     */
    private JPanel criarPainelEstatisticas() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        panel.setBackground(new Color(240, 240, 240));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        
        lblTotal = criarLabelEstatistica("📊 Total: 0", new Color(66, 139, 202));
        lblAdmin = criarLabelEstatistica("⚙️ Admins: 0", new Color(240, 173, 78));
        lblUser = criarLabelEstatistica("👤 Users: 0", new Color(92, 184, 92));
        
        panel.add(lblTotal);
        panel.add(new JSeparator(SwingConstants.VERTICAL));
        panel.add(lblAdmin);
        panel.add(new JSeparator(SwingConstants.VERTICAL));
        panel.add(lblUser);
        
        return panel;
    }
    
    /**
     * Cria um botão padrão.
     */
    private JButton criarBotao(String texto, Color cor) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("Arial", Font.BOLD, 13));
        botao.setBackground(cor);
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botao.setPreferredSize(new Dimension(160, 38));
        botao.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        
        // Efeito hover
        Color corEscura = cor.darker();
        botao.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (botao.isEnabled()) {
                    botao.setBackground(corEscura);
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                botao.setBackground(cor);
            }
        });
        
        return botao;
    }
    
    /**
     * Cria um botão pequeno.
     */
    private JButton criarBotaoPequeno(String texto, Color cor) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("Arial", Font.BOLD, 12));
        botao.setBackground(cor);
        botao.setForeground(Color.WHITE);
        botao.setFocusPainted(false);
        botao.setCursor(new Cursor(Cursor.HAND_CURSOR));
        botao.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 15));
        
        return botao;
    }
    
    /**
     * Cria um label de estatística.
     */
    private JLabel criarLabelEstatistica(String texto, Color cor) {
        JLabel label = new JLabel(texto);
        label.setFont(new Font("Arial", Font.BOLD, 13));
        label.setForeground(cor);
        return label;
    }
    
    /**
     * Carrega todos os usuários na tabela.
     */
    public void carregarUsuarios() {
        tableModel.setRowCount(0);
        List<Usuario> usuarios = controller.listarTodos();
        
        for (Usuario usuario : usuarios) {
            Object[] row = {
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTelefone() != null ? usuario.getTelefone() : "-",
                usuario.getPerfil().name(),
                usuario.getCidade() != null ? usuario.getCidade() : "-",
                usuario.getEstado() != null ? usuario.getEstado() : "-",
                usuario.getPais() != null ? usuario.getPais() : "-"
            };
            tableModel.addRow(row);
        }
    }
    
    /**
     * Busca usuários por nome.
     */
    private void buscarUsuarios() {
        String termo = txtBusca.getText().trim();
        
        if (termo.isEmpty()) {
            carregarUsuarios();
            return;
        }
        
        tableModel.setRowCount(0);
        List<Usuario> usuarios = controller.buscarPorNome(termo);
        
        for (Usuario usuario : usuarios) {
            Object[] row = {
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTelefone() != null ? usuario.getTelefone() : "-",
                usuario.getPerfil().name(),
                usuario.getCidade() != null ? usuario.getCidade() : "-",
                usuario.getEstado() != null ? usuario.getEstado() : "-",
                usuario.getPais() != null ? usuario.getPais() : "-"
            };
            tableModel.addRow(row);
        }
        
        if (usuarios.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Nenhum usuário encontrado com o termo: " + termo,
                "Busca", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    /**
     * Atualiza as estatísticas exibidas.
     */
    void atualizarEstatisticas() {
        int total = controller.contarUsuariosAtivos();
        int admins = controller.contarPorPerfil(Usuario.Perfil.admin);
        int users = controller.contarPorPerfil(Usuario.Perfil.user);
        
        lblTotal.setText("📊 Total: " + total);
        lblAdmin.setText("⚙️ Admins: " + admins);
        lblUser.setText("👤 Users: " + users);
    }
    
    /**
     * Abre o formulário para criar novo usuário.
     */
    private void novoUsuario() {
        UsuarioFormView form = new UsuarioFormView(this, null, usuarioLogado);
        form.setVisible(true);
    }
    
    /**
     * Abre o formulário para editar usuário selecionado.
     */
    private void editarUsuario() {
        int selectedRow = tabelaUsuarios.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Selecione um usuário na tabela para editar!",
                "Aviso", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int userId = (int) tableModel.getValueAt(selectedRow, 0);
        Usuario usuario = controller.buscarPorId(userId);
        
        if (usuario != null) {
            UsuarioFormView form = new UsuarioFormView(this, usuario, usuarioLogado);
            form.setVisible(true);
        }
    }
    
    /**
     * Exclui o usuário selecionado.
     */
    private void excluirUsuario() {
        int selectedRow = tabelaUsuarios.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Selecione um usuário na tabela para excluir!",
                "Aviso", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int userId = (int) tableModel.getValueAt(selectedRow, 0);
        
        // Não permite excluir o próprio usuário
        if (userId == usuarioLogado.getId()) {
            JOptionPane.showMessageDialog(this, 
                "Você não pode excluir seu próprio usuário!",
                "Erro", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (controller.excluir(userId)) {
            carregarUsuarios();
            atualizarEstatisticas();
        }
    }
    
    /**
     * Faz logout e retorna para tela de login.
     */
    private void sair() {
        int confirmacao = JOptionPane.showConfirmDialog(
            this,
            "Deseja realmente sair do sistema?",
            "Confirmar Saída",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (confirmacao == JOptionPane.YES_OPTION) {
            System.out.println("✓ Logout realizado: " + usuarioLogado.getEmail());
            new LoginView().setVisible(true);
            dispose();
        }
    }
}