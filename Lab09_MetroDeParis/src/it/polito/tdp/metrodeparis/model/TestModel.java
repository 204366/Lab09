package it.polito.tdp.metrodeparis.model;

import com.javadocmd.simplelatlng.LatLng;

public class TestModel {

	public static void main(String[] args) {
		
		Model model = new Model();
		//System.out.println("GRAFO********************");
		//System.out.println(model.getGraph());
		
		System.out.println("Cammino minimo");
		System.out.println(model.calcolaCamminoMinimoavanzato(new Fermata(15,"Argentine",new LatLng(2.29011,48.87537)),new Fermata(20,"Athis Mons",new LatLng(2.40332,48.71245))));
		
		
		System.out.println("tempo tot");
		System.out.println(model.tempo);
	}

}
