package org.lsmr.selfcheckout.MoneyManager;

public class InvalidCoinException extends Exception{

	/**
	 * Create an exception without an error message.
	 */
	public InvalidCoinException() {}

	/**
	 * Create an exception with an error message.
	 * 
	 * @param message
	 *            The error message to use.
	 */
	public InvalidCoinException(String message) {
		super(message);
	}
}
