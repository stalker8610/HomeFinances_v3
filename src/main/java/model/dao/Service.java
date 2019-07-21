package model.dao;

import model.Account;
import model.BudgetRow;
import model.Record;
import model.UserProfile;

import java.util.Date;
import java.util.List;

public interface Service {

    long addUser(String name, String pass);
    UserProfile getUserById(long id);

    long addAccount(UserProfile userProfile, String name);
    Account getAccountById(long id);
    List<Account> getAccountByName(String namePattern);
    void deleteAccount(long id);

    long addBudgetRow(String name, int rowType);
    BudgetRow getBudgetRowById(long id);
    List<BudgetRow> getBudgetRowByName(String namePattern);
    void deleteBudgetRow(long id);

    long addRecord(Date date, Account sender, Account recipient, BudgetRow row, int sum);
    Record getRecordById(long id);
    List<Record> getRecordsBySender(Account sender);
    void deleteRecord(long id);

}
