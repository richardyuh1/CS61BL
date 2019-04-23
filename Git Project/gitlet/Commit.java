package gitlet;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

public class Commit implements Serializable {
    String parent; // The Commit ID of its parent ID.
    String commitMessage; // Message input by user for commit.
    HashMap<String, String> blobs; // Maps filenames to blob IDs.
    String timeStamp; // The time when the commit is made.

    Commit(String parent, String message, String timeStamp,
           StagingArea stagingArea, HashSet<String> untracked) {
        /* Instantiates new commit with parent PARENT, log message MESSAGE,
         * and time stamp TIMESTAMP. Updates blobs according to staging area.
         */
        commitMessage = message;
        this.parent = parent;
        this.blobs = new HashMap<>();
        this.timeStamp = timeStamp;
        if (parent != null) {
            Commit parentCommit = (Commit) GlobalState.loadObject(parent, "commits");
            if (parentCommit != null) {
                for (String key : parentCommit.blobs.keySet()) {
                    if (untracked.contains(key)) {
                        continue;
                    } else {
                        blobs.put(key, parentCommit.blobs.get(key));
                    }
                }
                blobs.putAll(stagingArea.addedFiles);
            }
        }
    }
}
