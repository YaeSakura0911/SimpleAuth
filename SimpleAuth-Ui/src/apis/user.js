import {request} from "@/utils/request";

/**
 * 根据Session获取当前登录用户信息
 */
export function getUserBySession() {
    return request.get('/user')
}