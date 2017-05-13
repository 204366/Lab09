package it.polito.tdp.metrodeparis.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.javadocmd.simplelatlng.LatLng;

import it.polito.tdp.metrodeparis.model.Connessione;
import it.polito.tdp.metrodeparis.model.Fermata;
import it.polito.tdp.metrodeparis.model.FermataSuLinea;
import it.polito.tdp.metrodeparis.model.Linea;

public class MetroDAO {

	
	public List<Fermata> getAllFermate() {

		final String sql = "SELECT id_fermata, nome, coordx, coordy FROM fermata ORDER BY nome ASC";
		List<Fermata> fermate = new ArrayList<Fermata>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Fermata f = new Fermata(rs.getInt("id_Fermata"), rs.getString("nome"), new LatLng(rs.getDouble("coordx"), rs.getDouble("coordy")));
				fermate.add(f);
			}

			st.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore di connessione al Database.");
		}

		return fermate;
	}
	/*
	public List<FermataSuLinea> getFermataSuLinea(Fermata p) {
		

		final String sql = "SELECT distinct f1.id_fermata as idF1, f1.nome as nome1, f1.coordx as x1, f1.coordy as y1,"
				+ " linea.id_linea, linea.nome, velocita, intervallo  FROM fermata as f1,linea,connessione "
				+ "WHERE id_fermata = ? AND id_fermata = connessione.id_stazA "+
				"AND connessione.id_linea = linea.id_linea";
		
		List<FermataSuLinea> fermate = new ArrayList<FermataSuLinea>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, p.getIdFermata());
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				FermataSuLinea f = new FermataSuLinea(rs.getInt("idF1"), rs.getString("nome1"),
						new Linea(rs.getInt("id_linea"),rs.getString("nome"), rs.getDouble("velocita"), rs.getDouble("intervallo")),
						new LatLng(rs.getDouble("x1"), rs.getDouble("y1")));
				fermate.add(f);
			}

			st.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore di connessione al Database.");
		}

		return fermate;
	}
	*/
	
public List<FermataSuLinea> getFermataSuLinea() {
		

		final String sql = "SELECT distinct f1.id_fermata as idF1, f1.nome as nome1, f1.coordx as x1, f1.coordy as y1,"
				+ " linea.id_linea, linea.nome, velocita, intervallo  FROM fermata as f1,linea,connessione "
				+ "WHERE id_fermata = connessione.id_stazA "+
				"AND connessione.id_linea = linea.id_linea";
		
		List<FermataSuLinea> fermate = new ArrayList<FermataSuLinea>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				FermataSuLinea f = new FermataSuLinea(rs.getInt("idF1"), rs.getString("nome1"),
						new Linea(rs.getInt("id_linea"),rs.getString("nome"), rs.getDouble("velocita"), rs.getDouble("intervallo")),
						new LatLng(rs.getDouble("x1"), rs.getDouble("y1")));
				fermate.add(f);
			}

			st.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore di connessione al Database.");
		}

		return fermate;
	}
	
	
	public List<Connessione> getAllConnessioni() {

		final String sql = "SELECT id_connessione,f1.id_fermata as idF1, f1.nome as nome1, f1.coordx as x1, f1.coordy as y1, "+
				"f2.id_fermata as idF2, f2.nome as nome2, f2.coordx as x2, f2.coordy as y2, " +
				"linea.id_linea, linea.nome, velocita, intervallo "+
				"FROM connessione, fermata f1, fermata f2, linea "+
				"WHERE connessione.id_stazP = f1.id_fermata AND connessione.id_stazA = f2.id_fermata AND "
				+ "connessione.id_linea = linea.id_linea";
		
		List<Connessione> connessioni = new ArrayList<Connessione>();

		try {
			Connection conn = DBConnect.getInstance().getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				Connessione c = new Connessione(rs.getInt("id_connessione"), 
						new Linea(rs.getInt("id_linea"),rs.getString("nome"), rs.getDouble("velocita"), rs.getDouble("intervallo")), 
						new Fermata(rs.getInt("idF2"), rs.getString("nome2"), new LatLng(rs.getDouble("x2"), rs.getDouble("y2"))),
						new Fermata(rs.getInt("idF1"), rs.getString("nome1"), new LatLng(rs.getDouble("x1"), rs.getDouble("y1"))));
				connessioni.add(c);
			}

			st.close();
			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore di connessione al Database.");
		}

		return connessioni;
	}
}
