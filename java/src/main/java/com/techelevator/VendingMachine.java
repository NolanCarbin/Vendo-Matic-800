package com.techelevator;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.*;

public class VendingMachine {
    private BigDecimal customerBalance = new BigDecimal("0");
    private Map<VendingMachineItem, Integer> inventory = new HashMap<VendingMachineItem, Integer>();

    //These properties relate to the sales report
    private Map<String, Integer> numberOfItemsSold = new HashMap<String, Integer>();
    private BigDecimal totalAmountSold = new BigDecimal("0");


    //Customer Balance Methods
    public void makeChange(){
        //Uses Greedy's Algorithm approach
        int balanceAsInt = (int)(this.customerBalance.doubleValue() * 100);
        int[] coins = {1, 5, 10, 25, 100, 500};
        List<Integer> amountOwed = new ArrayList<Integer>();
        while(balanceAsInt > 0) {
            int largestCoin = findLargestCoin(coins, balanceAsInt);
            amountOwed.add(largestCoin);
            balanceAsInt -= largestCoin;
        }
        //Counters to keep track of the number of each coin needed to give back to the customer.
        int pennyCounter = 0;
        int nickelCounter = 0;
        int dimeCounter = 0;
        int quarterCounter = 0;
        int dollarCounter = 0;
        int fiveDollarCounter = 0;
        for(Integer coin : amountOwed) {
            if (coin == 1) {
                pennyCounter++;
            } else if (coin == 5) {
                nickelCounter++;
            } else if (coin == 10) {
                dimeCounter++;
            } else if (coin == 25) {
                quarterCounter++;
            } else if (coin == 100) {
                dollarCounter++;
            } else {
                fiveDollarCounter++;
            }
        }
        System.out.println("\n**DISPENSING CHANGE**\n");
        if(fiveDollarCounter > 0){
            System.out.println("Five Dollar Bills : " + fiveDollarCounter);
        } if (dollarCounter > 0) {
            System.out.println("One Dollar Bills : " + dollarCounter);
        } if (quarterCounter > 0) {
            System.out.println("Quarters : " + quarterCounter);
        } if (dimeCounter > 0) {
            System.out.println("Dimes : " + dimeCounter);
        } if (nickelCounter > 0) {
            System.out.println("Nickels : " + nickelCounter);
        } if (pennyCounter > 0) {
            System.out.println("Pennies : " + pennyCounter);
        }
    }

    // findLargestCoin() is used inside the makeChange() method to go through the valid coin denominations and return the
    //largest coin for the current amount
    private static int findLargestCoin(int[] coins, int balanceAsInt){
        for(int i=coins.length-1; i>=0; i--) {
            int currentCoin = coins[i];
            if(balanceAsInt >= currentCoin){
                return currentCoin;
            }
        }
            return 0;
    }

    public void addToCustomerBalance(String dollars) {
        BigDecimal dollarsAsBigDecimal = new BigDecimal(dollars);
        this.customerBalance = this.customerBalance.add(dollarsAsBigDecimal);
    }


    //Inventory Methods:
    public boolean removeItemFromInventory(String boughtItem) {
        for (Map.Entry<VendingMachineItem, Integer> entry : this.inventory.entrySet()) {
            String currentItemName = entry.getKey().getItemName();
            if (currentItemName.equals(boughtItem)) {
                if (entry.getValue() == 0) {
                    return false;
                }
                this.inventory.put(entry.getKey(),entry.getValue()-1);
            }
        } return true;
    }
    //Used to reset the stock of each item in the inventory
    public void addFiveToInventory(VendingMachineItem item){
        inventory.put(item,5);
    }

    //Reads from the vendingmachine.csv file to stock the inventory
    //Makes new instances of each vending machine item with the name, price, location ID, category, and message
    public void loadInventory() {
        File vendingMachineInventoryFile = new File("vendingmachine.csv");

        try (Scanner fileInput = new Scanner(vendingMachineInventoryFile)) {
            while (fileInput.hasNextLine()) {
                String line = fileInput.nextLine();
                String[] entry = line.split("\\|");
                String location = entry[0];
                String itemName = entry[1];
                BigDecimal price = new BigDecimal(entry[2]);
                String category = entry[3];

                String itemMessage;
                if (category.equals("Chip")){
                    itemMessage = "Crunch Crunch, Yum!";
                } else if (category.equals("Candy")) {
                    itemMessage = "Munch Munch, Yum!";
                } else if (category.equals("Drink")) {
                    itemMessage = "Glug Glug, Yum!";
                } else {
                    itemMessage = "Chew Chew, Yum!";
                }
                VendingMachineItem vendingMachineItem = new VendingMachineItem(location,itemName,price,category,itemMessage);
                this.addFiveToInventory(vendingMachineItem);
            }
        } catch (FileNotFoundException ex) {
            System.out.println("File not found : " + ex);
        }
    }

    //Prints to the console the current vending machine inventory
    public void showInventory() {
        System.out.println("\nVENDO-MATIC SNACKS:");
        for (Map.Entry<VendingMachineItem, Integer> entry : this.getInventory().entrySet()) {
            String location = entry.getKey().getLocation();
            String itemName = entry.getKey().getItemName();
            BigDecimal price = entry.getKey().getPrice();
            Integer quantity = entry.getValue();

            if (quantity == 0) {
                System.out.println(location + " " + itemName + " $" + price + " SOLD OUT");
            } else {
                System.out.println(location + " " + itemName + " $" + price + " Available: " + quantity);
            }
        }
    }

    //Getters & Setters
    public BigDecimal getCustomerBalance() {
        return customerBalance;
    }

    public void setCustomerBalance(BigDecimal customerBalance) {
        this.customerBalance = customerBalance;
    }

    public Map<VendingMachineItem, Integer> getInventory() {
        return inventory;
    }

    public Map<String, Integer> getNumberOfItemsSold() {
        return numberOfItemsSold;
    }

    public void setNumberOfItemsSold(Map<String, Integer> numberOfItemsSold) {
        this.numberOfItemsSold = numberOfItemsSold;
    }

    public BigDecimal getTotalAmountSold() {
        return totalAmountSold;
    }

    public void setTotalAmountSold(BigDecimal totalAmountSold) {
        this.totalAmountSold = totalAmountSold;
    }
}
