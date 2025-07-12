import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorLivros {

    private static final int PORTA = 12345;

    public static void main(String[] args) {
        System.out.println("Servidor de Livros iniciado na porta " + PORTA + "...");

        try (ServerSocket serverSocket = new ServerSocket(PORTA)) {
            while (true) {
                System.out.println("Esperando por conexão de cliente...");
                Socket clientSocket = serverSocket.accept();
                System.out.println("Cliente conectado: " + clientSocket.getInetAddress().getHostAddress());

                handleClient(clientSocket);
            }
        } catch (IOException e) {
            System.err.println("Erro no servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true); // 'true' para auto-flush
        ) {
            String request;
            while ((request = in.readLine()) != null) {
                System.out.println("Requisição do cliente: " + request);

                if ("GET_LIVROS".equalsIgnoreCase(request)) {
                    String jsonLivros = "[{\"titulo\":\"O Pequeno Príncipe\",\"autor\":\"Antoine de Saint-Exupéry\"},{\"titulo\":\"1984\",\"autor\":\"George Orwell\"}]";
                    out.println(jsonLivros);
                    System.out.println("JSON de livros enviado ao cliente.");
                } else if ("SAIR".equalsIgnoreCase(request)) {
                    System.out.println("Cliente solicitou SAIR. Fechando conexão.");
                    break;
                } else {
                    out.println("Comando desconhecido: " + request);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao lidar com o cliente: " + e.getMessage());
        } finally {
            try {
                clientSocket.close();
                System.out.println("Conexão com o cliente fechada.");
            } catch (IOException e) {
                System.err.println("Erro ao fechar o socket do cliente: " + e.getMessage());
            }
        }
    }
}