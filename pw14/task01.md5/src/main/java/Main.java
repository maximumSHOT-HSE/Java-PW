import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

public class Main {

    private final static char[] HEX_DIGITS = "0123456789abcdef".toCharArray();

    private static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xff;
            hexChars[j * 2] = HEX_DIGITS[v >>> 4];
            hexChars[j * 2 + 1] = HEX_DIGITS[v & 0x0f];
        }
        return new String(hexChars);
    }

    /**
     * Takes a path as argument of command line,
     * calculates md5 check sum of file, which is
     * available by this path using both
     * single and multi thread modes, then
     * prints the check sums an time which
     * has been used for calculating.
     * */
    public static void main(String[] args) throws
            NoSuchAlgorithmException {
        if (args.length < 1) {
            System.out.println("Invalid program arguments");
            System.out.println("usage: ./main <path>");
            System.exit(-1);
        }

        double before, after, singleThreadTime, forkJoinTime;
        byte[] singleThreadCheckSum = null;
        byte[] forkJointCheckSum;
        SingleThreadMD5 singleThreadMD5;
        ForkJoinMD5 forkJoinMD5;
        String path = args[0];
        if (!new File(path).exists()) {
            System.out.println("Sorry, file does not exist");
            System.exit(1);
        }

        before = System.currentTimeMillis();
        singleThreadMD5 = new SingleThreadMD5();
        try {
            singleThreadCheckSum = singleThreadMD5.calculateMD5(path);
        } catch (IOException e) {
            System.out.println("Sorry, there are some problems with reading a files:");
            e.printStackTrace();
            System.exit(2);
        }
        after = System.currentTimeMillis();
        singleThreadTime = after - before;

        before = System.currentTimeMillis();
        forkJoinMD5 = new ForkJoinMD5();
        forkJointCheckSum = forkJoinMD5.calculateMD5(path);
        after = System.currentTimeMillis();
        forkJoinTime = after - before;

        System.out.println("Single Thread Mode:");
        System.out.println("Time: " + singleThreadTime + " ms");
        System.out.println("Checksum = " + bytesToHex(singleThreadCheckSum));
        System.out.println("------------------");
        System.out.println("Multi Thread Mode (with fork-join pool):");
        System.out.println("Time: " + forkJoinTime + " ms");
        System.out.println("Checksum = " + bytesToHex(forkJointCheckSum));
    }
}
