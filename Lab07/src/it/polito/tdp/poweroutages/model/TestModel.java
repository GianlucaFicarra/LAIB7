package it.polito.tdp.poweroutages.model;

import java.util.List;

public class TestModel {

	public static void main(String[] args) {
		
		Model model = new Model();
		System.out.println(model.getNercList());
		
		
	 //(1)approccio top-down inizio dal test model, da qui chiamo metodo nel model che mi calcoli la superficie di blackout
		List<Nerc> nercList = model.getNercList();
		System.out.println("Nerc List size: " + nercList.size());
		
		Nerc selectedNerc = nercList.get(3);
		List<Blackout> worstCase = model.trovaSoluzione(selectedNerc,3, 250);
		
		System.out.println("Tot people affected: " + model.contaDisserviti(worstCase));
		System.out.println("Tot hours of outage: " + model.sumBlackoutHours(worstCase));
		
		for (Blackout event : worstCase) {
			System.out.println(String.format("%d %s %s %d %d", event.getYear(), event.getBlackStart(),
					event.getBlackEnd(), event.getOutageDuration(), event.getCustomerAffercted()));
		}
		/*selezionare il sottoinsieme di eventi di blackout che si sono verificati in un massimo di X anni, per un
          totale di Y ore di disservizio massimo, tale da massimizzare il numero totale di persone coinvolte.*/
		
	}
	
	
}
