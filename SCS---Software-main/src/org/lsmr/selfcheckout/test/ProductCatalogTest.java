package org.lsmr.selfcheckout.test;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.lsmr.selfcheckout.Numeral;
import org.lsmr.selfcheckout.Barcode;
import org.lsmr.selfcheckout.BarcodedItem;
import org.lsmr.selfcheckout.products.BarcodedProduct;
import org.lsmr.selfcheckout.ItemManager.ProductCatalog;
import org.lsmr.selfcheckout.ItemManager.notInInventoryException;
import java.math.BigDecimal;
import java.util.HashMap;

public class ProductCatalogTest {
	
	private ProductCatalog catalog;
	private Numeral[] numeral1;
	private Numeral[] numeral2;
	private Barcode barcodeA;
	private Barcode barcodeB;
	private BarcodedProduct appleProduct;
	private BarcodedItem appleItem;
	private BarcodedProduct orangeProduct;
	private BarcodedItem orangeItem;
	private HashMap<Barcode, BarcodedProduct> products;
	private HashMap<Barcode, BarcodedItem> items;
	
	@Before
	public void setUp() {
		catalog = new ProductCatalog();
		numeral1 = new Numeral[1];
		numeral2 = new Numeral[1];
		
		numeral1[0] = Numeral.one;
		numeral2[0] = Numeral.two;
		
		barcodeA = new Barcode(numeral1);
		barcodeB = new Barcode(numeral2);
		
		products = new HashMap<>();
		items = new HashMap<>();
		
	}
 
	@Test (expected = NullPointerException.class)
	public void testAddNullItem() {
		appleProduct = new BarcodedProduct(barcodeA, "Apple", BigDecimal.valueOf(1));
		
		catalog.addProduct(appleProduct, null);
	}
	
	@Test (expected = NullPointerException.class)
	public void testAddNullProduct() {
		appleItem = new BarcodedItem(barcodeB, 5.0);
		
		catalog.addProduct(null, appleItem);
	}
	
	@Test (expected = NullPointerException.class)
	public void testRemoveNullItem() throws notInInventoryException {
		appleProduct = new BarcodedProduct(barcodeA, "Apple", BigDecimal.valueOf(1));
		appleItem = new BarcodedItem(barcodeA, 5.0);
		
		catalog.addProduct(appleProduct, appleItem);
		catalog.removeProduct(appleProduct, null);
		
	}
	
	@Test (expected = NullPointerException.class)
	public void testRemoveNullProduct() throws notInInventoryException {
		appleProduct = new BarcodedProduct(barcodeA, "Apple", BigDecimal.valueOf(1));
		appleItem = new BarcodedItem(barcodeA, 5.0);
		
		catalog.addProduct(appleProduct, appleItem);
		catalog.removeProduct(null, appleItem);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testAddBarcodeMismatch() {
		appleProduct = new BarcodedProduct(barcodeA, "Apple", BigDecimal.valueOf(1));
		appleItem = new BarcodedItem(barcodeB, 5.0);
		
		catalog.addProduct(appleProduct, appleItem);
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testRemoveBarcodeMismatch() throws notInInventoryException {
		appleProduct = new BarcodedProduct(barcodeA, "Apple", BigDecimal.valueOf(1));
		orangeItem = new BarcodedItem(barcodeB, 5.0);
		
		catalog.removeProduct(appleProduct, orangeItem);
	}
	
	@Test 
	public void testAddNotInCatalog() {
		appleProduct = new BarcodedProduct(barcodeA, "Apple", BigDecimal.valueOf(1));
		appleItem = new BarcodedItem(barcodeA, 5.0);
		
		catalog.addProduct(appleProduct, appleItem);
		products = catalog.getProducts();
		items = catalog.getItems();
		
		Assert.assertTrue(products.containsKey(barcodeA));
		Assert.assertTrue(items.containsKey(barcodeA));
	}
	
	@Test
	public void testAddMultipleNotInCatalog() {
		appleProduct = new BarcodedProduct(barcodeA, "Apple", BigDecimal.valueOf(1));
		appleItem = new BarcodedItem(barcodeA, 5.0);
		
		orangeProduct = new BarcodedProduct(barcodeB, "Orange", BigDecimal.valueOf(2));
		orangeItem = new BarcodedItem(barcodeB, 10.0);
		
		catalog.addProduct(appleProduct, appleItem);
		catalog.addProduct(orangeProduct, orangeItem);
		products = catalog.getProducts();
		items = catalog.getItems();
		
		Assert.assertTrue(products.containsKey(barcodeA));
		Assert.assertTrue(items.containsKey(barcodeA));
		Assert.assertTrue(products.containsKey(barcodeB));
		Assert.assertTrue(items.containsKey(barcodeB));
	}
	
	@Test 
	public void testRemoveInCatalog() throws notInInventoryException {
		appleProduct = new BarcodedProduct(barcodeA, "Apple", BigDecimal.valueOf(1));
		appleItem = new BarcodedItem(barcodeA, 5.0);
		
		catalog.addProduct(appleProduct, appleItem);
		catalog.removeProduct(appleProduct, appleItem);
		products = catalog.getProducts();
		items = catalog.getItems();
		
		Assert.assertFalse(products.containsKey(barcodeA));
		Assert.assertFalse(items.containsKey(barcodeA));
	}
	
	@Test 
	public void testRemoveMultipleInCatalog() throws notInInventoryException {
		appleProduct = new BarcodedProduct(barcodeA, "Apple", BigDecimal.valueOf(1));
		appleItem = new BarcodedItem(barcodeA, 5.0);
		
		orangeProduct = new BarcodedProduct(barcodeB, "Orange", BigDecimal.valueOf(2));
		orangeItem = new BarcodedItem(barcodeB, 10.0);
		
		catalog.addProduct(appleProduct, appleItem);
		catalog.addProduct(orangeProduct, orangeItem);
		catalog.removeProduct(appleProduct, appleItem);
		catalog.removeProduct(orangeProduct, orangeItem);
		products = catalog.getProducts();
		items = catalog.getItems();
		
		Assert.assertFalse(products.containsKey(barcodeA));
		Assert.assertFalse(items.containsKey(barcodeA));
		Assert.assertFalse(products.containsKey(barcodeB));
		Assert.assertFalse(items.containsKey(barcodeB));
	}
	
	@Test (expected = notInInventoryException.class)
	public void testRemoveNotInCatalog() throws notInInventoryException {
		appleProduct = new BarcodedProduct(barcodeA, "Apple", BigDecimal.valueOf(1));
		appleItem = new BarcodedItem(barcodeA, 5.0);
		
		catalog.removeProduct(appleProduct, appleItem);
	}

	@Test
	public void testGetPriceInCata() throws notInInventoryException {
		appleProduct = new BarcodedProduct(barcodeA, "Apple", BigDecimal.valueOf(1));
		appleItem = new BarcodedItem(barcodeA, 5.0);
		
		catalog.addProduct(appleProduct, appleItem);
		
		Assert.assertEquals(BigDecimal.valueOf(1), catalog.getPrice(barcodeA));
	}
	
	@Test (expected = notInInventoryException.class)
	public void testGetPriceNotInCata() throws notInInventoryException {
		BigDecimal val = catalog.getPrice(barcodeA);
		
	}
	
	@Test
	public void testGetWeightInCata() throws notInInventoryException {
		appleProduct = new BarcodedProduct(barcodeA, "Apple", BigDecimal.valueOf(1));
		appleItem = new BarcodedItem(barcodeA, 5.0);
		
		catalog.addProduct(appleProduct, appleItem);
		
		Assert.assertEquals(5.0, catalog.getWeight(barcodeA), 0.0);
	}
	
	@Test (expected = notInInventoryException.class)
	public void testGetWeightNotInCata() throws notInInventoryException {
		double val = catalog.getWeight(barcodeA);
	}
	
	@Test
	public void testGetDescriptionInCata() throws notInInventoryException {
		String aString = "Apple";
		appleProduct = new BarcodedProduct(barcodeA, aString, BigDecimal.valueOf(1));
		appleItem = new BarcodedItem(barcodeA, 5.0);
		
		catalog.addProduct(appleProduct, appleItem);
		Assert.assertTrue(aString.equals(catalog.getDescription(barcodeA)));
		
	}
	
	@Test (expected = notInInventoryException.class)
	public void testGetDecriptionNotInCata() throws notInInventoryException {
		String val = catalog.getDescription(barcodeA);
	}
	
	@Test 
	public void testGetItems() {
		appleProduct = new BarcodedProduct(barcodeA, "Apple", BigDecimal.valueOf(1));
		appleItem = new BarcodedItem(barcodeA, 5.0);
		HashMap<Barcode, BarcodedItem> someHashMap = new HashMap<>(); 
		someHashMap.put(barcodeA , appleItem);
		catalog.addProduct(appleProduct, appleItem);
		items = catalog.getItems();
		Assert.assertEquals(someHashMap, items);
	}
	
	@Test
	public void testGetProduct() {
		appleProduct = new BarcodedProduct(barcodeA, "Apple", BigDecimal.valueOf(1));
		appleItem = new BarcodedItem(barcodeA, 5.0);
		HashMap<Barcode, BarcodedProduct> someHashMap = new HashMap<>(); 
		someHashMap.put(barcodeA , appleProduct);
		catalog.addProduct(appleProduct, appleItem);
		products = catalog.getProducts();
		Assert.assertEquals(someHashMap, products);
	}
}
