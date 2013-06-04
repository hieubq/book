package quanghieu.searchingbookclient;

import android.util.Log;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class TCPClient {

	private String serverMessage;
	public static final String SERVERIP = "192.168.56.1";
	public static final int SERVERPORT = 4649;
	private OnMessageReceived mMessageListener = null;
	private boolean mRun = false;
	
	PrintWriter out;
	BufferedReader in;
	
	/**
     *  Constructor of the class. OnMessagedReceived listens for the messages received from server
     */
    public TCPClient(OnMessageReceived listener) {
        mMessageListener = listener;
    }
 
    /**
     * Sends the message entered by client to the server
     * @param message text entered by client
     */
    public void sendMessage(String message){
        if (out != null && !out.checkError()) {
            out.println(message);
            out.flush();
        }
    }
 
    public void stopClient(){
        mRun = false;
    }
    
    public void run() {
        mRun = true;
        try {
            // khoi tao IP websocket server
            InetAddress serverAddr = InetAddress.getByName(SERVERIP);
            Log.e("TCP Client", "C: Connecting...");
            // khoi tao 1 socket de ket noi den serverss
            Socket socket = new Socket(serverAddr, SERVERPORT);
 
            try {
                // luong de gui du lieu den server
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                Log.e("TCP Client", "C: Sent.");
                Log.e("TCP Client", "C: Done.");
 
                // luong de nhan du lieu tu server gui den
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
 
                //in this while the client listens for the messages sent by the server
                while (mRun) {
                    serverMessage = in.readLine();
 
                    if (serverMessage != null && mMessageListener != null) {
                        //call the method messageReceived from MyActivity class
                        mMessageListener.messageReceived(serverMessage);
                    }
                    serverMessage = null;
                }
                Log.e("RESPONSE FROM SERVER", "S: Received Message: '" + serverMessage + "'");
            } catch (Exception e) {
                Log.e("TCP", "S: Error", e);
            } finally {
                // khi ket thuc phai dong socket lai, huy ket noi den server
                socket.close();
            }
        } catch (Exception e) {
            Log.e("TCP", "C: Error", e);
        }
    }
	
	// dinh nghia 1 interface. co phuong thuc messageReceived(String message) se duoc hien thuc trong 1 activity
    // dungf trong class asynckTask doInBackground
    public interface OnMessageReceived {
        public void messageReceived(String message);
    }
}
