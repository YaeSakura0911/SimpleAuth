<script setup>
import {reactive, computed} from 'vue'
import {GithubOutlined, GoogleOutlined, UserOutlined, LockOutlined} from '@ant-design/icons-vue'
import {login} from "@/apis/basic";
import {useRouter} from "vue-router";
import {message} from "ant-design-vue";

const router = useRouter()
const formState = reactive({
    username: '',
    password: '',
    // remember: true,
});
function onFinish(values) {
    login(values).then(() => {
        message.success('登录成功')
        router.push('/home')
    })
}
function onFinishFailed(errorInfo) {
    console.log('Failed:', errorInfo);
}
const disabled = computed(() => {
    return !(formState.username && formState.password);
});
</script>

<template>
    <a-layout>
        <a-card>
            <a-form
                :model="formState"
                name="normal_login"
                class="login-form"
                @finish="onFinish"
                @finishFailed="onFinishFailed"
            >
                <a-form-item label="用户名" name="username"
                             :rules="[{ required: true, message: 'Please input your username!' }]">
                    <a-input v-model:value="formState.username">
                        <template #prefix>
                            <UserOutlined class="site-form-item-icon"/>
                        </template>
                    </a-input>
                </a-form-item>

                <a-form-item
                    label="密码"
                    name="password"
                    :rules="[{ required: true, message: 'Please input your password!' }]"
                >
                    <a-input-password v-model:value="formState.password">
                        <template #prefix>
                            <LockOutlined class="site-form-item-icon"/>
                        </template>
                    </a-input-password>
                </a-form-item>

                <a-form-item>
                    <a-form-item name="remember" no-style>
                        <a-checkbox v-model:checked="formState.remember">记住我</a-checkbox>
                    </a-form-item>
                    <a class="login-form-forgot" href="">忘记密码</a>
                </a-form-item>

                <a-form-item>
                    <a-button :disabled="disabled" type="primary" html-type="submit" class="login-form-button">
                        登录
                    </a-button>
                    Or
                    <a href="">register now!</a>
                </a-form-item>

                <GithubOutlined />
                <GoogleOutlined />
            </a-form>
        </a-card>
    </a-layout>

</template>

<style scoped>
#components-form-demo-normal-login .login-form {
    max-width: 300px;
}

#components-form-demo-normal-login .login-form-forgot {
    float: right;
}

#components-form-demo-normal-login .login-form-button {
    width: 100%;
}
</style>