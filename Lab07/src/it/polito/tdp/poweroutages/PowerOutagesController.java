package it.polito.tdp.poweroutages;

/**
 * Sample Skeleton for 'PowerOutages.fxml' Controller Class
 */

import java.net.URL;

import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.poweroutages.model.Blackout;
import it.polito.tdp.poweroutages.model.Model;
import it.polito.tdp.poweroutages.model.Nerc;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class PowerOutagesController {


    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML 
    private ChoiceBox<Nerc> boxNerc; //dichiaro il tipo di elementi nella choice

    @FXML
    private TextField txtMaxYears;

    @FXML
    private TextField txtMaxHours;

    @FXML
    private Button btnWorstCase;

    @FXML
    private TextArea txtResult;


    
    private Model model;


	public void setModel(Model model) {

		this.model = model;
		
		//passo i valori dei nerc alla choice
		List<Nerc> nercList = model.getNercList();
		boxNerc.getItems().addAll(nercList);
	}
    
    @FXML
    void doCalcolaWorstCaseAnalysis(ActionEvent event) {
    	txtResult.clear();

		try {
			//prendo valore sceldo dalla choice e ne controllo esistenza
			Nerc selectedNerc = boxNerc.getSelectionModel().getSelectedItem();
			if (selectedNerc == null) {
				txtResult.setText("Select a NERC (area identifier)");
				return;
			}

			//prendo il numero di anni massimi ed effettuo controllo
			int maxY = Integer.parseInt(txtMaxYears.getText());
			
			/*CONTROLLO AGGIUNTIVO FATTO DAL PROF NON RICHIESTO
			 * salvo la dimensione della lista con gli anni in cui ho blackout
			 * se lista dal 2000 al 2014 la dimensione sara size=14 e posso inserire
			 * un massimo di 14 anni per il blackout, questo perche nel DB ho solo 14 anni,
			 * se metessi 16 mi darebbe il risutato di 14*/
			int yearListSize = model.getYearList().size();
			
			//anno passato non può essere negativo e non superare gli elementi della lista??????????????????????????????????????????????
			if (maxY <= 0 || maxY > yearListSize) {
				txtResult.setText("Select a number of years in range [1, " + yearListSize + "]");
				return;
			}

			
			//prendo il numero di ore massime passato ed effettuo controllo
			int maxH = Integer.parseInt(txtMaxHours.getText());
			if (maxH <= 0) { //ora non può essere negativa
				txtResult.setText("Select a number of hours greater than 0");
				return;
			}

			txtResult.setText(
					String.format("Computing the worst case analysis... for %d hours and %d years", maxH, maxY));
			
			//chiamo funz model che invoca ricorsiva per ottenere lista
			List<Blackout> worstCase = model.trovaSoluzione(selectedNerc, maxY, maxH);

			//stampo risultati num disserviti e ore totali di blackout
			txtResult.clear();
			txtResult.appendText("Tot people affected: " + model.contaDisserviti(worstCase) + "\n");
			txtResult.appendText("Tot hours of outage: " + model.sumBlackoutHours(worstCase) + "\n");

			//stampo le info del caso peggiore
			for (Blackout ee : worstCase) {
				txtResult.appendText(String.format("%d %s %s %d %d", ee.getYear(), ee.getBlackStart(),
						ee.getBlackEnd(), ee.getOutageDuration(), ee.getCustomerAffercted()));
				txtResult.appendText("\n");
			}

		} catch (NumberFormatException e) {
			txtResult.setText("Insert a valid number of years and of hours");
		}

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
    	   assert boxNerc != null : "fx:id=\"boxNerc\" was not injected: check your FXML file 'PowerOutages.fxml'.";
           assert txtMaxYears != null : "fx:id=\"txtMaxYears\" was not injected: check your FXML file 'PowerOutages.fxml'.";
           assert txtMaxHours != null : "fx:id=\"txtMaxHours\" was not injected: check your FXML file 'PowerOutages.fxml'.";
           assert btnWorstCase != null : "fx:id=\"btnWorstCase\" was not injected: check your FXML file 'PowerOutages.fxml'.";
           assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'PowerOutages.fxml'.";

     // Utilizzare questo font per incolonnare correttamente i dati
     		txtResult.setStyle("-fx-font-family: monospace");
    }
}
