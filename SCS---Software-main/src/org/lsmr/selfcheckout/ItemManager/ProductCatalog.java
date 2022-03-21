package org.lsmr.selfcheckout.ItemManager;

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
   
   Description: Database class which stores attributes of given BarcodedProducts and BarcodedItems
   into hashmaps. Provides a database for ItemManager.
   
 */

import java.math.BigDecimal;
import java.util.HashMap;
import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.BarcodedItem;
import org.lsmr.selfcheckout.products.BarcodedProduct;

public class ProductCatalog {
	private HashMap<Barcode, BarcodedProduct> products;
	private HashMap<Barcode, BarcodedItem> items;
	private HashMap<Barcode, BigDecimal> catalogPrices;
	private HashMap<Barcode, Double> catalogWeights;
	private HashMap<Barcode, String> catalogDescriptions;
	
	public ProductCatalog() {
		products = new HashMap<>();
		items = new HashMap<>();
		catalogPrices = new HashMap<>();
		catalogWeights = new HashMap<>();
		catalogDescriptions = new HashMap<>();
	}
	
       /**
	* Method to add products into database
	* 
	* @param barcodedItem Barcoded item needed to add into item database
	* @param barcodedProduct Barcoded product needed to add into product database
	*/
	public void addProduct(BarcodedProduct barcodedProduct, BarcodedItem barcodedItem) {
		if(barcodedProduct == null)
			throw new NullPointerException("Barcoded product given is null");

		if(barcodedItem == null)
			throw new NullPointerException("Barcoded item given is null");
		
		if(!barcodedProduct.getBarcode().equals(barcodedItem.getBarcode()))
			throw new IllegalArgumentException("Product and item given are not the same");
		
		products.putIfAbsent(barcodedProduct.getBarcode(), barcodedProduct);
		items.putIfAbsent(barcodedItem.getBarcode(), barcodedItem);
		addPrice(barcodedProduct);
		addWeight(barcodedItem);
		addDescription(barcodedProduct);
	}
	
       /**
	* Method to remove products from database
	*
	* @param barcodedItem Barcoded item needed to add into item database
	* @param barcodedProduct Barcoded product needed to add into product database
	*/
	public void removeProduct(BarcodedProduct barcodedProduct, BarcodedItem barcodedItem) throws notInInventoryException {
		if(barcodedProduct == null)
			throw new NullPointerException("Barcoded product given is null");

		if(barcodedItem == null)
			throw new NullPointerException("Barcoded item given is null");
		
		if(!barcodedProduct.getBarcode().equals(barcodedItem.getBarcode()))
			throw new IllegalArgumentException("Product and item given are not the same");
		
		Barcode barcode = barcodedProduct.getBarcode();
		
		if(products.containsKey(barcodedProduct.getBarcode())) {
			products.remove(barcode);
			items.remove(barcode);
			catalogPrices.remove(barcode);
			catalogWeights.remove(barcode);
			catalogDescriptions.remove(barcode);
		} else {
			throw new notInInventoryException("Selected Item/Product is not in inventory");
		}
	}
	
	/* Return price of a barcoded product */
	public BigDecimal getPrice(Barcode barcode) throws notInInventoryException {
		BigDecimal returnVal = BigDecimal.valueOf(0);
		if(catalogPrices.containsKey(barcode)) {
			returnVal = catalogPrices.get(barcode);
		} else {
			throw new notInInventoryException();
		}
		return returnVal;
	}
	
	/* Return weight of barcoded item */
	public double getWeight(Barcode barcode) throws notInInventoryException {
		double returnVal = 0.0;
		if (catalogWeights.containsKey(barcode))
			returnVal = catalogWeights.get(barcode);
		else {
			throw new notInInventoryException();
		}
		return returnVal;
	}
	
	/* Return description of barcoded product */
	public String getDescription(Barcode barcode) throws notInInventoryException {
		String returnVal = null;
		if (catalogDescriptions.containsKey(barcode))
			returnVal = catalogDescriptions.get(barcode);
		else {
			throw new notInInventoryException();
		}
		return returnVal;
	}
	
	/* Getter of products */
	public HashMap<Barcode, BarcodedProduct> getProducts(){
		return products;
	}
	
	/* Getter of items */
	public HashMap<Barcode, BarcodedItem> getItems() {
		return items;
	}
	
       /**
	*  Adds price of product to database
	*
	* @param barcodedProduct barcoded product needed to get its price
	*/
	public void addPrice(BarcodedProduct barcodedProduct) {
		catalogPrices.put(barcodedProduct.getBarcode(), barcodedProduct.getPrice());
	}
	
       /**
	*  Adds weight of item to database
	*
	* @param barcodedItem barcoded product needed to get its weight
	*/
	public void addWeight(BarcodedItem barcodedItem) {
		catalogWeights.put(barcodedItem.getBarcode(), barcodedItem.getWeight());
	}
	
       /**
	*  Adds description of product to database
	*
	* @param barcodedProduct barcoded product needed to get its decription
	*/
	public void addDescription(BarcodedProduct barcodedProduct) {
		catalogDescriptions.put(barcodedProduct.getBarcode(), barcodedProduct.getDescription());
	}
	
	
}
