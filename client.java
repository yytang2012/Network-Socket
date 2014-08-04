package net;
 
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;
 
public class DataSocket {
    public static void main(String[] args) throws IOException {
        File img1 = new File("C:/Documents and Settings/dell/My Documents/My Pictures/002.jpg");
        File img2 = new File("C:/Documents and Settings/dell/My Documents/My Pictures/03342240.jpg");
 
        Socket socket = new Socket("127.0.0.1", 4848);
        String header1 = "a.jpg;" + img1.length();
        String header2 = "b.jpg;" + img2.length();
 
        BufferedOutputStream out = new BufferedOutputStream(socket.getOutputStream());
 
        FileInputStream in = new FileInputStream(img1);
        FileInputStream in2 = new FileInputStream(img2);
        byte[] buffer = new byte[8192];
 
        int readBytes = -1;
 
        out.write(header1.getBytes());
        out.write("\r".getBytes());
        out.flush();
         
        while ((readBytes = in.read(buffer)) != -1) {
            out.write(buffer, 0, readBytes);
        }
        out.flush();
         
        out.write(header2.getBytes());
        out.write("\r".getBytes());
        out.flush();
         
        while ((readBytes = in2.read(buffer)) != -1) {
            out.write(buffer, 0, readBytes);
        }
        out.flush();
         
        in.close();
        in2.close();
        out.flush();
        out.close();
    }
}

