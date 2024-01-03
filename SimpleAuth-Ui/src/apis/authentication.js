import {request} from "@/utils/request";
import axios from "axios";

/**
 * 密码登录
 * @param form 密码登录表单
 */
export function passwordLogin(form) {
    return request.post('/auth/login', form)
}

/**
 * 验证码登录
 * @param form 验证码登录表单
 */
export function codeLogin(form) {
    return request.post('/auth/login/code', form)
}

export function githubOAuth(form) {
    return axios.post('https://github.com/login/oauth/access_token', form)
}

export function googleOAuth(form) {
    return axios.get('https://accounts.google.com/o/oauth2/v2/auth', {params: form})
}

/**
 * 注销
 */
export function logout() {
    return request.post('/auth/logout')
}

/**
 * 用户名注册
 * @param form 用户名注册表单
 */
export function usernameRegister(form) {
    return request.post('/auth/register/username', form)
}

/**
 * 邮箱注册
 * @param form 邮箱注册表单
 */
export function emailRegister(form) {
    return request.post('/auth/register/email', form)
}

/**
 * 手机号注册
 * @param form 手机号注册表单
 */
export function phoneRegister(form) {
    return request.post('/auth/register/phone', form)
}

/**
 * 发送短信验证码
 * @param phone 手机号
 */
export function sendSmsCode(phone) {
    return request.post("/auth/code/sms", {phone: phone})
}

/**
 * 发送邮件验证码
 * @param email 收件人邮箱
 */
export function sendEmailCode(email) {
    return request.post('/auth/code/email', {email: email})
}