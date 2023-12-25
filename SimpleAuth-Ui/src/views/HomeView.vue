<script setup>
import {useRouter} from "vue-router";
import {logout} from "@/apis/authentication";
import {message} from "ant-design-vue";

const router = useRouter()

function handleLogout() {
    logout().then(() => {
        router.push('/login')
        message.success('注销成功')
    })
}
</script>

<template>
    <a-layout>
        主页
        <a-button @click="handleLogout">
            注销
        </a-button>
        <router-view />
    </a-layout>
</template>

<style scoped>

</style>