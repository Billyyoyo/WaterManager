/*
 * Copyright (c) 2013, David Brodsky. All rights reserved.
 *
 *	This program is free software: you can redistribute it and/or modify
 *	it under the terms of the GNU General Public License as published by
 *	the Free Software Foundation, either version 3 of the License, or
 *	(at your option) any later version.
 *
 *	This program is distributed in the hope that it will be useful,
 *	but WITHOUT ANY WARRANTY; without even the implied warranty of
 *	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *	GNU General Public License for more details.
 *
 *	You should have received a copy of the GNU General Public License
 *	along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package cn.e_flo.managewatermeter.util;

import android.graphics.Bitmap;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @hide
 */
public class FileUtil {

    static final String TAG = "FileUtil";

    static final String OUTPUT_DIR = "HWEncodingExperiments";       // Directory relative to External or Internal (fallback) Storage

    public static String ROOT_DIR;

    public static String SDCARD_DIR;

    public static final String PICTURE_DIR = "picture";
    public static final String VIDEO_DIR = "video";
    public static final String AUDIO_DIR = "audio";
    public static final String FILE_DIR = "file";
    public static final String LOG_DIR = "log";
    public static final String HLS_CACHE = "cache";
    public static final String DOWNLOADS = "downloads";
    public static final String IMAGE_TMP = "TMP";

    /**
     * Returns a Java File initialized to a directory of given name
     * at the root storage location, with preference to external storage.
     * If the directory did not exist, it will be created at the conclusion of this call.
     * If a file with conflicting name exists, this method returns null;
     *
     * @param directory_name the name of the directory desired at the storage location
     * @return a File pointing to the storage directory, or null if a file with conflicting name
     * exists
     */
    public static File getRootStorageDirectory(String directory_name) {
        File result;
        // First, try getting access to the sdcard partition
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Log.d(TAG, "Using sdcard");
            result = new File(Environment.getExternalStorageDirectory(), directory_name);
        } else {
            // Else, use the internal storage directory for this application
            Log.d(TAG, "Using internal storage");
            result = new File(SDCARD_DIR, directory_name);
        }
        Log.d("getSDCard", Environment.getExternalStorageDirectory().getAbsolutePath() + " isExist " + Environment.getExternalStorageDirectory().exists());
        Log.d("getRootStorageDirectory", result.getAbsolutePath() + " isExist " + result.exists());
        return result;
    }

    /**
     * Returns a Java File initialized to a directory of given name
     * within the given location.
     *
     * @param parent_directory a File representing the directory in which the new child will reside
     * @return a File pointing to the desired directory, or null if a file with conflicting name
     * exists or if getRootStorageDirectory was not called first
     */
    public static File getStorageDirectory(File parent_directory, String new_child_directory_name) {

        File result = new File(parent_directory, new_child_directory_name);
        Log.d("getStorageDirectory", "directory ready: " + result.getAbsolutePath());
        return result;
    }

    public static File getRoot() {
        return getRootStorageDirectory(ROOT_DIR);
    }

    public static File getDirectory(String dirName) {
        File root = getRootStorageDirectory(ROOT_DIR);
        File dir = getStorageDirectory(root, dirName);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }

    public static File getFile(String dirName, String fileName) {
        File dir = getDirectory(dirName);
        return new File(dir, fileName);
    }

    /**
     * Returns a TempFile with given root, filename, and extension.
     * The resulting TempFile is safe for use with Android's MediaRecorder
     *
     * @param root
     * @param filename
     * @param extension
     * @return
     */
    public static File createTempFile(File root, String filename, String extension) {
        File output = null;
        try {
            if (filename != null) {
                if (!extension.contains("."))
                    extension = "." + extension;
                output = new File(root, filename + extension);
                output.createNewFile();
                //output = File.createTempFile(filename, extension, root);
                Log.i(TAG, "Created temp file: " + output.getAbsolutePath());
            }
            return output;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static File createTempFileInRootAppStorage(String filename) {
        File recordingDir = FileUtil.getRootStorageDirectory(OUTPUT_DIR);
        return createTempFile(recordingDir, filename.split("\\.")[0], filename.split("\\.")[1]);
    }

    public static String convertStreamToString(InputStream is) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        return sb.toString();
    }

    public static String getStringFromFile(String filePath) throws IOException {
        File fl = new File(filePath);
        FileInputStream fin = new FileInputStream(fl);
        String ret = convertStreamToString(fin);
        //Make sure you close all streams.
        fin.close();
        return ret;
    }

    public static void copy(File src, File dst) {
        try {
            InputStream in = new FileInputStream(src);
            OutputStream out = new FileOutputStream(dst);

            // Transfer bytes from in to out
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void copyAndReplace(File src, File dst, String tar, String replace) throws IOException {
        FileReader fr = null;
        FileWriter fw = null;
        BufferedReader br = null;
        StringBuffer sBuffer = new StringBuffer();
        try {
            try {
                fr = new FileReader(src);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            br = new BufferedReader(fr);// 建立BufferedReader对象，并实例化为br
            String Line = br.readLine();// 从文件读取一行字符串
            int i = 1;
            while (Line != null) {
                sBuffer.append(Line);
                sBuffer.append("\n");
                Line = br.readLine();// 从文件中继续读取一行数据
                i = i + 1;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null)
                    br.close();// 关闭BufferedReader对象
                if (fr != null)
                    fr.close();// 关闭文件
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        // 输出读取的结果
        writeStringToFile(sBuffer.toString().replaceAll(tar, replace), dst, false);
    }

    public static String readText(File file) {
        try {
            FileReader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = null;
            if ((line = bufferedReader.readLine()) != null) {    //#EXT-X-DISCONTINUITY
                return line;
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return null;
    }

    public static String readAllText(File file) {
        String encoding = "ISO-8859-1";
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        try {
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new String(filecontent, encoding);
        } catch (UnsupportedEncodingException e) {
            System.err.println("The OS does not support " + encoding);
            e.printStackTrace();
            return null;
        }

    }

    public static void writeStringToFile(String source, File dest, boolean append) {
        try {
            FileWriter writer = new FileWriter(dest, append);
            writer.write(source);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void download(String url, String path) throws Exception {
        File file = new File(path);
        URL myFileUrl = new URL(url);
        HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
        conn.setConnectTimeout(0);
        conn.setDoInput(true);
        conn.connect();
        InputStreamReader inRead = new InputStreamReader(conn.getInputStream());
        BufferedReader bufferedReader = new BufferedReader(inRead);
        String line = null;
        FileWriter writer = new FileWriter(file, true);
        while ((line = bufferedReader.readLine()) != null) {    //#EXT-X-DISCONTINUITY
            if (line.contains("ENDLIST")) {
                break;
            }
            writer.write(line + "\n");
        }
        writer.close();
        inRead.close();
        conn.disconnect();
    }

    public static void writeBytesToFile(byte[] bytes, File dest, boolean append) {
        try {
            FileOutputStream writer = new FileOutputStream(dest, append);
            writer.write(bytes);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean downloadBinary(String url, String path) {
        boolean ret = true;
        File file = new File(path);
        OutputStream out = null;
        InputStream in = null;
        try {
            URL myFileUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setConnectTimeout(0);
            conn.setDoInput(true);
            conn.connect();
            byte[] buffer = new byte[128];
            int iLength = 0;
            in = conn.getInputStream();
            out = new FileOutputStream(file);
            while ((iLength = in.read(buffer)) != -1) {
                out.write(buffer, 0, iLength);
            }
            out.flush();
            conn.disconnect();
        } catch (MalformedURLException mue) {
            mue.printStackTrace();
            ret = false;
        } catch (IOException ioe) {
            ioe.printStackTrace();
            ret = false;
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {

            }
        }
        return ret;
    }

    /**
     * Delete a directory and all its contents
     */
    public static void deleteDirectory(File fileOrDirectory) {
        deleteDirectory(fileOrDirectory, true);
    }


    public static void deleteDirectory(File fileOrDirectory, boolean delDir) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
                deleteDirectory(child);

        if (delDir) {
            fileOrDirectory.delete();
        }
    }

    public static Long saveFileFromInput(InputStream input, String path) {
        File file = null;
        OutputStream output = null;
        boolean state = false;
        try {
            file = createFile(path);
            output = new FileOutputStream(file, true);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = input.read(buffer)) > 0) {
                output.write(buffer, 0, len);
            }
            output.flush();
            state = true;
        } catch (Exception e) {
            e.printStackTrace();
            state = false;
        } finally {
            try {
                output.close();
                input.close();
                if (state) {
                    return -1l;
                } else {
                    FileInputStream fis = new FileInputStream(file);
                    long total = fis.available();
                    fis.close();
                    return total;
                }
            } catch (IOException e) {
                e.printStackTrace();
                file.deleteOnExit();
                return 0l;
            }
        }
    }

    public static File createFile(String path) {
        File file = createParent(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    private static File createParent(String path) {
        File file = new File(path);
        //寻找父目录是否存在
        File parent = new File(file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf(File.separator)));
        //如果父目录不存在，则递归寻找更上一层目录
        if (!parent.exists()) {
            createParent(parent.getPath());
            //创建父目录
            Log.i(TAG, "makedir : " + parent.mkdirs());
        }
        return file;
    }

    public static boolean saveBitmap(Bitmap bitmap, File file) {
        try {
            OutputStream outStream = new FileOutputStream(file);
            bitmap.compress((bitmap.hasAlpha()) ? Bitmap.CompressFormat.PNG : Bitmap.CompressFormat.JPEG, 100, outStream);
            outStream.flush();
            outStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static File getSystemPhotoDir() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);     //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);//获取跟目录
            return sdDir;
        } else {
            return null;
        }
    }

    public static String getEXT(String str) {
        if (!TextUtils.isEmpty(str) && str.contains(".")) {
            return str.substring(str.lastIndexOf("."));
        }
        return "";
    }

}