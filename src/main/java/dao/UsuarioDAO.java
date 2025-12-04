package dao;

import config.DatabaseConfig;
import model.Usuario;
import model.Usuario.Perfil;
import util.PasswordUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) para a entidade Usuario.
 * Responsável por todas as operações de banco de dados relacionadas a usuários.
 * 
 * @author Emerson Meneses Inocente <emerson_inocente@hotmail.com>
 * @version 1.0
 */
public class UsuarioDAO {
    
    // ==================== MÉTODOS DE AUTENTICAÇÃO ====================
    
    /**
     * Autentica um usuário no sistema usando BCrypt.
     * 
     * @param email Email do usuário
     * @param senha Senha do usuário em texto plano
     * @return Usuario autenticado ou null se credenciais inválidas
     * @throws SQLException Se houver erro na consulta ao banco
     */
    public Usuario autenticar(String email, String senha) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE email = ? AND ativo = TRUE";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String senhaArmazenada = rs.getString("senha");
                    
                    // Verifica se a senha armazenada é BCrypt ou texto plano
                    boolean senhaValida;
                    if (PasswordUtil.isSenhaCriptografada(senhaArmazenada)) {
                        // Senha já está criptografada, usa BCrypt
                        senhaValida = PasswordUtil.verificarSenha(senha, senhaArmazenada);
                    } else {
                        // Senha em texto plano (legado), compara diretamente
                        senhaValida = senha.equals(senhaArmazenada);
                        
                        // Se senha está correta, aproveita para criptografar
                        if (senhaValida) {
                            int userId = rs.getInt("id");
                            String senhaCriptografada = PasswordUtil.criptografarSenha(senha);
                            atualizarSenha(userId, senhaCriptografada);
                            System.out.println("✓ Senha migrada para BCrypt para usuário: " + email);
                        }
                    }
                    
                    if (senhaValida) {
                        return extrairUsuarioDoResultSet(rs);
                    }
                }
            }
        }
        return null;
    }
    
    // ==================== MÉTODOS DE CONSULTA ====================
    
    /**
     * Lista todos os usuários ativos do sistema.
     * 
     * @return Lista de usuários ativos
     * @throws SQLException Se houver erro na consulta ao banco
     */
    public List<Usuario> listarTodos() throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios WHERE ativo = TRUE ORDER BY nome";
        
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                usuarios.add(extrairUsuarioDoResultSet(rs));
            }
        }
        return usuarios;
    }
    
    /**
     * Lista todos os usuários (ativos e inativos).
     * 
     * @return Lista completa de usuários
     * @throws SQLException Se houver erro na consulta ao banco
     */
    public List<Usuario> listarTodosIncluindoInativos() throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios ORDER BY nome";
        
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                usuarios.add(extrairUsuarioDoResultSet(rs));
            }
        }
        return usuarios;
    }
    
    /**
     * Busca um usuário por ID.
     * 
     * @param id ID do usuário
     * @return Usuario encontrado ou null se não existir
     * @throws SQLException Se houver erro na consulta ao banco
     */
    public Usuario buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE id = ? AND ativo = TRUE";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extrairUsuarioDoResultSet(rs);
                }
            }
        }
        return null;
    }
    
    /**
     * Busca um usuário por email.
     * 
     * @param email Email do usuário
     * @return Usuario encontrado ou null se não existir
     * @throws SQLException Se houver erro na consulta ao banco
     */
    public Usuario buscarPorEmail(String email) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE email = ? AND ativo = TRUE";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return extrairUsuarioDoResultSet(rs);
                }
            }
        }
        return null;
    }
    
    /**
     * Lista usuários por perfil.
     * 
     * @param perfil Perfil desejado (user ou admin)
     * @return Lista de usuários com o perfil especificado
     * @throws SQLException Se houver erro na consulta ao banco
     */
    public List<Usuario> listarPorPerfil(Perfil perfil) throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios WHERE perfil = ? AND ativo = TRUE ORDER BY nome";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, perfil.name());
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    usuarios.add(extrairUsuarioDoResultSet(rs));
                }
            }
        }
        return usuarios;
    }
    
    /**
     * Busca usuários por nome (busca parcial).
     * 
     * @param nome Nome ou parte do nome para buscar
     * @return Lista de usuários encontrados
     * @throws SQLException Se houver erro na consulta ao banco
     */
    public List<Usuario> buscarPorNome(String nome) throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        String sql = "SELECT * FROM usuarios WHERE nome LIKE ? AND ativo = TRUE ORDER BY nome";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + nome + "%");
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    usuarios.add(extrairUsuarioDoResultSet(rs));
                }
            }
        }
        return usuarios;
    }
    
    // ==================== MÉTODOS DE INSERÇÃO ====================
    
    /**
     * Insere um novo usuário no banco de dados.
     * A senha será automaticamente criptografada com BCrypt.
     * 
     * @param usuario Usuario a ser inserido
     * @return true se inserção foi bem-sucedida, false caso contrário
     * @throws SQLException Se houver erro na inserção
     */
    public boolean inserir(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuarios (nome, email, senha, telefone, perfil, endereco, cidade, estado, pais, codigo_postal) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            // Criptografa a senha antes de inserir
            String senhaOriginal = usuario.getSenha();
            if (!PasswordUtil.isSenhaCriptografada(senhaOriginal)) {
                usuario.setSenha(PasswordUtil.criptografarSenha(senhaOriginal));
            }
            
            preencherStatementParaInsercao(stmt, usuario);
            
            int linhasAfetadas = stmt.executeUpdate();
            
            if (linhasAfetadas > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        usuario.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }
    
    // ==================== MÉTODOS DE ATUALIZAÇÃO ====================
    
    /**
     * Atualiza os dados de um usuário existente.
     * Não atualiza a senha (use atualizarSenha para isso).
     * 
     * @param usuario Usuario com dados atualizados
     * @return true se atualização foi bem-sucedida, false caso contrário
     * @throws SQLException Se houver erro na atualização
     */
    public boolean atualizar(Usuario usuario) throws SQLException {
        String sql = "UPDATE usuarios SET nome = ?, email = ?, telefone = ?, perfil = ?, " +
                     "endereco = ?, cidade = ?, estado = ?, pais = ?, codigo_postal = ? " +
                     "WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getTelefone());
            stmt.setString(4, usuario.getPerfil().name());
            stmt.setString(5, usuario.getEndereco());
            stmt.setString(6, usuario.getCidade());
            stmt.setString(7, usuario.getEstado());
            stmt.setString(8, usuario.getPais());
            stmt.setString(9, usuario.getCodigoPostal());
            stmt.setInt(10, usuario.getId());
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    /**
     * Atualiza a senha de um usuário.
     * A senha será automaticamente criptografada com BCrypt.
     * 
     * @param id ID do usuário
     * @param novaSenha Nova senha em texto plano
     * @return true se atualização foi bem-sucedida, false caso contrário
     * @throws SQLException Se houver erro na atualização
     */
    public boolean atualizarSenha(int id, String novaSenha) throws SQLException {
        String sql = "UPDATE usuarios SET senha = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // Criptografa a senha antes de atualizar
            String senhaCriptografada = PasswordUtil.isSenhaCriptografada(novaSenha) 
                                        ? novaSenha 
                                        : PasswordUtil.criptografarSenha(novaSenha);
            
            stmt.setString(1, senhaCriptografada);
            stmt.setInt(2, id);
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    /**
     * Atualiza o perfil de um usuário.
     * 
     * @param id ID do usuário
     * @param perfil Novo perfil
     * @return true se atualização foi bem-sucedida, false caso contrário
     * @throws SQLException Se houver erro na atualização
     */
    public boolean atualizarPerfil(int id, Perfil perfil) throws SQLException {
        String sql = "UPDATE usuarios SET perfil = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, perfil.name());
            stmt.setInt(2, id);
            
            return stmt.executeUpdate() > 0;
        }
    }
    
    // ==================== MÉTODOS DE EXCLUSÃO ====================
    
    /**
     * Desativa um usuário (exclusão lógica).
     * O usuário não é removido fisicamente do banco.
     * 
     * @param id ID do usuário
     * @return true se desativação foi bem-sucedida, false caso contrário
     * @throws SQLException Se houver erro na operação
     */
    public boolean excluir(int id) throws SQLException {
        String sql = "UPDATE usuarios SET ativo = FALSE WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
    
    /**
     * Reativa um usuário previamente desativado.
     * 
     * @param id ID do usuário
     * @return true se reativação foi bem-sucedida, false caso contrário
     * @throws SQLException Se houver erro na operação
     */
    public boolean reativar(int id) throws SQLException {
        String sql = "UPDATE usuarios SET ativo = TRUE WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
    
    /**
     * Exclui permanentemente um usuário do banco de dados.
     * CUIDADO: Esta ação é irreversível!
     * 
     * @param id ID do usuário
     * @return true se exclusão foi bem-sucedida, false caso contrário
     * @throws SQLException Se houver erro na operação
     */
    public boolean excluirPermanentemente(int id) throws SQLException {
        String sql = "DELETE FROM usuarios WHERE id = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        }
    }
    
    // ==================== MÉTODOS DE VALIDAÇÃO ====================
    
    /**
     * Verifica se um email já está cadastrado no sistema.
     * 
     * @param email Email a ser verificado
     * @return true se o email já existe, false caso contrário
     * @throws SQLException Se houver erro na consulta
     */
    public boolean emailExiste(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE email = ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    
    /**
     * Verifica se um email já existe, excluindo um usuário específico da verificação.
     * Útil para validar email na edição.
     * 
     * @param email Email a ser verificado
     * @param idUsuarioAtual ID do usuário atual (para excluir da verificação)
     * @return true se o email existe para outro usuário, false caso contrário
     * @throws SQLException Se houver erro na consulta
     */
    public boolean emailExisteParaOutroUsuario(String email, int idUsuarioAtual) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE email = ? AND id != ?";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            stmt.setInt(2, idUsuarioAtual);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }
    
    // ==================== MÉTODOS ESTATÍSTICOS ====================
    
    /**
     * Conta o total de usuários ativos no sistema.
     * 
     * @return Quantidade de usuários ativos
     * @throws SQLException Se houver erro na consulta
     */
    public int contarUsuariosAtivos() throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE ativo = TRUE";
        
        try (Connection conn = DatabaseConfig.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }
    
    /**
     * Conta usuários por perfil.
     * 
     * @param perfil Perfil a contar
     * @return Quantidade de usuários com o perfil especificado
     * @throws SQLException Se houver erro na consulta
     */
    public int contarPorPerfil(Perfil perfil) throws SQLException {
        String sql = "SELECT COUNT(*) FROM usuarios WHERE perfil = ? AND ativo = TRUE";
        
        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, perfil.name());
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        return 0;
    }
    
    // ==================== MÉTODOS AUXILIARES PRIVADOS ====================
    
    /**
     * Preenche o PreparedStatement com os dados do usuário para inserção.
     * 
     * @param stmt PreparedStatement a ser preenchido
     * @param usuario Usuario com os dados
     * @throws SQLException Se houver erro ao preencher
     */
    private void preencherStatementParaInsercao(PreparedStatement stmt, Usuario usuario) throws SQLException {
        stmt.setString(1, usuario.getNome());
        stmt.setString(2, usuario.getEmail());
        stmt.setString(3, usuario.getSenha());
        stmt.setString(4, usuario.getTelefone());
        stmt.setString(5, usuario.getPerfil().name());
        stmt.setString(6, usuario.getEndereco());
        stmt.setString(7, usuario.getCidade());
        stmt.setString(8, usuario.getEstado());
        stmt.setString(9, usuario.getPais());
        stmt.setString(10, usuario.getCodigoPostal());
    }
    
    /**
     * Extrai um objeto Usuario de um ResultSet.
     * 
     * @param rs ResultSet com os dados do usuário
     * @return Usuario extraído do ResultSet
     * @throws SQLException Se houver erro ao extrair dados
     */
    private Usuario extrairUsuarioDoResultSet(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        
        // Dados principais
        usuario.setId(rs.getInt("id"));
        usuario.setNome(rs.getString("nome"));
        usuario.setEmail(rs.getString("email"));
        usuario.setSenha(rs.getString("senha"));
        usuario.setTelefone(rs.getString("telefone"));
        usuario.setPerfil(Perfil.valueOf(rs.getString("perfil")));
        
        // Dados de endereço
        usuario.setEndereco(rs.getString("endereco"));
        usuario.setCidade(rs.getString("cidade"));
        usuario.setEstado(rs.getString("estado"));
        usuario.setPais(rs.getString("pais"));
        usuario.setCodigoPostal(rs.getString("codigo_postal"));
        
        // Dados de controle
        usuario.setAtivo(rs.getBoolean("ativo"));
        
        // Datas
        Timestamp dataCriacao = rs.getTimestamp("data_criacao");
        if (dataCriacao != null) {
            usuario.setDataCriacao(dataCriacao.toLocalDateTime());
        }
        
        Timestamp dataAtualizacao = rs.getTimestamp("data_atualizacao");
        if (dataAtualizacao != null) {
            usuario.setDataAtualizacao(dataAtualizacao.toLocalDateTime());
        }
        
        return usuario;
    }
}