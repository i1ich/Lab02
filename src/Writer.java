import ru.spbstu.pipeline.IExecutable;
import ru.spbstu.pipeline.IWriter;
import ru.spbstu.pipeline.ReturningCode;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;

public class Writer implements IWriter {
    IExecutable Producer;
    FileOutputStream F;
    int size;

    @Override
    public ReturningCode setOutputStream(FileOutputStream fos) {
        F = fos;
        return ReturningCode.NO_ERROR;
    }

    @Override
    public ReturningCode setConfig(String cfg) {
        try {
            FileInputStream F1 = new FileInputStream(cfg);
            Scanner input = new Scanner(F1);
            input.useDelimiter(" |\\n");
            if (input.hasNext()) {
                String s = input.next();
                if (!s.equals("Size:")) {

                    return ReturningCode.GRAMMAR_MISMATCH_ERROR;
                }
            } else
                return ReturningCode.WRITE_ERROR;
            if (input.hasNext()) {
                String s = input.next();
                size = Integer.valueOf(s);

            }

        } catch (FileNotFoundException e) {
            return ReturningCode.OUTPUT_FILE_OPEN_ERROR;
        }

        return ReturningCode.NO_ERROR;
    }

    @Override
    public ReturningCode setConsumer(IExecutable c) {
        // do nothing, it`s writer
        return ReturningCode.NO_ERROR;
    }

    @Override
    public ReturningCode setProducer(IExecutable p) {
        Producer = p;
        return ReturningCode.NO_ERROR;
    }

    @Override
    public ReturningCode execute(byte[] data) {
        try {
            if (data.length != size)
                return ReturningCode.BUFFER_SIZE_ERROR;
            F.write(data);
        } catch (IOException e) {
            return ReturningCode.WRITE_ERROR;
        }
        return ReturningCode.NO_ERROR;
    }
}
