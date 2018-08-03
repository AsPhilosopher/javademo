package com.plain.elasticsearch.controller;

import com.plain.elasticsearch.entity.Person;
import com.plain.elasticsearch.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
public class PersonController {

    @Autowired
    private PersonService personservice;

    /**
     * 新增
     *
     * @param name
     * @param sex
     * @param age
     * @param introduce
     * @param birthday
     * @return
     */
    @RequestMapping(value = "/save/person", method = RequestMethod.GET)
    public Object savePerson(@RequestParam(name = "name") String name,
                             @RequestParam(name = "sex") String sex,
                             @RequestParam(name = "age") Integer age,
                             @RequestParam(name = "introduce") String introduce,
                             @RequestParam(name = "birthday") @DateTimeFormat(pattern = "yyyy-MM-dd") Date birthday
    ) {
        System.out.println("name=" + name);
        System.out.println("sex=" + sex);
        System.out.println("age=" + age);
        System.out.println("introduce=" + introduce);
        System.out.println("birthday=" + birthday);
        Person person = new Person(name, age, sex, birthday, introduce);
        return personservice.savePerson(person);
    }


    /**
     * 更新
     *
     * @param id
     * @param name
     * @param sex
     * @param age
     * @param introduce
     * @param birthday
     * @return
     */
    @RequestMapping(value = "/update/person/{id}", method = RequestMethod.GET)
    public Object updatePerson(@PathVariable("id") String id
            , @RequestParam(name = "name") String name
            , @RequestParam(name = "sex") String sex
            , @RequestParam(name = "age") int age
            , @RequestParam(name = "introduce") String introduce
            , @RequestParam(name = "birthday") @DateTimeFormat(pattern = "yyyy-MM-dd") Date birthday
    ) {
        Person person = new Person(name, age, sex, birthday, introduce);
        person.setId(id);
        return personservice.updatePerson(person);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/del/person/{id}", method = RequestMethod.GET)
    public Object delPerson(@PathVariable("id") String id) {
        return personservice.delPerson(id);
    }

    /**
     * 获取数据
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/person/{id}", method = RequestMethod.GET)
    public Object getPerson(@PathVariable("id") String id) {
        return personservice.findPerson(id);
    }

    /**
     * 聚合查询
     *
     * @param name
     * @param age
     * @param introduce
     * @return
     */
    @RequestMapping(value = "/query/person/_search", method = RequestMethod.GET)
    public Object queryPerson(@RequestParam(name = "name", required = false) String name
            , @RequestParam(name = "age", required = false, defaultValue = "0") Integer age
            , @RequestParam(name = "introduce", required = false) String introduce) {
        Person person = new Person(name, age, null, null, introduce);
        return personservice.queryPerson(person);
    }

}
