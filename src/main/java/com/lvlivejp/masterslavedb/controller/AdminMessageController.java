package com.lvlivejp.masterslavedb.controller;
import java.util.Date;

import com.lvlivejp.masterslavedb.dao.AdminMessageMapper;
import com.lvlivejp.masterslavedb.model.AdminMessage;
import com.lvlivejp.masterslavedb.service.AdminMessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminMessageController {

    @Autowired
    AdminMessageService adminMessageService;

    @GetMapping("/selectmaster")
    public List<AdminMessage> selectmaster(){
        return adminMessageService.selectAdminMaster();
    }

    @GetMapping("/selectslave")
    public List<AdminMessage> selectslave(){
        return adminMessageService.selectAdminSlave();
    }

    @PostMapping("/insertMaster/{id}")
    public AdminMessage insertMaster(@PathVariable Long id){
        AdminMessage adminMessage = new AdminMessage();
        adminMessage.setId(id);
        adminMessage.setCtime(new Date());
        adminMessage.setStatus(0);
        adminMessage.setUtime(new Date());
        adminMessage.setContent("Master");
        adminMessage.setMessageType(0);
        adminMessage.setNeedOpen(false);
        adminMessage.setTitle("");
        adminMessageService.insertMaster(adminMessage);
        return adminMessage;
    }
    @PostMapping("/insertSlave/{id}")
    public AdminMessage insertSlave(@PathVariable Long id){
        AdminMessage adminMessage = new AdminMessage();
        adminMessage.setId(id);
        adminMessage.setCtime(new Date());
        adminMessage.setStatus(0);
        adminMessage.setUtime(new Date());
        adminMessage.setContent("Slave");
        adminMessage.setMessageType(0);
        adminMessage.setNeedOpen(false);
        adminMessage.setTitle("");
        adminMessageService.insertSlave(adminMessage);
        return adminMessage;
    }
}
