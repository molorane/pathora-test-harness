package io.github.molorane.pathora.testharness.spi;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EntryPointExecutorTest {

    static class SampleRequest {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    static class SampleResponse {
        private String result;

        SampleResponse(String result) {
            this.result = result;
        }

        public String getResult() {
            return result;
        }
    }

    static class SimpleExecutor implements EntryPointExecutor<SampleRequest, SampleResponse> {
        @Override
        public String getEntryPointName() {
            return "simple-service";
        }

        @Override
        public Class<SampleRequest> getRequestType() {
            return SampleRequest.class;
        }

        @Override
        public SampleResponse execute(SampleRequest request) {
            return new SampleResponse("Hello, " + request.getName());
        }
    }

    @Test
    @DisplayName("Should return configured entry point name, request type, and execute properly")
    void shouldExecuteGenericExecutor() {
        SimpleExecutor executor = new SimpleExecutor();
        SampleRequest request = new SampleRequest();
        request.setName("Pathora");

        assertThat(executor.getEntryPointName()).isEqualTo("simple-service");
        assertThat(executor.getRequestType()).isEqualTo(SampleRequest.class);

        SampleResponse response = executor.execute(request);
        assertThat(response.getResult()).isEqualTo("Hello, Pathora");
    }
}






