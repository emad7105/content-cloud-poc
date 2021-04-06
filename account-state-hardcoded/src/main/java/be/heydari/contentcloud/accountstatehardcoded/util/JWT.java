package be.heydari.contentcloud.accountstatehardcoded.util;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class JWT {

    public static final int HEADER = 0;
    public static final int PAYLOAD = 1;
    public static final int SIGNATURE = 2;

    public static Map<String, Object> parse(String token) {
        // three chunks header, payload and signature
        // assume already verified by spring security
        String[] chunks = token.split("\\.");
        assert chunks.length == 3;

        Base64.Decoder decoder = Base64.getDecoder();
        String payload = new String(decoder.decode(chunks[PAYLOAD]));

        Gson gson = new Gson();

        Type mapType = new TypeToken<HashMap<String, Object>>() {}.getType();
        Map<String, Object> object = gson.fromJson(payload, mapType);
        return object;
    }
}
