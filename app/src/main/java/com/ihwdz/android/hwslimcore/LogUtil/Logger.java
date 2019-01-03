package com.ihwdz.android.hwslimcore.LogUtil;

import android.util.Log;

import com.ihwdz.android.hwapp.utils.DateUtils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/02
 * desc :
 * version: 1.0
 * </pre>
 */
public class Logger {


    static private File logFile;
    static private boolean isInitialized;
    static boolean isInfoLoggerEnabled = false;
    static boolean isErrorLoggerEnabled = false;
    static boolean isDebugLoggerEnabled = false;

    public static void setInfoFlagEnable(Boolean enable) {
        isInfoLoggerEnabled = enable;
        if(enable && !isInitialized)
        {
            initialize();
        }
    }

    public static void setErrorFlagEnable(Boolean enable) {
        isErrorLoggerEnabled = enable;
        if(enable && !isInitialized)
        {
            initialize();
        }
    }

    public static void setDebugFlagEnable(Boolean enable) {
        isDebugLoggerEnabled = enable;
        if(enable && !isInitialized)
        {
            initialize();
        }
    }

    static final String APP_LOG_PATH = FileHelper.getLogFileRootPath();

    static public void initialize() {
        if(!isInitialized && (isDebugLoggerEnabled || isErrorLoggerEnabled || isInfoLoggerEnabled) ) {
            try {
                String today = DateUtils.getDateAsName();

                File logFolder = new File(APP_LOG_PATH);
                if (logFolder.exists()) {
                    String[] files = logFolder.list();
                    for (String filepath : files) {
                        File file = new File(logFolder, filepath);
                        if (file.exists()) {
                            //Delete all log files except today's.
                            if (!file.getName().contains(today)) {
                                file.delete();
                            }
                        }
                    }
                } else {
                    logFolder.mkdirs();
                }

                File logFile = new File(logFolder, "infoinputexpress_" + today + ".txt");
                Logger.logFile = logFile;

                if (!logFile.exists()) {
                    if (!logFile.createNewFile()) {
                        //Cannot create log file, why?
                        return;
                    }
                    logFile.setReadable(true, false);
                }

                appendLog("========================================\r\n");
                appendLog("   " + DateUtils.getDateTimeAsName() + "   -Start\n");
                appendLog("========================================\n");

                isInitialized = true;

            } catch (Exception e) {

            } finally {
                if (!isInitialized) {
                    if (logFile != null && logFile.exists()) {
                        logFile.delete();
                        logFile = null;
                    }
                }
            }
        }
    }

    static public void i(String tag, String msg) {
        if(isInfoLoggerEnabled) {
            if (isInitialized) {
                appendLog(tag, msg, "INFO");
            }
            Log.i(tag, msg);
        }
    }

    static public void d(String tag, String msg) {
        if(isDebugLoggerEnabled) {
            if (isInitialized) {
                appendLog(tag, msg, "DEBUG");
            }
        }
    }

    static public void e(String tag, String msg) {
        if(isErrorLoggerEnabled) {
            if (isInitialized) {
                appendLog(tag, msg, "ERROR");
            }
            Log.e(tag, msg);
        }
    }

    /**
     * Handy function to get a loggable stack trace from a Throwable
     * @param tr An exception to log
     */
    public static String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }

        // This is to reduce the amount of log spew that apps do in the non-error
        // condition of the network being unavailable.
        Throwable t = tr;
        //Do not ignore the UnknownHostException
        /*
        while (t != null) {
            if (t instanceof UnknownHostException) {
                return "";
            }
            t = t.getCause();
        }*/

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        pw.flush();
        return sw.toString();
    }

    static public void e(String tag, String msg, Throwable tr) {
        if(isErrorLoggerEnabled) {
            if (isInitialized) {
                appendLog(tag, msg + "\r\n" + getStackTraceString(tr), "ERROR");
            }
            Log.e(tag, msg, tr);
        }
    }

    static public void clearLogs() {
        if(logFile != null) {
            synchronized (logFile) {
                if (logFile != null && logFile.exists()) {
                    logFile.delete();
                    try {
                        logFile.createNewFile();
                    }catch (Exception e)
                    {
                        //What happened??
                    }
                }
            }
        }
    }

    static public File getFile() {
        return logFile;
    }

    static public void appendLog(String text) {
        if(logFile != null) {
            synchronized (logFile) {
                if (logFile != null && logFile.exists()) {
                    try {
                        //BufferedWriter for performance, true to set append to file flag
                        BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
                        buf.append(text);
                        buf.newLine();
                        buf.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    static public void appendLog(String tag, String msg, String type) {
        if(logFile != null) {
            StringBuilder sb = new StringBuilder();
            sb.append(DateUtils.getDateTimeAsName());
            sb.append("\t");
            sb.append(type);
            sb.append("\t");
            sb.append(tag);
            sb.append("\t");
            sb.append(msg);
            sb.append("\r\n");
            appendLog(sb.toString());
        }
    }

    static public String getLogText() {
        String str = "";
        try {
            InputStream is = new FileInputStream(logFile);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\r\n");
            }
            reader.close();
            str = sb.toString();

        }
        catch (Exception e)
        {

        }
        return str;
    }
}
