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
      <el-tab-pane label="设置" name="third">
        <el-text tag="b" size="large">数据集设置</el-text>
        <br>
        <el-text>在这里您可以修改数据集的工作方式以及其他设置</el-text>
        <el-form class="mt20" ref="form" :model="datasetInfo" label-width="200px">
          <el-form-item label="数据集名称">
            <el-input v-model="datasetInfo.datasetName" style="width: 500px;"></el-input>
          </el-form-item>
          <el-form-item label="数据集描述">
            <el-input type="textarea" v-model="datasetInfo.datasetName" style="width: 500px;"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary">保存</el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup lang="ts">
const id = useRoute().params.id;
import {ref} from 'vue'
import {Search} from '@element-plus/icons-vue'
import type {TabsPaneContext} from 'element-plus'

const datasetInfo = ref({})
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
