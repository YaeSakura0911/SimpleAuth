import {createRouter, createWebHistory} from 'vue-router'
import LoginView from "@/views/LoginView.vue";
import ForgetView from "@/views/ForgetView.vue";
import RegisterView from "@/views/RegisterView.vue";
import IndexView from "@/views/IndexView.vue";
import {useUserStore} from "@/stores/user";
import {getUserBySession} from "@/apis/user";

const modules = import.meta.glob('../views/**/*.vue')
// 路由白名单
const routeWhitelist = ['/login', '/register', '/forget']

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/login',
            name: 'Login',
            component: LoginView,
            meta: {
                title: '登录',
                requireAuth: false
            }
        },
        {
            path: '/forget',
            name: 'Forget',
            component: ForgetView,
            meta: {
                title: '忘记密码',
            }
        },
        {
            path: '/register',
            name: 'Register',
            component: RegisterView,
            meta: {
                title: '注册',
            }
        },
        {
            path: '/reset-password',
            name: 'ResetPassword',
            component: () => import('@/views/ResetPasswordView.vue'),
            meta: {
                title: '重置密码',
            }
        },
        {
            path: '/reset-password-success',
            name: 'ResetPasswordSuccess',
            component: () => import('@/views/ResetSuccess.vue'),
            meta: {
                title: '重置密码成功',
            }
        },
        {
            path: '',
            name: 'Index',
            component: IndexView,
            meta: {
                title: '主页'
            },
            children: [
                {
                    path: 'home',
                    name: 'Home',
                    component: () => import('@/views/HomeView.vue'),
                    meta: {
                        title: '主页',
                    }
                },
                // {
                //     path: 'system',
                //     name: 'System',
                //     meta: {
                //         title: '系统设置',
                //     },
                //     children: [
                //         {
                //             path: 'user',
                //             name: 'User',
                //             component: () => import('@/views/system/UserView.vue'),
                //             meta: {
                //                 title: '用户管理',
                //             }
                //         },
                //         {
                //             path: 'role',
                //             name: 'Role',
                //             component: () => import('@/views/system/RoleView.vue'),
                //             meta: {
                //                 title: '角色管理',
                //             }
                //         },
                //         {
                //             path: 'permission',
                //             name: 'Permission',
                //             meta: {
                //                 title: '权限管理',
                //             }
                //         },
                //         {
                //             path: 'log',
                //             name: 'Log',
                //             children: [
                //                 {
                //                     path: 'login-log',
                //                     name: 'LoginLog',
                //                     component: () => import('@/views/system/LoginLogView.vue'),
                //                     meta: {
                //                         title: '登录日志',
                //                     }
                //                 }
                //             ]
                //         },
                //     ]
                // },
            ]
        },
        // {
        //     path: '/404',
        //     name: '404',
        //     component: () => import('@/views/NotFoundView.vue'),
        //     meta: {
        //         title: '404'
        //     }
        // },
        // {
        //     path: '/:pathMatch(.*)*',
        //     redirect: '/404'
        // },
    ]
})

router.beforeEach(async (to, from) => {
    const user = useUserStore()

    // 目的地是否在白名单里
    if (routeWhitelist.indexOf(to.path) === -1) {
        // 获取用户信息
        await getUserBySession().then(resp => {
            if (resp.code === 200) {
                const respData = resp.data
                user.$patch({
                    id: respData.id,
                    name: respData.name,
                    routes: generateDynamicRoute(respData.permissions),
                    authorizations: generateDynamicAuthorizations(respData.permissions),
                    isAuthentication: true
                })
            }
        })

        // 是否已认证
        if (user.isAuthentication) {
            // 目的地是否存在
            if (!router.hasRoute(to.name)) {
                // 是否存在路由信息
                if (user.routes.length === 0) {
                    user.getRoutes()
                }

                // 生成动态路由
                user.routes.forEach(route => {
                    router.addRoute('Index', route)
                })

                return to.fullPath
            }
        } else {
            // 跳转去登录页
            return ('/login')
        }
    }
})

router.afterEach((to, from) => {
    const routeTitle = to.meta.title;
    const originTitle = document.title.split(' | ').slice(-1);
    document.title = routeTitle + ' | ' + originTitle
})

export default router

/**
 * 生成动态路由
 */
function generateDynamicRoute(dataArray, parent = null) {
    const routes = []

    for (const data of dataArray) {
        if (data.parent === parent) {
            const route = {
                path: data.path,
                name: data.name,
                component: data.component ? modules[`../views${data.component}.vue`] : null,
                meta: {
                    title: data.title,
                },
                children: generateDynamicRoute(dataArray, data.id)
            }
            routes.push(route)
        }
    }

    if (routes.length === 0) {
        return null
    }
    else {
        return routes
    }
}

/**
 * 生成动态权限
 */
function generateDynamicAuthorizations(data) {
    let permissions = []
    data.forEach(item => permissions.push(item.code))
    return permissions
}