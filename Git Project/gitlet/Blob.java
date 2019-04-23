package gitlet;

import java.io.File;
import java.io.Serializable;

public class Blob implements Serializable {
    byte[] fileContent;

    Blob(String fileName) {
        /* Instantiates new blob for file FILENAME
         * Saves contents of file as byte array.
         */
        File f = new File(fileName);
        fileContent = Utils.readContents(f);
    }
}
