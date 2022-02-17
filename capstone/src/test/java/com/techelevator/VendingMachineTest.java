package com.techelevator;

import com.techelevator.view.Menu;
import com.techelevator.view.Submenu;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class VendingMachineTest {


    Menu testMenu = new Menu(System.in, System.out);
    Submenu testSubmenu = new Submenu(System.in, System.out);
    VendingMachine vendTest = new VendingMachine(testMenu, testSubmenu);
    String testFilePath = "src/test/resources/VendTestFile.txt";
    File vendTestFile = new File(testFilePath);


    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();




    @Test
    public void stockVendingMachine() {

        List<String[]> testArray = vendTest.stockVendingMachine(vendTestFile);

        List<String[]> expectedArray = new ArrayList<>();
        String[] chipArray = new String[]{"A1", "Lays", "1.50", "Chip", "5"};
        String[] drinkArray = new String[]{"B1", "Coke", "1.25", "Drink", "5"};
        String[] gumArray = new String[]{"C1", "Big League Chew", "2.00", "Gum", "5"};
        String[] candyArray = new String[]{"D1", "Snickers", "0.85", "Candy", "5"};
        expectedArray.add(chipArray);
        expectedArray.add(drinkArray);
        expectedArray.add(gumArray);
        expectedArray.add(candyArray);


        Assert.assertArrayEquals(expectedArray.get(0), testArray.get(0));
        Assert.assertArrayEquals(expectedArray.get(1), testArray.get(1));
        Assert.assertArrayEquals(expectedArray.get(2), testArray.get(2));
        Assert.assertArrayEquals(expectedArray.get(3), testArray.get(3));
    }

    @Test
    public void printDisplay() {

        System.setOut(new PrintStream(outputStreamCaptor));

        List<String[]> testArray = new ArrayList<>();
        String[] chipArray = new String[]{"A1", "Lays", "1.50", "Chip", "5"};
        String[] drinkArray = new String[]{"B1", "Coke", "1.25", "Drink", "5"};
        String[] gumArray = new String[]{"C1", "Big League Chew", "2.00", "Gum", "5"};
        String[] candyArray = new String[]{"D1", "Snickers", "0.85", "Candy", "5"};
        testArray.add(chipArray);
        testArray.add(drinkArray);
        testArray.add(gumArray);
        testArray.add(candyArray);
        String expectedPrintOut = "A1) Lays: $1.50 (5 items in stock)\r\nB1) Coke: $1.25 (5 items in stock)\r\nC1) Big League Chew: $2.00 (5 items in stock)\r\nD1) Snickers: $0.85 (5 items in stock)";

        vendTest.printDisplay(testArray);

        Assert.assertEquals(expectedPrintOut, outputStreamCaptor.toString().trim());

        System.setOut(System.out);
    }
}