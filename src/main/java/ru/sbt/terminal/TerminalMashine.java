package ru.sbt.terminal;

import java.util.Date;

public class TerminalMashine implements Terminal {

    private Account account;

    public TerminalMashine(Account account) {
        this.account = account;
    }

    @Override
    public int checkAccountStatus() {
        int result = this.account.getCountIncorrectPin();
        if (this.account.isAccountAccess()) {
            result = 808;
        }
        return result;
    }

    @Override
    public int removeMoney(int amount) {
        if (!this.account.isAccountAccess()) {
            return 0;
        }
        return Account.removeAccountMoney(this.account, amount);
    }

    @Override
    public int putMoney(int amount) {
        if (!this.account.isAccountAccess()) {
            return 0;
        }
        return Account.putAccountMoney(this.account, amount);
    }

    @Override
    public void validatePin(String pin) {
        Account.validatePin(this.account, pin);
    }

    public Date getTimeWait() {
        return this.account.getTimeWait();
    }

    public int getCashAccount() {
        return this.account.getCashAccount();
    }
}