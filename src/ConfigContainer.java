import java.util.Vector;

public final class ConfigContainer {
    public final String Input;
    public final String ReaderConf;
    public final String WriterConf;
    public final String Output;
    public final Vector<MyPair> Exec;
    // Executors class name in correct order
    public final Vector<String> Order;

    public ConfigContainer(String input, String readerConf, String writerConf, String output, Vector<MyPair> exec, Vector<String> order) {
        Input = input;
        ReaderConf = readerConf;
        WriterConf = writerConf;
        Output = output;
        Exec = exec;
        Order = order;
    }

    public ConfigContainer(String input, String readerConf, String writerConf,
                           String output) {
        Input = input;
        ReaderConf = readerConf;
        WriterConf = writerConf;
        Output = output;
        Exec = new Vector<>();
        Order =  new Vector<>();
    }
}
