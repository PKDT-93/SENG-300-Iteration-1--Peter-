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
   
   Description: Observes events emanating from a CheckoutManager.
 */

public interface CheckoutObserver {
	
	/**
	 * An event announcing that some amount of weight is on the scale
	 * and the user can proceed to checkout
	 * 
	 * @param slot
	 *            The manager on which the event occurred.
	 */
	public void notifyNonzeroWeight(CheckoutManager checkout);
	
	
	/**
	 * An event announcing that the customer has inserted enough money into the machine to
	 * accept the transaction
	 * 
	 * @param slot
	 *            The manager on which the event occurred.
	 */
	public void notifyPaymentVerified(CheckoutManager checkout);
	
}
