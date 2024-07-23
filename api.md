# API 接口说明

## 公共部分

### 请求

除特殊说明外，所有接口请求都仅接受 Post 请求，请求类型均为 application/x-www-form-urlencoded，返回都使用 application/json

响应状态码均为字符串，定义参见 controller.response.ResponseErrorCode

### 消息

#### 推送消息

此接口可以使用 GET 或 POST 请求

#### 请求地址

`/api/message/push`

##### 参数列表

| 字段          | 类型     | 必须？ | 默认值    | 备注                                           |
|-------------|--------|-----|--------|----------------------------------------------|
| key         | String | Y   |        | 推送消息时使用的key                                  |
| content     | String | Y   |        | 消息内容，特别地，若消息类型为`image`，则为图片链接，长度限制为 65535 字节 |
| type        | Enum   | N   | text   | 消息类型，取值为`Text`（文本）/`Picture`（图片）/`Markdown`  |
| title       | String | N   |        | 消息标题                                         |
| group       | String | N   | *null* | 要推送到的消息组，若不存在则推送到默认话题                        |
| coverImgUrl | String | N   | *null* | 封面图片                                         |
| priority    | Int    | N   | 5      | 推送消息的优先级                                     |

其余接口文档正在完善