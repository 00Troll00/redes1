/********************************************************************
 * Author: Alan Bonfim Santos
 * Registration: 201911912
 * Initial date: 30/07/2021 21:53
 * Last update: 30/07/2021 
 * Name: MainContrellor.java
 * Function: 
 *******************************************************************/
package transmitter;

import controllers.MainController;
import javafx.scene.control.TextArea;

public class TransmitterApplication {
  private TextArea textArea;

  public TransmitterApplication(TextArea textArea){
    this.textArea = textArea;
  }

  //sends the mensage to the phisical layer
  public void sendToPhisicalLayer(MainController controller, int codificationType){
    TextArea asciiTextArea = controller.getTrasmitterAsciiTextArea();

    char[] message = textArea.getText().toCharArray();
    int[] asciiMessage = new int[message.length];
    //transforms from char in int, with the ascii code
    for(int i=0; i<message.length; i++){
      asciiMessage[i] = message[i];
      //adds the ascii code to the TextArea
      asciiTextArea.setText(asciiTextArea.getText() + message[i] + " = " + asciiMessage[i] + "\n");
    }
    
    //sends the message to the phisical layer
    TransmitterPhisicalLayer.receiveFromApplicationLayer(asciiMessage, codificationType, controller);
  }
}
