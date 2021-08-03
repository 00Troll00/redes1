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
import javafx.scene.control.TextArea;

public class TransmitterPhisicalLayer {
  public static TextArea codificationTextArea;

  public static void receiveFromApplicationLayer(int[] asciiMessage, int codificationType, MainController controller){
    codificationTextArea = controller.getTransmitterCodificationTextArea();

    int[] fluxOfBits = null;
    switch(codificationType){
      //binary codification
      case 0:
      fluxOfBits = binary(asciiMessage);
        break;
      //Manchester codification
      case 1:
      fluxOfBits = manchester(asciiMessage);
        break;
      //Differential Manchester codification
      case 2:
        fluxOfBits = differentialManchester(asciiMessage);
        break;
    }
    Comunication.comunicate(fluxOfBits, codificationType, controller);
  }

  private static int[] binary(int[] asciiMessage) {
    int[] bits = new int[asciiMessage.length * 8];
    char[] temp;
    int position;
    for(int i=0; i<asciiMessage.length; i++){
      temp = Integer.toBinaryString(asciiMessage[i]).toCharArray();

      position = 8 * (1+i); //defines the last position, where a bit must be storage
      position--;
      for(int j=temp.length-1; j>=0; j--){
        bits[position] = Character.getNumericValue(temp[j]);
        position--;
      }//end for

      //------- Changes in the GUI ---------------------------------------------------------------
      position = i*8;//the position where the bit starts
      //adds the bits to the TextArea
      for(int j = position; j<position+8; j++){
        codificationTextArea.setText(codificationTextArea.getText() + bits[j]);
      }
      //adds a line
      codificationTextArea.setText(codificationTextArea.getText() + "\n");
      //------------------------------------------------------------------------------------------
    }//end for
    return bits;
  }

  private static int[] manchester(int[] asciiMessage) {
    int[] bits = new int[asciiMessage.length * 16];
    String binary;
    int position;
    for(int i=0; i<asciiMessage.length; i++){
      binary = Integer.toBinaryString(asciiMessage[i]);

      position = 16 * (1+i); //defines the last position, where a bit must be storage
      position--;
      for(int j = binary.length()-1; j>=0; j--){
        if(binary.charAt(j) == '1'){
          bits[position] = 0;
          bits[position-1] = 1;
        }
        else{
          bits[position] = 1;
          bits[position-1] = 0;
        }
        position -= 2;
      }//end for

      //------- Changes in the GUI ---------------------------------------------------------------
      position = i*16;//the position where the bit starts
      //adds the bits to the TextArea
      for(int j = position; j<position+16; j++){
        codificationTextArea.setText(codificationTextArea.getText() + bits[j]);
      }
      //adds a line
      codificationTextArea.setText(codificationTextArea.getText() + "\n");
      //------------------------------------------------------------------------------------------
    }//end for
    return bits;
  }
  
  public static int[] differentialManchester(int[] asciiMessage){
    int[] differentialManchester = new int[asciiMessage.length*16];
    String binary;
    int position = 0;
    for(int i=0; i<asciiMessage.length; i++){
      binary = Integer.toBinaryString(asciiMessage[i]);
      position = (16 * (i + 1)) - binary.length()*2;

      differentialManchester[position    ] = 1;
      differentialManchester[position + 1] = 0;
      position += 2;

      for(int j=1; j<binary.length(); j++){
        if(binary.charAt(j) == '0'){
          differentialManchester[position    ] = differentialManchester [position - 2];
          differentialManchester[position + 1] = differentialManchester [position - 1];
        }
        else{
          differentialManchester[position    ] = differentialManchester [position - 1];
          differentialManchester[position + 1] = differentialManchester [position - 2];
        }
        position += 2;
      }//end for

      //------- Changes in the GUI ---------------------------------------------------------------
      position = i*16;//the position where the bit starts
      //adds the bits to the TextArea
      for(int j = position; j<position+16; j++){
        codificationTextArea.setText(codificationTextArea.getText() + differentialManchester[j]);
      }
      //adds a line
      codificationTextArea.setText(codificationTextArea.getText() + "\n");
      //------------------------------------------------------------------------------------------
    } 
    return differentialManchester;
  }
  /*
  //receives the message from the application layer
  public static void receiveFromApplicationLayer(int[] asciiMessage, int codificationType, MainController controller){
    switch(codificationType){
      //binary codification
      case 0:
        Comunication.comunicate(binary(asciiMessage), codificationType, controller);
        break;
      //Manchester codification
      case 1:
        Comunication.comunicate(manchester(asciiMessage), codificationType, controller);
        break;
      //Differential Manchester codification
      case 2:
        //fluxOfBits = differentialManchester(binary(asciiMessage));
        break;
    }

    //calls the methood comunicate in the Comunication class
    //Comunication.comunicate(fluxOfBits, codificationType, controller);
  }

  public static int[] binary(int[] asciiMessage){
    int[] binaryMessage = new int[asciiMessage.length];

    //transforms from ascii to binary string and then transforms in int
    for(int i = 0; i<asciiMessage.length; i++)
      binaryMessage[i] = Integer.parseInt(Integer.toBinaryString(asciiMessage[i]));
    
    return binaryMessage;
  }

  public static String[] manchester(int[] asciiMessage){
    String[] manchesterCode = new String[asciiMessage.length];
    char[] temp;
    for(int i=0; i<manchesterCode.length; i++){
      temp = Integer.toBinaryString(asciiMessage[i]).toCharArray();
      manchesterCode[i] = "";
      for(int j=0; j<temp.length; j++){
        if(temp[j] == '1')
          manchesterCode[i] += "10";
        else
          manchesterCode[i] += "01";
      }//end for
    }//end for
    return manchesterCode;
  }
  
  public static int[] differentialManchester(int[] binaryMesage){
    return null;
  }
  */
  /*
  public static int[] manchester(int[] asciiMessage){
    int[] manchester = new int[asciiMessage.length*2];
    char[] binary;
    String temp;
    for(int i=0; i<asciiMessage.length; i++){
      //converting from ascii code to binary and then to a array of char
      binary = Integer.toBinaryString(asciiMessage[i]).toCharArray();
      temp = "";
      
       * This part divide the manchester code in 2, sice the manchester code is too big 
       * to put in an int, since I'm using int array instead of a String array or an int 
       * matrix
       

      //converting from binary to manchester code
      for(int j=0; j<binary.length/2; j++){
        if(binary[j] == '1')
          temp += "10";
        else
          temp += "01";
      }
      manchester[2*i] = Integer.parseInt(temp);
      temp = "";
      for(int j=binary.length/2; j<binary.length; j++){
        if(binary[j] == '1')
          temp += "10";
        else
          temp += "01";
      }
      
       * Because I can't put a number that starts with 0 in an int variable without losing that 
       * information, I decided to use put a number 1 in the start, that is later taken off when 
       * parsing the int to String
       
      if(temp.charAt(0) == '0')
        temp = "1" + temp;
      manchester[2*i+1] = Integer.parseInt(temp);
    }
    return manchester;
  }*/
}
