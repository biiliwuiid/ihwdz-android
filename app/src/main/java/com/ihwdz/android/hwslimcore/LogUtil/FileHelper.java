package com.ihwdz.android.hwslimcore.LogUtil;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * <pre>
 * author : Duan
 * time : 2018/08/02
 * desc :
 * version: 1.0
 * </pre>
 */
public class FileHelper {


    static final String APP_FOLDER_NAME = "infoinputexpress";
    static final String HELD_FOLDER_NAME = "heldDocuments";

    final static String TAG = "FileHelper";
    public static boolean copyFile(String srcFilePath,String dstFilePath)
    {
        try {
            InputStream is =  new FileInputStream(srcFilePath);
            FileOutputStream fos = new FileOutputStream(dstFilePath);
            byte[] buffer = new byte[8192];
            int count = 0;

            while((count=is.read(buffer)) > 0)
            {
                fos.write(buffer, 0, count);
            }
            fos.close();
            is.close();
        } catch ( Exception e) {
            e.printStackTrace();
            return  false;
        }

        return true;
    }
    public static boolean moveFile(String srcFilePath,String dstFilePath)
    {
        File srcFile = new File(srcFilePath);
        File dstFile = new File(dstFilePath);
        boolean bSuccess = false;
        try {

            if(!srcFile.exists()) return  false;
            if(dstFile.exists())
            {
                dstFile.delete();
            }
            if(!dstFile.exists()) {
                dstFile.createNewFile();
            }
            bSuccess = srcFile.renameTo(dstFile);

        } catch ( Exception e) {
            Logger.e(TAG, "copyFile failed", e);
            return  false;
        }
        finally {
            if(bSuccess)
                srcFile.delete();
        }

        return true;
    }
    public static boolean copyContextResolvedFile(Context context, Uri uri, String dstFilePath)
    {
        try {
            InputStream is = context.getContentResolver().openInputStream(uri);
            if(is != null) {
                FileOutputStream fos = new FileOutputStream(dstFilePath);
                byte[] buffer = new byte[8192];
                int count = 0;

                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
            }else{
                return false;
            }
        } catch (Exception e) {
            Logger.e(TAG, "copyContextResolvedFile failed", e);
            return false;
        }

        return true;
    }

    public static boolean exportJpeg(Context cxt, String inputFilePath, String outputFilePath)
    {
        try {
            InputStream is = new FileInputStream(inputFilePath);

            try {
                FileOutputStream fos = new FileOutputStream(outputFilePath);
                byte[] buffer = new byte[8192];
                int count = 0;

                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
            } catch (Exception e) {
                Logger.e(TAG, "exportJpeg failed", e);
                return false;
            } finally {
                is.close();
            }

            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }
    public static boolean exportResource(Context cxt,int resId,String filePath)
    {
        InputStream is = cxt.getResources().openRawResource(resId);

        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            byte[] buffer = new byte[8192];
            int count = 0;

            while((count=is.read(buffer)) > 0)
            {
                fos.write(buffer, 0, count);
            }
            fos.close();
            is.close();
        } catch ( Exception e) {
            Logger.e(TAG, "exportResource failed", e);
            return  false;
        }

        return true;
    }

    public static String getDocumentRootPath(Context context)
    {
        String internalSDCardRootPath = VolumeSelector.getInstance(context).getInternalPath();
        if(internalSDCardRootPath!=null) {
            String path = internalSDCardRootPath + "/" + APP_FOLDER_NAME + "/data/.images";
            File file = new File(path);
            //String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString()+"/documents";
            //File file = new File(path);
            file.mkdirs();
            return path;
        }
        return null;
    }

    public static String getHeldDocumentRootPath(Context context)
    {
        //String path = context.getExternalCacheDir() + "/documents";
        String path = context.getFilesDir() + "/" + HELD_FOLDER_NAME;
        File file = new File(path);
        file.mkdirs();
        return path;
    }

    public static String getFilesDir(Context context)
    {
        //String path = context.getExternalCacheDir() + "/documents";
        String path = context.getFilesDir().getAbsolutePath();
        File file = new File(path);
        file.mkdirs();
        return path;
    }

    public static String getCacheFileRootPath(Context context)
    {
        File file = new File(context.getCacheDir().getAbsolutePath()+"/work-cache");
        file.mkdirs();
        return file.getAbsolutePath();
    }

    public static String getExternalAppPath()
    {
        String path = Environment.getExternalStorageDirectory().getPath() + "/" + APP_FOLDER_NAME + "/";
        File file = new File(path);
        file.mkdirs();
        return path;
    }

    public static String getCrashInfoPath()
    {
        String appPath = getExternalAppPath();
        File file = new File(appPath, "crash");
        file.mkdirs();
        return file.getAbsolutePath();
    }

    public static String getLogFileRootPath()
    {
        String appPath = getExternalAppPath();
        File file = new File(appPath, "log");
        file.mkdirs();
        return file.getAbsolutePath();
    }

    public static Map<String, ?> readSharedPreferences(Context context, String fileNameNoExt) {
        SharedPreferences preferences = context.getSharedPreferences(
                fileNameNoExt, Context.MODE_PRIVATE);
        return preferences.getAll();
    }


    public static void writeSharePreference(Context context, String fileNameNoExt, Map<String, ?> values) {
        try {
            SharedPreferences preferences = context.getSharedPreferences(fileNameNoExt, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            for (Iterator iterator = values.entrySet().iterator(); iterator.hasNext();)
            {
                Map.Entry<String, ?> entry = (Map.Entry<String, ?>) iterator.next();
                if(entry.getValue() == null)
                {
                    continue;
                }
                if (entry.getValue() instanceof String) {
                    editor.putString(entry.getKey(), (String) entry.getValue());
                } else if (entry.getValue() instanceof Boolean) {
                    editor.putBoolean(entry.getKey(), (Boolean) entry.getValue());
                } else if (entry.getValue() instanceof Float) {
                    editor.putFloat(entry.getKey(), (Float) entry.getValue());
                } else if (entry.getValue() instanceof Long) {
                    editor.putLong(entry.getKey(), (Long) entry.getValue());
                } else if(entry.getValue() instanceof Set)
                {
                    editor.putStringSet(entry.getKey(), (Set<String>)entry.getValue());
                }
                else  {
                    editor.putInt(entry.getKey(),(Integer) entry.getValue());
                }
            }
            editor.commit();
        } catch (Exception e) {
            Logger.e(TAG, "writeSharePreference failed", e);
        }
    }

    public static void updateFromMediaStore(Context context, String filePath)
    {
        //deleteFromMediaStore(context, filePath);
        //deleteFromMediaStore(context, filePath);
        MediaScannerConnection.scanFile(context,
                new String[]{filePath}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        // pass the mime type, else passing a null will enable file extension to dictate the mime type
                        // you are good to go

                    }
                });
    }

    public static void deleteFromMediaStore(Context context, String filePath)
    {
        try {
            //We need to remove the image from media store
            String[] projection = {MediaStore.Images.Media._ID};

            // Match on the file path\
            String selection = MediaStore.Images.Media.DATA + " = ?";
            String[] selectionArgs = new String[]{filePath};

            // Query for the ID of the media matching the file path
            Uri queryUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            ContentResolver contentResolver = context.getContentResolver();
            Cursor c = contentResolver.query(queryUri, projection, selection, selectionArgs, null);
            if (c.moveToFirst()) {
                // We found the ID. Deleting the item via the content provider will also remove the file
                long id = c.getLong(c.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
                Uri deleteUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
                contentResolver.delete(deleteUri, null, null);
            } else {
                // File not found in media store DB, do nothing
            }
            c.close();
        }catch (Exception e){
            Logger.e(TAG, "deleteFromMediaStore failed", e);
        }
    }

    public static  void clearCache(Context context)
    {
        deleteRecrusive(context.getCacheDir());
    }

    public static void deleteRecrusive(File fileOrDirectory)
    {

        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteRecrusive(child);

        fileOrDirectory.delete();

    }
    public static File makeBakFile(File srcFile)
    {
        File file = new File(srcFile.getAbsolutePath()+".bak");
        File dstFile = new File(file.getAbsolutePath());
        if(dstFile.exists()){
            dstFile.delete();
        }
        copyFile(srcFile.getAbsolutePath(),dstFile.getAbsolutePath());
        return dstFile;
    }
    public static File CopyFileToTempFolder(Context cxt,File srcFile)
    {
        File file = new File(cxt.getCacheDir().getAbsolutePath()+"/temp-cache");
        file.mkdirs();
        File dstFile = new File(file,srcFile.getName());
        if(dstFile.exists()){
            dstFile.delete();
        }
        copyFile(srcFile.getAbsolutePath(),dstFile.getAbsolutePath());
        return dstFile;
    }
    public static void CopyFileToExternalAppFolder(File srcFile)
    {
        File dstFile = new File(getExternalAppPath(),srcFile.getName());
        copyFile(srcFile.getAbsolutePath(),dstFile.getAbsolutePath());
    }

    public static boolean ShrinkBitmap(File img)
    {
        int width = 80;
        int height = 80;

        File tmpFile = new File(img.getAbsolutePath()+".thumnail");


        Bitmap bm = null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        bm = BitmapFactory.decodeFile(img.getAbsolutePath(), options);

        options.inJustDecodeBounds = false;
        int beWidth = options.outWidth/width;
        int beHeight = options.outHeight/height;
        int be = 1;
        if(beWidth < beHeight){
            be = beWidth;
        }else {
            be = beHeight;
        }
        if(be <= 1){
            be = 1;
        }

        options.inSampleSize = be;

        bm = BitmapFactory.decodeFile(img.getAbsolutePath(), options);
        bm = ThumbnailUtils.extractThumbnail(bm, width, height,
                ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        try {
            if(bm.compress(Bitmap.CompressFormat.PNG,100,new FileOutputStream(tmpFile,false)))
            {
                moveFile(tmpFile.getAbsolutePath(),img.getAbsolutePath());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return  true;
    }
}
