/********************************************************************
 * Author: Alan Bonfim Santos
 * Registration: 201911912
 * Initial date: 30/07/2021 21:53
 * Last update: 19/08/2021 20:48
 * Name: TransmitterEnlaceLayer.java
 * Function: simulates the transmitter's enlace layer
 *******************************************************************/
package transmitter;

import java.util.Random;

import controllers.MainController;
import javafx.scene.control.TextArea;
import util.Convert;

public class TransmitterEnlaceLayer{
  private static TextArea enlaceTextArea;

  public static void enquadramento(String message, MainController controller){
    enlaceTextArea = controller.getTrasmitterEnlaceTextArea();
    controller.getTransmitterTabPane().getSelectionModel().select(
      controller.getTransmitterTabPane().getTabs().get(0)
    );

    int frame[] = null;
    switch(controller.getFrameWorkTpe()){
      case 0://character counting
        frame = framing(message.toCharArray());
        break;
      case 1://character stufing
        frame = characterStufing(message.toCharArray());
        break;
      case 2:
        frame = bitStufing(message.toCharArray());
        break;
      case 3:
        frame = phisicalLayerViolation(message.toCharArray());
        break;
    }//end switch

    //sending the ascii codes to the phical layer
    sendToPhisicalLayer(frame, message.toCharArray(), controller);
  }//end enquadramento

  private static int[] framing(char[] message){
    //calculate the size of the new array and the size of the frames
    int[] sizeFrames = new int[message.length];
    int messageSize = message.length;
    int position = 0;
    Random random = new Random(); //to generates a random number
    
    while(messageSize>4){
      sizeFrames[position] = random.nextInt(4) + 1;;//generates a number between 1 and 4
      messageSize-= sizeFrames[position];
      position++;
    }
    sizeFrames[position] = messageSize;//defines the size of the last frame
    position++;
    
    char[] framedMessage = new char[message.length + position];
    //building the message with the conts
    int countCharPosition = 0;//counts the position that has the bytes added
    int messagePosition = 0;

    for(int i=0; i<position; i++){
      framedMessage[countCharPosition] = (char) (sizeFrames[i] + 48 + 1);//converting to the number char + 1 (the is to includes the count byte)
      countCharPosition++;
      for(int j=countCharPosition; j<(countCharPosition+sizeFrames[i]);j++){
        framedMessage[j] = message[messagePosition];
        messagePosition++;
      }
      countCharPosition += sizeFrames[i];
    }//end for

    addToTextArea(framedMessage);
    return Convert.charArrayToAscii(framedMessage);
  }//end framing
  
  private static int[] characterStufing(char[] message){
    final char FLAG_START = 'S';
    final char FLAG_END = 'E';
    final char ESC = '|';
    final int FRAME_SIZE = 2;
    String framedMessage = "";
    int cont = 0;
    for(int i=0; i<message.length; i++){
      if(cont == FRAME_SIZE){
        framedMessage += FLAG_END;
        cont=0;
      }
      if(cont == 0){
        framedMessage+= FLAG_START;
      }
      if(message[i] == FLAG_START || message[i] == FLAG_END || message[i] == ESC)
        framedMessage += ESC;
      framedMessage += message[i];

      cont++;
    }
    framedMessage += FLAG_END;//adds the last flag

    char[] charArrayMessage = framedMessage.toCharArray();
    addToTextArea(charArrayMessage);
    return Convert.charArrayToAscii(charArrayMessage);
  }//end characterStufing

  private static int[] bitStufing(char[] message){
    //transforming from ascii in a String of binary
    String binaryMessage = "";
    String binary = "";
    for(char c: message){
      binary = Integer.toBinaryString(c);
      while(binary.length() != 8)
        binary = "0" + binary;
      binaryMessage += binary;
    }
    
    //adding the bit
    int cont = 0;
    String bitStufingMessage = "";
    for(int i=0; i<binaryMessage.length(); i++){
      bitStufingMessage += binaryMessage.charAt(i);
      if(binaryMessage.charAt(i) == '1')
        cont++;
      else
        cont = 0;
      if(cont == 5)
        bitStufingMessage += '0';
    }//end for

    cont = 0; //reusing the cont varible
    final String FLAG = "01111110";
    final int FRAME_SIZE = 50; //a frame has 50 bits
    String framedMessage = FLAG; //adds the start flag
    for(int i=0; i<bitStufingMessage.length(); i++){
      framedMessage += bitStufingMessage.charAt(i);
      cont++;
      if(cont == FRAME_SIZE){
        framedMessage += FLAG; //adds a flag
        cont = 0; //reset the cont
      }
    }
    //if the cont is equal to 0 it means that a final flag has already been added
    if(cont != 0)
      framedMessage += FLAG; //adds the end flag

    //adding the flag


    addToTextArea(framedMessage.toCharArray());

    //transforming in int[]
    int[] binaryArray = new int[framedMessage.length()];
    String bit = "";
    for(int i = 0; i<binaryArray.length; i++){
      bit += framedMessage.charAt(i);
      binaryArray[i] = Integer.parseInt(bit);
      bit = "";
    }

    return binaryArray;
  }

  private static int[] phisicalLayerViolation(char[] message) {
    //just transform the characters in int
    int[] ascii = new int[message.length];
    for(int i=0; i<message.length; i++)
      ascii[i] = message[i];
    return ascii;
  }

  private static void addToTextArea(char[] message){
    enlaceTextArea.setText(String.valueOf(message));
  }

  private static void sendToPhisicalLayer(int[] asciiMessage, char[] message, MainController controller){
    //changing to the ascii tab
    controller.getTransmitterTabPane().getSelectionModel().select(
      controller.getTransmitterTabPane().getTabs().get(1)
    );

    new Thread( () -> {
      //I did this so I could show the inforation in the TextArea, whitout froze the JavaFX thread
      if(controller.getFrameWorkTpe() != 2){
      //transforms from char in int, with the ascii code
        for(int i=0; i<asciiMessage.length; i++){
          //adds the ascii code to the TextArea
          addAsciiToTextArea(controller.getTrasmitterAsciiTextArea(), (char) asciiMessage[i], asciiMessage[i]);
        }
      }
      else{
        for(int i=0; i<message.length; i++){
          //adds the ascii code to the TextArea
          addAsciiToTextArea(controller.getTrasmitterAsciiTextArea(), message[i], message[i]);
        }
      }
      //sends the message to the phisical layer      
      TransmitterPhisicalLayer.receive(asciiMessage, controller);
    }).start();
  }//end sendToPhisicalLayer

  public static void addAsciiToTextArea(TextArea asciiTextArea, char charactere, int number){
    try {
      Thread.sleep(100);
    } catch (InterruptedException e) { }
    asciiTextArea.setText(asciiTextArea.getText() + charactere + " = " + number + "\n");
    asciiTextArea.setScrollTop(9999);//make the scroll go down if the texts exceeds the size of the TextArea
  }//end addAscii
}