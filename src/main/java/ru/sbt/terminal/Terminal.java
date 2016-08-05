package ru.sbt.terminal;

public interface Terminal {

    int checkAccountStatus();

    int removeMoney(int amount);

    int putMoney(int amount);

    void validatePin(String pin);
}