package org.lsmr.selfcheckout.CheckoutManager;
/* Project: Iteration 1
 * SENG 300
   UCID     - Name
   10079497 - Peter Tran
   10057560 - Abraham Abalos
   30115250 - John Lugue
   10098895 - Dingkai Wu
   30128732 - Shaohuan Xia
   30087986 - Caleb Fedyshen   

   Date: March 20, 2022
   
   Description: This exception is thrown when the customer attempts to checkout before scanning any items
 */
public class InsufficientItemsException extends Exception{
	private static final long serialVersionUID = 7813659221550664284L;

	/**
	 * Create an exception without an error message.
	 */
	public InsufficientItemsException() {}

	/**
	 * Create an exception with an error message.
	 * 
	 * @param message
	 *            The error message to use.
	 */
	public InsufficientItemsException(String message) {
		super(message);
	}

}
