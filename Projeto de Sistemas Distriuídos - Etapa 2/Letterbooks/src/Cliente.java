import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {

    private static final String ENDERECO_SERVIDOR = "localhost";
    private static final int PORTA = 12345;

    public static void main(String[] args) {
        System.out.println("Cliente de Livros iniciado...");

        try (
                Socket socket = new Socket(ENDERECO_SERVIDOR, PORTA);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                Scanner scanner = new Scanner(System.in)
        ) {
            System.out.println("Conectado ao servidor em " + ENDERECO_SERVIDOR + ":" + PORTA);

            String userInput;
            do {
                System.out.print("Digite um comando (GET_LIVROS, SAIR): ");
                userInput = scanner.nextLine();

                out.println(userInput);
                System.out.println("Mensagem enviada: " + userInput);

                String response = in.readLine();
                System.out.println("Resposta do servidor: " + response);

            } while (!"SAIR".equalsIgnoreCase(userInput));

        } catch (IOException e) {
            System.err.println("Erro no cliente: " + e.getMessage());
            e.printStackTrace();
        } finally {
            System.out.println("Cliente desconectado.");
        }
    }
}