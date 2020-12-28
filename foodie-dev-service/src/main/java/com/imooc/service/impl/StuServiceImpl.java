package com.imooc.service.impl;

import com.imooc.mapper.StuMapper;

import com.imooc.pojo.Stu;
import com.imooc.service.StuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

//开启事务支持
@Transactional(propagation = Propagation.SUPPORTS)
@Service
public class StuServiceImpl implements StuService {

    @Autowired
    private StuMapper stuMapper;


    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Stu getStuInfo(int id) {
        Stu stu = stuMapper.selectByPrimaryKey(id);
        return stu;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void saveStu() {
        Stu stu = new Stu();
        stu.setName("王大拿");
        stu.setAge(69);
        stuMapper.insertUseGeneratedKeys(stu);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateStu(int id) {
        Stu stu = new Stu();
        stu.setName("王小拿");
        stu.setAge(29);
        stu.setId(id);
        stuMapper.updateByPrimaryKey(stu);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteStu(int id) {
        stuMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void saveParent(){
        Stu stu = new Stu();
        stu.setName("爸爸");
        stu.setAge(40);
        stuMapper.insert(stu);
    }

    @Transactional(propagation = Propagation.NESTED)
    @Override
    public void saveChildren(){
        saveChild1();
        int a = 1/0;
        saveChild2();
    }

    public void saveChild1(){
        Stu stu1 = new Stu();
        stu1.setName("儿子1");
        stu1.setAge(19);
        stuMapper.insert(stu1);
    }

    public void saveChild2(){
        Stu stu1 = new Stu();
        stu1.setName("儿子2");
        stu1.setAge(18);
        stuMapper.insert(stu1);
    }
}
