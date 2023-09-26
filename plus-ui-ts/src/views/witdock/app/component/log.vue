<template>
  <el-form ref="form" :model="form" :inline="true" label-width="80px">
    <el-form-item>
      <el-date-picker
        v-model="value2"
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
  </el-form>
  <el-table v-loading="loading" :data="sessionList">
    <el-table-column label="发起时间" align="center" prop="createTime"/>
    <el-table-column label="发起用户" align="center" prop="createByName"/>
    <el-table-column label="会话标题" align="center" prop="sessionTitle"/>
    <el-table-column label="消息数" align="center" prop="msgNum"/>
    <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
      <template #default="scope">
        <el-button link type="primary" icon="View" @click="handleViewLog(scope.row.id)">
          查看会话日志
        </el-button>
      </template>
    </el-table-column>
  </el-table>

  <el-dialog v-model="openSessionLog" width="65%" style="height: 80%">
    <div><span>对话id: {{ queryParams.sessionId }}</span></div>
    <!--    会话日志展示表-->
    <el-table v-loading="loading" :data="sessionLogList">
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
      @pagination="getList"
    />
  </el-dialog>
</template>
<script setup lang="ts">
import {ref} from "vue";
import {SessionQuery} from "@/api/witdock/session/types";
import {listSession} from "@/api/witdock/session";
import {SessionLogForm, SessionLogQuery, SessionLogVO} from "@/api/witdock/sessionLog/type";
import {listSessionLog} from "@/api/witdock/sessionLog/api";

const total = ref(0);
const sessionLogList = ref<SessionLogVO[]>([])
const currSessionId = ref('');
const openSessionLog = ref(false);
const loading = ref(false)
const sessionList = ref([]);
const form = ref({scope: "all"})
const value2 = ref('')
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
  sessionId: undefined,
  query: undefined,
  answer: undefined,
  reDatetime: undefined,
  msgToken: undefined,
  params: {}
});

const currentAppId = useRoute().params.id as String;

const initSessionList = async () => {
  loading.value = true
  let sessionQuery: SessionQuery = {
    appId: currentAppId,
    isAsc: "desc",
    orderByColumn: "createTime"
  }
  const res = await listSession(sessionQuery);
  sessionList.value = res.rows
  loading.value = false
}

/** 查询会话日志表列表 */
const getList = async () => {
  loading.value = true;
  const res = await listSessionLog(queryParams.value);
  sessionLogList.value = res.rows;
  total.value = res.total;
  loading.value = false;
}

function handleViewLog(sessionId) {
  openSessionLog.value = true;
  queryParams.sessionId = sessionId;
  getList();
}

initSessionList()

</script>
<style scoped lang="scss">

</style>
