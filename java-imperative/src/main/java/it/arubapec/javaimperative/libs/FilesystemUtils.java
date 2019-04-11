package it.arubapec.javaimperative.libs;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Random;

public class FilesystemUtils {

    private static Random random = new Random();

    public static Path imperativeEriteRandomFile(Path toWrite, int size){
        try (AsynchronousFileChannel file = AsynchronousFileChannel.open(toWrite, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE, StandardOpenOption.READ)){
            byte[] contents = new byte[size];
            random.nextBytes(contents);
            file.write(ByteBuffer.wrap(contents, 0, size), 0);
        }catch(Exception ex) {
            throw new RuntimeException(ex);
        }
        return toWrite;
    }

}
