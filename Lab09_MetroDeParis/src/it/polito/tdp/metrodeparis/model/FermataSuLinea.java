package it.polito.tdp.metrodeparis.model;

import com.javadocmd.simplelatlng.LatLng;

public class FermataSuLinea {
	private int idFermata;
	private String nome;
	private Linea linea;
	private LatLng coords;
	
	public FermataSuLinea(int idFermata, String nome, Linea linea, LatLng coords) {
		super();
		this.idFermata = idFermata;
		this.nome = nome;
		this.linea = linea;
		this.coords = coords;
	}

	public int getIdFermata() {
		return idFermata;
	}

	public void setIdFermata(int idFermata) {
		this.idFermata = idFermata;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Linea getLinea() {
		return linea;
	}

	public void setLinea(Linea linea) {
		this.linea = linea;
	}

	public LatLng getCoords() {
		return coords;
	}

	public void setCoords(LatLng coords) {
		this.coords = coords;
	}

	@Override
	public String toString() {
		return "FermataSuLinea [idFermata=" + idFermata + ", nome=" + nome + ", linea=" + linea + ", coords=" + coords
				+ "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idFermata;
		result = prime * result + ((linea == null) ? 0 : linea.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FermataSuLinea other = (FermataSuLinea) obj;
		if (idFermata != other.idFermata)
			return false;
		if (linea == null) {
			if (other.linea != null)
				return false;
		} else if (!linea.equals(other.linea))
			return false;
		return true;
	}
	
	

}
