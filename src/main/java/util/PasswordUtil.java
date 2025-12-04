package util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Classe utilitária para criptografia e validação de senhas usando BCrypt.
 * BCrypt é um algoritmo de hash de senha seguro com salt automático.
 * 
 * @author Emerson Meneses Inocente <emerson_inocente@hotmail.com>
 * @version 1.0
 */
public class PasswordUtil {
    
    /**
     * Número de rounds do algoritmo BCrypt.
     * Quanto maior, mais seguro mas mais lento.
     * Valor padrão: 10 (recomendado para a maioria dos casos)
     */
    private static final int BCRYPT_ROUNDS = 10;
    
    /**
     * Construtor privado para impedir instanciação.
     * Esta é uma classe utilitária com métodos estáticos.
     */
    private PasswordUtil() {
        throw new UnsupportedOperationException("Classe utilitária não deve ser instanciada");
    }
    
    /**
     * Criptografa uma senha usando BCrypt.
     * Gera automaticamente um salt único para cada senha.
     * 
     * @param senhaTextoPlano Senha em texto plano
     * @return Senha criptografada (hash)
     * @throws IllegalArgumentException se a senha for nula ou vazia
     */
    public static String criptografarSenha(String senhaTextoPlano) {
        if (senhaTextoPlano == null || senhaTextoPlano.isEmpty()) {
            throw new IllegalArgumentException("Senha não pode ser nula ou vazia");
        }
        
        return BCrypt.hashpw(senhaTextoPlano, BCrypt.gensalt(BCRYPT_ROUNDS));
    }
    
    /**
     * Verifica se uma senha em texto plano corresponde ao hash armazenado.
     * 
     * @param senhaTextoPlano Senha digitada pelo usuário
     * @param senhaHash Senha criptografada armazenada no banco
     * @return true se a senha está correta, false caso contrário
     */
    public static boolean verificarSenha(String senhaTextoPlano, String senhaHash) {
        if (senhaTextoPlano == null || senhaTextoPlano.isEmpty()) {
            return false;
        }
        
        if (senhaHash == null || senhaHash.isEmpty()) {
            return false;
        }
        
        try {
            return BCrypt.checkpw(senhaTextoPlano, senhaHash);
        } catch (IllegalArgumentException e) {
            // Hash inválido ou senha não criptografada
            return false;
        }
    }
    
    /**
     * Verifica se uma string é um hash BCrypt válido.
     * Útil para verificar se uma senha já está criptografada.
     * 
     * @param senha String a ser verificada
     * @return true se for um hash BCrypt válido, false caso contrário
     */
    public static boolean isSenhaCriptografada(String senha) {
        if (senha == null || senha.isEmpty()) {
            return false;
        }
        
        // Hash BCrypt começa com $2a$, $2b$ ou $2y$
        // e tem exatamente 60 caracteres
        return senha.matches("^\\$2[aby]\\$\\d{2}\\$.{53}$");
    }
    
    /**
     * Valida a força de uma senha.
     * 
     * @param senha Senha a ser validada
     * @return true se a senha atende aos requisitos mínimos, false caso contrário
     */
    public static boolean validarForcaSenha(String senha) {
        if (senha == null || senha.length() < 6) {
            return false;
        }
        
        // Pode adicionar mais validações aqui:
        // - Pelo menos uma letra maiúscula
        // - Pelo menos uma letra minúscula
        // - Pelo menos um número
        // - Pelo menos um caractere especial
        
        return true;
    }
    
    /**
     * Retorna uma mensagem descritiva sobre a força da senha.
     * 
     * @param senha Senha a ser avaliada
     * @return Mensagem sobre a força da senha
     */
    public static String avaliarForcaSenha(String senha) {
        if (senha == null || senha.isEmpty()) {
            return "Senha não pode ser vazia";
        }
        
        if (senha.length() < 6) {
            return "Senha muito fraca (mínimo 6 caracteres)";
        }
        
        int pontos = 0;
        
        // Comprimento
        if (senha.length() >= 8) pontos++;
        if (senha.length() >= 12) pontos++;
        
        // Letras maiúsculas
        if (senha.matches(".*[A-Z].*")) pontos++;
        
        // Letras minúsculas
        if (senha.matches(".*[a-z].*")) pontos++;
        
        // Números
        if (senha.matches(".*\\d.*")) pontos++;
        
        // Caracteres especiais
        if (senha.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) pontos++;
        
        if (pontos <= 2) return "Senha fraca";
        if (pontos <= 4) return "Senha média";
        return "Senha forte";
    }
    
    /**
     * Método de teste para demonstrar o uso da classe.
     */
    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("TESTE DE CRIPTOGRAFIA DE SENHAS");
        System.out.println("=".repeat(60));
        System.out.println();
        
        // Teste 1: Criptografar senha
        String senhaOriginal = "admin123";
        System.out.println("Senha original: " + senhaOriginal);
        
        String senhaCriptografada = criptografarSenha(senhaOriginal);
        System.out.println("Senha criptografada: " + senhaCriptografada);
        System.out.println("Tamanho do hash: " + senhaCriptografada.length() + " caracteres");
        System.out.println();
        
        // Teste 2: Verificar senha correta
        System.out.println("Teste com senha CORRETA:");
        boolean senhaCorreta = verificarSenha("admin123", senhaCriptografada);
        System.out.println("Resultado: " + (senhaCorreta ? "✓ VÁLIDA" : "✗ INVÁLIDA"));
        System.out.println();
        
        // Teste 3: Verificar senha incorreta
        System.out.println("Teste com senha INCORRETA:");
        boolean senhaIncorreta = verificarSenha("senhaerrada", senhaCriptografada);
        System.out.println("Resultado: " + (senhaIncorreta ? "✓ VÁLIDA" : "✗ INVÁLIDA"));
        System.out.println();
        
        // Teste 4: Verificar se é hash BCrypt
        System.out.println("Verificar se string é hash BCrypt:");
        System.out.println("  Hash válido: " + isSenhaCriptografada(senhaCriptografada));
        System.out.println("  Texto plano: " + isSenhaCriptografada("admin123"));
        System.out.println();
        
        // Teste 5: Avaliar força da senha
        String[] senhasTeste = {"123", "abc123", "Abc123", "Abc@123", "MyP@ssw0rd2024!"};
        System.out.println("Avaliação de força de senhas:");
        for (String s : senhasTeste) {
            System.out.println("  '" + s + "' → " + avaliarForcaSenha(s));
        }
        System.out.println();
        
        System.out.println("=".repeat(60));
        System.out.println("Use este hash para o usuário admin no banco:");
        System.out.println(senhaCriptografada);
        System.out.println("=".repeat(60));
    }
}