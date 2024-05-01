//Maria Peralta 40112207
//jiaxuan zhao 40083126
//KunYi Wang 40057906

import javax.swing.*; // for GUI components
import java.awt.event.ActionEvent; // for events
import java.awt.event.ActionListener; // for listeners
import java.awt.event.WindowAdapter; // for events
import java.awt.event.WindowEvent; // for events
import java.io.BufferedReader; // for input
import java.io.DataOutputStream; // for output
import java.io.InputStreamReader; // for input
import java.net.Socket; // for network connections

/**
* Part of the code was extracted from the code provided by the TA
* @author Mohammad Altahat
*/


// Declare the client class
public class client {

	// Declare the class variables
    private static Socket clientSocket; // the client's socket
    private static JTextArea receivedTextArea; // text area for received messages
    private static JTextArea sendTextArea; // text area for sending messages
    private static String clientName = ""; // the client's name

    // Declare the main method
    public static void main(String[] args) throws Exception {


    // Connection first row (Label Name, input name, and connection Button)
    	 // Create the GUI frame and components
        JFrame frame = new JFrame ("Chatting Client"); // create a new frame
        frame.setLayout(null); // set layout to null
        frame.setBounds(100, 100, 480, 600); // set the bounds of the frame
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // set the default close operation

        // User Name
        JLabel userName = new JLabel("Client Name"); // create a new label
        userName.setBounds(20, 40, 150, 30); // set the bounds of the label
        frame.getContentPane().add(userName); // add the label to the frame

        //  input field for name
        JTextField userTxtField = new JTextField(); // create a new text field
        userTxtField.setBounds(130, 40, 150, 30); // set the bounds of the text field
        frame.getContentPane().add(userTxtField); // add the text field to the frame

        // Connect/Disconnect button
        JButton connectButton = new JButton("Connect"); // create a new button
        connectButton.setBounds(330, 40, 100, 30); // set the bounds of the button
        frame.getContentPane().add(connectButton); // add the button to the frame
    // connection ends 


        // Begin chatting (text area)

        receivedTextArea = new JTextArea(); // create a new text area
        receivedTextArea.setBounds(20, 100, 420, 300); // set the bounds of the text area
        receivedTextArea.setEditable(false); // make the text area not editable
        frame.getContentPane().add(receivedTextArea); // add the text area to the frame
        receivedTextArea.setVisible(false); // set the visibility to false

        JScrollPane receivedTextAreaScroll = new JScrollPane(receivedTextArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); // create a new scroll pane
        receivedTextAreaScroll.setBounds(20,  100,  420,  300); // set the bounds of the scroll pane
        frame.getContentPane().add(receivedTextAreaScroll); // add the scroll pane to the frame
        receivedTextAreaScroll.setVisible(false); // set the visibility to false

        // chats ends


     // Sending inputs (Name receiver)
     // Create a label for "Send to"
     JLabel sendToLabel = new JLabel("Send to");
     // Set the position and size of the label on the frame
     sendToLabel.setBounds(20, 430, 150, 30);
     // Add the label to the frame's content pane
     frame.getContentPane().add(sendToLabel);
     // Set the visibility of the label to false
     sendToLabel.setVisible(false);

     // Create a text field for the user to input the recipient of the message
     JTextField sendToTextField = new JTextField();
     // Set the position and size of the text field on the frame
     sendToTextField.setBounds(130, 430, 150, 30);
     // Add the text field to the frame's content pane
     frame.getContentPane().add(sendToTextField);
     // Set the visibility of the text field to false
     sendToTextField.setVisible(false);
     // end user choice 

     // Sending text field (message inputed)
     // Create a text area for the user to compose the message
     sendTextArea = new JTextArea();
     // Set the position and size of the text area on the frame
     sendTextArea.setBounds(20, 480, 300, 300);
     // Add the text area to the frame's content pane
     frame.getContentPane().add(sendTextArea);
     // Set the visibility of the text area to false
     sendTextArea.setVisible(false);

     // Create a button for the user to send the message
     JButton sendButton = new JButton("Send");
     // Set the position and size of the button on the frame
     sendButton.setBounds(330, 500, 80, 30);
     // Add the button to the frame's content pane
     frame.getContentPane().add(sendButton);
     // Set the visibility of the button to false
     sendButton.setVisible(false);
 // ending the text field


        // Action listener when connect button is pressed
        connectButton.addActionListener( new ActionListener() {
            public void actionPerformed(ActionEvent e)
            {
            	try {

                    if (connectButton.getText().equals("Connect")) { //if pressed to connect

                        // get the client name from the text field
                        clientName = userTxtField.getText();

                        // check if the client name is not empty and not equal to "ALL_USERS"
                        if (! (clientName.equals("") || clientName.equals("ALL_USERS"))) {
                            // create a new socket to connect with the server application
                            clientSocket = new Socket ("localhost", 6789);

                            // call function StartThread to start a new thread that will always read messages from the server
                            StartThread();

                            //make the GUI components visible, so the client can send and receive messages
                            userTxtField.setEditable(false);
                            receivedTextArea.setVisible(true);
                            receivedTextAreaScroll.setVisible(true);
                            sendToLabel.setVisible(true);
                            sendToTextField.setVisible(true);
                            sendTextArea.setVisible(true);
                            sendButton.setVisible(true);

                            // change the Connect button text to disconnect
                            connectButton.setText("Disconnect");
                        }

                    } else { // if pressed to disconnect

                        // create an output stream and send a Remove message to disconnect from the server
                        DataOutputStream outToServer = new DataOutputStream (clientSocket.getOutputStream());

                        outToServer.writeBytes("-Disconnect," + clientName + "\n");

                        outToServer.writeBytes("-Remove\n");

                        // close the client's socket
                        clientSocket.close();

                        clientName = "";

                        // make the GUI components invisible
                        userTxtField.setEditable(true);
                        receivedTextArea.setVisible(false);
                        receivedTextAreaScroll.setVisible(false);
                        sendToLabel.setVisible(false);
                        sendToTextField.setVisible(false);
                        sendTextArea.setVisible(false);
                        sendButton.setVisible(false);

                        // change the Connect button text to connect
                        connectButton.setText("Connect");
                    }

                } catch (Exception ex) {
                    System.out.println(ex.toString());
                }
            }});



     // Disconnect on close
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {

                try {

                    // create an output stream and send a Remove message to disconnect from the server
                    DataOutputStream outToServer = new DataOutputStream (clientSocket.getOutputStream());
                    outToServer.writeBytes("-Remove\n");

                    // close the client's socket
                    clientSocket.close();

                    clientName = "";

                    // make the GUI components invisible
                    userTxtField.setEditable(true);
                    receivedTextArea.setVisible(false);
                    receivedTextAreaScroll.setVisible(false);
                    sendToLabel.setVisible(false);
                    sendToTextField.setVisible(false);
                    sendTextArea.setVisible(false);
                    sendButton.setVisible(false);

                    // change the Connect button text to connect
                    connectButton.setText("Connect");

                    System.exit(0);

                } catch (Exception ex) {
                    System.out.println(ex.toString());
                }


            }
        });

     // send button
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String toClient;
                    if (sendToTextField.getText().equals(""))
                        toClient = "ALL_CLIENTS";
                    else
                        toClient = sendToTextField.getText();

                    DataOutputStream outToServer = new DataOutputStream (clientSocket.getOutputStream());
                    outToServer.writeBytes("-Message," + sendTextArea.getText() + "," + toClient + "," + clientName + "\n");

                    sendTextArea.setText("");

                } catch (Exception er) {}
            }
        });

     
        frame.setVisible(true);

    }

    // Thread to always read messages from the server and print them in the textArea
    private static void StartThread() {

        new Thread (new Runnable(){ @Override
        public void run() {

            try {

                // create a buffer reader and connect it to the socket's input stream
                BufferedReader inFromServer = new BufferedReader (new InputStreamReader(clientSocket.getInputStream()));
                String receivedSentence;

                //create an output stream
                DataOutputStream outToServer = new DataOutputStream (clientSocket.getOutputStream());

                // always read received messages and append them to the textArea
                while (true) {
                    // Read the incoming message from the server
                    receivedSentence = inFromServer.readLine();
                    System.out.println(receivedSentence);

                    // Handle different types of messages
                    if (receivedSentence.startsWith("-Connected")) {
                        // Send the client's name to the server upon connecting
                        outToServer.writeBytes("-Name," + clientName + "\n");
                    } else if (receivedSentence.startsWith("-Joined")) {
                        // Update the text area with a message when a user joins the chat
                        String[] strings = receivedSentence.split(",");

                        if (strings[1].equals(clientName)) {
                            receivedTextArea.setText("You are connected." + "\n");
                        } else {
                            receivedTextArea.append(strings[1] + " is connected." + "\n");
                        }
                    } else if (receivedSentence.startsWith("-Message")) {
                        // Append the received message to the text area
                        String[] strings = receivedSentence.split(",");

                        if (strings[2].equals(clientName)) {
                            receivedTextArea.append("You: " + strings[1] + "\n");
                        } else {
                            receivedTextArea.append(strings[2] + ": " + strings[1] + "\n");
                        }
                    } else if (receivedSentence.startsWith("-Disconnect")) {
                        // Update the text area with a message when a user disconnects from the chat
                        String[] strings = receivedSentence.split(",");
                        receivedTextArea.append(strings[1] + " disconnected." + "\n");
                    }
                }
            } catch (Exception ex) {
                // Handle any exceptions that occur
            }
        }}).start();
    }
}