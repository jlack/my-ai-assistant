<template>
  <div class="app-container">
    <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
      <el-tab-pane label="概览" name="first">
        <el-card class="box-card mb10" shadow="never">
          <el-row>
            <el-col :span="6">
              <el-statistic title="全部消息数" :value="268500"/>
            </el-col>
            <el-col :span="6">
              <el-statistic :value="138">
                <template #title>
                  <div style="display: inline-flex; align-items: center">
                    调用次数
                    <el-icon style="margin-left: 4px" :size="12">
                      <Male/>
                    </el-icon>
                  </div>
                </template>
                <template #suffix>/100</template>
              </el-statistic>
            </el-col>
            <el-col :span="6">
              <el-statistic title="活跃用户数" :value="172000"/>
            </el-col>
            <el-col :span="6">
              <el-statistic title="费用消耗" :value="562">
                <template #suffix>
                  <el-icon style="vertical-align: -0.125em">
                    <ChatLineRound/>
                  </el-icon>
                </template>
              </el-statistic>
            </el-col>
          </el-row>
        </el-card>
        <el-row :gutter="10">
          <el-col :span="12">
            <el-card class="box-card" shadow="never">
              <div slot="header" class="clearfix">
                <span>{{ app.appName }}</span>
                <el-switch
                  v-model="app.enableSite"
                  class="ml-2"
                  inline-prompt
                  style=" float: right;--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949"
                  active-text="运行中"
                  inactive-text="已停用"
                  @change="enableSite(app)"
                />
              </div>
              <el-text>公开访问URL</el-text>
              <el-input readonly :value="visitUrl" ref="appUrlRef" id="appUrl">
                <template #append>
                  <el-row :gutter="30">
                    <el-col :span="12">
                      <el-button :icon="DocumentCopy" @click="copyVisitUrl()"/>
                    </el-col>
                    <el-col :span="12">
                      <el-button :icon="Refresh" @click="resetUrl(app)"/>
                    </el-col>
                  </el-row>
                </template>
              </el-input>
              <el-row class="mt10">
                <el-button type="primary" plain @click="handlePreview()">
                  <el-icon class="mr5">
                    <Position/>
                  </el-icon>
                  预览
                </el-button>
                <el-button type="primary" plain>
                  <el-icon class="mr5">
                    <Tools/>
                  </el-icon>
                  设置
                </el-button>
              </el-row>
            </el-card>
          </el-col>
          <el-col :span="12">
            <el-card class="box-card" shadow="never">
              <div slot="header" class="clearfix">
                <span>后端服务 API</span>
                <el-switch
                  @change="enableApi(app)"
                  v-model="app.enableApi"
                  class="ml-2"
                  inline-prompt
                  style=" float: right;--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949"
                  active-text="运行中"
                  inactive-text="已停用"
                />
              </div>
              <el-text>api访问依据</el-text>
              <el-input>
                <template #append>
                  <el-button :icon="DocumentCopy"/>
                </template>
              </el-input>
              <el-row class="mt10">
                <el-button type="primary" plain>
                  <el-icon class="mr5">
                    <Key/>
                  </el-icon>
                  API 密钥
                </el-button>
                <el-button type="primary" plain>
                  <el-icon class="mr5">
                    <Document/>
                  </el-icon>
                  查阅API文档
                </el-button>
              </el-row>

            </el-card>
          </el-col>
        </el-row>

      </el-tab-pane>
      <el-tab-pane label="提示词编排" name="second">
        <Config :id=id></Config>
      </el-tab-pane>
      <el-tab-pane label="访问API" name="third"></el-tab-pane>
      <el-tab-pane label="日志与标注" name="fourth">
        <log></log>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>
<script lang="ts" setup>
import {ref} from 'vue'
import Config from "@/views/witdock/app/component/config.vue";
import Log from "@/views/witdock/app/component/log.vue";
import type {TabsPaneContext} from 'element-plus'
import {getApp, updateApp, resetCode} from "@/api/witdock/app";
import {AppVO, AppForm} from "@/api/witdock/app/type";
import {DocumentCopy, Refresh} from "@element-plus/icons-vue";
import useClipboard from 'vue-clipboard3';


const {proxy} = getCurrentInstance() as ComponentInternalInstance
const router = useRouter();
const {toClipboard} = useClipboard()
const activeName = ref('first')
const id = (useRoute().params.id || 0) as string;
const app = ref<AppVO>({
  id: "",
  appName: "",
  appDesc: "",
  code: "",
  enableSite: false,
  enableApi: false,
  datasetIds: [],
  prolog: ""
});
const appUrlRef = ref();
const visitUrl = ref('');

function handlePreview() {
  window.open("/chatPage/" + app.value.code, "_blank");
}

async function copyVisitUrl() {
  try {
    await toClipboard(visitUrl.value);
    ElMessage.success("复制成功")
  } catch (e) {
    console.error(e)
  }
}

const resetUrl = async (app: AppVO) => {
  await proxy?.$modal.confirm('确认要重置URL吗?');
  const res = await resetCode(app.id)
  await init()
  proxy?.$modal.msgSuccess(res.msg)
}

const enableSite = async (app: AppVO) => {
  let text = app.enableSite ? "启用" : "停用"
  try {
    await proxy?.$modal.confirm('确认要"' + text + '""' + app.appName + '"吗?');
    let appForm: AppForm | null
    appForm = {
      id: app.id,
      enableSite: app.enableSite
    }
    await updateApp(appForm);
    proxy?.$modal.msgSuccess(text + "成功");
  } catch (err) {
    app.enableSite = !app.enableSite;
  }
}

const enableApi = async (app: AppVO) => {
  let text = app.enableApi ? "启用" : "停用"
  try {
    await proxy?.$modal.confirm('确认要"' + text + '""' + app.appName + '"的后端服务API吗?');
    let appForm: AppForm | null
    appForm = {
      id: app.id,
      enableApi: app.enableApi
    }
    await updateApp(appForm);
    proxy?.$modal.msgSuccess(text + "成功");
  } catch (err) {
    app.enableApi = !app.enableApi;
  }
}


const handleClick = (tab: TabsPaneContext, event: Event) => {
  console.log(tab, event)
}
const init = async () => {
  const res = await getApp(id)
  app.value = res.data;
  visitUrl.value = 'https://witdock.manascloud.com/chatPage/' + app.value.code;
}
init()
</script>
<style>

</style>
