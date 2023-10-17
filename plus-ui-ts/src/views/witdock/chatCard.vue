<template>
  <div class="chat-card" v-loading="loading" v-if="currAppId">
    <div class="chat-list">
      <el-button type="primary" plain @click="newConversation()" icon="Edit">新建会话</el-button>
      <div
        v-for="(conversationItem, index) in conversationList"
        :key="index"
        :index="(conversationItem.id as string)"
        class="chat-item"
        :class="currConversationId === conversationItem.id ? 'active-chat' : ''"
        @click="chooseConversation(conversationItem)"
      >
        <div class="title">
          <!-- <ChatLineRound /> -->
          <span>{{ conversationItem.conversationTitle }}</span>
        </div>
        <div class="btn-group">
          <el-tooltip v-if="!conversationItem.topping" effect="dark" content="置顶" placement="top">
            <el-icon>
              <Top @click="handleTopPost(conversationItem.id, true)" />
            </el-icon>
          </el-tooltip>
          <el-tooltip v-if="conversationItem.topping" effect="dark" content="取消置顶" placement="top">
            <el-icon>
              <Bottom @click="handleTopPost(conversationItem.id, false)" />
            </el-icon>
          </el-tooltip>
          <el-tooltip effect="dark" content="编辑" placement="top">
            <el-icon>
              <EditPen @click="handleRename(conversationItem.id, conversationItem.conversationTitle)" />
            </el-icon>
          </el-tooltip>
          <el-tooltip effect="dark" content="删除" placement="top">
            <el-icon class="delete">
              <Delete @click="handleDelete(conversationItem.id)" />
            </el-icon>
          </el-tooltip>
        </div>
      </div>
    </div>

    <chat v-if="currConversationId" :conversation-id="currConversationId" />

    <div v-if="conversationList?.length === 0 && !loading" class="new-chat">
      <div class="app-name">{{ currApp.appName }}</div>
      <el-button type="primary" @click="startConversation()">
        <el-icon class="mr5">
          <ChatDotSquare />
        </el-icon>
        开始对话
      </el-button>
    </div>
  </div>

  <el-dialog title="会话重命名" v-model="openRename" width="500px" :append-to-body="true">
    <!--    @submit.native.prevent 可以阻止按回车刷新页面-->
    <el-form ref="conversationRenameRef" :model="selectedConversation" :rules="renameRules" label-width="100px" @submit.native.prevent>
      <el-form-item label="会话名称" prop="conversationTitle">
        <el-input
          style="width: 85%;"
          clearable
          v-model="selectedConversation.conversationTitle"
          placeholder="请输入会话名称"
          @keyup.enter="submitRenameForm"
        />
      </el-form-item>
    </el-form>
    <template #footer>
      <div class="dialog-footer">
        <el-button :loading="buttonLoading" type="primary" @click="submitRenameForm">确 定</el-button>
        <el-button @click="() => {openRename = false}">取 消</el-button>
      </div>
    </template>
  </el-dialog>
</template>

<script setup lang="ts">
import {
  ConversationConstants,
  ConversationInfoForm,
  ConversationInfoQuery,
  ConversationInfoVO
} from "@/api/witdock/conversationInfo/types";
import {
  addConversationInfo,
  delConversationInfo,
  listConversationInfo,
  updateConversationInfo
} from "@/api/witdock/conversationInfo/api";
import Chat from "@/views/witdock/chat.vue";
import {getApp, listApp} from "@/api/witdock/app";
import {AppVO} from "@/api/witdock/app/type";

const route = useRoute();
const appCode = route.params.code;
const currAppId = ref<string | number>('');
const chatToken = localStorage.getItem('chatToken');

const props = defineProps({
  propAppId: {
    type: String
  },
  isVistor: {
    type: Boolean,
    default: false
  }
})

const renameRules = reactive({
  conversationTitle: [
    {required: true, message: "会话名称不能为空", trigger: "blur"}
  ]
})

const conversationList = ref<ConversationInfoVO[]>([]);
const loading = ref(false);
const currConversationId = ref('')
const conversationRenameRef = ref(null);
const selectedConversation = ref({conversationTitle: ''});
const openRename = ref(false);
const buttonLoading = ref(false);
const currApp = ref<AppVO>(<AppVO>{});

async function startConversation() {
  await newConversation();
  await chooseConversation(conversationList.value[0]);
}

const newConversation = async () => {
  loading.value = true
  let conversation: ConversationInfoForm = {
    appId: currAppId.value,
    chatToken: props.isVistor && chatToken != null ? chatToken : '',
    createBy: props.isVistor ? 0 : null,
  }
  await addConversationInfo(conversation)
  await initConversationList()
}

const initConversationList = async () => {
  loading.value = true
  let conversationQuery: ConversationInfoQuery = {
    appId: currAppId.value,
    pageNum: 1,
    pageSize: ConversationConstants.QuerySize,
    isAsc: "desc,desc",
    orderByColumn: "topping,updateTime"
  }
  const res = await listConversationInfo(
    {
      ...conversationQuery,
      chatToken: props.isVistor && chatToken != null ? chatToken : '',
      createBy: props.isVistor ? 0 : null
    } as ConversationInfoQuery,
  );
  conversationList.value = res.rows
  loading.value = false
}


const chooseConversation = async (item: ConversationInfoVO) => {
  currConversationId.value = item.id as string
}

watch(() => props.propAppId, async () => {
  if (props.propAppId) {
    currAppId.value = props.propAppId;
    let res = await getApp(currAppId.value);
    currApp.value = res.data;
    await initConversationList();
    currConversationId.value = '';
  }
}, {immediate: true});

function handleTopPost(conversationId: number | string, isTopping: boolean) {
  updateConversationInfo({id: conversationId, topping: isTopping}).then((res) => {
    let info = (isTopping) ? "置顶" : "取消置顶"
    if (res.code === 200) {
      initConversationList().finally(() => {
        ElMessage.success(info + '成功')
      })
    } else {
      ElMessage.warning(info + '失败')
    }
  })
}

function handleRename(conversationId: string | number, conversationTitle: string) {
  selectedConversation.value = {id: conversationId, conversationTitle: conversationTitle} as ConversationInfoVO;
  openRename.value = true;
}

function handleDelete(conversationId: string | number) {
  delConversationInfo(conversationId).then((res) => {
    if (res.code === 200) {
      ElMessage.success("删除成功");
      if (currConversationId.value === conversationId) {
        currConversationId.value = '';
      }
      initConversationList();
    } else {
      ElMessage.warning("删除失败");
    }
  })
}

/** 提交按钮 */
const submitRenameForm = () => {
  (conversationRenameRef.value as any).validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      await updateConversationInfo(selectedConversation.value).finally(() => buttonLoading.value = false);
      ElMessage.success("会话重命名成功")
      openRename.value = false;
      await initConversationList();
    }
  });
}
</script>

<style scoped></style>
