<template>
  <h2 class="mb-2">工作区</h2>
  <el-row :gutter="10">
    <el-col :span="4">
      <el-menu
        class="el-menu-vertical-demo"
      >
        <el-menu-item :index="appItem.id" v-for="appItem in appList" @click="chooseApp(appItem)">
          <el-icon>
            <icon-menu/>
          </el-icon>
          <span>{{ appItem.appName }}</span>
        </el-menu-item>
      </el-menu>
    </el-col>
    <el-col :span="20">
      <el-card class="box-card" v-loading="loading">
        <el-row :gutter="10">
          <el-col :span="4">
            <el-menu class="el-menu-demo">
              <el-menu-item>
                <el-button type="primary" @click="newSession()">新建会话</el-button>
              </el-menu-item>
              <el-menu-item :index="sessionItem.id" v-for="sessionItem in sessionList"
                            @click="chooseSession(sessionItem)">
                {{ sessionItem.sessionTitle }}
              </el-menu-item>
            </el-menu>
          </el-col>
        </el-row>
      </el-card>
    </el-col>
  </el-row>

</template>
<script setup lang="ts">


import {listApp} from "@/api/witdock/app";
import {AppVO} from "@/api/witdock/app/type";
import {addSession, listSession} from "@/api/witdock/session";
import {SessionForm, SessionQuery, SessionVO} from "@/api/witdock/session/types";

const appList = ref<AppVO[]>([])
const sessionList = ref<SessionVO[]>([])
const currentAppId = ref('')
const currentSessionId = ref('')
const loading = ref(false)
const init = async () => {
  let res = await listApp();
  appList.value = res.rows
}

const newSession = async () => {
  loading.value = true
  let session: SessionForm
  session = {
    appId: currentAppId.value
  }
  const res = await addSession(session)
  await initSessionList()
}

const chooseApp = async (item: AppVO) => {
  currentAppId.value = item.id as string
  await initSessionList()
}
const chooseSession = async (item: SessionVO) => {
  currentSessionId.value = item.id as string
  await initSessionLogList()
}

const initSessionLogList = async () => {
  loading.value = true
  loading.value = false
}
const initSessionList = async () => {
  loading.value = true
  let sessionQuery: SessionQuery
  sessionQuery = {
    appId: currentAppId.value,
    pageNum: 1,
    pageSize: 20
  }
  const res = await listSession(sessionQuery);
  sessionList.value = res.rows
  loading.value = false
}

init()

</script>
<style scoped lang="scss">

</style>
