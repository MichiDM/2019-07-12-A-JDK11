package it.polito.tdp.food.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.food.db.FoodDao;

public class Model {
	
	private FoodDao dao;
	private Graph<Food, DefaultWeightedEdge> grafo;
	private Map<Integer, Food> foodsIdMap;
	
	
	public Model() {
		this.dao = new FoodDao();
		
		this.foodsIdMap = new HashMap<Integer, Food>();
		
		//Popoliamo l'identity map, in caso ci servisse dopo
		List<Food> foods = this.dao.listAllFoods();
		for (Food f : foods) {
			this.foodsIdMap.put(f.getFood_code(), f);
		}
		
	}
	

	public void creaGrafo(int porzioni) {
		// TODO Auto-generated method stub
		
		//costruzione di un nuovo grafo
				this.grafo = new SimpleWeightedGraph<Food, DefaultWeightedEdge>(DefaultWeightedEdge.class);
				
				//assegnazione dei vertici
				List<Food> vertici = this.dao.getVertici(porzioni);
				Graphs.addAllVertices(this.grafo, vertici);
				
				//assegnazione degli archi
				//calcoliamo gli archi da query
				List<Arco> archi = this.dao.getArchi();
				for (Arco a : archi) {
					if (vertici.contains(this.foodsIdMap.get(a.getFoodCode1())) && 
							vertici.contains(this.foodsIdMap.get(a.getFoodCode2()))) {
					Food f1 = this.foodsIdMap.get(a.getFoodCode1());
					Food f2 = this.foodsIdMap.get(a.getFoodCode2());
					double peso = a.getAvgCondimentsCalories();
					Graphs.addEdgeWithVertices(this.grafo, f1, f2, peso);
					}
				}
			
		
	}

	/**
	 * Metodo che restituisce il numero di vertici del grafo
	 * @return
	 */
	public int getNVertici(){
		return this.grafo.vertexSet().size();
	}
	
	
	/**
	 * Metodo che restituisce il numero di archi del grafo
	 * @return
	 */
	public int getNArchi(){
		return this.grafo.edgeSet().size();
	}
	
	/**
	 * Metodo che restituisce una lista di vertici dell'arco
	 * @return
	 */
	public List<Food> getVertici(){
		return new ArrayList<Food>(this.grafo.vertexSet());
	}
	
	// COMPONENTE CONNESSA
	/**
	 * Metodo che calcola ed analizza la componente connessa per il prodotto p selezionato
	 * @param r
	 * @return
	 */
	public List<StatsConnessa> analizzaComponente(Food f) {
		// Trova componente connessa  (Connectivity Inspector)
		ConnectivityInspector<Food, DefaultWeightedEdge> inspector =
				new ConnectivityInspector<Food, DefaultWeightedEdge>(this.grafo);
		Set<Food> connessi = inspector.connectedSetOf(f);
			
		
		// creo una lista per ordinarla in ordine decrescente di avgCalories
		// Possiamo prendere gli archi del grafo uno a uno, e verificare se i suoi 
		// vertici sono nella componente connessa. In caso affermativo, possiamo creare
		// un oggetto della classe StatsConnessa che prende il food adiacente al food f inserito dall'utente
		// e la usa avgCalories
		List<StatsConnessa> connessiList = new ArrayList<>();
		for(DefaultWeightedEdge e : this.grafo.edgeSet()) {
			if (connessi.contains(this.grafo.getEdgeSource(e)) && this.grafo.getEdgeSource(e).equals(f) &&
					connessi.contains(this.grafo.getEdgeTarget(e))) {
				StatsConnessa s = new StatsConnessa(this.grafo.getEdgeTarget(e), this.grafo.getEdgeWeight(e));
				connessiList.add(s);
			}
		}
		
		
		Collections.sort(connessiList);
		
		List<StatsConnessa> connessiRidotta = null;
		// prendo solo i primi 5 food che hanno avgCalories maggiore
		if (connessiList.size() >=5) {
			connessiRidotta = connessiList.subList(0,5);
		}
		else {
			connessiRidotta = connessiList.subList(0, connessiList.size());
		}

		return connessiRidotta;
	}


	
	

}
