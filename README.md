# Sistema de Gerenciamento de Usuários

Sistema completo de controle de acesso de usuários desenvolvido em Java 21 com arquitetura MVC, interface gráfica Swing e banco de dados MySQL.

## 🚀 Tecnologias

- **Java 21** - Linguagem de programação
- **MySQL 8.0+** - Banco de dados
- **JDBC** - Conexão com banco de dados
- **Swing** - Interface gráfica
- **Maven** - Gerenciamento de dependências
- **MVC** - Padrão de arquitetura
- **OOP** - Programação Orientada a Objetos

## 📋 Funcionalidades

### ✅ Implementadas
- ✓ Login de usuários com autenticação
- ✓ Listagem de todos os usuários cadastrados
- ✓ Cadastro de novos usuários
- ✓ Edição de usuários existentes
- ✓ Exclusão lógica de usuários (apenas admin)
- ✓ Perfis de acesso (user e admin)
- ✓ Validação de dados
- ✓ Interface gráfica responsiva

### 📊 Campos do Usuário
- Nome (obrigatório)
- E-mail (obrigatório, único)
- Senha (obrigatória no cadastro)
- Telefone (formato internacional: +55 51 99999-9999)
- Perfil (user ou admin)
- Endereço
- Cidade
- Estado
- País
- Código Postal

## 🏗️ Estrutura do Projeto

```
src/
├── config/
│   └── DatabaseConfig.java      # Configuração da conexão com BD
├── model/
│   └── Usuario.java              # Modelo de dados do usuário
├── dao/
│   └── UsuarioDAO.java           # Acesso a dados (CRUD)
├── controller/
│   └── UsuarioController.java   # Lógica de negócio
└── view/
    ├── LoginView.java            # Tela de login
    ├── MainView.java             # Tela principal com listagem
    └── UsuarioFormView.java      # Formulário de cadastro/edição
```

## 🛠️ Instalação e Configuração

### 1. Pré-requisitos
- Java 21 ou superior instalado
- MySQL 8.0 ou superior instalado
- Maven 3.6+ instalado
- IDE (IntelliJ IDEA, Eclipse, VS Code, etc.)

### 2. Configurar o Banco de Dados

Execute o script SQL fornecido para criar o banco de dados e a tabela:

```sql
CREATE DATABASE user_management;
USE user_management;
-- Execute o resto do script fornecido
```

### 3. Configurar a Conexão

Edite o arquivo `DatabaseConfig.java` com suas credenciais:

```java
private static final String URL = "jdbc:mysql://localhost:3306/user_management";
private static final String USER = "seu_usuario";
private static final String PASSWORD = "sua_senha";
```

### 4. Adicionar Dependências

O projeto usa Maven. Certifique-se de ter o arquivo `pom.xml` configurado.

Execute:
```bash
mvn clean install
```

### 5. Executar o Projeto

**Opção 1: Via IDE**
- Abra o projeto na sua IDE
- Execute a classe `LoginView.java`

**Opção 2: Via Maven**
```bash
mvn compile exec:java -Dexec.mainClass="view.LoginView"
```

**Opção 3: Gerar JAR executável**
```bash
mvn clean package
java -jar target/gerenciamento-usuarios-1.0-SNAPSHOT.jar
```

## 👤 Usuário Padrão

O sistema vem com um usuário administrador pré-cadastrado:

- **Email:** admin@sistema.com
- **Senha:** admin123
- **Perfil:** admin

## 🎯 Como Usar

### Login
1. Inicie a aplicação
2. Digite o email e senha
3. Clique em "Entrar"

### Gerenciar Usuários
1. Após o login, você verá a lista de usuários
2. Use os botões:
   - **Novo Usuário**: Cadastrar novo usuário
   - **Editar**: Editar usuário selecionado
   - **Excluir**: Excluir usuário (apenas admin)
   - **Atualizar Lista**: Recarregar a tabela

### Cadastrar Usuário
1. Clique em "Novo Usuário"
2. Preencha os campos obrigatórios (*)
3. Clique em "Salvar"

### Editar Usuário
1. Selecione um usuário na tabela
2. Clique em "Editar"
3. Modifique os campos desejados
4. Clique em "Salvar"

## 🔒 Segurança

### Implementações Atuais
- Validação de campos obrigatórios
- Validação de formato de email
- Senha mínima de 6 caracteres
- Perfis de acesso diferenciados
- Exclusão lógica (mantém histórico)

### Melhorias Sugeridas para Produção
1. **Criptografia de Senhas**: Implementar BCrypt
   ```java
   import org.mindrot.jbcrypt.BCrypt;
   
   // No cadastro
   String senhaCriptografada = BCrypt.hashpw(senha, BCrypt.gensalt());
   
   // No login
   if (BCrypt.checkpw(senhaDigitada, usuario.getSenha())) {
       // Senha correta
   }
   ```

2. **Proteção contra SQL Injection**: Já implementado com PreparedStatement

3. **Sessão de Usuário**: Implementar controle de sessão com timeout

4. **Logs de Auditoria**: Registrar todas as operações críticas

5. **HTTPS**: Para ambiente de produção

## 🧪 Testando

### Cenários de Teste

**1. Login**
- Login com credenciais válidas ✓
- Login com credenciais inválidas ✓
- Campos vazios ✓

**2. Cadastro**
- Cadastro com todos os campos ✓
- Cadastro apenas com campos obrigatórios ✓
- Email duplicado ✓
- Email inválido ✓
- Senhas não coincidem ✓

**3. Edição**
- Editar dados do usuário ✓
- Não permitir edição de senha na tela de edição ✓

**4. Exclusão**
- Excluir usuário (apenas admin) ✓
- Não permitir autoexclusão ✓

## 📝 Padrões Utilizados

### MVC (Model-View-Controller)
- **Model**: `Usuario.java` - Representa os dados
- **View**: `LoginView.java`, `MainView.java`, `UsuarioFormView.java` - Interface gráfica
- **Controller**: `UsuarioController.java` - Lógica de negócio

### DAO (Data Access Object)
- `UsuarioDAO.java` - Encapsula acesso ao banco de dados

### Singleton
- `DatabaseConfig.java` - Única instância de conexão

## 🔧 Troubleshooting

### Erro de Conexão com Banco
```
Verifique:
- MySQL está rodando?
- Credenciais corretas?
- Banco de dados foi criado?
- Firewall não está bloqueando?
```

### Driver JDBC não encontrado
```bash
mvn clean install
# Ou adicione manualmente o JAR do MySQL Connector
```

### Erro de compilação
```
Certifique-se de estar usando Java 21:
java -version
```

## 📈 Próximas Melhorias

- [x] Criptografia de senhas com BCrypt
- [ ] Recuperação de senha por email
- [ ] Filtros e busca na listagem
- [ ] Paginação da tabela
- [ ] Exportar relatórios (PDF, Excel)
- [ ] Log de auditoria
- [ ] Testes unitários
- [ ] API REST

## 📄 Licença

Este projeto é livre para uso educacional e comercial.

---

**Nota**: Este é um sistema básico para fins educacionais. Para ambientes de produção, implemente as melhorias de segurança sugeridas.