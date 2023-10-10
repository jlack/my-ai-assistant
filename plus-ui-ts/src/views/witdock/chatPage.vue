<template>
  <chat-card v-if="chatVis" :is-vistor="true" :prop-app-id="currAppId"/>
</template>

<script setup lang="ts">
import ChatCard from "@/views/witdock/chatCard.vue";
import {listApp} from "@/api/witdock/app";
import {genChatToken} from "@/api/witdock/conversationInfo/api";

const route = useRoute()
const appCode = route.params.code;
const currAppId = ref('');
const chatVis = ref(false);

async function init() {
  console.log('call init!' + appCode)
  if (appCode) {
    let res = await listApp({
      code: String(appCode),
      pageNum: 1,
      pageSize: 10
    });
    currAppId.value = String(res.rows[0].id);

    let chatToken = localStorage.getItem('chatToken');
    if (chatToken == null) {
      console.log('生成新的chatToken');
      let res = await genChatToken();
      localStorage.setItem('chatToken', res.data);
    }
    chatVis.value = true;
  }
}

init();
</script>

