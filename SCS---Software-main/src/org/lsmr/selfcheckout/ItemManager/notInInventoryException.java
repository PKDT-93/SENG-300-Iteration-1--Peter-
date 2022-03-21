package org.lsmr.selfcheckout.ItemManager;

public class notInInventoryException extends Exception{
	private static final long serialVersionUID = 7813659561550664284L;

	/**
	 * Create an exception without an error message.
	 */
	public notInInventoryException() {}

	/**
	 * Create an exception with an error message.
	 * 
	 * @param message
	 *            The error message to use.
	 */
	public notInInventoryException(String message) {
		super(message);
	}
}
