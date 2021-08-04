/********************************************************************
 * Author: Alan Bonfim Santos
 * Registration: 201911912
 * Initial date: 30/07/2021 22:29
 * Last update: 30/07/2021 
 * Name: MainContrellor.java
 * Function: 
 *******************************************************************/
package receiver;

import controllers.MainController;
import javafx.scene.control.TextArea;
import util.Convert;

public class ReceiverPhisicalLayer {
  public static TextArea asciiTextArea;

  public static void receives(int[] bitsMessage, int codificationType, ReceiverApplication receiver, MainController controller){
    asciiTextArea = controller.getReceiverAsciiTextArea();

    //changing to the ascii tab
    controller.getReceiverTabpane().getSelectionModel().select(
      controller.getReceiverTabpane().getTabs().get(1)
    );

    int asciiMessage [] = null;

    switch(codificationType){
      //binary decodification
      case 0:
        asciiMessage = binary(bitsMessage);
        break;
      //Manchester decodification
      case 1:
        asciiMessage = manchester(bitsMessage);
        break;
      //Differential Manchester decodification
      case 2:
        asciiMessage = differentialManchester(bitsMessage);
        break;
    }
    controller.getSendButton().setDisable(false);//enable the button
    receiver.receiver(asciiMessage);
  }

  private static int[] binary(int[] bitsMessage) {
    int[] asciiMessage = new int[bitsMessage.length/8];
    int position = 0; //the position of the bit in the array
    int stopValue; //the value that the positios must reach to stop the while
    String binaryString;
    for(int i=0; i<asciiMessage.length; i++){
      binaryString = "";
      stopValue = 8*(i+1);
      while(position != stopValue){
        binaryString += bitsMessage[position];
        position++;      
      }
      asciiMessage[i] = Convert.binaryToDecimal(Integer.parseInt(binaryString));
      //adds the charactere into the TextArea int the interface
      addAsciiCodeToTextArea(asciiMessage[i]);
    }//end for
    return asciiMessage;
  }//end binary

  private static int[] manchester(int[] bitsMessage) {
    int[] asciiMessage = new int[bitsMessage.length/16];
    int position = 0; //the position of the bit in the array
    int stopValue; //the value that the positios must reach to stop the while
    String manchesterString;
    for(int i=0; i<asciiMessage.length; i++){
      manchesterString = "";
      stopValue = 16*(i+1);
      while(position != stopValue){
        manchesterString += bitsMessage[position];
        position++;      
      }
      asciiMessage[i] = Convert.manchesterToDecimal(manchesterString);
      //adds the charactere into the TextArea int the interface
      addAsciiCodeToTextArea(asciiMessage[i]);
    }//end for
    return asciiMessage;
  }//end manchester

  private static int[] differentialManchester(int[] bitsMessage){
    int[] asciiMessage = new int[bitsMessage.length/16];
    int position = 0; //the position of the bit in the array
    int stopValue; //the value that the positios must reach to stop the while
    String binary;

    for(int i=0; i<asciiMessage.length; i++){
      binary = "";
      //jumps the 0's in the begin of the code, since some bytes have 0's in the begin
      while(bitsMessage[position] == bitsMessage[position+1])
        position+=2;

      binary += "1"; //the sequence will always starts with 1
      position += 2; //jumps the first 2 bits of the code

      stopValue = 16*(i+1);
      while(position != stopValue){
        //this means that don't occur a transition
        if(bitsMessage[position] == bitsMessage[position-2])
          binary += "0";
        else
          binary += "1";
        position += 2;
      }
      asciiMessage[i] = Convert.binaryToDecimal(Integer.parseInt(binary));
      //adds the charactere into the TextArea int the interface
      addAsciiCodeToTextArea(asciiMessage[i]);
    }
    return asciiMessage;
  }

  private static void addAsciiCodeToTextArea(int ascii){
    try {
      Thread.sleep(100);
    } catch (InterruptedException e) { }
    asciiTextArea.setText(asciiTextArea.getText() + (char) ascii + " = " + ascii + "\n");
    asciiTextArea.setScrollTop(9999);//make the scroll go down if the texts exceeds the size of the TextArea
  }

  /*
  public static void receives(int[] bitsMessage, String[] bitsMessageString, int codificationType, ReceiverApplication receiver){
    int asciiMessage [] = null;

    switch(codificationType){
      //binary decodification
      case 0:
        asciiMessage = binary(bitsMessage);
        break;
      //Manchester decodification
      case 1:
        asciiMessage = manchester(bitsMessageString);
        break;
      //Differential Manchester decodification
      case 2:
        //asciiMessage = differentialManchester(bitsMessage);
        break;
    }
    receiver.receiver(asciiMessage);
  }

  private static int[] binary(int[] bitsMessage) {
    int[] asciiMessage = new int[bitsMessage.length];
    //convert from binary to ascii code using the function binaryToDecimal
    for(int i=0; i<asciiMessage.length; i++)
      asciiMessage[i] = Convert.binaryToDecimal(bitsMessage[i]);
    return asciiMessage;
  }

  private static int[] manchester(String[] manchesterArray) {
    int[] asciiMessage = new int[manchesterArray.length];
    for(int i=0; i<manchesterArray.length; i++){
      asciiMessage[i] = Convert.manchesterToDecimal(manchesterArray[i]);
    }
    return asciiMessage;
  }

  private static int[] differentialManchester(Object bitsMessage) {
    return null;
  }

   /*
  private static int[] manchester(int[] manchester) {
    int[] asciiMessage = new int[manchester.length/2];
    String temp;
    //converts from manchester to ascii using the function manchesterToDecimal
    for(int i=0; i<asciiMessage.length; i++){
      temp = String.valueOf(manchester[i*2 +1]);
      if(temp.length() % 2 == 0)
        asciiMessage[i] = Convert.manchesterToDecimal(String.valueOf(manchester[i*2]) + temp);
      else
        asciiMessage[i] = Convert.manchesterToDecimal(String.valueOf(manchester[i*2]) + temp.substring(1));
    }
    return asciiMessage;
  }*/
}
