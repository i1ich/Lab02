import ru.spbstu.pipeline.IExecutable;
import ru.spbstu.pipeline.IReader;
import ru.spbstu.pipeline.ReturningCode;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Reader implements IReader {
    IExecutable Consumer;
    FileInputStream F;
    int size;


    @Override
    public ReturningCode setInputStream(FileInputStream fis) {
        F = fis;
        return ReturningCode.NO_ERROR;
    }
    // Only buffer size in config(Size: 100 for example)
    @Override
    public ReturningCode setConfig(String cfg) {
        try {
            FileInputStream F1 = new FileInputStream(cfg);
            Scanner input = new Scanner(F1);
            input.useDelimiter(" |\\n");
            if(input.hasNext()) {
                String s = input.next();
                if(!s.equals("Size:")) {

                    return ReturningCode.GRAMMAR_MISMATCH_ERROR;
                }
            }
            else
                return ReturningCode.READ_ERROR;
            if(input.hasNext()) {
                String s = input.next();
                size = Integer.valueOf(s);

            }

        } catch (FileNotFoundException e) {
            return ReturningCode.INPUT_FILE_OPEN_ERROR;
        }

        return ReturningCode.NO_ERROR;
    }

    @Override
    public ReturningCode setConsumer(IExecutable c) {
        Consumer = c;
        return ReturningCode.NO_ERROR;
    }

    @Override
    public ReturningCode setProducer(IExecutable p) {
        // do nothing, it is reader
        return ReturningCode.NO_ERROR;
    }

    @Override
    public ReturningCode execute(byte[] data) {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(F, size);
        byte[] buff =new byte[size];
        try {
            int bytes = bufferedInputStream.read(buff, 0, size);
        } catch (IOException e) {
            return ReturningCode.READ_ERROR;
        }
        ReturningCode RC;
        RC = Consumer.execute(buff);
        return RC;
    }
}
