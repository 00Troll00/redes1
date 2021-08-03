/********************************************************************
 * Author: Alan Bonfim Santos
 * Registration: 201911912
 * Initial date: 30/07/2021 18:18
 * Last update: 30/07/2021 
 * Name: MainContrellor.java
 * Function: 
 *******************************************************************/
package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import receiver.ReceiverApplication;
import transmitter.TransmitterApplication;

public class MainController implements Initializable{
  @FXML private TextArea transmitterTextArea;
  @FXML private TextArea receiverTextArea;
  @FXML private Button sendButton;
  @FXML private Canvas canvas;
  @FXML private ScrollBar scroll;
  @FXML private ComboBox<String> codificationComboBox;

  //codification components, where the ascii and bits are showed
  @FXML private Tab transmitterCodificationTab;
  @FXML private Tab receiverCodificationTab;
  @FXML private TextArea trasmitterAsciiTextArea;
  @FXML private TextArea transmitterCodificationTextArea;
  @FXML private TextArea receiverCodificationTextArea;
  @FXML private TextArea receiverAsciiTextArea;

  private int codificationType = 0;
  private TransmitterApplication transmitter;
  private ReceiverApplication receiver;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    //ComboBox settings
    codificationComboBox.getItems().setAll("Binary", "Manchester", "Differential Manchester");
    codificationComboBox.getSelectionModel().selectFirst();
    codificationComboBox.valueProperty().addListener((v, oldeValue, newValue) ->{
      if (newValue.equals("Binary")) codificationType = 0;
      if (newValue.equals("Manchester")) codificationType = 1; 
      if (newValue.equals("Differential Manchester")) codificationType = 2;
    });

    receiver = new ReceiverApplication(receiverTextArea);
    transmitter = new TransmitterApplication(transmitterTextArea);

    //button configuration
    sendButton.setOnAction( event -> {
      //cleaning the text areas when the send button is pressend
      receiverTextArea.setText("");
      trasmitterAsciiTextArea.setText("");
      transmitterCodificationTextArea.setText("");
      receiverCodificationTextArea.setText("");
      receiverAsciiTextArea.setText("");

      //sending the "message" and the codification type to the phisical layer
      transmitter.sendToPhisicalLayer(this, codificationType);
    });


    scroll.setMax(canvas.getWidth() - scroll.getWidth());

    scroll.valueProperty().addListener( (v, oldValue, newValue) -> {
      canvas.setLayoutX(newValue.intValue()* (-1));
    });
  }//end initialize

  public TextArea getTrasmitterAsciiTextArea() {
    return trasmitterAsciiTextArea;
  }

  public TextArea getTransmitterCodificationTextArea() {
    return transmitterCodificationTextArea;
  }

  public TextArea getReceiverCodificationTextArea() {
    return receiverCodificationTextArea;
  }

  public TextArea getReceiverAsciiTextArea() {
    return receiverAsciiTextArea;
  }

  public Canvas getCanvas() {
    return canvas;
  }

  public ScrollBar getScroll() {
    return scroll;
  }

  public ReceiverApplication getReceiver() {
    return receiver;
  }
}
