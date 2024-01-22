<script setup>

</script>

<template>
    <a-layout style="height: 100vh">
        <a-layout-content style="height: 100vh">
            <a-flex justify="center" align="center" style="height: 100vh">
                <a-result status="404" title="404" sub-title="抱歉, 你访问的页面不存在" style="min-height: max-content">
                    <template #icon>

                    </template>
                    <template #extra>
                        <router-link to="/home">
                            <a-button type="primary">返回主页</a-button>
                        </router-link>
                    </template>
                </a-result>
            </a-flex>
        </a-layout-content>
    </a-layout>
</template>

<style scoped>

</style>