<template>
  <div style="height: 100%;">
    <el-card class="box-card" :body-style="{height: '100%'}" style="height: 100%;" v-loading="loading" v-if="currAppId">
      <el-row :gutter="10" style="height: 100%">
        <el-col :span="4">
          <el-menu class="el-menu-demo">
            <el-menu-item>
              <el-button type="primary" @click="newConversation()">新建会话</el-button>
            </el-menu-item>
            <div v-for="(conversationItem, index) in conversationList">
              <el-menu-item :index="conversationItem.id"
                            @click="chooseConversation(conversationItem)"
                            style="width: 100%">
                {{ conversationItem.conversationTitle }}
                <el-dropdown style="position: absolute; right: 4%;">
                  <el-button>...</el-button>
                  <template #dropdown>
                    <el-dropdown-menu>
                      <el-dropdown-item v-if="!conversationItem.topping" icon="CaretTop"
                                        @click="handleTopPost(conversationItem.id, true)">置顶
                      </el-dropdown-item>
                      <el-dropdown-item v-if="conversationItem.topping" icon="CaretBottom"
                                        @click="handleTopPost(conversationItem.id, false)">取消置顶
                      </el-dropdown-item>
                      <el-dropdown-item icon="EditPen"
                                        @click="handleRename(conversationItem.id, conversationItem.conversationTitle)">
                        重命名
                      </el-dropdown-item>
                      <el-dropdown-item icon="Delete" @click="handleDelete(conversationItem.id)">删除
                      </el-dropdown-item>
                    </el-dropdown-menu>
                  </template>
                </el-dropdown>
              </el-menu-item>
              <el-divider content-class="custom-divider" v-if="index === topConversationNum - 1"/>
            </div>
          </el-menu>
        </el-col>
        <el-col :span="20">
          <div style="height: 100%;">
            <chat style="height: 100%" v-if="currConversationId" :conversation-id="currConversationId"/>
            <div v-if="!conversationList?.length > 0 && loading === false"
                 style="height: 100%; display: flex; justify-content: center; align-items: center">
              <el-card :header="currApp.appName" style="height: 110px; width: 50%; margin: auto; border-radius: 20px">
                <el-button type="primary" style="border-radius: 12px"
                           @click="startConversation()">
                  <el-icon class="mr5">
                    <ChatDotSquare/>
                  </el-icon>
                  开始对话
                </el-button>
              </el-card>
            </div>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <el-dialog title="会话重命名" v-model="openRename" width="500px" :append-to-body="true">
      <!--    @submit.native.prevent 可以阻止按回车刷新页面-->
      <el-form ref="conversationRenameRef" :model="selectedConversation" :rules="renameRules" label-width="100px"
               @submit.native.prevent>
        <el-form-item label="会话名称" prop="conversationTitle">
          <el-input style="width: 85%;" clearable v-model="selectedConversation.conversationTitle"
                    placeholder="请输入会话名称" @keyup.enter="submitRenameForm"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button :loading="buttonLoading" type="primary" @click="submitRenameForm">确 定</el-button>
          <el-button @click="() => {openRename = false}">取 消</el-button>
        </div>
      </template>
    </el-dialog>

  </div>
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

const topConversationNum = ref(0);
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
  topConversationNum.value = res.rows.filter(conversationItem => conversationItem.topping === true).length;
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

<style scoped>

</style>
