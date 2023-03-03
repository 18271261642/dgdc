package com.jkcq.homebike.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileUtil {

    private static final String TAG = FileUtil.class.getSimpleName();

    /**
     * 根目录
     */
    private static String APP_ROOT_DIR = "com.xingye.project";

    /**
     * 日志目录
     */
    private static final String LOG_FILE = "log";

    /**
     * 图片目录
     */
    private static final String IMAGE_FILE = "image";

    /**
     * 图片缓存目录
     */
    private static final String IMAGE_CACHE_FILE = "imageCache";

    /**
     * 视频目录
     */
    public static final String VIDEO_DIR = "/bike/videodir/secen";
    public static final String VIDEO_FREE_DIR = "/bike/videodir/free";
    public static final String MUSIC_DIR = "/bike/videodir/music";
    public static final String VIDEO_SHARE = "/bike/share";
    public static final String VIDEO_COURSE_DIR = "/bike/videodir/course";
    public static final String VIDEO_TEMP_DIR = "/bike/videodir/temp";
    public static final String DEVICE_DIR = "/bike/device";

    /**
     * 网络请求缓存目录
     */
    private static final String NET_CACHE_FILE = "netCache";

    /**
     * 字体文件下载目录
     */
    private static final String FONTS_FILE = "fonts";

    /**
     * 语音缓存目录
     */
    private static final String VOICE_CACHE_FILE = "voice";

    private static String appPath;

    public static void initFile(Context context) {
        APP_ROOT_DIR = context.getPackageName();
        appPath = getRootDir(context);

        if (!isFileExists(appPath)) {
            createDir(appPath);
        }
    }

    /**
     * 获取手机根目录存储地址
     *
     * @param context
     * @return
     */
    public static String getRootDir(Context context) {
        if (isSDExists()) {
            return getSDPath();
        }
        return getAppPath(context);
    }

    /**
     * 获取手机SD卡根目录存储地址
     *
     * @return
     */
    public static String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);

        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();
        }
        return (null == sdDir) ? null : sdDir.getPath();
    }


    public static String getFreeDir(){
        return getSDPath() + VIDEO_FREE_DIR;
    }


    public static String getVideoDir() {
        return getSDPath() + VIDEO_DIR;
    }

    public static String getMusicDir() {
        return getSDPath() + MUSIC_DIR;
    }

    public static String getVideoCorseDir() {
        return getSDPath() + VIDEO_COURSE_DIR;
    }

    public static String getTempDir() {
        return getSDPath() + VIDEO_TEMP_DIR;
    }

    public static String getDeviceDir() {
        return getSDPath() + DEVICE_DIR;
    }

    /**
     * 获取手机内存根目录存储地址
     *
     * @param context 上下文对象
     * @return 目录
     */
    public static String getAppPath(Context context) {
        String fileDir = context.getFilesDir().getAbsolutePath();
        if (fileDir.lastIndexOf("/") > 0) {
            fileDir = fileDir.substring(0, fileDir.lastIndexOf("/"));
        }
        return fileDir;
    }

    /**
     * 获取缓存的总目录
     *
     * @return
     */
    public static File getCacheFile() {
        String path;
        if (isSDExists()) {
            path = getSDPath();
        } else {
            path = appPath;
        }
        if (null == path) {
            return null;
        }
        String logPath = path + File.separator + APP_ROOT_DIR;
        File file = new File(logPath);
        return file;
    }

    /**
     * 获取图片缓存路径
     *
     * @return 缓存路径File
     */
    public static File getImageCacheFile() {
        String path;
        if (isSDExists()) {
            path = getSDPath();
        } else {
            path = appPath;
        }
        if (null == path) {
            return null;
        }
        String logPath = path + File.separator + APP_ROOT_DIR + File.separator
                + IMAGE_CACHE_FILE;
        if (isFileExists(logPath)) {
            createDir(logPath);
        }

        File file = new File(logPath);
        return file;
    }

    /**
     * 获取网络请求缓存路径
     *
     * @return 缓存路径File
     */
    public static File getNetCacheFile() {
        String path;
        if (isSDExists()) {
            path = getSDPath();
        } else {
            path = appPath;
        }
        if (null == path) {
            return null;
        }
        String logPath = path + File.separator + APP_ROOT_DIR + File.separator
                + NET_CACHE_FILE;
        if (isFileExists(logPath)) {
            createDir(logPath);
        }

        File file = new File(logPath);
        return file;
    }

    public static File getImageFile(String name) {


        String logPath = getSDPath() + VIDEO_SHARE;
        String newPathName = logPath + File.separator + name;
        File newPath = new File(newPathName);
        if (!newPath.exists()) {
            createFile(newPathName);
        }
        File file = new File(newPathName);
        return file;
    }

    public static String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyyMMddHHmmss");
        return "share" + dateFormat.format(date) + ".png";
        // return "share" + ".png";
    }

    /**
     * 获取图片路径
     *
     * @return 缓存路径File
     */
    public static File getImageFile() {
        String path;
        if (isSDExists()) {
            path = getSDPath();
        } else {
            path = appPath;
        }
        if (null == path) {
            return null;
        }
        String logPath = path + File.separator + APP_ROOT_DIR + File.separator
                + IMAGE_FILE;
        if (isFileExists(logPath)) {
            createDir(logPath);
        }

        File file = new File(logPath);
        return file;
    }

    /**
     * 获取用户头像固定路径，目的是调用摄像头拍摄时将大图保存在指定位置
     *
     * @return
     */
    public static File getLogoImageFile() {
        File file = getImageFile();

        String logoPath = file.getAbsolutePath() + File.separator + "logo.png";
        File logoFile = new File(logoPath);
        if (!logoFile.exists()) {
            createFile(logoPath);
        }
        return logoFile;
    }

    /**
     * 获取字体文件路径
     *
     * @return 缓存路径File
     */
    public static File getFontsFile() {
        String path;
        if (isSDExists()) {
            path = getSDPath();
        } else {
            path = appPath;
        }
        if (null == path) {
            return null;
        }
        String logPath = path + File.separator + APP_ROOT_DIR + File.separator
                + FONTS_FILE;
        if (isFileExists(logPath)) {
            createDir(logPath);
        }

        File file = new File(logPath);
        return file;
    }

    /**
     * 获取语音缓存目录
     *
     * @return
     */
    public static File getVoiceFile() {
        String path;
        if (isSDExists()) {
            path = getSDPath();
        } else {
            path = appPath;
        }
        if (null == path) {
            return null;
        }
        String logPath = path + File.separator + APP_ROOT_DIR + File.separator
                + VOICE_CACHE_FILE;
        if (isFileExists(logPath)) {
            createDir(logPath);
        }

        File file = new File(logPath);
        return file;
    }

    /**
     * 获取文件夹下所有的文件大小 MB
     *
     * @param file
     * @return
     */
    public static double getDirSize(File file) {
        //判断文件是否存在
        if (file.exists()) {
            //如果是目录则递归计算其内容的总大小
            if (file.isDirectory()) {
                File[] children = file.listFiles();
                double size = 0;
                for (File f : children)
                    size += getDirSize(f);
                return size;
            } else {//如果是文件则直接返回其大小,以“兆”为单位
                double size = (double) file.length() / 1024 / 1024;
                return size;
            }
        } else {
            return 0.0;
        }
    }

    public static boolean isSDExists() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }

    public static String getCurrentLogPath() {
        String path;
        if (isSDExists()) {
            path = getSDPath();
        } else {
            path = appPath;
        }
        if (null == path) {
            return null;
        }
        String logPath = path + File.separator + APP_ROOT_DIR + File.separator
                + LOG_FILE;
        if (isFileExists(logPath)) {
            createDir(logPath);
        }
        return logPath;
    }

    /**
     * 判断文件是否存在
     *
     * @param filePath 文件地址
     * @return true 文件已经存在 false 文件不存在
     */
    public static boolean isFileExists(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return false;
        }
        File file = new File(filePath);
        if (null == file || !file.exists()) {
            return false;
        }
        return true;
    }

    /**
     * 创建文件/文件夹
     *
     * @param fullDir 完整地址
     * @return 创建好的文件
     */
    public static File createDir(String fullDir) {
        File file = new File(fullDir);

        boolean isSucceed;
        if (!file.exists()) {
            File parentDir = new File(file.getParent());
            if (!parentDir.exists()) {
                isSucceed = parentDir.mkdirs();
                if (!isSucceed) {
                }
            }
            try {
                isSucceed = file.createNewFile();
                if (!isSucceed) {
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * 创建文件
     *
     * @param filePath
     */
    public static boolean createFile(String filePath) {
        try {
            File file = new File(filePath);
           /* if(file.exists()){
                file.delete();
            }*/
            if (!file.exists()) {
                if (!file.getParentFile().exists()) {
                    file.getParentFile().mkdirs();
                }
                return file.createNewFile();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 删除文件
     *
     * @param filePath
     */
    public static void deleteFile(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return;
        }
        File file = new File(filePath);
        if (null == file || !file.exists()) {
            return;
        }
        file.delete();
    }

    public static void deleteFile(File file) {
        try {
            if (null == file || !file.exists()) {
                return;
            }
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     * If a deletion fails, the method stops attempting to
     * delete and returns "false".
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    /**
     * 判断文件名是否是yyyyMMdd的日期格式
     *
     * @param filename
     */
    public static boolean isFileNamedWithDate(String filename) {
        Pattern pattern = Pattern.compile("\\d{4}\\-{1}\\d{2}\\-{1}\\d{2}");
        Matcher matcher = pattern.matcher(filename);
        if (matcher.find()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 日志文件超过24小时删除
     *
     * @param filePath
     */
    public static void deleteFileByDate(String filePath) {
        File file = new File(filePath);
        if (!file.exists() || !file.isDirectory()) {
            return;
        }
//        String currentTime = TimeUtil.getTime();
//        File[] files = file.listFiles();
//        if (null == files) {
//            return;
//        }
//        for (File f : files) {
//            if (null == f) {
//                continue;
//            }
//            if (isFileNamedWithDate(f.getName())) {
//                String fileName = f.getName();
//                fileName = fileName.substring(0, fileName.lastIndexOf("."));
//                if (TimeUtil.compareDate(fileName, currentTime)) {
//                    deleteFile(f);
//                }
//            }
//        }
    }

    /**
     * 将字符保存在指定文件中
     *
     * @param filePath
     * @param buffer
     */
    public static void saveToFile(String filePath, StringBuffer buffer) {
        if (null == filePath || null == buffer || buffer.length() == 0) {
            return;
        }
        File crashFile = new File(filePath);
        if (!isFileExists(filePath)) {
            crashFile = createDir(filePath);
        }

        OutputStreamWriter out = null;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filePath);
            out = new OutputStreamWriter(fos, "UTF-8");
            out.write(buffer.toString());
            out.flush();
        } catch (IOException e) {
            if (crashFile.exists()) {
                deleteFile(crashFile);
            }
        } finally {
            try {
                if (null != fos) {
                    fos.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public static File writeImage(Bitmap bitmap, File f, int quality) {

        if (bitmap != null) {
            FileOutputStream fos;
            try {
                fos = new FileOutputStream(f);
                //bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                bitmap.compress(Bitmap.CompressFormat.PNG, 70, fos);
                fos.flush();
                fos.close();
            } catch (Exception e) {
                Log.e("writeImage", e.toString());
                //  e.printStackTrace();
            }
        }


        return f;
    }

    /*  public static void writeImage(Bitmap bitmap, String destPath, int quality) {
          try {
              FileUtil.deleteFile(destPath);
              if (FileUtil.createFile(destPath)) {
                  FileOutputStream out = new FileOutputStream(destPath);
                  bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                  try {
                      out.flush();
                      out.close();
                  } catch (IOException e) {
                      e.printStackTrace();
                  }
              }
          } catch (IOException e) {
              e.printStackTrace();
          }
      }*/

    public static boolean deletTempFile(String oldPathName) {
        File oldFile = new File(oldPathName);
        if (!oldFile.exists()) {
            Log.e("--Method--", "copyFile:  oldFile not exist.");
            return false;
        } else if (!oldFile.isFile()) {
            Log.e("--Method--", "copyFile:  oldFile not file.");
            return false;
        }
        return oldFile.delete();
    }

    public static boolean copyFile(String oldPathName, String newPathName) {
        try {
            File oldFile = new File(oldPathName);
            if (!oldFile.exists()) {
                Log.e("--Method--", "copyFile:  oldFile not exist.");
                return false;
            } else if (!oldFile.isFile()) {
                Log.e("--Method--", "copyFile:  oldFile not file.");
                return false;
            } else if (!oldFile.canRead()) {
                Log.e("--Method--", "copyFile:  oldFile cannot read.");
                return false;
            }


            File newPath = new File(newPathName);
            if (!newPath.exists()) {
                createFile(newPathName);
            }

            /* 如果不需要打log，可以使用下面的语句
            if (!oldFile.exists() || !oldFile.isFile() || !oldFile.canRead()) {
                return false;
            }
            */

            FileInputStream fileInputStream = new FileInputStream(oldPathName);
            FileOutputStream fileOutputStream = new FileOutputStream(newPathName);
            byte[] buffer = new byte[1024];
            int byteRead;
            while (-1 != (byteRead = fileInputStream.read(buffer))) {
                fileOutputStream.write(buffer, 0, byteRead);
            }
            fileInputStream.close();
            fileOutputStream.flush();
            fileOutputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}