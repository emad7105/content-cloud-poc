package be.heydari.contentcloud.opa;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class PolicyPusher {
    public static void main(String[] args) throws Exception {
        var env = System.getenv();
        var lang = env.getOrDefault("POLICY_LANGUAGE", "Rego");

        switch (lang) {
            case "Rego":
                pushRegoPolicy(env);
                break;
            case "Java":
                pushHardCodedPolicy(env);
                break;
            default:
                throw new IllegalArgumentException("unknown policy language: " + lang);
        }
    }

    public static void pushRegoPolicy(Map<String, String> env) throws Exception {
        var pdpAddr = env.getOrDefault("PDP_ADDR", "localhost:8181");
        var policyFile = env.getOrDefault("POLICY_FILE", "./opa/src/main/resources/query/selectivity_1.rego");

        var url = new URL("http://" + pdpAddr + "/v1/policies/account-state");
        var httpCon = (HttpURLConnection) url.openConnection();
        httpCon.setDoOutput(true);
        httpCon.setRequestMethod("PUT");
        var out = new OutputStreamWriter(
            httpCon.getOutputStream()
        );

        var path = Path.of(policyFile);
        System.out.println("policy file: " + path.toString());
        var policies = Files.readString(path);

        out.write(policies);
        out.close();

        var statusCode = httpCon.getResponseCode();
        if (statusCode == 200) {
            System.out.println("successfully pushed rego policies" + policyFile);
            return;
        }
        System.out.println("failed to push policies:");
        System.out.println("headers:");
        System.out.println(httpCon.getHeaderFields());


        var in = httpCon.getErrorStream();
        if (in != null) {
            var response = in.readAllBytes();
            var body = new String(response, StandardCharsets.UTF_8);
            System.out.println("body:");
            System.out.println(body);
        }
    }

    public static void pushHardCodedPolicy(Map<String, String> env) throws Exception {
        var policyName = env.getOrDefault("POLICY_NAME", "selectivity_1");
        var pdpAddr = env.getOrDefault("PDP_ADDR", "localhost:8070");
        var url = new URL("http://" + pdpAddr + "/policy?name=" + policyName);

        System.out.println("url: " + url.toString());

        var httpCon = (HttpURLConnection) url.openConnection();
        httpCon.setDoOutput(false);
        httpCon.setRequestMethod("GET");

        var code = httpCon.getResponseCode();
        if (code == 200) {
            System.out.println("successfully pushed java policies");
            return;
        }

        System.out.println("failed to push policies");
        System.out.println("code: " + code);
        System.out.println("headers:");
        System.out.println(httpCon.getHeaderFields());

        var in = httpCon.getErrorStream();
        if (in != null) {
            var response = in.readAllBytes();
            var body = new String(response, StandardCharsets.UTF_8);
            System.out.println("body:");
            System.out.println(body);
        }
    }
}
