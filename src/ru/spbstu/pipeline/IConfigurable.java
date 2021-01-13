package ru.spbstu.pipeline;

public interface IConfigurable {
    ReturningCode setConfig(String cfg);
    ReturningCode setConsumer(IExecutable c);
    ReturningCode setProducer(IExecutable p);
}
