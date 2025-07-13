import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {

    private static final String ENDERECO_SERVIDOR = "localhost"; //definindo o endereco do servidor (local, nesse caos)
    private static final int PORTA = 12345; //porta em comum com o servidor

    public static void main(String[] args) {
        System.out.println("Cliente de Livros iniciado...");
        Scanner scanner = new Scanner(System.in);
        String userInput;
        do {
            System.out.print("Digite um comando (GET_LIVROS, SAIR): ");
            userInput = scanner.nextLine();

            if ("SAIR".equalsIgnoreCase(userInput)) {
                System.out.println("Saindo do cliente...");
                break; // Sai do loop principal se o usuário digitar SAIR
            }
            try ( //para cada comando, abre uma nova conexao
                  Socket socket = new Socket(ENDERECO_SERVIDOR, PORTA);
                  BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                  PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            ) { //Caso consiga se conectar, pode obter o "catálago" por meio de comandos
                System.out.println("Conectado ao servidor em " + ENDERECO_SERVIDOR + ":" + PORTA);

                out.println(userInput); //envia o comando para o servidor
                System.out.println("Mensagem enviada: " + userInput);
                String response = in.readLine(); //Le a resposta do servidor
                System.out.println("Resposta do servidor: " + response);
            } catch (IOException e) {
                System.err.println("Erro no cliente: " + e.getMessage());
                e.printStackTrace();
            } finally {
                System.out.println("Cliente desconectado.");
            }
        }while (true); //Loop até que o usuario saia
        scanner.close();
        System.out.println("Cliente desconectado.");


    }
}