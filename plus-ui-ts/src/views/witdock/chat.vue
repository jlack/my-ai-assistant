<template>
  <div class="chat-container">
    <div class="chat-header">
      <h2>应用名称</h2>
    </div>
    <div class="chat-messages">
      <div v-for="(message, index) in messages" :key="index" class="message"
           :class="{ 'message-sent': message.sentByUser }">
        <div class="message-content">{{ message.text }}</div>
      </div>
    </div>
    <div class="chat-input">
      <el-input v-model="newMessage" placeholder="输入消息..." @keydown.enter="sendMessage"/>
      <el-button @click="sendMessage">发送</el-button>
    </div>
  </div>
</template>

<script setup>
import {ref} from 'vue';

const messages = ref([
  {text: '你好！', sentByUser: false},
  {text: '你好！有什么新鲜事吗？', sentByUser: true},
]);

const newMessage = ref('');

const sendMessage = () => {
  if (newMessage.value === '') return;
  messages.value.push({text: newMessage.value, sentByUser: true});
  newMessage.value = '';
};
</script>

<style scoped>
.chat-container {
  max-width: 400px;
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

.message {
  display: flex;
  align-items: center;
  padding: 10px;
  margin: 10px 0;
  border-radius: 10px;
  font-size: 14px;
}

.message-sent {
  background-color: #0078d4;
  color: white;
  align-self: flex-end;
}

.message-content {
  word-wrap: break-word;
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
