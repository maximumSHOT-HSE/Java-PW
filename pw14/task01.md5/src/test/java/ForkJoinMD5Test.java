import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ForkJoinMD5Test {

    @NotNull
    private ForkJoinMD5 hasher;

    @BeforeEach
    void setUp() {
        hasher = new ForkJoinMD5();
    }

    @RepeatedTest(10)
    void testEmptyFile() throws
            NoSuchAlgorithmException {
        var messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(new byte[0]);
        assertArrayEquals(messageDigest.digest(),
                hasher.calculateMD5("src/test/resources/testFolder/emptyFile"));
    }

    @RepeatedTest(10)
    void testEmptyDirectory() throws NoSuchAlgorithmException {
        var messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update("emptyDirectory".getBytes());
        assertArrayEquals(messageDigest.digest(),
                hasher.calculateMD5("src/test/resources/testFolder/emptyDirectory/"));
    }

    @RepeatedTest(10)
    void testLongChain() throws NoSuchAlgorithmException {
        MessageDigest messageDigest;

        /*
         chain1/
            └── chain2
                └── chain3
                    └── file.txt
         * */

        messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update("Hello World!".getBytes());
        byte[] fileHash = messageDigest.digest();

        messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update("chain3".getBytes());
        messageDigest.update(fileHash);
        byte[] chain3Hash = messageDigest.digest();

        messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update("chain2".getBytes());
        messageDigest.update(chain3Hash);
        byte[] chain2Hash = messageDigest.digest();

        messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update("chain1".getBytes());
        messageDigest.update(chain2Hash);
        byte[] chain1Hash = messageDigest.digest();

        assertArrayEquals(chain1Hash,
                hasher.calculateMD5("src/test/resources/chain1"));
    }

    @RepeatedTest(10)
    void testComplicatedTree() throws NoSuchAlgorithmException {
        MessageDigest messageDigest;

        /*
        * root/
            ├── son1
            │   └── son1file.txt ("I am the first son!")
            └── son2
                ├── grandson1
                └── grandson2
                    └── grandson2file.txt ("I am the son of second son!")
        * */

        messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update("I am the first son!".getBytes());
        byte[] son1fileHash = messageDigest.digest();

        messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update("son1".getBytes());
        messageDigest.update(son1fileHash);
        byte[] son1Hash = messageDigest.digest();

        messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update("I am the son of second son!".getBytes());
        byte[] grandson2fileHash = messageDigest.digest();

        messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update("grandson2".getBytes());
        messageDigest.update(grandson2fileHash);
        byte[] grandson2Hash = messageDigest.digest();

        messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update("grandson1".getBytes());
        byte[] grandson1Hash = messageDigest.digest();

        messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update("son2".getBytes());
        messageDigest.update(grandson1Hash);
        messageDigest.update(grandson2Hash);
        byte[] son2Hash = messageDigest.digest();

        messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update("root".getBytes());
        messageDigest.update(son1Hash);
        messageDigest.update(son2Hash);
        byte[] rootHash = messageDigest.digest();

        assertArrayEquals(rootHash,
                hasher.calculateMD5("src/test/resources/root"));
    }

    @RepeatedTest(10)
    void tetIllegalFileName() {
        assertThrows(IllegalArgumentException.class, () -> hasher.calculateMD5("bla-bla-bla"));
    }

    @RepeatedTest(10)
    void testLargeFile() throws NoSuchAlgorithmException {
        String s = "eklrjghbgjrelgbregberjlgrelkgreklgrhebgrelbgerlgrgn[we[oqr8023[8jrn33[  823jr [r[2  3 rjionf[   98 2 98 49oij3498t t3498 t43 89th3t";
        int numberOfBlock = 2048;
        var messageDigest = MessageDigest.getInstance("MD5");
        for (int i = 0; i < numberOfBlock; i++) {
            messageDigest.update(s.getBytes());
        }
        assertArrayEquals(messageDigest.digest(),
                hasher.calculateMD5("src/test/resources/testFolder/text.txt"));
    }
}