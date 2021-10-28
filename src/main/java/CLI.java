import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Scanner;

public class CLI {
    private static Socket clientSocket; //сокет для общения
    private static BufferedReader in; // поток чтения из сокета
    private static BufferedWriter out; // поток записи в сокет

    public static void main(String[] args) throws FileNotFoundException {
        try {
            try {

                String host = args[0];
                int port = Integer.parseInt(args[1]);

                List<String> usernames = new ArrayList<>();
                List<String> passwords = new ArrayList<>();

                Scanner scanner = new Scanner(new File(args[2]));
                while (scanner.hasNext()) {
                    usernames.add(scanner.next());
                }
                scanner = new Scanner(new File(args[3]));
                while (scanner.hasNext()) {
                    passwords.add(scanner.next());
                }


                clientSocket = new Socket(host, port); // этой строкой мы запрашиваем
                //clientSocket = new Socket("localhost", 4004); // этой строкой мы запрашиваем
                //  у сервера доступ на соединение
                // читать соообщения с сервера
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                // писать туда же
                out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));

                String serverWord;
                for (String name : usernames
                ) {
                    for (String pass : passwords
                    ) {

                        serverWord = "";
                        while (!serverWord.equals("Username:")) {
                            serverWord += (char) in.read();

                        }


                        // System.out.println(serverWord);

                        out.write(name + "\n"); // отправляем сообщение на сервер
                        out.flush();

                        serverWord = "";
                        while (!serverWord.equals("Password:")) {

                            serverWord += (char) in.read();
                        }




                        out.write(pass + "\n"); // отправляем сообщение на сервер
                        out.flush();

                        serverWord = in.readLine(); // ждём, что скажет сервер
                        if (serverWord.equals("Successfully")) {

                            System.out.println("Username: " + name
                                    + " Password: " + pass + "\n");

                        }

                    }
                }

            } finally { // в любом случае необходимо закрыть сокет и потоки
                clientSocket.close();
                in.close();
                out.close();
            }
        } catch (IOException e) {
            System.err.println(e);
        }

    }
}
