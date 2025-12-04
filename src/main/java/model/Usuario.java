package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Classe modelo que representa um usuário do sistema.
 * Contém todos os atributos e métodos relacionados a um usuário.
 * 
 * @author Emerson Meneses Inocente <emerson_inocente@hotmail.com>
 * @version 1.0
 */
public class Usuario {
    
    // Atributos principais
    private int id;
    private String nome;
    private String email;
    private String senha;
    private String telefone;
    private Perfil perfil;
    
    // Atributos de endereço
    private String endereco;
    private String cidade;
    private String estado;
    private String pais;
    private String codigoPostal;
    
    // Atributos de controle
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;
    private boolean ativo;
    
    /**
     * Enum que define os perfis de acesso do sistema.
     */
    public enum Perfil {
        user("Usuário"),
        admin("Administrador");
        
        private final String descricao;
        
        Perfil(String descricao) {
            this.descricao = descricao;
        }
        
        public String getDescricao() {
            return descricao;
        }
        
        @Override
        public String toString() {
            return descricao;
        }
    }
    
    // ==================== CONSTRUTORES ====================
    
    /**
     * Construtor padrão.
     * Inicializa o usuário como ativo e com perfil de usuário comum.
     */
    public Usuario() {
        this.ativo = true;
        this.perfil = Perfil.user;
        this.dataCriacao = LocalDateTime.now();
        this.dataAtualizacao = LocalDateTime.now();
    }
    
    /**
     * Construtor com dados básicos.
     * 
     * @param nome Nome completo do usuário
     * @param email Email do usuário
     * @param senha Senha do usuário
     */
    public Usuario(String nome, String email, String senha) {
        this();
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }
    
    /**
     * Construtor completo.
     * 
     * @param id ID do usuário
     * @param nome Nome completo do usuário
     * @param email Email do usuário
     * @param senha Senha do usuário
     * @param telefone Telefone em formato internacional
     * @param perfil Perfil de acesso
     */
    public Usuario(int id, String nome, String email, String senha, String telefone, Perfil perfil) {
        this(nome, email, senha);
        this.id = id;
        this.telefone = telefone;
        this.perfil = perfil;
    }
    
    // ==================== GETTERS E SETTERS ====================
    
    /**
     * @return ID do usuário
     */
    public int getId() {
        return id;
    }
    
    /**
     * Define o ID do usuário.
     * @param id ID do usuário
     */
    public void setId(int id) {
        this.id = id;
    }
    
    /**
     * @return Nome completo do usuário
     */
    public String getNome() {
        return nome;
    }
    
    /**
     * Define o nome do usuário.
     * @param nome Nome completo do usuário
     */
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    /**
     * @return Email do usuário
     */
    public String getEmail() {
        return email;
    }
    
    /**
     * Define o email do usuário.
     * @param email Email do usuário
     */
    public void setEmail(String email) {
        this.email = email;
    }
    
    /**
     * @return Senha do usuário
     */
    public String getSenha() {
        return senha;
    }
    
    /**
     * Define a senha do usuário.
     * @param senha Senha do usuário
     */
    public void setSenha(String senha) {
        this.senha = senha;
    }
    
    /**
     * @return Telefone em formato internacional
     */
    public String getTelefone() {
        return telefone;
    }
    
    /**
     * Define o telefone do usuário.
     * @param telefone Telefone em formato internacional (ex: +55 51 99999-9999)
     */
    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
    
    /**
     * @return Perfil de acesso do usuário
     */
    public Perfil getPerfil() {
        return perfil;
    }
    
    /**
     * Define o perfil de acesso do usuário.
     * @param perfil Perfil (user ou admin)
     */
    public void setPerfil(Perfil perfil) {
        this.perfil = perfil;
    }
    
    /**
     * @return Endereço completo do usuário
     */
    public String getEndereco() {
        return endereco;
    }
    
    /**
     * Define o endereço do usuário.
     * @param endereco Endereço completo
     */
    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }
    
    /**
     * @return Cidade do usuário
     */
    public String getCidade() {
        return cidade;
    }
    
    /**
     * Define a cidade do usuário.
     * @param cidade Nome da cidade
     */
    public void setCidade(String cidade) {
        this.cidade = cidade;
    }
    
    /**
     * @return Estado do usuário
     */
    public String getEstado() {
        return estado;
    }
    
    /**
     * Define o estado do usuário.
     * @param estado Nome do estado
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }
    
    /**
     * @return País do usuário
     */
    public String getPais() {
        return pais;
    }
    
    /**
     * Define o país do usuário.
     * @param pais Nome do país
     */
    public void setPais(String pais) {
        this.pais = pais;
    }
    
    /**
     * @return Código postal (CEP)
     */
    public String getCodigoPostal() {
        return codigoPostal;
    }
    
    /**
     * Define o código postal do usuário.
     * @param codigoPostal Código postal/CEP
     */
    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }
    
    /**
     * @return Data e hora de criação do registro
     */
    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }
    
    /**
     * Define a data de criação do usuário.
     * @param dataCriacao Data e hora de criação
     */
    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
    
    /**
     * @return Data e hora da última atualização
     */
    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }
    
    /**
     * Define a data de atualização do usuário.
     * @param dataAtualizacao Data e hora da última atualização
     */
    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }
    
    /**
     * @return true se o usuário está ativo, false caso contrário
     */
    public boolean isAtivo() {
        return ativo;
    }
    
    /**
     * Define o status de ativo do usuário.
     * @param ativo Status ativo (true) ou inativo (false)
     */
    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }
    
    // ==================== MÉTODOS DE UTILIDADE ====================
    
    /**
     * Verifica se o usuário é administrador.
     * @return true se for admin, false caso contrário
     */
    public boolean isAdmin() {
        return this.perfil == Perfil.admin;
    }
    
    /**
     * Retorna o endereço completo formatado.
     * @return Endereço formatado ou mensagem padrão se incompleto
     */
    public String getEnderecoCompleto() {
        StringBuilder sb = new StringBuilder();
        
        if (endereco != null && !endereco.isEmpty()) {
            sb.append(endereco);
        }
        
        if (cidade != null && !cidade.isEmpty()) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(cidade);
        }
        
        if (estado != null && !estado.isEmpty()) {
            if (sb.length() > 0) sb.append(" - ");
            sb.append(estado);
        }
        
        if (pais != null && !pais.isEmpty()) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(pais);
        }
        
        if (codigoPostal != null && !codigoPostal.isEmpty()) {
            if (sb.length() > 0) sb.append(" - ");
            sb.append("CEP: " + codigoPostal);
        }
        
        return sb.length() > 0 ? sb.toString() : "Endereço não informado";
    }
    
    /**
     * Retorna a data de criação formatada.
     * @return Data formatada (dd/MM/yyyy HH:mm)
     */
    public String getDataCriacaoFormatada() {
        if (dataCriacao == null) return "N/A";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return dataCriacao.format(formatter);
    }
    
    /**
     * Retorna a data de atualização formatada.
     * @return Data formatada (dd/MM/yyyy HH:mm)
     */
    public String getDataAtualizacaoFormatada() {
        if (dataAtualizacao == null) return "N/A";
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        return dataAtualizacao.format(formatter);
    }
    
    /**
     * Valida se o email tem formato válido (básico).
     * @return true se o email é válido, false caso contrário
     */
    public boolean isEmailValido() {
        if (email == null || email.isEmpty()) return false;
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }
    
    /**
     * Retorna uma cópia do usuário sem a senha (para segurança).
     * @return Novo objeto Usuario sem senha
     */
    public Usuario semSenha() {
        Usuario copia = new Usuario();
        copia.id = this.id;
        copia.nome = this.nome;
        copia.email = this.email;
        copia.senha = null; // Não copia a senha
        copia.telefone = this.telefone;
        copia.perfil = this.perfil;
        copia.endereco = this.endereco;
        copia.cidade = this.cidade;
        copia.estado = this.estado;
        copia.pais = this.pais;
        copia.codigoPostal = this.codigoPostal;
        copia.dataCriacao = this.dataCriacao;
        copia.dataAtualizacao = this.dataAtualizacao;
        copia.ativo = this.ativo;
        return copia;
    }
    
    // ==================== MÉTODOS SOBRESCRITOS ====================
    
    /**
     * Representação em String do objeto Usuario.
     * Não inclui a senha por segurança.
     */
    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", telefone='" + telefone + '\'' +
                ", perfil=" + perfil +
                ", cidade='" + cidade + '\'' +
                ", estado='" + estado + '\'' +
                ", pais='" + pais + '\'' +
                ", ativo=" + ativo +
                ", dataCriacao=" + getDataCriacaoFormatada() +
                '}';
    }
    
    /**
     * Verifica igualdade entre usuários baseado no ID e email.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return id == usuario.id && Objects.equals(email, usuario.email);
    }
    
    /**
     * Gera hash code baseado no ID e email.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, email);
    }
}