<script setup>
import {reactive, ref} from 'vue'
import {GithubOutlined, GoogleOutlined, UserOutlined, LockOutlined, WindowsOutlined} from '@ant-design/icons-vue'
import {googleOAuth, login} from "@/apis/authentication";
import {onBeforeRouteUpdate, useRouter} from "vue-router";
import {message} from "ant-design-vue";

const router = useRouter()
const currentLoginMethod = ref('密码登录')
const codeButtonText = ref('获取验证码')
const showPasswordLogin = ref(true)
const showCodeLogin = ref(false)
const passwordLoginFormRef = ref({})
const passwordLoginForm = reactive({
    username: '',
    password: '',
    remember: false,
});

const codeLoginFormRef = ref({})
const codeLoginForm = reactive({
    emailOrPhone: '',
    code: '',
    remember: false
})

/**
 *
 */
function handleSegmentedChange(title) {
    switch (title) {
        case '密码登录':
            showPasswordLogin.value = true
            showCodeLogin.value = false
            break
        case '验证码登录':
            showPasswordLogin.value = false
            showCodeLogin.value = true
            break
    }
}

/**
 *
 */
function handlePasswordLogin() {
    login(passwordLoginForm).then(() => {
        router.push('/home')
        message.success('登录成功')
    })
}

/**
 *
 */
function handleCodeLogin() {

}

function handleGithubOAuth() {
    window.open('http://127.0.0.1:8080/login/oauth2/github', '_blank', 'width=500, height=500')
}

function handleGoogleOAuth() {
    // const url = 'https://accounts.google.com/o/oauth2/v2/auth'
    // const client_id = '648723572402-842982hcrr4ln1i6gd0lk206lel4h60a.apps.googleusercontent.com'
    // const redirect_uri = 'http://localhost:5173/register'
    // const response_type = 'token'
    // const scope = 'https://www.googleapis.com/auth/userinfo.email+https://www.googleapis.com/auth/userinfo.profile+openid'
    // window.open(`${url}?client_id=${client_id}&redirect_uri=${redirect_uri}&response_type=${response_type}&scope=${scope}`, '_self')
    window.open('http://127.0.0.1:8080/login/oauth2/google', '_blank', 'width=500, height=500')
}
</script>

<template>
    <a-layout style="min-height: 100vh;">
        <a-layout-content>
            <a-row justify="center" align="middle" style="min-height: 100vh">
                <a-col :span="5">
                    <a-card>
                        <a-typography-title :level="3">SimpleAuth</a-typography-title>

                        <br />

                        <a-segmented v-model:value="currentLoginMethod" :options="['密码登录', '验证码登录']" @change="handleSegmentedChange" block />

                        <br />

                        <!-- 用户名登录 开始 -->
                        <a-form v-if="showPasswordLogin"
                                ref="passwordLoginFormRef"
                                class="login-form"
                                :model="passwordLoginForm">
                            <a-form-item
                                name="username"
                                :rules="[{ required: true, message: '请输入用户名！', trigger: 'blur' }]">
                                <a-input v-model:value="passwordLoginForm.username" placeholder="用户名">
                                    <template #prefix>
                                        <UserOutlined class="site-form-item-icon"/>
                                    </template>
                                </a-input>
                            </a-form-item>
                            <a-form-item
                                name="password"
                                :rules="[{ required: true, message: '请输入密码！', trigger: 'blur' }]">
                                <a-input-password v-model:value="passwordLoginForm.password" placeholder="密码">
                                    <template #prefix>
                                        <LockOutlined class="site-form-item-icon"/>
                                    </template>
                                </a-input-password>
                            </a-form-item>
                            <a-form-item>
                                <a-form-item name="remember" no-style>
                                    <a-checkbox v-model:checked="passwordLoginForm.remember">记住我</a-checkbox>
                                </a-form-item>
                                <router-link class="login-form-forgot" to="/forget">忘记密码</router-link>
                            </a-form-item>
                            <a-form-item>
                                <a-button type="primary" @click="handlePasswordLogin" block>登录</a-button>
                            </a-form-item>
                        </a-form>
                        <!-- 用户名登录 结束 -->

                        <!-- 验证码登录 开始 -->
                        <a-form v-if="showCodeLogin" ref="codeLoginFormRef" :model="codeLoginForm">
                            <a-form-item>
                                <a-input placeholder="邮箱/手机号">
                                    <template #prefix>
                                        <UserOutlined />
                                    </template>
                                </a-input>
                            </a-form-item>
                            <a-form-item>
                                <a-flex gap="small">
                                    <a-input placeholder="验证码">
                                        <template #prefix>
                                            <LockOutlined />
                                        </template>
                                    </a-input>
                                    <a-button>{{ codeButtonText }}</a-button>
                                </a-flex>
                            </a-form-item>
                            <a-form-item>
                                <a-form-item name="remember" no-style>
                                    <a-checkbox v-model:checked="passwordLoginForm.remember">记住我</a-checkbox>
                                </a-form-item>
                                <router-link class="login-form-forgot" to="/forget">忘记密码</router-link>
                            </a-form-item>
                            <a-form-item>
                                <a-button type="primary" @click="handleCodeLogin" block>登录</a-button>
                            </a-form-item>
                        </a-form>
                        <!-- 验证码登录 结束 -->

                        没有账号？
                        <router-link to="/register">立即注册</router-link>

                        <a-divider>
                            <a-typography-text type="secondary">其他登录方式</a-typography-text>
                        </a-divider>

                        <a-space direction="vertical" style="width: 100%">
                            <a-button href="http://127.0.0.1:8080/login/oauth2/github" block>
                                <github-outlined/>
                                使用 Github 登录
                            </a-button>
                            <a-button href="http://127.0.0.1:8080/login/oauth2/google" block>
                                <google-outlined/>
                                使用 Google 登录
                            </a-button>
                            <a-button block>
                                <windows-outlined/>
                                使用 Microsoft 登录
                            </a-button>
                        </a-space>
                    </a-card>
                </a-col>
            </a-row>
        </a-layout-content>
    </a-layout>
</template>

<style scoped>
.login-form {
    //max-width: 300px;
    width: 100%;
}

.login-form-forgot {
    float: right;
}
</style>