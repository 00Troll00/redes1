/********************************************************************
 * Author: Alan Bonfim Santos
 * Registration: 201911912
 * Initial date: 15/08/2021 13:28
 * Last update: 17/08/2021 19:25
 * Name: ReceiverEnlaceLayer.java
 * Function: Simulates the enlace layer of the receiver
 *******************************************************************/
package receiver;

import controllers.MainController;
import javafx.scene.control.TextArea;
import util.Convert;

public class ReceiverEnlaceLayer {
  private static TextArea enlaceTextArea;

  public static void enquadramento(int[] asciiMessage, MainController controller){
    enlaceTextArea = controller.getReceiverEnlaceTextArea();
    controller.getReceiverTabpane().getSelectionModel().select(
      controller.getReceiverTabpane().getTabs().get(2)
    );
    //adding the message to the TextArea related to the enlace
    if(controller.getFrameWorkTpe() < 2)
      addsFramedMessageToTextArea(asciiMessage);
    
    String message = null;
    switch(controller.getFrameWorkTpe()){
      case 0://character counting
        message = framing(asciiMessage);
        break;
      case 1://character stufing
        message = characterStufing(asciiMessage);
        break;
      case 2:
        message = bitStufing(asciiMessage);
        break;
      case 3:
        message = phisicalLayerViolation(asciiMessage);
        break;
    }//end switch
    
    controller.getSendButton().setDisable(false);//enable the button
    controller.getTransmitterTextArea().setEditable(true);//enable the text area    

    if(controller.getFrameWorkTpe() != 2)
      controller.getReceiver().receive(message);
    else{
      //changing to the ascii tab
      controller.getReceiverTabpane().getSelectionModel().select(
        controller.getReceiverTabpane().getTabs().get(1)
      );
      //printing the ascii message, when it's the bit stufing option
      char[] asciiArray = message.toCharArray();
      for(int ascii : asciiArray){
        controller.getReceiverAsciiTextArea().setText(controller.getReceiverAsciiTextArea().getText() + ascii + " = " + ((char) ascii) + "\n");
        controller.getReceiverAsciiTextArea().setScrollTop(9999);
        try {
          Thread.sleep(100);
        } catch (InterruptedException e) { }
      }

      //changing to the enlace tab
      controller.getReceiverTabpane().getSelectionModel().select(
        controller.getReceiverTabpane().getTabs().get(2)
      );
      controller.getReceiver().receive(message);
    }//end else
  }//end enquadramento

  private static String phisicalLayerViolation(int[] asciiMessage) {
    //just transform in String
    String message = "";
    for(int ascii : asciiMessage)
      message += (char) ascii;
    return message;
  }//end phisical layer violation

  private static String framing(int[] asciiMessage){
    int cont;
    //int stopNumber = 0;
    String message = "";
    for(int i =0; i<asciiMessage.length; i++){
      cont = asciiToDigit((char) asciiMessage[i]) + i -1;//adds i to adjust the position in the array, and subtract 1, that is the count to the framing byte
      while(i<cont){
        i++;
        message += (char) asciiMessage[i];
      }
    }//end for
    
    return message;
  }//end framing

  private static int asciiToDigit(int ascii){
    return ascii - 48;
  }

  private static String characterStufing(int[] asciiMessage){
    //transforms from ascii to a String
    String framedMessage = "";
    for(int asciiCode : asciiMessage)
      framedMessage += (char) asciiCode;

    final char FLAG_START = 'S';
    final char FLAG_END = 'E';
    final char ESC = '|';
    String message = "";
    char charactere;
    for(int i=0; i<framedMessage.length(); i++){
      charactere = framedMessage.charAt(i);
      if(charactere != FLAG_START && charactere != FLAG_END){
        if(charactere == ESC){
          message += framedMessage.charAt(1+i);
          i++; //jumps a caractere
        }
        else
          message += charactere;
      }
    }//end for

    //System.out.println(message);
    return message;
  }//end characterStufing

  private static String bitStufing(int[] framedBits){
    //GUI related-----------------------------------------------------------------
    for(int bit : framedBits)
      enlaceTextArea.setText(enlaceTextArea.getText() + String.valueOf(bit));
    //----------------------------------------------------------------------------
    String message = "";
    int cont = 0;
    int position = 0;
    int asciiPosition = 0;
    int[] bitsSequence = new int[8];
    int[] asciiMessage = new int[framedBits.length/7];
    for(int i = 0; i<asciiMessage.length; i++)
      asciiMessage[i] = -1;//turns all numbers int -1 so I can know where the message ends
    
      //transforming in ascii
    for(int bit: framedBits){
      if(cont==5)
        cont = 0;
      else{
        if(bit == 1)
          cont++;
        if(bit == 0)
          cont = 0;
        bitsSequence[position] = bit;
        position++;
        if(position>7){
          asciiMessage[asciiPosition] = Convert.binaryToDecimal(bitsSequence);
          asciiPosition++;
          position=0;
        }
      }//end else
    }//end for
    
    //transforming the ascii codes in to the message
    for(int i = 0; i<asciiMessage.length; i++){
      if(asciiMessage[i] == -1)
        break;
      message += (char) asciiMessage[i];
    }
    return message;
  }

  private static void addsFramedMessageToTextArea(int[] asciiMessage){
    String framedMessage = "";
    for(int asciiCode : asciiMessage)
      framedMessage += (char) asciiCode;
    enlaceTextArea.setText(framedMessage);
  }
}
