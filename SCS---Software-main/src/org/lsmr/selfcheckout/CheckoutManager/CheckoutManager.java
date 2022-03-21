package org.lsmr.selfcheckout.CheckoutManager;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.ItemManager.ItemManager;
import org.lsmr.selfcheckout.ItemManager.ProductCatalog;
import org.lsmr.selfcheckout.ItemManager.notInInventoryException;
import org.lsmr.selfcheckout.MoneyManager.MoneyManager;
import org.lsmr.selfcheckout.MoneyManager.NegativeFundsException;

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
   
   Description: This class implements functions required for the customer to check out 
 */
public class CheckoutManager{
	
	private final Set<CheckoutObserver> observers = new HashSet<>();

	private MoneyManager mm;
	private ItemManager  im;
	public CheckoutManager(ItemManager im, MoneyManager mm) {
		this.im = im;
		this.mm = mm;
	}
	
	//When we have a user interface the following function will be triggered by a ButtonObserver by the Checkout Button
	//This process must be verified before user can proceed to payment
	public boolean verifyWeight() throws InsufficientItemsException{
		
		boolean nonzero = false;
		Double databaseWeight = im.getTotalWeight();
		
		if(databaseWeight > 0) {
			nonzero = true;
			for(CheckoutObserver observer : observers)
				observer.notifyNonzeroWeight(this);
		}
		else {
			throw new InsufficientItemsException();
		}
		
		return nonzero;
	}
	
	//This function is called when the user is finished paying, and it verifies that enough money hass been inserted into the machine
	public boolean verifyPayment() throws InsufficientFundsException, NegativeFundsException{
		
		boolean sufficientFunds = false;
		BigDecimal totalCoins = mm.getTotalCoins();
		BigDecimal totalBanknotes = mm.getTotalBanknotes();
		BigDecimal fundsAmt = mm.sum_total(totalCoins, totalBanknotes);
		BigDecimal costAmt  = im.getTotalPrice();
		
		if(fundsAmt.compareTo(costAmt) == 0 || fundsAmt.compareTo(costAmt) == 1){
			sufficientFunds = true;
			for(CheckoutObserver observer : observers)
				observer.notifyPaymentVerified(this);
		}
		else {
			throw new InsufficientFundsException();
		}
		return sufficientFunds;
	}
	
	// Prints receipt if payment is successfully verified.
	public void printReceipt(MoneyManager mm, ItemManager im) {
		HashMap<Barcode, Integer> barcodes = im.getBarcodes();
		ProductCatalog pc = im.getDatabase();
		for (Map.Entry<Barcode, Integer> pair: barcodes.entrySet()) {
			Barcode key = pair.getKey();
			int count = pair.getValue();
			for(int i=0; i < count; i++) {
				try {
					String S = NumberFormat.getCurrencyInstance().format(pc.getPrice(key));
					System.out.format("Item:  %s \nPrice: " + S +" \n", pc.getDescription(key));
				} catch (notInInventoryException e) {
				}
			}
		}
		String t = NumberFormat.getCurrencyInstance().format(im.getTotalPrice());
		System.out.format("Total Price::  %s \n\n", t);
	}
	
	public void register(CheckoutObserver observer) {
		observers.add(observer);
	}
	
}
