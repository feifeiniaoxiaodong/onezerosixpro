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

    //BytedealUtil  byteutil=new BytedealUtil(); //�ֽڴ�������
   // List<DataAlarmInfo> alarmarry = new ArrayList<>(160); //������Ϣ����


    //�����ص�byte�ֽ���ת��Ϊjava���ݴ浽DataVersion������
    //��ȡ�汾��Ϣ
    public static  DataVersion batoJavaver(byte[] verbyte){

        if(verbyte.length!=(32*8)){
            throw  new IllegalArgumentException("�汾��Ϣ�ֽ������ԣ�");
            //return null;
        }
        DataVersion daver=new DataVersion();
        String tmpsysver=BytedealUtil.getString(BytedealUtil.copyOfRange(verbyte,0,32)); //ϵͳ�汾
        String armver=BytedealUtil.getString( BytedealUtil.copyOfRange(verbyte,32*1,32));//Ӧ�ð汾
        String dspver=BytedealUtil.getString( BytedealUtil.copyOfRange(verbyte,32*2,32));//�岹�汾
        String FPGAver=BytedealUtil.getString( BytedealUtil.copyOfRange(verbyte,32*3,32));//λ�ذ汾
        String plcver=BytedealUtil.getString( BytedealUtil.copyOfRange(verbyte,32*4,32));//��ͼ�ļ���
        String hardver=BytedealUtil.getString( BytedealUtil.copyOfRange(verbyte,32*5,32));//Ӳ���汾
        String softnum=BytedealUtil.getString( BytedealUtil.copyOfRange(verbyte,32*6,32));//������
        String hardnum=BytedealUtil.getString( BytedealUtil.copyOfRange(verbyte,32*7,32));//Ӳ�����

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


/*    //�����������Ϣ�� List<DataAlarmInfo>
    public DataAlarmArray batoJavaalam(byte[] verbyte) {
        DataAlarmInfo daver = null; //������Ϣ����
        int  infocount=0; //alarm ��Ϣ������

        //�ֽ�����ǰ�ĸ��ֽڱ�ʾint�͵ı�����Ϣ������
        int leninfo=BytedealUtil.getInt(BytedealUtil.copyOfRange(verbyte,0,4));

        int length=verbyte.length-4;  //��Ϣ������Ϣ�ֽ�������
        if(length%96 ==0){
            infocount=length/96; //alarm ��Ϣ������
        }
        if(infocount!=leninfo){
            Log.i("ALAM","�ֽ����ݳ��Ȳ��ԣ�");
            return null ;
        }

        DataAlarmArray almarray=new DataAlarmArray(); //������Ϣ���ж���
        almarray.clear(); //��ն���
        for (int i=0;i<leninfo;i++) {
           daver = bytetoalamobj(BytedealUtil.copyOfRange(verbyte,4+i*96,96)); //�ֽ�������DataAlarmInfo����

            if (daver instanceof DataAlarmInfo) {
                almarray.addalam2array(daver,i);
            }else {
                Log.i("ALAM","daverΪ�գ�");
            }
        }
        return almarray;
    }*/

/*    //��һ��������Ϣ
    private  DataAlarmInfo  bytetoalamobj(byte [] dstbyte){

        if(dstbyte.length!=96){
            Log.i("ALAM","�ֽ����ݳ��Ȳ���!");
            return  null;
        }

        int index=BytedealUtil.getInt( BytedealUtil.copyOfRange(dstbyte,0,4));//������
        int axisNo=BytedealUtil.getInt(BytedealUtil.copyOfRange(dstbyte,4,4));//������Ż��վ��
        String errorNo=BytedealUtil.getString(BytedealUtil.copyOfRange(dstbyte,8,8));//������
        String errortime=BytedealUtil.getString(BytedealUtil.copyOfRange(dstbyte,16,16));//����ʱ��
        String errormsg=BytedealUtil.getString(BytedealUtil.copyOfRange(dstbyte,32,64));//������Ϣ

        DataAlarmInfo alamtmp=new DataAlarmInfo();  //������Ϣ����

        alamtmp.setIndex(index);
        alamtmp.setAxisNo(axisNo);
        alamtmp.setErrorNoStr(errorNo);
        alamtmp.setErrorTime(errortime);
        alamtmp.setErrorMessage(errormsg);

        return  alamtmp;

    }*/

    //��ȡ����Ϣ
    public static DataAxisInfo batoJavaaxis(byte[] axisba){

        if(axisba.length!= 280){
            Log.i("AxisTest","����Ϣ�ֽ������ȴ���");
            return  null;
        }
        int pos=0;
        double [] abs_coor=BytedealUtil.getDoubleArray(BytedealUtil.copyOfRange(axisba,pos,64));//��������
        pos+=64;
        double [] rel_coor=BytedealUtil.getDoubleArray(BytedealUtil.copyOfRange(axisba,pos,64)); //�������
        pos+=64;
        double [] mac_coor=BytedealUtil.getDoubleArray(BytedealUtil.copyOfRange(axisba,pos,64));//��������
        pos+=64;
        double [] addi_coor=BytedealUtil.getDoubleArray(BytedealUtil.copyOfRange(axisba,pos,64));//ʣ�����
        pos+=64;
        double  Frd=BytedealUtil.getDouble( BytedealUtil.copyOfRange(axisba,pos,8));   //Frd
        pos+=8;
        int     spd=BytedealUtil.getInt(BytedealUtil.copyOfRange(axisba,pos,4));//Spd
        pos+=4;
        int     F=BytedealUtil.getInt( BytedealUtil.copyOfRange(axisba,pos,4));//����
        pos+=4;
        int     S=BytedealUtil.getInt( BytedealUtil.copyOfRange(axisba,pos,4)); //ת��
        pos+=4;
        int     J=BytedealUtil.getInt( BytedealUtil.copyOfRange(axisba,pos,4)); //���ٱ���

        DataAxisInfo  axisobj=new DataAxisInfo(); //�½�DataAxisInfo����
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


    /*//��ȡ������Ϣ
    public  DataRunstatInfo batojavaruns(byte[] runba){

        if(runba.length!=196){
            Log.i("RunTest","������Ϣ�ֽ������ȴ���");
            return  null;
        }
        DataRunstatInfo runobj=new DataRunstatInfo(); //������Ϣ����
        int pos=0;
        char opmode=BytedealUtil.getAsciiChar( BytedealUtil.getOneByte(runba,pos));//����ģʽ
        pos+=1;
        char runmode=BytedealUtil.getAsciiChar( BytedealUtil.getOneByte(runba,pos));//����ģʽ
        pos+=1;
        char emergflag=BytedealUtil.getAsciiChar( BytedealUtil.getOneByte(runba,pos));//��ͣ�ź�����
        pos+=1;
        char curalamflag=BytedealUtil.getAsciiChar( BytedealUtil.getOneByte(runba,pos));//��ǰ�Ƿ��ڱ���״̬����������
        pos+=1;
        int curalanum=BytedealUtil.getUnsignedShort(BytedealUtil.copyOfRange(runba,pos,2));//��ǰ������
        pos+=2;
        int partcount=BytedealUtil.getUnsignedShort(BytedealUtil.copyOfRange(runba,pos,2));//�ӹ������
        pos+=2;
        String gmode=BytedealUtil.getString(BytedealUtil.copyOfRange(runba,pos,22));//��ǰG����ģ̬
        pos+=22;
        String nextmode=BytedealUtil.getString(BytedealUtil.copyOfRange(runba,pos,22));//�¶�G����ģ̬
        pos+=22;
        String svcurrent=BytedealUtil.getString(BytedealUtil.copyOfRange(runba,pos,8));//�����Ḻ��
        pos+=8;
        String spdcurrent=BytedealUtil.getString(BytedealUtil.copyOfRange(runba,pos,2));//���Ḻ��
        pos+=2;
        String progfilename=BytedealUtil.getString(BytedealUtil.copyOfRange(runba,pos,32));//��ǰ�ļ�
        pos+=32;
        pos+=2; //����int�������
        long run_time=BytedealUtil.getUnsignedInt(BytedealUtil.copyOfRange(runba,pos,4));  //����ʱ��
        pos+=4;
        long process_time=BytedealUtil.getUnsignedInt(BytedealUtil.copyOfRange(runba,pos,4));//�ӹ�ʱ��
        pos+=4;
        long cnc_time=    BytedealUtil.getUnsignedInt(BytedealUtil.copyOfRange(runba,pos,4));//ϵͳʱ��
        pos+=4;
        long blocknum=    BytedealUtil.getUnsignedInt(BytedealUtil.copyOfRange(runba,pos,4));//�к�
        pos+=4;
        int run_time_cnt=BytedealUtil.getInt(BytedealUtil.copyOfRange(runba,pos,4)); //�ۼ�����ʱ��
        pos+=4;
        int process_time_cnt=BytedealUtil.getInt(BytedealUtil.copyOfRange(runba,pos,4));//�ۼƼӹ�ʱ��
        pos+=4;
        int power_time_all=BytedealUtil.getInt(BytedealUtil.copyOfRange(runba,pos,4));//�ۼ��ܴ���
        pos+=4;
        int power_time_month=BytedealUtil.getInt(BytedealUtil.copyOfRange(runba,pos,4));//�ۼƱ����ܴ���
        pos+=4;
        char loginlevel=BytedealUtil.getAsciiChar(BytedealUtil.getOneByte(runba,pos));//��ǰ��¼�ȼ�
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
        Log.i("AxisTest","����Ϣ�ֽ������ȴ���");
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
    short almflag=BytedealUtil.getShort(BytedealUtil.copyOfRange(bhsample,pos,2)); pos+=2;//�쳣�ź�
    short reserved=BytedealUtil.getShort(BytedealUtil.copyOfRange(bhsample,pos,2));

//    dataBHSAMPLE_static.setCas(cas);
//    dataBHSAMPLE_static.setCcs(ccs);
    DataBHSAMPLE_STATIC  dataBHSAMPLE_static=new DataBHSAMPLE_STATIC(cas,ccs,aload,aspd,apst,
            cpst,load, progname,runstatus,almhead,almtime,alminfor,runtime,ontime,gclinenum,
            prognum,gcmode,almflag,reserved);

    return dataBHSAMPLE_static;
}

}
