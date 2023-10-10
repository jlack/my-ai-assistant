<template>
  <div class="app-container">
    {{datasetId}}
    <steps-add/>
<!--    <el-steps :active="active" finish-status="success">-->
<!--      <el-step title="选择数据源"/>-->
<!--      <el-step title="文本分段与清洗"/>-->
<!--      <el-step title="处理与完成"/>-->
<!--    </el-steps>-->

<!--    <div v-if="active==0">-->
<!--      <file-upload :fileType="['txt','html','md','pdf','xlsx','csv','docx']" v-model="docForm.ossIds"/>-->
<!--      <el-button :disabled="docForm.ossIds === undefined || docForm.ossIds.length === 0"-->
<!--                 style="margin-top: 12px" @click="next">下一步-->
<!--      </el-button>-->
<!--    </div>-->
<!--    <div v-if="active==1">-->
<!--      <el-button style="margin-top: 12px" @click="pre">上一步</el-button>-->
<!--      <el-button style="margin-top: 12px" @click="next">下一步</el-button>-->
<!--    </div>-->
<!--    <div v-if="active==2">-->
<!--      <el-form v-if="!datasetId" ref="datasetFormRef" :model="dataSetForm" title="新增数据集" :rules="rules"-->
<!--               label-width="80px">-->
<!--        <el-form-item label="数据集名称" prop="datasetName">-->
<!--          <el-input v-model="dataSetForm.datasetName" placeholder="请输入数据集名称"/>-->
<!--        </el-form-item>-->
<!--        <el-form-item label="数据集描述" prop="datasetDesc">-->
<!--          <el-input v-model="dataSetForm.datasetDesc" placeholder="请输入数据集描述"/>-->
<!--        </el-form-item>-->
<!--      </el-form>-->
<!--      <el-button style="margin-top: 12px" @click="pre">上一步</el-button>-->
<!--      <el-button :loading="buttonLoading" style="margin-top: 12px"-->
<!--                 type="primary" @click="handleComplete">完成-->
<!--      </el-button>-->
<!--    </div>-->
  </div>
</template>

<script lang="ts" setup>
import {ref} from 'vue'
import {addDoc, addDocs, updateDoc} from "@/api/witdock/datasetDoc/api";
import {AddDocsBo, DocForm, DocVO} from "@/api/witdock/datasetDoc/type";
import {addDataset, addDatasetWithDocs, updateDataset} from "@/api/witdock/dataset";
import {DatasetForm} from "@/api/witdock/dataset/types";
import {Burger} from "@element-plus/icons-vue";
import {useRouter} from "vue-router";
import StepsAdd from "@/views/witdock/dataset/stepsAdd.vue";

const router = useRouter();
const datasetId = useRoute().params.id as string;

</script>
