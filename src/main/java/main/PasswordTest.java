package main;

// Teste para gerar hash de senha
public class PasswordTest {
    public static void main(String[] args) {

        String senha = "1234"; // Senha do usuário teste
        String hash = utils.security.PasswordUtil.hashPassword(senha);
        System.out.println("Hash da senha: " + hash);

    }
}