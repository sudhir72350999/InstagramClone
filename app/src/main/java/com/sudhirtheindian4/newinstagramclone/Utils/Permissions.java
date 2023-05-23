package com.sudhirtheindian4.newinstagramclone.Utils;

import static java.util.jar.Manifest.*;

import java.util.jar.Manifest;

public class Permissions {

    public static final   String[] PERMISSIONS= {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA

    };

    public static final   String[] CAMERA_PERMISSIONS= {

            android.Manifest.permission.CAMERA

    };

    public static final   String[] WRITE_STORAGE_PERMISSIONS= {

            android.Manifest.permission.WRITE_EXTERNAL_STORAGE

    };
    public static final   String[] READ_STORAGE_PERMISSIONS= {

            android.Manifest.permission.READ_EXTERNAL_STORAGE

    };



}
