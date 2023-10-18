<template>
  <div class="wit-explore">
    <div class="wit-app-list">
      <div class="explore-title">工作区</div>
      <div
        v-for="appItem in appList"
        :key="appItem.id"
        class="wit-app"
        :class="appItem.id === currentAppId ? 'active-app' : ''"
        @click="chooseApp(appItem)"
      >
        <img src="@/assets/images/app.png" />
        <span>{{ appItem.appName }}</span>
      </div>
    </div>
    <chat-card :is-user="false" :prop-app-id="currentAppId" />
  </div>
</template>
<script setup lang="ts">
import {listApp} from "@/api/witdock/app";
import {AppVO} from "@/api/witdock/app/type";
import ChatCard from "@/views/witdock/chatCard.vue";


const appList = ref<AppVO[]>([])

const currentAppId = ref('')

const init = async () => {
  let res = await listApp();
  appList.value = res.rows
}

const chooseApp = async (item: AppVO) => {
  currentAppId.value = item.id as string
}

init()
</script>

<style lang="scss">
@import "./explore.scss";
</style>
