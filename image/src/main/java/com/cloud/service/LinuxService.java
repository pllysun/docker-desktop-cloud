package com.cloud.service;

import com.cloud.DTO.DeskTopDto;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.IOException;

public interface LinuxService {

    void connectNfs(Integer userId,DeskTopDto deskTopDto, Session session) throws JSchException;

    Integer computeStore(Session session) throws IOException, JSchException;

    void restartNfs(Session session) throws JSchException;

    void createNfsFile(Integer userId,DeskTopDto deskTopDto, Session session) throws JSchException;

}
