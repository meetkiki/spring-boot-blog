//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.meetkiki.blog.utils;

import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public final class EncryptUtils {
    private static final char[] HEX_DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private static final int workload = 12;
    private static final String TripleDES_Transformation = "DESede/ECB/NoPadding";
    private static final String TripleDES_Algorithm = "DESede";
    private static String AES_Transformation = "AES/ECB/NoPadding";
    private static final String AES_Algorithm = "AES";
    public static String DES_Transformation = "DES/ECB/NoPadding";
    private static final String DES_Algorithm = "DES";

    public static String md5(String data) {
        return md5(data.getBytes());
    }

    public static String md5(String data, String salt) {
        return bytes2HexString(md5ToByte((data + salt).getBytes()));
    }

    public static String md5(byte[] data) {
        return bytes2HexString(md5ToByte(data));
    }

    public static String md5(byte[] data, byte[] salt) {
        if (data != null && salt != null) {
            byte[] dataSalt = new byte[data.length + salt.length];
            System.arraycopy(data, 0, dataSalt, 0, data.length);
            System.arraycopy(salt, 0, dataSalt, data.length, salt.length);
            return bytes2HexString(md5ToByte(dataSalt));
        } else {
            return null;
        }
    }

    static byte[] md5ToByte(byte[] data) {
        return hashTemplate(data, "MD5");
    }

    public static String md5File(String filePath) {
        File file = StringUtils.isEmpty(filePath) ? null : new File(filePath);
        return md5File(file);
    }

    public static byte[] md5FileToByte(String filePath) {
        File file = StringUtils.isEmpty(filePath) ? null : new File(filePath);
        return md5FileToByte(file);
    }

    public static String md5File(File file) {
        return bytes2HexString(md5FileToByte(file));
    }

    public static byte[] md5FileToByte(File file) {
        if (file == null) {
            return null;
        } else {
            FileInputStream fis = null;
            DigestInputStream digestInputStream = null;

            Object var4;
            try {
                fis = new FileInputStream(file);
                MessageDigest md = MessageDigest.getInstance("MD5");
                digestInputStream = new DigestInputStream(fis, md);
                byte[] buffer = new byte[262144];

                while(digestInputStream.read(buffer) > 0) {
                }

                md = digestInputStream.getMessageDigest();
                byte[] var5 = md.digest();
                return var5;
            } catch (IOException | NoSuchAlgorithmException var9) {
                var9.printStackTrace();
                var4 = null;
            } finally {
                IOUtils.closeQuietly(fis);
                IOUtils.closeQuietly(digestInputStream);
            }

            return (byte[])var4;
        }
    }

    public static String SHA1(String data) {
        return SHA1(data.getBytes());
    }

    public static String SHA1(byte[] data) {
        return bytes2HexString(SHA1ToByte(data));
    }

    public static byte[] SHA1ToByte(byte[] data) {
        return hashTemplate(data, "SHA1");
    }

    public static String SHA256(String data) {
        return SHA256(data.getBytes());
    }

    public static String SHA256(byte[] data) {
        return bytes2HexString(SHA256ToByte(data));
    }

    public static byte[] SHA256ToByte(byte[] data) {
        return hashTemplate(data, "SHA-256");
    }

    public static String SHA512(String data) {
        return SHA512(data.getBytes());
    }

    public static String SHA512(byte[] data) {
        return bytes2HexString(SHA512ToByte(data));
    }

    public static byte[] SHA512ToByte(byte[] data) {
        return hashTemplate(data, "SHA-512");
    }

    private static byte[] hashTemplate(byte[] data, String algorithm) {
        if (data != null && data.length > 0) {
            try {
                MessageDigest md = MessageDigest.getInstance(algorithm);
                md.update(data);
                return md.digest();
            } catch (NoSuchAlgorithmException var3) {
                var3.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public static String hmacMd5(String data, String key) {
        return hmacMd5(data.getBytes(), key.getBytes());
    }

    public static String hmacMd5(byte[] data, byte[] key) {
        return bytes2HexString(hmacMd5ToByte(data, key));
    }

    public static byte[] hmacMd5ToByte(byte[] data, byte[] key) {
        return hmacTemplate(data, key, "HmacMD5");
    }

    public static String hmacSHA1(String data, String key) {
        return hmacSHA1(data.getBytes(), key.getBytes());
    }

    public static String hmacSHA1(byte[] data, byte[] key) {
        return bytes2HexString(hmacSHA1ToByte(data, key));
    }

    public static byte[] hmacSHA1ToByte(byte[] data, byte[] key) {
        return hmacTemplate(data, key, "HmacSHA1");
    }

    public static String hmacSHA256(String data, String key) {
        return hmacSHA256(data.getBytes(), key.getBytes());
    }

    public static String hmacSHA256(byte[] data, byte[] key) {
        return bytes2HexString(hmacSHA256ToByte(data, key));
    }

    public static byte[] hmacSHA256ToByte(byte[] data, byte[] key) {
        return hmacTemplate(data, key, "HmacSHA256");
    }

    public static String hmacSHA512(String data, String key) {
        return hmacSHA512(data.getBytes(), key.getBytes());
    }

    public static String hmacSHA512(byte[] data, byte[] key) {
        return bytes2HexString(hmacSHA512ToByte(data, key));
    }

    public static byte[] hmacSHA512ToByte(byte[] data, byte[] key) {
        return hmacTemplate(data, key, "HmacSHA512");
    }

    private static byte[] hmacTemplate(byte[] data, byte[] key, String algorithm) {
        if (data != null && data.length != 0 && key != null && key.length != 0) {
            try {
                SecretKeySpec secretKey = new SecretKeySpec(key, algorithm);
                Mac mac = Mac.getInstance(algorithm);
                mac.init(secretKey);
                return mac.doFinal(data);
            } catch (NoSuchAlgorithmException | InvalidKeyException var5) {
                var5.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public static byte[] DES2Base64(byte[] data, byte[] key) {
        return Base64.getEncoder().encode(DES(data, key));
    }

    public static String DES2HexString(byte[] data, byte[] key) {
        return bytes2HexString(DES(data, key));
    }

    public static byte[] DES(byte[] data, byte[] key) {
        return desTemplate(data, key, "DES", DES_Transformation, true);
    }

    public static byte[] decryptBase64DES(byte[] data, byte[] key) {
        try {
            return decryptDES(Base64.getDecoder().decode(data), key);
        } catch (Exception var3) {
            return null;
        }
    }

    public static byte[] decryptHexStringDES(String data, byte[] key) {
        return decryptDES(hexString2Bytes(data), key);
    }

    public static byte[] decryptDES(byte[] data, byte[] key) {
        return desTemplate(data, key, "DES", DES_Transformation, false);
    }

    public static byte[] encrypt3DES2Base64(byte[] data, byte[] key) {
        try {
            return Base64.getEncoder().encode(encrypt3DES(data, key));
        } catch (Exception var3) {
            return null;
        }
    }

    public static String encrypt3DES2HexString(byte[] data, byte[] key) {
        return bytes2HexString(encrypt3DES(data, key));
    }

    public static byte[] encrypt3DES(byte[] data, byte[] key) {
        return desTemplate(data, key, "DESede", "DESede/ECB/NoPadding", true);
    }

    public static byte[] decryptBase64_3DES(byte[] data, byte[] key) {
        try {
            return decrypt3DES(Base64.getDecoder().decode(data), key);
        } catch (Exception var3) {
            return null;
        }
    }

    public static byte[] decryptHexString3DES(String data, byte[] key) {
        return decrypt3DES(hexString2Bytes(data), key);
    }

    public static byte[] decrypt3DES(byte[] data, byte[] key) {
        return desTemplate(data, key, "DESede", "DESede/ECB/NoPadding", false);
    }

    public static byte[] encryptAES2Base64(byte[] data, byte[] key) {
        try {
            return Base64.getEncoder().encode(encryptAES(data, key));
        } catch (Exception var3) {
            return null;
        }
    }

    public static String encryptAES2HexString(byte[] data, byte[] key) {
        return bytes2HexString(encryptAES(data, key));
    }

    public static byte[] encryptAES(byte[] data, byte[] key) {
        return desTemplate(data, key, "AES", AES_Transformation, true);
    }

    public static byte[] decryptBase64AES(byte[] data, byte[] key) {
        try {
            return decryptAES(Base64.getDecoder().decode(data), key);
        } catch (Exception var3) {
            return null;
        }
    }

    public static byte[] decryptHexStringAES(String data, byte[] key) {
        return decryptAES(hexString2Bytes(data), key);
    }

    public static byte[] decryptAES(byte[] data, byte[] key) {
        return desTemplate(data, key, "AES", AES_Transformation, false);
    }

    public static byte[] desTemplate(byte[] data, byte[] key, String algorithm, String transformation, boolean isEncrypt) {
        if (data != null && data.length != 0 && key != null && key.length != 0) {
            try {
                SecretKeySpec keySpec = new SecretKeySpec(key, algorithm);
                Cipher cipher = Cipher.getInstance(transformation);
                SecureRandom random = new SecureRandom();
                cipher.init(isEncrypt ? 1 : 2, keySpec, random);
                return cipher.doFinal(data);
            } catch (Throwable var8) {
                var8.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public static String bytes2HexString(byte[] bytes) {
        if (bytes == null) {
            return null;
        } else {
            int len = bytes.length;
            if (len <= 0) {
                return null;
            } else {
                char[] ret = new char[len << 1];
                int i = 0;

                for(int var4 = 0; i < len; ++i) {
                    ret[var4++] = HEX_DIGITS[bytes[i] >>> 4 & 15];
                    ret[var4++] = HEX_DIGITS[bytes[i] & 15];
                }

                return (new String(ret)).toLowerCase();
            }
        }
    }

    public static byte[] hexString2Bytes(String hexString) {
        if (StringUtils.isEmpty(hexString)) {
            return null;
        } else {
            int len = hexString.length();
            if (len % 2 != 0) {
                hexString = "0" + hexString;
                ++len;
            }

            char[] hexBytes = hexString.toUpperCase().toCharArray();
            byte[] ret = new byte[len >> 1];

            for(int i = 0; i < len; i += 2) {
                ret[i >> 1] = (byte)(ConvertKit.hex2Dec(hexBytes[i]) << 4 | ConvertKit.hex2Dec(hexBytes[i + 1]));
            }

            return ret;
        }
    }

    private EncryptUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
