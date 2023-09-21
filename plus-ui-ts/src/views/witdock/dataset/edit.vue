<template>
  <div class="app-container">
    <div>编辑id {{ id }}</div>
    <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
      <el-tab-pane label="文档" name="first">
        <el-row :gutter="10">
          <el-col :span="20">
            <el-input
              style="width: 220px"
              v-model="input2"
              class="w-50 m-2"
              placeholder="搜索"
              :prefix-icon="Search"
            />
          </el-col>

          <el-col :span="4">
            <el-button type="primary" style="float: right">添加文件</el-button>
          </el-col>
        </el-row>


        <el-table :data="tableData" style="width: 100%">
          <el-table-column prop="date" label="Date" width="180"/>
          <el-table-column prop="name" label="Name" width="180"/>
          <el-table-column prop="address" label="Address"/>
        </el-table>
      </el-tab-pane>
      <el-tab-pane label="命中测试" name="second"></el-tab-pane>
      <el-tab-pane label="设置" name="third"></el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup lang="ts">
const id = useRoute().params.id;
import {ref} from 'vue'
import {Search} from '@element-plus/icons-vue'
import type {TabsPaneContext} from 'element-plus'

const activeName = ref('first')
const tableData = [
  {
    date: '2016-05-03',
    name: 'Tom',
    address: 'No. 189, Grove St, Los Angeles',
  },
  {
    date: '2016-05-02',
    name: 'Tom',
    address: 'No. 189, Grove St, Los Angeles',
  },
  {
    date: '2016-05-04',
    name: 'Tom',
    address: 'No. 189, Grove St, Los Angeles',
  },
  {
    date: '2016-05-01',
    name: 'Tom',
    address: 'No. 189, Grove St, Los Angeles',
  },
]
const handleClick = (tab: TabsPaneContext, event: Event) => {
  console.log(tab, event)
}
</script>
<style>

</style>
