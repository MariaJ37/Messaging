//Maria Peralta 40112207
//jiaxuan zhao 40083126
//KunYi Wang 40057906


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
/**
* Part of the code was extracted from the code provided by the TA
* @author Mohammad Altahat
*/




public class clientThread extends Thread{

    // The ClientServiceThread class extends the Thread class and has the following parameters
    public String name;
    private int number; // client name
    public Socket connectionSocket; // client connection socket
    ArrayList<clientThread> Clients; // list of all clients connected to the server

    // constructor function
    public clientThread(int number, Socket connectionSocket, ArrayList<clientThread> Clients) {

        this.number = number;
        this.connectionSocket = connectionSocket;
        this.Clients = Clients;
        this.name = "";

    }

    // thread's run function
    public void run() {

        try {

            // create a buffer reader and connect it to the client's connection socket
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            String clientSentence;
            DataOutputStream outToClient;

            // always read messages from client
            while (true) {

                clientSentence = inFromClient.readLine();
                System.out.println(clientSentence);

                // check the start of the message

                if (clientSentence.startsWith("-Remove")) { // Remove Client
                    for (int i = 0; i < Clients.size(); i++) {
                        if (Clients.get(i).number == number) {
                            Clients.remove(i);
                        }
                    }
                }

                else if (clientSentence.startsWith("-Name")) {
                    String []client = clientSentence.split(",");
                    name = client[1];

                    server.sendJoinedNameMessage(name);
                }

                else if (clientSentence.startsWith("-Message")) {
                    String []strings = clientSentence.split(",");

                    server.sendMessage(strings[1], strings[2], strings[3]);
                }

                else if (clientSentence.startsWith("-Disconnect")) {
                    String []client = clientSentence.split(",");

                    server.sendDisconnectMessage(client[1]);
                }

            }


        } catch (Exception ex) {
        }

    }
}
