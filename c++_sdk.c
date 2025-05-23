#include <iostream>
#include <string>
#include <curl/curl.h>
#include <nlohmann/json.hpp>

// 回调函数，用于处理响应数据
size_t WriteCallback(void *contents, size_t size, size_t nmemb, std::string *s) {
    size_t newLength = size * nmemb;
    try {
        s->append((char*)contents, newLength);
    } catch(std::bad_alloc &e) {
        return 0;
    }
    return newLength;
}

// 发送 POST 请求
nlohmann::json sendPostRequest(const std::string& url, const std::string& postFields) {
    CURL *curl;
    CURLcode res;
    std::string readBuffer;

    curl = curl_easy_init();
    if(curl) {
        curl_easy_setopt(curl, CURLOPT_URL, url.c_str());
        curl_easy_setopt(curl, CURLOPT_POSTFIELDS, postFields.c_str());
        curl_easy_setopt(curl, CURLOPT_WRITEFUNCTION, WriteCallback);
        curl_easy_setopt(curl, CURLOPT_WRITEDATA, &readBuffer);

        res = curl_easy_perform(curl);
        if(res != CURLE_OK) {
            std::cerr << "curl_easy_perform() failed: " << curl_easy_strerror(res) << std::endl;
        }
        curl_easy_cleanup(curl);
    }

    try {
        return nlohmann::json::parse(readBuffer);
    } catch(const nlohmann::json::parse_error& e) {
        std::cerr << "JSON parse error: " << e.what() << std::endl;
        return nlohmann::json();
    }
}

// 获取门店列表
nlohmann::json getShopList(const std::string& baseUrl, const std::string& apiKey, const std::string& accountId) {
    std::string url = baseUrl + "/api/OnlineAccountApi/shopList";
    std::string postFields = "apikey=" + apiKey + "&account_id=" + accountId;
    return sendPostRequest(url, postFields);
}

// 预核销
nlohmann::json preVerification(const std::string& baseUrl, const std::string& apiKey, const std::string& accountId, const std::string& code) {
    std::string url = baseUrl + "/api/OnlineAccountApi/productList";
    std::string postFields = "apikey=" + apiKey + "&account_id=" + accountId + "&code=" + code;
    return sendPostRequest(url, postFields);
}

// 验证券
nlohmann::json verifyTicket(const std::string& baseUrl, const std::string& apiKey, const std::string& accountId, const std::string& code, const std::string& shopId) {
    std::string url = baseUrl + "/api/OnlineAccountApi/prepare";
    std::string postFields = "apikey=" + apiKey + "&account_id=" + accountId + "&code=" + code + "&shop_id=" + shopId;
    return sendPostRequest(url, postFields);
}

int main() {
    std::string baseUrl = "https://your-api-base-url"; // 替换为实际的基础 URL
    std::string apiKey = "your-apikey"; // 替换为实际的 API Key
    std::string accountId = "your-account-id"; // 替换为实际的账户 ID

    // 获取门店列表
    nlohmann::json shopList = getShopList(baseUrl, apiKey, accountId);
    std::cout << "Shop List: " << shopList.dump(4) << std::endl;

    std::string code = "your-verification-code"; // 替换为实际的核销码
    // 预核销
    nlohmann::json preVerificationResult = preVerification(baseUrl, apiKey, accountId, code);
    std::cout << "Pre-verification Result: " << preVerificationResult.dump(4) << std::endl;

    std::string shopId = "your-shop-id"; // 替换为实际的门店 ID
    // 验证券
    nlohmann::json verifyTicketResult = verifyTicket(baseUrl, apiKey, accountId, code, shopId);
    std::cout << "Verify Ticket Result: " << verifyTicketResult.dump(4) << std::endl;

    return 0;
}
