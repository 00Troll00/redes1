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
        asciiMessage = differentialManchester(bitsMessage);
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

  private static int[] differentialManchester(Object bitsMessage) {
    return null;
  }

  private static int[] manchester(Object bitsMessage) {
    return null;
  }
}
