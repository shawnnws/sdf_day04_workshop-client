package workshop04;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Console;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class App 
{
    public static void main( String[] args ) throws NumberFormatException, UnknownHostException, IOException
    {
        String serverHost = args[0];
        String serverPort = args[1];

        //Establish connection to server.
        Socket socket = new Socket(serverHost, Integer.parseInt(serverPort));

        //Setup console to take in client input.
        Console cons = System.console();
        String input = "";
        String receivedMessage = "";

        try (InputStream is = socket.getInputStream()) {
            BufferedInputStream bis = new BufferedInputStream(is);
            DataInputStream dis = new DataInputStream(bis);

            try (OutputStream os = socket.getOutputStream()) {
                BufferedOutputStream bos = new BufferedOutputStream(os);
                DataOutputStream dos = new DataOutputStream(bos);

                while (!input.equals("close")) {
                    input = cons.readLine("Enter a command: ");

                    //Send client input through communication tunnel and flush dos.
                    dos.writeUTF(input);
                    dos.flush();

                    //Reading data from server.
                    receivedMessage = dis.readUTF();
                    System.out.println(receivedMessage); 

                }
                dos.close();
                bos.close();
                os.close();
            }
            catch (EOFException ex) {

            }
            dis.close();
            bis.close();
            is.close();
        }
        catch (EOFException ex) {

        }
    }
}
