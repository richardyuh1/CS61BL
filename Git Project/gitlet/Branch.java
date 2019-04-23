package gitlet;

import java.io.Serializable;

public class Branch implements Serializable {
    String name;
    String commitID;

    Branch(String name, String commitID) {
        /* Instantiates new branch with name
         * NAME and commitID COMMITID.
         */
        this.name = name;
        this.commitID = commitID;
    }

}
