package com.example.wei.gsknetclient_studio;

/**
 * Created by wei on 2017/4/10.
 */

public class GSKNativeApi {

    public GSKNativeApi(){

    }
    
  /*  static {
        System.loadLibrary("gsknetw-lib");
    }*/

    public static native long GSKRM_Initialization(String addr,int port);

    public static native int GSKRM_Connnect(long client);

    public static native int GSKRM_CloseConnect(long client);

    public static native int GSKRM_GetConnectState(long client);

    public static native String GSKRM_GetCNCTypeName(long client);


    public static native  byte[]  GSKRM_GetVersionInfo(long client);

    public static native  byte[]  GSKRM_GetRunInfo(long client);
    public static native  byte[]  GSKRM_GetAxisInfo(long client);
    public static native  byte[]  GSKRM_GetAlarmInfo(long client );
    public static native  byte[]  GSKRM_GetBeihangInfo(long client);

    public static native  int   GSKRM_FreeObj(long client);

}
