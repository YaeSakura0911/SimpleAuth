<script setup>
import {ref} from "vue"
import locale from 'ant-design-vue/es/date-picker/locale/zh_CN'
import dayjs from 'dayjs'
import 'dayjs/locale/zh-cn'

dayjs.locale('zh-cn')

const columns = [
    {
        key: 'principal',
        title: '登录凭证',
        dataIndex: 'principal'
    },
    {
        key: 'ip',
        title: 'IP地址',
        dataIndex: 'ip'
    },
    {
        key: 'locate',
        title: '登录地点',
        dataIndex: 'locate'
    },
    {
        key: 'device',
        title: '登录设备',
        dataIndex: 'device'
    },
    {
        key: 'status',
        title: '登录状态'
    },
    {
        key: 'remark',
        title: '备注',
        dataIndex: 'remark'
    },
    {
        key: 'time',
        title: '登录时间',
        dataIndex: 'time'
    }
]
const pagination ={
    current: 1,
    pageSize: 10,
    total: 0,
    showSizeChanger: true,
    showTotal: (total, range) => `${range[0]}-${range[1]} of ${total} 条`
}
const dataSource = ref([
    {
        principal: 'admin',
        ip: '46.35.54.33',
        locate: '广西 桂林市',
        device: 'IOS',
        status: 0,
        remark: '密码错误',
        time: '2024-01-09 11:19:46'
    },
    {
        principal: 'admin',
        ip: '65.95.84.36',
        locate: '广西 桂林市',
        device: 'Android',
        status: 1,
        remark: '',
        time: '2024-01-09 11:17:35'
    }
])
</script>

<template>
    <a-layout>
        <a-layout-content style="margin: 24px; padding: 24px; background-color: #ffffff">
            <a-form>
                <a-row :gutter="24">
                    <a-col :xs="24" :xl="6">
                        <a-form-item label="登录凭证">
                            <a-input allow-clear />
                        </a-form-item>
                    </a-col>
                    <a-col :xs="24" :xl="6">
                        <a-form-item label="登录设备">
                            <a-select allow-clear>
                                <a-select-option :value="0">Windows</a-select-option>
                                <a-select-option :value="1">Mac</a-select-option>
                                <a-select-option :value="2">iOS</a-select-option>
                                <a-select-option :value="3">Android</a-select-option>
                            </a-select>
                        </a-form-item>
                    </a-col>
                    <a-col :xs="24" :xl="6">
                        <a-form-item label="登录状态">
                            <a-select allow-clear>
                                <a-select-option :value="1">登录成功</a-select-option>
                                <a-select-option :value="0">登录失败</a-select-option>
                            </a-select>
                        </a-form-item>
                    </a-col>
                    <a-col :xs="24" :xl="6">
                        <a-form-item label="登录日期">
                            <a-range-picker :locale="locale" allow-clear />
                        </a-form-item>
                    </a-col>
                </a-row>
                <a-row>
                    <a-col :span="24" style="text-align: right;">
                        <a-space>
                            <a-button type="primary">搜索</a-button>
                            <a-button>重置</a-button>
                        </a-space>
                    </a-col>
                </a-row>
            </a-form>

            <br />

            <a-table :columns="columns" :data-source="dataSource" :pagination="pagination">
                <template #bodyCell="{column, record}">
                    <template v-if="column.key === 'status'">
                        <a-tag color="success" v-if="record.status === 1">登录成功</a-tag>
                        <a-tag color="error" v-else-if="record.status === 0">登录失败</a-tag>
                    </template>
                </template>
            </a-table>
        </a-layout-content>
    </a-layout>
</template>

<style scoped>
.ant-picker {
    width: 100%
}
</style>