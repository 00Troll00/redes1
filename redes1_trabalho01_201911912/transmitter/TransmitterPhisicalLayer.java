/********************************************************************
 * Author: Alan Bonfim Santos
 * Registration: 201911912
 * Initial date: 30/07/2021 21:55
 * Last update: 30/07/2021 
 * Name: MainContrellor.java
 * Function: 
 *******************************************************************/
package transmitter;

import controllers.MainController;
import global.Comunication;

public class TransmitterPhisicalLayer {
  
  //receives the message from the application layer
  public static void receiveFromApplicationLayer(int[] asciiMessage, int codificationType, MainController controller){
    int fluxOfBits [] = null;

    switch(codificationType){
      //binary codification
      case 0:
        fluxOfBits = binary(asciiMessage);
        break;
      //Manchester codification
      case 1:
        fluxOfBits = manchester(binary(asciiMessage));
        break;
      //Differential Manchester codification
      case 2:
        fluxOfBits = differentialManchester(binary(asciiMessage));
        break;
    }

    //calls the methood comunicate in the Comunication class
    Comunication.comunicate(fluxOfBits, codificationType, controller);
  }

  public static int[] binary(int[] asciiMessage){
    int[] binaryMessage = new int[asciiMessage.length];

    //transforms from ascii to binary string and then transforms in int
    for(int i = 0; i<asciiMessage.length; i++)
      binaryMessage[i] = Integer.parseInt(Integer.toBinaryString(asciiMessage[i]));
    
    return binaryMessage;
  }

  public static int[] manchester(int[] binaryMesage){
    return null;
  }

  public static int[] differentialManchester(int[] binaryMesage){
    return null;
  }
}
