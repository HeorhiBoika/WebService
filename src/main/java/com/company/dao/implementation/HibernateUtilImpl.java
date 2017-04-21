package com.company.dao.implementation;

import com.company.bean.Book;
import com.company.dao.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import java.util.List;

/**
 * Created by PC on 14.04.2017.
 */
public class HibernateUtilImpl implements HibernateUtil {
    private SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();

    public HibernateUtilImpl() {
    }

    public void addBook(Book book) {
        Session session = sessionFactory.openSession();
        Transaction transaction = null;

        transaction = session.beginTransaction();
        session.save(book);
        transaction.commit();
        session.close();
    }

    public List listBooks() {
        Session session = this.sessionFactory.openSession();
        Transaction transaction = null;

        transaction = session.beginTransaction();
        Query query = session.createQuery("FROM Book");
        List books = query.list();
        transaction.commit();
        session.close();
        return books;
    }

    public void updateBook(Book book) {
        Session session = this.sessionFactory.openSession();
        Transaction transaction = null;

        transaction = session.beginTransaction();
        session.update(book);
        transaction.commit();
        session.close();
    }

    public void removeBookById(int id) {
        Session session = this.sessionFactory.openSession();
        Transaction transaction = null;

        transaction = session.beginTransaction();
        Book book = session.get(Book.class, id);
        session.delete(book);
        transaction.commit();
        session.close();
    }

    public void removeBooks() {
        Session session = null;

        session = this.sessionFactory.openSession();
        Transaction transaction = session.beginTransaction();
        session.createQuery("DELETE FROM Book").executeUpdate();
        transaction.commit();
        session.close();
    }

    public Book getBookById(int id) {
        Session session = this.sessionFactory.openSession();
        Transaction transaction = null;

        transaction = session.beginTransaction();
        Book book = session.get(Book.class, id);
        transaction.commit();
        session.close();

        return book;
    }

}
