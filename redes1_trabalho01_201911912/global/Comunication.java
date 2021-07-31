/********************************************************************
 * Author: Alan Bonfim Santos
 * Registration: 201911912
 * Initial date: 30/07/2021 21:57
 * Last update: 30/07/2021 
 * Name: MainContrellor.java
 * Function: 
 *******************************************************************/
package global;

import controllers.MainController;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollBar;
import javafx.scene.paint.Color;
import receiver.ReceiverPhisicalLayer;

public class Comunication { 
  private static int canvasXPosition;
  
  public static void comunicate(int[] transmitterBits, int codificationType, MainController controller){
    int[] receiverBits = new int[transmitterBits.length];

    int temp;
    Canvas canvas = controller.getCanvas();
    ScrollBar scroll = controller.getScroll();
    GraphicsContext graContext = canvas.getGraphicsContext2D();
    graContext.setFill(Color.WHITE);
    canvas.setLayoutX(0);
    scroll.setValue(0);
    canvas.setWidth(transmitterBits.length * 7 * 16);
    scroll.setMax(canvas.getWidth() - scroll.getWidth());
    graContext.fillRect(0, 0, canvas.getWidth(), 50);
    graContext.setFill(Color.BLACK);
    canvasXPosition = 0;


    for(int i = 0; i<transmitterBits.length; i++){
      receiverBits[i] = transmitterBits[i];
      temp = receiverBits[i];
      
      //changes that are made to the interface
      while(temp != 0){
        if(temp % 10 == 1)
          graContext.fillRect(canvasXPosition, 5, 15, 3);
        else
          graContext.fillRect(canvasXPosition, 32, 15, 3);
        canvasXPosition +=16;
        temp /= 10;
      }
    }
    ReceiverPhisicalLayer.receives(receiverBits, codificationType, controller.getReceiver());
  }
}
