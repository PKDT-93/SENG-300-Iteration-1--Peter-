package org.lsmr.selfcheckout.test;
import org.junit.Assert;
import org.junit.Before;
import org.junit.rules.ExpectedException;
import org.lsmr.selfcheckout.CheckoutManager.CheckoutManager;
import org.lsmr.selfcheckout.CheckoutManager.InsufficientItemsException;
import org.lsmr.selfcheckout.ItemManager.ItemManager;
import org.lsmr.selfcheckout.ItemManager.ProductCatalog;
import org.lsmr.selfcheckout.ItemManager.notInInventoryException;
import org.lsmr.selfcheckout.MoneyManager.MoneyManager;
import org.lsmr.selfcheckout.MoneyManager.NegativeFundsException;
import org.lsmr.selfcheckout.devices.DisabledException;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;
import org.lsmr.selfcheckout.products.BarcodedProduct;

import java.math.BigDecimal;
import org.lsmr.selfcheckout.CheckoutManager.CheckoutWatcher;
import org.lsmr.selfcheckout.CheckoutManager.InsufficientFundsException;
import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.BarcodedItem;
import org.lsmr.selfcheckout.Coin;
import org.lsmr.selfcheckout.Numeral;

import java.util.Currency;
import java.util.Locale;
/**
 * This test class is for testing the CheckoutManager
 */
public class CheckoutManagerTestFinal {
	private ItemManager im;
	private MoneyManager mm;
	private CheckoutWatcher cw;
	private Coin coin;
	private BigDecimal coinvalue;
	private BigDecimal coinvalue2;
	private BigDecimal coinvalue3;
	private SelfCheckoutStation scs;
	private Barcode barcode1;
	private Numeral[] numeral1;
	private BarcodedProduct appleProduct;
	private BarcodedItem appleItem;
	private Barcode barcode2;
	private Numeral[] numeral2;
	private BarcodedProduct watermelonProduct;
	private BarcodedItem watermelonItem;
	private ProductCatalog pCatalog;
/**
 * Setting up the simulation
 */
	@Before
	public void setup(){
		Currency CAD = Currency.getInstance(Locale.CANADA);
		int[] bd = {1,5,10};
		
		coinvalue = new BigDecimal(1);
		coinvalue2 = new BigDecimal(0.1);
		coinvalue3 = new BigDecimal(0.5);
		BigDecimal[] cd = {coinvalue,coinvalue2};
		scs = new SelfCheckoutStation(CAD, bd, cd, 50, 1);
		
		

		coin = new Coin(CAD,coinvalue);
		mm = new MoneyManager(scs.banknoteInput,scs.coinSlot, scs.coinDispensers, scs.banknoteDispensers);
		im = new ItemManager(scs.scanner, scs.scale);
		cw = new CheckoutWatcher();
		
		// "Apple" product, barcode = 10, price = 2, weight = 100.0
		numeral1 = new Numeral[2];
		numeral1[0] = Numeral.one; numeral1[1] = Numeral.zero; // 10
		barcode1 = new Barcode(numeral1); // barcode is 10
		appleProduct = new BarcodedProduct(barcode1, "Apple", BigDecimal.valueOf(6));
		appleItem = new BarcodedItem(barcode1, 100.0);
		
		// "Watermelon" product, barcode = 11, price = 10, weight = 7000.0
		numeral2 = new Numeral[2];		
		numeral2[0] = Numeral.one; numeral2[1] = Numeral.one;  // 11		
		barcode2 = new Barcode(numeral2);
		watermelonProduct = new BarcodedProduct(barcode2, "Watermelon", BigDecimal.valueOf(3));
		watermelonItem = new BarcodedItem(barcode2, 7000.0);
		
		pCatalog = new ProductCatalog();
		im.assignProductDatabase(pCatalog);
		pCatalog.addProduct(appleProduct, appleItem);
		pCatalog.addProduct(watermelonProduct, watermelonItem);
		im.addBarcode(barcode1);
		im.addBarcode(barcode2);
}
	/**
	 * This test case it used to test if the correct exception will be thrown when the total weight is
	 * less than zero
	 */
	@SuppressWarnings("deprecation")
	@org.junit.Rule
	public ExpectedException exceptionRule = ExpectedException.none();
	public ExpectedException exceptionRule1 = ExpectedException.none();
	boolean Nonzeroweight_accessed = false;
	@org.junit.Test
    public void testDatabaseWeightLessthanZero() throws InsufficientItemsException, DisabledException {

		im.setTotalWeight(-1);
		CheckoutManager cm = new CheckoutManager(im,mm);
		exceptionRule.expect(InsufficientItemsException.class);
		cm.verifyWeight();
	}
	
	/**
	 * This test case it used to test if cm.verifyWeight() returns true when total weight
	 * is greater than zero
	 */
	@org.junit.Test
    public void testDatabaseWeightGreaterthanZero() throws InsufficientItemsException {
		im.setTotalWeight(1);
		CheckoutManager cm = new CheckoutManager(im,mm);
		cm.register(cw);
		Assert.assertTrue(cm.verifyWeight());
        
	}
	
	/**
	 * This test case it used to test if cm.verifyPayment() returns true when total funds
	 * is greater than price
	 */
	@org.junit.Test
    public void testFundGreaterThanCost() throws InsufficientItemsException, NegativeFundsException, InsufficientFundsException {
		mm.setTotalBanknotes(coinvalue);
		mm.setTotalCoins(coinvalue2);
		im.setTotalPrice(coinvalue);
		CheckoutManager cm = new CheckoutManager(im,mm);
		cm.register(cw);
		Assert.assertTrue(cm.verifyPayment());
        
	}
	
	/**
	 * This test case it used to test if cm.verifyPayment() throws exception when 
	 * the total funds is less than the total price
	 */
	@org.junit.Test
    public void testFundLessThanCost() throws InsufficientItemsException, NegativeFundsException, InsufficientFundsException {
		mm.setTotalBanknotes(coinvalue2);
		mm.setTotalCoins(coinvalue2);
		im.setTotalPrice(coinvalue);
		CheckoutManager cm = new CheckoutManager(im,mm);
		cm.register(cw);
		exceptionRule.expect(InsufficientFundsException.class);
		cm.verifyPayment();
        
	}
	
	/**
	 * This test case it used to test if cm.verifyPayment() returns true when total funds
	 * is equal to the total price
	 */
	@org.junit.Test
    public void testFundEqualsToCost() throws InsufficientItemsException, NegativeFundsException, InsufficientFundsException {
		mm.setTotalBanknotes(coinvalue3);
		mm.setTotalCoins(coinvalue3);
		im.setTotalPrice(coinvalue);
		CheckoutManager cm = new CheckoutManager(im,mm);
		cm.register(cw);
		Assert.assertTrue(cm.verifyPayment());
        
	}
	
	/**
	 * This test prints the reciept
	 */
	@org.junit.Test
    public void testReceipt(){
		CheckoutManager cm = new CheckoutManager(im,mm);
		cm.register(cw);
        cm.printReceipt(mm,im);
        
	}
	
	@org.junit.Test
    public void testItemNotInInventory() throws notInInventoryException{
		CheckoutManager cm = new CheckoutManager(im,mm);
		cm.register(cw);
		im.getDatabase().removeProduct(appleProduct, appleItem);
		exceptionRule.expect(notInInventoryException.class);
		cm.printReceipt(mm, im);
		im.getDatabase().removeProduct(appleProduct, appleItem);

        
	}
	
}
