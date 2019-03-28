package com.lvlivejp.masterslavedb.service;

import com.lvlivejp.masterslavedb.annotation.RouteingDataSource;
import com.lvlivejp.masterslavedb.config.DataSources;
import com.lvlivejp.masterslavedb.dao.AdminMessageMapper;
import com.lvlivejp.masterslavedb.model.AdminMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AdminMessageService {

    @Autowired
    private AdminMessageMapper adminMessageMapper;

    @RouteingDataSource()
    public List<AdminMessage> selectAdminMaster(){
        return adminMessageMapper.selectAll();
    }

    @RouteingDataSource(DataSources.SLAVE_DB)
    public List<AdminMessage> selectAdminSlave(){
        return adminMessageMapper.selectAll();
    }

    @Transactional
    public void insertMaster(AdminMessage adminMessage) {
        adminMessageMapper.insert(adminMessage);
    }

    @Transactional
    @RouteingDataSource(DataSources.SLAVE_DB)
    public void insertSlave(AdminMessage adminMessage) {
        adminMessageMapper.insert(adminMessage);
    }
}
