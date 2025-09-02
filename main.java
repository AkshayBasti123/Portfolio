import java.io.*;
import java.net.*;
import java.nio.file.*;

public class main {
    public static void main(String[] args) throws Exception {
        int port = 8080;
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Portfolio running at http://localhost:" + port);

        while (true) {
            Socket client = serverSocket.accept();
            BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            OutputStream out = client.getOutputStream();

            String request = in.readLine();
            if (request == null) continue;

            String[] parts = request.split(" ");
            String path = parts[1].equals("/") ? "/index.html" : parts[1];

            File file = new File("." + path);
            if (!file.exists()) file = new File("./index.html");

            byte[] content = Files.readAllBytes(file.toPath());
            String contentType = path.endsWith(".css") ? "text/css" : "text/html";

            String response = "HTTP/1.1 200 OK\r\n" +
                    "Content-Length: " + content.length + "\r\n" +
                    "Content-Type: " + contentType + "\r\n\r\n";

            out.write(response.getBytes());
            out.write(content);
            out.flush();
            client.close();
        }
    }
}
