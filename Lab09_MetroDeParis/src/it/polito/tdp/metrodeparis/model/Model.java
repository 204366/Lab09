package it.polito.tdp.metrodeparis.model;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DirectedWeightedMultigraph;
import org.jgrapht.graph.WeightedMultigraph;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.metrodeparis.dao.MetroDAO;
import it.polito.tdp.metrodeparis.model.TrattaEdge;

public class Model {

	WeightedMultigraph<Fermata, TrattaEdge> grafo;
	MetroDAO dao = new MetroDAO();
	List<Connessione> conn;
	GraphPath<Fermata, TrattaEdge> camm;
	GraphPath<FermataSuLinea, TrattaEdge> camm2;
	List<Fermata> cammMinimo;
	List<FermataSuLinea> cammMinimoSuLinea;
	List<String> cammMinimoString;
	double tempoPercorrenza;
	double tempoPercorrenzaSuLinea;
	double  tempoPercorrenzaSuLineaTot;
	List<FermataSuLinea> fermateSuLinea ;
	List<Fermata> fermate;

	DirectedWeightedMultigraph<FermataSuLinea, TrattaEdge> grafo2;

	
	public List<Fermata> getFermate(){
		if(fermate == null){
			this.fermate = dao.getAllFermate();
		}
		return fermate;
	}
	public WeightedMultigraph<Fermata, TrattaEdge> createGraph() {
		grafo = new WeightedMultigraph<Fermata, TrattaEdge>(TrattaEdge.class);

		for (Connessione c : this.getConnessione()) {
			this.getGraph().addVertex(c.getFermataP());
			this.getGraph().addVertex(c.getFermataA());

			TrattaEdge tratta = this.getGraph().addEdge(c.getFermataP(), c.getFermataA());

			Double distance = LatLngTool.distance(
					new LatLng(c.getFermataP().getCoords().getLatitude(), c.getFermataP().getCoords().getLongitude()),
					new LatLng(c.getFermataA().getCoords().getLatitude(), c.getFermataA().getCoords().getLongitude()),
					LengthUnit.KILOMETER);
			Double tempo = (distance / c.getLinea().getVelocita()) * 3600;
			tratta.setId(c.getLinea().getId());
			tratta.setIntervallo(c.getLinea().getIntervallo());
			tratta.setNome(c.getLinea().getNome());
			tratta.setVelocita(c.getLinea().getVelocita());

			this.getGraph().setEdgeWeight(tratta, tempo);

		}
		return grafo;

	}

	public WeightedMultigraph<Fermata, TrattaEdge> getGraph() {
		if (grafo == null) {
			this.grafo = this.createGraph();
		}
		return this.grafo;

	}

	public List<Connessione> getConnessione() {
		if (conn == null) {
			this.conn = dao.getAllConnessioni();
		}
		return this.conn;

	}

	public List<Fermata> camminoMinimo(Fermata p, Fermata a) {
		tempoPercorrenza = 0.0;
		cammMinimo = new ArrayList<Fermata>();
		DijkstraShortestPath<Fermata, TrattaEdge> dijkstra = new DijkstraShortestPath<Fermata, TrattaEdge>(
				this.getGraph(), p, a);
		camm = dijkstra.getPath();

		for (TrattaEdge t : camm.getEdgeList()) {
			cammMinimo.add(this.getGraph().getEdgeTarget(t));
			tempoPercorrenza += this.getGraph().getEdgeWeight(t);

		}
		return cammMinimo;
	}
	
	public double calcolaTempoTot(Fermata p, Fermata a) {
		Double tempoTot = this.camminoMinimo(p, a).size() * 30 + tempoPercorrenza;
		return tempoTot;

	}
	
	//************ Secondo punto*************************************************************************

	
	
	public List<FermataSuLinea> getFermateSuLinea() {
		if (fermateSuLinea == null) {
			this.fermateSuLinea = dao.getFermataSuLinea();
		}
		return this.fermateSuLinea;

	}
	
	
	
	public DirectedWeightedMultigraph<FermataSuLinea, TrattaEdge> createGraphSuLinea() {
		
		grafo2 = new DirectedWeightedMultigraph<FermataSuLinea, TrattaEdge>(TrattaEdge.class);
	

		// STEP1: Per ciascuna connessione aggiungo un arco tra la coppia di fermate su linea
		for (Connessione c : this.getConnessione()) {
			
			FermataSuLinea p = new FermataSuLinea(c.getFermataP().getIdFermata(), c.getFermataP().getNome(), c.getLinea(), c.getFermataP().getCoords());
			FermataSuLinea a = new FermataSuLinea(c.getFermataA().getIdFermata(), c.getFermataA().getNome(), c.getLinea(), c.getFermataA().getCoords());

			this.getGraph2().addVertex(p);
			this.getGraph2().addVertex(a);

			TrattaEdge tratta = grafo2.addEdge(a, p);
			
			if (tratta != null){
				Double distance = LatLngTool.distance(p.getCoords(), a.getCoords(), LengthUnit.KILOMETER);
				Double tempo = (distance / c.getLinea().getVelocita()) * 3600;
				grafo2.setEdgeWeight(tratta, tempo);
				
				tratta.setId(c.getLinea().getId());
				tratta.setIntervallo(c.getLinea().getIntervallo());
				tratta.setNome(c.getLinea().getNome());
				tratta.setVelocita(c.getLinea().getVelocita());
			}
		}
		
		System.out.println("step1 completed!");
		
		// STEP2: Creo i collegamenti tra stazioni uguali, ma linee diverse
		
		// Aggiungo i vertici mancanti
		for (FermataSuLinea fsl : this.getFermateSuLinea()){
			if (!grafo2.containsVertex(fsl))
				grafo2.addVertex(fsl);
		}
		
		// Creo i collegamenti tra stazioni uguali, ma linee diverse
		for (FermataSuLinea fsl : this.getFermateSuLinea()){
			for (FermataSuLinea fsl2 : this.getFermateSuLinea()){
				if(fsl.getIdFermata() == fsl2.getIdFermata() && !fsl.getLinea().equals(fsl2.getLinea())){
					TrattaEdge tratta = grafo2.addEdge(fsl, fsl2);
					this.getGraph2().setEdgeWeight(tratta, fsl2.getLinea().getIntervallo());
				}
			}
		}
		

		System.out.println(grafo2);

		return grafo2;

	}
	
	public List<String> camminoMinimoSuLinea(Fermata p, Fermata a) {
		tempoPercorrenzaSuLineaTot = 100000000000000000000000000000000.00;
		List<FermataSuLinea> ottima = new ArrayList<FermataSuLinea>();
		cammMinimoString = new ArrayList<String>();
	
		for(FermataSuLinea fl1: this.getFermateSuLinea()){
			if(fl1.getIdFermata() == p.getIdFermata()){
				for(FermataSuLinea fl2: this.getFermateSuLinea()){
					if(fl2.getIdFermata() == a.getIdFermata()){
						List<FermataSuLinea> prova = this.camminoMinimoPrimoPasso(fl1, fl2);
						if(tempoPercorrenzaSuLinea < tempoPercorrenzaSuLineaTot){
							tempoPercorrenzaSuLineaTot = tempoPercorrenzaSuLinea;
							ottima.clear();
							ottima.addAll(prova);
						}
					}	
				}
			}		
		}
		cammMinimoString.add("\n Prendo Linea: " + ottima.get(0).getLinea().getNome());
		int t = ottima.get(0).getIdFermata();
		ottima.remove(0);
		
		for(FermataSuLinea f: ottima){
			
			cammMinimoString.add(f.getNome());
			if(f.getIdFermata() == t){
				cammMinimoString.add("\n Cambio su  Linea: " + f.getLinea().getNome());
			}
			t = f.getIdFermata();
		}
		return cammMinimoString;
	}
	public List<FermataSuLinea> camminoMinimoPrimoPasso(FermataSuLinea p, FermataSuLinea a){
		tempoPercorrenzaSuLinea = 0.0;
		cammMinimoSuLinea = new ArrayList<FermataSuLinea>();
		DijkstraShortestPath<FermataSuLinea, TrattaEdge> dijkstra = new DijkstraShortestPath<FermataSuLinea, TrattaEdge>(
				this.getGraph2(), p, a);
		camm2 = dijkstra.getPath();
		cammMinimoSuLinea.add(this.getGraph2().getEdgeSource(camm2.getEdgeList().get(0)));
		for(TrattaEdge t : camm2.getEdgeList()){
			cammMinimoSuLinea.add(this.getGraph2().getEdgeTarget(t));
			if(this.getGraph2().getEdgeTarget(t).getIdFermata() != this.getGraph2().getEdgeSource(t).getIdFermata()){
				tempoPercorrenzaSuLinea += this.getGraph2().getEdgeWeight(t) + 30;
			}else{
				tempoPercorrenzaSuLinea += this.getGraph2().getEdgeWeight(t);
			}
		}
		this.tempoPercorrenzaSuLinea = tempoPercorrenzaSuLinea - 30;
		return cammMinimoSuLinea;
		
	}
	
	public double calcolaTempoTotSuLinea(){
		return tempoPercorrenzaSuLineaTot;
		
	}


	public DirectedWeightedMultigraph<FermataSuLinea, TrattaEdge> getGraph2() {
		if (grafo2 == null) {
			this.grafo2 = this.createGraphSuLinea();
		}
		return this.grafo2;

	}

}
