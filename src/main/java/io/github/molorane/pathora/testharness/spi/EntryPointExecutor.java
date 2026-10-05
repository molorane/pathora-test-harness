package io.github.molorane.pathora.testharness.spi;

/**
 * Service Provider Interface (SPI) for executing system entry points under test.
 *
 * <p>Implementations bridge the test harness to specific business logic, services,
 * or workflow entry points by deserializing input requests and executing the target logic.</p>
 *
 * @param <REQ> the type of the request payload accepted by the entry point
 * @param <RES> the type of the response payload returned by the entry point
 */
public interface EntryPointExecutor<REQ, RES> {

    /**
     * Returns the unique name of the entry point handled by this executor.
     *
     * @return the entry point name
     */
    String getEntryPointName();

    /**
     * Returns the class type of the request object used for deserialization.
     *
     * @return the request class type
     */
    Class<REQ> getRequestType();

    /**
     * Executes the business entry point with the provided request payload.
     *
     * @param request the deserialized request object
     * @return the response object produced by the entry point
     */
    RES execute(REQ request);
}






