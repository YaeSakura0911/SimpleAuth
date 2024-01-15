import {request} from "@/utils/request";
import qs from 'qs'

/**
 * 根据分页查询登录日志
 * @param form
 */
export function getLoginLogByPage(form) {
    return request.get('/log/login/list',
        {
            params: form,
            paramsSerializer: params => {
                console.log(params)
                console.log(qs.stringify(params, {arrayFormat: 'repeat'}))
                return qs.stringify(params, {arrayFormat: 'repeat'})
            }
        })
}