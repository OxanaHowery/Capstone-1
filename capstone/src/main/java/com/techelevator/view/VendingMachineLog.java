package com.techelevator.view;

import com.techelevator.VendingMachine;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

public class VendingMachineLog {
    private static final SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a");
    private static final Date date = new Date();
    private static final Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    private static File salesReport = new File("src/main/resources/salesReport.txt");



    private static boolean isReport = false;
    private static BigDecimal _legacySalesTotal = new BigDecimal(0);
    public static List<String[]> legacyList = new ArrayList<>();



    private static String logFilePath = "src/main/resources/log.txt";
    private static List<String[]> itemsArray = VendingMachine.getItemsArray();

    //getter
    public static boolean isIsReport() {
        return isReport;
    }

    public static SimpleDateFormat getFormatter() {
        return formatter;
    }

    public static Timestamp getTimestamp() {
        return timestamp;
    }

    public static List<String[]> getItemsArray() {
        return itemsArray;
    }
    public static File getSalesReport() {
        return salesReport;
    }

    public static void setItemsArray(List<String[]> itemsArray) {
        VendingMachineLog.itemsArray = itemsArray;
    }

    public static List<String[]> getLegacyList() {
        return legacyList;
    }
    public static String getLogFilePath() {
        return logFilePath;
    }

    public static void setLogFilePath(String logFilePath) {
        VendingMachineLog.logFilePath = logFilePath;
    }

    public static void log(String message, String logFilePath) {
        try (PrintWriter writer = new PrintWriter(new FileOutputStream(logFilePath, true))) {
            writer.println(formatter.format(timestamp) + " " + message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void readSalesReport(File salesReport) {
        try (Scanner salesScanner = new Scanner(salesReport)) {
            if (salesScanner.hasNext()) {
                isReport = true;
                for (int i = 0; i < VendingMachine.getItemsArray().size(); i++) {
                    legacyList.add(salesScanner.nextLine().split("\\|"));
                }
                if (salesScanner.next().contains("Total")) {
                    String[] salesArray = salesScanner.nextLine().split("\\$");
                    _legacySalesTotal = new BigDecimal(salesArray[1]);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void writeSalesReport(String itemPurchased, File salesReport) {
        try (PrintWriter writer = new PrintWriter(new FileOutputStream(salesReport, false))) {
            if (!isReport) {
                for (String[] itemLine : itemsArray) {
                    if (itemLine[1].equals(itemPurchased)) {
                        writer.println(itemLine[1] + "|" + 1);
                    } else {
                        writer.println(itemLine[1] + "|" + 0);
                    }
                }
            } else {
                for (int i = 0; i < legacyList.size(); i++) {
                    if (legacyList.get(i)[0].equals(itemPurchased)) {
                        int toInteger = Integer.parseInt(legacyList.get(i)[1]);
                        writer.println(legacyList.get(i)[0] + "|" + (toInteger + 1));
                    } else {
                        writer.println(legacyList.get(i)[0] + "|" + legacyList.get(i)[1]);
                    }
                }
                legacyList.clear();
            }
            writer.println();
            writer.println("Total Sales: $" + totalSalesCalc(itemPurchased));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static BigDecimal totalSalesCalc(String itemPurchased) {
        BigDecimal _totalSales = new BigDecimal(0);

        for (String[] item : itemsArray) {
            if (item[1].contains(itemPurchased)) {
                BigDecimal priceOfItem = new BigDecimal(item[2]);
                _totalSales = _totalSales.add(priceOfItem); // multiply here by number of sales
            }
        }
        _totalSales = _totalSales.add(_legacySalesTotal);
        return _totalSales;
    }
}
