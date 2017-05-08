package ch.imedias.rsccfx.model;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by jp on 08/05/17.
 */
public class Rscccfp {

  private final Rscc model;

  private Socket connectionSocket;
  private DataOutputStream outputStream;
  private BufferedReader inputStream;

  public Rscccfp(Rscc model) {
    this.model = model;

  }


  public void startRscccfpServer() {

    ServerSocket serverSocket;
    try {
      serverSocket = new ServerSocket(5900);
      connectionSocket = serverSocket.accept();

      inputStream = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
      outputStream = new DataOutputStream(connectionSocket.getOutputStream());

      sendSdp("TEST");
      receiveSdp();
      //do STUN Magic
      //wait for other StunStatus
      //send STUN ownStun result
      closeConnection();

//      clientSentence = inputStream.readLine();
//      capitalizedSentence = clientSentence.toUpperCase() + '\n';
//      outputStream.writeBytes(capitalizedSentence);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }


  public void startRscccfpClient(String host, int port) {
    try {
      connectionSocket = new Socket("127.0.0.1", 5900);

      outputStream = new DataOutputStream(connectionSocket.getOutputStream());
      inputStream = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));

      receiveSdp();
      sendSdp("TEST");
      //do STUN Magic
      //send STUN ownStun result
      //wait for other StunStatus

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void receiveSdp() {
    StringBuilder receivedSdp = new StringBuilder();
    try {
      if (inputStream.readLine().equals("sdpStart")) {
        String nextline = inputStream.readLine();
        while (!nextline.equals("sdpEnd")) {
          receivedSdp.append(nextline);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    System.out.println(receivedSdp.toString());
  }


  public void closeConnection() {
    try {
      connectionSocket.close();
      outputStream.close();
      inputStream.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void sendSdp(String sdpDump) {
    try {
      outputStream.writeBytes("sdpStart" + '\n');
      outputStream.writeBytes(sdpDump + '\n');
      outputStream.writeBytes("sdpEnd" + '\n');
      outputStream.flush();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }

  public void sendResult() {


  }

}
