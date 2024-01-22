import {createRouter, createWebHistory} from 'vue-router'
import LoginView from "@/views/LoginView.vue";
import ForgetView from "@/views/ForgetView.vue";
import RegisterView from "@/views/RegisterView.vue";
import IndexView from "@/views/IndexView.vue";
import {useUserStore} from "@/stores/user";

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
                requireAuth: false
            }
        },
        {
            path: '/register',
            name: 'Register',
            component: RegisterView,
            meta: {
                title: '注册',
                requireAuth: false
            }
        },
        {
            path: '/reset-password',
            name: 'ResetPassword',
            component: () => import('@/views/ResetPasswordView.vue'),
            meta: {
                title: '重置密码',
                requireAuth: false
            }
        },
        {
            path: '/reset-password-success',
            name: 'ResetPasswordSuccess',
            component: () => import('@/views/ResetSuccess.vue'),
            meta: {
                title: '重置密码成功',
                requireAuth: false
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
                        requireAuth: true
                    }
                },
                {
                    path: 'system',
                    name: 'System',
                    meta: {
                        title: '系统设置',
                        requireAuth: true
                    },
                    children: [
                        {
                            path: 'user',
                            name: 'User',
                            component: () => import('@/views/system/UserView.vue'),
                            meta: {
                                title: '用户管理',
                                requireAuth: true
                            }
                        },
                        {
                            path: 'role',
                            name: 'Role',
                            component: () => import('@/views/system/RoleView.vue'),
                            meta: {
                                title: '角色管理',
                                requireAuth: true
                            }
                        },
                        {
                            path: 'permission',
                            name: 'Permission',
                            meta: {
                                title: '权限管理',
                                requireAuth: true
                            }
                        },
                        {
                            path: 'log',
                            name: 'Log',
                            children: [
                                {
                                    path: 'login-log',
                                    name: 'LoginLog',
                                    component: () => import('@/views/system/LoginLogView.vue'),
                                    meta: {
                                        title: '登录日志',
                                        requireAuth: true
                                    }
                                }
                            ]
                        },
                    ]
                },

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

    // 如果目的地需要认证
    if (to.meta.requireAuth) {
        // 获取用户信息
        user.getUser()
    }

    // 如果不存在指定路由
    if (!router.hasRoute(to.name)) {
        // TODO: 生成动态路由
        user.generateRoutes()
    }
})

router.afterEach((to, from) => {
    const routeTitle = to.meta.title;
    const originTitle = document.title.split(' | ').slice(-1);
    document.title = routeTitle + ' | ' + originTitle
})

export default router
