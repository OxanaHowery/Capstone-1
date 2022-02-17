package com.techelevator;

import com.techelevator.view.Submenu;
import com.techelevator.view.VendingMachineLog;
import com.techelevator.view.Menu;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class VendingMachine {

    //properties
    private static final String MAIN_MENU_OPTION_DISPLAY_ITEMS = "Display Vending Machine Items"; //1
    private static final String MAIN_MENU_OPTION_PURCHASE = "Purchase"; //2
    private static final String MAIN_MENU_OPTION_EXIT = "Exit"; //3
    private static final String MAIN_MENU_OPTION_SALES_REPORT = "Sales Report"; //4, hidden from user
    private static final String[] MAIN_MENU_OPTIONS = //[0-3] = option 1-4
            {MAIN_MENU_OPTION_DISPLAY_ITEMS, MAIN_MENU_OPTION_PURCHASE, MAIN_MENU_OPTION_EXIT, MAIN_MENU_OPTION_SALES_REPORT};
    private static final String SUBMENU_OPTION_FEED_MONEY = "Feed Money";
    private static final String SUBMENU_OPTION_SELECT_PRODUCT = "Select Product";
    private static final String SUBMENU_OPTION_FINISH_TRANSACTION = "Finish Transaction";
    private static final String[] SUBMENU_OPTIONS =
            {SUBMENU_OPTION_FEED_MONEY, SUBMENU_OPTION_SELECT_PRODUCT, SUBMENU_OPTION_FINISH_TRANSACTION, ""};

    private static List<String[]> itemsArray;
    private static BigDecimal _machineBalance;
    private static String filePath = "src/main/resources/VendingMachine.txt";
    private static File vendingMachineFile = new File(filePath);
    private Scanner stockScanner;
    private Menu menu;
    private Submenu submenu;


    // getters
    public static List<String[]> getItemsArray() {
        return itemsArray;
    }

    public static BigDecimal get_machineBalance() {
        return _machineBalance;
    }

    //setter
    public static void setItemsArray(List<String[]> itemsArray) {
        VendingMachine.itemsArray = itemsArray;
    }

    public static void set_machineBalance(BigDecimal _machineBalance) {
        VendingMachine._machineBalance = _machineBalance;
    }

    //constructor
    public VendingMachine(Menu menu, Submenu submenu) {
        this.submenu = submenu;
        this.menu = menu;
        itemsArray = stockVendingMachine(vendingMachineFile);
    }

    //methods
    public void run() {
        VendingMachineLog.log("New Run", VendingMachineLog.getLogFilePath());
        while (true) { //main menu loop

            String choice = (String) menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);

            if (choice.equals(MAIN_MENU_OPTION_DISPLAY_ITEMS)) {
                // display vending machine items
                printDisplay(itemsArray);
            } else if (choice.equals(MAIN_MENU_OPTION_PURCHASE)) {
                while (true) { //submenu loop
                    String subchoice = (String) menu.getChoiceFromOptions(SUBMENU_OPTIONS);

                    if (subchoice.equals(SUBMENU_OPTION_FEED_MONEY)) {
                        //
                        set_machineBalance(submenu.feedMoney(submenu.askForMoney()));
                        System.out.println("Current Balance is $" + get_machineBalance());
                    } else if (subchoice.equals(SUBMENU_OPTION_SELECT_PRODUCT)) {
                        //
                        printDisplay(itemsArray);
                        System.out.println();
                        set_machineBalance(submenu.selectProduct(get_machineBalance()));
                        VendingMachineLog.readSalesReport(VendingMachineLog.getSalesReport());
                        VendingMachineLog.writeSalesReport(submenu.getItemPurchased(), VendingMachineLog.getSalesReport());
                        System.out.println("Current Balance is $" + _machineBalance);
                    } else if (subchoice.equals(SUBMENU_OPTION_FINISH_TRANSACTION)) {
                        //give change method
                        set_machineBalance(submenu.getChange(_machineBalance));
                        System.out.println("Current Balance is " + _machineBalance);
                        break;
                    }
                }
                // do purchase
            } else if (choice.equals(MAIN_MENU_OPTION_EXIT)) {
                System.out.println("Have a nice day!");
                break;
            } else if (choice.equals(MAIN_MENU_OPTION_SALES_REPORT)) {
                try {
                    Scanner scanner = new Scanner(new File("src/main/resources/salesReport.txt"));
                    while (scanner.hasNextLine()) {
                        System.out.println(scanner.nextLine());
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 0 spot| 1 item name| 2 price| 3 item type
    //inventory = list of arrays of strings
    public List<String[]> stockVendingMachine(File vendingMachineFile) {
        List<String[]> itemsArray = new ArrayList<>();
        try {
            stockScanner = new Scanner(vendingMachineFile);
            List<String> items = new ArrayList<>();
            while (stockScanner.hasNextLine()) {
                String line = stockScanner.nextLine();
                items.add(line + "|5");
            }
            for (int i = 0; i < items.size(); i++) {
                String[] itemArray = items.get(i).split("\\|");
                itemsArray.add(itemArray);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return itemsArray;
    }

    // customer wants to see 1, 0, 2 (item name, item slot, item price)
    public void printDisplay(List<String[]> itemsArray) {
        for (String[] itemLine : itemsArray) {
            if (!itemLine[4].contains("0")) {
                System.out.println(itemLine[0] + ") " + itemLine[1] + ": $" + itemLine[2] + " (" + itemLine[4] + " items in stock)");
            } else
                System.out.println(itemLine[0] + ") " + itemLine[1] + ": $" + itemLine[2] + " (Out of Stock)");
        }
    }

    // main
    public static void main(String[] args) {
        Menu menu = new Menu(System.in, System.out);
        Submenu submenu = new Submenu(System.in, System.out);
        VendingMachine cli = new VendingMachine(menu, submenu);
        cli.run();
    }

}
