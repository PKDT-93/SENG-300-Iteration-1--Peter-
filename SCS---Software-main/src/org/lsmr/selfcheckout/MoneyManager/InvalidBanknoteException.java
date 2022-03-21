package org.lsmr.selfcheckout.MoneyManager;

public class InvalidBanknoteException extends Exception{

	/**
	 * Create an exception without an error message.
	 */
	public InvalidBanknoteException() {}

	/**
	 * Create an exception with an error message.
	 * 
	 * @param message
	 *            The error message to use.
	 */
	public InvalidBanknoteException(String message) {
		super(message);
	}
}
