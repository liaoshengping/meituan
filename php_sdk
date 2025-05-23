<?php

class MeiTuanVerificationApi {
    private $baseUrl;
    private $apikey;
    private $accountId;

    public function __construct($baseUrl, $apikey, $accountId) {
        $this->baseUrl = $baseUrl;
        $this->apikey = $apikey;
        $this->accountId = $accountId;
    }

    /**
     * 获取门店列表
     */
    public function getShopList() {
        $url = $this->baseUrl . '/api/OnlineAccountApi/shopList';
        $data = [
            'apikey' => $this->apikey,
            'account_id' => $this->accountId
        ];
        return $this->sendPostRequest($url, $data);
    }

    /**
     * 预核销
     */
    public function preVerification($code) {
        $url = $this->baseUrl . '/api/OnlineAccountApi/productList';
        $data = [
            'apikey' => $this->apikey,
            'account_id' => $this->accountId,
            'code' => $code
        ];
        return $this->sendPostRequest($url, $data);
    }

    /**
     * 验证券
     */
    public function verifyTicket($code, $shopId) {
        $url = $this->baseUrl . '/api/OnlineAccountApi/prepare';
        $data = [
            'apikey' => $this->apikey,
            'account_id' => $this->accountId,
            'code' => $code,
            'shop_id' => $shopId
        ];
        return $this->sendPostRequest($url, $data);
    }

    /**
     * 发送POST请求
     */
    private function sendPostRequest($url, $data) {
        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, $url);
        curl_setopt($ch, CURLOPT_POST, 1);
        curl_setopt($ch, CURLOPT_POSTFIELDS, http_build_query($data));
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        $response = curl_exec($ch);
        curl_close($ch);
        return json_decode($response, true);
    }
}

// 使用示例
$baseUrl = 'https://your-api-base-url'; // 替换为实际的API基础URL
$apikey = 'your-apikey'; // 替换为实际的apikey
$accountId = 'your-account-id'; // 替换为实际的account_id

$api = new MeiTuanVerificationApi($baseUrl, $apikey, $accountId);

// 获取门店列表
$shopList = $api->getShopList();
print_r($shopList);

// 预核销
$preVerificationResult = $api->preVerification('your-verification-code');
print_r($preVerificationResult);

// 验证券
$verifyTicketResult = $api->verifyTicket('your-verification-code', 'your-shop-id');
print_r($verifyTicketResult);
