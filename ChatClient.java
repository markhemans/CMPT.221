import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.*;

public class ChatClient {
    BufferedReader in;
    PrintWriter out;
    JFrame frame = new JFrame("FoxChat");
    JTextField textField = new JTextField(100);
    JTextArea messageArea = new JTextArea(10, 100);

    
    public ChatClient() {

        textField.setEditable(false);
        messageArea.setEditable(false);
        frame.getContentPane().add(textField, "North");
        frame.getContentPane().add(new JScrollPane(messageArea), "Center");
        frame.pack();
        textField.addActionListener(new ActionListener()
        {
           public void actionPerformed(ActionEvent e)
           {
           out.println(textField.getText());
           }
        });
    }

    
    
    private String getServerAddress()
    {
        
        Desktop desktop = java.awt.Desktop.getDesktop();
        URI oURL = null;
        try {
        oURL = new URI("https://www.iplocation.net/find-ip-address");
        }
        catch (URISyntaxException ex)
        {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            desktop.browse(oURL);
        } 
        catch (IOException ex)
        {
            Logger.getLogger(ChatClient.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return JOptionPane.showInputDialog(frame,"What's the server's IP Adress?","Foxchat - Chat with anyone at Marist!",JOptionPane.QUESTION_MESSAGE);
    }

    
    private String getCWID()
    {
        return JOptionPane.showInputDialog(frame,"Enter your CWID","Marist ID",JOptionPane.PLAIN_MESSAGE);
    }

    
    void run() throws IOException {
 
        String serverAddress = getServerAddress();
        Socket socket = new Socket(serverAddress, 9000);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);

        while (true) {
            String line = in.readLine();
            if (line.startsWith("SUBMITCWID"))
            {
                out.println(getCWID());
            }
            else if (line.startsWith("CWIDACCEPTED"))
            {
                textField.setEditable(true);
            }
            else if (line.startsWith("MESSAGE"))
            {
                messageArea.append(line.substring(8) + "\n");
            }
        }
    }

    
   
    public static void main(String[] args) throws Exception
    {
        ChatClient client = new ChatClient();
        client.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.frame.setVisible(true);
        client.run();
    }
    
}
