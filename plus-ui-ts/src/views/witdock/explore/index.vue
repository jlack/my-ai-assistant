<template>
  <h2 class="mb-2">工作区</h2>
  <el-row :gutter="10">
    <el-col :span="4">
      <el-menu class="el-menu-vertical-demo">
        <el-menu-item :index="appItem.id" v-for="appItem in appList" @click="chooseApp(appItem)">
          <el-icon>
            <icon-menu/>
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
                <el-button type="primary" @click="newSession()">新建会话</el-button>
              </el-menu-item>
              <div v-for="(sessionItem, index) in sessionList">
                <el-menu-item :index="sessionItem.id"
                              @click="chooseSession(sessionItem)" style="width: 100%">
                  {{ sessionItem.sessionTitle }}
                  <el-dropdown style="position: absolute; right: 4%;">
                    <el-button>...</el-button>
                    <template #dropdown>
                      <el-dropdown-menu>
                        <el-dropdown-item v-if="!sessionItem.topping" icon="CaretTop" @click="handleTopPost(sessionItem.id)">置顶</el-dropdown-item>
                        <el-dropdown-item v-if="sessionItem.topping" icon="CaretTop" @click="handleUnTopPost(sessionItem.id)">取消置顶</el-dropdown-item>
                        <el-dropdown-item icon="EditPen" @click="handleRename(sessionItem.id, sessionItem.sessionTitle)">
                          重命名
                        </el-dropdown-item>
                        <el-dropdown-item icon="Delete" @click="handleDelete(sessionItem.id)">删除</el-dropdown-item>
                      </el-dropdown-menu>
                    </template>
                  </el-dropdown>
                </el-menu-item>
                <el-divider content-class="custom-divider" v-if="index === topSessionNum - 1"/>
              </div>


            </el-menu>
          </el-col>
          <el-col :span="20">
            <chat v-if="currentSessionId" :session-id="currentSessionId"></chat>
          </el-col>
        </el-row>
      </el-card>
    </el-col>
  </el-row>


  <el-dialog title="会话重命名" v-model="openRename" width="500px" :append-to-body="true">
    <el-form ref="sessionRenameRef" :model="selectedSession" :rules="renameRules" label-width="100px">
      <el-form-item label="会话名称" prop="sessionTitle">
        <el-input style="width: 85%;" clearable v-model="selectedSession.sessionTitle" placeholder="请输入会话名称"/>
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


import {addApp, listApp, updateApp} from "@/api/witdock/app";
import {AppVO} from "@/api/witdock/app/type";
import {addSession, delSession, listSession, updateSession} from "@/api/witdock/session";
import {SessionForm, SessionQuery, SessionVO} from "@/api/witdock/session/types";
import Chat from "@/views/witdock/chat.vue";
import {listSessionLog} from "@/api/witdock/sessionLog/api";
import {SessionLogForm, SessionLogVO} from "@/api/witdock/sessionLog/type";

const topSessionNum = ref(0);
const sessionRenameRef = ref(null);
const selectedSession = ref({});
const openRename = ref(false);
const buttonLoading = ref(false);
const appList = ref<AppVO[]>([])
const sessionList = ref<SessionVO[]>([])
const sessionLogList = ref<SessionLogVO[]>([])
const currentAppId = ref('')
const currentSessionId = ref('')
const loading = ref(false)
const renameRules = reactive({
  sessionTitle: [
    {required: true, message: "会话名称不能为空", trigger: "blur"}
  ]
})


const init = async () => {
  let res = await listApp();
  appList.value = res.rows
}

const newSession = async () => {
  loading.value = true
  let session: SessionForm
  session = {
    appId: currentAppId.value
  }
  await addSession(session)
  await initSessionList()
}

const chooseApp = async (item: AppVO) => {
  currentAppId.value = item.id as string
  await initSessionList()
}
const chooseSession = async (item: SessionVO) => {
  currentSessionId.value = item.id as string
  // await initSessionLogList()
}

const initSessionLogList = async () => {
  loading.value = true
  let form: SessionLogForm
  form = {
    sessionId: currentSessionId.value
  }
  const res = await listSessionLog(form);
  sessionLogList.value = res.rows
  loading.value = false
}
const initSessionList = async () => {
  loading.value = true
  let sessionQuery: SessionQuery
  sessionQuery = {
    appId: currentAppId.value,
    pageNum: 1,
    pageSize: 20,
    isAsc: "desc,desc",
    orderByColumn: "topping,updateTime"
  }
  const res = await listSession(sessionQuery);
  sessionList.value = res.rows
  topSessionNum.value = res.rows.filter(sessionItem => sessionItem.topping === true).length;
  loading.value = false
}

function handleTopPost(sessionId: number | string) {
  updateSession({id: sessionId, topping: true}).then((res) => {
    if (res.code === 200) {
      initSessionList().finally(() => {
        ElMessage.success("置顶成功")
      })
    } else {
      ElMessage.warning("置顶失败")
    }
  })
}

function handleUnTopPost(sessionId: number | string) {
  updateSession({id: sessionId, topping: false}).then((res) => {
    if (res.code === 200) {
      initSessionList().finally(() => {
        ElMessage.success("置顶成功")
      })
    } else {
      ElMessage.warning("置顶失败")
    }
  })
}


function handleRename(sessionId: string | number, sessionTitle: string) {
  console.log(sessionId)
  selectedSession.value = {id: sessionId, sessionTitle: sessionTitle} as SessionVO;
  openRename.value = true;
}

function handleDelete(sessionId: string | number) {
  delSession(sessionId).then((res) => {
    if (res.code === 200) {
      ElMessage.success("删除成功");
      initSessionList();
    } else {
      ElMessage.warning("删除失败");
    }
  })
}

/** 提交按钮 */
const submitRenameForm = () => {
  (sessionRenameRef.value)?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      await updateSession(selectedSession.value).finally(() => buttonLoading.value = false);
      ElMessage.success("会话重命名成功")
      openRename.value = false;
      await initSessionList();
    }
  });
}

init()

</script>
<style scoped lang="scss">

</style>
