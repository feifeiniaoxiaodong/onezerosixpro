package com.cnc.gsk.datautils;

import android.util.Log;


import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.cnc.gsk.domain.DataAxisInfo;
import com.cnc.gsk.domain.DataBHSAMPLE_STATIC;
import com.cnc.gsk.domain.DataVersion;

/**
 * guang shu
 * parse data bytes to information object
 * Created by wei on 2017/4/17.
 */
public class BytetoJavaUtil {

    //BytedealUtil  byteutil=new BytedealUtil(); //字节处理工具类
   // List<DataAlarmInfo> alarmarry = new ArrayList<>(160); //报警信息队列


    //将读回的byte字节流转化为java数据存到DataVersion对象中
    //获取版本信息
    public static  DataVersion batoJavaver(byte[] verbyte){

        if(verbyte.length!=(32*8)){
            throw  new IllegalArgumentException("版本信息字节数不对！");
            //return null;
        }
        DataVersion daver=new DataVersion();
        String tmpsysver=BytedealUtil.getString(BytedealUtil.copyOfRange(verbyte,0,32)); //系统版本
        String armver=BytedealUtil.getString( BytedealUtil.copyOfRange(verbyte,32*1,32));//应用版本
        String dspver=BytedealUtil.getString( BytedealUtil.copyOfRange(verbyte,32*2,32));//插补版本
        String FPGAver=BytedealUtil.getString( BytedealUtil.copyOfRange(verbyte,32*3,32));//位控版本
        String plcver=BytedealUtil.getString( BytedealUtil.copyOfRange(verbyte,32*4,32));//梯图文件名
        String hardver=BytedealUtil.getString( BytedealUtil.copyOfRange(verbyte,32*5,32));//硬件版本
        String softnum=BytedealUtil.getString( BytedealUtil.copyOfRange(verbyte,32*6,32));//软件序号
        String hardnum=BytedealUtil.getString( BytedealUtil.copyOfRange(verbyte,32*7,32));//硬件序号

        daver.setSysVersion(tmpsysver);
        daver.setArmVersion(armver);
        daver.setDspVersion(dspver);
        daver.setFPGAVersion(FPGAver);
        daver.setPlcfileName(plcver);
        daver.setHardVersion(hardver);
        daver.setHardWareNumber(hardnum);
        daver.setSoftWareNumber(softnum);

        return daver;
    }


/*    //存多条报警信息到 List<DataAlarmInfo>
    public DataAlarmArray batoJavaalam(byte[] verbyte) {
        DataAlarmInfo daver = null; //报警信息对象
        int  infocount=0; //alarm 信息的条数

        //字节流的前四个字节表示int型的报警信息的条数
        int leninfo=BytedealUtil.getInt(BytedealUtil.copyOfRange(verbyte,0,4));

        int length=verbyte.length-4;  //信息报警信息字节流长度
        if(length%96 ==0){
            infocount=length/96; //alarm 信息的条数
        }
        if(infocount!=leninfo){
            Log.i("ALAM","字节数据长度不对！");
            return null ;
        }

        DataAlarmArray almarray=new DataAlarmArray(); //报警信息队列对象
        almarray.clear(); //清空队列
        for (int i=0;i<leninfo;i++) {
           daver = bytetoalamobj(BytedealUtil.copyOfRange(verbyte,4+i*96,96)); //字节流存入DataAlarmInfo对象

            if (daver instanceof DataAlarmInfo) {
                almarray.addalam2array(daver,i);
            }else {
                Log.i("ALAM","daver为空！");
            }
        }
        return almarray;
    }*/

/*    //存一条报警信息
    private  DataAlarmInfo  bytetoalamobj(byte [] dstbyte){

        if(dstbyte.length!=96){
            Log.i("ALAM","字节数据长度不对!");
            return  null;
        }

        int index=BytedealUtil.getInt( BytedealUtil.copyOfRange(dstbyte,0,4));//索引号
        int axisNo=BytedealUtil.getInt(BytedealUtil.copyOfRange(dstbyte,4,4));//报警轴号或从站号
        String errorNo=BytedealUtil.getString(BytedealUtil.copyOfRange(dstbyte,8,8));//报警号
        String errortime=BytedealUtil.getString(BytedealUtil.copyOfRange(dstbyte,16,16));//报警时间
        String errormsg=BytedealUtil.getString(BytedealUtil.copyOfRange(dstbyte,32,64));//报警信息

        DataAlarmInfo alamtmp=new DataAlarmInfo();  //报警信息对象

        alamtmp.setIndex(index);
        alamtmp.setAxisNo(axisNo);
        alamtmp.setErrorNoStr(errorNo);
        alamtmp.setErrorTime(errortime);
        alamtmp.setErrorMessage(errormsg);

        return  alamtmp;

    }*/

    //获取轴信息
    public static DataAxisInfo batoJavaaxis(byte[] axisba){

        if(axisba.length!= 280){
            Log.i("AxisTest","轴信息字节流长度错误！");
            return  null;
        }
        int pos=0;
        double [] abs_coor=BytedealUtil.getDoubleArray(BytedealUtil.copyOfRange(axisba,pos,64));//绝对坐标
        pos+=64;
        double [] rel_coor=BytedealUtil.getDoubleArray(BytedealUtil.copyOfRange(axisba,pos,64)); //相对坐标
        pos+=64;
        double [] mac_coor=BytedealUtil.getDoubleArray(BytedealUtil.copyOfRange(axisba,pos,64));//机床坐标
        pos+=64;
        double [] addi_coor=BytedealUtil.getDoubleArray(BytedealUtil.copyOfRange(axisba,pos,64));//剩余距离
        pos+=64;
        double  Frd=BytedealUtil.getDouble( BytedealUtil.copyOfRange(axisba,pos,8));   //Frd
        pos+=8;
        int     spd=BytedealUtil.getInt(BytedealUtil.copyOfRange(axisba,pos,4));//Spd
        pos+=4;
        int     F=BytedealUtil.getInt( BytedealUtil.copyOfRange(axisba,pos,4));//进给
        pos+=4;
        int     S=BytedealUtil.getInt( BytedealUtil.copyOfRange(axisba,pos,4)); //转速
        pos+=4;
        int     J=BytedealUtil.getInt( BytedealUtil.copyOfRange(axisba,pos,4)); //快速倍率

        DataAxisInfo  axisobj=new DataAxisInfo(); //新建DataAxisInfo对象
        axisobj.setAbs_Coord(abs_coor);
        axisobj.setRel_Coord(rel_coor);
        axisobj.setMac_Coord(mac_coor);
        axisobj.setAddi_Coord(addi_coor);
        axisobj.setSpd(spd);
        axisobj.setF(F);
        axisobj.setS(S);
        axisobj.setJ(J);

        return  axisobj;
    }


    /*//获取运行信息
    public  DataRunstatInfo batojavaruns(byte[] runba){

        if(runba.length!=196){
            Log.i("RunTest","运行信息字节流长度错误！");
            return  null;
        }
        DataRunstatInfo runobj=new DataRunstatInfo(); //运行信息对象
        int pos=0;
        char opmode=BytedealUtil.getAsciiChar( BytedealUtil.getOneByte(runba,pos));//操作模式
        pos+=1;
        char runmode=BytedealUtil.getAsciiChar( BytedealUtil.getOneByte(runba,pos));//运行模式
        pos+=1;
        char emergflag=BytedealUtil.getAsciiChar( BytedealUtil.getOneByte(runba,pos));//急停信号输入
        pos+=1;
        char curalamflag=BytedealUtil.getAsciiChar( BytedealUtil.getOneByte(runba,pos));//当前是否处于报警状态，报警个数
        pos+=1;
        int curalanum=BytedealUtil.getUnsignedShort(BytedealUtil.copyOfRange(runba,pos,2));//当前报警号
        pos+=2;
        int partcount=BytedealUtil.getUnsignedShort(BytedealUtil.copyOfRange(runba,pos,2));//加工零件数
        pos+=2;
        String gmode=BytedealUtil.getString(BytedealUtil.copyOfRange(runba,pos,22));//当前G代码模态
        pos+=22;
        String nextmode=BytedealUtil.getString(BytedealUtil.copyOfRange(runba,pos,22));//下段G代码模态
        pos+=22;
        String svcurrent=BytedealUtil.getString(BytedealUtil.copyOfRange(runba,pos,8));//进给轴负载
        pos+=8;
        String spdcurrent=BytedealUtil.getString(BytedealUtil.copyOfRange(runba,pos,2));//主轴负载
        pos+=2;
        String progfilename=BytedealUtil.getString(BytedealUtil.copyOfRange(runba,pos,32));//当前文件
        pos+=32;
        pos+=2; //满足int对齐规则
        long run_time=BytedealUtil.getUnsignedInt(BytedealUtil.copyOfRange(runba,pos,4));  //运行时间
        pos+=4;
        long process_time=BytedealUtil.getUnsignedInt(BytedealUtil.copyOfRange(runba,pos,4));//加工时间
        pos+=4;
        long cnc_time=    BytedealUtil.getUnsignedInt(BytedealUtil.copyOfRange(runba,pos,4));//系统时间
        pos+=4;
        long blocknum=    BytedealUtil.getUnsignedInt(BytedealUtil.copyOfRange(runba,pos,4));//行号
        pos+=4;
        int run_time_cnt=BytedealUtil.getInt(BytedealUtil.copyOfRange(runba,pos,4)); //累计运行时间
        pos+=4;
        int process_time_cnt=BytedealUtil.getInt(BytedealUtil.copyOfRange(runba,pos,4));//累计加工时间
        pos+=4;
        int power_time_all=BytedealUtil.getInt(BytedealUtil.copyOfRange(runba,pos,4));//累计总次数
        pos+=4;
        int power_time_month=BytedealUtil.getInt(BytedealUtil.copyOfRange(runba,pos,4));//累计本月总次数
        pos+=4;
        char loginlevel=BytedealUtil.getAsciiChar(BytedealUtil.getOneByte(runba,pos));//当前登录等级
        pos+=1;
        char opt_error=BytedealUtil.getAsciiChar(BytedealUtil.getOneByte(runba,pos));
        pos+=1;
        char custom_error=BytedealUtil.getAsciiChar(BytedealUtil.getOneByte(runba,pos));
        pos+=1;
        String reserver=BytedealUtil.getString(BytedealUtil.copyOfRange(runba,pos,64));

        runobj.setOpMode(opmode);
        runobj.setRunMode(runmode);
        runobj.setEmergFlag(emergflag);
        runobj.setCurrentalarmflag(curalamflag);
        runobj.setCurrentAlarmNum(curalanum);
        runobj.setPartCount(partcount);
        runobj.setGmode(gmode);
        runobj.setGmodeNext(nextmode);
        runobj.setSvCurrent(svcurrent);
        runobj.setSpdCurrent(spdcurrent);
        runobj.setProgfileName(progfilename);

        runobj.setRun_time(run_time);
        runobj.setProcess_time(process_time);
        runobj.setCNC_time(cnc_time);
        runobj.setBlockNumber(blocknum);
        runobj.setRun_time_cnt(run_time_cnt);
        runobj.setProcess_time_cnt(process_time_cnt);
        runobj.setPowerup_times_all(power_time_all);
        runobj.setPowerup_times_month(power_time_month);
        runobj.setLoginLevel(loginlevel);
        runobj.setOpt_error(opt_error);
        runobj.setCustom_error(custom_error);
        runobj.setReserver(reserver);

        return runobj;
    }*/


public static DataBHSAMPLE_STATIC batojavaDataBHSAMPLE_STATIC(byte[] bhsample){
    if(bhsample.length!= 2288){
        Log.i("AxisTest","轴信息字节流长度错误！");
        return  null;
    }
//    DataBHSAMPLE_STATIC  dataBHSAMPLE_static=new DataBHSAMPLE_STATIC();
    int pos=0;
    float cas=BytedealUtil.getFloat(BytedealUtil.copyOfRange(bhsample,pos,4));
    pos+=4;
    float ccs=BytedealUtil.getFloat(BytedealUtil.copyOfRange(bhsample,pos,4));
    pos+=4;
    float aload=BytedealUtil.getFloat(BytedealUtil.copyOfRange(bhsample,pos,4));
    pos+=4;
    float[] aspd =BytedealUtil.getFloatArray(BytedealUtil.copyOfRange(bhsample,pos,8*4));
    pos+=32;
    float[] apst =BytedealUtil.getFloatArray(BytedealUtil.copyOfRange(bhsample,pos,8*4));
    pos+=32;
    float[] cpst =BytedealUtil.getFloatArray(BytedealUtil.copyOfRange(bhsample,pos,8*4));
    pos+=32;
    float[] load =BytedealUtil.getFloatArray(BytedealUtil.copyOfRange(bhsample,pos,8*4));
    pos+=32;
    String progname=BytedealUtil.getString(BytedealUtil.copyOfRange(bhsample,pos,20));
    pos+=20;
    String runstatus=BytedealUtil.getString(BytedealUtil.copyOfRange(bhsample,pos,60));
    pos+=60;
    String almhead[]=new String[16];
    for(int i=0;i<16;i++){
        almhead[i]=BytedealUtil.getString(BytedealUtil.copyOfRange(bhsample,pos,20));
        pos+=20;
    }
    String almtime[]=new String[16];
    for(int i=0;i<16;i++){
        almtime[i]=BytedealUtil.getString(BytedealUtil.copyOfRange(bhsample,pos,38));
        pos+=38;
    }
    String alminfor[]=new String [16];
    for(int i=0;i<16;i++){
        alminfor[i]=BytedealUtil.getString(BytedealUtil.copyOfRange(bhsample,pos,70));
        pos+=70;
    }
    int runtime=BytedealUtil.getInt(BytedealUtil.copyOfRange(bhsample,pos,4)); pos+=4;
    int ontime =BytedealUtil.getInt(BytedealUtil.copyOfRange(bhsample,pos,4)); pos+=4;
    int gclinenum =BytedealUtil.getInt(BytedealUtil.copyOfRange(bhsample,pos,4)); pos+=4;

    short prognum=BytedealUtil.getShort(BytedealUtil.copyOfRange(bhsample,pos,2)); pos+=2;
    short gcmode=BytedealUtil.getShort(BytedealUtil.copyOfRange(bhsample,pos,2)); pos+=2;
    short almflag=BytedealUtil.getShort(BytedealUtil.copyOfRange(bhsample,pos,2)); pos+=2;//异常信号
    short reserved=BytedealUtil.getShort(BytedealUtil.copyOfRange(bhsample,pos,2));

//    dataBHSAMPLE_static.setCas(cas);
//    dataBHSAMPLE_static.setCcs(ccs);
    DataBHSAMPLE_STATIC  dataBHSAMPLE_static=new DataBHSAMPLE_STATIC(cas,ccs,aload,aspd,apst,
            cpst,load, progname,runstatus,almhead,almtime,alminfor,runtime,ontime,gclinenum,
            prognum,gcmode,almflag,reserved);

    return dataBHSAMPLE_static;
}

}
