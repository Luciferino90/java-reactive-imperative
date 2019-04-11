package it.arubapec.javareactive.libs;

import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class FilesystemUtils {

    private static Random random = new Random();
    private static DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();

    public static Mono<String> reactiveWriteRandomFile(Path toWrite, int size){
        AtomicLong startTime = new AtomicLong(new Date().getTime());
        return Mono.just(getAsyncFileChannel(toWrite))
                .zipWhen(asyncChannel -> DataBufferUtils.write(multipleChunks(size), asyncChannel).collectList())
                .map(asyncChannelDataBufferT -> {
                    closeChannel(asyncChannelDataBufferT.getT1());
                    return asyncChannelDataBufferT.getT2();
                })
                .map(list -> "Durata in ms: " + ( new Date().getTime() - startTime.get()) + " path: " + toWrite.toString());
    }

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

    private static Publisher<DataBuffer> multipleChunks(int size) {
        int chunkSize = size / 10;
        return Flux.range(1, 10).map(integer -> randomBuffer(chunkSize));
    }

    private static DataBuffer randomBuffer(int size) {
        byte[] bytes = new byte[size];
        random.nextBytes(bytes);
        DataBuffer buffer = dataBufferFactory.allocateBuffer(size);
        buffer.write(bytes);
        return buffer;
    }

    private static AsynchronousFileChannel getAsyncFileChannel(Path toWrite){
        try {
            return AsynchronousFileChannel.open(toWrite, StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW);
        }catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private static void closeChannel(AsynchronousFileChannel asynchronousFileChannel){
        try {
            asynchronousFileChannel.close();
        }catch(Exception ex) {
            throw new RuntimeException(ex);
        }
    }

}
