package com.miron.hackathon1;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
public class RESTController {

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    ArrayList<S3Object> list() {
        ArrayList<S3Object> s3Objects = new ArrayList<S3Object>();

        AWSCredentials credentials = new BasicAWSCredentials("AKIAI4W42W4AAW7GAO3A", "HSc/9FnJRP1O2P5SSnQ1oqn7l32zTYEc2YkbGBLs");
        ClientConfiguration clientConfig = new ClientConfiguration();
        AmazonS3 s3client = new AmazonS3Client(credentials, clientConfig);

        final ListObjectsV2Request req = new ListObjectsV2Request().withBucketName("hackathon1-cs499").withMaxKeys(2);
        ListObjectsV2Result result;
        do {
            result = s3client.listObjectsV2(req);

            for (S3ObjectSummary objectSummary :
                    result.getObjectSummaries()) {

                S3Object s3Object = new S3Object(objectSummary.getKey(),
                        objectSummary.getLastModified(),
                        objectSummary.getETag(),
                        objectSummary.getSize(),
                        objectSummary.getStorageClass(),
                        "",
                        "",
                        "https://s3-us-west-1.amazonaws.com/hackathon1-cs499/" + objectSummary.getKey()
                );

                s3Objects.add(s3Object);

                System.out.println(" - " + objectSummary.getKey() + "  " +
                        "(size = " + objectSummary.getSize() +
                        ")");
            }
            System.out.println("Next Continuation Token : " + result.getNextContinuationToken());
            req.setContinuationToken(result.getNextContinuationToken());
        } while(result.isTruncated() );

        return s3Objects;
    }
}
