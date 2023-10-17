<template>
  <div class="chat-container">
    <div class="chat-header">{{ conversation.conversationTitle || '会话标题' }}</div>
    <div class="chat-messages" ref="chatMessages">
      <div v-for="(message, index) in msgInfoList" :key="index" class="message">
        <div class="message-query">
          <div class="message-content">{{ message.query }}</div>
          <div v-if="message.id==undefined">发送中</div>
          <div class="avatar">
            <el-avatar :size="32" :icon="UserFilled" />
          </div>
        </div>

        <div class="message-answer">
          <div class="avatar">
            <el-avatar :size="32" :src="manasCloudLogo" />
          </div>
          <div class="message-content">
            <div v-if="message.answer">{{ message.answer }}</div>
            <el-icon v-else class="is-loading">
              <Loading />
            </el-icon>
          </div>
        </div>
      </div>
    </div>
    <div class="chat-input-box">
      <el-input v-model="newMessage" placeholder="输入消息..." type="textarea" @keydown.enter="sendMessage" :rows="3" resize="none" />
      <el-button @click="sendMessage" :disabled="waitServerRes" round type="primary" icon="Promotion">
        {{ waitServerRes ? '发送中...' : '发送' }}
      </el-button>
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

const manasCloudLogo = "/src/assets/images/profile.jpg";
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
  await scrollToBottom()
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

<style scoped></style>
