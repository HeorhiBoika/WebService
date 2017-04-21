package com.company.dao.factory;

import com.company.dao.HibernateUtil;
import com.company.dao.implementation.HibernateUtilImpl;

/**
 * Created by PC on 21.04.2017.
 */
public class DaoFactory {
    private static final DaoFactory instance = new DaoFactory();

    private final HibernateUtil hibernateUtil = new HibernateUtilImpl();

    private DaoFactory() {

    }

    public static DaoFactory getInstance() {
        return instance;
    }

    public HibernateUtil getBook() {
        return hibernateUtil;
    }

}
