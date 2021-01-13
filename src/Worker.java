import ru.spbstu.pipeline.IExecutable;
import ru.spbstu.pipeline.IExecutor;
import ru.spbstu.pipeline.ReturningCode;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Worker implements IExecutor {
    IExecutable Consumer;
    IExecutable Producer;
    int size;
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
        Producer = p;
        return ReturningCode.NO_ERROR;
    }

    @Override
    public ReturningCode execute(byte[] data) {
        ReturningCode RC;
        if(data.length == size){
            for (int i = 0; i < data.length; i++) {
                data[i] = '1';
            }
            RC = Consumer.execute(data);
        }
        else
            return ReturningCode.BUFFER_SIZE_ERROR;
        return RC;
    }
}
