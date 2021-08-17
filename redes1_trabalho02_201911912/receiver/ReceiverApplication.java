/********************************************************************
 * Author: Alan Bonfim Santos
 * Registration: 201911912
 * Initial date: 30/07/2021 22:36
 * Last update: 15/08/2021 13:49
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

  public void receive(String message){
    textArea.setText(message);
  }//end receive
}
