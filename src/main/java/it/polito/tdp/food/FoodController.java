/**
 * Sample Skeleton for 'Food.fxml' Controller Class
 */

package it.polito.tdp.food;

import java.net.URL;

import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.Model;
import it.polito.tdp.food.model.StatsConnessa;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FoodController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtPorzioni"
    private TextField txtPorzioni; // Value injected by FXMLLoader

    @FXML // fx:id="txtK"
    private TextField txtK; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalisi"
    private Button btnAnalisi; // Value injected by FXMLLoader

    @FXML // fx:id="btnCalorie"
    private Button btnCalorie; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="boxFood"
    private ComboBox<Food> boxFood; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Creazione grafo...");
    	 	
    	// recupero valori immessi dall'utente con i relativi controlli
    	
    	int numeroPorzioni =0;
    	try {
    		numeroPorzioni = Integer.parseInt( this.txtPorzioni.getText() );
    	} catch(NumberFormatException e) {
    	    this.txtResult.setText("Invalid argument. N. Porzioni must be a integer!");
    	    return;
    	}
    	// controllo che il numero non sia negativo
    	if(numeroPorzioni<0) {
    	  this.txtResult.setText("N. Porzioni must be a nonnegative integer.");
    	  return;
    	}

    	
    	// creo il grafo
        this.model.creaGrafo(numeroPorzioni);
        	
        List<Food> vertici = this.model.getVertici();
    	// per ordinare i vertici
        Collections.sort(vertici);
        this.txtResult.setText("Grafo creato, con " + this.model.getNVertici() + " vertici e " + this.model.getNArchi()+ " archi\n");
        	
        //Popolamento cmbBox dei cibi
    	this.boxFood.getItems().clear();
    	this.boxFood.getItems().addAll(vertici);
    	
    	//abilita i vari controlli della gui
    	this.btnCalorie.setDisable(false);
        this.btnSimula.setDisable(false);
        
    	
    }
    
    @FXML
    void doCalorie(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Analisi calorie...");
    	
    	// recupero valori immessi dall'utente con i relativi controlli
    	Food cibo = this.boxFood.getValue();
    	if (cibo==null) {
    	    this.txtResult.setText("Please select a food");
    	    return;
    	}
    	
    	//analizza la componente connessa
    	// creata classe 'StatsConnessa' per stampare i risultati
    	List<StatsConnessa> result = this.model.analizzaComponente(cibo);
  
    	this.txtResult.appendText("\nl’elenco dei 5 cibi aventi le “calorie congiunte” massime tra quelli \n" 
    			+ "adiacenti al cibo selezionato nella tendina sono: \n");
    	if (result.size() == 0) {
    		this.txtResult.appendText("Il cibo selezionato "+ cibo+ " non ha componenti connesse");
    	}
    	else {
	    	for (StatsConnessa s : result) {
	    		this.txtResult.appendText(s.getFood() + ", "+s.getAvgCalories() + "\n");  		
	    	}
    	}
    	
    }

    @FXML
    void doSimula(ActionEvent event) {
    	txtResult.clear();
    	txtResult.appendText("Simulazione...");
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtPorzioni != null : "fx:id=\"txtPorzioni\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtK != null : "fx:id=\"txtK\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnAnalisi != null : "fx:id=\"btnAnalisi\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnCalorie != null : "fx:id=\"btnCalorie\" was not injected: check your FXML file 'Food.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Food.fxml'.";
        assert boxFood != null : "fx:id=\"boxFood\" was not injected: check your FXML file 'Food.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Food.fxml'.";
        this.btnCalorie.setDisable(true);
        this.btnSimula.setDisable(true);
    }
    
    public void setModel(Model model) {
    	this.model = model;
    }
}
