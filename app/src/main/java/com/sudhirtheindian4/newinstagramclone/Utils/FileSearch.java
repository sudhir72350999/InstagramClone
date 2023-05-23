package com.sudhirtheindian4.newinstagramclone.Utils;


import android.app.ProgressDialog;

import java.io.File;
import java.util.ArrayList;


public class FileSearch {

    /**
     * Search a directory and return a list of all **directories** contained inside
     * @param directory
     * @return
     */
    public static ArrayList<String> getDirectoryPaths(String directory){
        ArrayList<String> pathArray = new ArrayList<>();
        File file = new File(directory);
        File[] listfiles = file.listFiles();

        for(int i = 0; i < listfiles.length; i++){
            if(listfiles[i].isDirectory()){
                pathArray.add(listfiles[i].getAbsolutePath());
            }
        }
        return pathArray;
    }

    /**
     * Search a directory and return a list of all **files** contained inside
     * @param directory
     * @return
     */
    public static ArrayList<String> getFilePaths(String directory){
        ArrayList<String> pathArray = new ArrayList<>();
        File file = new File(directory);
        File[] listfiles = file.listFiles();
        for(int i = 0; i < listfiles.length; i++){
            if(listfiles[i].isFile()){
                pathArray.add(listfiles[i].getAbsolutePath());
            }
        }
        return pathArray;
    }
}














//
//import java.io.File;
//import java.util.ArrayList;
//
//public class FileSearch {
//
//    /*
//    search  a directory and return  a list of all the directory*** contained inside
//     */
//    public static ArrayList<String> getDirectoryPath(String directory){
//        ArrayList<String> pathArray = new ArrayList<>();
//        File file = new File(directory);
//        File[] listFiles = file.listFiles();
//      for (int i =0; i<listFiles.length;i++){
//            if(listFiles[i].isDirectory()){
//                pathArray.add(listFiles[i].getAbsolutePath());
//            }
//        }
//        return pathArray;
//    }
//
//    /*
//   search  a directory and return  a list of all the files*** contained inside
//    */
//    public static ArrayList<String> getFilePath(String directory){
//        ArrayList<String> pathArray = new ArrayList<>();
//        File file = new File(directory);
//        File[] listFiles = file.listFiles();
//        for (int i =0; i<listFiles.length;i++){
//            if(listFiles[i].isFile()){
//                pathArray.add(listFiles[i].getAbsolutePath());
//            }
//        }
//        return pathArray;
//
//    }
//
//}
