package com.cnc.encryption.aes;

/**
 * AES���ݼ��ܽ��ܺ���
 */
public class AESEncryptApi {


    /**
     * ���ݼ��ܺ���
     * @param srcData ��������
     * @return ���ܺ������
     */
    public static byte[] encryptionFuntion(byte[] srcData){
        int n=srcData.length;
        byte[] encryptedData=null;

        int[] srcDataint=ByteDealUtil.ByteArraytoIntArray(srcData);
        int[] encryptedDataint = encryptIntArray(srcDataint);
        encryptedData =ByteDealUtil.IntArraytoByteArray(encryptedDataint);

        return encryptedData;

    }

    /**
     * ���ݽ��ܺ���
     * @param encryptedData ��������
     * @return   ���ܺ����������
     */
    public static byte[] verEncryptionFuntion(byte[] encryptedData){
        byte[]  srcData=null;
        int[] encryptedDataint =ByteDealUtil.ByteArraytoIntArray(encryptedData);
        int[] srcDataint = verEncryptIntArray(encryptedDataint);
        srcData= ByteDealUtil.IntArraytoByteArray(srcDataint);

        return srcData;
    }


    /*
     * ����һ�����ȵ��ֽ����飬����δ֪
     */
    private static int[] encryptIntArray(int [] toencry){
        int []  tmp16block_toencry=null; //��Ҫ�����ܵĿ�
        int []  tmp16block_hadencry=null;//�ѱ����ܵĿ�
        int i,offset;
        int len=toencry.length; //��ȡ����
        int  count=len/16;  //�����
        if(len%16 != 0){
            count+=1;   //�����鲹��
        }
        int hadencry[] =new int[count*16];//������ܺ������

        //ѭ�������м���
        for(i=0;i<count;i++){
            offset=i*16; //ƫ��
            tmp16block_toencry=ByteDealUtil.getbyteblock(toencry, offset, 16); //ȡ16�ֽڵĿ�
            tmp16block_hadencry=KeyEncryUtil.cipher(tmp16block_toencry); //����
            ByteDealUtil.setbyteblock(hadencry, tmp16block_hadencry, offset);//�����
        }
        return hadencry; //���ؼ��ܺ������
    }

    /*
     * ����һ�����ȵ��ֽ����飬����δ֪
     */
    private static int[] verEncryptIntArray(int [] to_ver_encry){
        int []  tmp16block_to_ver_encry=null; //��Ҫ�����ܵĿ�
        int []  tmp16block_had_ver_encry=null;//�ѱ����ܵĿ�
        int i,offset;
        int len=to_ver_encry.length; //��ȡ����
        int  count=len/16;  //�����
        if(len%16 != 0){
            count+=1;   //�����鲹��
        }
        int had_ver_encry[] =new int[count*16];//������ܺ������
        //ѭ�������м���
        for(i=0;i<count;i++){
            offset=i*16; //ƫ��
            tmp16block_to_ver_encry=ByteDealUtil.getbyteblock(to_ver_encry, offset, 16); //ȡ16�ֽڵĿ�
            tmp16block_had_ver_encry=KeyEncryUtil.inv_cipher(tmp16block_to_ver_encry); //����
            ByteDealUtil.setbyteblock(had_ver_encry, tmp16block_had_ver_encry, offset);//�����
        }

        return had_ver_encry; //���ؽ��ܺ������
    }
}
