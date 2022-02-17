package com.techelevator.view;

import com.techelevator.VendingMachine;

import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Scanner;

import static java.math.BigDecimal.*;
import static java.math.BigDecimal.ONE;

public class Submenu extends Menu {
    //properties

    //BigDecimal
    final BigDecimal _NICKEL = new BigDecimal(".05");
    final BigDecimal _DIME = new BigDecimal(".10");
    final BigDecimal _QUARTER = new BigDecimal(".25");

    static String itemPurchased = "";

    //getters
    public Scanner getIn() {
        return super.getIn();
    }


    public String getItemPurchased() {
        return itemPurchased;
    }



    //constructor
    public Submenu(InputStream input, OutputStream output) {
        super(input, output);
    }


    //methods
    public int askForMoney() {
        System.out.println("What bill are you inserting? (Only whole dollar amounts)"); //valid, whole dollar amounts—for example, $1, $2, $5, $10, $20.
        String testMoney = super.getIn().nextLine().trim();
        int moneyIn;
        if (testMoney.equals("1") || testMoney.equals("2") || testMoney.equals("5") || testMoney.equals("10") || testMoney.equals("20") || testMoney.equals("50") || testMoney.equals("100")) {
            moneyIn = Integer.parseInt(testMoney);
        } else {
            System.out.println("Please insert right bill");
            moneyIn = 0;
        }
        return moneyIn;
    }


    public BigDecimal feedMoney(int moneyIn) {
        BigDecimal _machineBalance = ZERO;
        BigDecimal _money = ZERO;
        if (_money.compareTo(ZERO) == 0) {
            _money = _money.add(BigDecimal.valueOf(moneyIn));
        }
        _machineBalance = _machineBalance.add(_money);

        String message = "FEED MONEY" + " $" + _money + " $" + _machineBalance;

        VendingMachineLog.log(message, VendingMachineLog.getLogFilePath());
        return _machineBalance;
    }

    // item name, item slot, machine balance, machine after price
    public BigDecimal selectProduct(BigDecimal _machineBalance) {
        System.out.println("What product would you like? (please enter your alphanumeric choice)");
        String selection = super.getIn().nextLine();
        boolean isExists = false;

        List<String[]> itemsArray = VendingMachine.getItemsArray(); //16 items, each as an array
        for (String[] itemLine : itemsArray) { //each item as the array
            BigDecimal price = new BigDecimal(itemLine[2]);
            if (selection.equals(itemLine[0])) {
                isExists = true;
                if (_machineBalance.compareTo(price) == -1) {
                    System.out.println("Please feed money and select again");
                    break;
                }
                if (Integer.parseInt(itemLine[4]) > 0) {
                    itemPurchased = itemLine[1];
                    int n = Integer.parseInt(itemLine[4]);
                    n--;
                    itemLine[4] = Integer.toString(n);
                    VendingMachine.setItemsArray(itemsArray);
                    System.out.println(itemLine[1] + ": $" + itemLine[2]);
                    makeSound(itemLine[3]);
                    BigDecimal _machineBalancePrePurchase = _machineBalance;
                    _machineBalance = _machineBalancePrePurchase.subtract(price);
                    String message = itemLine[1] + " " + itemLine[0] + " $" + _machineBalancePrePurchase + " $" + _machineBalance;
                    VendingMachineLog.log(message, VendingMachineLog.getLogFilePath());
                } else System.out.println("Item is out of stock.");
                break;
            }
        }
        if (!isExists) {
            System.out.println("This code is not valid. Please enter another.");
        }
        return _machineBalance;
    }

    public void makeSound(String type) {
        switch (type) {
            case "Chip":
                System.out.println("Crunch Crunch, Yum!");
                break;
            case "Candy":
                System.out.println("Munch Munch, Yum!");
                break;
            case "Gum":
                System.out.println("Chew Chew, Yum!");
                break;
            case "Drink":
                System.out.println("Glug Glug, Yum!");
                break;
        }
    }

    public BigDecimal getChange(BigDecimal _machineBalance) {
        BigDecimal _numberOfQuarters;
        BigDecimal _numberOfDimes;
        BigDecimal _numberOfNickels;
        BigDecimal _machineBalancePreChange = _machineBalance;

        _numberOfQuarters = _machineBalance.divide(_QUARTER, 0, RoundingMode.DOWN);
        _machineBalance = _machineBalance.subtract(_numberOfQuarters.multiply(_QUARTER));
        _numberOfDimes = _machineBalance.divide(_DIME, 0, RoundingMode.DOWN);
        _machineBalance = _machineBalance.subtract(_numberOfDimes.multiply(_DIME));
        _numberOfNickels = _machineBalance.divide(_NICKEL, 0, RoundingMode.DOWN);
        _machineBalance = _machineBalance.subtract(_numberOfNickels.multiply(_NICKEL));

        String change = "";
        if (_numberOfQuarters.compareTo(ONE) == 0) {
            change += _numberOfQuarters + " Quarter, ";
        } else {
            change += _numberOfQuarters + " Quarters, ";
        }
        if (_numberOfDimes.compareTo(ONE) == 0) {
            change += _numberOfDimes + " Dime, ";
        } else {
            change += _numberOfDimes + " Dimes, ";
        }
        if (_numberOfNickels.compareTo(ONE) == 0) {
            change += _numberOfNickels + " Nickel, ";
        } else {
            change += _numberOfNickels + " Nickels.";
        }
        System.out.println("You got back " + change);
        String message = "GIVE CHANGE" + " $" + _machineBalancePreChange + " $" + _machineBalance;
        VendingMachineLog.log(message, VendingMachineLog.getLogFilePath());
        return _machineBalance;
    }
}
