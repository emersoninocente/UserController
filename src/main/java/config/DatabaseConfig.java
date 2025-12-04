package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe responsável pela configuração e gerenciamento da conexão com o banco de dados MySQL.
 * Implementa o padrão Singleton para garantir uma única instância de conexão.
 * 
 * @author Emerson Meneses Inocente <emerson_inocente@hotmail.com>
 * @version 1.0
 */
public class DatabaseConfig {
    
    // Configurações do banco de dados - ALTERE CONFORME SEU AMBIENTE
    private static final String HOST = "localhost";
    private static final String PORT = "3306";
    private static final String DATABASE = "user_management";
    private static final String URL = "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE 
                                     + "?useSSL=false"
                                     + "&serverTimezone=UTC"
                                     + "&allowPublicKeyRetrieval=true"
                                     + "&useUnicode=true"
                                     + "&characterEncoding=UTF-8";
    
    // Credenciais do banco de dados - ALTERE COM SUAS CREDENCIAIS
    private static final String USER = "usr_mgmt";
    private static final String PASSWORD = "P@ssw0rd";  // Coloque sua senha do MySQL aqui
    
    // Driver JDBC do MySQL
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    
    // Instância única da conexão (Singleton)
    private static Connection connection = null;
    
    /**
     * Construtor privado para impedir instanciação direta da classe.
     * Força o uso do método getConnection() para obter a conexão.
     */
    private DatabaseConfig() {
        // Construtor privado
    }
    
    /**
     * Obtém uma conexão ativa com o banco de dados.
     * Se a conexão não existir ou estiver fechada, cria uma nova.
     * Implementa o padrão Singleton.
     * 
     * @return Connection - Conexão ativa com o banco de dados
     * @throws SQLException - Se houver erro ao conectar com o banco
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Verifica se a conexão está nula ou fechada
            if (connection == null || connection.isClosed()) {
                // Carrega o driver JDBC do MySQL
                Class.forName(DRIVER);
                
                // Estabelece a conexão
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                
                System.out.println("✓ Conexão com banco de dados estabelecida com sucesso!");
                System.out.println("  Database: " + DATABASE);
                System.out.println("  Host: " + HOST + ":" + PORT);
            }
            
            return connection;
            
        } catch (ClassNotFoundException e) {
            String mensagem = "Driver JDBC do MySQL não encontrado!\n" +
                            "Certifique-se de que o MySQL Connector está no classpath.";
            System.err.println("✗ " + mensagem);
            throw new SQLException(mensagem, e);
            
        } catch (SQLException e) {
            String mensagem = "Erro ao conectar com o banco de dados!\n" +
                            "Verifique:\n" +
                            "  - MySQL está rodando?\n" +
                            "  - Banco '" + DATABASE + "' foi criado?\n" +
                            "  - Credenciais (usuário/senha) estão corretas?\n" +
                            "  - Host e porta estão corretos?";
            System.err.println("✗ " + mensagem);
            System.err.println("  Detalhes: " + e.getMessage());
            throw new SQLException(mensagem, e);
        }
    }
    
    /**
     * Fecha a conexão com o banco de dados se ela estiver aberta.
     * Deve ser chamado ao finalizar a aplicação.
     */
    public static void closeConnection() {
        if (connection != null) {
            try {
                if (!connection.isClosed()) {
                    connection.close();
                    System.out.println("✓ Conexão com banco de dados fechada.");
                }
            } catch (SQLException e) {
                System.err.println("✗ Erro ao fechar conexão: " + e.getMessage());
                e.printStackTrace();
            } finally {
                connection = null;
            }
        }
    }
    
    /**
     * Testa a conexão com o banco de dados.
     * Útil para verificar se as configurações estão corretas.
     * 
     * @return boolean - true se a conexão foi bem-sucedida, false caso contrário
     */
    public static boolean testarConexao() {
        try {
            Connection conn = getConnection();
            if (conn != null && !conn.isClosed()) {
                System.out.println("✓ Teste de conexão: SUCESSO");
                return true;
            }
        } catch (SQLException e) {
            System.err.println("✗ Teste de conexão: FALHOU");
            System.err.println("  Erro: " + e.getMessage());
        }
        return false;
    }
    
    /**
     * Retorna as informações de configuração do banco de dados.
     * Não expõe a senha por segurança.
     * 
     * @return String com as informações de configuração
     */
    public static String getConfigInfo() {
        return "Configuração do Banco de Dados:\n" +
               "  Host: " + HOST + "\n" +
               "  Porta: " + PORT + "\n" +
               "  Database: " + DATABASE + "\n" +
               "  Usuário: " + USER + "\n" +
               "  Driver: " + DRIVER;
    }
    
    /**
     * Método main para testar a conexão independentemente.
     * Execute esta classe diretamente para verificar se a conexão está funcionando.
     */
    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("TESTE DE CONEXÃO COM BANCO DE DADOS");
        System.out.println("=".repeat(60));
        System.out.println();
        
        System.out.println(getConfigInfo());
        System.out.println();
        
        System.out.println("Tentando conectar...");
        System.out.println();
        
        if (testarConexao()) {
            System.out.println();
            System.out.println("=".repeat(60));
            System.out.println("CONEXÃO BEM-SUCEDIDA! Sistema pronto para usar.");
            System.out.println("=".repeat(60));
            closeConnection();
        } else {
            System.out.println();
            System.out.println("=".repeat(60));
            System.out.println("FALHA NA CONEXÃO! Verifique as configurações.");
            System.out.println("=".repeat(60));
            System.exit(1);
        }
    }
}