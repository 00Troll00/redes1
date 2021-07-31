package receiver;

import javafx.scene.control.TextField;

public class ReceiverApplication {
  private TextField textField;

  public ReceiverApplication(TextField textField) {
    this.textField = textField;
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

    textField.setText(message);
  }
}
