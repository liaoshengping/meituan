using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;

namespace MeiTuanVerificationSDK
{
    public class MeiTuanVerificationClient
    {
        private readonly string _baseUrl = "https://your-api-base-url"; // 需要替换为实际的基础 URL
        private readonly string _apiKey;
        private readonly string _accountId;
        private readonly HttpClient _httpClient;

        public MeiTuanVerificationClient(string apiKey, string accountId)
        {
            _apiKey = apiKey;
            _accountId = accountId;
            _httpClient = new HttpClient();
        }

        // 获取门店列表
        public async Task<Dictionary<string, object>> GetShopList()
        {
            var url = _baseUrl + "/api/OnlineAccountApi/shopList";
            var parameters = new Dictionary<string, string>
            {
                { "apikey", _apiKey },
                { "account_id", _accountId }
            };
            return await SendPostRequest(url, parameters);
        }

        // 预核销
        public async Task<Dictionary<string, object>> PreVerification(string code)
        {
            var url = _baseUrl + "/api/OnlineAccountApi/productList";
            var parameters = new Dictionary<string, string>
            {
                { "apikey", _apiKey },
                { "account_id", _accountId },
                { "code", code }
            };
            return await SendPostRequest(url, parameters);
        }

        // 验证券
        public async Task<Dictionary<string, object>> VerifyTicket(string code, string shopId)
        {
            var url = _baseUrl + "/api/OnlineAccountApi/prepare";
            var parameters = new Dictionary<string, string>
            {
                { "apikey", _apiKey },
                { "account_id", _accountId },
                { "code", code },
                { "shop_id", shopId }
            };
            return await SendPostRequest(url, parameters);
        }

        // 发送 POST 请求
        private async Task<Dictionary<string, object>> SendPostRequest(string url, Dictionary<string, string> parameters)
        {
            var content = new FormUrlEncodedContent(parameters);
            content.Headers.ContentType = new MediaTypeHeaderValue("application/x-www-form-urlencoded");

            var response = await _httpClient.PostAsync(url, content);
            response.EnsureSuccessStatusCode();

            var responseBody = await response.Content.ReadAsStringAsync();
            return JsonConvert.DeserializeObject<Dictionary<string, object>>(responseBody);
        }
    }

    class Program
    {
        static async Task Main()
        {
            string apiKey = "your-apikey"; // 需要替换为实际的 apikey
            string accountId = "your-account-id"; // 需要替换为实际的 account_id
            var client = new MeiTuanVerificationClient(apiKey, accountId);

            try
            {
                // 获取门店列表
                var shopList = await client.GetShopList();
                Console.WriteLine("Shop List: " + JsonConvert.SerializeObject(shopList));

                // 预核销
                string code = "your-verification-code"; // 需要替换为实际的核销码
                var preVerificationResult = await client.PreVerification(code);
                Console.WriteLine("Pre-verification Result: " + JsonConvert.SerializeObject(preVerificationResult));

                // 验证券
                string shopId = "your-shop-id"; // 需要替换为实际的门店 ID
                var verifyTicketResult = await client.VerifyTicket(code, shopId);
                Console.WriteLine("Verify Ticket Result: " + JsonConvert.SerializeObject(verifyTicketResult));
            }
            catch (Exception ex)
            {
                Console.WriteLine("Error: " + ex.Message);
            }
        }
    }
}
