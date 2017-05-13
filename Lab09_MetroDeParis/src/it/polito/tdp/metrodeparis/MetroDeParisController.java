package it.polito.tdp.metrodeparis;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.metrodeparis.model.Fermata;
import it.polito.tdp.metrodeparis.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class MetroDeParisController {

	Model model;
    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ChoiceBox<Fermata> chooseFP;

    @FXML
    private ChoiceBox<Fermata> chooseFA;

    @FXML
    private Button btnCalcola;

    @FXML
    private TextArea txtResult;

    public void setModel(Model model) {
		this.model = model;
		chooseFP.getItems().addAll(model.getFermate());
		chooseFA.getItems().addAll(model.getFermate());
		
	}
    @FXML
    void doCalcola(ActionEvent event) {
    	if(chooseFP.getValue().equals(chooseFA.getValue())){
    		txtResult.setText("Scegliere una diversa stazione di arrivo!");
    	}
    	else{
    		txtResult.setText(model.camminoMinimoSuLinea(chooseFP.getValue(), chooseFA.getValue()).toString());
        	txtResult.appendText("\n Tempo di percorrenza stimato: " + model.calcolaTempoTotSuLinea());
    	}
    	
    }

    @FXML
    void initialize() {
        assert chooseFP != null : "fx:id=\"chooseFP\" was not injected: check your FXML file 'MetroDeParis.fxml'.";
        assert chooseFA != null : "fx:id=\"chooseFA\" was not injected: check your FXML file 'MetroDeParis.fxml'.";
        assert btnCalcola != null : "fx:id=\"btnCalcola\" was not injected: check your FXML file 'MetroDeParis.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'MetroDeParis.fxml'.";

    }

	
}
