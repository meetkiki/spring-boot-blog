//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.meetkiki.blog.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.zip.GZIPOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class IOUtils {
    private static final Logger log = LoggerFactory.getLogger(IOUtils.class);

    public static void closeQuietly(Closeable closeable) {
        try {
            if (null == closeable) {
                return;
            }

            closeable.close();
        } catch (Exception var2) {
            log.error("Close closeable error", var2);
        }

    }

    public static String readToString(String file) throws IOException {
        return readToString(Paths.get(file));
    }

    public static String readToString(BufferedReader bufferedReader) {
        return (String)bufferedReader.lines().collect(Collectors.joining(System.lineSeparator()));
    }

    public static String readToString(Path path) throws IOException {
        BufferedReader bufferedReader = Files.newBufferedReader(path);
        return (String)bufferedReader.lines().collect(Collectors.joining(System.lineSeparator()));
    }

    public static String readToString(InputStream input) throws IOException {
        BufferedReader buffer = new BufferedReader(new InputStreamReader(input, "UTF-8"));
        Throwable var2 = null;

        String var3;
        try {
            var3 = (String)buffer.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (Throwable var12) {
            var2 = var12;
            throw var12;
        } finally {
            if (buffer != null) {
                if (var2 != null) {
                    try {
                        buffer.close();
                    } catch (Throwable var11) {
                        var2.addSuppressed(var11);
                    }
                } else {
                    buffer.close();
                }
            }

        }

        return var3;
    }

    public static void copyFile(File source, File dest) throws IOException {
        FileChannel in = (new FileInputStream(source)).getChannel();
        Throwable var3 = null;

        try {
            FileChannel out = (new FileOutputStream(dest)).getChannel();
            Throwable var5 = null;

            try {
                out.transferFrom(in, 0L, in.size());
            } catch (Throwable var28) {
                var5 = var28;
                throw var28;
            } finally {
                if (out != null) {
                    if (var5 != null) {
                        try {
                            out.close();
                        } catch (Throwable var27) {
                            var5.addSuppressed(var27);
                        }
                    } else {
                        out.close();
                    }
                }

            }
        } catch (Throwable var30) {
            var3 = var30;
            throw var30;
        } finally {
            if (in != null) {
                if (var3 != null) {
                    try {
                        in.close();
                    } catch (Throwable var26) {
                        var3.addSuppressed(var26);
                    }
                } else {
                    in.close();
                }
            }

        }

    }

    public static void compressGZIP(File input, File output) throws IOException {
        GZIPOutputStream out = new GZIPOutputStream(new FileOutputStream(output));
        Throwable var3 = null;

        try {
            FileInputStream in = new FileInputStream(input);
            Throwable var5 = null;

            try {
                byte[] buffer = new byte[1024];

                int len;
                while((len = in.read(buffer)) != -1) {
                    out.write(buffer, 0, len);
                }
            } catch (Throwable var29) {
                var5 = var29;
                throw var29;
            } finally {
                if (in != null) {
                    if (var5 != null) {
                        try {
                            in.close();
                        } catch (Throwable var28) {
                            var5.addSuppressed(var28);
                        }
                    } else {
                        in.close();
                    }
                }

            }
        } catch (Throwable var31) {
            var3 = var31;
            throw var31;
        } finally {
            if (out != null) {
                if (var3 != null) {
                    try {
                        out.close();
                    } catch (Throwable var27) {
                        var3.addSuppressed(var27);
                    }
                } else {
                    out.close();
                }
            }

        }

    }

    public static byte[] compressGZIPAsString(String content, Charset charset) throws IOException {
        if (content != null && content.length() != 0) {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            GZIPOutputStream gzip = new GZIPOutputStream(out);
            gzip.write(content.getBytes(charset));
            gzip.close();
            return out.toByteArray();
        } else {
            return null;
        }
    }

    private IOUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}
