package com.miron.hackathon1;

import java.util.Date;

public class S3Object {

    public String Key;
    public Date LastModified;
    public String ETag;
    public long Size;
    public String StorageClass;
    public Owner Owner;
    public String Url;

    public class Owner{
        public String DisplayName;
        public String ID;
    }
}
