package com.miron.hackathon1;

import java.io.File;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.*;

public class LocalApplication {

    public static void main(String[] args) throws Exception {
        // Create new Dropbox folder
        new File("/home/ec2-user/dropbox").mkdir();

        // Watch file changes
        WatchService watcher = FileSystems.getDefault().newWatchService();
        Path dir = Paths.get("/home/ec2-user/dropbox");
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
                    System.out.println("Created");
                }
                else if (kind == ENTRY_DELETE) {
                    System.out.println("Deleted");
                }
                else if (kind == ENTRY_MODIFY) {
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