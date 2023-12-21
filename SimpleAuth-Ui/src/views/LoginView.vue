<script setup>
import {reactive} from 'vue'
import {GithubOutlined, GoogleOutlined, UserOutlined, LockOutlined, WindowsOutlined} from '@ant-design/icons-vue'
import {googleOAuth, login} from "@/apis/basic";
import {onBeforeRouteUpdate, useRouter} from "vue-router";
import {message} from "ant-design-vue";

const router = useRouter()
const loginForm = reactive({
    username: '',
    password: '',
    remember: true,
});

function onFinish(values) {
    login(values).then(() => {
        router.push('/home')
        message.success('登录成功')
    })
}

function onFinishFailed(errorInfo) {
    console.log('Failed:', errorInfo);
}

function handleGithubOAuth() {
    window.open('http://127.0.0.1:8080/login/oauth2/github','_blank', 'width=500, height=500')
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

                        <br/>

                        <a-form
                            :model="loginForm"
                            class="login-form"
                            @finish="onFinish"
                            @finishFailed="onFinishFailed">
                            <a-form-item
                                name="username"
                                :rules="[{ required: true, message: '请输入用户名！', trigger: 'blur' }]">
                                <a-input v-model:value="loginForm.username" placeholder="用户名">
                                    <template #prefix>
                                        <UserOutlined class="site-form-item-icon"/>
                                    </template>
                                </a-input>
                            </a-form-item>

                            <a-form-item
                                name="password"
                                :rules="[{ required: true, message: '请输入密码！', trigger: 'blur' }]">
                                <a-input-password v-model:value="loginForm.password" placeholder="密码">
                                    <template #prefix>
                                        <LockOutlined class="site-form-item-icon"/>
                                    </template>
                                </a-input-password>
                            </a-form-item>

                            <a-form-item>
                                <a-form-item name="remember" no-style>
                                    <a-checkbox v-model:checked="loginForm.remember">记住我</a-checkbox>
                                </a-form-item>
                                <router-link class="login-form-forgot" to="/forget">忘记密码</router-link>
                            </a-form-item>

                            <a-form-item>
                                <a-space direction="vertical" style="width: 100%">
                                    <a-button type="primary" html-type="submit" block>
                                        登录
                                    </a-button>
                                    <router-link to="/register">
                                        <a-button block>注册</a-button>
                                    </router-link>
                                </a-space>
                            </a-form-item>
                        </a-form>

                        <a-divider>
                            <a-typography-text type="secondary">其他登录方式</a-typography-text>
                        </a-divider>

                        <a-space direction="vertical" style="width: 100%">
                            <a-button href="http://127.0.0.1:8080/login/oauth2/github" block>
                                <github-outlined />
                                使用 Github 登录
                            </a-button>
                            <a-button href="http://127.0.0.1:8080/login/oauth2/google" block>
                                <google-outlined />
                                使用 Google 登录
                            </a-button>
                            <a-button block>
                                <windows-outlined />
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