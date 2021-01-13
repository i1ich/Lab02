import javafx.util.Pair;
import ru.spbstu.pipeline.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Vector;

public class Manager {
    private String ConfigName;
    private Vector<Object> pipeline;
    // Streams to close
    private FileInputStream fileInputStream;
    private FileOutputStream fileOutputStream;
    private IWriter writer = new Writer();
    private IReader reader = new Reader();
    private Vector<Pair<String, IExecutor>> workers;

    public Manager(String Name) {
        this.ConfigName = Name;
        pipeline = new Vector<>();
        workers = new Vector<>();
    }

    public ReturningCode SyntaxParse() {
        ConfigParse c = new ConfigParse(ConfigName);
        ReturningCode RC = ReturningCode.NO_ERROR;
        try {
            ConfigContainer data = c.RefreshData();
            RC = CreateSession(data);

        } catch (MyException e) {
            e.myOwnExceptionMsg(); // to log
            if (RC == ReturningCode.NO_ERROR)
                RC = ReturningCode.ALGORITHM_PARAM_ERROR;
        }
        return RC;
    }

    IExecutor FindWorker(String S) {
        for (Pair P : workers
        ) {
            if (P.getKey().equals(S)) {
                return (IExecutor) P.getValue();
            }

        }
        return null;
    }

    ReturningCode MakeConveyor(ConfigContainer conf) {
        pipeline.add(reader);
        for (int i = 1; i < conf.Order.size() - 1; i++) {
            IExecutor w = FindWorker(conf.Order.get(i));
            if (w == null)
                return ReturningCode.ALGORITHM_PARAM_ERROR;
            pipeline.add(w);
        }
        pipeline.add(writer);

        IReader reader = (IReader) pipeline.get(0);
        reader.setConsumer((IExecutable) pipeline.get(1));
        IWriter writer = (IWriter) pipeline.get(pipeline.size() - 1);

        writer.setProducer((IExecutable) pipeline.get(pipeline.size() - 2));
        for (int i = 1; i < pipeline.size() - 1; i++) {
            IExecutor w = (IExecutor) pipeline.get(i);
            w.setConsumer((IExecutable) pipeline.get(i + 1));
            w.setProducer((IExecutable) pipeline.get(i - 1));
        }
        return ReturningCode.NO_ERROR;
    }

    // Parametrize conveyor/session
    ReturningCode CreateSession(ConfigContainer conf) {
        // Reader
        try {
            fileInputStream = new FileInputStream(conf.Input);
            ReturningCode RC = reader.setConfig(conf.ReaderConf);
            if (RC != ReturningCode.NO_ERROR)
                return RC;
            reader.setInputStream(fileInputStream);
        } catch (FileNotFoundException e) {
            return ReturningCode.INPUT_FILE_OPEN_ERROR; // to log
        }
        // Workers
        for (MyPair P : conf.Exec) {
            Worker worker = new Worker();
            ReturningCode RC = worker.setConfig(conf.ReaderConf);
            if (RC != ReturningCode.NO_ERROR)
                return RC;

            // all are Workers
            //Class cls = Class.forName(P.getClassName());
            //if (!cls.isInstance(worker))
            //    return ReturningCode.ALGORITHM_PARAM_ERROR;

            workers.add(new Pair(P.getClassName(), worker));
        }
        // Writer
        try {
            fileOutputStream = new FileOutputStream(conf.Output);
            ReturningCode RC = writer.setConfig(conf.WriterConf);
            if (RC != ReturningCode.NO_ERROR)
                return RC;
            writer.setOutputStream(fileOutputStream);
        } catch (IOException e) {
            return ReturningCode.OUTPUT_FILE_OPEN_ERROR; // to log
        }
        /*IReader reader = (IReader) pipeline.get(0);
        reader.setConsumer((IExecutable) pipeline.get(1));
        IWriter writer = (IWriter) pipeline.get(pipeline.size() - 1);

        writer.setConsumer((IExecutable) pipeline.get(pipeline.size() - 2));
        for (int i = 1; i < pipeline.size() - 1; i++) {
            IExecutor w = (IExecutor) pipeline.get(i);
            w.setConsumer((IExecutable) pipeline.get(i + 1));
            w.setProducer((IExecutable) pipeline.get(i - 1));
        }*/

        ReturningCode RC = MakeConveyor(conf);
        return RC;
    }

    ReturningCode Run() {
        IExecutable input = (IExecutable) pipeline.get(0);
        byte[] a = {0};
        ReturningCode RC = input.execute(a);
        try {
            fileInputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            return ReturningCode.ALGORITHM_PARAM_ERROR;
        }
        return RC;
    }
}
