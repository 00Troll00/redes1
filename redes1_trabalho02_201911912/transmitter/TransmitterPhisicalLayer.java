/********************************************************************
 * Author: Alan Bonfim Santos
 * Registration: 201911912
 * Initial date: 30/07/2021 21:55
 * Last update: 15/08/2021 21:19
 * Name: TransmitterPhisicalLayer.java
 * Function: Simulates the phisical layer of the transmitter
 *******************************************************************/
package transmitter;

import controllers.MainController;
import global.Comunication;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextArea;

//version 0.3.0 (third version)
public class TransmitterPhisicalLayer {
  public static TextArea codificationTextArea;

  public static void receive(int[] asciiMessage, MainController controller){
    //Related to the GUI -------------------------------------------------------------------------------
    codificationTextArea = controller.getTransmitterCodificationTextArea();

    //creates a listener that force the scroll to be set at the bottom
    ChangeListener<Number> listener = new ChangeListener<Number>(){
      @Override
      public void changed(ObservableValue<? extends Number> v, Number oldValue, Number newValue){
        codificationTextArea.setScrollTop(9999);//make the scroll go down if the texts exceeds the size of the TextArea
      }
    };
    codificationTextArea.scrollTopProperty().addListener(listener);
    
    //changing to the bits's tab
    controller.getTransmitterTabPane().getSelectionModel().select(
      controller.getTransmitterTabPane().getTabs().get(2)
    );
    //--------------------------------------------------------------------------------------------------

    new Thread( () -> {
      int[] binaryArray;
      if(controller.getFrameWorkTpe() == 2)
        binaryArray = asciiMessage;
      else
        binaryArray = asciiToBinary(asciiMessage);

      int[] fluxOfBits = null;
      switch(controller.getCodificationType()){
        //binary codification
        case 0:
          fluxOfBits = binary(binaryArray);
          break;
        //Manchester codification
        case 1:
          fluxOfBits = manchester(binaryArray);
          break;
        //Differential Manchester codification
        case 2:
          fluxOfBits = differentialManchester(binaryArray);
          break;
      }
      
      //violates the phical layer
      if(controller.getFrameWorkTpe() == 3)
        fluxOfBits = violate(fluxOfBits);

      //adding the bits to the TextArea
      for(int bit : fluxOfBits){
        addBitToTextArea(bit);
        try {Thread.sleep(25);} //hmmmm soninho
        catch (InterruptedException e) { }
      }

      //remove the listener, so the scroll can be used again
      codificationTextArea.scrollTopProperty().removeListener(listener);

      //sends the bits to the comunication mean
      Comunication.comunicate(fluxOfBits, controller);
    }).start(); 
  }//end receive

  private static int[] violate(int[] bits){
    final int FRAME_SIZE = 30;
    int position = 0;//position in the framed array
    //adds the size to the new bits
    int[] framedBits = new int[bits.length + ((bits.length/FRAME_SIZE + 2)*2)];
    for(int i=0; i<bits.length; i++){
      if(i % FRAME_SIZE == 0){
        framedBits[position  ] = 1;
        framedBits[position+1] = 1;
        position +=2;
      }
      framedBits[position] = bits[i];
      position++;
    }//end for
    framedBits[position  ] = 1;
    framedBits[position+1] = 1;

    return framedBits;
  }//violate

  private static int[] asciiToBinary(int[] asciiMessage) {
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
    }//end for
    return bits;
  }//end asciiToBinary

  private static int[] binary(int[] binaryArray){
    return binaryArray; //it's already in binary
  }

  private static int[] manchester(int[] binaryArray){
    int[] manchester = new int[binaryArray.length*2];

    //transforms from binary in manchester
    for(int i=0; i<binaryArray.length; i++){
      if(binaryArray[i] == 1){
        manchester[2*i  ] = 1;
        manchester[2*i+1] = 0;
      }
      else{
        manchester[2*i  ] = 0;
        manchester[2*i+1] = 1;
      }
    }
    return manchester;
  }

  private static int[] differentialManchester(int[] binaryArray){
    int[] differentialManchester = new int[binaryArray.length*2];
    //transforms from binary in manchester
    int position = 0; //defines the position where the bit must be placed, relative to the binary position
    //puting the start bits
    if(binaryArray[0] == 1){
      differentialManchester[position  ] = 1;
      differentialManchester[position+1] = 0;
    }
    else{
      differentialManchester[position  ] = 0;
      differentialManchester[position+1] = 1;
    }
    position+=2;

    for(int i=1; i<binaryArray.length; i++){
      if(binaryArray[i] == 1){
        differentialManchester[position  ] = differentialManchester[position-1];
        differentialManchester[position+1] = differentialManchester[position-2];
      }
      else{
        differentialManchester[position  ] = differentialManchester[position-2];
        differentialManchester[position+1] = differentialManchester[position-1];
      }
      position+=2;
    }
    return differentialManchester;
  }

  private static void addBitToTextArea(int bit){
    Platform.runLater( () -> codificationTextArea.setText(codificationTextArea.getText() + bit));
  }

  /**
   * Old phisical layer, the second, version 0.2.0
   */

  /*
  private static void addLineToTextArea(){
    Platform.runLater( () -> {
      codificationTextArea.setText(codificationTextArea.getText() + "\n");
      codificationTextArea.setScrollTop(9999);
    });
  }
  */
  /*
  private static int[] binary(int[] asciiMessage, int frameWorkTpe) {
    if(frameWorkTpe == 2)
      return asciiMessage;
    
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
        try {
          Thread.sleep(25);
        } catch (InterruptedException e) { }
        addBitToTextArea(bits[j]);
      }
      //adds a line
      addLineToTextArea();
      //------------------------------------------------------------------------------------------
    }//end for
    return bits;
  }//end binary

  private static int[] manchester(int[] asciiMessage, int frameWorkTpe) {
    //I had to do this, since my manchester codification work in a different way
    if(frameWorkTpe == 2){
      int[] manchester = new int[asciiMessage.length * 2];
      for(int i=0; i<asciiMessage.length; i++){
        if(asciiMessage[i] == 1){
          manchester[2*i  ] = 1;
          manchester[2*i+1] = 0;
        }
        else{
          manchester[2*i  ] = 0;
          manchester[2*i+1] = 1;
        }
      }//end for

      //------- Changes in the GUI ---------------------------------------------------------------
      //adds the bits to the TextArea
      for(int bit : manchester){
        try {
          Thread.sleep(25);
        } catch (InterruptedException e) { }
        addBitToTextArea(bit);
      }
      //------------------------------------------------------------------------------------------
      return manchester;
    }//end if
    else{
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
          try {
            Thread.sleep(25);
          } catch (InterruptedException e) { }
          addBitToTextArea(bits[j]);
        }
        //adds a line
        addLineToTextArea();
        
        //------------------------------------------------------------------------------------------
      }//end for
      
      return bits;
    }//end else
  }//end manchester
  
  public static int[] differentialManchester(int[] asciiMessage){
    int[] bits = new int[asciiMessage.length*16];
    String binary;
    int position = 0;
    for(int i=0; i<asciiMessage.length; i++){
      binary = Integer.toBinaryString(asciiMessage[i]);
      position = (16 * (i + 1)) - binary.length()*2;

      bits[position    ] = 1;
      bits[position + 1] = 0;
      position += 2;

      for(int j=1; j<binary.length(); j++){
        if(binary.charAt(j) == '0'){
          bits[position    ] = bits [position - 2];
          bits[position + 1] = bits [position - 1];
        }
        else{
          bits[position    ] = bits [position - 1];
          bits[position + 1] = bits [position - 2];
        }
        position += 2;
      }//end for

      //------- Changes in the GUI ---------------------------------------------------------------
      position = i*16;//the position where the bit starts
      //adds the bits to the TextArea
      for(int j = position; j<position+16; j++){
        try {
          Thread.sleep(25);
        } catch (InterruptedException e) { }
        addBitToTextArea(bits[j]);      
      }//end for
      //adds a line
      addLineToTextArea();
      //------------------------------------------------------------------------------------------
    }//end for 
    return bits;
  }//end differentialManchester

  */

  /**********************************************
   * The code below is not used, it was my firt
   * attempt, since I miss understood the problem
   * and how it showed be done
   **********************************************/

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
