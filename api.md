# API 接口说明

## 公共部分

### 请求

除特殊说明，所有接口请求均为 GraphQL，终结点为 `/graphql`

### 消息

#### 推送消息

此接口可以使用 GraphQL 或 Rest 请求

##### 参数列表

| 字段        | 类型      | 必须？ | 默认值    | 备注                                                |
|-----------|---------|-----|--------|---------------------------------------------------|
| pushToken | String! | Y   |        | 推送消息时使用的token                                     |
| text      | String! | Y   |        | 消息内容，特别地，若消息类型为`image`，则为图片链接，长度限制为 16,777,215 字节 |
| type      | String  | N   | text   | 消息类型，取值为`text`（文本）/`image`（图片）/`markdown`         |
| title     | String  | N   |        | 消息标题                                              |
| topicId   | String  | N   | *null* | 要推送到的话题，若不存在则推送到默认话题                              |

#### GraphQL 请求

- 请求示例
    - 查询请求
      ```graphql
      mutation($param:PushMessageParams!){
          pushMessage(params:$param){
              messageId # 消息 id
              pushedAt # 消息推送时间
              failedCount # 推送失败的设备数量  
          }
      }
      ```
    - 请求参数
      ```json
      {
          "param":{
              "pushToken":"123456",
              "text":"测试"
          }
      }
      ```
- 响应示例
- 成功
  ```json5
  {
      "data": {
         "failedCount": 0, // 推送失败的设备数量  
         "messageId": 47, // 消息 id
         "pushedAt": 1695693736219 // 消息推送时间
      }
  }
  ```
    - 失败
      ```json5
        {
            "errors": [
                {
                    "message": "push token does not exists", // 错误消息
                    "locations": [],
                    "extensions": {
                        "classification": "DataFetchingException"
                    }
                }
            ],
            "data": {
                "pushMessage": null
            }
        }
      ```

#### Rest 请求

- endpoint: `/message/push`

    - 返回
        - 成功
            - HTTP Code: `200 OK`
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
        - 失败
            - HTTP Code: `400 Bad Request`
              ```json5
                {
                  "data": null,
                  "message": "Push token does not exists" // 错误消息
                }
              ```

其余接口文档正在完善