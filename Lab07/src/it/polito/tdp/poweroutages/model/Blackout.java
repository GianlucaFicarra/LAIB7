package it.polito.tdp.poweroutages.model;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class Blackout { //JAVABEAN

	private int id;     //codice del blackout
	private Nerc nerc; //essendo relazione 1:N
	private int customerAffercted; //clienti colpiti dal disservizio
	
	private LocalDateTime blackStart; //datainizio
	private LocalDateTime blackEnd; //datafine
	
	private long outageDuration; //durata del blackout corrente (end-start)
	private int year;
	
	
	public Blackout(int id, Nerc nerc, int customerAffercted, LocalDateTime blackStart, LocalDateTime blackEnd) {
		this.id = id;
		this.nerc = nerc;
		this.customerAffercted = customerAffercted;
		this.blackStart = blackStart;
		this.blackEnd = blackEnd;
		
		//per inizzializzare l'intervallo di blackout:
		//salvo la data locale del momento di inizio blackout, e vedo quando dista in ore da quello di fine
		LocalDateTime tempDateTime = LocalDateTime.from(blackStart);
		this.outageDuration = tempDateTime.until(blackEnd, ChronoUnit.HOURS);

		this.year = blackStart.getYear(); //salvo anno del momento di inizio
		
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public Nerc getNerc() {
		return nerc;
	}


	public void setNerc(Nerc nerc) {
		this.nerc = nerc;
	}


	public int getCustomerAffercted() {
		return customerAffercted;
	}


	public void setCustomerAffercted(int customerAffercted) {
		this.customerAffercted = customerAffercted;
	}


	public LocalDateTime getBlackStart() {
		return blackStart;
	}


	public void setBlackStart(LocalDateTime blackStart) {
		this.blackStart = blackStart;
	}


	public LocalDateTime getBlackEnd() {
		return blackEnd;
	}


	public void setBlackEnd(LocalDateTime blackEnd) {
		this.blackEnd = blackEnd;
	}


	public long getOutageDuration() {
		return outageDuration;
	}


	public void setOutageDuration(long outageDuration) {
		this.outageDuration = outageDuration;
	}


	public int getYear() {
		return year;
	}


	public void setYear(int year) {
		this.year = year;
	}


	//blackout sono uguali se hanno lo stesso id
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
		Blackout other = (Blackout) obj;
		if (id != other.id)
			return false;
		return true;
	}
	
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(id);
		return builder.toString();
	}

	
	
}
