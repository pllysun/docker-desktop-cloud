package com.cloud.entity;

import org.springframework.context.annotation.Configuration;


public class ConfigEntity {
    public  static final String Image_NameSpace="dev";//命名空间
    public  static final Integer Replicas=1;//副本数量
    public  static final Integer RevisionHistoryLimit=3;//回退版本数
    public  static final String  MatchLabels_Key="app";
    public  static final String  Service_Type="NodePort";
    public  static final String  CPU="cpu";
    public  static final String  Memory="memory";
    public  static final String  Service="-service";
    public  static final String  ContainerStatus="Open";
    public  static final String  InitialRelease="v1";
    public  static final String  Port_Name="http";
    public  static final String  Port_Protocol="TCP";
    public  static final Integer  Port_TargetPort=80;
    public  static final Integer  Port_Port=80;
    public  static final String   Host_Ip="192.168.10.100";
    public  static final Integer  Close_Replicas=0;
    public static  final Integer Open_Replicas=1;
}
