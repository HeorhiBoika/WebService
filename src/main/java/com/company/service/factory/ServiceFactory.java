package com.company.service.factory;

import com.company.service.RestWebService;
import com.company.service.implementation.RestWebServiceImpl;

/**
 * Created by PC on 21.04.2017.
 */
public class ServiceFactory {
    private static final ServiceFactory instance = new ServiceFactory();

    private final RestWebService restWebService = new RestWebServiceImpl();

    private ServiceFactory() {

    }

    public static ServiceFactory getInstance() {
        return instance;
    }

    public RestWebService getBookService() {
        return restWebService;
    }

}
