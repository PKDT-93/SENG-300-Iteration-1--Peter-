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
   Testing class for MoneyManager.
 */

package org.lsmr.selfcheckout.test;

import static org.junit.Assert.fail;

import java.math.BigDecimal;

import org.junit.Test;
import org.lsmr.selfcheckout.Banknote;
import org.lsmr.selfcheckout.Coin;
import org.lsmr.selfcheckout.MoneyManager.MoneyManager;
import org.lsmr.selfcheckout.MoneyManager.NegativeFundsException;
import org.lsmr.selfcheckout.devices.BanknoteDispenser;
import org.lsmr.selfcheckout.devices.BanknoteSlot;
import org.lsmr.selfcheckout.devices.CoinDispenser;
import org.lsmr.selfcheckout.devices.CoinSlot;
import org.lsmr.selfcheckout.devices.SelfCheckoutStation;

import java.util.Currency;
import java.util.HashMap;
import java.util.ArrayList;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.Locale;
import java.util.Map;

public class MoneyManagerTest {
	
	// Instantiated variables for testing purpose.

    Currency CAD = Currency.getInstance(Locale.CANADA);

    Coin cent = new Coin(CAD, BigDecimal.valueOf(0.01));
    Coin quarter = new Coin(CAD, BigDecimal.valueOf(0.25));
    Coin dime = new Coin(CAD, BigDecimal.valueOf(0.10));
    Coin nickel = new Coin(CAD, BigDecimal.valueOf(0.05));
    Coin loonie = new Coin(CAD, BigDecimal.valueOf(1.00));
    Coin toonie = new Coin(CAD, BigDecimal.valueOf(2.00));

    BigDecimal[] coinArray = new BigDecimal[] { cent.getValue(), quarter.getValue(), dime.getValue(),
            nickel.getValue(), loonie.getValue() };

    Banknote fiveBanknote = new Banknote(CAD, 5);
    Banknote tenBanknote = new Banknote(CAD, 10);
    Banknote twentyBanknote = new Banknote(CAD, 20);
    Banknote fiftyBanknote = new Banknote(CAD, 50);
    Banknote hundredBanknote = new Banknote(CAD, 100);

    int[] banknoteArray = new int[] { fiveBanknote.getValue(), tenBanknote.getValue(), twentyBanknote.getValue(),
            fiftyBanknote.getValue(), hundredBanknote.getValue() };

    // Test for inserting a new coin into coin slot.
    @Test
    public void coinInsertedTest(){
        try {
            SelfCheckoutStation selfCheckout = new SelfCheckoutStation(CAD, banknoteArray, coinArray, 1000, 1);
            MoneyManager moneyManager = new MoneyManager(selfCheckout.banknoteInput, selfCheckout.coinSlot, 
            		selfCheckout.coinDispensers, selfCheckout.banknoteDispensers);
            Coin coin = new Coin(CAD, BigDecimal.valueOf(0.05));
            moneyManager.addCoin(coin);
        } catch (Exception e) {
            fail("Exception thrown when creating new coin.");
        }
    } 

    // Test for inserting a new banknote into banknote slot.
    @Test
    public void banknoteInsertedTest(){
        try {
            SelfCheckoutStation selfCheckout = new SelfCheckoutStation(CAD, banknoteArray, coinArray, 1000, 1);
            MoneyManager moneyManager = new MoneyManager(selfCheckout.banknoteInput, selfCheckout.coinSlot,
                    selfCheckout.coinDispensers, selfCheckout.banknoteDispensers);
            Banknote banknote = new Banknote(CAD, 5);
            moneyManager.addBanknote(banknote);
        } catch (Exception e) {
            fail("Exception thrown when creating new banknote.");
        }
    }
    
    // Test for pass null parameters then try to create a new instance of MoneyManager.
    @Test
    public void moneyManagerTest() {
    	SelfCheckoutStation selfCheckout = new SelfCheckoutStation(CAD, banknoteArray, coinArray, 1000, 1);
        try {
        	BanknoteSlot bns = null;
            MoneyManager moneyManager = new MoneyManager(bns, selfCheckout.coinSlot,
                     selfCheckout.coinDispensers, selfCheckout.banknoteDispensers);
             
            fail("Exception not thrown when BanknoteSlot is null.");
        } catch (IllegalArgumentException ex) {
         
        }

         try {
             CoinSlot c = null;
             MoneyManager moneyManager = new MoneyManager(selfCheckout.banknoteInput, c,
                     selfCheckout.coinDispensers, selfCheckout.banknoteDispensers);
             fail("Exception not thrown when CoinSlot is null.");
         } catch (IllegalArgumentException ex) {

         }
         
         try {
        	 Map<BigDecimal, CoinDispenser> coinDispenser = new HashMap<BigDecimal, CoinDispenser>();      
        	 coinDispenser = null;
             MoneyManager moneyManager = new MoneyManager(selfCheckout.banknoteInput, selfCheckout.coinSlot,
                     coinDispenser, selfCheckout.banknoteDispensers);
             fail("Exception not thrown when coinDispenser is null.");
         } catch (NullPointerException ex) {

         } 
         
         try {
        	 Map<Integer, BanknoteDispenser> banknoteDispenser = new HashMap<Integer, BanknoteDispenser>();      
        	 banknoteDispenser = null;
             MoneyManager moneyManager = new MoneyManager(selfCheckout.banknoteInput, selfCheckout.coinSlot,
            		 selfCheckout.coinDispensers, banknoteDispenser);
             fail("Exception not thrown when banknoteDispeners is null.");
         } catch (NullPointerException ex) {

         }
     }

    // Test for calculating the total value of coins.
    @Test
    public void totalCoinsTest() {
        SelfCheckoutStation selfCheckout = new SelfCheckoutStation(CAD, banknoteArray, coinArray, 1000, 1);
        MoneyManager moneyManager = new MoneyManager(selfCheckout.banknoteInput, selfCheckout.coinSlot,
        selfCheckout.coinDispensers, selfCheckout.banknoteDispensers);

        BigDecimal expected = BigDecimal.valueOf(6.0);
        moneyManager.setTotalCoins(expected);
        BigDecimal actual = moneyManager.getTotalCoins();

        assertEquals("Total coin incorrectly updated.", expected, actual);
    }

    // Test for calculating the total value of coins.
    @Test
    public void totalBanknotesTest() {
        SelfCheckoutStation selfCheckout = new SelfCheckoutStation(CAD, banknoteArray, coinArray, 1000, 1);
        MoneyManager moneyManager = new MoneyManager(selfCheckout.banknoteInput, selfCheckout.coinSlot,
                    selfCheckout.coinDispensers, selfCheckout.banknoteDispensers);

        BigDecimal expected = BigDecimal.valueOf(6.0);
        moneyManager.setTotalBanknotes(expected);
        BigDecimal actual = moneyManager.getTotalBanknotes();

        assertEquals("Total coin incorrectly updated.", expected, actual);
    }

    // Test for calculating a negative total sum.
    @Test
    public void sumTotalNegativeTest() {
        SelfCheckoutStation selfCheckout = new SelfCheckoutStation(CAD, banknoteArray, coinArray, 1000, 1);
       MoneyManager moneyManager = new MoneyManager(selfCheckout.banknoteInput, selfCheckout.coinSlot,
                    selfCheckout.coinDispensers, selfCheckout.banknoteDispensers);

        BigDecimal total_coins = BigDecimal.valueOf(-1.0);
        BigDecimal total_banknotes = BigDecimal.valueOf(-5.0);

        try {
            moneyManager.sum_total(total_coins, total_banknotes);
            fail("Exception not thrown when sum is negative.");
        } catch (NegativeFundsException ignored) {

        }
    }
    
    // Test for calculating the sum of coins and banknotes.
    @Test
    public void sumTotalTest() {
        SelfCheckoutStation selfCheckout = new SelfCheckoutStation(CAD, banknoteArray, coinArray, 1000, 1);
        MoneyManager moneyManager = new MoneyManager(selfCheckout.banknoteInput, selfCheckout.coinSlot,
                    selfCheckout.coinDispensers, selfCheckout.banknoteDispensers);

        BigDecimal total_coins = BigDecimal.valueOf(1.0);
        BigDecimal total_banknotes = BigDecimal.valueOf(5.0);

        try {
            moneyManager.sum_total(total_coins, total_banknotes);
        } catch (NegativeFundsException e) {
            fail("Exception thrown when calculating total sum.");
        }
    }
    
    // Test for calculating the sum of coins.
    @Test
    public void sumCoinsTest() {
        SelfCheckoutStation selfCheckout = new SelfCheckoutStation(CAD, banknoteArray, coinArray, 1000, 1);
        MoneyManager moneyManager = new MoneyManager(selfCheckout.banknoteInput, selfCheckout.coinSlot,
                    selfCheckout.coinDispensers, selfCheckout.banknoteDispensers);

        ArrayList<BigDecimal> coin_array = new ArrayList<BigDecimal>();

        try {
            BigDecimal one = BigDecimal.valueOf(1.0);
            BigDecimal two = BigDecimal.valueOf(2.0);
            BigDecimal three = BigDecimal.valueOf(3.0);

            coin_array.add(one);
            coin_array.add(two);
            coin_array.add(three);

            BigDecimal actual = moneyManager.sum_coins(coin_array);
            BigDecimal expected = BigDecimal.valueOf(6.0);

            assertEquals("Current value incorrectly updated.", expected, actual);
        }

        catch (NegativeFundsException e) {
            fail("Exception should not be thrown.");
        }
    }
    
    // Test for calculating negative sum of coins.
    @Test
    public void sumNegativeCoinTest() {
        SelfCheckoutStation selfCheckout = new SelfCheckoutStation(CAD, banknoteArray, coinArray, 1000, 1);
        MoneyManager moneyManager = new MoneyManager(selfCheckout.banknoteInput, selfCheckout.coinSlot,
                    selfCheckout.coinDispensers, selfCheckout.banknoteDispensers);

        ArrayList<BigDecimal> coin_array = new ArrayList<BigDecimal>();

        try {
            BigDecimal one = BigDecimal.valueOf(-1.0);
            BigDecimal two = BigDecimal.valueOf(-2.0);
            BigDecimal three = BigDecimal.valueOf(-3.0);

            coin_array.add(one);
            coin_array.add(two);
            coin_array.add(three);

            moneyManager.sum_coins(coin_array);
            fail("Exception not thrown when Coin sum is negative.");
        } catch (NegativeFundsException ignored) {

        }
    }

    // Test for calculating the sum of banknotes.
    @Test
    public void sumBanknoteTest() {
        SelfCheckoutStation selfCheckout = new SelfCheckoutStation(CAD, banknoteArray, coinArray, 1000, 1);
        MoneyManager moneyManager = new MoneyManager(selfCheckout.banknoteInput, selfCheckout.coinSlot,
                    selfCheckout.coinDispensers, selfCheckout.banknoteDispensers);

        ArrayList<Integer> banknote_array = new ArrayList<Integer>();

        try {
            banknote_array.add(1);
            banknote_array.add(2);
            banknote_array.add(3);

            BigDecimal actual = moneyManager.sum_banknote(banknote_array);
            BigDecimal expected = BigDecimal.valueOf(6.0);

            assertEquals("Current value incorrectly updated.", expected, actual);
        }

        catch (NegativeFundsException e) {
            fail("Exception should not be thrown.");
        }
    }
    
    // Test for calculating negative sum of banknotes.
    @Test
    public void sumNegativeBanknoteTest() {
        SelfCheckoutStation selfCheckout = new SelfCheckoutStation(CAD, banknoteArray, coinArray, 1000, 1);
        MoneyManager moneyManager = new MoneyManager(selfCheckout.banknoteInput, selfCheckout.coinSlot,
                    selfCheckout.coinDispensers, selfCheckout.banknoteDispensers);

        ArrayList<Integer> banknote_array = new ArrayList<Integer>();

        try {
            banknote_array.add(-1);
            banknote_array.add(-2);
            banknote_array.add(-3);

            moneyManager.sum_banknote(banknote_array);
            fail("Exception not thrown when Banknote sum is negative.");
        } catch (NegativeFundsException ignored) {

        }
    }
}
