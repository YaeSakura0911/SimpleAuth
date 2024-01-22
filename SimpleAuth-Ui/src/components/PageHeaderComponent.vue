<script setup>
import {h, onMounted, ref} from "vue";
import {useRoute, useRouter} from "vue-router";

const route = useRoute()
const router = useRouter()
const breadcrumb = ref({
    routes: [],
    itemRender: ({route}) => {
        return h('span', route.breadcrumbName)
    }
})

onMounted(() => {
    breadcrumb.value.routes = []
    for (let routeItem of route.matched) {
        // 如果路径为主页则不添加进面包屑
        if (routeItem.path === '/home') {
            continue
        }
        breadcrumb.value.routes.push({breadcrumbName: routeItem.meta.title})
    }
})
</script>

<template>
    <a-page-header :title="router.currentRoute.value.meta.title" :ghost="false" :breadcrumb="breadcrumb">
        <template #extra>
            <slot name="extra"></slot>
        </template>
    </a-page-header>
</template>

<style scoped>

</style>