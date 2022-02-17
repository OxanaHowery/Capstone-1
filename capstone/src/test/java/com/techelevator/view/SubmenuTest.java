package com.techelevator.view;

import com.techelevator.VendingMachine;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SubmenuTest {

    Submenu testSubmenu;
    int money = 20;
    String result = "";
    SimpleDateFormat formatter = VendingMachineLog.getFormatter();
    Timestamp timestamp = VendingMachineLog.getTimestamp();
    String testLogFilePath = "src/test/resources/logTest.txt";
    List<String[]> testArray = new ArrayList<>();
    String[] chipArray = new String[]{"A1", "Lays", "1.50", "Chip", "5"};
    String[] drinkArray = new String[]{"B1", "Coke", "1.25", "Drink", "5"};
    String[] gumArray = new String[]{"C1", "Big League Chew", "2.00", "Gum", "5"};
    String[] candyArray = new String[]{"D1", "Snickers", "0.85", "Candy", "5"};
    private ByteArrayOutputStream output;
    BigDecimal _machineBalance;



    @Before
    public void setUp() {

        Submenu testSubmenu = new Submenu(System.in, System.out);
        VendingMachine.set_machineBalance(testSubmenu.feedMoney(money));
        output = new ByteArrayOutputStream();
        testArray.add(chipArray);
        testArray.add(drinkArray);
        testArray.add(gumArray);
        testArray.add(candyArray);

        VendingMachineLog.setItemsArray(testArray);
        VendingMachine.setItemsArray(testArray);

    }

    private BigDecimal selectProductHelper(){
        Submenu testSubmenuInput = getMenuForTestingWithUserInput("A1" + System.lineSeparator());
        VendingMachine.set_machineBalance(testSubmenuInput.selectProduct(VendingMachine.get_machineBalance()));
        return VendingMachine.get_machineBalance();
    }

    private void getChangeHelper(){
        Submenu testSubmenu = new Submenu(System.in, System.out);
        VendingMachine.set_machineBalance(testSubmenu.getChange(VendingMachine.get_machineBalance()));

    }

@After
public void tearDown(){
    try {
        PrintWriter writer = new PrintWriter(new FileOutputStream(testLogFilePath, false));
        writer.print("");
        writer.close();

    } catch (FileNotFoundException e) {
        e.printStackTrace();
    }

        testArray.clear();

}

    @Test
    public void feedMoney() {

        BigDecimal _machineBalance = VendingMachine.get_machineBalance();
        String message = "FEED MONEY" + " $" + money + " $" + _machineBalance;
        VendingMachineLog.log(message, testLogFilePath);

        String expectedResult = formatter.format(timestamp) + " FEED MONEY $20 $20";

        result = checkResult();

        Assert.assertEquals(expectedResult, result);

    }

    @Test
    public void selectProduct() {

        selectProductHelper();
        BigDecimal _machineBalance = VendingMachine.get_machineBalance();
        BigDecimal _machineBalancePrePurchase = BigDecimal.valueOf(money);
        String message = "Lays A1" + " $" + _machineBalancePrePurchase + " $" + _machineBalance;
        VendingMachineLog.log(message, testLogFilePath);

        String expectedResult = formatter.format(timestamp) + " Lays A1 $20 $18.50";

       result = checkResult();

        Assert.assertEquals(expectedResult, result);

    }

    @Test
    public void getChange() {

        BigDecimal _MBPreChange = selectProductHelper();
        getChangeHelper();
        BigDecimal _machineBalance = VendingMachine.get_machineBalance();
        String message = "GIVE CHANGE" + " $" + _MBPreChange + " $" + _machineBalance;
        VendingMachineLog.log(message, testLogFilePath);

        String expectedResult = formatter.format(timestamp) + " GIVE CHANGE $18.50 $0.00";
        result = checkResult();
        Assert.assertEquals(expectedResult, result);

    }

    private Submenu getMenuForTestingWithUserInput(String userInput) {
        ByteArrayInputStream input = new ByteArrayInputStream(String.valueOf(userInput).getBytes());
        return new Submenu(input, output);
    }

    private Submenu getMenuForTesting() {

        return getMenuForTestingWithUserInput("1" + System.lineSeparator());
    }

    private String checkResult (){
        try (Scanner testScanner = new Scanner(new File(testLogFilePath))) {
            while (testScanner.hasNextLine()) {
                result = testScanner.nextLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}