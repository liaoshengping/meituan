package main

import (
	"bytes"
	"encoding/json"
	"fmt"
	"io/ioutil"
	"net/http"
	"net/url"
)

// MeiTuanClient 美团核销接口客户端
type MeiTuanClient struct {
	BaseURL   string
	APIKey    string
	AccountID string
}

// NewMeiTuanClient 创建一个新的美团核销接口客户端
func NewMeiTuanClient(baseURL, apiKey, accountID string) *MeiTuanClient {
	return &MeiTuanClient{
		BaseURL:   baseURL,
		APIKey:    apiKey,
		AccountID: accountID,
	}
}

// GetShopList 获取门店列表
func (c *MeiTuanClient) GetShopList() (map[string]interface{}, error) {
	params := url.Values{}
	params.Add("apikey", c.APIKey)
	params.Add("account_id", c.AccountID)

	return c.sendRequest("/api/OnlineAccountApi/shopList", params)
}

// PreVerification 预核销
func (c *MeiTuanClient) PreVerification(code string) (map[string]interface{}, error) {
	params := url.Values{}
	params.Add("apikey", c.APIKey)
	params.Add("account_id", c.AccountID)
	params.Add("code", code)

	return c.sendRequest("/api/OnlineAccountApi/productList", params)
}

// VerifyTicket 验证券
func (c *MeiTuanClient) VerifyTicket(code, shopID string) (map[string]interface{}, error) {
	params := url.Values{}
	params.Add("apikey", c.APIKey)
	params.Add("account_id", c.AccountID)
	params.Add("code", code)
	params.Add("shop_id", shopID)

	return c.sendRequest("/api/OnlineAccountApi/prepare", params)
}

// sendRequest 发送 POST 请求
func (c *MeiTuanClient) sendRequest(path string, params url.Values) (map[string]interface{}, error) {
	url := c.BaseURL + path
	resp, err := http.PostForm(url, params)
	if err != nil {
		return nil, err
	}
	defer resp.Body.Close()

	body, err := ioutil.ReadAll(resp.Body)
	if err != nil {
		return nil, err
	}

	var result map[string]interface{}
	err = json.Unmarshal(body, &result)
	if err != nil {
		return nil, err
	}

	return result, nil
}

func main() {
	baseURL := "https://your-api-base-url" // 替换为实际的 API 基础 URL
	apiKey := "your-apikey"                // 替换为实际的 API Key
	accountID := "your-account-id"         // 替换为实际的账户 ID

	client := NewMeiTuanClient(baseURL, apiKey, accountID)

	// 获取门店列表
	shopList, err := client.GetShopList()
	if err != nil {
		fmt.Println("获取门店列表失败:", err)
	} else {
		fmt.Println("门店列表:", shopList)
	}

	// 预核销
	code := "your-verification-code" // 替换为实际的核销码
	preVerificationResult, err := client.PreVerification(code)
	if err != nil {
		fmt.Println("预核销失败:", err)
	} else {
		fmt.Println("预核销结果:", preVerificationResult)
	}

	// 验证券
	shopID := "your-shop-id" // 替换为实际的门店 ID
	verifyTicketResult, err := client.VerifyTicket(code, shopID)
	if err != nil {
		fmt.Println("验证券失败:", err)
	} else {
		fmt.Println("验证券结果:", verifyTicketResult)
	}
}
