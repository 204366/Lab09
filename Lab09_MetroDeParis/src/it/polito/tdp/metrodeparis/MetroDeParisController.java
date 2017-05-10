package it.polito.tdp.metrodeparis;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class MetroDeParisController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ChoiceBox<?> chooseFP;

    @FXML
    private ChoiceBox<?> chooseFA;

    @FXML
    private Button btnCalcola;

    @FXML
    private TextArea txtResult;

    @FXML
    void doCalcola(ActionEvent event) {

    }

    @FXML
    void initialize() {
        assert chooseFP != null : "fx:id=\"chooseFP\" was not injected: check your FXML file 'MetroDeParis.fxml'.";
        assert chooseFA != null : "fx:id=\"chooseFA\" was not injected: check your FXML file 'MetroDeParis.fxml'.";
        assert btnCalcola != null : "fx:id=\"btnCalcola\" was not injected: check your FXML file 'MetroDeParis.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'MetroDeParis.fxml'.";

    }
}
