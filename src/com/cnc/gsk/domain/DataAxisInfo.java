package com.cnc.gsk.domain;

import android.util.Log;

/**
 * ��������Ϣ����
 * @author wei
 *
 */
public class DataAxisInfo {
	
	//int NET_AXIS_NUM=8;
    //String TAG="JNITest";
    public double []Abs_Coord =new double[Mcronum.NET_AXIS_NUM];//��������
    private double []Rel_Coord =new double[Mcronum.NET_AXIS_NUM];//�������
    private double[] Mac_Coord =new double[Mcronum.NET_AXIS_NUM];//��������
    private double[] Addi_Coord =new double[Mcronum.NET_AXIS_NUM];//ʣ�����
    private double Frd; //�����ٶ�
    private int    Spd; //����ת��

    private int F;   //��������
    private int S;  //����ת�ٱ���
    private int J;  //�ֶ�����

    public double[] getAbs_Coord() {
        return Abs_Coord;
    }

    public void setAbs_Coord(double[] abs_Coord) {
        Abs_Coord = abs_Coord;
    }

    public double[] getRel_Coord() {
        return Rel_Coord;
    }

    public void setRel_Coord(double[] rel_Coord) {
        Rel_Coord = rel_Coord;
    }

    public double[] getMac_Coord() {
        return Mac_Coord;
    }

    public void setMac_Coord(double[] mac_Coord) {
        Mac_Coord = mac_Coord;
    }

    public double[] getAddi_Coord() {
        return Addi_Coord;
    }

    public void setAddi_Coord(double[] addi_Coord) {
        Addi_Coord = addi_Coord;
    }

    public double getFrd() {
        return Frd;
    }

    public void setFrd(double frd) {
        Frd = frd;
    }

    public int getSpd() {
        return Spd;
    }

    public void setSpd(int spd) {
        Spd = spd;
    }

    public int getF() {
        return F;
    }

    public void setF(int f) {
        F = f;
    }

    public int getS() {
        return S;
    }

    public void setS(int s) {
        S = s;
    }

    public int getJ() {
        return J;
    }

    public void setJ(int j) {
        J = j;
    }

    public void loginfor(){

        Log.v("JNITest","�������꣺"+String.valueOf(this.Abs_Coord[0]) +";"+ String.valueOf(this.Abs_Coord[1]));
        Log.v("JNITest","�������꣺"+String.valueOf(this.Abs_Coord[2]) +";"+ String.valueOf(this.Abs_Coord[3]));
        Log.v("JNITest","�������꣺"+String.valueOf(this.Abs_Coord[4]) +";"+ String.valueOf(this.Abs_Coord[5]));
        Log.v("JNITest","�������꣺"+String.valueOf(this.Abs_Coord[6]) +";"+ String.valueOf(this.Abs_Coord[7]));

        Log.v("JNITest","������꣺"+String.valueOf(this.getRel_Coord()[0]) +";"+ String.valueOf(this.getRel_Coord()[1]));
        Log.v("JNITest","������꣺"+String.valueOf(this.getRel_Coord()[2]) +";"+ String.valueOf(this.getRel_Coord()[3]));
        Log.v("JNITest","������꣺"+String.valueOf(this.getRel_Coord()[4]) +";"+ String.valueOf(this.getRel_Coord()[5]));
        Log.v("JNITest","������꣺"+String.valueOf(this.getRel_Coord()[6]) +";"+ String.valueOf(this.getRel_Coord()[7]));

        Log.v("JNITest","�������꣺"+String.valueOf(this.getMac_Coord()[0]) +";"+ String.valueOf(this.getMac_Coord()[1]));
        Log.v("JNITest","�������꣺"+String.valueOf(this.getMac_Coord()[2]) +";"+ String.valueOf(this.getMac_Coord()[3]));
        Log.v("JNITest","�������꣺"+String.valueOf(this.getMac_Coord()[4]) +";"+ String.valueOf(this.getMac_Coord()[5]));
        Log.v("JNITest","�������꣺"+String.valueOf(this.getMac_Coord()[6]) +";"+ String.valueOf(this.getMac_Coord()[7]));

        Log.v("JNITest","ʣ����룺"+String.valueOf(this.getAddi_Coord()[0]) +";"+ String.valueOf(this.getAddi_Coord()[1]));
        Log.v("JNITest","ʣ����룺"+String.valueOf(this.getAddi_Coord()[2]) +";"+ String.valueOf(this.getAddi_Coord()[3]));
        Log.v("JNITest","ʣ����룺"+String.valueOf(this.getAddi_Coord()[4]) +";"+ String.valueOf(this.getAddi_Coord()[5]));
        Log.v("JNITest","ʣ����룺"+String.valueOf(this.getAddi_Coord()[6]) +";"+ String.valueOf(this.getAddi_Coord()[7]));

        Log.v("JNITest","Frd     : "+String.valueOf(this.getFrd()));
        Log.v("JNITest","Spd     :"+String.valueOf(this.getSpd()));
        Log.v("JNITest","����    ��"+String.valueOf(this.getF()));
        Log.v("JNITest","ת��    ��"+String.valueOf(this.getS()));
        Log.v("JNITest","���ٱ��ʣ�"+String.valueOf(this.getJ()));

    }

}
