package com.plain.elasticsearch.dao;


import com.plain.elasticsearch.entity.Person;

public interface PersonDao {
    String save(Person person);

    String update(Person person);

    String deltele(String id);

    Object find(String id);

    Object query(Person person);
}
