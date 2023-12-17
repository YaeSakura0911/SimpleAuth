import {request} from "@/utils/request";

/**
 * 登录
 * @param form 登录表单
 */
export function login(form) {
    return request.post('/login', form)
}

/**
 * 注销
 */
export function logout() {
    return request.post('/logout')
}