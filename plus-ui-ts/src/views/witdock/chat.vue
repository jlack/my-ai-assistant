<template>
  <div class="chat-container">
    <div class="chat-header">
      <h2>会话标题</h2>
    </div>
    <div class="chat-messages">
      <div v-for="(message, index) in msgInfoList" :key="index" class="message">
        <div class="message-query">
          <div class="message-content">{{ message.query }}</div>
          <div class="avatar">
            <el-avatar :icon="UserFilled"/>
          </div>
        </div>

        <div class="message-answer" v-if="message.answer">
          <div class="avatar">
            <el-avatar :src="manasCloudLogo"/>
          </div>
          <div class="message-content">{{ message.answer }}</div>
        </div>
      </div>
    </div>
    <div class="chat-input">
      <el-input v-model="newMessage" placeholder="输入消息..." @keydown.enter="sendMessage"/>
      <el-button @click="sendMessage" :disabled="waitServerRes">{{ waitServerRes ? '发送中...' : '发送' }}</el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import {ref} from 'vue';
import {UserFilled} from '@element-plus/icons-vue'
import {addMessageInfo, listMessageInfo} from "@/api/witdock/messageInfo/api";
import {MessageInfoQuery, MessageInfoVO} from "@/api/witdock/messageInfo/types";

const manasCloudLogo = "/src/assets/logo/logo.png";
import {getToken} from "@/utils/auth";

const {proxy} = getCurrentInstance() as ComponentInternalInstance
const waitServerRes = ref(false)
const msgInfoList = ref<MessageInfoVO[]>([])
const props = defineProps({
  conversationId: {
    type: String
  }
})

const newMessage = ref('');
const webSocketUrl = "ws://localhost:8080/resource/websocket?clientid="
  + import.meta.env.VITE_APP_CLIENT_ID
  + "&Authorization="
  + "Bearer " + getToken();

const webSocket = useWebSocket(webSocketUrl);


watch(webSocket.status, (status) => {
  if (status.valueOf() === 'CLOSED') {
    proxy?.$modal.msgError("链接websocket关闭了")
  }
})

// 监听 WebSocket 数据
watch(webSocket.data, (newData) => {
  if (newData) {
    // 处理收到的数据
    const res = JSON.parse(newData) as MessageInfoVO;
    //遍历msgInfoList,如果不存在说明只是返回了 query，直接push. 否则就是返回answer进行更新
    if (res.reDatetime) {
      //倒序循环
      for (let i = msgInfoList.value.length - 1; i >= 0; i--) {
        const item = msgInfoList.value[i];
        if (item.id === res.id) {
          item.answer = res.answer;
          item.reDatetime = res.reDatetime;
          waitServerRes.value = false
          break; // 找到匹配元素后跳出循环
        }
      }
    } else {
      msgInfoList.value.push(res)
    }
  }
});

watch(() => props.conversationId, (newVal, oldVal) => {
  initMsgInfoList()
},{ immediate: true});

async function initMsgInfoList() {
  const res = await listMessageInfo({
    conversationId: props.conversationId
  } as MessageInfoQuery);
  msgInfoList.value = res.rows
}

const sendMessage = async () => {
  if (newMessage.value === '')
    return;
  waitServerRes.value = true
  let form = {
    conversationId: props.conversationId,
    query: newMessage.value,
    wsType: 'chatMsg'
  }
  let isSuccess = webSocket.send(JSON.stringify(form));
  if (isSuccess) {
    newMessage.value = ''
  } else {
    proxy?.$modal.msgError("消息发送失败")
    waitServerRes.value = false
  }
};
</script>

<style scoped>
.chat-container {
  max-width: 100%;
  margin: 0 auto;
  padding: 20px;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  background-color: #f9f9f9;
  font-family: Arial, sans-serif;
}

.chat-header {
  text-align: center;
  padding-bottom: 10px;
  border-bottom: 1px solid #e0e0e0;
}

.chat-messages {
  max-height: 300px;
  overflow-y: auto;
}


.avatar {
  width: 50px; /* 头像的宽度 */
  height: 50px; /* 头像的高度 */
  overflow: hidden;
  margin-right: 10px;
  margin-left: 10px;
}

.message-query {
  display: flex;
  color: white; /* 发送消息的文字颜色 */
  border-top-right-radius: 0; /* 右上角不设为圆角 */
  justify-content: flex-end; /* 靠右显示 */
}

.message-answer {
  display: flex;
  justify-content: flex-start; /* 靠左显示 */
  color: white; /* 发送消息的文字颜色 */
  border-top-right-radius: 0; /* 右上角不设为圆角 */
}

.message-content {
  background-color: #409eff;
  padding: 10px;
  border-radius: 8px;
}


.chat-input {
  display: flex;
  gap: 10px;
  align-items: center;
  background-color: white;
  padding: 10px;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
}

.el-input {
  flex-grow: 1;
  margin-bottom: 0;
}

.el-button {
  background-color: #0078d4;
  color: white;
  border: none;
  border-radius: 5px;
  padding: 10px 20px;
  font-size: 14px;
  cursor: pointer;
}

.el-button:hover {
  background-color: #005ea6;
}
</style>
