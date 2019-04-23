package gitlet;

import java.io.Serializable;
import java.util.HashMap;

public class StagingArea implements Serializable {
    HashMap<String, String> addedFiles;

    StagingArea() {
        addedFiles = new HashMap<>();
    }
    void add(String fileName, String blobID) {
        addedFiles.put(fileName, blobID);
    }
    void clear() {
        addedFiles.clear();
    }
    void remove(String fileName) {
        addedFiles.remove(fileName);
    }
}

