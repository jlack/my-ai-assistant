<template>
  <div class="app-container">
    {{ active }}
    <el-steps :active="active" finish-status="success">
      <el-step title="选择数据源"/>
      <el-step title="文本分段与清洗"/>
      <el-step title="处理与完成"/>
    </el-steps>

    <div v-if="active==0">
      <file-upload></file-upload>
      <el-button style="margin-top: 12px" @click="next">下一步</el-button>
    </div>
    <div v-if="active==1">
      <el-button style="margin-top: 12px" @click="pre">上一步</el-button>
      <el-button style="margin-top: 12px" @click="next">下一步</el-button>
    </div>
    <div v-if="active==2">
      <el-button style="margin-top: 12px" @click="pre">上一步</el-button>
      <el-button style="margin-top: 12px" type="primary">完成</el-button>
    </div>


  </div>
</template>

<script lang="ts" setup>
import {ref} from 'vue'

const active = ref(0)

const next = () => {
  if (active.value++ > 2) active.value = 0
}
const pre = () => {
  active.value--
}
</script>
