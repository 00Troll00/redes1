package receiver;

import javafx.scene.control.TextArea;

public class ReceiverApplication {
  private TextArea textArea;

  public ReceiverApplication(TextArea textArea) {
    this.textArea = textArea;
  }

  public void receiver(int[] asciiMessage){
    System.out.println("5");
    String message = "";
    char temp;
    //transforms from ascii array to String
    for(int i=0; i<asciiMessage.length; i++){
      temp = (char) asciiMessage[i];
      message += temp; 
    }
    textArea.setText(message);
  }
}
