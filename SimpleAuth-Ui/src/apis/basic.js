import {request} from "@/utils/request";
import axios from "axios";

/**
 * 登录
 * @param form 登录表单
 */
export function login(form) {
    return request.post('/login', form)
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
    return request.post('/logout')
}