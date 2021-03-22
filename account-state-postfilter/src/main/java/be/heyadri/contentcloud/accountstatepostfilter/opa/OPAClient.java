package be.heyadri.contentcloud.accountstatepostfilter.opa;

import brave.Span;
import brave.Tracer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class OPAClient {
    private OPAClientConfig config;

    public OPAClient(OPAClientConfig config) {
        this.config = config;
    }

    public OPAResponse queryOPA(OPAInput input) throws IOException {
        Tracer tracer = this.config.getTracer();
        Span span = tracer.nextSpan().name("call-opa");
        try (Tracer.SpanInScope ws = tracer.withSpanInScope(span.start())) {
            span.tag("query", this.config.getQuery());
            return this.queryOPAInner(this.config.getQuery(), input);
        } finally {
            span.finish();
        }
    }

    private OPAResponse queryOPAInner(String query, OPAInput input) throws IOException {
        OPADataRequestBody body = OPADataRequestBody.builder().input(input).pretty(true).build();
        System.out.println("request body:" + body.toJson());

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
            String.format("%s/v1/%s",
                this.config.getBaseUrl(),
                query.replace(".", "/")
                )
            )
            .queryParam("pretty", true)
            .queryParam("input", input.toJson());


        String urlString = String.format("%s/v1/%s?pretty=true&input=%s",
            this.config.getBaseUrl(),
            query.replace(".", "/"),
            input.toJson());

        URL url = new URL(urlString);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        int status = con.getResponseCode();
        if (status != 200) {
            throw new IllegalStateException("status code should be 200 got" + status + "instead.");
        }

        BufferedReader in = new BufferedReader(
            new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();

        String response = content.toString();
        System.out.println(response);

        Gson gson = new Gson();
        return gson.fromJson(response, OPAResponse.class);
    }

    @Data
    @Builder
    static class OPADataRequestBody {
        OPAInput input;
        boolean pretty;
        String toJson() throws JsonProcessingException {
            return new ObjectMapper().writeValueAsString(this);
        }
    }

    @Data
    public static class OPAResponse {

        public boolean result;

        public OPAResponse(boolean result) {
            this.result = result;
        }
    }
}
