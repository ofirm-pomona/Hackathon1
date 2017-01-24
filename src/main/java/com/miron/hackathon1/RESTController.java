package com.miron.hackathon1;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class RESTController {

    @CrossOrigin
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    List<S3Object> list() {
        List<S3Object> s3Objects = new ArrayList<>();

        AWSCredentials credentials = new BasicAWSCredentials("XXX", "XXX");
        ClientConfiguration clientConfig = new ClientConfiguration();
        AmazonS3 s3client = new AmazonS3Client(credentials, clientConfig);

        final ListObjectsV2Request req = new ListObjectsV2Request().withBucketName("hackathon1-cs499");
        ListObjectsV2Result result;
        do {
            result = s3client.listObjectsV2(req);

            for (S3ObjectSummary objectSummary :
                    result.getObjectSummaries()) {

                S3Object s3Object = new S3Object();
                s3Object.Key = objectSummary.getKey();
                s3Object.LastModified = objectSummary.getLastModified();
                s3Object.ETag = objectSummary.getETag();
                s3Object.Size = objectSummary.getSize();
                s3Object.StorageClass = objectSummary.getStorageClass();

                if (objectSummary.getOwner() != null){
                    if (objectSummary.getOwner().getDisplayName() != null)
                        s3Object.Owner.DisplayName = objectSummary.getOwner().getDisplayName();
                    if (objectSummary.getOwner().getId() != null)
                        s3Object.Owner.ID = objectSummary.getOwner().getId();
                }

                s3Object.Url = "https://s3-us-west-1.amazonaws.com/hackathon1-cs499/" + objectSummary.getKey();

                s3Objects.add(s3Object);
            }
            System.out.println("Next Continuation Token : " + result.getNextContinuationToken());
            req.setContinuationToken(result.getNextContinuationToken());
        } while(result.isTruncated() );

        return s3Objects;
    }
}
