package com.wesine.device_sdk.utils;

import android.os.Environment;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.Date;

/**
 * Created by doug on 18-3-18.
 */

public class FileUtils {
    /**
     * 删除文件，可以是文件或文件夹
     *
     * @param fileName 要删除的文件名
     * @return 删除成功返回true，否则返回false
     */
    public static boolean delete(String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            Logger.d("删除文件失败: %s 不存在！", fileName);
            return false;
        } else {
            if (file.isFile())
                return deleteFile(fileName);
            else
                return deleteDirectory(fileName);
        }
    }

    /**
     * 删除单个文件
     *
     * @param fileName 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String fileName) {
        File file = new File(fileName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                Logger.d("删除单个文件: %s 成功！", fileName);
                return true;
            } else {
                Logger.d("删除单个文件: %s 失败！", fileName);
                return false;
            }
        } else {
            Logger.d("删除单个文件失败: %s 不存在！", fileName);
            return false;
        }
    }

    /**
     * 删除目录及目录下的文件
     *
     * @param dir 要删除的目录的文件路径
     * @return 目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String dir) {
        // 如果dir不以文件分隔符结尾，自动添加文件分隔符
        if (!dir.endsWith(File.separator))
            dir = dir + File.separator;
        File dirFile = new File(dir);
        // 如果dir对应的文件不存在，或者不是一个目录，则退出
        if ((!dirFile.exists()) || (!dirFile.isDirectory())) {
            Logger.d("删除目录失败: %s 不存在！", dir);
            return false;
        }
        boolean flag = true;
        // 删除文件夹中的所有文件包括子目录
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // 删除子文件
            if (files[i].isFile()) {
                flag = FileUtils.deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
            // 删除子目录
            else if (files[i].isDirectory()) {
                flag = FileUtils.deleteDirectory(files[i]
                        .getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag) {
            Logger.d("删除目录失败！");
            return false;
        }
        // 删除当前目录
        if (dirFile.delete()) {
            Logger.d("删除目录 %s 成功！", dir);
            return true;
        } else {
            return false;
        }
    }

    public static void deleteVideoFile(int count) {
        Date date = TimeUtil.getDate(count);
        File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File[] files = dir.listFiles();
        if (files.length > 0) {
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                String name = file.getName();
                if (name.compareTo(TimeUtil.getDateString(date)) <= 0) {
                    FileUtils.deleteDirectory(file.getAbsolutePath());
                }
            }
        }
    }

    public static void main(String[] args) {
        deleteVideoFile(-7);
//  // 删除单个文件
//  String file = "c:/test/test.txt";
//  DeleteFileUtil.deleteFile(file);
//  System.out.println();
        // 删除一个目录
        String dir = "D:/home/web/upload/upload/files";
        FileUtils.deleteDirectory(dir);
//  System.out.println();
//  // 删除文件
//  dir = "c:/test/test0";
//  DeleteFileUtil.delete(dir);

    }
}
