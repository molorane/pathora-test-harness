package za.co.pathora.testharness.spi;

public interface EntryPointExecutor {

    String getEntryPointName();

    Class<?> getRequestType();

    Object execute(Object request);
}