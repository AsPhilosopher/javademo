package com.plain.elasticsearch.service.impl;

import com.plain.elasticsearch.dao.PersonDao;
import com.plain.elasticsearch.entity.Person;
import com.plain.elasticsearch.entity.RespnseModel;
import com.plain.elasticsearch.service.PersonService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PersonServiceImpl implements PersonService {

	private Logger log = Logger.getLogger(getClass());
	
	@Autowired
	private PersonDao personDao;
	
	@Override
	public Map<String, Object> savePerson(Person p) {
		String id =null;
		try {
			id = personDao.save(p);
			System.out.println("save Person="+p);
			System.out.println("id="+id);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("save error", e);
			return RespnseModel.getErrorModel();
		}
		return RespnseModel.getModel("ok", 200, id);
	}

	@Override
	public Map<String, Object> updatePerson(Person p) {
		String id =null;
		try {
			System.out.println("update Person="+p);
			id = personDao.update(p);
			System.out.println("id="+id);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("save error", e);
			return RespnseModel.getErrorModel();
		}
		return RespnseModel.getModel("ok", 200, id);
	}

	@Override
	public Map<String, Object> delPerson(String id) {
		try {
			id = personDao.deltele(id);
			System.out.println("id="+id);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("save error", e);
			return RespnseModel.getErrorModel();
		}
		return RespnseModel.getModel("ok", 200, id);
	}

	@Override
	public Map<String, Object> findPerson(String id) {
		Object obj = null;
		try {
			obj = personDao.find(id);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("save error", e);
			return RespnseModel.getErrorModel();
		}
		return RespnseModel.getModel("ok", 200, obj);
	}

	@Override
	public Map<String, Object> queryPerson(Person p) {
		Object obj = null;
		try {
			System.out.println("query Person="+p);
			obj = personDao.query(p);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("save error", e);
			return RespnseModel.getErrorModel();
		}
		return RespnseModel.getModel("ok", 200, obj);
	}
	
	
	
}
