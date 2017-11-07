package com.goit.jdbc.app.dao;

import com.goit.jdbc.app.Developer;

import java.util.List;

//CRUD - Create, Read, Update, Delete
public interface DeveloperDAO {
    void createDeveloper(Developer developer);
    Developer getDeveloper(long id);
    void updateDeveloper(Developer developer);
    void deleteDeveloper(long id);

    List<Developer> listDevelopers();
    void addDevelopers(List<Developer> developers);
}
