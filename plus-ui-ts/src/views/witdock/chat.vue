<template>
  <div class="chat-container">
    <div class="chat-header">
      <h2 v-if="conversation.conversationTitle">{{ conversation.conversationTitle }}</h2>
      <h2 v-else>会话标题</h2>
    </div>
    <div class="chat-messages" ref="chatMessages">
      <div v-for="(message, index) in msgInfoList" :key="index" class="message">
        <div class="message-query">
          <div class="message-content">{{ message.query }}</div>
          <div v-if="message.id==undefined">发送中</div>
          <div class="avatar">
            <el-avatar :icon="UserFilled"/>
          </div>
        </div>

        <div class="message-answer">
          <div class="avatar">
            <el-avatar :src="manasCloudLogo"/>
          </div>
          <div class="message-content">
            <div v-if="message.answer">{{ message.answer }}</div>
            <el-icon v-else class="is-loading">
              <Loading/>
            </el-icon>
          </div>

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
import {v4 as uuidv4} from 'uuid'
import {addMessageInfo, listMessageInfo} from "@/api/witdock/messageInfo/api";
import {MessageInfoForm, MessageInfoQuery, MessageInfoVO} from "@/api/witdock/messageInfo/types";
import {getToken} from "@/utils/auth";
import {getConversationInfo} from "@/api/witdock/conversationInfo/api";
import {ConversationInfoVO} from "@/api/witdock/conversationInfo/types";

const manasCloudLogo = "/src/assets/logo/logo.png";
const {proxy} = getCurrentInstance() as ComponentInternalInstance
const waitServerRes = ref(false)

const conversation = ref<ConversationInfoVO>({} as ConversationInfoVO);
const msgInfoList = ref<MessageInfoVO[]>([])
const props = defineProps({
  conversationId: {
    type: String
  }
})
const chatMessages = ref<HTMLElement | null>(null);
const newMessage = ref('');
const webSocketUrl = "ws://localhost:8080/websocket/chat?clientid="
  + import.meta.env.VITE_APP_CLIENT_ID
  + "&Authorization="
  + "Bearer " + getToken();

const webSocket = useWebSocket(webSocketUrl);

watch(webSocket.status, (status) => {
  if (status.valueOf() === 'CLOSED') {
    try {
      webSocket.open
    } catch (e) {
      proxy?.$modal.msgError("链接websocket关闭了")
    }
  }
})

// 监听 WebSocket 数据
watch(webSocket.data, (newData) => {
  console.log(newData)
  if (newData) {
    // 处理收到的数据
    const res = JSON.parse(newData) as MessageInfoVO;
    //遍历msgInfoList
    for (let i = msgInfoList.value.length - 1; i >= 0; i--) {
      const item = msgInfoList.value[i];
      if (item.msgLocalId === res.msgLocalId) {
        item.id = res.id
        if (res.streamText) {
          item.answer = item.answer + res.streamText
        } else {
          item.answer = res.answer;
        }
        item.reDatetime = res.reDatetime;
        waitServerRes.value = false
        //滚动聊天窗口到最下面
        scrollToBottom()
        break; // 找到匹配元素后跳出循环
      }
    }
  }
});

watch(() => props.conversationId, (newVal, oldVal) => {
  initConversation()
  initMsgInfoList()
  waitServerRes.value = false
}, {immediate: true});

async function initConversation() {
  const res = await getConversationInfo(props.conversationId as string)
  conversation.value = res.data
}

async function initMsgInfoList() {
  const res = await listMessageInfo({
    conversationId: props.conversationId
  } as MessageInfoQuery);
  msgInfoList.value = res.rows
  //滚动聊天窗口到最下面
  scrollToBottom()
}

const sendMessage = async () => {
  if (newMessage.value === '')
    return;
  waitServerRes.value = true
  let form: MessageInfoForm = {
    conversationId: props.conversationId,
    query: newMessage.value,
    msgLocalId: uuidv4()
  }
  msgInfoList.value.push(form as MessageInfoVO)
  //滚动聊天窗口到最下面
  scrollToBottom()
  newMessage.value = ''
  let isSuccess = webSocket.send(JSON.stringify(form));
  if (!isSuccess) {
    proxy?.$modal.msgError("消息发送失败")
    waitServerRes.value = false
  }
};

// 滚动到底部
const scrollToBottom = async () => {
  if (chatMessages.value) {
    await nextTick()
    chatMessages.value.scrollTop = chatMessages.value.scrollHeight;
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
