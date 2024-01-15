<script setup>
import {computed, h, onMounted, reactive, ref, watch} from "vue";
import {useRouter, RouterLink, useRoute} from "vue-router";
import {logout} from "@/apis/authentication";
import {message} from "ant-design-vue";
import { BookOutlined, HomeOutlined, SettingOutlined, MenuUnfoldOutlined, MenuFoldOutlined, LogoutOutlined } from "@ant-design/icons-vue"

const route = useRoute()
const router = useRouter()
const collapsed = ref(false)
const collapsedWidth = ref(80)
const openKey = ref([])
const selectedKey = ref([])
const breadcrumb = ref({
    routes: [],
    itemRender: ({route}) => {
        return h('span', route.breadcrumbName)
    }
})
const menuItems = reactive([
    {
        key: 'home',
        icon: () => h(HomeOutlined),
        label: h(RouterLink, {to: '/home'}, () => '主页'),
        title: '主页',
    },
    {
        key: 'system',
        icon: () => h(SettingOutlined),
        label: '系统设置',
        title: '系统设置',
        children: [
            {
                key: 'user',
                label: h(RouterLink, {to: '/user'}, () => '用户管理'),
            },
            {
                key: 'role',
                label: h(RouterLink, {to: '/role'}, () => '角色管理'),
            },
            {
                key: 'permission',
                label: h(RouterLink, {to: ''}, () => '权限管理'),
            },
            {
                key: 'log',
                label: '日志管理',
                children: [
                    {
                        key: 'loginLog',
                        label: h(RouterLink, {to: '/login-log'}, () => '登录日志'),
                    },
                ]
            }
        ]
    }
])

/**
 * 监听路由变化
 */
watch(() => route.matched, (newValue) => {
    console.log(newValue)
    breadcrumb.value.routes = []
    for (let routeItem of newValue) {
        // 如果路径为主页则不添加进面包屑
        if (routeItem.path === '/home') {
            continue
        }
        breadcrumb.value.routes.push({breadcrumbName: routeItem.meta.title})
    }
})

function handleMenuSelect(selectedItem) {
    // console.log(selectedItem)
    selectedKey.value = selectedItem.selectedKeys
}

function handleMenuOpenChange(openKeys) {
    // console.log(openKeys)
    openKey.value = openKeys
}

function handleBreakPoint(broken) {
    if (broken) {
        collapsedWidth.value = 0
    }
    else {
        collapsedWidth.value = 80
    }
}

function handleLogout() {
    logout().then(() => {
        router.push('/login')
        message.success('注销成功')
    })
}
</script>

<template>
    <a-layout style="min-height: 100vh;" has-sider>
        <!-- 侧边栏菜单 开始 -->
        <a-layout-sider breakpoint="md" :collapsed-width="collapsedWidth" :collapsed="collapsed" :trigger="null" @breakpoint="handleBreakPoint" collapsible>
            <div class="logo">
                <div></div>
            </div>

            <a-menu theme="dark" mode="inline" :items="menuItems" :open-keys="openKey" :selected-keys="selectedKey" @openChange="handleMenuOpenChange" @select="handleMenuSelect"></a-menu>
        </a-layout-sider>
        <!-- 侧边栏菜单 结束 -->

        <a-layout>
            <!---->
            <a-layout-header style="background-color: #fff; padding: 0">
                <a-flex justify="space-between">
                    <div>
                        <menu-unfold-outlined class="trigger" v-if="collapsed" @click="() => collapsed = !collapsed" />
                        <menu-fold-outlined class="trigger" v-else @click="() => collapsed = !collapsed" />
                    </div>

                    <div>
                        <a-dropdown class="dropdown" :trigger="['click']">
                            <a-space>
                                <a-avatar size="large">A</a-avatar>
                                <a-typography-text type="secondary">Admin</a-typography-text>
                            </a-space>

                            <template #overlay>
                                <a-menu>
                                    <a-menu-item>
                                        <a @click="handleLogout">
                                            <logout-outlined />
                                            退出登录</a>
                                    </a-menu-item>
                                </a-menu>
                            </template>
                        </a-dropdown>
                    </div>
                </a-flex>
            </a-layout-header>
            <!---->

            <!---->
            <a-layout-content>
                <a-page-header :title="router.currentRoute.value.meta.title" :ghost="false" :breadcrumb="breadcrumb" />

                <router-view />
            </a-layout-content>
            <!---->

            <!---->
            <a-layout-footer style="text-align: center">
                <a-typography-text>SimpleAuth ©2023 Create by YaeSakura</a-typography-text>
            </a-layout-footer>
            <!---->
        </a-layout>
    </a-layout>
</template>

<style scoped>
.logo {
    height: 64px;
    padding: 12px;
}

.logo div {
    height: 100%;
    width: 100%;
    background-color: #334454;
}

.trigger {
    font-size: 18px;
    line-height: 64px;
    padding: 0 24px;
    transition: color 300ms
}

.trigger:hover {
    color: #1890ff;
}

.dropdown {
    padding: 0 24px;
    transition: background-color 300ms;
}

.dropdown:hover {
    cursor: pointer;
    background-color: rgba(0, 0, 0, 0.06);
}
</style>