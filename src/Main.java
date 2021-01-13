import ru.spbstu.pipeline.ReturningCode;

import java.io.*;
import java.util.IllegalFormatException;

public class Main {
    public static void main(String[] args) {
        ReturningCode RC = ReturningCode.NO_ERROR;
        FileInputStream In = null;
        try {
            if (args.length != 1) {
                String E = "Bad cmd line format";
                throw new MyException(E);
            }
            Manager M = new Manager(args[0]);
            RC = M.SyntaxParse();
            if (RC == ReturningCode.NO_ERROR)
                RC = M.Run();
            BufferedWriter writer = new BufferedWriter(new FileWriter("log.txt"));
            switch (RC) {
                case ALGORITHM_PARAM_ERROR:
                    writer.write("ERROR in algorithm\n");
                    break;
                case READ_ERROR:
                    writer.write("ERROR in read module");
                    break;
                case WRITE_ERROR:
                    writer.write("ERROR in write module");
                    break;
                case BUFFER_SIZE_ERROR:
                    writer.write("ERROR size between executors is different");
                    break;
                case GRAMMAR_MISMATCH_ERROR:
                    writer.write("ERROR in config files");
                    break;
                case INPUT_FILE_OPEN_ERROR:
                    writer.write("ERROR: input file not open");
                    break;
                case OUTPUT_FILE_OPEN_ERROR:
                    writer.write("ERROR: output file not open");
                    break;
                case INVALID_ARGUMENT_ERROR:
                    System.err.println("ERROR: output file not open");
                    break;
            }
            writer.close();
        } catch (MyException ex) {
            ex.myOwnExceptionMsg();
        } catch (IOException e) {
            System.err.println("ERROR: log file not open");
        }

    }
}
