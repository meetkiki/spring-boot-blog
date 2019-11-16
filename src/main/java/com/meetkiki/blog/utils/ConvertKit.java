//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.meetkiki.blog.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

public final class ConvertKit {
    private static final char[] HEX_DIGITS = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

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

                return new String(ret);
            }
        }
    }

    public static byte[] hexString2Bytes(String hexString) {
        if (isSpace(hexString)) {
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
                ret[i >> 1] = (byte)(hex2Dec(hexBytes[i]) << 4 | hex2Dec(hexBytes[i + 1]));
            }

            return ret;
        }
    }

    public static int hex2Dec(char hexChar) {
        if (hexChar >= '0' && hexChar <= '9') {
            return hexChar - 48;
        } else if (hexChar >= 'A' && hexChar <= 'F') {
            return hexChar - 65 + 10;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static byte[] chars2Bytes(char[] chars) {
        if (chars != null && chars.length > 0) {
            int len = chars.length;
            byte[] bytes = new byte[len];

            for(int i = 0; i < len; ++i) {
                bytes[i] = (byte)chars[i];
            }

            return bytes;
        } else {
            return null;
        }
    }

    public static char[] bytes2Chars(byte[] bytes) {
        if (bytes == null) {
            return null;
        } else {
            int len = bytes.length;
            if (len <= 0) {
                return null;
            } else {
                char[] chars = new char[len];

                for(int i = 0; i < len; ++i) {
                    chars[i] = (char)(bytes[i] & 255);
                }

                return chars;
            }
        }
    }

    public static long memorySize2Byte(long memorySize, int unit) {
        return memorySize < 0L ? -1L : memorySize * (long)unit;
    }

    public static double byte2MemorySize(long byteNum, int unit) {
        return byteNum < 0L ? -1.0D : (double)byteNum / (double)unit;
    }

    public static String byte2FitMemorySize(long byteNum) {
        if (byteNum < 0L) {
            return "shouldn't be less than zero!";
        } else if (byteNum < 1024L) {
            return String.format("%.3fB", (double)byteNum);
        } else if (byteNum < 1048576L) {
            return String.format("%.3fKB", (double)byteNum / 1024.0D);
        } else {
            return byteNum < 1073741824L ? String.format("%.3fMB", (double)byteNum / 1048576.0D) : String.format("%.3fGB", (double)byteNum / 1.073741824E9D);
        }
    }

    public static String byte2FitMemoryString(long byteNum) {
        if (byteNum < 0L) {
            return "shouldn't be less than zero!";
        } else if (byteNum < 1024L) {
            return String.format("%d B", byteNum);
        } else if (byteNum < 1048576L) {
            return String.format("%d KB", byteNum / 1024L);
        } else {
            return byteNum < 1073741824L ? String.format("%d MB", byteNum / 1048576L) : String.format("%d GB", byteNum / 1073741824L);
        }
    }

    public static String bytes2Bits(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        byte[] var2 = bytes;
        int var3 = bytes.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            byte aByte = var2[var4];

            for(int j = 7; j >= 0; --j) {
                sb.append((char)((aByte >> j & 1) == 0 ? '0' : '1'));
            }
        }

        return sb.toString();
    }

    public static byte[] bits2Bytes(String bits) {
        int lenMod = bits.length() % 8;
        int byteLen = bits.length() / 8;
        if (lenMod != 0) {
            for(int i = lenMod; i < 8; ++i) {
                bits = "0" + bits;
            }

            ++byteLen;
        }

        byte[] bytes = new byte[byteLen];

        for(int i = 0; i < byteLen; ++i) {
            for(int j = 0; j < 8; ++j) {
                bytes[i] = (byte)(bytes[i] << 1);
                bytes[i] = (byte)(bytes[i] | bits.charAt(i * 8 + j) - 48);
            }
        }

        return bytes;
    }

    public static ByteArrayOutputStream input2OutputStream(InputStream is) {
        if (is == null) {
            return null;
        } else {
            Object var2;
            try {
                ByteArrayOutputStream os = new ByteArrayOutputStream();
                byte[] b = new byte[1024];

                int len;
                while((len = is.read(b, 0, 1024)) != -1) {
                    os.write(b, 0, len);
                }

                ByteArrayOutputStream var4 = os;
                return var4;
            } catch (IOException var8) {
                var8.printStackTrace();
                var2 = null;
            } finally {
                IOUtils.closeQuietly(is);
            }

            return (ByteArrayOutputStream)var2;
        }
    }

    public static ByteArrayInputStream output2InputStream(OutputStream out) {
        return out == null ? null : new ByteArrayInputStream(((ByteArrayOutputStream)out).toByteArray());
    }

    public static byte[] inputStream2Bytes(InputStream is) {
        return is == null ? null : input2OutputStream(is).toByteArray();
    }

    public static InputStream bytes2InputStream(byte[] bytes) {
        return bytes != null && bytes.length > 0 ? new ByteArrayInputStream(bytes) : null;
    }

    public static byte[] outputStream2Bytes(OutputStream out) {
        return out == null ? null : ((ByteArrayOutputStream)out).toByteArray();
    }

    public static OutputStream bytes2OutputStream(byte[] bytes) {
        if (bytes != null && bytes.length > 0) {
            ByteArrayOutputStream os = null;

            Object var3;
            try {
                os = new ByteArrayOutputStream();
                os.write(bytes);
                ByteArrayOutputStream var2 = os;
                return var2;
            } catch (IOException var7) {
                var7.printStackTrace();
                var3 = null;
            } finally {
                IOUtils.closeQuietly(os);
            }

            return (OutputStream)var3;
        } else {
            return null;
        }
    }

    public static String inputStream2String(InputStream is, String charsetName) {
        if (is != null && !isSpace(charsetName)) {
            try {
                return new String(inputStream2Bytes(is), charsetName);
            } catch (UnsupportedEncodingException var3) {
                var3.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public static InputStream string2InputStream(String string, String charsetName) {
        if (string != null && !isSpace(charsetName)) {
            try {
                return new ByteArrayInputStream(string.getBytes(charsetName));
            } catch (UnsupportedEncodingException var3) {
                var3.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public static String outputStream2String(OutputStream out, String charsetName) {
        if (out != null && !isSpace(charsetName)) {
            try {
                return new String(outputStream2Bytes(out), charsetName);
            } catch (UnsupportedEncodingException var3) {
                var3.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    public static OutputStream string2OutputStream(String string, String charsetName) {
        if (string != null && !isSpace(charsetName)) {
            try {
                return bytes2OutputStream(string.getBytes(charsetName));
            } catch (UnsupportedEncodingException var3) {
                var3.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    private static boolean isSpace(String s) {
        if (s == null) {
            return true;
        } else {
            int i = 0;

            for(int len = s.length(); i < len; ++i) {
                if (!Character.isWhitespace(s.charAt(i))) {
                    return false;
                }
            }

            return true;
        }
    }

    private ConvertKit() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
