package model.dao.impl;

import model.Account;
import model.BudgetRow;
import model.Record;
import model.UserProfile;
import model.dao.Service;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.Date;
import java.util.List;

public class ServiceImpl implements Service {

    private static SessionFactory sessionFactory;

    public ServiceImpl(){
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration().configure();
                configuration.addAnnotatedClass(Account.class);
                configuration.addAnnotatedClass(BudgetRow.class);
                configuration.addAnnotatedClass(Record.class);
                configuration.addAnnotatedClass(UserProfile.class);

                StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
                sessionFactory = configuration.buildSessionFactory(builder.build());

            } catch (Exception e) {
                System.out.println("Can't to configure Hibernate!" + e);
            }
        }
        return sessionFactory;
    }


    public long addAccount(UserProfile userProfile, String name) throws HibernateException {

        Session session = getSessionFactory().openSession();

        session.beginTransaction();
        Account account = new Account(name);
        account.setUserProfile(userProfile);
        long id = (Long) session.save(account);
        session.getTransaction().commit();

        session.close();
        return id;
    }

    public Account getAccountById(long id) throws HibernateException{
        Session session = getSessionFactory().openSession();
        return session.get(Account.class, id);
    }

    public List<Account> getAccountByName(String namePattern) throws HibernateException{

        Session session = getSessionFactory().openSession();
        List<Account> result;

        if (namePattern.equals("")){
            Query query = session.createQuery("From "+Account.class.getSimpleName());
            result = (List<Account>)query.list();
        }
        else{
            Query query = session.createQuery("from "+Account.class.getSimpleName()+" where name like :name");
            query.setParameter("name", "%" + namePattern + "%");
            result = (List<Account>)query.list();
        }

        session.close();
        return result;
    }

    public void deleteAccount(long id) throws HibernateException{

        Session session = getSessionFactory().openSession();
        session.beginTransaction();
        session.delete(getAccountById(id));
        session.getTransaction().commit();
        session.close();

    }

    public long addBudgetRow(String name, int rowType) throws HibernateException{

        Session session = getSessionFactory().openSession();

        session.beginTransaction();

        BudgetRow budgetRow;

        if (rowType == 0 )
            budgetRow = BudgetRow.costBudgetRow(name);
        else if (rowType == 1)
            budgetRow = BudgetRow.incomeBudgetRow(name);
        else
            budgetRow = BudgetRow.movementBudgetRow(name);

        long id = (Long) session.save(budgetRow);
        session.getTransaction().commit();
        session.close();
        return id;
    }

    public BudgetRow getBudgetRowById(long id) throws HibernateException{
        Session session = getSessionFactory().openSession();
        BudgetRow result = session.get(BudgetRow.class, id);
        session.close();
        return result;
    }

    public List<BudgetRow> getBudgetRowByName(String namePattern) throws HibernateException{

        Session session = getSessionFactory().openSession();

        List<BudgetRow> result;

        if (namePattern.equals("")){
            Query query = session.createQuery("From "+BudgetRow.class.getSimpleName());
            result = (List<BudgetRow>)query.list();
        }
        else{
            Query query = session.createQuery("from "+BudgetRow.class.getSimpleName()+" where name like :name");
            query.setParameter("name", "%" + namePattern + "%");
            result = (List<BudgetRow>)query.list();
        }

        session.close();
        return result;
    }

    public void deleteBudgetRow(long id) throws HibernateException{
        Session session = getSessionFactory().openSession();
        session.beginTransaction();
        session.delete(getBudgetRowById(id));
        session.getTransaction().commit();
        session.close();
    }



    public long addRecord(Date date, Account sender, Account recipient, BudgetRow row, int sum) throws HibernateException{

        Session session = getSessionFactory().openSession();
        session.beginTransaction();

        Record doc = new Record(date);
        doc.setBudgetRow(row);
        doc.setSum(sum);
        doc.setSender(sender);
        doc.setRecipient(recipient);

        long id = (Long) session.save(doc);
        session.getTransaction().commit();
        session.close();

        return id;

    }

    public Record getRecordById(long id) throws HibernateException{
        Session session = getSessionFactory().openSession();
        Record result = session.get(Record.class, id);
        session.close();
        return result;
    }

    public void deleteRecord(long id) throws HibernateException{
        Session session = getSessionFactory().openSession();
        session.beginTransaction();
        session.delete(getRecordById(id));
        session.getTransaction().commit();
        session.close();
    }

    @Override
    public List<Record> getRecordsBySender(Account sender)throws HibernateException{

        Session session = getSessionFactory().openSession();

        List<Record> result;

        if (sender == null){
            Query query = session.createQuery("From "+Record.class.getSimpleName());
            result = (List<Record>)query.list();
        }
        else{
            Query query = session.createQuery("from "+Record.class.getSimpleName()+" where sender = :sender");
            query.setParameter("sender", sender);
            result = (List<Record>)query.list();
        }

        session.close();
        return result;

    }

    public long addUser(String name, String pass) {
        Session session = getSessionFactory().openSession();

        session.beginTransaction();
        UserProfile userProfile = new UserProfile(name, pass);
        long id = (Long) session.save(userProfile);
        session.getTransaction().commit();

        session.close();
        return id;
    }

    public UserProfile getUserById(long id) {
        Session session = getSessionFactory().openSession();
        UserProfile result = session.get(UserProfile.class, id);
        session.close();
        return result;
    }
}
