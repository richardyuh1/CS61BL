package gitlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class CommandHandler {
    /* Found concise way to initialize ArrayList online.
     * Source: https://stackoverflow.com/questions/1005073/
     */
    static void main(String[] args) throws IOException, ClassNotFoundException {
        int len = args.length;
        if (len == 0) {
            System.out.println("Please enter a command.");
            return;
        }
        ArrayList<String> validCommands = new ArrayList<>(
                Arrays.asList("init", "add", "commit", "rm", "log",
                              "global-log", "find", "status", "checkout",
                              "branch", "rm-branch", "reset", "merge"));
        String command = args[0].toLowerCase();
        if (!validCommands.contains(command)) {
            System.out.println("No command with that name exists.");
        } else {
            File gitletPath = Utils.join(System.getProperty("user.dir"), ".gitlet");
            if (!command.equals("init")) {
                if (!gitletPath.exists() || !gitletPath.isDirectory()) {
                    System.out.println("Not in an initialized gitlet directory.");
                    return;
                }
            } else {
                if (len == 1) {
                    CommandHandler.init();
                } else {
                    System.out.println("Incorrect operands.");
                }
                return;
            }
            File in = Utils.join(gitletPath, "stateInstance");
            ObjectInputStream input = new ObjectInputStream(new FileInputStream(in));
            GlobalState repo = (GlobalState) input.readObject();
            input.close();
            if (len == 1) {
                if (command.equals("log")) {
                    repo.log();
                } else if (command.equals("global-log")) {
                    repo.globalLog();
                } else if (command.equals("status")) {
                    repo.status();
                }
            } else if (len == 2) {
                if (command.equals("add")) {
                    repo.add(args[1]);
                } else if (command.equals("commit")) {
                    if (args[1].length() > 0) {
                        repo.addCommit(args[1]);
                    } else {
                        System.out.println("Please enter a commit message.");
                    }
                } else if (command.equals("rm")) {
                    repo.remove(args[1]);
                } else if (command.equals("find")) {
                    repo.find(args[1]);
                } else if (command.equals("checkout")) {
                    repo.checkout(args[1]);
                } else if (command.equals("branch")) {
                    repo.addBranch(args[1]);
                } else if (command.equals("rm-branch")) {
                    repo.rmBranch(args[1]);
                } else if (command.equals("reset")) {
                    repo.reset(args[1]);
                } else if (command.equals("merge")) {
                    repo.merge(args[1]);
                }
            } else if (len == 3) {
                if (command.equals("checkout")) {
                    repo.checkout(args[1], args[2]);
                }
            } else if (len == 4) {
                if (command.equals("checkout")) {
                    repo.checkout(args[1], args[2], args[3]);
                }
            } else {
                System.out.println("Incorrect operands.");
            }
            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(in));
            output.writeObject(repo);
            output.close();
        }
    }

    static void init() throws IOException {
        /* Instantiates new gitlet repository in working directory
         * if one is not already present. Instantiates and saves corresponding
         * instance of globalstate.
         */
        File gitletPath = Utils.join(System.getProperty("user.dir"), ".gitlet");
        if (gitletPath.exists()) {
            System.out.println(
                    "A gitlet version-control system already exists in the current directory");
        } else {
            boolean success = true;
            File[] files = {Utils.join(gitletPath, "commits"), Utils.join(gitletPath, "blobs")};
            for (File file : files) {
                success = file.mkdirs() && success;
            }
            if (!success) {
                throw new RuntimeException("Directory creation failed.");
            } else {
                GlobalState repo = new GlobalState();
                File out = Utils.join(gitletPath, "stateInstance");
                ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(out));
                output.writeObject(repo);
                output.close();
            }
        }
    }
}
