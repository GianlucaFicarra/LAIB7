package it.polito.tdp.poweroutages.db;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.poweroutages.model.BlackIdMap;
import it.polito.tdp.poweroutages.model.Blackout;
import it.polito.tdp.poweroutages.model.Nerc;
import it.polito.tdp.poweroutages.model.NercIdMap;

public class PowerOutageDAO {

	public List<Nerc> getNercList(NercIdMap nercIdMap) {

		String sql = "SELECT id, value FROM nerc";
		List<Nerc> nercList = new ArrayList<>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Nerc n = new Nerc(res.getInt("id"), res.getString("value"));
				nercList.add(nercIdMap.get(n));
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return nercList;
	}

	
	
	public List<Blackout> getAllBlack(BlackIdMap blackmap, NercIdMap nercmap) {
		String sql = "SELECT id, nerc_id, date_event_began, date_event_finished, customers_affected FROM poweroutages";
		List<Blackout> blackList = new ArrayList<>();

		try {
			Connection conn = ConnectDB.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				//vedo se il nerc passato esiste
				Nerc n = nercmap.get(res.getInt("nerc_id"));
				if (n == null) {
					System.err.println("Database is not consistent. Missing corresponding NERC");
				} else {
				
				
				//aggiungo nerk al black   da mappa nerc recupero nerc passando id perche il costruttore di blacout vuole nerc
				Blackout b = new Blackout(res.getInt("id"),n, res.getInt("customers_affected"),
						res.getTimestamp("date_event_began").toLocalDateTime(),
						res.getTimestamp("date_event_finished").toLocalDateTime());
				blackList.add(b);
				
				//avendo riferimento ai due oggetti già qui creo i riferimenti
				//-->aggiungo black al nerk
				n.setBlackout(blackList);
				}
				
			}

			conn.close();

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return blackList;
	}


	
}
