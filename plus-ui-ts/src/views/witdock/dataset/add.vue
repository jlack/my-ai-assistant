<template>
  <div class="app-container">
    {{datasetId}}
    <el-steps :active="active" finish-status="success">
      <el-step title="选择数据源"/>
      <el-step title="文本分段与清洗"/>
      <el-step title="处理与完成"/>
    </el-steps>

    <div v-if="active==0">
      <file-upload :fileType="['txt','html','md','pdf','xlsx','csv','docx']" v-model="docForm.ossIds"/>
      <el-button :disabled="docForm.ossIds === undefined || docForm.ossIds.length === 0"
                 style="margin-top: 12px" @click="next">下一步
      </el-button>
    </div>
    <div v-if="active==1">
      <el-button style="margin-top: 12px" @click="pre">上一步</el-button>
      <el-button style="margin-top: 12px" @click="next">下一步</el-button>
    </div>
    <div v-if="active==2">
      <el-form v-if="!datasetId" ref="datasetFormRef" :model="dataSetForm" title="新增数据集" :rules="rules"
               label-width="80px">
        <el-form-item label="数据集名称" prop="datasetName">
          <el-input v-model="dataSetForm.datasetName" placeholder="请输入数据集名称"/>
        </el-form-item>
        <el-form-item label="数据集描述" prop="datasetDesc">
          <el-input v-model="dataSetForm.datasetDesc" placeholder="请输入数据集描述"/>
        </el-form-item>
      </el-form>
      <el-button style="margin-top: 12px" @click="pre">上一步</el-button>
      <el-button :loading="buttonLoading" style="margin-top: 12px"
                 type="primary" @click="handleComplete">完成
      </el-button>
    </div>
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

const router = useRouter();
const datasetId = useRoute().params.id as string;

const docForm = ref<Partial<AddDocsBo>>({});
const dataSetForm = ref<Partial<DatasetForm>>({});
// const docForm = ref<DocVO>()
const active = ref(0)
const buttonLoading = ref(false);
const datasetFormRef = ref<ElFormInstance>();

const next = () => {
  if (active.value++ > 2)
    active.value = 0
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

async function handleComplete() {
  console.log("call handleCom" + datasetId)
  if (!datasetId) {
    datasetFormRef.value?.validate(async (valid: boolean) => {
      if (valid) {
        buttonLoading.value = true;
        await addDatasetWithDocs({...dataSetForm.value, ...docForm.value})
          .then((res) => {
            if (res.code === 200) {
              ElMessage.success("数据集" + dataSetForm.value.datasetName + "新增成功")
              router.push("/dataset");
            } else {
              ElMessage.error("数据集新增失败")
            }
          })
          .finally(() => buttonLoading.value = false);

      } else {
        ElMessage.error("请填写必要数据")
      }
    });
  } else {
    await addDocs({datasetId: datasetId, ossIds: docForm.value.ossIds})
      .then((res) => {
        if (res.code === 200) {
          ElMessage.success("文件上传成功")
          router.push("/dataset/detail/" + datasetId);
        } else {
          ElMessage.error("文件上传失败")
        }
      })
      .finally(() => buttonLoading.value = false);
  }

}

const rules = ref({
  id: [
    {required: true, message: "不能为空", trigger: "blur"}
  ],
  datasetName: [
    {required: true, message: "数据集名称不能为空", trigger: "blur"}
  ],
  datasetDesc: [
    {required: true, message: "数据集描述不能为空", trigger: "blur"}
  ],
  visiblePermission: [
    {required: true, message: "me,all不能为空", trigger: "change"}
  ],
  isDeleted: [
    {required: true, message: "是否删除不能为空", trigger: "blur"}
  ]
});


</script>
