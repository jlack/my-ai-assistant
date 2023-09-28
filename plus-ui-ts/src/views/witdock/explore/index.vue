<template>
  <div>
    <h2 class="mb-2">工作区</h2>
    <el-row :gutter="10">
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
        <el-card class="box-card" v-loading="loading" v-if="currentAppId">
          <el-row :gutter="10">
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
                                            @click="handleTopPost(conversationItem.id)">置顶
                          </el-dropdown-item>
                          <el-dropdown-item v-if="conversationItem.topping" icon="CaretBottom"
                                            @click="handleUnTopPost(conversationItem.id)">取消置顶
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
              <div style="height: 500px">
                <chat v-if="currConversationId" :conversation-id="currConversationId"></chat>
                <div v-if="!conversationList?.length > 0 && loading === false" style="height: 100%; display: flex; justify-content: center; align-items: center">
                  <el-card :header="currentAppName" style="height: 110px; width: 50%; margin: auto; border-radius: 20px">
                    <el-button type="primary" style="border-radius: 12px"
                               @click="startConversation()">
                      <el-icon class="mr5"><ChatDotSquare /></el-icon>开始对话
                    </el-button>
                  </el-card>
                </div>
              </div>

            </el-col>
          </el-row>
        </el-card>
      </el-col>
    </el-row>

    <el-dialog title="会话重命名" v-model="openRename" width="500px" :append-to-body="true">
      <el-form ref="conversationRenameRef" :model="selectedConversation" :rules="renameRules" label-width="100px">
        <el-form-item label="会话名称" prop="conversationTitle">
          <el-input style="width: 85%;" clearable v-model="selectedConversation.conversationTitle"
                    placeholder="请输入会话名称"/>
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


import {listApp} from "@/api/witdock/app";
import {AppVO} from "@/api/witdock/app/type";
import Chat from "@/views/witdock/chat.vue";
import {ConversationInfoForm, ConversationInfoQuery, ConversationInfoVO} from "@/api/witdock/conversationInfo/types";
import {
  addConversationInfo,
  delConversationInfo,
  listConversationInfo,
  updateConversationInfo
} from "@/api/witdock/conversationInfo/api";
import {listMessageInfo} from "@/api/witdock/messageInfo/api";
import {MessageInfoVO} from "@/api/witdock/messageInfo/types";

const topConversationNum = ref(0);
const conversationRenameRef = ref(null);
const selectedConversation = ref({});
const openRename = ref(false);
const buttonLoading = ref(false);
const appList = ref<AppVO[]>([])
const conversationList = ref<ConversationInfoVO[]>([])
const currentAppId = ref('')
const currentAppName = ref('')
const currConversationId = ref('')
const loading = ref(false)
const renameRules = reactive({
  conversationTitle: [
    {required: true, message: "会话名称不能为空", trigger: "blur"}
  ]
})


const init = async () => {
  let res = await listApp();
  appList.value = res.rows
}

async function startConversation() {
  await newConversation();
  await chooseConversation(conversationList.value[0]);
}

const newConversation = async () => {
  loading.value = true
  let conversation: ConversationInfoForm = {
    appId: currentAppId.value
  }
  await addConversationInfo(conversation)
  await initConversationList()
}

const chooseApp = async (item: AppVO) => {
  currentAppId.value = item.id as string
  let currApp = appList.value.find(app => app.id === currentAppId.value);
  currentAppName.value = currApp ? currApp.appName : '';
  await initConversationList()
}
const chooseConversation = async (item: ConversationInfoVO) => {
  currConversationId.value = item.id as string
}

const initConversationList = async () => {
  loading.value = true
  let conversationQuery: ConversationInfoQuery = {
    appId: currentAppId.value,
    pageNum: 1,
    pageSize: 20,
    isAsc: "desc,desc",
    orderByColumn: "topping,updateTime"
  }
  const res = await listConversationInfo(conversationQuery);
  conversationList.value = res.rows
  topConversationNum.value = res.rows.filter(conversationItem => conversationItem.topping === true).length;
  loading.value = false
}

function handleTopPost(conversationId: number | string) {
  updateConversationInfo({id: conversationId, topping: true}).then((res) => {
    if (res.code === 200) {
      initConversationList().finally(() => {
        ElMessage.success("置顶成功")
      })
    } else {
      ElMessage.warning("置顶失败")
    }
  })
}

function handleUnTopPost(conversationId: number | string) {
  updateConversationInfo({id: conversationId, topping: false}).then((res) => {
    if (res.code === 200) {
      initConversationList().finally(() => {
        ElMessage.success("置顶成功")
      })
    } else {
      ElMessage.warning("置顶失败")
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
  (conversationRenameRef.value)?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      await updateConversationInfo(selectedConversation.value).finally(() => buttonLoading.value = false);
      ElMessage.success("会话重命名成功")
      openRename.value = false;
      await initConversationList();
    }
  });
}

init()

</script>
<style scoped lang="scss">

</style>
