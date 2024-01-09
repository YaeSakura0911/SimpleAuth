import axios from "axios";
import router from "@/router";
import {message} from "ant-design-vue";

export const request = axios.create({
    baseURL: 'http://192.168.1.184:8080',
    withCredentials: true,
    withXSRFToken: true
})

/**
 * 配置请求拦截器
 */
request.interceptors.request.use(config => {
    return config
}, error => {
    return Promise.reject(error)
})

/**
 * 配置响应拦截器
 */
request.interceptors.response.use(response => {
    return response.data
}, error => {
    switch (error.response.status) {
        case 401:
            router.push('/login')
            message.error(error.response.data)
            break
        case 500:
            message.error(error.response.data)
    }

    return Promise.reject(error)
})