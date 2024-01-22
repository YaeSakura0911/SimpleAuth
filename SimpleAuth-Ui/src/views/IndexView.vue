<script setup>
import {h, onBeforeMount, reactive, ref, resolveComponent} from "vue";
import {useRouter, RouterLink} from "vue-router";
import {logout} from "@/apis/authentication";
import {message} from "ant-design-vue";
import {useUserStore} from "@/stores/user";
import {getPermissionBySession} from "@/apis/permission";

const router = useRouter()
const user = useUserStore()
const collapsed = ref(false)
const collapsedWidth = ref(80)
const openKey = ref([])
const selectedKey = ref([])
const sidebarMenu = ref([
    {
        key: 'home',
        icon: () => h(resolveComponent('HomeOutlined')),
        label: h(RouterLink, {to: '/home'}, () => '主页'),
        title: '主页',
    }
    // {
    //     key: 'system',
    //     icon: () => h(resolveComponent('SettingOutlined')),
    //     label: '系统设置',
    //     title: '系统设置',
    //     children: [
    //         {
    //             key: 'user',
    //             label: h(RouterLink, {to: {name: 'User'}}, () => '用户管理'),
    //         },
    //         {
    //             key: 'role',
    //             label: h(RouterLink, {to: {name: 'Role'}}, () => '角色管理'),
    //         },
    //         {
    //             key: 'permission',
    //             label: h(RouterLink, {to: {name: 'Permission'}}, () => '权限管理'),
    //         },
    //         {
    //             key: 'log',
    //             label: '日志管理',
    //             children: [
    //                 {
    //                     key: 'loginLog',
    //                     label: h(RouterLink, {to: {name: 'LoginLog'}}, () => '登录日志'),
    //                 },
    //             ]
    //         }
    //     ]
    // }
])

onBeforeMount(() => {
    // 动态生成菜单
    getPermissionBySession().then(resp => {
        generateDynamicMenu(resp.data.permissions, null).forEach(dynamicMenu => {
            sidebarMenu.value.push(dynamicMenu)
        })
    })
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

/**
 * 动态生成菜单
 * @param dataArray 数据数组
 * @param parent 父级ID
 */
function generateDynamicMenu(dataArray, parent = null) {
    const result = [];

    for (const data of dataArray) {
        if (data.parent === parent) {
            const menuItem = {
                key: data.id,
                icon: data.icon ? () => h(resolveComponent(data.icon)) : null,
                label: data.component ? h(RouterLink, {to: {name: data.name}}, () => data.title) : data.title,
                title: data.title,
                children: generateDynamicMenu(dataArray, data.id)
            };
            result.push(menuItem);
        }
    }

    if (result.length === 0) {
        return null
    } else {
        return result;
    }
}
</script>

<template>
    <a-layout style="min-height: 100vh;" has-sider>
        <!-- 侧边栏菜单 开始 -->
        <a-layout-sider breakpoint="md" :collapsed-width="collapsedWidth" :collapsed="collapsed" :trigger="null" @breakpoint="handleBreakPoint" collapsible>
            <div class="logo">
                <div></div>
            </div>

            <a-menu theme="dark" mode="inline" :items="sidebarMenu" :open-keys="openKey" :selected-keys="selectedKey" @openChange="handleMenuOpenChange" @select="handleMenuSelect"></a-menu>
        </a-layout-sider>
        <!-- 侧边栏菜单 结束 -->

        <a-layout>
            <!---->
            <a-layout-header style="background-color: #fff; padding: 0">
                <a-flex justify="space-between">
                    <div>
                        <MenuUnfoldOutlined class="trigger" v-if="collapsed" @click="() => collapsed = !collapsed" />
                        <MenuFoldOutlined class="trigger" v-else @click="() => collapsed = !collapsed" />
                    </div>

                    <div>
                        <a-dropdown class="dropdown" :trigger="['click']">
                            <a-space>
                                <a-avatar size="large">{{ user.name }}</a-avatar>
                                <a-typography-text type="secondary">{{ user.name }}</a-typography-text>
                            </a-space>

                            <template #overlay>
                                <a-menu>
                                    <a-menu-item>
                                        <a-typography-text style="text-align: center" @click="handleLogout">
                                            <LogoutOutlined />
                                            退出登录
                                        </a-typography-text>
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