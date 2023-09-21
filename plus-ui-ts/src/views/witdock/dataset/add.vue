<template>
  <div class="app-container">
    <el-steps :active="active" finish-status="success">
      <el-step title="选择数据源"/>
      <el-step title="文本分段与清洗"/>
      <el-step title="处理与完成"/>
    </el-steps>

    <div v-if="active==0">
      {{docForm}}
      <file-upload :fileType="['txt','html','md','pdf','xlsx','csv','docx']" v-model="docForm.ossIds"></file-upload>
      <el-button style="margin-top: 12px" @click="next">下一步</el-button>
    </div>
    <div v-if="active==1">
      <el-button style="margin-top: 12px" @click="pre">上一步</el-button>
      <el-button style="margin-top: 12px" @click="next">下一步</el-button>
    </div>
    <div v-if="active==2">
      {{docForm}}<br>
      {{dataSetForm}}
      <el-form ref="datasetFormRef" :model="dataSetForm" title="新增数据集" :rules="rules" label-width="80px">
        <el-form-item label="数据集名称" prop="datasetName">
          <el-input v-model="dataSetForm.datasetName" placeholder="请输入数据集名称" />
        </el-form-item>
        <el-form-item label="数据集描述" prop="datasetDesc">
          <el-input v-model="dataSetForm.datasetDesc" placeholder="请输入数据集描述" />
        </el-form-item>
      </el-form>
      <el-button style="margin-top: 12px" @click="pre">上一步</el-button>
      <el-button :loading="buttonLoading" style="margin-top: 12px" type="primary" @click="handleComplete">完成</el-button>
    </div>


  </div>
</template>

<script lang="ts" setup>
import {ref} from 'vue'
import {addDoc, addDocs, updateDoc} from "@/api/witdock/datasetDoc/api";
import {AddDocsBo, DocForm, DocVO} from "@/api/witdock/datasetDoc/type";
import {addDataset, updateDataset} from "@/api/witdock/dataset";
import {DatasetForm} from "@/api/witdock/dataset/types";

const docForm = ref<Partial<AddDocsBo>>({});
const dataSetForm = ref<Partial<DatasetForm>>({});
// const docForm = ref<DocVO>()
const active = ref(0)
const buttonLoading = ref(false);
const datasetFormRef = ref<ElFormInstance>();

const next = () => {
  if (active.value++ > 2) active.value = 0
}

const pre = () => {
  active.value--
}

function addDS() {
  addDataset(dataSetForm.value).then((res) => {
    if (res.code === 200) {
      console.log("新增dataset成功");
    }
  })
}

function handleComplete() {
  datasetFormRef.value?.validate(async (valid: boolean) => {
    console.log("into validate")
    if (valid) {
      buttonLoading.value = true;
      if (dataSetForm.value.id) {
        await updateDataset(dataSetForm.value).finally(() =>  buttonLoading.value = false);
      } else {
        await addDataset(dataSetForm.value).finally(() =>  buttonLoading.value = false);
        await addDocs(docForm.value).then((res) => {
          if (res.code === 200) {
            ElMessage.success("上传数据集成功")
          } else {
            ElMessage.error("上传数据集失败")
          }
        })
      }
    }
  });
}

const rules = ref({
  id: [
    { required: true, message: "不能为空", trigger: "blur" }
  ],
  datasetName: [
    { required: true, message: "数据集名称不能为空", trigger: "blur" }
  ],
  datasetDesc: [
    { required: true, message: "数据集描述不能为空", trigger: "blur" }
  ],
  visiblePermission: [
    { required: true, message: "me\all不能为空", trigger: "change" }
  ],
  isDeleted: [
    { required: true, message: "是否删除不能为空", trigger: "blur" }
  ]
});


</script>
