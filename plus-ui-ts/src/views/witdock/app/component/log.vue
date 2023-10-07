<template>
  <el-form ref="form" :model="queryParams" :inline="true" label-width="80px">
    <el-form-item>
      <el-date-picker
        v-model="dateRange"
        value-format="YYYY-MM-DD HH:mm:ss"
        type="daterange"
        unlink-panels
        range-separator="至"
        start-placeholder="开始时间"
        end-placeholder="截止时间"
        :shortcuts="shortcuts"></el-date-picker>
    </el-form-item>
    <el-form-item>
      <el-form-item label="活动区域">
        <el-select v-model="form.scope" placeholder="请选择活动区域">
          <el-option label="全部" value="all"></el-option>
          <el-option label="已标注改进项" value="flag"></el-option>
          <el-option label="未标注" value="noflag"></el-option>
        </el-select>
      </el-form-item>
    </el-form-item>
    <el-form-item>
      <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
      <el-button icon="Refresh" @click="resetQuery">重置</el-button>
    </el-form-item>
  </el-form>
  <el-table v-loading="loading" :data="conversationList" stripe>
    <el-table-column label="发起时间" align="center" prop="createTime"/>
    <el-table-column label="发起用户" align="center" prop="createByName"/>
    <el-table-column label="会话标题" align="center" prop="conversationTitle"/>
    <el-table-column label="消息数" align="center" prop="msgNum"/>
    <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
      <template #default="scope">
        <el-button link type="primary" icon="View" @click="handleViewMsgInfo(scope.row.id)">
          查看会话日志
        </el-button>
      </template>
    </el-table-column>
  </el-table>

  <el-dialog v-model="openMsgInfo" width="65%" style="height: 80%">
    <div><span>对话id: {{ queryParams.conversationId }}</span></div>
    <!--消息日志展示表-->
    <el-table v-loading="loading" :data="msgInfoList" stripe max-height="500px">
      <el-table-column label="序号" width="70" type="index" align="center">
        <template #default="scope">
          <span>{{ (queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1 }}</span>
        </template>
      </el-table-column>
      <el-table-column label="提问内容" align="center" prop="query"/>
      <el-table-column label="回答内容" align="center" prop="answer"/>
      <el-table-column label="回答时间" align="center" prop="reDatetime" width="180">
        <template #default="scope">
          <span>{{ parseTime(scope.row.reDatetime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="花费token" align="center" prop="msgToken"/>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getMsgInfoList"
    />
  </el-dialog>
</template>
<script setup lang="ts">
import {ref} from "vue";
import {listMessageInfo} from "@/api/witdock/messageInfo/api";
import {listConversationInfo} from "@/api/witdock/conversationInfo/api";
import {ConversationInfoQuery, ConversationInfoVO} from "@/api/witdock/conversationInfo/types";
import {func} from "vue-types";
import {MessageInfoVO} from "@/api/witdock/messageInfo/types";

const { proxy } = getCurrentInstance() as ComponentInternalInstance;
const dateRange = ref([]);
const total = ref(0);
const msgInfoList = ref<MessageInfoVO[]>([])
const openMsgInfo = ref(false);
const loading = ref(false)
const conversationList = ref<ConversationInfoVO[]>([]);
const form = ref({scope: "all"})
const shortcuts = [
  {
    text: '今天',
    value: [new Date(), new Date()],
  },
  {
    text: '今年',
    value: () => {
      const end = new Date()
      const start = new Date(new Date().getFullYear(), 0)
      return [start, end]
    },
  },
  {
    text: '前6个月',
    value: () => {
      const end = new Date()
      const start = new Date()
      start.setMonth(start.getMonth() - 6)
      return [start, end]
    },
  },
]

const queryParams = ref({
  pageNum: 1,
  pageSize: 10,
  conversationId: undefined,
  query: undefined,
  answer: undefined,
  reDatetime: undefined,
  msgToken: undefined,
  isAsc: undefined,
  orderByColumn: undefined,
  params: {}
});

const currentAppId = useRoute().params.id;

const initConversationList = async () => {
  loading.value = true
  let conversationQuery: ConversationInfoQuery = {
    appId: currentAppId as string,
    isAsc: "desc",
    orderByColumn: "createTime",
    pageNum: 1,
    pageSize: 10
  }
  const res = await listConversationInfo(conversationQuery);
  conversationList.value = res.rows
  loading.value = false
}

/** 查询会话日志表列表 */
const getMsgInfoList = async () => {
  loading.value = true;
  const res = await listMessageInfo(queryParams.value);
  msgInfoList.value = res.rows;
  total.value = res.total;
  loading.value = false;
}

function handleViewMsgInfo(conversationId: any) {
  openMsgInfo.value = true;
  queryParams.value.conversationId = conversationId;
  getMsgInfoList();
}

async function handleQuery() {
  queryParams.value.pageNum = 1;
  proxy?.addDateRange(queryParams.value, dateRange.value, 'CreateTime');
  loading.value = true;
  let res = await listConversationInfo(queryParams.value)
  conversationList.value = res.rows;
  loading.value = false;
}

function resetQuery() {
  dateRange.value = [];
  handleQuery();
}

initConversationList()

</script>
<style scoped lang="scss">

</style>
