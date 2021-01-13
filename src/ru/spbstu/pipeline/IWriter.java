package ru.spbstu.pipeline;
import java.io.FileOutputStream;

public interface IWriter extends IConfigurable, IExecutable {
    ReturningCode setOutputStream(FileOutputStream fos);
}
