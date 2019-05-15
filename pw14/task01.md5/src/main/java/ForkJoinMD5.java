import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

/**
 * Class for calculating check sums of
 * files and directories in a multi thread mode
 * via fork join technique.
 * */
public class ForkJoinMD5 {

    /**
     * Calculates md5 check sum in a multi thread mode
     * by recursive rule, namely:
     * f(file) = MD5(content)
     * f(directory) = MD5((directory name) + f(file1) + ...)
     *
     * If there were some problems with reading some file
     * then undefined behaviour may be caused.
     *
     * Multi thread md5 calculator depends on single thread
     * md5 calculator, when md5 check sum of file (but not of
     * directory) should be calculated, so subtask of calculating
     * md5(file) (not md5(directories)) can not fork any another task.
     * In such situations method of single thread md5 calculator
     * will be used.
     *
     * @return calculated hash in terms of byte array
     * @param fileName is the name of file
     * which hash needs to be calculated.
     * @throws IllegalArgumentException when there is no
     * such file with given name  */
    public @NotNull byte[] calculateMD5(@NotNull String fileName) {
        File file = new File(fileName);
        if (!file.exists()) {
            throw new IllegalArgumentException("File with name : "
                    + fileName + " does not exists");
        }
        return new ForkJoinPool().invoke(new MD5Calculator(file));
    }

    private class MD5Calculator extends RecursiveTask<byte[]> {

        @NotNull private File file;

        public MD5Calculator(@NotNull File file) {
            this.file = file;
        }

        @Override
        protected byte[] compute() {
            if (file.isFile()) {
                SingleThreadMD5 singleThreadMD5 = new SingleThreadMD5();
                try {
                    return singleThreadMD5.calculateMD5(file.getAbsolutePath());
                } catch (NoSuchAlgorithmException | IOException ignored) {
                    /*
                     * The behaviour in such situations is not described
                     * in the statement.
                     * */
                    return new byte[0];
                }
            }
            MessageDigest messageDigest;
            try {
                messageDigest = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException ignored) {
                /*
                 * The behaviour in such situations is not described
                 * in the statement.
                 * */
                return new byte[0];
            }
            messageDigest.update(file.getName().getBytes());
            List<MD5Calculator> subTasks = new ArrayList<>();
            File[] childFiles = SingleThreadMD5.getSortedChildFiles(file);
            for (var child : childFiles) {
                MD5Calculator task = new MD5Calculator(child);
                task.fork();
                subTasks.add(task);
            }
            for (var task : subTasks) {
                messageDigest.update(task.join());
            }
            return messageDigest.digest();
        }
    }
}
