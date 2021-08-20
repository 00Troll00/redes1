/********************************************************************
 * Author: Alan Bonfim Santos
 * Registration: 201911912
 * Initial date: 15/08/2021 13:28
 * Last update: 19/08/2021 22:13
 * Name: ReceiverEnlaceLayer.java
 * Function: Simulates the enlace layer of the receiver
 *******************************************************************/

// I will separete in a ArrayList of frames in the "trabalho03"

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
    int cont = 0;
    String fluxOfBits = "";
    String temp = ""; //this string keeps a sequence that can be the flag
    //desmounting the frames
    for(int bit: framedBits){
      if(bit == 0){
        if(cont == 0){
          temp += bit;
          cont++;
        }
        else if(cont == 6){//found a bit inserted and don't added it to the flux
          fluxOfBits += temp;
          temp = "";
          cont = 0;
        }
        else if(cont == 7){//found a flag
          //hmmmmmmmm gambiarrazinha
          // I never implemented a automato, so there is a bug that adds the first 0 off the flag, this part "resolves" it
          try{
            fluxOfBits = fluxOfBits.substring(0, (fluxOfBits.length()-1));
          } catch (IndexOutOfBoundsException e) { }
          //the try catch is because the first part is a flag, and the String will be of size 0
          temp = "";
          cont = 0;
        } 
        else{//not a inserted bit, so just add it to the flux
          temp += bit;
          fluxOfBits += temp;
          temp = "";
          cont = 1;//already counted a 0
        }
      }//end if
      else{
        temp += bit;
        cont++;
      }//end else
    }//end for

    //transforming the string of bits in a int arrray'
    int[] bits = new int[fluxOfBits.length()];
    for(int i=0; i<bits.length; i++){
      if(fluxOfBits.charAt(i) == '0')
        bits[i] = 0;
      else
        bits[i] = 1;
    }
    
    //transforms from a int array of binaryes in a int array of ascii codes
    int[] asciiMessage = binaryToAscii(bits);
    String message = "";
    
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

  private static int[] binaryToAscii(int[] binaryArray) {
    int position = 0;
    int asciiPosition = 0;//position in the asciiArray
    int[] bitsSequence = new int[8];// a sequence of 8 bits
    int[] asciiMessage = new int[binaryArray.length/8];
    //transforming from binary to ascii
    for(int bit : binaryArray){
      bitsSequence[position] = bit;
      position++;
      if(position>7){
        asciiMessage[asciiPosition] = Convert.binaryToDecimal(bitsSequence);
        asciiPosition++;
        position=0;
      }
    }//end for
    return asciiMessage;
  }//end binaryToAscii
}
