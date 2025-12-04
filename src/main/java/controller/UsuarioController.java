package controller;

import dao.UsuarioDAO;
import model.Usuario;
import model.Usuario.Perfil;
import util.PasswordUtil;

import javax.swing.*;
import java.sql.SQLException;
import java.util.List;

/**
 * Controller responsável pela lógica de negócio relacionada aos usuários.
 * Faz a intermediação entre a camada View e a camada DAO.
 * 
 * @author Emerson Meneses Inocente <emerson_inocente@hotmail.com>
 * @version 1.0
 */
public class UsuarioController {
    
    private final UsuarioDAO usuarioDAO;
    
    /**
     * Construtor do controller.
     * Inicializa o DAO de usuários.
     */
    public UsuarioController() {
        this.usuarioDAO = new UsuarioDAO();
    }
    
    // ==================== MÉTODOS DE AUTENTICAÇÃO ====================
    
    /**
     * Autentica um usuário no sistema.
     * 
     * @param email Email do usuário
     * @param senha Senha em texto plano
     * @return Usuario autenticado ou null se falhar
     */
    public Usuario autenticar(String email, String senha) {
        try {
            // Validações básicas
            if (email == null || email.trim().isEmpty()) {
                exibirErro("Email é obrigatório!");
                return null;
            }
            
            if (senha == null || senha.trim().isEmpty()) {
                exibirErro("Senha é obrigatória!");
                return null;
            }
            
            // Tenta autenticar
            Usuario usuario = usuarioDAO.autenticar(email.trim(), senha);
            
            if (usuario == null) {
                exibirErro("Email ou senha inválidos!");
                return null;
            }
            
            // Sucesso
            System.out.println("✓ Login bem-sucedido: " + usuario.getNome());
            return usuario;
            
        } catch (SQLException e) {
            exibirErro("Erro ao autenticar usuário: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    // ==================== MÉTODOS DE CONSULTA ====================
    
    /**
     * Lista todos os usuários ativos do sistema.
     * 
     * @return Lista de usuários ou lista vazia em caso de erro
     */
    public List<Usuario> listarTodos() {
        try {
            return usuarioDAO.listarTodos();
        } catch (SQLException e) {
            exibirErro("Erro ao listar usuários: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }
    
    /**
     * Lista todos os usuários incluindo inativos.
     * 
     * @return Lista completa de usuários
     */
    public List<Usuario> listarTodosIncluindoInativos() {
        try {
            return usuarioDAO.listarTodosIncluindoInativos();
        } catch (SQLException e) {
            exibirErro("Erro ao listar usuários: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }
    
    /**
     * Busca um usuário por ID.
     * 
     * @param id ID do usuário
     * @return Usuario encontrado ou null
     */
    public Usuario buscarPorId(int id) {
        try {
            return usuarioDAO.buscarPorId(id);
        } catch (SQLException e) {
            exibirErro("Erro ao buscar usuário: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Busca um usuário por email.
     * 
     * @param email Email do usuário
     * @return Usuario encontrado ou null
     */
    public Usuario buscarPorEmail(String email) {
        try {
            return usuarioDAO.buscarPorEmail(email);
        } catch (SQLException e) {
            exibirErro("Erro ao buscar usuário: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Busca usuários por nome (busca parcial).
     * 
     * @param nome Nome ou parte do nome
     * @return Lista de usuários encontrados
     */
    public List<Usuario> buscarPorNome(String nome) {
        try {
            return usuarioDAO.buscarPorNome(nome);
        } catch (SQLException e) {
            exibirErro("Erro ao buscar usuários: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }
    
    /**
     * Lista usuários por perfil.
     * 
     * @param perfil Perfil desejado
     * @return Lista de usuários com o perfil
     */
    public List<Usuario> listarPorPerfil(Perfil perfil) {
        try {
            return usuarioDAO.listarPorPerfil(perfil);
        } catch (SQLException e) {
            exibirErro("Erro ao listar usuários por perfil: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }
    
    // ==================== MÉTODOS DE CADASTRO ====================
    
    /**
     * Cadastra um novo usuário no sistema.
     * Realiza todas as validações necessárias.
     * 
     * @param usuario Usuario a ser cadastrado
     * @return true se cadastro foi bem-sucedido, false caso contrário
     */
    public boolean cadastrar(Usuario usuario) {
        try {
            // Validações
            if (!validarUsuario(usuario)) {
                return false;
            }
            
            // Valida senha
            if (!validarSenha(usuario.getSenha())) {
                return false;
            }
            
            // Verifica se email já existe
            if (usuarioDAO.emailExiste(usuario.getEmail())) {
                exibirErro("Este email já está cadastrado!");
                return false;
            }
            
            // Insere no banco (senha será criptografada automaticamente no DAO)
            boolean sucesso = usuarioDAO.inserir(usuario);
            
            if (sucesso) {
                exibirSucesso("Usuário cadastrado com sucesso!");
                System.out.println("✓ Usuário cadastrado: " + usuario.getEmail());
            }
            
            return sucesso;
            
        } catch (SQLException e) {
            exibirErro("Erro ao cadastrar usuário: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // ==================== MÉTODOS DE ATUALIZAÇÃO ====================
    
    /**
     * Atualiza os dados de um usuário existente.
     * Não atualiza a senha (use alterarSenha para isso).
     * 
     * @param usuario Usuario com dados atualizados
     * @return true se atualização foi bem-sucedida, false caso contrário
     */
    public boolean atualizar(Usuario usuario) {
        try {
            // Validações
            if (!validarUsuario(usuario)) {
                return false;
            }
            
            // Verifica se email já existe para outro usuário
            if (usuarioDAO.emailExisteParaOutroUsuario(usuario.getEmail(), usuario.getId())) {
                exibirErro("Este email já está sendo usado por outro usuário!");
                return false;
            }
            
            // Atualiza no banco
            boolean sucesso = usuarioDAO.atualizar(usuario);
            
            if (sucesso) {
                exibirSucesso("Usuário atualizado com sucesso!");
                System.out.println("✓ Usuário atualizado: " + usuario.getEmail());
            }
            
            return sucesso;
            
        } catch (SQLException e) {
            exibirErro("Erro ao atualizar usuário: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Altera a senha de um usuário.
     * Valida a senha atual antes de alterar.
     * 
     * @param id ID do usuário
     * @param senhaAtual Senha atual do usuário
     * @param novaSenha Nova senha
     * @param confirmacaoSenha Confirmação da nova senha
     * @return true se alteração foi bem-sucedida, false caso contrário
     */
    public boolean alterarSenha(int id, String senhaAtual, String novaSenha, String confirmacaoSenha) {
        try {
            // Busca usuário
            Usuario usuario = usuarioDAO.buscarPorId(id);
            
            if (usuario == null) {
                exibirErro("Usuário não encontrado!");
                return false;
            }
            
            // Valida senha atual
            if (!PasswordUtil.verificarSenha(senhaAtual, usuario.getSenha())) {
                exibirErro("Senha atual incorreta!");
                return false;
            }
            
            // Valida nova senha
            if (novaSenha == null || novaSenha.trim().isEmpty()) {
                exibirErro("Nova senha não pode ser vazia!");
                return false;
            }
            
            if (!validarSenha(novaSenha)) {
                return false;
            }
            
            // Verifica confirmação
            if (!novaSenha.equals(confirmacaoSenha)) {
                exibirErro("A nova senha e a confirmação não coincidem!");
                return false;
            }
            
            // Verifica se a nova senha é diferente da atual
            if (PasswordUtil.verificarSenha(novaSenha, usuario.getSenha())) {
                exibirErro("A nova senha deve ser diferente da senha atual!");
                return false;
            }
            
            // Atualiza senha (será criptografada automaticamente no DAO)
            boolean sucesso = usuarioDAO.atualizarSenha(id, novaSenha);
            
            if (sucesso) {
                exibirSucesso("Senha alterada com sucesso!");
                System.out.println("✓ Senha alterada para usuário ID: " + id);
            }
            
            return sucesso;
            
        } catch (SQLException e) {
            exibirErro("Erro ao alterar senha: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Atualiza o perfil de um usuário.
     * 
     * @param id ID do usuário
     * @param perfil Novo perfil
     * @return true se atualização foi bem-sucedida, false caso contrário
     */
    public boolean atualizarPerfil(int id, Perfil perfil) {
        try {
            boolean sucesso = usuarioDAO.atualizarPerfil(id, perfil);
            
            if (sucesso) {
                exibirSucesso("Perfil atualizado com sucesso!");
                System.out.println("✓ Perfil atualizado para: " + perfil);
            }
            
            return sucesso;
            
        } catch (SQLException e) {
            exibirErro("Erro ao atualizar perfil: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // ==================== MÉTODOS DE EXCLUSÃO ====================
    
    /**
     * Exclui (desativa) um usuário do sistema.
     * 
     * @param id ID do usuário
     * @return true se exclusão foi bem-sucedida, false caso contrário
     */
    public boolean excluir(int id) {
        try {
            int confirmacao = JOptionPane.showConfirmDialog(
                null,
                "Tem certeza que deseja excluir este usuário?\nEsta ação pode ser revertida posteriormente.",
                "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );
            
            if (confirmacao == JOptionPane.YES_OPTION) {
                boolean sucesso = usuarioDAO.excluir(id);
                
                if (sucesso) {
                    exibirSucesso("Usuário excluído com sucesso!");
                    System.out.println("✓ Usuário ID " + id + " foi desativado");
                }
                
                return sucesso;
            }
            
            return false;
            
        } catch (SQLException e) {
            exibirErro("Erro ao excluir usuário: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Reativa um usuário previamente desativado.
     * 
     * @param id ID do usuário
     * @return true se reativação foi bem-sucedida, false caso contrário
     */
    public boolean reativar(int id) {
        try {
            boolean sucesso = usuarioDAO.reativar(id);
            
            if (sucesso) {
                exibirSucesso("Usuário reativado com sucesso!");
                System.out.println("✓ Usuário ID " + id + " foi reativado");
            }
            
            return sucesso;
            
        } catch (SQLException e) {
            exibirErro("Erro ao reativar usuário: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    /**
     * Exclui permanentemente um usuário.
     * CUIDADO: Esta ação é irreversível!
     * 
     * @param id ID do usuário
     * @return true se exclusão foi bem-sucedida, false caso contrário
     */
    public boolean excluirPermanentemente(int id) {
        try {
            int confirmacao = JOptionPane.showConfirmDialog(
                null,
                "ATENÇÃO: Esta ação é IRREVERSÍVEL!\n\n" +
                "O usuário será excluído PERMANENTEMENTE do banco de dados.\n" +
                "Todos os seus dados serão perdidos.\n\n" +
                "Deseja realmente continuar?",
                "Confirmar Exclusão Permanente",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.ERROR_MESSAGE
            );
            
            if (confirmacao == JOptionPane.YES_OPTION) {
                boolean sucesso = usuarioDAO.excluirPermanentemente(id);
                
                if (sucesso) {
                    exibirAviso("Usuário excluído PERMANENTEMENTE!");
                    System.out.println("✓ Usuário ID " + id + " foi excluído permanentemente");
                }
                
                return sucesso;
            }
            
            return false;
            
        } catch (SQLException e) {
            exibirErro("Erro ao excluir usuário permanentemente: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    // ==================== MÉTODOS ESTATÍSTICOS ====================
    
    /**
     * Retorna o total de usuários ativos.
     * 
     * @return Quantidade de usuários ativos
     */
    public int contarUsuariosAtivos() {
        try {
            return usuarioDAO.contarUsuariosAtivos();
        } catch (SQLException e) {
            exibirErro("Erro ao contar usuários: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }
    
    /**
     * Retorna o total de usuários por perfil.
     * 
     * @param perfil Perfil a contar
     * @return Quantidade de usuários
     */
    public int contarPorPerfil(Perfil perfil) {
        try {
            return usuarioDAO.contarPorPerfil(perfil);
        } catch (SQLException e) {
            exibirErro("Erro ao contar usuários por perfil: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }
    
    // ==================== MÉTODOS DE VALIDAÇÃO ====================
    
    /**
     * Valida os dados básicos de um usuário.
     * 
     * @param usuario Usuario a ser validado
     * @return true se válido, false caso contrário
     */
    private boolean validarUsuario(Usuario usuario) {
        if (usuario == null) {
            exibirErro("Dados do usuário inválidos!");
            return false;
        }
        
        // Valida nome
        if (usuario.getNome() == null || usuario.getNome().trim().isEmpty()) {
            exibirErro("Nome é obrigatório!");
            return false;
        }
        
        if (usuario.getNome().trim().length() < 3) {
            exibirErro("Nome deve ter no mínimo 3 caracteres!");
            return false;
        }
        
        // Valida email
        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            exibirErro("Email é obrigatório!");
            return false;
        }
        
        if (!usuario.isEmailValido()) {
            exibirErro("Email inválido! Use o formato: exemplo@dominio.com");
            return false;
        }
        
        // Valida telefone (se fornecido)
        if (usuario.getTelefone() != null && !usuario.getTelefone().trim().isEmpty()) {
            if (!validarTelefone(usuario.getTelefone())) {
                exibirErro("Telefone inválido! Use o formato internacional: +55 51 99999-9999");
                return false;
            }
        }
        
        // Valida perfil
        if (usuario.getPerfil() == null) {
            exibirErro("Perfil é obrigatório!");
            return false;
        }
        
        return true;
    }
    
    /**
     * Valida uma senha.
     * 
     * @param senha Senha a ser validada
     * @return true se válida, false caso contrário
     */
    private boolean validarSenha(String senha) {
        if (senha == null || senha.isEmpty()) {
            exibirErro("Senha é obrigatória!");
            return false;
        }
        
        if (senha.length() < 6) {
            exibirErro("Senha deve ter no mínimo 6 caracteres!");
            return false;
        }
        
        if (senha.length() > 50) {
            exibirErro("Senha deve ter no máximo 50 caracteres!");
            return false;
        }
        
        // Avalia força da senha
        String forcaSenha = PasswordUtil.avaliarForcaSenha(senha);
        if (forcaSenha.equals("Senha muito fraca (mínimo 6 caracteres)")) {
            exibirErro(forcaSenha);
            return false;
        }
        
        return true;
    }
    
    /**
     * Valida formato de telefone internacional.
     * 
     * @param telefone Telefone a ser validado
     * @return true se válido, false caso contrário
     */
    private boolean validarTelefone(String telefone) {
        if (telefone == null || telefone.trim().isEmpty()) {
            return true; // Telefone é opcional
        }
        
        // Remove espaços para validação
        String tel = telefone.replaceAll("\\s", "");
        
        // Formato básico: deve começar com + e ter números
        return tel.matches("^\\+\\d{1,3}\\s?\\d{2,3}\\s?\\d{4,5}-?\\d{4}$") ||
               tel.matches("^\\+\\d{10,15}$");
    }
    
    // ==================== MÉTODOS DE INTERFACE ====================
    
    /**
     * Exibe mensagem de erro.
     * 
     * @param mensagem Mensagem a ser exibida
     */
    private void exibirErro(String mensagem) {
        JOptionPane.showMessageDialog(
            null,
            mensagem,
            "Erro",
            JOptionPane.ERROR_MESSAGE
        );
    }
    
    /**
     * Exibe mensagem de sucesso.
     * 
     * @param mensagem Mensagem a ser exibida
     */
    private void exibirSucesso(String mensagem) {
        JOptionPane.showMessageDialog(
            null,
            mensagem,
            "Sucesso",
            JOptionPane.INFORMATION_MESSAGE
        );
    }
    
    /**
     * Exibe mensagem de aviso.
     * 
     * @param mensagem Mensagem a ser exibida
     */
    private void exibirAviso(String mensagem) {
        JOptionPane.showMessageDialog(
            null,
            mensagem,
            "Aviso",
            JOptionPane.WARNING_MESSAGE
        );
    }
}