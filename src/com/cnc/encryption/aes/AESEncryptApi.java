package com.cnc.encryption.aes;

/**
 * AES数据加密解密函数
 */
public class AESEncryptApi {


    /**
     * 数据加密函数
     * @param srcData 明文数据
     * @return 加密后的数据
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
     * 数据解密函数
     * @param encryptedData 密文数据
     * @return   解密后的明文数据
     */
    public static byte[] verEncryptionFuntion(byte[] encryptedData){
        byte[]  srcData=null;
        int[] encryptedDataint =ByteDealUtil.ByteArraytoIntArray(encryptedData);
        int[] srcDataint = verEncryptIntArray(encryptedDataint);
        srcData= ByteDealUtil.IntArraytoByteArray(srcDataint);

        return srcData;
    }


    /*
     * 加密一定长度的字节数组，长度未知
     */
    private static int[] encryptIntArray(int [] toencry){
        int []  tmp16block_toencry=null; //将要被加密的块
        int []  tmp16block_hadencry=null;//已被加密的块
        int i,offset;
        int len=toencry.length; //获取长度
        int  count=len/16;  //块个数
        if(len%16 != 0){
            count+=1;   //非整块补齐
        }
        int hadencry[] =new int[count*16];//保存加密后的数据

        //循环逐块进行加密
        for(i=0;i<count;i++){
            offset=i*16; //偏移
            tmp16block_toencry=ByteDealUtil.getbyteblock(toencry, offset, 16); //取16字节的块
            tmp16block_hadencry=KeyEncryUtil.cipher(tmp16block_toencry); //加密
            ByteDealUtil.setbyteblock(hadencry, tmp16block_hadencry, offset);//保存块
        }
        return hadencry; //返回加密后的数组
    }

    /*
     * 解密一定长度的字节数组，长度未知
     */
    private static int[] verEncryptIntArray(int [] to_ver_encry){
        int []  tmp16block_to_ver_encry=null; //将要被解密的块
        int []  tmp16block_had_ver_encry=null;//已被解密的块
        int i,offset;
        int len=to_ver_encry.length; //获取长度
        int  count=len/16;  //块个数
        if(len%16 != 0){
            count+=1;   //非整块补齐
        }
        int had_ver_encry[] =new int[count*16];//保存解密后的数据
        //循环逐块进行加密
        for(i=0;i<count;i++){
            offset=i*16; //偏移
            tmp16block_to_ver_encry=ByteDealUtil.getbyteblock(to_ver_encry, offset, 16); //取16字节的块
            tmp16block_had_ver_encry=KeyEncryUtil.inv_cipher(tmp16block_to_ver_encry); //解密
            ByteDealUtil.setbyteblock(had_ver_encry, tmp16block_had_ver_encry, offset);//保存块
        }

        return had_ver_encry; //返回解密后的数组
    }
}
