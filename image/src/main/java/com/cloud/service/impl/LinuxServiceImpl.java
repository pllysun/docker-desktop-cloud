package com.cloud.service.impl;

import com.cloud.DTO.DeskTopDto;
import com.cloud.config.LinuxConfig;
import com.cloud.service.LinuxService;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Service
@Slf4j
public class LinuxServiceImpl implements LinuxService {

    @Autowired
    LinuxConfig linuxConfig;

    @Override
    public void  createNfsFile(Integer userId,DeskTopDto deskTopDto, Session session) throws JSchException {
        String soleName=deskTopDto.getPodControllerName()+"-"+userId;
        log.info("soleName:{}",soleName);
        Channel channel = session.openChannel("exec");
        String createFolderCommand = "mkdir "+linuxConfig.getFilename()+ soleName;
        ((ChannelExec) channel).setCommand(createFolderCommand);
        // 获取命令执行结果
        channel.setInputStream(null);
        ((ChannelExec) channel).setErrStream(System.err);
        // 连接并执行命令
        channel.connect();
        channel.disconnect();
    }

    @Override
    //文件连接nfs服务
    public void connectNfs(Integer userId,DeskTopDto deskTopDto,Session session) throws JSchException {
        String soleName=deskTopDto.getPodControllerName()+"-"+userId;
        Channel channel = session.openChannel("exec");
        String writeNfsOrder = "echo \""+linuxConfig.getFilename()+ soleName+" "+linuxConfig.getNfsOrder()+"\" "+" >> "+linuxConfig.getNfsFile();
        System.out.println(writeNfsOrder);
        ((ChannelExec) channel).setCommand(writeNfsOrder);
        // 获取命令执行结果
        channel.setInputStream(null);
        ((ChannelExec) channel).setErrStream(System.err);
        // 连接并执行命令
        channel.connect();
        channel.disconnect();
    }

    @Override
    public Integer computeStore(Session session) throws IOException, JSchException {
        String command = "df -k /root | tail -n 1 | awk '{print $4}'";
        ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
        channelExec.setCommand(command);
        channelExec.setInputStream(null);
        channelExec.setErrStream(System.err);
        InputStream in = channelExec.getInputStream();
        channelExec.connect();

        int availableSpaceKB = 0;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
            String line = reader.readLine();
            if (line != null) {
                availableSpaceKB = Integer.parseInt(line.trim());
            }
        }
        return availableSpaceKB / 1024 / 1024;
    }
    @Override
    //重启nfs服务
    public void restartNfs(Session session) throws JSchException {
        Channel channel = session.openChannel("exec");
        String deleteFolderCommand ="systemctl restart nfs-server";
        ((ChannelExec) channel).setCommand(deleteFolderCommand);
        // 获取命令执行结果
        channel.setInputStream(null);
        ((ChannelExec) channel).setErrStream(System.err);
        // 连接并执行命令
        channel.connect();
        channel.disconnect();
    }
}
