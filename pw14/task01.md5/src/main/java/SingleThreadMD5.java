import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Class for calculating check sums of
 * files and directories in a single thread mode.
 * */
public class SingleThreadMD5 {

    private static final int BLOCK_SIZE = 4096;

    private @NotNull byte[] calculateFileHash(@NotNull File file) throws
            NoSuchAlgorithmException,
            IOException {
        var messageDigest = MessageDigest.getInstance("MD5");
        var digestInputStream = new DigestInputStream(
                new FileInputStream(file), messageDigest);
        byte[] buffer = new byte[BLOCK_SIZE];
        while (digestInputStream.read(buffer, 0, BLOCK_SIZE) > 0) {
            // nothing to do
        }
        return messageDigest.digest();
    }

    /**
     * Finds all child files of given, sorts them by their absolute
     * paths and returns array of them. If there are no any child file
     * then empty array will be returned.
     *
     * @param file is File instance of file, whose child should be processed
     * @return an array of child files sorted by absolute paths
     * */
    @NotNull public static File[] getSortedChildFiles(@NotNull File file) {
        var childFiles = file.listFiles();
        if (childFiles == null) {
            return new File[0];
        }
        Arrays.sort(childFiles, Comparator.comparing(File::getAbsolutePath));
        return childFiles;
    }

    private @NotNull byte[] calculateHash(@NotNull File file) throws
            NoSuchAlgorithmException,
            IOException {
        if (file.isFile()) { // is not directory
            return calculateFileHash(file);
        }
        // is directory
        var messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(file.getName().getBytes());
        var childFiles = getSortedChildFiles(file);
        for (var child : childFiles) {
            var childHash = calculateHash(child);
            messageDigest.update(childHash);
        }
        return messageDigest.digest();
    }

    /**
     * Calculates md5 check sum in a single thread mode
     * by recursive rule, namely:
     * f(file) = MD5(content)
     * f(directory) = MD5((directory name) + f(file1) + ...)
     *
     * @return calculated hash in terms of byte array
     * @param fileName is the name of file
     * which hash needs to be calculated.
     * @throws IllegalArgumentException when there is no
     * such file with given name
     * @throws NoSuchAlgorithmException if md5 does not exist
     * @throws IOException if during calculating check sum
     * there were problems with reading some files
     * */
    public @NotNull byte[] calculateMD5(@NotNull String fileName) throws
            NoSuchAlgorithmException,
            IOException {
        var file = new File(fileName);
        if (!file.exists()) {
            throw new IllegalArgumentException("File with name : "
                    + fileName + " does not exists");
        }
        return calculateHash(file);
    }

}
