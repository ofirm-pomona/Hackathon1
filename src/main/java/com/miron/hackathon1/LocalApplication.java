package com.miron.hackathon1;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.File;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.*;

public class LocalApplication {

    public static void main(String[] args) throws Exception {
        System.out.println("Local application started...");

        // Amazon SDK
        AWSCredentials credentials = new BasicAWSCredentials("XXX", "XXX");
        ClientConfiguration clientConfig = new ClientConfiguration();
        AmazonS3 s3client = new AmazonS3Client(credentials, clientConfig);

        // Create new Dropbox folder
        new File("C:\\Users\\OMiro\\Downloads\\Dropbox").mkdir();

        // Watch file changes
        WatchService watcher = FileSystems.getDefault().newWatchService();
        Path dir = Paths.get("C:\\Users\\OMiro\\Downloads\\Dropbox");
        dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);

        while (true) {
            WatchKey key;
            try {
                // wait for a key to be available
                key = watcher.take();
            } catch (InterruptedException ex) {
                return;
            }

            for (WatchEvent<?> event : key.pollEvents()) {
                // get event type
                WatchEvent.Kind<?> kind = event.kind();

                // get file name
                @SuppressWarnings("unchecked")
                WatchEvent<Path> ev = (WatchEvent<Path>) event;
                Path fileName = ev.context();

                System.out.println(kind.name() + ": " + fileName);

                if (kind == OVERFLOW) {
                    continue;
                }
                else if (kind == ENTRY_CREATE) {
                    File file = new File(fileName.toFile().getAbsolutePath());
                    System.out.println("File: " + file + ", exists? " + file.exists());
//                    s3client.putObject("hackathon1-cs499", fileName.toString(), new File(fileName.toFile().getAbsolutePath()));
                    System.out.println("Created");
                }
                else if (kind == ENTRY_DELETE) {
                    s3client.deleteObject(new DeleteObjectRequest("hackathon1-cs499", fileName.toString()));
                    System.out.println("Deleted");
                }
                else if (kind == ENTRY_MODIFY) {
//                    s3client.deleteObject(new DeleteObjectRequest("hackathon1-cs499", fileName.toString()));
                    s3client.putObject("hackathon1-cs499", fileName.toString(), new File(fileName.toFile().getAbsolutePath()));
                    System.out.println("Modified");
                }
            }

            // The key must be reset after processed
            boolean valid = key.reset();
            if (!valid) {
                break;
            }
        }
    }
}