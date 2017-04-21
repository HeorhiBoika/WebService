package com.company.service.implementation;

import com.company.bean.Book;
import com.company.dao.HibernateUtil;
import com.company.dao.factory.DaoFactory;
import com.company.service.RestWebService;

import java.util.List;

/**
 * Created by PC on 21.04.2017.
 */
public class RestWebServiceImpl implements RestWebService {
    @Override
    public void addBook(Book book) {
        DaoFactory daoFactory = DaoFactory.getInstance();
        HibernateUtil hibernateUtil = daoFactory.getBook();
        hibernateUtil.addBook(book);
    }

    @Override
    public List listBooks() {
        DaoFactory daoFactory = DaoFactory.getInstance();
        HibernateUtil hibernateUtil = daoFactory.getBook();
        return hibernateUtil.listBooks();
    }

    @Override
    public void updateBook(Book book) {
        DaoFactory daoFactory = DaoFactory.getInstance();
        HibernateUtil hibernateUtil = daoFactory.getBook();
        hibernateUtil.updateBook(book);
    }

    @Override
    public void removeBookById(int id) {
        DaoFactory daoFactory = DaoFactory.getInstance();
        HibernateUtil hibernateUtil = daoFactory.getBook();
        hibernateUtil.removeBookById(id);
    }

    @Override
    public void removeBooks() {
        DaoFactory daoFactory = DaoFactory.getInstance();
        HibernateUtil hibernateUtil = daoFactory.getBook();
        hibernateUtil.removeBooks();
    }

    @Override
    public Book getBookById(int id) {
        DaoFactory daoFactory = DaoFactory.getInstance();
        HibernateUtil hibernateUtil = daoFactory.getBook();
        return hibernateUtil.getBookById(id);
    }
}
