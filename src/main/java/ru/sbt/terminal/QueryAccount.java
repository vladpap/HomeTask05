package ru.sbt.terminal;

public interface QueryAccount {

    Account getAccount(int accountNumber);

    void setAccount(Account account);
}
