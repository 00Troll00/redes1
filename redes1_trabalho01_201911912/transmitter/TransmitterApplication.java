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
  private TextArea asciiTextArea;

  public TransmitterApplication(TextArea textArea){
    this.textArea = textArea;
  }

  //sends the mensage to the phisical layer
  public void sendToPhisicalLayer(MainController controller, int codificationType){
    asciiTextArea = controller.getTrasmitterAsciiTextArea();

    //changing to the ascii tab
    controller.getTransmitterTabPane().getSelectionModel().select(
      controller.getTransmitterTabPane().getTabs().get(0)
    );

    new Thread( () -> {
      char[] message = textArea.getText().toCharArray();
      int[] asciiMessage = new int[message.length];
      //transforms from char in int, with the ascii code
      for(int i=0; i<message.length; i++){
        asciiMessage[i] = message[i];
        //adds the ascii code to the TextArea
        addAscii(message[i], asciiMessage[i]);
      }
      
      //sends the message to the phisical layer
      TransmitterPhisicalLayer.receiveFromApplicationLayer(asciiMessage, codificationType, controller);
    }).start();
  }

  public void addAscii(char charactere, int number){
    try {
      Thread.sleep(100);
    } catch (InterruptedException e) { }
    asciiTextArea.setText(asciiTextArea.getText() + charactere + " = " + number + "\n");
    asciiTextArea.setScrollTop(9999);//make the scroll go down if the texts exceeds the size of the TextArea
  }
}
