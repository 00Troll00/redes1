/********************************************************************
 * Author: Alan Bonfim Santos
 * Registration: 201911912
 * Initial date: 30/07/2021 22:36
 * Last update: 31/07/2021 20:01
 * Name: ReceiverApplication.java
 * Function: Simulates the receiver application layer
 *******************************************************************/
package receiver;

import javafx.scene.control.TextArea;

public class ReceiverApplication {
  private TextArea textArea;

  public ReceiverApplication(TextArea textArea) {
    this.textArea = textArea;
  }

  public void receive(int[] asciiMessage){
    String message = "";
    char temp;
    //transforms from ascii array to String
    for(int i=0; i<asciiMessage.length; i++){
      temp = (char) asciiMessage[i];
      message += temp; 
    }
    textArea.setText(message);
  }//end receive
}
