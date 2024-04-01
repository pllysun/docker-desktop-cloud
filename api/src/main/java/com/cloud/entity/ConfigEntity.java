package com.cloud.entity;


public class ConfigEntity {

    public  static final String Image_NameSpace="dev";

    public  static final Integer Replicas=1;

    public  static final Integer RevisionHistoryLimit=3;

    public  static final String  MatchLabels_Key="app";

    public  static final String  Service_Type="NodePort";

    public  static final String  CPU="cpu";

    public  static final String  Memory="memory";

    public  static final String  Service="-service";

    public  static final String  Open_Container_Status="Open";

    public  static final String  Initial_Release="v1";

    public  static final String  Port_Name="http";

    public  static final String  Port_Protocol="TCP";

    public  static final Integer  Port_TargetPort=6901;

    public  static final String   Host_Ip="175.27.141.199";

    public  static final Integer  Close_Replicas=0;

    public static  final Integer Open_Replicas=1;

    public static  final String  Capacity_Storage="storage";

    public static  final String  Disk_Unit="Gi";

    public static final String  Memory_Unit="Gi";

    public static  final String  AccessModes="ReadWriteOnce";

    public static  final String  PersistentVolumeReclaimPolicy="Retain";

    public static  final String  NfsFileName="/root/data/";

    public static  final String  PV_Name="-pv";

    public static  final String  PVC_Name="-pvc";

    public static  final String  VolumeName="my-volume";

    public static  final boolean BoolReadOnly=false;

    public static  final Integer Image_Log_Type=2;

    public static  final String  Image_Log_Content="创建桌面";

    public static  final Integer Open_Log_Type=3;

    public static  final String  Open_Log_Content="开启桌面";

    public static  final Integer Close_Log_Type=4;

    public static final String  Close_Log_Content="关闭桌面";

    public static final  Integer Expansion_Log_Type=5;

    public static final Integer Office_Image_Source=0;

    public static final Integer Custom_Image_Source=2;


    public static final String Network_Name="-network";

    public static final Integer Number_Of_Desktop_Limit=6;

    public static final Integer Container_Name_Limit=30;

    public static final String Close_Container_Status="Close";

    public static final Integer Date_Image_Use_Count=7;

    public static final String MountPath="/root";

    public static final Integer Use_Time=7;

    public static final Integer Today =1;

    public static final String Network_Suffix="0/24";

    public static final String Docker_Tcp="tcp://118.195.216.42:2375";

    public static final String Image="-image";

    public static final String Image_Pull_Policy="Never";

    public static final String Nfs_Ip="10.206.16.17";

    public  static  String  Expansion_Log_Content(String podControllerName,Integer PodSystemDisk){
        return "扩容桌面"+podControllerName+"扩容到"+PodSystemDisk+"Gi";
    }

    public static  final Integer Delete_Log_Type=6;

    public static final  String  Delete_Log_Content="删除桌面";

    public static  final Integer Update_Log_Type=7;

    public static  String  Update_Log_Content(String podControllerName){
        return "修改"+podControllerName+"桌面名称为";
    }

    public static final  Integer Reinstall_Log_Type=8;

    public  static  final String Reinstall_Log_Content="重装桌面";

    public static   final Integer Delete_Image_Log_Type=9;

    public  static   final String Delete_Image_Log_Content="删除桌面镜像";

    public static   final Integer Create_Image_Log_Type=10;

    public  static   final String Create_Image_Log_Content="创建桌面镜像";

    public static   final Integer Update_Image_Log_Type=11;

    public static  String  Update_Image_Log_Content(String imageRemark){
        return "修改"+imageRemark+"桌面名称为";
    }

    public static final Integer Delete_Time=7;

    public static final Integer Disk_Top=20;

    public static final Integer CPU_Top=2;

    public static final Integer Memory_Top=8;

    public static final String Pod_Selector_Key="role";

    public static final String NetWork_policyTypes="Ingress";

    public static final Integer Create_Network_Log_Type=12;

    public static final String Create_Network_Log_Content="创建网络";

    public static final Integer Delete_Network_Log_Type=13;

    public static final String Delete_Network_Log_Content="删除网络";

    public static final Integer Update_Network_Log_Type=14;
    public static  String  Update_Network_Log_Content(String NetworkName){
        return "修改"+NetworkName+"网络名称为";
    }

    public static final  Integer User_Source=1;
}

