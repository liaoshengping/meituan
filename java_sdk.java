import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

// 美团核销接口客户端类
public class MeiTuanVerificationClient {
    private static final String BASE_URL = "https://your-api-base-url"; // 需要替换为实际的基础 URL
    private final String apiKey;
    private final String accountId;
    private final Gson gson;

    public MeiTuanVerificationClient(String apiKey, String accountId) {
        this.apiKey = apiKey;
        this.accountId = accountId;
        this.gson = new Gson();
    }

    // 获取门店列表
    public Map<String, Object> getShopList() throws IOException {
        String url = BASE_URL + "/api/OnlineAccountApi/shopList";
        Map<String, String> params = new HashMap<>();
        params.put("apikey", apiKey);
        params.put("account_id", accountId);
        return sendPostRequest(url, params);
    }

    // 预核销
    public Map<String, Object> preVerification(String code) throws IOException {
        String url = BASE_URL + "/api/OnlineAccountApi/productList";
        Map<String, String> params = new HashMap<>();
        params.put("apikey", apiKey);
        params.put("account_id", accountId);
        params.put("code", code);
        return sendPostRequest(url, params);
    }

    // 验证券
    public Map<String, Object> verifyTicket(String code, String shopId) throws IOException {
        String url = BASE_URL + "/api/OnlineAccountApi/prepare";
        Map<String, String> params = new HashMap<>();
        params.put("apikey", apiKey);
        params.put("account_id", accountId);
        params.put("code", code);
        params.put("shop_id", shopId);
        return sendPostRequest(url, params);
    }

    // 发送 POST 请求
    private Map<String, Object> sendPostRequest(String urlStr, Map<String, String> params) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String, String> param : params.entrySet()) {
            if (postData.length() != 0) {
                postData.append('&');
            }
            postData.append(URLEncoder.encode(param.getKey(), StandardCharsets.UTF_8));
            postData.append('=');
            postData.append(URLEncoder.encode(param.getValue(), StandardCharsets.UTF_8));
        }
        byte[] postDataBytes = postData.toString().getBytes(StandardCharsets.UTF_8);

        try (OutputStream os = connection.getOutputStream()) {
            os.write(postDataBytes);
        }

        int responseCode = connection.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                return gson.fromJson(response.toString(), HashMap.class);
            }
        } else {
            throw new IOException("HTTP request failed with status code: " + responseCode);
        }
    }

    public static void main(String[] args) {
        String apiKey = "your-apikey"; // 需要替换为实际的 apikey
        String accountId = "your-account-id"; // 需要替换为实际的 account_id
        MeiTuanVerificationClient client = new MeiTuanVerificationClient(apiKey, accountId);

        try {
            // 获取门店列表
            Map<String, Object> shopList = client.getShopList();
            System.out.println("Shop List: " + shopList);

            // 预核销
            String code = "your-verification-code"; // 需要替换为实际的核销码
            Map<String, Object> preVerificationResult = client.preVerification(code);
            System.out.println("Pre-verification Result: " + preVerificationResult);

            // 验证券
            String shopId = "your-shop-id"; // 需要替换为实际的门店 ID
            Map<String, Object> verifyTicketResult = client.verifyTicket(code, shopId);
            System.out.println("Verify Ticket Result: " + verifyTicketResult);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
