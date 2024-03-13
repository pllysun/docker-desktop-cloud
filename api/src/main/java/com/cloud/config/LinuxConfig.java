package com.cloud.config;

import com.cloud.DTO.DeskTopDto;
import com.jcraft.jsch.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Component
public class LinuxConfig {
    //@Value("${Linux.host}")
    public  String host="175.27.141.199";
    //@Value("${Linux.port}")
    public  int port=22;
    //@Value("${Linux.username}")
    public  String user="root";
    //@Value("${Linux.password}")
    public  String password="1234509876@aaaa";
    //@Value("${Linux.filename}")
    public  String filename="/root/data/";
    //@Value("${Nfs.order}")
    public  String NfsOrder="175.27.141.0/24(rw,no_root_squash)";
    //@Value("${Nfs.file}")
    public  String NfsFile="/etc/exports";


    /**
     *  连接Linux服务器
     * @return
     * @throws JSchException
     */

//    @Bean
//    public Session sshConnect() throws JSchException {
//        // 创建JSch对象
//        JSch jsch = new JSch();
//        // 建立SSH会话
//        Session session = jsch.getSession(user, host, port);
//        session.setPassword(password);
//        session.setConfig("StrictHostKeyChecking", "no"); // 忽略HostKey检查
//        session.connect();
//        return session;
//    }


}
