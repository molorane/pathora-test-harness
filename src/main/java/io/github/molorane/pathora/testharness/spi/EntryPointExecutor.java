package io.github.molorane.pathora.testharness.spi;

public interface EntryPointExecutor {

    String getEntryPointName();

    Class<?> getRequestType();

    Object execute(Object request);
}