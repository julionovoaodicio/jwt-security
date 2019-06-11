package com.jakublesko.jwtsecurity.documentdb.sample.dao;

public class TodoDaoFactory {
    private static TodoDao myTodoDao;

    public static TodoDao getDao() {
        if (myTodoDao == null) {
            myTodoDao = new DocDbDao();
        }
        return myTodoDao;
    }
}
