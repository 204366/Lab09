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

public class Model {

	WeightedMultigraph<Fermata, TrattaEdge> grafo;
	MetroDAO dao = new MetroDAO();
	List<Connessione> conn;
	GraphPath<Fermata, TrattaEdge> camm;
	GraphPath<FermataSuLinea, TrattaEdgeSuLinea> camm2;
	List<Fermata> cammMinimo;
	List<FermataSuLinea> cammMinimo2;
	List<String> cammMinimoString;
	Double tempoPercorrenza;
	double tempo;

	DirectedWeightedMultigraph<FermataSuLinea, TrattaEdgeSuLinea> grafo2;

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

	public List<String> camminoMinimoPrimoPasso(FermataSuLinea p, FermataSuLinea a) {
		TrattaEdgeSuLinea temp = null;
		tempoPercorrenza = 0.0;
		cammMinimo = new ArrayList<Fermata>();
		DijkstraShortestPath<FermataSuLinea, TrattaEdgeSuLinea> dijkstra = new DijkstraShortestPath<FermataSuLinea, TrattaEdgeSuLinea>(
				this.getGraph2(), p, a);
		camm2 = dijkstra.getPath();
		temp = camm2.getEdgeList().get(0);

		for (TrattaEdgeSuLinea t : camm2.getEdgeList()) {

			if (temp.getId() == t.getId()) {
				cammMinimo2.add(this.getGraph2().getEdgeTarget(t));
				tempoPercorrenza += this.getGraph2().getEdgeWeight(t);
				cammMinimoString.add(this.getGraph2().getEdgeTarget(t).getNome());
			}

			else {
				temp = t;
				cammMinimo2.add(this.getGraph2().getEdgeTarget(t));
				tempoPercorrenza += this.getGraph2().getEdgeWeight(t);
				cammMinimoString.add("Cambio su linea " + t.getNome());
				cammMinimoString.add(this.getGraph2().getEdgeTarget(t).getNome());

			}

		}
		return cammMinimoString;
	}

	public List<String> calcolaCamminoMinimoavanzato(Fermata p, Fermata a) {
		List<FermataSuLinea> fermate1 = dao.getFermataSuLinea(p);
		List<FermataSuLinea> fermate2 = dao.getFermataSuLinea(a);
		List<String> minimo = null;
		tempo = 0.0;

		for (FermataSuLinea par : fermate1) {
			for (FermataSuLinea arr : fermate2) {
				this.camminoMinimoPrimoPasso(par, arr);
				if (tempo > this.calcolaTempoTot2(par, arr)) {
					tempo = this.calcolaTempoTot2(par, arr);
					minimo = this.camminoMinimoPrimoPasso(par, arr);
				}
			}
		}

		return minimo;

	}

	public double calcolaTempoTot(Fermata p, Fermata a) {
		Double tempoTot = this.camminoMinimo(p, a).size() * 30 + tempoPercorrenza;
		return tempoTot;

	}

	public double calcolaTempoTot2(FermataSuLinea p, FermataSuLinea a) {
		Double tempoTot = this.cammMinimo2.size() * 30 + tempoPercorrenza;
		return tempoTot;

	}

	public DirectedWeightedMultigraph<FermataSuLinea, TrattaEdgeSuLinea> createGraphSuLinea() {
		grafo2 = new DirectedWeightedMultigraph<FermataSuLinea, TrattaEdgeSuLinea>(TrattaEdgeSuLinea.class);

		for (Connessione c : this.getConnessione()) {
			FermataSuLinea p = new FermataSuLinea(c.getFermataP().getIdFermata(), c.getFermataP().getNome(),
					c.getLinea(), c.getFermataP().getCoords());
			FermataSuLinea a = new FermataSuLinea(c.getFermataA().getIdFermata(), c.getFermataA().getNome(),
					c.getLinea(), c.getFermataA().getCoords());

			this.getGraph2().addVertex(p);
			this.getGraph2().addVertex(a);

			for (FermataSuLinea temp : dao.getFermataSuLinea(c.getFermataP())) {
				if (!a.equals(temp)  && !dao.getFermataSuLinea(c.getFermataP()).isEmpty()) {
					this.getGraph2().addVertex(temp);
					TrattaEdgeSuLinea t = this.getGraph2().addEdge(p, temp);

					this.getGraph2().setEdgeWeight(t, temp.getLinea().getIntervallo());

					TrattaEdgeSuLinea tratta = t;

					Double distance = LatLngTool.distance(
							new LatLng(p.getCoords().getLatitude(), p.getCoords().getLongitude()),
							new LatLng(a.getCoords().getLatitude(), a.getCoords().getLongitude()),
							LengthUnit.KILOMETER);
					Double tempo = (distance / c.getLinea().getVelocita()) * 3600;
					tratta.setId(c.getLinea().getId());
					tratta.setIntervallo(c.getLinea().getIntervallo());
					tratta.setNome(c.getLinea().getNome());
					tratta.setVelocita(c.getLinea().getVelocita());

					this.getGraph2().setEdgeWeight(tratta, tempo);
				}
			}
			for (FermataSuLinea temp : dao.getFermataSuLinea(c.getFermataA())) {

				if (!a.equals(temp) && !dao.getFermataSuLinea(c.getFermataP()).isEmpty()) {
					this.getGraph2().addVertex(temp);
					TrattaEdgeSuLinea t = this.getGraph2().addEdge(a, temp);

					this.getGraph2().setEdgeWeight(t, temp.getLinea().getIntervallo());

					TrattaEdgeSuLinea tratta = t;

					Double distance = LatLngTool.distance(
							new LatLng(p.getCoords().getLatitude(), p.getCoords().getLongitude()),
							new LatLng(a.getCoords().getLatitude(), a.getCoords().getLongitude()),
							LengthUnit.KILOMETER);
					Double tempo = (distance / c.getLinea().getVelocita()) * 3600;
					tratta.setId(c.getLinea().getId());
					tratta.setIntervallo(c.getLinea().getIntervallo());
					tratta.setNome(c.getLinea().getNome());
					tratta.setVelocita(c.getLinea().getVelocita());

					this.getGraph2().setEdgeWeight(tratta, tempo);
				}
			}

		}

		return grafo2;

	}

	public DirectedWeightedMultigraph<FermataSuLinea, TrattaEdgeSuLinea> getGraph2() {
		if (grafo2 == null) {
			this.grafo2 = this.createGraphSuLinea();
		}
		return this.grafo2;

	}

}
