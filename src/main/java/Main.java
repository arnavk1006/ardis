import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    public static void main(String[] args) {
        // You can use print statements as follows for debugging, they'll be visible when running tests.
        System.out.println("Logs from your program will appear here!");

        Socket clientSocket = null;
        int port = 6379;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            try {
                // Since the tester restarts your program quite often, setting SO_REUSEADDR
                // ensures that we don't run into 'Address already in use' errors
                serverSocket.setReuseAddress(true);

                while (true) {
                    // Wait for connection from client.
                    clientSocket = serverSocket.accept();

                /*
                    Create a new thread to handle the client connection.
                    Each client gets its own thread, and thus gets its own I/O streams.
                    Look at the thread implementation in ClientHandler.java.
                */
                    ClientHandler clientHandler = new ClientHandler(clientSocket);
                    Thread thread = new Thread(clientHandler);
                    thread.start();
                }

            } catch (IOException e) {
                System.out.println("IOException: " + e.getMessage());
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }
}
