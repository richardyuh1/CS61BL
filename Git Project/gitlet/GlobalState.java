package gitlet;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class GlobalState implements Serializable {

    private HashMap<String, Branch> branches;
    private Branch HEAD;
    private StagingArea stagingArea;
    private HashSet<String> untracked;

    GlobalState() throws IOException {
        /* Generates new instance of GlobalState gitlet managing class.
         * Initializes head branch as new branch master pointing to
         * initial, empty commit.
         */
        branches = new HashMap<>();
        stagingArea = new StagingArea();
        DateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        Commit firstCommit = new Commit(null,
                "initial commit", dateformat.format(date), stagingArea, untracked);
        String commitID = Utils.sha1(firstCommit.commitMessage, firstCommit.timeStamp);
        File path = Utils.join(System.getProperty("user.dir"), ".gitlet", "commits", commitID);
        ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(path));
        out.writeObject(firstCommit);
        out.close();
        HEAD = new Branch("master", commitID);
        untracked = new HashSet<>();
        branches.put(HEAD.name, HEAD);
    }

    void log() {
        // Prints out all commits made in the current branch
        Commit currCommit;
        String currentID = HEAD.commitID;
        while (currentID != null) {
            currCommit = (Commit) loadObject(currentID, "commits");
            System.out.println("===");
            System.out.println("Commit " + currentID);
            System.out.println(currCommit.timeStamp);
            System.out.println(currCommit.commitMessage);
            System.out.println();
            currentID = currCommit.parent;
        }
    }

    void globalLog() {
        // Prints out all commits ever made
        List<String> allFiles = Utils.plainFilenamesIn(
                Utils.join(System.getProperty("user.dir"), ".gitlet", "commits"));
        for (String myFile : allFiles) {
            System.out.println("===");
            System.out.println("Commit " + myFile);
            Commit translator = (Commit) loadObject(myFile, "commits");
            System.out.println(translator.timeStamp);
            System.out.println(translator.commitMessage);
            System.out.println();
        }
    }

    void addBranch(String branchName) {
        /* Creates new branch with name BRANCHNAME pointing to current commit.
         * If pre-existing branch with name BRANCHNAME exists, instead prints
         * error message indicating this.
         */
        if (branches.containsKey(branchName)) {
            System.out.println("A branch with that name already exists.");
        } else {
            branches.put(branchName, new Branch(branchName, HEAD.commitID));
        }
    }

    void rmBranch(String branchName) {
        /* Deletes branch with BRANCHNAME from list of branches,
         * if it exists and isn't the current branch.
         */
        if (!branches.containsKey(branchName)) {
            System.out.println("A branch with that name does not exist.");
        } else if (HEAD.name.equals(branchName)) {
            System.out.println("Cannot remove the current branch.");
        } else {
            branches.remove(branchName);
        }
    }

    void addCommit(String logMessage) throws IOException {
        /* Generates a new commit with message LOGMESSAGE and parent equal to current commit.
         * Serializes this new commit and updates head branch commit pointer.
         * Clears staging area and list of files marked for removal upon completion.
         */
        if (stagingArea.addedFiles.isEmpty() && untracked.isEmpty()) {
            System.out.println("No changes added to the commit.");
        } else {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            String commitTime = dateFormat.format(date);
            Commit newCommit = new Commit(HEAD.commitID,
                    logMessage, commitTime, stagingArea, untracked);
            HashMap<String, String> blobs = newCommit.blobs;
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ObjectOutputStream objectStream = new ObjectOutputStream(stream);
            objectStream.writeObject(blobs);
            objectStream.close();
            byte[] blobsByte = stream.toByteArray();
            String commitID = Utils.sha1(HEAD.commitID, logMessage, blobsByte, commitTime);
            HEAD.commitID = commitID;
            File out = Utils.join(System.getProperty("user.dir"), ".gitlet", "commits", commitID);
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(out));
            output.writeObject(newCommit);
            output.close();
            stagingArea.clear();
            untracked.clear();
        }

    }

    void add(String fileName) throws IOException {
        /* Adds file FILENAME to staging area and (if applicable) removes its
         * untracked marker, if it exists.
         */
        File location = Utils.join(System.getProperty("user.dir"), fileName);
        if (location.exists() && !location.isDirectory()) {
            Blob blob = new Blob(fileName);
            String blobHash = Utils.sha1(fileName, blob.fileContent);
            Commit parent = (Commit) loadObject(HEAD.commitID, "commits");
            if (!parent.blobs.getOrDefault(fileName, "empty").equals(blobHash)) {
                File out = Utils.join(System.getProperty("user.dir"), ".gitlet", "blobs", blobHash);
                ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(out));
                output.writeObject(blob);
                output.close();
                stagingArea.add(fileName, blobHash);
            }
            untracked.remove(fileName);
        } else {
            System.out.println("File does not exist.");
        }
    }

    void find(String logMessage) {
        /* Iterates through commits in commit folder and prints IDs of commits
         * with message LOGMESSAGE.
         */
        boolean found = false;
        File commitsPath = Utils.join(System.getProperty("user.dir"), ".gitlet", "commits");
        List<String> list = Utils.plainFilenamesIn(commitsPath);
        if (list != null) {
            for (String s : list) {
                Commit obj = (Commit) loadObject(s, "commits");
                if (obj.commitMessage.equals(logMessage)) {
                    System.out.println(s);
                    found = true;
                }
            }
        }
        if (!found) {
            System.out.println("Found no commit with that message.");
        }

    }

    void status() {
        /* Prints out branches, staged files, and files marked for removal
         * in lexicographic order.
         */
        System.out.println("=== Branches ===");
        branches.keySet().stream().sorted().forEach(x -> {
            if (x.equals(HEAD.name)) {
                System.out.println("*" + x);
            } else {
                System.out.println(x);
            }
        });
        System.out.println();
        System.out.println("=== Staged Files ===");
        stagingArea.addedFiles.keySet().stream().sorted().forEach(System.out::println);
        System.out.println();
        System.out.println("=== Removed Files ===");
        untracked.stream().sorted().forEach(System.out::println);
        System.out.println();
        System.out.println("=== Modifications Not Staged For Commit ===");
        System.out.println();
        System.out.println("=== Untracked Files ===");
    }

    void remove(String fileName) {
        /* If file FILENAME is in the staging area, removes it. If file in current commit,
         * deletes it from working directory and marks it to be untracked.
         */
        Commit currentCommit = (Commit) GlobalState.loadObject(HEAD.commitID, "commits");
        if (!currentCommit.blobs.containsKey(fileName)
                && !stagingArea.addedFiles.containsKey(fileName)) {
            System.out.println("No reason to remove the file.");
        } else {
            if (currentCommit.blobs.containsKey(fileName)) {
                Utils.restrictedDelete(fileName);
                untracked.add(fileName);
            }
            if (stagingArea.addedFiles.containsKey(fileName)) {
                stagingArea.remove(fileName);
            }
        }
    }

    void checkout(String... args) {
        // Overwrites designated portion of repo according to arguments
        if (args.length == 2) {
            if (!args[0].equals("--")) {
                System.out.println("Incorrect operands.");
                return;
            }
            Commit headCommit = (Commit) loadObject(HEAD.commitID, "commits");
            if (!headCommit.blobs.containsKey(args[1])) {
                System.out.println("The given file does not exist in the previous commit.");
            } else {
                Blob blob = (Blob) loadObject(headCommit.blobs.get(args[1]), "blobs");
                File outFile = Utils.join(System.getProperty("user.dir"), args[1]);
                Utils.writeContents(outFile, blob.fileContent);
            }
        }

        if (args.length == 3) {
            if (!args[1].equals("--")) {
                System.out.println("Incorrect operands.");
            }
            if (args[0].length() < 40) {
                args[0] = findFullID(args[0]);
            }
            if (args[0] != null
                    && Utils.join(System.getProperty("user.dir"),
                    ".gitlet", "commits", args[0]).exists()) {
                Commit sourceCommit = (Commit) loadObject(args[0], "commits");
                if (!sourceCommit.blobs.containsKey(args[2])) {
                    System.out.println("File does not exist in that commit.");
                } else {
                    Blob blob = (Blob) loadObject(sourceCommit.blobs.get(args[2]), "blobs");
                    File outFile = Utils.join(System.getProperty("user.dir"), args[2]);
                    Utils.writeContents(outFile, blob.fileContent);
                }
                return;
            }
            System.out.println("No commit with that id exists.");
        }

        if (args.length == 1) {
            Branch sourceBranch = branches.get(args[0]);
            if (!branches.containsKey(args[0])) {
                System.out.println("No such branch exists.");
            } else if (args[0].equals(HEAD.name)) {
                System.out.println("No need to check out the current branch");
            } else if (untrackedCheck(HEAD.commitID, sourceBranch.commitID)) {
                System.out.println(
                        "There is an untracked file in the way; delete it or add it first.");
            } else {
                Commit sourceCommit = (Commit) loadObject(sourceBranch.commitID, "commits");
                Commit headCommit = (Commit) loadObject(HEAD.commitID, "commits");
                HashSet<String> allFiles = new HashSet<>();
                allFiles.addAll(headCommit.blobs.keySet());
                allFiles.addAll(sourceCommit.blobs.keySet());
                for (String fileName : allFiles) {
                    if (!sourceCommit.blobs.containsKey(fileName)) {
                        Utils.restrictedDelete(fileName);
                    } else {
                        Blob sourceBlob = (Blob) loadObject(
                                sourceCommit.blobs.get(fileName), "blobs");
                        File outFile = Utils.join(System.getProperty("user.dir"), fileName);
                        Utils.writeContents(outFile, sourceBlob.fileContent);
                    }
                }
                HEAD = sourceBranch;
                stagingArea.clear();
            }
        }
    }

    void reset(String commitID) {
        /* Checks out all files tracked by commit with id COMMITID.
         * Removes tracked files not present in given commit.
         * Updates HEAD variable to given commit. Clears staging area.
         */
        if (!Utils.join(System.getProperty("user.dir"), ".gitlet", "commits", commitID).exists()) {
            System.out.println("No commit with that id exists.");
        } else if (untrackedCheck(HEAD.commitID, commitID)) {
            System.out.println("There is an untracked file in the way; delete it or add it first.");
        } else {
            Commit givenCommit = (Commit) loadObject(commitID, "commits");
            for (String filename : givenCommit.blobs.keySet()) {
                checkout(commitID, "--", filename);
            }
            Commit currentCommit = (Commit) loadObject(HEAD.commitID, "commits");
            for (String filename : currentCommit.blobs.keySet()) {
                if (!givenCommit.blobs.containsKey(filename)) {
                    Utils.restrictedDelete(filename);
                }
            }
            HEAD.commitID = commitID;
            stagingArea.clear();
            untracked.clear();
        }
    }

    void merge(String branchName) throws IOException {
        /* Searches for split point, compares files, merges. Throws conflict if
         * conflicting files found.
         */
        if (!stagingArea.addedFiles.isEmpty() || !untracked.isEmpty()) {
            System.out.println("You have uncommitted changes.");
        } else if (!branches.containsKey(branchName)) {
            System.out.println("A branch with that name does not exist.");
        } else if (HEAD.name.equals(branchName)) {
            System.out.println("Cannot merge a branch with itself.");
        } else if (untrackedCheck(HEAD.commitID, branches.get(branchName).commitID)) {
            System.out.println("There is an untracked file in the way; delete it or add it first.");
        } else {
            boolean conflictEncountered = false;
            Branch givenBranch = branches.get(branchName);
            String sP = findSplitPoint(HEAD.commitID, givenBranch.commitID);
            if (sP.equals(givenBranch.commitID)) {
                System.out.println("Given branch is an ancestor of the current branch.");
            } else if (sP.equals(HEAD.commitID)) {
                HEAD.commitID = givenBranch.commitID;
                System.out.println("Current branch fast-forwarded.");
            } else {
                Commit splitPoint = (Commit) loadObject(sP, "commits");
                Commit givenCommit = (Commit) loadObject(givenBranch.commitID, "commits");
                Commit headCommit = (Commit) loadObject(HEAD.commitID, "commits");
                HashSet<String> allFileNames = new HashSet<>();
                allFileNames.addAll(givenCommit.blobs.keySet());
                allFileNames.addAll(headCommit.blobs.keySet());
                for (String fileName : allFileNames) {
                    boolean inGiven = givenCommit.blobs.containsKey(fileName);
                    boolean inHead = headCommit.blobs.containsKey(fileName);
                    boolean inSplit = splitPoint.blobs.containsKey(fileName);
                    boolean allPresent = inGiven && inHead && inSplit;
                    String bKey = givenCommit.blobs.getOrDefault(fileName, null);
                    String cKey = headCommit.blobs.getOrDefault(fileName, null);
                    String sKey = splitPoint.blobs.getOrDefault(fileName, null);
                    if ((allPresent && compareKeys(cKey, sKey) && !compareKeys(bKey, sKey))
                        || (!inSplit && !inHead)) {
                        checkout(givenBranch.commitID, "--", fileName);
                        add(fileName);
                    } else if (!inGiven && inSplit && compareKeys(sKey, cKey)) {
                        remove(fileName);
                    } else if (!((allPresent && bKey.equals(sKey))
                        || (!inSplit && !inGiven)
                        || (!inHead && compareKeys(sKey, bKey)))) {
                        String bContents = inGiven ? new String(((Blob) loadObject(
                                givenCommit.blobs.get(fileName), "blobs")).fileContent) : "";
                        String cContents = inHead ? new String(((Blob) loadObject(
                                headCommit.blobs.get(fileName), "blobs")).fileContent) : "";
                        File xFile = Utils.join(System.getProperty("user.dir"), fileName);
                        String xContents = "<<<<<<< HEAD" + System.lineSeparator() + cContents
                                + "=======" + System.lineSeparator() + bContents
                                + ">>>>>>>" + System.lineSeparator();
                        Utils.writeContents(xFile, xContents.getBytes());
                        conflictEncountered = true;
                    }
                }
                if (conflictEncountered) {
                    System.out.println("Encountered a merge conflict.");
                } else {
                    addCommit("Merged " + HEAD.name + " with " + branchName + ".");
                }
            }
        }
    }

    private String findSplitPoint(String id1, String id2) {
        /* Helper method for finding split point between two commits
         * of commit ids ID1 and ID2. Assumes remotes have note been
         * implemented yet.
         */
        HashSet<String> past = new HashSet<>();
        Commit commit1 = (Commit) loadObject(id1, "commits");
        Commit commit2 = (Commit) loadObject(id2, "commits");
        while (!(commit1 == null && commit2 == null)) {
            if (commit1 != null) {
                if (past.contains(id1)) {
                    return id1;
                }
                past.add(id1);
                id1 = commit1.parent;
                commit1 = commit1.parent == null
                        ? null : (Commit) loadObject(commit1.parent, "commits");
            }
            if (commit2 != null) {
                if (past.contains(id2)) {
                    return id2;
                }
                past.add(id2);
                id2 = commit2.parent;
                commit2 = commit2.parent == null
                        ? null : (Commit) loadObject(commit2.parent, "commits");
            }
        }
        return null;
    }

    static Object loadObject(String id, String subFolder) {
        /* Deserializes object with hash id ID from subfolder SUBFOLDER.
         * Works with both blobs and commits.
         */
        try {
            File in = Utils.join(System.getProperty("user.dir"), ".gitlet", subFolder, id);
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(in));
            Object obj = input.readObject();
            input.close();
            return obj;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Concurrent modification of " + subFolder + " folder.");
        }
    }

    private boolean untrackedCheck(String originalCommit, String newCommit) {
        /* Checks if there are untracked files that will be overwritten
         * by an operation from commit ORIGINALCOMMIT to commit NEWCOMMIT.
         */
        File dir = Utils.join(System.getProperty("user.dir"));
        String[] names = dir.list();
        Commit ogCommit = (Commit) loadObject(originalCommit, "commits");
        Commit nCommit = (Commit) loadObject(newCommit, "commits");
        if (names != null) {
            ArrayList<String> workingDir = new ArrayList<>(Arrays.asList(names));
            HashSet<String> allFiles = new HashSet<>();
            allFiles.addAll(ogCommit.blobs.keySet());
            allFiles.addAll(nCommit.blobs.keySet());
            for (String fileName : allFiles) {
                if (!ogCommit.blobs.containsKey(fileName) && workingDir.contains(fileName)) {
                    return true;
                }
            }
        }
        return false;
    }

    private String findFullID(String shortID) {
        // Returns 40 digit hash id from an abbreviated id SHORTID
        File dir = Utils.join(System.getProperty("user.dir"), ".gitlet", "commits");
        List<String> commits = Utils.plainFilenamesIn(dir);
        for (String commitID : commits) {
            if (commitID.startsWith(shortID)) {
                return commitID;
            }
        }
        return null;
    }

    private boolean compareKeys(String key1, String key2) {
        // Compares two hash ids KEY1 and KEY2
        return key1 == key2 || (key1 != null && key1.equals(key2))
                || (key2 != null && key2.equals(key1));
    }
}
