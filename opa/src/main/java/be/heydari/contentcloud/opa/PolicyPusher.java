package be.heydari.contentcloud.opa;

import java.io.File;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public class PolicyPusher {
    public static void main(String[] args) throws Exception {
        var env = System.getenv();
        var opaAddr = env.getOrDefault("OPA_ADDR", "localhost:8181");
        var policyFile = env.getOrDefault("POLICY_FILE", "./opa/src/main/resources/postfilter/selectivity_10.rego");

        var url = new URL("http://" + opaAddr + "/v1/policies/account-state");
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
            System.out.println("successfully pushed policies");
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
}
