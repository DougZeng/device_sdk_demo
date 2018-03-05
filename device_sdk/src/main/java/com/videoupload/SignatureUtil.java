package com.videoupload;

import java.util.Random;

/**
 * Created by doug on 18-2-28.
 */

public class SignatureUtil {
    private static final String TAG = SignatureUtil.class.getSimpleName();

    private static SignatureUtil mSignatureUtil;

    private SignatureUtil() {

    }

    public static SignatureUtil getSignatureUtil() {
        if (mSignatureUtil == null) {
            synchronized (SignatureUtil.class) {
                mSignatureUtil = new SignatureUtil();
            }
        }

        return mSignatureUtil;
    }


    public String getSignature() {
        String signature = "";
        Signature sign = new Signature();
        sign.setSecretId(UploadConfig.SECRET_ID);//个人API密钥中的Secret Id
        sign.setSecretKey(UploadConfig.SECRET_KEY);//个人API密钥中的Secret Key
        sign.setCurrentTime(System.currentTimeMillis() / 1000);
        sign.setRandom(new Random().nextInt(Integer.MAX_VALUE));
        sign.setSignValidDuration(3600 * 24 * 30);

        try {
            signature = sign.getUploadSignature();
//            System.out.println("signature : " + signature);
        } catch (Exception e) {
            System.out.print("获取签名失败");
            e.printStackTrace();
        }
        return signature;
    }


}
