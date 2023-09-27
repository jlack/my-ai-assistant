<template>
  <div class="chat-container">
    <div class="chat-header">
      <h2>会话标题</h2>
    </div>
    <div class="chat-messages">
      <div v-for="(message, index) in sessionLogList" :key="index" class="message">
        <div class="message-query">
          <div class="message-content">{{ message.query }}</div>
          <div class="avatar">
            <el-avatar :icon="UserFilled"/>
          </div>
        </div>

        <div class="message-answer" v-if="message.answer">
          <div class="avatar">
            <el-avatar :icon="UserFilled"/>
          </div>
          <div class="message-content">{{ message.answer }}</div>
        </div>
      </div>
    </div>
    <div class="chat-input" v-if="enableInput">
      <el-input v-model="newMessage" placeholder="输入消息..." @keydown.enter="sendMessage"/>
      <el-button @click="sendMessage">发送</el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import {onMounted, ref} from 'vue';
import {SessionLogForm, SessionLogVO} from "@/api/witdock/sessionLog/type";
import {addSessionLog, listSessionLog} from "@/api/witdock/sessionLog/api";
import {UserFilled} from '@element-plus/icons-vue'

const newMessage = ref('');
const {data, status, close, open, send, ws} = useWebSocket("ws://localhost:8080/resource/websocket?clientid=import.meta.env.VITE_APP_CLIENT_ID&Authorization=Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsb2dpblR5cGUiOiJsb2dpbiIsImxvZ2luSWQiOiJzeXNfdXNlcjoxIiwicm5TdHIiOiJHTDkxMXVmdzBsR0swN0dsTXNNRW9xMXFqdFZOdVdZNCIsImNsaWVudGlkIjoiZTVjZDdlNDg5MWJmOTVkMWQxOTIwNmNlMjRhN2IzMmUiLCJ0ZW5hbnRJZCI6IjAwMDAwMCIsInVzZXJJZCI6MX0.mVExuen-eNihz8lK_2vWdI1hZYgd8jzFDHWTOISc-os")

const sessionLogList = ref<SessionLogVO[]>([])

const props = defineProps({
  sessionId: {
    type: String
  },
  enableInput: {
    type: Boolean,
    default: true
  }
})

data.value

watch(() => props.sessionId, (newSessionId, oldSessionId) => {
  initSessionLogList()
});

const initSessionLogList = async () => {

  let form: SessionLogForm
  form = {
    sessionId: props.sessionId
  }
  const res = await listSessionLog(form);
  sessionLogList.value = res.rows
}

const sendMessage = async () => {
  if (newMessage.value === '') return;
  // sessionLogList.value.push({text: newMessage.value, sentByUser: true});
  let form: SessionLogForm
  form = {
    sessionId: props.sessionId,
    query: newMessage.value
  }
  let res = await addSessionLog(form);
  webSocket.send(newMessage.value)
  newMessage.value = '';
  initSessionLogList()
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
  border-radius: 50%; /* 圆形头像 */
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
