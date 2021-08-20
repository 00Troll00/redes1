/********************************************************************
 * Author: Alan Bonfim Santos
 * Registration: 201911912
 * Initial date: 30/07/2021 22:29
 * Last update: 16/08/2021 21:28
 * Name: ReceiverPhisicalLayer.java
 * Function: Simulates the phisical layer of the receiver
 *******************************************************************/
package receiver;

import controllers.MainController;
import javafx.scene.control.TextArea;
import util.Convert;

//version 0.3.0 (third version)
public class ReceiverPhisicalLayer {
  public static TextArea asciiTextArea;

  public static void receive(int[] bitsMessage, MainController controller){
    asciiTextArea = controller.getReceiverAsciiTextArea();

    //changing to the ascii tab
    controller.getReceiverTabpane().getSelectionModel().select(
      controller.getReceiverTabpane().getTabs().get(1)
    );

    //take off the flags, if the violation was used
    if(controller.getFrameWorkTpe() == 3)
      bitsMessage = resolveViolation(bitsMessage);

    int binaryArray [] = null;

    switch(controller.getCodificationType()){
      //binary decodification
      case 0:
        binaryArray = binary(bitsMessage);
        break;
      //Manchester decodification
      case 1:
        binaryArray = manchester(bitsMessage);
        break;
      //Differential Manchester decodification
      case 2:
        binaryArray = differentialManchester(bitsMessage);
        break;
    }
    //transforming from binary in to ascii
    int[] asciiMessage = binaryToAscii(binaryArray, controller.getFrameWorkTpe());

    //adding the ascii code to the TextArea
    if(controller.getFrameWorkTpe()!=2)
      printAsciiCodeTextArea(asciiMessage);

    //sends the ascii codes to the enlace layer
    ReceiverEnlaceLayer.enquadramento(asciiMessage, controller);;
  }//end receive

  private static int[] resolveViolation(int[] violatedBits) {
    final int FRAME_SIZE = 30;
    //starts with a size that is equal to the valid information
    int[] bits = new int[violatedBits.length - ((violatedBits.length/FRAME_SIZE + 2)*2)];
    int position = 0;//positon in the bits array
    for(int i = 0; i<violatedBits.length; i+=2){
      if( !(violatedBits[i] == violatedBits[i+1] && violatedBits[i] == 1)){
        bits[position] = violatedBits[i];
        bits[position+1] = violatedBits[i+1];
        position += 2;
      }
    }
    
    return bits;
  }//end resolveViolation

  private static void printAsciiCodeTextArea(int[] asciiMessage) {
    for(int ascii : asciiMessage){
      asciiTextArea.setText(asciiTextArea.getText() + ascii + " = " + ((char) ascii) + "\n");
      asciiTextArea.setScrollTop(9999);
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) { }
    }
  }//end printAsciiCodeTextArea

  private static int[] binaryToAscii(int[] binaryArray, int frameWorkTpe) {
    if(frameWorkTpe == 2)
      return binaryArray; //the enlace layer will take care of this in the bit stufing
  
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

  private static int[] binary(int[] binaryArray){
    return binaryArray;//already in binary
  }

  private static int[] manchester(int[] manchester){
    int[] binaryArray = new int[manchester.length/2];
    //transforms from manchester in binary
    for(int i=0; i<binaryArray.length; i++){
      if(manchester[2*i] == 1 && manchester[2*i+1] == 0)
        binaryArray[i] = 1;
      else 
        if(manchester[2*i] == 0 && manchester[2*i+1] == 1)
          binaryArray[i] = 0;
        else
          System.out.println("ERROR");
    }

    return binaryArray;
  }

  private static int[] differentialManchester(int[] differentialManchester){
    int[] binaryArray = new int[differentialManchester.length/2];

    //the start case
    int position = 2;//position in the diferretial manchester array
    if(differentialManchester[0] == 0 && differentialManchester[1] == 1){
      binaryArray[0] = 0;
    }
    if(differentialManchester[0] == 1 && differentialManchester[1] == 0){
      binaryArray[0] = 0;
    }
    //transforming from differential manchester in binary
    for(int i=1; i<binaryArray.length; i++){
      if( differentialManchester[position] == differentialManchester[position-2] 
          && differentialManchester[position+1] == differentialManchester[position-1])
        binaryArray[i] = 0;
      else
        if( differentialManchester[position] == differentialManchester[position-1] 
            && differentialManchester[position+1] == differentialManchester[position-2])
          binaryArray[i] = 1;
        else
          System.out.println("ERROR");
      position+=2;
    }
    return binaryArray;
  }

  /**
   * Old phisical layer, the second, version 0.2.0
   */

  /*
  private static int[] binary(int[] bitsMessage, int frameWorkTpe) {
    if(frameWorkTpe == 2)
      return bitsMessage;//sends the binary sequence, to be treated in enlace layer

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

  private static int[] manchester(int[] bitsMessage, int frameWorkTpe) {
    if(frameWorkTpe == 2){
      int[] binaryBits = new int[bitsMessage.length /2];
      //transforms from manchester in binary
      for(int i=0; i<binaryBits.length; i++){
        if(bitsMessage[2*i] == 1 && bitsMessage[2*i+1] == 0)
          binaryBits[i] = 1;
        else 
          if(bitsMessage[2*i] == 0 && bitsMessage[2*i+1] == 1)
            binaryBits[i] = 0;
          else
            System.out.println("ERROR");
      }
      return binaryBits;
    }//end if
    else{
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
    }
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
  }//end differentialManchester

  private static void addAsciiCodeToTextArea(int ascii){
    try {
      Thread.sleep(100);
    } catch (InterruptedException e) { }
    asciiTextArea.setText(asciiTextArea.getText() + ascii + " = " + (char) ascii + "\n");
    asciiTextArea.setScrollTop(9999);//make the scroll go down if the texts exceeds the size of the TextArea
  }//end addAsciiCodeToTextArea
  */
  /**********************************************
   * The code below is not used, it was my firt
   * attempt, since I miss understood the problem
   * and how it showed be done
   **********************************************/

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
