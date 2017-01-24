package com.miron.hackathon1;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;

import java.io.File;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.*;

public class LocalApplication {

    public void start() throws Exception {
        System.out.println("Local application started...");

        // Amazon SDK
        AWSCredentials credentials = new BasicAWSCredentials("AKIAI4W42W4AAW7GAO3A", "HSc/9FnJRP1O2P5SSnQ1oqn7l32zTYEc2YkbGBLs");
        ClientConfiguration clientConfig = new ClientConfiguration();
        AmazonS3 s3client = new AmazonS3Client(credentials, clientConfig);

        // Create new Dropbox folder
        String directoryPath = "C:\\Users\\OMiro\\Downloads\\Dropbox";
        File directory = new File(directoryPath);
        directory.mkdir();

        // Watch file changes
        WatchService watcher = FileSystems.getDefault().newWatchService();
        Path dir = Paths.get(directoryPath);
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
                    s3client.putObject("hackathon1-cs499", fileName.toString(), new File(directory + File.separator + fileName.toString()));
                }
                else if (kind == ENTRY_DELETE) {
                    s3client.deleteObject(new DeleteObjectRequest("hackathon1-cs499", fileName.toString()));
                }
                else if (kind == ENTRY_MODIFY) {
                    s3client.deleteObject(new DeleteObjectRequest("hackathon1-cs499", fileName.toString()));
                    s3client.putObject("hackathon1-cs499", fileName.toString(), new File(directory + File.separator + fileName.toString()));
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