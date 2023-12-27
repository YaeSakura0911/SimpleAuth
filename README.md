# SimpleAuth

## 基本配置文件

application.yml

```yaml
spring:
  data:
    # Redis配置
    redis:
      host: # Redis连接地址
      password: # Redis连接密码
  # 数据库配置
  datasource:
    username: # 数据库连接用户名
    password: # 数据库连接密码
    url: # 数据库连接地址
    driver-class-name: # 数据库连接驱动
  mail:
    host: # 邮件服务器地址
    username: # 电子邮箱
    password: # POP3/IMAP/SMTP/Exchange/CardDAV 授权码
    test-connection: true 
    properties: # 额外参数

# 阿里云配置
aliyun:
  endpoint: dysmsapi.aliyuncs.com
  access-key-id: 
  access-key-secret:

# 软件全局配置
simple-auth:
  config:
    code-length: # 短信验证码长度（4-6位，小于4时按4算，大于6时按6算）
    email-code-expire: # 邮件验证码过期时间（单位：分钟）
```