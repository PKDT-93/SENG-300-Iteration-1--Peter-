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
   
   Description: MoneyManager is responsible for handling any incoming Coins & Banknotes inserted through the CoinSlot & BanknoteSlot and records their denominations into an arraylist, which is then summed up and returned into attributes that are called upon in CheckoutManager
 */


package org.lsmr.selfcheckout.MoneyManager;
import org.lsmr.selfcheckout.*;

import org.lsmr.selfcheckout.devices.AbstractDevice;
import org.lsmr.selfcheckout.devices.BanknoteDispenser;
import org.lsmr.selfcheckout.devices.BanknoteSlot;
import org.lsmr.selfcheckout.devices.CoinDispenser;
import org.lsmr.selfcheckout.devices.CoinSlot;
import org.lsmr.selfcheckout.devices.DisabledException;
import org.lsmr.selfcheckout.devices.OverloadException;
import org.lsmr.selfcheckout.devices.SimulationException;
import org.lsmr.selfcheckout.devices.observers.AbstractDeviceObserver;
import org.lsmr.selfcheckout.devices.observers.BanknoteDispenserObserver;
import org.lsmr.selfcheckout.devices.observers.CoinDispenserObserver;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ArrayList;

public class MoneyManager {

    private BanknoteSlot banknoteSlot;
    private CoinSlot coinslot;
    private BigDecimal coinValue;
    private int banknoteValue;
    Currency CAD = Currency.getInstance(Locale.CANADA);
    public BigDecimal totalSum;
    private BigDecimal totalCoins;
    private BigDecimal totalbankNotes;
    ArrayList<Integer> banknoteArray = new ArrayList<Integer>();
    ArrayList<BigDecimal> coinArray = new ArrayList<BigDecimal>();
    Map<BigDecimal, CoinDispenser> coinDispenser = new HashMap<BigDecimal, CoinDispenser>();
    Map<Integer, BanknoteDispenser> banknoteDispenser = new HashMap<Integer, BanknoteDispenser>();
    
    public class CDO implements CoinDispenserObserver
    {
       // Coin value is assigned to a variable, which is then added to an arraylist of coins when the event is announced
        @Override 
        public void coinAdded(CoinDispenser dispenser, Coin coin)                                                           
        {
            coinValue = coin.getValue();
            coinArray.add(coinValue);
        }

        @Override
        public void enabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
            // IGNORE
        }

        @Override
        public void disabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
            // IGNORE
        }

        @Override
        public void coinsFull(CoinDispenser dispenser) {
            // IGNORE
        }

        @Override
        public void coinsEmpty(CoinDispenser dispenser) {
            // IGNORE     
        }

        @Override
        public void coinRemoved(CoinDispenser dispenser, Coin coin) {
            // IGNORE
        }

        @Override
        public void coinsLoaded(CoinDispenser dispenser, Coin... coins) {
            // IGNORE
        }

        @Override
        public void coinsUnloaded(CoinDispenser dispenser, Coin... coins) {
            // IGNORE
        }
    }

    public class BDO implements BanknoteDispenserObserver
    {
        // banknote value is assigned to a variable, which is then added to an arraylist of coins when the even is announced
    	@Override
        public void billAdded(BanknoteDispenser dispenser, Banknote banknote) {                                                     
            banknoteValue = banknote.getValue();
            banknoteArray.add(banknoteValue);
        }
    	
    	@Override
        public void enabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
            // IGNORE
        }

        @Override
        public void disabled(AbstractDevice<? extends AbstractDeviceObserver> device) {
            // IGNORE
        }

        @Override
        public void moneyFull(BanknoteDispenser dispenser) {
            // IGNORE      
        }

        @Override
        public void banknotesEmpty(BanknoteDispenser dispenser) {
            // IGNORE      
        }      

        @Override
        public void banknoteRemoved(BanknoteDispenser dispenser, Banknote banknote) {
             // IGNORE      
        }

        @Override
        public void banknotesLoaded(BanknoteDispenser dispenser, Banknote... banknotes) {
             // IGNORE    
        }

        @Override
        public void banknotesUnloaded(BanknoteDispenser dispenser, Banknote... banknotes) {
             // IGNORE
        }
    }

    public MoneyManager(BanknoteSlot bns, CoinSlot c, Map<BigDecimal, CoinDispenser>coinDispensers, Map<Integer, BanknoteDispenser>banknoteDispensers)
    		throws IllegalArgumentException, NullPointerException {
                 // This constructor takes in 4 parameters from a variety of hardware classes and assigns it to local attributes in order to attach observers classes to them, exception handling is added below for null values that are passed through
    	banknoteSlot = bns;
    	coinslot = c;
    	coinDispenser = coinDispensers;
    	banknoteDispenser = banknoteDispensers;
    			
        try {
        	for (BanknoteDispenser bnDispenser : banknoteDispensers.values()) {
                bnDispenser.attach(new BDO());
            }
        }
        catch (IllegalArgumentException e) {
        	System.out.println("banknoteDispensers is null.");
        }
         
        try { 
        	for (CoinDispenser dispenser : coinDispensers.values()) {
        		dispenser.attach(new CDO());
        	}
        }
        catch (IllegalArgumentException e) {
        	System.out.println("coinDispensers is null.");
        }
        
        if (banknoteSlot == null) {
             throw new IllegalArgumentException("BanknoteSlot is null.");
        }
         
        if(coinslot == null) {
             throw new IllegalArgumentException("CoinSlot is null");
        }     
    }

    public void addCoin(Coin coin) throws DisabledException, SimulationException { 
        // This method takes in a parameter of type coin and passes it into coinSlot's accept method to notify it's observer and check if sink is full via deliver().
        this.coinslot.accept(coin);
    }

    public void setTotalCoins(BigDecimal totalCoins)
        //setter method
    {
        this.totalCoins = totalCoins;
    }

    public BigDecimal getTotalCoins()
    {   
        //getter method
        return totalCoins;
    }

    public void addBanknote(Banknote banknote) throws DisabledException, OverloadException {
        // This method takes in a parameter of type banknote and passes it into banknoteslot's accept method to notify it's observer and check if sink is full deliver().
        this.banknoteSlot.accept(banknote);
    }

    public void setTotalBanknotes(BigDecimal totalBanknotes)
    {
        // setter method
        this.totalbankNotes= totalBanknotes;
    }

    public BigDecimal getTotalBanknotes()
    {
        // getter method
        return totalbankNotes;
    }

    public BigDecimal sum_total(BigDecimal totalCoins, BigDecimal totalbankNotes) throws NegativeFundsException
    {
        // This method sums the values of coins & banknotes in BigDecimal format and returns the total into totalSum
        totalSum = totalCoins.add(totalbankNotes);
        BigDecimal bd_zero = new BigDecimal(0);
        try 
        {
            if(totalSum.compareTo(bd_zero) < 0)
                throw new NegativeFundsException("Funds below Zero");
        }
        finally{}
        return totalSum;
    }
    
    public BigDecimal sum_coins(ArrayList<BigDecimal> coinArray) throws NegativeFundsException
    {
        // this method sums up all coin values in the arraylist and returns it into totalCoins
        totalCoins = coinArray.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
        try 
        {
            if(totalCoins.compareTo(BigDecimal.ZERO) < 0)
                throw new NegativeFundsException("Funds below Zero");
        }
        finally{}

        return totalCoins;
    }

    public BigDecimal sum_banknote(ArrayList<Integer> banknoteArray) throws NegativeFundsException                   
    {
        // this method sums up all banknote values in the arraylist by converting int into double and then into big decimal then returns the converted value into totalbankNotes
        double[] doublebanknoteArray = banknoteArray.stream().mapToDouble(i -> (double) i).toArray();
        double d_total = 0;
        for(double elements: doublebanknoteArray)
        {
            d_total += elements;
        }
        totalbankNotes = BigDecimal.valueOf(d_total);
        try 
        {
            if(totalbankNotes.compareTo(BigDecimal.ZERO) < 0)
                throw new NegativeFundsException("Funds below Zero");
        }
        finally{}
        return totalbankNotes;
    }
    
}
