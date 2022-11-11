package com.example.miaofupos.utils;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.security.Key;
import java.util.Base64;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;

public class DESHelperUtil {
    /**
     * 偏移变量，固定占8位字节
     */
    private final static String IV_PARAMETER = "12345678";
    /**
     * 密钥算法
     */
    private static final String ALGORITHM = "DES";
    /**
     * 加密/解密算法-工作模式-填充模式
     */
    private static final String CIPHER_ALGORITHM = "DES/CBC/PKCS5Padding";
    /**
     * 默认编码
     */
    private static final String CHARSET = "UTF-8";

    /**
     * 生成key
     *
     * @param password
     * @return
     * @throws Exception
     */
    private static Key generateKey(String password) throws Exception {
        DESKeySpec dks = new DESKeySpec(password.getBytes(CHARSET));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        return keyFactory.generateSecret(dks);
    }

    /**
     * DES加密字符串
     *
     * @param password 加密密码，长度不能够小于8位
     * @param data     待加密字符串
     * @return 加密后内容
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String encrypt(String password, String data) {
        if (data == null)
            return null;
        try {
            Key secretKey = generateKey(password);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            IvParameterSpec iv = new IvParameterSpec(IV_PARAMETER.getBytes(CHARSET));
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
            byte[] bytes = cipher.doFinal(data.getBytes(CHARSET));

            //JDK1.8及以上可直接使用Base64，JDK1.7及以下可以使用BASE64Encoder
            //Android平台可以使用android.util.Base64
            return new String(Base64.getEncoder().encode(bytes));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * DES解密字符串
     *
     * @param password 解密密码，长度不能够小于8位
     * @param data     待解密字符串
     * @return 解密后内容
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String decrypt(String password, String data) {
        if (data == null)
            return null;
        try {
            Key secretKey = generateKey(password);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            IvParameterSpec iv = new IvParameterSpec(IV_PARAMETER.getBytes(CHARSET));
            cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
            return new String(cipher.doFinal(Base64.getDecoder().decode(data.getBytes(CHARSET))), CHARSET);
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获得加密数据的秘钥
     *
     * @return
     */
    public static String getSecretKey() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void main(String[] args) {
        String money = "123";
        // 加密
        for (int i = 0; i < 10; i++) {
            // 获得秘钥
            String secreKey = getSecretKey();
            // 获得加密的信息
            String secretBalance = encrypt(secreKey, money);
            System.out.println("secreKey=" + secreKey + ",secretBalance=" + secretBalance);
        }
        /**
         * secret_key=a2b0dcff3bf446ac842f3a530d779af4,secret_balance=hLtNQopwEtQ=
         * secret_key=091f33ac4ab94eca91b5a233fcd6e329,secret_balance=ijyLT87xPx8=
         * secret_key=816ccebe1a644c928e960334f96b0f71,secret_balance=SMdFNJiLVJQ=
         * secret_key=76019210d07d4952907dc8b278cd33c1,secret_balance=8XGnyAxqkS4=
         * secret_key=c6e8a05ba8d543ec80406bd0dc51e5ba,secret_balance=OTbx+45kBFA=
         * secret_key=ac425bf3e8ad473d9f283fcbcfd09eb0,secret_balance=bRfphqqJzf4=
         * secret_key=30bca9fa2c8f4e61ae78bc81f206243b,secret_balance=ttjRg8nj5zY=
         * secret_key=c255c7e453474c32948cc78052619788,secret_balance=UMfjsc8j2So=
         * secret_key=895f9d95fd9547a5b5109c6406d034b3,secret_balance=51SC/6WwU9w=
         * secret_key=cd5107e2fffd4b04983f07f0962689fe,secret_balance=eMIHpIaZlok=
         */
        // 解密
        String str1 = decrypt("a2b0dcff3bf446ac842f3a530d779af4", "hLtNQopwEtQ=");
        System.out.println("str1=" + str1);
        //System.out.println(UUID.randomUUID().toString().replace("-","").length());
    }
}
