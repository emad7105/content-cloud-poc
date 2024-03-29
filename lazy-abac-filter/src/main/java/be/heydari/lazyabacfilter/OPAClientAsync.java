package be.heydari.lazyabacfilter;

import be.heydari.AstWalker;
import be.heydari.lib.converters.protobuf.ProtobufUtils;
import be.heydari.lib.converters.protobuf.generated.PDisjunction;
import be.heydari.lib.expressions.Disjunction;
//import brave.Span;
//import brave.Tracer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.io.IOException;
import java.net.URI;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

@Data
public class OPAClientAsync {
    private static final Logger LOGGER = LoggerFactory.getLogger(OPAClientAsync.class);

    private String baseUrl;
//    private Tracer tracer;
    private WebClient reactiveClient;

    public OPAClientAsync(String baseUrl/*, Tracer tracer*/, WebClient reactiveClient) {
        LOGGER.info("elastic version");

        this.baseUrl = baseUrl;
//        this.tracer = tracer;
        this.reactiveClient = reactiveClient;

//        HttpClient httpClient = HttpClient.create(ConnectionProvider
//                .elastic("OpaHttpClientPool"));
//        this.reactiveClient = WebClient
//                .builder()
//                .clientConnector(new ReactorClientHttpConnector(httpClient))
//                .baseUrl(baseUrl)
//                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
//                .build();
    }

    public Mono<String> queryOPA(String query, OpaInput input, List<String> unknowns) {
//        Span span = tracer.nextSpan().name("call-opa");
//        try (Tracer.SpanInScope ws = this.tracer.withSpanInScope(span.start())) {
//            span.tag("query", query);
            OpaQuery opaQuery = OpaQuery.builder()
                    .query(query)
                    .input(input)
                    .unknowns(unknowns)
                    .build();

            return reactiveClient.post()
                    .uri(URI.create(format("%s/v1/compile", baseUrl)))
                    .body(Mono.just(opaQuery), OpaQuery.class)
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(String.class)
                    .map(residualPolicy -> {
                        return convertResidualPolicyToProtoBuf(residualPolicy).get();
                    });
            //String opaMockResponseBroker0AccountStatesCall = "{\"result\":{\"queries\":[[{\"index\":0,\"terms\":{\"type\":\"ref\",\"value\":[{\"type\":\"var\",\"value\":\"data\"},{\"type\":\"string\",\"value\":\"partial\"},{\"type\":\"string\",\"value\":\"accountstates\"},{\"type\":\"string\",\"value\":\"allow\"}]}}]],\"support\":[{\"package\":{\"path\":[{\"type\":\"var\",\"value\":\"data\"},{\"type\":\"string\",\"value\":\"partial\"},{\"type\":\"string\",\"value\":\"accountstates\"}]},\"rules\":[{\"head\":{\"name\":\"allow\",\"value\":{\"type\":\"boolean\",\"value\":true}},\"body\":[{\"index\":0,\"terms\":[{\"type\":\"ref\",\"value\":[{\"type\":\"var\",\"value\":\"eq\"}]},{\"type\":\"string\",\"value\":\"broker0\"},{\"type\":\"ref\",\"value\":[{\"type\":\"var\",\"value\":\"data\"},{\"type\":\"string\",\"value\":\"accountState\"},{\"type\":\"string\",\"value\":\"brokerName\"}]}]}]},{\"default\":true,\"head\":{\"name\":\"allow\",\"value\":{\"type\":\"boolean\",\"value\":false}},\"body\":[{\"index\":0,\"terms\":{\"type\":\"boolean\",\"value\":true}}]}]}]}}";
//            return Mono.just(convertResidualPolicyToProtoBuf(opaMockResponseBroker0AccountStatesCall).get());
//        } finally {
//            span.finish();
//        }
    }

    public Optional<String> convertResidualPolicyToProtoBuf(String residualPolicy) {
        //ResponseAST
        Disjunction disjunction = null;
        try {
            disjunction = AstWalker.walk(residualPolicy);
            PDisjunction pDisjunction = ProtobufUtils.from(disjunction, "");
            byte[] protoBytes = pDisjunction.toByteArray();
            if (protoBytes.length == 0) {
                return Optional.empty();
            }
            return Optional.ofNullable(Base64.getEncoder().encodeToString(protoBytes));
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            return Optional.empty();
        }
    }

    /**
     * Example:
     * {
     * "query": "data.abac_policies.allow_partial == false",
     * "input": {
     * "action": "GET",
     * "brokerId": "1l"
     * },
     * "unknowns": [
     * "data.accountState"
     * ]
     * }
     */
    @Data
    @Builder
    static class OpaQuery {
        String query;
        OpaInput input;
        List<String> unknowns;

        String toJson() throws JsonProcessingException {
            return new ObjectMapper().writeValueAsString(this);
        }
    }
}
