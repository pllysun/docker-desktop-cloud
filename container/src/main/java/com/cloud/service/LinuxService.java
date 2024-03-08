package com.cloud.service;

import com.cloud.DTO.ContainerDto;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

import java.io.IOException;


public interface LinuxService {
    Integer computeStore(Session session) throws IOException, JSchException;

    void deleteNfsFile(ContainerDto deskTopDto, Session session) throws JSchException;

    void emptyNfsFile(ContainerDto deskTopDto, Session session)throws JSchException;
}
