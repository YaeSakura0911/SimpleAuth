import {request} from "@/utils/request";

/**
 * 根据Session获取权限
 */
export function getPermissionBySession() {
    return request.get('/permission')
}