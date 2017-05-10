package it.polito.tdp.metrodeparis.dao;

import java.util.List;

import com.javadocmd.simplelatlng.LatLng;

import it.polito.tdp.metrodeparis.model.Connessione;
import it.polito.tdp.metrodeparis.model.Fermata;
import it.polito.tdp.metrodeparis.model.FermataSuLinea;

public class TestDAO {

	public static void main(String[] args) {
		
		MetroDAO metroDAO = new MetroDAO();
		
		System.out.println("Lista fermate");
		List<Fermata> fermate = metroDAO.getAllFermate();
		System.out.println(fermate);
		
		System.out.println("Lista connessioni");
		List<Connessione> conn = metroDAO.getAllConnessioni();
		System.out.println(conn);
		
		System.out.println("Lista fermate su linea");
		//List<FermataSuLinea> f = metroDAO.getFermataSuLinea(15,"Argentine",new LatLng(2.29011,48.87537)),new Fermata(20,"Athis Mons",new LatLng(2.40332,48.71245);
		
	}

}
