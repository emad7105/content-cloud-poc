package be.heydari.lazyabacfilter;

import be.heydari.AstWalker;
import be.heydari.lib.converters.protobuf.ProtobufUtils;
import be.heydari.lib.converters.protobuf.generated.PDisjunction;
import be.heydari.lib.expressions.Disjunction;
import brave.Span;
import brave.Tracer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.cloud.sleuth.annotation.SpanTag;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

import static java.lang.String.format;

@Data
public class OPAClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(OPAClient.class);

    private String baseUrl;
    private Tracer tracer;

    public OPAClient(String baseUrl, Tracer tracer) {
        this.baseUrl = baseUrl;
        this.tracer = tracer;
    }

    public String queryOPA(String query, OpaInput input, List<String> unknowns) throws IOException {
        Span span = tracer.nextSpan().name("call-opa");
        try (Tracer.SpanInScope ws = this.tracer.withSpanInScope(span.start())) {
            span.tag("query", query);
            OpaQuery opaQuery = OpaQuery.builder()
                .query(query)
                .input(input)
                .unknowns(unknowns)
                .build();

            String residualPolicy = new RestTemplate()
                .postForObject(format("%s/v1/compile", baseUrl), opaQuery, String.class);

            System.out.println(format("Residual policy: %s", residualPolicy));

            //ResponseAST
            Disjunction disjunction = AstWalker.walk(residualPolicy);
            PDisjunction pDisjunction = ProtobufUtils.from(disjunction, "");
            byte[] protoBytes = pDisjunction.toByteArray();
            if (protoBytes.length == 0) {
                return null;
            }
            return Base64.getEncoder().encodeToString(protoBytes);
        } finally {
            span.finish();
        }
    }

/*    public static String queryOPA(Long brokerId) throws IOException {
        OpaQuery opaQuery = OpaQuery.builder()
                .query("data.abac_policies.allow_partial == false")
                .input(new OpaInput("GET", format("%sL", brokerId)))
                .unknowns(Collections.singletonList("data.accountState"))
                .build();

        String residualPolicy = new RestTemplate()
                .postForObject("http://127.0.0.1:8181/v1/compile", opaQuery, String.class);

        //ResponseAST
        Disjunction disjunction = AstWalker.walk(residualPolicy);
        PDisjunction pDisjunction = ProtobufUtils.from(disjunction, "");
        byte[] protoBytes = pDisjunction.toByteArray();
        return Base64.getEncoder().encodeToString(protoBytes);
    }*/


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
