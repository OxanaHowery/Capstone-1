package com.techelevator.view;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class VendingMachineLogTest {

    VendingMachineLog testVML = new VendingMachineLog();

    private final String LOG_FILE_PATH = "src/test/resources/log.txt";
    private File logTestFile = new File(LOG_FILE_PATH);

    private final String SALES_FILE_PATH = "src/test/resources/salesReport.txt";
    private File salesTestFile = new File(SALES_FILE_PATH);
    private String testResult = "";
    List<String[]> testArray = new ArrayList<>();
    String[] chipArray = new String[]{"A1", "Lays", "1.50", "Chip", "5"};
    String[] drinkArray = new String[]{"B1", "Coke", "1.25", "Drink", "5"};
    String[] gumArray = new String[]{"C1", "Big League Chew", "2.00", "Gum", "5"};
    String[] candyArray = new String[]{"D1", "Snickers", "0.85", "Candy", "5"};
    List<String[]> legacyTestList;


    @Before
    public void setUp() {
        testArray.add(chipArray);
        testArray.add(drinkArray);
        testArray.add(gumArray);
        testArray.add(candyArray);
        VendingMachineLog.setItemsArray(testArray);
        legacyTestListHelper();
    }

    @After
    public void tearDown() {
        testArray.clear();
    }

    public List<String[]> legacyTestListHelper(){
        legacyTestList =  VendingMachineLog.getLegacyList();
        return legacyTestList;
    }

    @Test
    public void logMessagesMatch_returnsTrue() {
        SimpleDateFormat formatter = VendingMachineLog.getFormatter();
        Timestamp timestamp = VendingMachineLog.getTimestamp();

        String message = "test successful!";
        String expectedResult = formatter.format(timestamp) + " " + message;
        VendingMachineLog.log(message, LOG_FILE_PATH);
        String testResult = "";

        try (Scanner scanner = new Scanner(logTestFile)) {
            while (scanner.hasNextLine()) {
                testResult = scanner.nextLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Assert.assertEquals(testResult, expectedResult);

    }

    @Test
    public void readSalesReportFull() {

        List<String[]> expectedArray = new ArrayList<>();
        String[] chipArray = new String[]{"Lays", "0"};
        String[] drinkArray = new String[]{"Coke", "0"};
        String[] gumArray = new String[]{"Big League Chew", "0"};
        String[] candyArray = new String[]{"Snickers", "1"};
        expectedArray.add(chipArray);
        expectedArray.add(drinkArray);
        expectedArray.add(gumArray);
        expectedArray.add(candyArray);

        Assert.assertArrayEquals(expectedArray.get(0), legacyTestList.get(0));
        Assert.assertArrayEquals(expectedArray.get(1), legacyTestList.get(1));
        Assert.assertArrayEquals(expectedArray.get(2), legacyTestList.get(2));
        Assert.assertArrayEquals(expectedArray.get(3), legacyTestList.get(3));

    }

    @Test
    public void writeSalesReportFull() {

        VendingMachineLog.writeSalesReport("Snickers", salesTestFile);
        String expectedResult = "Lays|0\r\nCoke|0\r\nBig League Chew|0\r\nSnickers|1\r\n\r\nTotal Sales: $0.85\r\n";
        StringBuilder builder = new StringBuilder();
        try (Scanner scanner = new Scanner(salesTestFile)) {
            while (scanner.hasNextLine()) {
                builder.append(scanner.nextLine()).append("\r\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        testResult = builder.toString();
        Assert.assertEquals(expectedResult, testResult);

    }

    @Test
    public void writeSalesReportEmpty() {

        VendingMachineLog.writeSalesReport("Snickers", salesTestFile);
        String expectedResult = "Lays|0\r\nCoke|0\r\nBig League Chew|0\r\nSnickers|1\r\n\r\nTotal Sales: $0.85\r\n";
        StringBuilder builder = new StringBuilder();
        try (Scanner scanner = new Scanner(salesTestFile)) {
            while (scanner.hasNextLine()) {
                builder.append(scanner.nextLine()).append("\r\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        testResult = builder.toString();
        Assert.assertEquals(expectedResult, testResult);

    }

    @Test
    public void totalSalesCalcForLaysOneFifty() {

        String itemPurchased = chipArray[1];
        BigDecimal result = VendingMachineLog.totalSalesCalc(itemPurchased);
        BigDecimal expectedResult = new BigDecimal("0");
        expectedResult = expectedResult.add(new BigDecimal("1.50"));

        Assert.assertEquals(expectedResult, result);
    }
}


