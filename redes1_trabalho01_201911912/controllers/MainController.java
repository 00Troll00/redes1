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
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import receiver.ReceiverApplication;
import transmitter.TransmitterApplication;

public class MainController implements Initializable{
  @FXML private TextField transmitterTextField;
  @FXML private TextField receiverTextField;
  @FXML private Button sendButton;

  private TransmitterApplication transmitter;
  private ReceiverApplication receiver;

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    receiver = new ReceiverApplication(receiverTextField);
    transmitter = new TransmitterApplication(transmitterTextField, receiver);
    //button configuration
    sendButton.setOnAction( event -> transmitter.sendToPhisicalLayer());
  }
  
}
