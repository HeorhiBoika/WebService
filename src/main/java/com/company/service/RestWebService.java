package com.company.service;

import com.company.bean.Book;

import java.util.List;

/**
 * Created by PC on 21.04.2017.
 */
public interface RestWebService {

    void addBook(Book book);

    List listBooks();

    void updateBook(Book book);

    void removeBookById(int id);

    void removeBooks();

    Book getBookById(int id);
}
