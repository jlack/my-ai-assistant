<template>
  <div>
    <h2 class="mb-2">工作区</h2>
    <el-row :gutter="10" style="height: 100%">
      <el-col :span="4">
        <el-menu class="el-menu-vertical-demo">
          <el-menu-item :index="appItem.id" v-for="appItem in appList" @click="chooseApp(appItem)">
            <el-icon>
              <House/>
            </el-icon>
            <span>{{ appItem.appName }}</span>
          </el-menu-item>
        </el-menu>
      </el-col>
      <el-col :span="20">
          <chat-card style="height: 600px" :prop-app-id="currentAppId"></chat-card>
      </el-col>
    </el-row>

  </div>


</template>
<script setup lang="ts">


import {listApp} from "@/api/witdock/app";
import {AppVO} from "@/api/witdock/app/type";
import ChatCard from "@/views/witdock/chatCard.vue";


const appList = ref<AppVO[]>([])

const currentAppId = ref('')
const currentAppName = ref('')


const init = async () => {
  let res = await listApp();
  appList.value = res.rows
}

const chooseApp = async (item: AppVO) => {
  currentAppId.value = item.id as string
  // await initConversationList()
}

init()

</script>
<style scoped lang="scss">

</style>
