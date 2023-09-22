<template>
  <div class="app-container">
    <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick" v-if="app">
      <el-tab-pane label="概览" name="first">

        <el-row :gutter="10">
          <el-col :span="12">
            <el-card class="box-card">
              <div slot="header" class="clearfix">
                <span>{{ app.appName }}</span>
                <el-switch
                  v-model="app.enableSite"
                  class="ml-2"
                  inline-prompt
                  style=" float: right;--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949"
                  active-text="运行中"
                  inactive-text="已停用"
                />
              </div>
              <el-text>公开访问URL</el-text>
              <el-input v-model="app.code" readonly :value="'https://witdock.manascloud.com/chat/'+app.code">
                <template #append>
                  <el-row :gutter="30">
                    <el-col :span="12">
                      <el-button :icon="DocumentCopy"/>
                    </el-col>
                    <el-col :span="12">
                      <el-button :icon="Refresh"/>
                    </el-col>
                  </el-row>
                </template>
              </el-input>
            </el-card>
          </el-col>
          <el-col :span="12">
            <el-card class="box-card">
              <div slot="header" class="clearfix">
                <span>后端服务 API</span>
                <el-switch
                  v-model="app.enableApi"
                  class="ml-2"
                  inline-prompt
                  style=" float: right;--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949"
                  active-text="运行中"
                  inactive-text="已停用"
                />
              </div>
              <el-input>
                <template #append>
                  <el-button :icon="DocumentCopy"/>
                </template>
              </el-input>
              <el-button type="primary" plain>API 密钥</el-button>
              <el-button type="primary" plain>查阅API文档</el-button>
            </el-card>
          </el-col>
        </el-row>

      </el-tab-pane>
      <el-tab-pane label="提示词编排" name="second"></el-tab-pane>
      <el-tab-pane label="访问API" name="third"></el-tab-pane>
      <el-tab-pane label="日志与标注" name="fourth"></el-tab-pane>
    </el-tabs>
  </div>
</template>
<script lang="ts" setup>
import {ref} from 'vue'
import type {TabsPaneContext} from 'element-plus'
import {getApp} from "@/api/witdock/app";
import {AppVO} from "@/api/witdock/app/type";
import {DocumentCopy, Refresh} from "@element-plus/icons-vue";

const activeName = ref('first')
const id = useRoute().params.id;
const app = ref<AppVO | null>(null)

const handleClick = (tab: TabsPaneContext, event: Event) => {
  console.log(tab, event)
}
const init = async () => {
  const res = await getApp(id as string)
  app.value = res.data
}
init()
</script>
<style>

</style>
