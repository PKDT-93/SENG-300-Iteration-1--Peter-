package org.lsmr.selfcheckout.MoneyManager;

public class NegativeFundsException extends Exception{

	/**
	 * Create an exception without an error message.
	 */
	public NegativeFundsException() {}

	/**
	 * Create an exception with an error message.
	 * 
	 * @param message
	 *            The error message to use.
	 */
	public NegativeFundsException(String message) {
		super(message);
	}
}
