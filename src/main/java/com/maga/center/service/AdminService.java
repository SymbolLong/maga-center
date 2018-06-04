package com.maga.center.service;

import com.maga.center.constants.SystemConstants;
import org.springframework.stereotype.Service;

@Service
public class AdminService {
    public void test(){
        SystemConstants.servers.get("100000");
    }
}
