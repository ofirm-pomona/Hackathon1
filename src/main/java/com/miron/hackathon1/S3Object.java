package com.miron.hackathon1;

import java.util.Date;

/**
 * Created by OMiro on 1/23/2017.
 */

public class S3Object {

    private String Key;
    private Date LastModified;
    private String ETag;
    private long Size;
    private String StorageClass;
    private Owner Owner;
    private String Url;

    public S3Object(String key, Date lastModified, String ETag, long size, String storageClass, String displayName, String ID, String url) {
        Key = key;
        LastModified = lastModified;
        this.ETag = ETag;
        Size = size;
        StorageClass = storageClass;
        Owner = new Owner(displayName, ID);
        Url = url;
    }

    private class Owner{
        private String DisplayName;
        private String ID;

        private Owner(String DisplayName, String ID){
            this.DisplayName = DisplayName;
            this.ID = ID;
        }
    }
}
