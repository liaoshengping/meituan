import requests

class MeiTuanVerificationSDK:
    def __init__(self, base_url, apikey, account_id):
        """
        初始化 SDK 实例
        :param base_url: API 的基础 URL
        :param apikey: API 密钥
        :param account_id: 账户 ID
        """
        self.base_url = base_url
        self.apikey = apikey
        self.account_id = account_id

    def get_shop_list(self):
        """
        获取门店列表
        :return: 响应的 JSON 数据
        """
        url = f"{self.base_url}/api/OnlineAccountApi/shopList"
        data = {
            "apikey": self.apikey,
            "account_id": self.account_id
        }
        response = requests.post(url, data=data)
        return response.json()

    def pre_verification(self, code):
        """
        预核销操作
        :param code: 核销码
        :return: 响应的 JSON 数据
        """
        url = f"{self.base_url}/api/OnlineAccountApi/productList"
        data = {
            "apikey": self.apikey,
            "account_id": self.account_id,
            "code": code
        }
        response = requests.post(url, data=data)
        return response.json()

    def verify_ticket(self, code, shop_id):
        """
        验证券操作
        :param code: 核销码
        :param shop_id: 门店 ID
        :return: 响应的 JSON 数据
        """
        url = f"{self.base_url}/api/OnlineAccountApi/prepare"
        data = {
            "apikey": self.apikey,
            "account_id": self.account_id,
            "code": code,
            "shop_id": shop_id
        }
        response = requests.post(url, data=data)
        return response.json()


# 使用示例
if __name__ == "__main__":
    # 请替换为实际的基础 URL、API 密钥和账户 ID
    base_url = "https://your-api-base-url"
    apikey = "your-apikey"
    account_id = "your-account-id"

    sdk = MeiTuanVerificationSDK(base_url, apikey, account_id)

    # 获取门店列表
    shop_list = sdk.get_shop_list()
    print("门店列表:", shop_list)

    # 预核销
    verification_code = "your-verification-code"
    pre_verification_result = sdk.pre_verification(verification_code)
    print("预核销结果:", pre_verification_result)

    # 验证券
    shop_id = "your-shop-id"
    verify_ticket_result = sdk.verify_ticket(verification_code, shop_id)
    print("验证券结果:", verify_ticket_result)
