<script setup>
import {computed, onBeforeMount, reactive, ref} from "vue";
import {onBeforeRouteUpdate, useRoute, useRouter} from "vue-router";
import {githubOAuth} from "@/apis/basic";
import {message} from "ant-design-vue";

const route = useRoute()
const router = useRouter()
const buttonLoading = ref(false)
const getCaptchaCountdown = ref(60)
const getCaptchaDisabled = ref(false)
const captchaButtonText = ref('获取验证码')
const currentRegisterMethod = ref('用户名注册')
const showUsernameRegister = ref(true)
const registerFormRef = ref()
const registerForm = reactive({
    username: '',
    password: '',
    email: '',
    captcha: ''
})
const showEmailRegister = ref(false)
const emailRegisterFormRef = ref()
const emailRegisterForm = reactive({
    email: '',
    password: ''
})
const validateRules = {
    username: [
        {required: true, message: '请输入账号！', trigger: 'blur'},
        {validator: checkUsernameExist, trigger: 'blur'}
    ],
    password: [
        {required: true, message: '请输入密码！', trigger: 'blur'},
        {min: 8, message: '最少8位字符！', trigger: 'blur'}
    ],
    email: [
        {required: true, message: '请输入电子邮件！', trigger: 'blur'},
        {type: 'email', message: '请输入有效的邮箱地址。', trigger: 'blur'}
    ]
}

const computeCanSendVerifyCode = computed(() => {

})

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

function handleSegmentedChange(title) {
    console.log(title)

    switch (title) {
        case '用户名注册':
            showUsernameRegister.value = true
            showEmailRegister.value = false
            break
        case '邮箱注册':
            showUsernameRegister.value = false
            showEmailRegister.value = true
            break
        case '手机号注册':

            break
    }
}

function handleCaptcha() {
    getCaptchaDisabled.value = true
    if (getCaptchaDisabled.value) {
        // 验证

        // TODO: 调用发送验证码API
        message.success('验证码发送成功')
    }
    const lock = setInterval(() => {
        getCaptchaCountdown.value = getCaptchaCountdown.value - 1
        captchaButtonText.value = getCaptchaCountdown.value + '秒后重发'
        if (getCaptchaCountdown.value === 0) {
            getCaptchaCountdown.value = 60
            captchaButtonText.value = '获取验证码'
            getCaptchaDisabled.value = false
            clearInterval(lock)
        }
    }, 1000)
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

                        <a-segmented v-model:value="currentRegisterMethod"
                                     :options="['用户名注册', '邮箱注册', '手机号注册']" @change="handleSegmentedChange"
                                     block/>

                        <br/>

                        <!-- 用户名注册 开始 -->
                        <a-form class="register-form"
                                ref="registerFormRef"
                                v-if="showUsernameRegister"
                                :model="registerForm"
                                :rules="validateRules"
                                :label-col="{ style: { width: '72px' } }"
                                :colon="false"
                                @finish="handleFinish">
                            <a-form-item name="username" label="用户名">
                                <a-input v-model:value="registerForm.username"/>
                            </a-form-item>
                            <a-form-item name="password" label="密码">
                                <a-input-password v-model:value="registerForm.password"/>
                            </a-form-item>
                            <a-form-item>
                                <a-button type="primary" html-type="submit" :loading="buttonLoading" block>注册</a-button>
                            </a-form-item>
                        </a-form>
                        <!-- 用户名注册 结束 -->

                        <!-- 邮箱注册 开始 -->
                        <a-form class="register-form"
                                ref="emailRegisterFormRef"
                                v-if="showEmailRegister"
                                :model="emailRegisterForm"
                                :rules="validateRules"
                                :label-col="{ style: { width: '72px' } }"
                                :colon="false"
                                @finish="handleFinish">
                            <a-form-item name="email" label="电子邮件">
                                <a-input v-model:value="emailRegisterForm.email"/>
                            </a-form-item>
                            <a-form-item name="password" label="密码">
                                <a-input-password v-model:value="emailRegisterForm.password"/>
                            </a-form-item>
                            <a-form-item name="" label="验证码">
                                <a-space style="width: 100%">
                                    <a-input v-model:value="registerForm.captcha"/>
                                    <a-button :disabled="getCaptchaDisabled" @click="handleCaptcha">{{
                                            captchaButtonText
                                        }}
                                    </a-button>
                                </a-space>
                            </a-form-item>
                            <a-form-item>
                                <a-button type="primary" html-type="submit" :loading="buttonLoading" block>注册</a-button>
                            </a-form-item>
                        </a-form>
                        <!-- 邮箱注册 结束 -->

                        <!-- 手机号注册 开始 -->
                        <a-form>
                            <a-form-item name="phone" label="手机号">
                                <a-input />
                            </a-form-item>
                            <a-form-item name="password" label="密码">
                                <a-input-password v-model:value="emailRegisterForm.password"/>
                            </a-form-item>
                            <a-form-item name="" label="验证码">
                                <a-space style="width: 100%">
                                    <a-input v-model:value="registerForm.captcha"/>
                                    <a-button :disabled="getCaptchaDisabled" @click="handleCaptcha">{{
                                            captchaButtonText
                                        }}
                                    </a-button>
                                </a-space>
                            </a-form-item>
                        </a-form>
                        <!-- 手机号注册 结束 -->

                        已有账号？
                        <router-link to="/login">立即登录</router-link>
                    </a-card>
                </a-col>
            </a-row>
        </a-layout-content>
        {{ currentRegisterMethod }}
    </a-layout>
</template>

<style scoped>
.register-form {
    width: 100%;
}
</style>