<script setup>
import {reactive, ref} from "vue";
import {onBeforeRouteUpdate, useRoute, useRouter} from "vue-router";
import {
    emailRegister,
    githubOAuth,
    phoneRegister,
    sendEmailCode,
    sendSmsCode,
    usernameRegister
} from "@/apis/authentication";
import {message} from "ant-design-vue";

const route = useRoute()
const router = useRouter()
const buttonLoading = ref(false)
const getCaptchaCountdown = ref(60)
const getCodeDisabled = ref(false)
const codeButtonText = ref('获取验证码')
const currentRegisterMethod = ref('用户名注册')
const showUsernameRegister = ref(true)
const usernameRegisterFormRef = ref()
// 用户名注册表单
const usernameRegisterForm = reactive({
    username: '',
    password: '',
})
const showEmailRegister = ref(false)
const emailRegisterFormRef = ref()
// 邮箱注册表单
const emailRegisterForm = reactive({
    email: '',
    password: '',
    code: ''
})
const showPhoneRegister = ref(false)
const phoneRegisterFormRef = ref()
// 电话注册表单
const phoneRegisterForm = reactive({
    phone: '',
    password: '',
    code: ''
})
// 表单验证规则
const validateRules = {
    username: [
        { required: true, message: '请输入账号！', trigger: 'blur' },
        { validator: checkUsernameExist, trigger: 'blur' }
    ],
    email: [
        { required: true, message: '请输入电子邮件！', trigger: 'blur' },
        { type: 'email', message: '请输入有效的邮箱地址！', trigger: 'blur' }
    ],
    phone: [
        { required: true, message: '请输入手机号！', trigger: 'blur' },
        { pattern: /^(13[0-9]|14[01456879]|15[0-35-9]|16[2567]|17[0-8]|18[0-9]|19[0-35-9])\d{8}$/, message: '请输入有效的手机号！', trigger: 'blur' }
    ],
    password: [
        { required: true, message: '请输入密码！', trigger: 'blur' },
        { min: 8, message: '最少8位字符！', trigger: 'blur' }
    ],
    code: [
        { required: true, message: '请输入验证码！', trigger: 'blur' }
    ]
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
            showPhoneRegister.value = false
            break
        case '邮箱注册':
            showUsernameRegister.value = false
            showEmailRegister.value = true
            showPhoneRegister.value = false
            break
        case '手机号注册':
            showUsernameRegister.value = false
            showEmailRegister.value = false
            showPhoneRegister.value = true
            break
    }
}

/**
 * 用户名注册
 */
function handleUsernameRegister() {
    usernameRegisterFormRef.value.validate().then(() => {
        buttonLoading.value = true
        usernameRegister(usernameRegisterForm).then(() => {
            message.success('注册成功')
            buttonLoading.value = false

            router.push('/login')
        })
    })
}

/**
 * 邮箱注册
 */
function handleEmailRegister() {
    emailRegisterFormRef.value.validate().then(() => {
        buttonLoading.value = true
        emailRegister(emailRegisterForm).then(() => {
            message.success('注册成功！')
            buttonLoading.value = false

            router.push('/login')
        })
    })
}

/**
 * 手机号注册
 */
function handlePhoneRegister() {
    phoneRegisterFormRef.value.validate().then(() => {
        buttonLoading.value = true
        phoneRegister(phoneRegisterForm).then(() => {
            message.success('注册成功')
            buttonLoading.value = false

            router.push('/login')
        })
    })
}

function handleEmailCode() {
    emailRegisterFormRef.value.validateFields(['email']).then(() => {
        getCodeDisabled.value = true

        sendEmailCode(emailRegisterForm.email).then(() => {
            message.success('验证码发送成功')
        })

        const lock = setInterval(() => {
            getCaptchaCountdown.value = getCaptchaCountdown.value - 1
            codeButtonText.value = getCaptchaCountdown.value + '秒后重发'
            if (getCaptchaCountdown.value === 0) {
                getCaptchaCountdown.value = 60
                codeButtonText.value = '获取验证码'
                getCodeDisabled.value = false
                clearInterval(lock)
            }
        }, 1000)
    })
}

/**
 * 发送短信验证码
 */
function handleSendSmsCode() {
    phoneRegisterFormRef.value.validateFields(['phone']).then(() => {
        getCodeDisabled.value = true

        // 发送验证码
        sendSmsCode(phoneRegisterForm.phone).then(() => {
            message.success('验证码发送成功')
        })

        const lock = setInterval(() => {
            getCaptchaCountdown.value = getCaptchaCountdown.value - 1
            codeButtonText.value = getCaptchaCountdown.value + '秒后重发'
            if (getCaptchaCountdown.value === 0) {
                getCaptchaCountdown.value = 60
                codeButtonText.value = '获取验证码'
                getCodeDisabled.value = false
                clearInterval(lock)
            }
        }, 1000)
    })
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
                            <a-typography-title :level="4">注册</a-typography-title>
                        </a-typography-title>

                        <br/>

                        <a-segmented v-model:value="currentRegisterMethod"
                                     :options="['用户名注册', '邮箱注册', '手机号注册']" @change="handleSegmentedChange"
                                     block/>

                        <br/>

                        <!-- 用户名注册 开始 -->
                        <a-form class="register-form"
                                ref="usernameRegisterFormRef"
                                v-if="showUsernameRegister"
                                layout="vertical"
                                :model="usernameRegisterForm"
                                :rules="validateRules"
                                :label-col="{ style: { width: '72px' } }"
                                :colon="false">
                            <a-form-item name="username" label="用户名">
                                <a-input v-model:value="usernameRegisterForm.username"/>
                            </a-form-item>
                            <a-form-item name="password" label="密码">
                                <a-input-password v-model:value="usernameRegisterForm.password"/>
                            </a-form-item>
                            <a-form-item>
                                <a-button type="primary" :loading="buttonLoading" @click="handleUsernameRegister" block>注册</a-button>
                            </a-form-item>
                        </a-form>
                        <!-- 用户名注册 结束 -->

                        <!-- 邮箱注册 开始 -->
                        <a-form class="register-form"
                                v-if="showEmailRegister"
                                ref="emailRegisterFormRef"
                                layout="vertical"
                                :model="emailRegisterForm"
                                :rules="validateRules"
                                :label-col="{ style: { width: '72px' } }">
                            <a-form-item name="email" label="电子邮件">
                                <a-input v-model:value="emailRegisterForm.email"/>
                            </a-form-item>
                            <a-form-item name="password" label="密码">
                                <a-input-password v-model:value="emailRegisterForm.password"/>
                            </a-form-item>
                            <a-form-item name="code" label="验证码">
                                <a-flex gap="small">
                                        <a-input v-model:value="emailRegisterForm.code"/>
                                    <a-button :disabled="getCodeDisabled" @click="handleEmailCode">{{
                                            codeButtonText
                                        }}
                                    </a-button>
                                </a-flex>
                            </a-form-item>
                            <a-form-item>
                                <a-button type="primary" :loading="buttonLoading" @click="handleEmailRegister" block>注册</a-button>
                            </a-form-item>
                        </a-form>
                        <!-- 邮箱注册 结束 -->

                        <!-- 手机号注册 开始 -->
                        <a-form v-if="showPhoneRegister"
                                ref="phoneRegisterFormRef"
                                layout="vertical"
                                :model="phoneRegisterForm"
                                :rules="validateRules"
                                :label-col="{ style: { width: '72px' } }">
                            <a-form-item name="phone" label="手机号">
                                <a-input v-model:value="phoneRegisterForm.phone" />
                            </a-form-item>
                            <a-form-item name="password" label="密码">
                                <a-input-password v-model:value="phoneRegisterForm.password"/>
                            </a-form-item>
                            <a-form-item name="code" label="验证码">
                                <a-flex gap="small">
                                    <a-input v-model:value="phoneRegisterForm.code"/>
                                    <a-button :disabled="getCodeDisabled" @click="handleSendSmsCode">{{
                                            codeButtonText
                                        }}
                                    </a-button>
                                </a-flex>
                            </a-form-item>
                            <a-form-item>
                                <a-button type="primary" :loading="buttonLoading" @click="handlePhoneRegister" block>注册</a-button>
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