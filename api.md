# API 接口说明

## 公共部分

### 请求

1. 除非特殊说明，所有接口都同时支持 POST/GET 方法
2. 除非特殊说明，请求参数既可以包含在 URL 中，也可以包含在 POST 请求体中
3. 除非特殊说明，所有的接口都需要登录态鉴权，否则返回 `HTTP 403`
4. 当接口返回 HTTP Code 不是 2xx 时，可以在请求体 message 字段取得错误详细信息
    > 以下为未鉴权请求接口的示例
    ```
    Request Method: GET
    Status Code: 403 FORBIDDEN
   
   {"message":"未登录"}
   ```
   
### 消息

#### 推送消息

- 此接口无需登录态鉴权
- endpoint: `/message/push`
- 参数:

| 字段        | 类型     | 必须？ | 默认值  | 备注                                        |
|-----------|--------|-----|------|-------------------------------------------|
| pushToken | String | Y   |      | 推送消息时使用的token                             |
| text      | String | Y   |      | 消息内容，特别地，若消息类型为`image`，则为图片链接             |
| type      | String | N   | text | 消息类型，取值为`text`（文本）/`image`（图片）/`markdown` |
| title     | String | N   |      | 消息标题                                      |

- 返回
  - 正常返回 
    - HTTP Code: `200 OK`
    - 内容
        ```json5
        {
            "message": "", // 错误内容，正常返回时为空
             "data": {
                 "failedCount": 0, // 推送失败的设备数量
                 "messageId": 47, // 消息 id
                 "pushedAt": 1695693736219 // 消息推送时间
            }
        }
        ```
      
其余接口文档正在完善