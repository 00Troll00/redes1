/********************************************************************
 * Author: Alan Bonfim Santos
 * Registration: 201911912
 * Initial date: 30/07/2021 22:29
 * Last update: 30/07/2021 
 * Name: MainContrellor.java
 * Function: 
 *******************************************************************/
package receiver;

import util.Convert;

public class ReceiverPhisicalLayer {
  public static void receives(int[] bitsMessage, int codificationType, ReceiverApplication receiver){
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
        //asciiMessage = differentialManchester(bitsMessage);
        break;
    }
    receiver.receiver(asciiMessage);
  }

  private static int[] binary(int[] bitsMessage) {
    int[] asciiMessage = new int[bitsMessage.length/8];
    int position = 0; //the position of the bit in the array
    String temp;
    for(int i=0; i<asciiMessage.length; i++){
      temp = "";
      while(position != (8*(i+1))){
        temp += bitsMessage[position];
        position++;      
      }
      asciiMessage[i] = Convert.binaryToDecimal(Integer.parseInt(temp));
    }//end for
    return asciiMessage;
  }

  private static int[] manchester(int[] bitsMessage) {
    return null;
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
