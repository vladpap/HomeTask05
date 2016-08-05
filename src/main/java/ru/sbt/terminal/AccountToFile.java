package ru.sbt.terminal;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

public class AccountToFile {
    private static final String fileName = "baseAccount.txt";
    private static final Path path = Paths.get("./" + fileName);

    public static Account getAccount(int accountNumber) {
        List<String> list = new ArrayList<>(getAllAccounts());
        for (String s : list) {
            if (s.contains(String.valueOf(accountNumber))) {
                return Account.stringParseToAccount(s);
            }
        }
        return new Account();
    }

    public static boolean isNameAccount(String name) {
        List<String> listAccounts = new ArrayList<>(getAllAccounts());
        for (String s : listAccounts) {
            if (s.toLowerCase().contains(name.toLowerCase())) {
                return true;
            }
        }
        return false;
    }


    public static void setAccount(Account account) {

        byte data[] = (account.accountParthe() + "\r").getBytes();

        try (OutputStream out = new BufferedOutputStream(
                Files.newOutputStream(path, CREATE, APPEND))) {
            out.write(data, 0, data.length);
        } catch (IOException e) {
            throw new QueryAccountException("Error during write account from file", e);
        }
    }

    private static List<String> getAllAccounts() {

        List<String> strings;

        try {
            strings = Files.readAllLines(path);
        } catch (IOException e) {
            if (e.getClass() != NoSuchFileException.class) {
                throw new QueryAccountException("Error during reading accounts from file", e);
            } else {
                strings = new ArrayList<>();
            }
        }
        return strings;
    }

    public static void updateBase(Account accountUpdate) {
        List<String> strings = getAllAccounts();
        String[] account = accountUpdate.accountParthe().split("§");
        List<String> newStrings = new ArrayList<>();
        for (String s : strings) {
            if (s.contains(account[2])) {
                newStrings.add(accountUpdate.accountParthe());
            } else {
                newStrings.add(s);
            }
        }
        updateFileBase(newStrings);
    }

    private static void updateFileBase(List<String> newStrings) {
        try (OutputStream out = new BufferedOutputStream(Files.newOutputStream(path, CREATE))) {
            for (String s : newStrings) {
                byte data[] = (s + "\r").getBytes();
                out.write(data, 0, data.length);
            }
        } catch (IOException e) {
            throw new QueryAccountException("Error during write account from file", e);
        }
    }
}