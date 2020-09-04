import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import static java.lang.Integer.parseInt;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

 
public class ChatServer {
    
  

    
    private static final int PORT = 9000;
    private static HashSet<String> names = new HashSet<String>();
    private static HashSet<PrintWriter> stream = new HashSet<PrintWriter>();

   
    public static void main(String[] args) throws Exception
    {
        ChatServer chatServer = new ChatServer();
        ServerSocket listen = new ServerSocket(PORT);
        System.out.println("Server On.");  
        try {
            while (true)
            {
                new Foxchat(listen.accept()).start();
            }
        }
        
        finally
        { 
            listen.close();
        }
         
        
    }
    
     public ChatServer()  {
        JFrame frame = new JFrame("The Server is now running!");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(700, 200));
        frame.pack();
        frame.setVisible(true);
        JPanel panel = new JPanel();
        JLabel label = new JLabel("Welcome");
        JButton startButton = new JButton("Start");
        startButton.addActionListener(new ActionListener()
        {
        public void actionPerformed(ActionEvent e)
        {
        
            try {
                Runtime runTime = Runtime.getRuntime();
                Process process = runTime.exec("java -classpath Users/mark/NetBeansProjects/FoxChat/src/ChatClient.java");
            }
            catch (IOException ex) {
                Logger.getLogger(ChatServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        });
        
        frame.add(label);
        frame.add(startButton);
	
         
         
     }
    
    public static String toPigLatin(String x)
 {
    if (x == null)
    {
        return null;
    }
    String[] a = x.toLowerCase().split(" ");
    String piggy = "";
    int i = 0;
    while (i < a.length) {
        piggy = "bcdfghjklmnpqrstvwxz".contains("" + a[i].charAt(0)) ? String.valueOf(piggy) + a[i].substring(1, a[i].length()) + a[i].charAt(0) + "ay" : String.valueOf(piggy) + a[i] + "yay";
        if (i != a.length - 1)
            piggy = String.valueOf(piggy) + " ";
        ++i;
    }
    return piggy;
}

    
    private static class Foxchat extends Thread {
        private String name;
        private Socket socket;
        private BufferedReader in;
        private PrintWriter out;

        
        public Foxchat(Socket socket)
        {
            this.socket = socket;
        }

        
        public void run() {
            try {

                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);

              
                while (true)
                {
                    out.println("SUBMITCWID");
                    name = in.readLine();
                    if (name == null)
                    {
                        return;
                    }
                    if (parseInt(name) < 0)
                    {
                        name = "guest";
                        return;
                    }
                    synchronized (names)
                    {
                        if (!names.contains(name))
                        {
                            names.add(name);
                            break;
                        }
                    }
                }
                  
                out.println("CWIDACCEPTED");
                stream.add(out);

                
                while (true)
                {
                    String input = in.readLine();
                    if (input == null)
                    {
                        return;
                    }
                    for (PrintWriter writer : stream)
                    {
                        writer.println("MESSAGE " + name + ": " + toPigLatin(input));
                    }
                }
            }
            catch (IOException e)
            {
                System.out.println(e);
            }
            
                
        }
    }
}
    
