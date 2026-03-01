package za.co.nedbank.brm.testharness.spi;

public interface EntryPointExecutor {

    String getEntryPointName();

    Class<?> getRequestType();

    Object execute(Object request);
}