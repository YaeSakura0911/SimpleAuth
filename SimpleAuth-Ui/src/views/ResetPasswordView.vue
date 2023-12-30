<script setup>
import {reactive} from "vue";
import {useRouter} from "vue-router";

const router = useRouter()
const resetPasswordForm = reactive({
    password: ''
})

/**
 *
 */
function handleResetPassword() {
   // TODO: 跳转去结果页
    router.push('/reset-password-success')
}
</script>

<template>
    <a-layout>
        <a-layout-content>
            <a-row justify="center" align="middle" style="min-height: 100vh">
                <a-col :span="5">
                    <a-card>
                        <a-typography-title :level="3">
                            SimpleAuth
                            <br />
                            <a-typography-title :level="4">重置密码</a-typography-title>
                        </a-typography-title>

                        <br/>

                        <a-form layout="vertical" :model="resetPasswordForm">
                            <a-form-item label="新密码">
                                <a-input v-model:value="resetPasswordForm.password" />
                            </a-form-item>
                            <a-form-item>
                                <a-button type="primary" @click="handleResetPassword" block>重置</a-button>
                            </a-form-item>
                        </a-form>
                    </a-card>
                </a-col>
            </a-row>
        </a-layout-content>
    </a-layout>
</template>

<style scoped>

</style>