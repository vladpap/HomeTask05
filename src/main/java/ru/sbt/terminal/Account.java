package ru.sbt.terminal;

import java.util.Date;
import java.util.Random;

public class Account {
    private static final int foldAmountOfMoney = 100;
    private static final String splinParserString = "§";

    private static boolean isAccountAccess;
    private int countIncorrectPin;
    private String pin;
    private int paymentAccountNumber;
    private String name;
    private int cashAccount;
    private Date timeWait;


    public Account() {
        this.isAccountAccess = false;
        this.countIncorrectPin = -1;
    }

    private void Account(int paymentAccountNumber) {
        this.isAccountAccess = false;
        this.countIncorrectPin = 0;
        this.paymentAccountNumber = paymentAccountNumber;
    }

    public boolean isAccountAccess() {
        return isAccountAccess;
    }

    public int getCountIncorrectPin() {
        return countIncorrectPin;
    }

    public Date getTimeWait() {
        return timeWait;
    }

    public int getCashAccount() {
        return cashAccount;
    }

    public static int putAccountMoney(Account account, int money) {
        if ((money % foldAmountOfMoney != 0) && (!account.isAccountAccess)) {
            return 0;
        }
        account.cashAccount += money;
        AccountToFile.updateBase(account);
        return money;
    }

    public static int removeAccountMoney(Account account, int money) {
        if ((money % foldAmountOfMoney != 0) && (!account.isAccountAccess)) {
            return 0;
        }
        if (account.cashAccount < money) {
            money = account.cashAccount;
        }
        account.cashAccount -= money;

        AccountToFile.updateBase(account);
        return money;
    }


    public static Account createNewAccount(String name) {
        if (AccountToFile.isNameAccount(name)) {
            throw new QueryAccountException("The " + name + " user has already.");
        }
        Account account = new Account();
        account.name = name;
        // Generate random account number
        String temp = "";
        Random rn = new Random();
        do {
            for (int i = 0; i < 9; i++) {
                int number = rn.nextInt(10);
                temp += number;
            }
        } while ((AccountToFile.isNameAccount(temp)) && (temp.length() == 9));
        account.paymentAccountNumber = Integer.valueOf(temp);
        // Generate random pin number
        temp = "";
        for (int i = 0; i < 4; i++) {
            int number = rn.nextInt(10);
            temp += number;
        }
        account.pin = temp;

        return account;
        /*if (countIncorrectPin == -1) {
            throw new AccountException("This metod only save account.");
        }
        this.paymentAccountNumber = paymentAccountNumber;
        this.name = name;
        this.cashAccount = cashAccount;*/
    }

    public String accountParthe() {
        String result = splinParserString + this.name +
                splinParserString + this.paymentAccountNumber +
                splinParserString + this.cashAccount +
                splinParserString + this.pin;
        return result;
    }

    public static Account stringParseToAccount(String s) {
        String[] parseStr = s.split(splinParserString);
        if (parseStr.length != 5) {
            throw new AccountException("Error parse string to Account");
        }
        Account result = new Account();
        // parseStr[0] == "";
        result.name = parseStr[1];
        result.paymentAccountNumber = Integer.valueOf(parseStr[2]);
        result.cashAccount = Integer.valueOf(parseStr[3]);
        result.pin = parseStr[4];
        result.countIncorrectPin = 0;
        return result;
    }

    public static void validatePin(Account account, String pin) {
        if (account.countIncorrectPin == 3) {
            Date currentTime = new Date();
            if (currentTime.getTime() < (account.timeWait.getTime() + 20000)) {
                return;
            } else {
                account.countIncorrectPin = 2;
            }
        }
        if (account.pin.equals(pin)) {
            account.isAccountAccess = true;
        } else {
            account.countIncorrectPin++;
            if (account.countIncorrectPin == 3) {
                account.timeWait = new Date();
            }
        }
    }
}