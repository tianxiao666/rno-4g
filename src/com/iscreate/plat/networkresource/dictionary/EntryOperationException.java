package com.iscreate.plat.networkresource.dictionary;


public class EntryOperationException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8466758892024378100L;

	EntryOperationConsequence consequence = null;

	public EntryOperationException(EntryOperationConsequence consequence) {
		this.consequence = consequence;
	}
	
	public EntryOperationConsequence getConsequence() {
		return consequence;
	}


	public String toString() {
		return consequence.getCode() + ":" + consequence.getConsequence();
	}

}
