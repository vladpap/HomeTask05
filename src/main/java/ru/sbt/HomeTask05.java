package ru.sbt;


import ru.sbt.terminal.*;

import java.util.Date;
import java.util.Scanner;

public class HomeTask05 {
    public static void main(String[] args) {
        System.out.println("Hello ...");
        try {
            AccountToFile.setAccount(Account.createNewAccount("Vladimir Papin"));
            AccountToFile.setAccount(Account.createNewAccount("Ivan Sidorov"));
            AccountToFile.setAccount(Account.createNewAccount("Petr Ivanov"));
        } catch (QueryAccountException e) {
            System.out.println("View baseAccount.txt file.");
        }


        boolean isExit = false;
        String lineRead = "";
        Scanner scanner = new Scanner(System.in);
        do {
            printLine();
            System.out.print("Enter account number or \'exit\' to exit: ");
            lineRead = scanner.nextLine();
            if (lineRead.toLowerCase().contains("exit")) {
                isExit = true;
            } else {
                int number = 0;
                if (lineRead.length() != 9) {
                    System.out.println("No correct account number. 9 symbols of numeric.");
                    continue;
                }
                try {
                    number = Integer.valueOf(lineRead);
                } catch (NumberFormatException e) {
                    System.out.println("No correct account number.");
                }
                TerminalMashine terminalMashine = new TerminalMashine(AccountToFile.getAccount(number));
                isExit = workTerminal(terminalMashine);
            }
        } while (!isExit);
    }


    public static boolean workTerminal(TerminalMashine terminalMashine) {
        boolean isExit = false;
        String lineRead = "";
        Scanner scanner = new Scanner(System.in);
        do {
            printLine();
            if (terminalMashine.checkAccountStatus() == -1) {
                System.out.println("With no user number");
                continue;
            }
            if (terminalMashine.checkAccountStatus() == 808) {
                isExit = workWithAccount(terminalMashine);
                if (!isExit) {
                    isExit = true;
                    lineRead = "0";
                }
                continue;
            }
            System.out.print("Enter pin code or \'0\' for change account number or \'exit\' for exit :");
            lineRead = scanner.nextLine();
            if (lineRead.equals("0")) {
                isExit = true;
                continue;
            }
            if (lineRead.toLowerCase().equals("exit")) {
                isExit = true;
                continue;
            }
            if (terminalMashine.checkAccountStatus() == 3) {
                long waitSec = 20 - ((new Date().getTime()) - terminalMashine.getTimeWait().getTime()) / 1000;
                System.out.println("3 attempts used. Wait :" + (waitSec > 0 ? waitSec : 0) + " sec.");
                if (waitSec > 0) {
                    continue;
                }
            }
            terminalMashine.validatePin(lineRead);

        } while (!isExit);
        if (lineRead.equals("0")) {
            isExit = false;
        }
        return isExit;
    }

    public static boolean workWithAccount(TerminalMashine terminalMashine) {
        boolean isExit = false;
        System.out.println("Work with account.");
        Scanner scanner = new Scanner(System.in);
        String lineRead;
        do {
            printLine();
            System.out.println("1 - check cash account");
            System.out.println("2 - add cash account");
            System.out.println("3 - withdraw account");
            System.out.println("0 - return");
            System.out.print("\'Exit\' - to exit :");
            lineRead = scanner.nextLine();
            if (lineRead.equals("0")) {
                isExit = true;
                continue;
            }
            if (lineRead.toLowerCase().equals("exit")) {
                isExit = true;
                continue;
            }
            // TODO: 05.08.16 работа со счетом
            if (lineRead.equals("1")) {         //      check cash account
                System.out.println("Current cash account = " + terminalMashine.getCashAccount());
            }
            if (lineRead.equals("2")) {         //      add cash account
                System.out.print("Enter cash : ");
                lineRead = scanner.nextLine();
                int number = 0;
                try {
                    number = Integer.valueOf(lineRead);
                } catch (NumberFormatException e) {
                    System.out.println("No correct account number.");
                }
                int result = terminalMashine.putMoney(number);
                if (result == number) {
                    System.out.println("Its okey, add " + result + " money");
                } else {
                    System.out.println("Error, add " + result + " money");
                }
            }
            if (lineRead.equals("3")) {         //      withdraw account
                System.out.print("Enter cash : ");
                lineRead = scanner.nextLine();
                int number = 0;
                try {
                    number = Integer.valueOf(lineRead);
                } catch (NumberFormatException e) {
                    System.out.println("No correct account number.");
                }
                int result = terminalMashine.removeMoney(number);
                if (result == number) {
                    System.out.println("Its okey, withdraw " + result + " money");
                } else {
                    System.out.println("Error, withdraw " + result + " money");
                }
            }
        } while (!isExit);
        if (lineRead.equals("0")) {
            isExit = false;
        }
        return isExit;
    }

    public static void printLine() {
        System.out.println("------------------------------------------------------------------------------------");
    }

}