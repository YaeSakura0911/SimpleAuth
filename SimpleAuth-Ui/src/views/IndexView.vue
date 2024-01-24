<script setup>
import {h, onBeforeMount, ref, resolveComponent} from "vue";
import {useRouter, RouterLink} from "vue-router";
import {logout} from "@/apis/authentication";
import {message} from "ant-design-vue";
import {useUserStore} from "@/stores/user";
import {getPermissionBySession} from "@/apis/permission";

const router = useRouter()
const user = useUserStore()
// 侧边栏折叠状态
const collapsed = ref(false)
const collapsedWidth = ref(80)
const openKey = ref([])
const selectedKey = ref([])
// 侧边栏导航菜单
const sidebarMenu = ref([
    {
        key: 'home',
        icon: () => h(resolveComponent('HomeOutlined')),
        label: h(RouterLink, {to: '/home'}, () => '主页'),
        title: '主页',
    }
])

onBeforeMount(() => {
    // 动态生成菜单
    getPermissionBySession().then(resp => {
        sidebarMenu.value.push(...generateDynamicMenu(resp.data.permissions, null))
    })

})

// 处理菜单选择
function handleMenuSelect(selectedItem) {
    // console.log(selectedItem)
    selectedKey.value = selectedItem.selectedKeys
}

function handleMenuOpenChange(openKeys) {
    // console.log(openKeys)
    openKey.value = openKeys
}

// 处理断点变化
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
 * @param parentId 父级ID
 * @param parentPath 父级路径
 */
function generateDynamicMenu(dataArray, parentId = null, parentPath = '') {
    const menu = [];

    for (const data of dataArray) {
        let fullPath = parentPath + '/' + data.path
        if (data.parent === parentId) {
            const menuItem = {
                key: data.id,
                icon: data.icon ? () => h(resolveComponent(data.icon)) : null,
                label: data.component ? h(RouterLink, {to: fullPath}, () => data.title) : data.title,
                title: data.title,
                children: generateDynamicMenu(dataArray, data.id, fullPath)
            };
            menu.push(menuItem);
        }
    }

    if (menu.length === 0) {
        return null
    } else {
        return menu;
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
                                <a-avatar size="large">{{ user.name.charAt(0).toUpperCase() }}</a-avatar>
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
    display: block;
    font-size: 18px;
    line-height: 64px;
    padding: 0 24px;
    transition: color 300ms
}

.trigger:hover {
    //color: #1890ff;
    background-color: rgba(0, 0, 0, 0.06);
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