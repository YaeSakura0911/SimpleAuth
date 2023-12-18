<script setup>
import {reactive, ref} from "vue";

const buttonLoading = ref(false)
const registerFormRef = ref()
const registerForm = reactive({
    username: '',
    password: '',
    confirmPassword: '',
    email: ''
})
const validateRules = {
    username: [
        {required: true, message: '请输入用户名！', trigger: 'blur'},
        {validator: checkUsernameExist, trigger: 'blur'}
    ],
    password: [
        {required: true, message: '请输入密码！', trigger: 'blur'},
        {min: 8, message: '最少8位字符！', trigger: 'blur'}
    ],
    confirmPassword: [
        {validator: checkConfirmPassword, trigger: 'blur'}
    ],
    email: [
        {required: true, message: '请输入电子邮件！', trigger: 'blur'},
        {type: 'email', message: '请输入有效的邮箱地址。', trigger: 'blur'}
    ]
}

function handleFinish() {
    buttonLoading.value = true
}

/**
 * 自定义校验规则 - 验证用户是否存在
 * @param value
 */
async function checkUsernameExist(_rule, value) {
    console.log(_rule.validateStatus, value)

    // TODO: 从后台获取用户名
    if (value === 'admin') {
        return Promise.reject('用户名已存在！')
    }
    return Promise.resolve()
}

/**
 * 自定义校验规则
 * @param value
 */
async function checkConfirmPassword(_rule, value) {
    console.log(_rule, value)
    if (value !== registerForm.password) {
        return Promise.reject('两次密码不一致！')
    }
    return Promise.resolve()
}
</script>

<template>
    <a-layout>
        <a-layout-content>
            <a-row justify="center" align="middle" style="min-height: 100vh">
                <a-col :span="5">
                    <a-card>
                        <a-typography-title :level="3">SimpleAuth</a-typography-title>

                        <br/>

                        <a-form class="register-form"
                                layout="vertical"
                                ref="registerFormRef"
                                :model="registerForm"
                                :rules="validateRules"
                                @finish="handleFinish">
                            <a-form-item name="username" label="用户名">
                                <a-input v-model:value="registerForm.username"/>
                            </a-form-item>
                            <a-form-item name="password" label="密码">
                                <a-input-password v-model:value="registerForm.password"/>
                            </a-form-item>
                            <a-form-item name="confirmPassword" label="确认密码">
                                <a-input-password v-model:value="registerForm.confirmPassword"/>
                            </a-form-item>
                            <a-form-item name="email" label="电子邮件">
                                <a-input v-model:value="registerForm.email"/>
                            </a-form-item>
                            <a-form-item>
                                <a-space direction="vertical" style="width: 100%">
                                    <a-button type="primary" html-type="submit" :loading="buttonLoading" block>注册</a-button>
                                    <router-link to="/login">
                                        <a-button block>登录</a-button>
                                    </router-link>
                                </a-space>
                            </a-form-item>
                        </a-form>
                    </a-card>
                </a-col>
            </a-row>
        </a-layout-content>
    </a-layout>
</template>

<style scoped>
.register-form {
    width: 100%;
}
</style>