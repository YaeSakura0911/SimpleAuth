import {defineStore} from "pinia";
import {getUserBySession} from "@/apis/user";

const modules = import.meta.glob('../views/**/*.vue')

export const useUserStore = defineStore('user', {
    state: () => {
        return {
            id: '', // 用户ID
            name: '', // 用户名称
            avatar: '', // 用户头像
            routes: [], // 动态路由
            permissions: [],
            authorizations: [], // 权限
            isAuthentication: false // 是否认证
        }
    },
    actions: {
        /**
         * 获取用户信息
         */
        getUser() {
            getUserBySession().then(async resp => {
                console.log(resp)
                if (resp.code === 200) {
                    const respData = resp.data
                    await this.$patch({
                        id: respData.id,
                        name: respData.name,
                        routes: generateDynamicRoute(respData.permissions),
                        authorizations: generateDynamicAuthorizations(respData.permissions),
                        isAuthentication: true
                    })
                }
            })
        },
        /**
         * 获取路由信息
         */
        getRoutes() {

        },
        /**
         * 判断是否有指定权限
         * @param permission 权限
         * @returns Boolean
         */
        hasPermission(permission) {
            return this.authorizations.includes(permission)
        },
    }
})