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
  <el-table
    :data="[{ date: '2016-05-02', name: '王小虎', address: '上海市普陀区金沙江路 1518 弄' }, { date: '2016-05-04', name: '王小虎', address: '上海市普陀区金沙江路 1517 弄' }]"
    style="width: 100%">
    <el-table-column
      prop="date"
      label="时间"
      width="180">
    </el-table-column>
    <el-table-column
      prop="name"
      label="用户"
      width="180">
    </el-table-column>
    <el-table-column
      prop="address"
      label="摘要">
    </el-table-column>
  </el-table>
</template>
<script setup lang="ts">
import {ref} from "vue";

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
</script>
<style scoped lang="scss">

</style>
