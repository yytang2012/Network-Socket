package net;
 
import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
 
public class DataSocketServer {
    final public static int DEFAULT_PORT = 4848;
    final public static String FILE_DIR = "D:/";
 
    public static void main(String[] args) {
        ServerSocket server = null;
        try {
            server = new ServerSocket(DEFAULT_PORT);
 
            while (true) {
                new Thread(new RequestProcessorTask(server.accept())).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    static class RequestProcessorTask implements Runnable {
        private Socket socket = null;
 
        public RequestProcessorTask(Socket socket) {
            this.socket = socket;
        }
 
        public void run() {
            try {
                boolean isEnd = false;
 
                BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
 
                while (!isEnd) {
                    int d = -1;
                    StringBuilder header = new StringBuilder();
 
                    while ((d = in.read()) != '\r') {
                        if (d == -1) {
                            isEnd = true;
                            break;
                        }
                        header.append((char) d);
                    }
                    if (!isEnd) {
                        String[] parms = header.toString().split(";");
 
                        FileOutputStream out = new FileOutputStream(FILE_DIR + parms[0]);
                        long size = Long.parseLong(parms[1]);
 /*
                        while (size > 0 && (d = in.read()) != -1) {
                            out.write(d);
                            size--;
                        }
 */						
                        byte[] byteStream = new byte[(int) size];	
                        int len = 0;  
                        while (len < size) {  
                            len += in.read(byteStream, len, (int) (size - len));                              
                        }
                        out.write(byteStream, 0, (int) size);
                        out.flush();
                        out.close();
                    }
                }
                in.close();
            } catch (IOException e) {
                throw new RuntimeException("获取客户端输入流失败", e);
            }
        }
    }
}

