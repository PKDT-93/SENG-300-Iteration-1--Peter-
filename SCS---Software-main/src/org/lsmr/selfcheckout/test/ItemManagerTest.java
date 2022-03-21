/* Project: Iteration 1
   SENG 300
   UCID     - Name
   10079497 - Peter Tran
   10057560 - Abraham Abalos
   30115250 - John Lugue
   10098895 - Dingkai Wu
   30128732 - Shaohuan Xia
   30087986 - Caleb Fedyshen   

   Date: March 20, 2022
   
   Description:
   Test cases for item manager and all its methods, fields, and other nested classes.
 */

package org.lsmr.selfcheckout.test;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;
import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.BarcodedItem;
import org.lsmr.selfcheckout.Numeral;
import org.lsmr.selfcheckout.ItemManager.ItemManager;
import org.lsmr.selfcheckout.ItemManager.ProductCatalog;
import org.lsmr.selfcheckout.ItemManager.notInInventoryException;
import org.lsmr.selfcheckout.devices.BarcodeScanner;
import org.lsmr.selfcheckout.devices.ElectronicScale;
import org.lsmr.selfcheckout.devices.SimulationException;
import org.lsmr.selfcheckout.products.BarcodedProduct;


public class ItemManagerTest {

	private ItemManager items;
	private ElectronicScale eScale;
	private BarcodeScanner bScanner;
	private ProductCatalog pCatalog;
	private Numeral[] numeral1;
	private Numeral[] numeral2;
	private Barcode barcode1;
	private Barcode barcode2;
	private BarcodedProduct appleProduct;
	private BarcodedProduct watermelonProduct;
	private BarcodedItem appleItem;
	private BarcodedItem watermelonItem;
	
	@Before public void setUp() {
		eScale = new ElectronicScale(10000, 10);
		bScanner = new BarcodeScanner();
		eScale.endConfigurationPhase();
		bScanner.endConfigurationPhase();
		pCatalog = new ProductCatalog();
		
		// "Apple" product, barcode = 10, price = 2, weight = 100.0
		numeral1 = new Numeral[2];
		numeral1[0] = Numeral.one; numeral1[1] = Numeral.zero; // 10
		barcode1 = new Barcode(numeral1); // barcode is 10
		appleProduct = new BarcodedProduct(barcode1, "Apple", BigDecimal.valueOf(2));
		appleItem = new BarcodedItem(barcode1, 100.0);
		
		// "Watermelon" product, barcode = 11, price = 10, weight = 7000.0
		numeral2 = new Numeral[2];		
		numeral2[0] = Numeral.one; numeral2[1] = Numeral.one;  // 11		
		barcode2 = new Barcode(numeral2);
		watermelonProduct = new BarcodedProduct(barcode2, "Watermelon", BigDecimal.valueOf(10));
		watermelonItem = new BarcodedItem(barcode2, 7000.0);
		
		items = new ItemManager(bScanner, eScale);
		items.assignProductDatabase(pCatalog);
		pCatalog.addProduct(appleProduct, appleItem);
		pCatalog.addProduct(watermelonProduct, watermelonItem);
	}
	
	/* Test for when BarcodeScanner passed in is null */
	@Test (expected = NullPointerException.class)
	public void testNullBarcodeScanner() {
		items = new ItemManager(null, eScale);
		
	}
	
	/* Test for when ElectronicScale passed in is null */
	@Test (expected = NullPointerException.class)
	public void testNullElectronicScale() {
		items = new ItemManager(bScanner, null);
	}
	
	/* Test case for null productDatabase */
	@Test (expected = NullPointerException.class)
	public void testNullProductDatabase() {
		items = new ItemManager(bScanner, eScale);
		items.assignProductDatabase(null);
		items.addBarcode(barcode1);
	}
	
	/* Test constructor initial configuration */
	@Test
	public void testConstructor() {
		BigDecimal price = items.getTotalPrice();
		double weight = items.getTotalWeight();
		boolean weightError = items.getWeightStatus();
		assertEquals(BigDecimal.valueOf(0), price);
		assertEquals(0, weight, 0.001);
		assertFalse(weightError);
	}
	
	/* Barcode Equality Test */
	@Test
	public void testBarcodeEquality() {
		assertEquals(barcode1, appleProduct.getBarcode());
	}
	
	/* Test for negative price (Products throw a SimulationException when given negative price) */
	@Test (expected = SimulationException.class)
	public void testNegativePrice() {
		appleProduct = new BarcodedProduct(barcode1, "Apple", BigDecimal.valueOf(-1));
	}
	
	/* Test for negative weight (Items throw a SimulationException when given negative weight */
	@Test (expected = SimulationException.class)
	public void testNegativeWeight() {
		appleItem = new BarcodedItem(barcode1, -1);
	}
	
	/* Test total weight is equal to weight of scanned items */
	@Test
	public void testTotalWeight() {
		double weight;
		
		// Total weight for multiple of the same type of item
		bScanner.scan(appleItem);
		bScanner.scan(appleItem);
		weight = items.getTotalWeight();
		/* We check for three values because BarcodeScanner has a probability to fail a scan */
		assertTrue(weight == 0 || weight == 100 || weight == 200);
		
		// Total weight for different items
		bScanner.scan(watermelonItem);
		weight = items.getTotalWeight();
		assertTrue(weight == 0 || weight == 100 || weight == 200 || weight == 7000 
				|| weight == 7100 || weight == 7200);
		
		items.setTotalWeight(7200);
		assertEquals(7200, items.getTotalWeight(), 0.001);
	}
	
	/* Test total price matches the price of items */
	@Test
	public void testTotalPrice() throws notInInventoryException {
		BigDecimal price;
		
		// Total price for multiple of the same type of item
		bScanner.scan(appleItem);
		bScanner.scan(appleItem);
		// items.setTotalPrice(BigDecimal.valueOf(4));
		price = items.getTotalPrice();
		System.out.println(price);
		/* We check for three values because BarcodeScanner has a probability to fail a scan */
		assertTrue(price.equals(BigDecimal.valueOf(0)) || price.equals(BigDecimal.valueOf(2)) 
				|| price.equals(BigDecimal.valueOf(4)));
		
		// Total price for different items
		bScanner.scan(watermelonItem);
		price = items.getTotalPrice();
		assertTrue(price.equals(BigDecimal.valueOf(0)) || price.equals(BigDecimal.valueOf(2)) 
				|| price.equals(BigDecimal.valueOf(4)) || price.equals(BigDecimal.valueOf(10))
				|| price.equals(BigDecimal.valueOf(12)) || price.equals(BigDecimal.valueOf(14)));
		
		items.setTotalPrice(BigDecimal.valueOf(14));
		assertEquals(BigDecimal.valueOf(14), items.getTotalPrice());
	}

	/* Test product is put into item manager (by comparing its value) */
	@Test
	public void testBarcodeList() {
		HashMap<Barcode, Integer> barcodes = items.getBarcodes();
		items.addBarcode(barcode1);
		items.addBarcode(barcode1);
		items.addBarcode(barcode2);
		
		/* Look up each barcode and test amount of each item */
		int amount = barcodes.get(barcode1);
		assertEquals(2, amount);
		amount = barcodes.get(barcode2);
		assertEquals(1, amount);
	}
	
	/* Test exception when product is not in product catalog */
	@Test (expected = NullPointerException.class)
	public void testProductNotFound() throws notInInventoryException {
		pCatalog.removeProduct(appleProduct, appleItem);
		items.addBarcode(barcode1);
	}
	
	/* Test weight error (when product is not put in bagging area of the electronic scale */
	@Test
	public void testBaggingAreaWeightError() {
		/* This is a weight mismatch, barcode1 = apple */
		eScale.add(watermelonItem);
		items.addBarcode(barcode1);
		items.addBarcode(barcode2);
	}
	
	/* Test for no weight errors */
	@Test
	public void testBaggingAreaNoError() {
		items.addBarcode(barcode1);
		eScale.add(appleItem);
		items.addBarcode(barcode2);
		eScale.add(watermelonItem);
	}
	
	/* Test if weight on scale match or doesn't match */
	@Test
	public void testProductWeights() {
		items.addBarcode(barcode1);
		items.addBarcode(barcode2);
		eScale.add(appleItem);
		eScale.add(watermelonItem);
		assertFalse(items.getWeightStatus());
		
		eScale.remove(watermelonItem);
		assertTrue(items.getWeightStatus());
	}
	
	/* Test if observers are reporting if devices are enabled, disabled, overloaded, out of overload */
	@Test
	public void testDeviceStates() {
		eScale.disable();
		eScale.enable();
		bScanner.disable();
		bScanner.enable();
		eScale.add(watermelonItem);
		eScale.remove(watermelonItem);
		watermelonItem = new BarcodedItem(barcode2, 11000.0);
		eScale.add(watermelonItem);
		assertTrue(items.getWeightStatus());
		eScale.remove(watermelonItem);
		assertFalse(items.getWeightStatus());
	}	
}
