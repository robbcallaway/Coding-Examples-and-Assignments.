package DataComProjectServ;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
public class Client implements Runnable{

    private Socket client;
    //handles reading input portion
    private BufferedReader in;
    //handles the output portion
    private PrintWriter out;
    //boolean variable to check whether done or not
    private boolean done;


    @Override
    public void run() {
        try{
            //use local host IP address 127.0.0.1 on the port 8888
            client = new Socket("127.0.0.1", 8888);

            out = new PrintWriter(client.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));

            InputHandler IH = new InputHandler();

            Thread t = new Thread(IH);
            t.start();
            String InMessage;
            //while InMessage = in and isn't empty(null) print it to the console
            while((InMessage = in.readLine()) != null){
                System.out.println(InMessage);
            }

        }catch(IOException e){
            shutdown(); 
        }
    }

    //shutdown function
    public void shutdown(){
        //set done's default to true
        done = true;
        try{
            in.close();
            out.close();
            //check to see if the client is still open, and close it if so
            if(!client.isClosed()){
                client.close();
            }
        }catch(IOException ex){
            //ignore
        }
    }

    class InputHandler implements Runnable{

        @Override
        public void run() {
            try{
                BufferedReader inReader = new BufferedReader(new InputStreamReader(System.in));
                while(!done){
                    //asks for user input
                    String message = inReader.readLine();
                    if(message.equals("/quit")){
                        inReader.close();
                        shutdown();
                    }
                    else{
                        out.println(message);
                    }
                }
                
            }
            catch(IOException e) {
                shutdown();
            }
        }
        
    }


    public static void main(String[] args) {
        Client c = new Client();
        c.run();
    }
    
}


