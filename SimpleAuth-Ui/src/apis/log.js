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

/**
 * 导出登录日志
 * @param form
 */
export function exportLoginLog(form) {
    return request.get('/log/login/export', {
        params: form,
        paramsSerializer: params => {
            console.log(params)
            console.log(qs.stringify(params, {arrayFormat: 'repeat'}))
            return qs.stringify(params, {arrayFormat: 'repeat'})
        },
        responseType: "blob"
    }).then(resp => {
        console.log(resp)
        const downloadLink = document.createElement('a');
        downloadLink.href = window.URL.createObjectURL(resp)
        downloadLink.setAttribute('download', '登录日志.xlsx')
        document.body.appendChild(downloadLink)
        downloadLink.click()
        document.body.removeChild(downloadLink)
    })
}