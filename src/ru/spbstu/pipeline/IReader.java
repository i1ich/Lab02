package ru.spbstu.pipeline;
import java.io.FileInputStream;

public interface IReader extends IConfigurable, IExecutable {
    ReturningCode setInputStream(FileInputStream fis);
}
