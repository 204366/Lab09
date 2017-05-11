package it.polito.tdp.metrodeparis.model;

public class Connessione {
	private int id;
	private Linea linea;
	private Fermata fermataA;
	private Fermata fermataP;
	
	public Connessione(int id, Linea linea, Fermata fermataA, Fermata fermataP) {
		super();
		this.id = id;
		this.linea = linea;
		this.fermataA = fermataA;
		this.fermataP = fermataP;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Linea getLinea() {
		return linea;
	}

	public void setLinea(Linea linea) {
		this.linea = linea;
	}

	public Fermata getFermataA() {
		return fermataA;
	}

	public void setFermataA(Fermata fermataA) {
		this.fermataA = fermataA;
	}

	public Fermata getFermataP() {
		return fermataP;
	}

	public void setFermataP(Fermata fermataP) {
		this.fermataP = fermataP;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
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
		Connessione other = (Connessione) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Connessione [id=" + id + ", linea=" + linea + ", fermataA=" + fermataA + ", fermataP=" + fermataP + "]";
	}
	
	
	
}
