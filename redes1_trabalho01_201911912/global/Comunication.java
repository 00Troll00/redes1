/********************************************************************
 * Author: Alan Bonfim Santos
 * Registration: 201911912
 * Initial date: 30/07/2021 21:57
 * Last update: 30/07/2021 
 * Name: MainContrellor.java
 * Function: 
 *******************************************************************/
package global;

import receiver.ReceiverApplication;
import receiver.ReceiverPhisicalLayer;

public class Comunication {
  public static void comunicate(int[] transmitterBits, int codificationType, ReceiverApplication receiver){
    System.out.println("3");
    int[] receiverBits = new int[transmitterBits.length];
    for(int i = 0; i<transmitterBits.length; i++){
      //change the interface
      receiverBits[i] = transmitterBits[i];
    }
    ReceiverPhisicalLayer.receives(receiverBits, codificationType, receiver);
  }
}
