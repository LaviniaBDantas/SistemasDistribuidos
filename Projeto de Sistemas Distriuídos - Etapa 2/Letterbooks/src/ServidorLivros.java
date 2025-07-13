import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class ServidorLivros {

    private static final int PORTA = 12345; //definindo a porta de comunicação

    public static void main(String[] args) {
        System.out.println("Servidor de Livros iniciado na porta " + PORTA + "...");

        try (ServerSocket serverSocket = new ServerSocket(PORTA)) { //cria um socket
            while (true) { //fica na escuta esperando as conexoes/requisicoes do cliente
                System.out.println("Esperando por conexao de cliente...");
                Socket clientSocket = serverSocket.accept(); //aceita o cliente
                String clientAddress = clientSocket.getInetAddress().getHostAddress();
                System.out.println("Cliente conectado: " + clientAddress);

                new Thread(() -> { //abre uma thread para tratar cada cliente, permitindo que mais de um cliente se conecte simultaneamente
                    System.out.println("Thread para cliente " + clientAddress + " iniciada. ID: " + Thread.currentThread().getId());
                    handleClient(clientSocket);
                    System.out.println("Thread para cliente " + clientAddress + " finalizada. ID: " + Thread.currentThread().getId());
                }).start();
            }
        } catch (IOException e) {
            System.err.println("Erro no servidor: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        String clientAddress = clientSocket.getInetAddress().getHostAddress(); //pega o endereco do cliente da thread
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        ) {
            String request = in.readLine(); //LE APENAS 1 REQ POR CLIENTE
            if (request != null) {
                System.out.println("Thread ID " + Thread.currentThread().getId() + " - Requisicao do cliente " + clientAddress + ": " + request);

                if ("GET_LIVROS".equalsIgnoreCase(request)) {
                    String jsonLivros = "[{\"titulo\":\"O Pequeno Príncipe\",\"autor\":\"Antoine de Saint-Exupéry\"},{\"titulo\":\"1984\",\"autor\":\"George Orwell\"}]";
                    out.println(jsonLivros);
                    System.out.println("JSON de livros enviado ao cliente.");
                } else if ("SAIR".equalsIgnoreCase(request)) {
                    System.out.println("Thread ID " + Thread.currentThread().getId() + " - Cliente " + clientAddress + " solicitou SAIR. Fechando conexao.");
                } else {
                    out.println("Comando desconhecido: " + request);
                    System.out.println("Thread ID " + Thread.currentThread().getId() + " - Comando desconhecido do cliente " + clientAddress + ": " + request);
                }
            }
            else{
                System.out.println("Thread ID " + Thread.currentThread().getId() + " - Cliente " + clientAddress + " desconectou sem enviar requisiçao.");
            }
        } catch (IOException e) {
            System.err.println("Thread ID " + Thread.currentThread().getId() + " - Erro ao lidar com o cliente " + clientAddress + ": " + e.getMessage());
        } finally {
            try {
                // É crucial fechar o socket do cliente aqui para liberar a conexão
                clientSocket.close();
                System.out.println("Thread ID " + Thread.currentThread().getId() + " - Conexao com o cliente " + clientAddress + " fechada.");
            } catch (IOException e) {
                System.err.println("Thread ID " + Thread.currentThread().getId() + " - Erro ao fechar o socket do cliente " + clientAddress + ": " + e.getMessage());
            }
        }
    }
}