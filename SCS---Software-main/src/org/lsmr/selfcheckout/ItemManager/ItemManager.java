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
   
   Description:
   Item/Product Manager, handles scanning of items with barcodes, puts them them into
   a hashmap. The barcodes hashmap is used along with productCatalog class to calculate the total
   price and total weight of the scanned barcoded items.
 */
package org.lsmr.selfcheckout.ItemManager;

import java.math.BigDecimal;
import java.util.HashMap;

import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.devices.AbstractDevice;
import org.lsmr.selfcheckout.devices.BarcodeScanner;
import org.lsmr.selfcheckout.devices.ElectronicScale;
import org.lsmr.selfcheckout.devices.observers.AbstractDeviceObserver;
import org.lsmr.selfcheckout.devices.observers.BarcodeScannerObserver;
import org.lsmr.selfcheckout.devices.observers.ElectronicScaleObserver;

public class ItemManager {
	
	private HashMap<Barcode, Integer> barcodes;
	private BarcodeScanner scanner;
	private ElectronicScale scale;
	private ProductCatalog productDatabase;
	private BigDecimal totalPrice;
	private double bagAreaWeight;
	private double totalWeight;
	private boolean weightError;
	
	private class BSO implements BarcodeScannerObserver {

		@Override
		public void enabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
			// Not needed
		}

		@Override
		public void disabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
			// Not needed
		}

		@Override
		public void barcodeScanned(BarcodeScanner barcodeScanner, Barcode barcode) {
			// Add barcode to the list of barcodes
			addBarcode(barcode);
		}
	}
	
	private class ESO implements ElectronicScaleObserver {

		@Override
		public void enabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
			// Not needed
		}

		@Override
		public void disabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
			// Not needed
		}

		@Override
		public void weightChanged(ElectronicScale scale, double weightInGrams) {
			/* When the ElectronicScaleObserver is notified by ElectronicScale 
			 * This will update the bagAreaWeight and verify totalWeight */
			bagAreaWeight = weightInGrams;
			verifyBaggingAreaWeight();
		}

		@Override
		public void overload(ElectronicScale scale) {
			weightError = true;
		}

		@Override
		public void outOfOverload(ElectronicScale scale) {
			weightError = false;
		}
		
	}
	
	/* Constructor for ItemManager 
	 * Parameter: BarcodeScanner object needed in order to attach observer 
	 */
	public ItemManager(BarcodeScanner bScanner, ElectronicScale eScale) {
		scanner = bScanner;
		scale = eScale;
		
		if (scanner == null)
			throw new NullPointerException("Provided BarcodeScanner object is null.");
		if (scale == null)
			throw new NullPointerException("Provided ElectronicScanner object is null.");
		
		scanner.attach(new BSO());
		scale.attach(new ESO());
		
		/* Initialize barcodes, totalPrice, totalWeight, bagAreaWeight, weightError */
		barcodes = new HashMap<>();
		totalPrice = BigDecimal.valueOf(0);
		totalWeight = 0;
		bagAreaWeight = 0;
		weightError = false;
		productDatabase = null;
	}
	
	/* Links database of products to item manager */
	public void assignProductDatabase(ProductCatalog products) {
		productDatabase = products;
	}

	/* Add the barcode to the barcode hashmap if it is valid.
	 * Update the totals by using validated barcode.
	 * Parameter: Barcode object
	 */
	public void addBarcode(Barcode barcode) {
		if (productDatabase == null) {
			throw new NullPointerException("Product database is unassigned or is null.");
		}
		if (checkProductExists(barcode) && !weightError) {
			if (barcodes.containsKey(barcode)) {
				/* Increment count of that barcode */
				barcodes.replace(barcode, barcodes.get(barcode) + 1);
			} else {
				/* Add the new barcode to HashMap */
				barcodes.put(barcode, 1);
			}
			try {
				/* Add product price to totalPrice */
				totalPrice = totalPrice.add(productDatabase.getPrice(barcode));
				/* Add to product weight to totalWeight */
				totalWeight += productDatabase.getWeight(barcode);	
			} catch (notInInventoryException e) {
				/* Error: barcode doesn't have an associated price and/or weight */
				System.out.println("Not in inventory!");
			}
		} else if (weightError) {
			/* System message: Clear bagging area weight error */
			System.out.println("Item not in bagging area");
		} else {
			/* System message: Barcode not in database */
			System.out.println("Barcode not in database!");
			throw new NullPointerException("Barcode is not in the database.");
		}
	}
	
	/* Checks if the product is in the product database by getting its price.
	 * Products entered in product catalog must have a price, weight, description. */
	public boolean checkProductExists(Barcode barcode) {
		try {
			productDatabase.getPrice(barcode);
			return true;
		} catch (notInInventoryException e) {
			return false;
		}
	}
	
	/* Sets weightError field based on if baggingAreaWeight does not match totalWeight */
	private void verifyBaggingAreaWeight() {
		if (totalWeight != bagAreaWeight)
			weightError = true;
		else
			weightError = false;
	}
	
	public BigDecimal getTotalPrice() {
		return totalPrice;
	}
	
	public double getTotalWeight() {
		return totalWeight;
	}
	
	/* Returns true when there is an error, else returns false */
	public boolean getWeightStatus() {
		return weightError;
	}
	
	public HashMap<Barcode, Integer> getBarcodes() {
		return barcodes;
	}
	public ProductCatalog getDatabase() {
		return productDatabase;
	}
	/* Set total weight for testing purposes */
	public void setTotalWeight(double totalWeight) {
		this.totalWeight = totalWeight;
	}
	
	/* Set total price for testing purposes */
	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}	
	
}
