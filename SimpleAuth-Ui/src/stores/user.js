import {defineStore} from "pinia";
import router from "@/router";
import {getUserBySession} from "@/apis/user";
import {h, resolveComponent} from "vue";

export const useUserStore = defineStore('user', {
    state: () => {
        return {
            id: '', // 用户ID
            name: '', // 用户名称
            avatar: '', // 用户头像
            menus: [], // 菜单
            authorizations: [], // 权限
            isAuthentication: false // 是否认证
        }
    },
    actions: {
        /**
         * 获取用户信息
         */
        getUser() {
            getUserBySession().then(resp => {
                console.log(resp)
                if (resp.code === 200) {
                    const respData = resp.data
                    this.$patch({
                        id: respData.id,
                        name: respData.name,
                        // menus: generateDynamicMenu(respData.permissions),
                        // authorizations: filterAsyncPermissions(respData.permissions),
                        isAuthentication: true
                    })
                }
            })
        },
        /**
         * 判断是否有指定权限
         * @param permission 权限
         * @returns Boolean
         */
        hasPermission(permission) {
            return this.authorizations.includes(permission)
        }
    }
})

/**
 * 格式化路由数组
 */
function formatRoutes(routes, parentName = 'Index') {
    if (routes.length === 0) {
        return
    }

    for (let route of routes) {
        console.log(route)
        let r = {
            path: route.path,
            name: route.name,
            component: () => import(`@/views/${route.component}.vue`),
            meta: {
                requireAuth: true,
                title: route.title
            }
        }
        router.addRoute(parentName, r)

        if (route.children != null && route.children.length > 0) {
            formatRoutes(route.children, route.name)
        }
    }
}

function generateDynamicMenu(data) {
    const children = data.filter(child => child.parent === data.id).map(child => generateDynamicMenu(child))
    return {
        key: '',
        icon: () => h(resolveComponent(data.icon)),
        label: h(resolveComponent('RouterLink'), {to: {name: data.name}}, () => {
            data.title
        }),
        title: data.title,
        children: children
    }
}

/**
 * 异步过滤权限
 */
function filterAsyncPermissions(data) {
    let permissions = []
    data.forEach(item => permissions.push(item.code))
    return permissions
}