<script setup>
import {computed, reactive, ref} from "vue";
import {useRouter} from "vue-router";

const router = useRouter()
const currentMethod = ref('邮箱找回')
const showEmailForget = ref(true)
const showPhoneForget = ref(false)
// 邮箱找回表单
const emailForgetForm = reactive({
    email: ''
})
// 手机号找回表单
const phoneForgetForm = reactive({
    phone: '',
    code: ''
})

function handleSegmentedChange(title) {
    console.log(title)

    switch (title) {
        case '邮箱找回':
            showEmailForget.value = true
            showPhoneForget.value = false
            break
        case '手机号找回':
            showEmailForget.value = false
            showPhoneForget.value = true
            break
    }
}

/**
 *
 */
function handleEmailForget() {
    // TODO: 跳转到发送成功结果页
}

/**
 *
 */
function handlePhoneForget() {
    router.push('/reset-password')
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
                            <a-typography-title :level="4">忘记密码</a-typography-title>
                        </a-typography-title>

                        <br/>

                        <a-segmented v-model:value="currentMethod" :options="['邮箱找回', '手机号找回']" @change="handleSegmentedChange" block/>

                        <br/>

                        <a-form v-if="showEmailForget" layout="vertical" :model="emailForgetForm">
                            <a-form-item>
                                <a-typography-text>输入你注册SimpleAuth所使用的邮箱，我们将向您发送重置密码所需的链接</a-typography-text>
                            </a-form-item>

                            <a-form-item label="电子邮箱"
                                         name="email"
                                         :rules="[{ type: 'email', message: '请输入有效的邮箱地址。', trigger: 'blur' }]">
                                <a-input v-model:value="emailForgetForm.email"/>
                            </a-form-item>
                            <a-form-item>
                                <a-button type="primary" @click="handleEmailForget" block>继续</a-button>
                            </a-form-item>
                        </a-form>

                        <a-form v-if="showPhoneForget" layout="vertical" :model="phoneForgetForm">
                            <a-form-item label="手机号">
                                <a-input v-model:value="phoneForgetForm.phone"/>
                            </a-form-item>
                            <a-form-item label="验证码">
                                <a-flex gap="small">
                                    <a-input v-model:value="phoneForgetForm.code"/>
                                    <a-button></a-button>
                                </a-flex>
                            </a-form-item>
                            <a-form-item>
                                <a-button type="primary" @click="handlePhoneForget" block>继续</a-button>
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