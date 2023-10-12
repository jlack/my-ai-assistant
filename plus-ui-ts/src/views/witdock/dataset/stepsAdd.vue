<template>
  <div class="app-container">
    <el-steps :active="active" finish-status="success">
      <el-step title="选择数据源"/>
      <el-step title="文本分段与清洗"/>
      <el-step title="处理与完成"/>
    </el-steps>
    <br>
    <div v-if="active==0">
      <file-upload :fileType="['txt','html','md','pdf','xlsx','csv','docx']" v-model="docForm.ossIds"/>
      <el-button :disabled="docForm.ossIds === undefined || docForm.ossIds.length === 0"
                 style="margin-top: 12px" @click="next">下一步
      </el-button>
    </div>
    <div v-if="active==1">
      <el-row class="mt10" :gutter="20">
        <el-col :span="16">
          <!--        {{'segResultList : ' + segResult}}-->
          <h2>分段参数</h2>
          <label>分段尺寸</label>
          <el-input style="width: 40%;" class="ml10" placeholder="请输入输入分段尺寸token值" v-model="maxSegSize"/>
          <el-button type="primary" plain class="ml20" @click="previewSegResult">预览分段结果</el-button>

          <el-divider></el-divider>
          <div class="mt20">
            <h2>待处理的文件</h2>
            <el-table :data="ossList" stripe>
              <el-table-column label="序号" width="70" type="index" align="center">
                <template #default="scope">
                  <span>{{ scope.$index + 1 }}</span>
                </template>
              </el-table-column>
              <el-table-column prop="originalName" label="文件名" width="250" align="center"/>
            </el-table>
          </div>
        </el-col>
        <el-col :span="8">
          <h2>分段预览</h2>
          <div class="para-card-container">
            <template v-for="(paragraphs, index) in segResult" :key="index">
              <el-card class="mt10" v-for="(content, contentIndex) in paragraphs" :key="contentIndex">
                <template #header>{{ '#' + (contentIndex + 1) }}</template>
                {{ content }}
              </el-card>
              <el-divider>文件分界线</el-divider>
            </template>
          </div>
        </el-col>
      </el-row>

      <el-button style="margin-top: 12px" @click="pre">上一步</el-button>
      <el-button style="margin-top: 12px" @click="next">下一步</el-button>
    </div>
    <div v-if="active==2">
      <el-form v-if="!props.datasetId" ref="datasetFormRef" :model="dataSetForm" title="新增数据集" :rules="rules"
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

<script setup lang="ts">
import {ref} from "vue";
import {AddDocsBo} from "@/api/witdock/datasetDoc/type";
import {DatasetForm} from "@/api/witdock/dataset/types";
import {addDataset, addDatasetWithDocs} from "@/api/witdock/dataset";
import {addDocs, getDocSegResult, splitDocToPara} from "@/api/witdock/datasetDoc/api";
import {showResultMsg} from "@/utils/message";
import {listByIds} from "@/api/system/oss";
import {OssVO} from "@/api/system/oss/types";


const props = defineProps({
  datasetId: {
    type: String,
    required: false
  }
})

const router = useRouter();
const maxSegSize = ref(0);
const docForm = ref<Partial<AddDocsBo>>({});
const dataSetForm = ref<Partial<DatasetForm>>({});
const active = ref(0)
const buttonLoading = ref(false);
const datasetFormRef = ref<ElFormInstance>();
const segResult = ref([]);
const ossList = ref<OssVO[]>([]);

const next = () => {
  if (active.value++ > 2)
    active.value = 0
}

const pre = () => {
  active.value--
}

function addDS() {
  addDataset(dataSetForm.value).then((res) => {
    showResultMsg(res, "新增dataset");
  })
}

async function handleDocSplit() {
  let res = await splitDocToPara({
    ossIds: docForm.value.ossIds as string,
    maxSegmentSizeInTokens: maxSegSize.value
  });
}

async function previewSegResult() {
  let ossListRes = await listByIds(docForm.value.ossIds as string);
  let res = await getDocSegResult({
    ossIds: docForm.value.ossIds as string,
    maxSegmentSizeInTokens: maxSegSize.value
  })
  segResult.value = res.data;
  ossList.value = ossListRes.data;
}

async function handleComplete() {
  if (!props.datasetId) {
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
    await addDocs({datasetId: props.datasetId, ossIds: docForm.value.ossIds})
      .then((res) => {
        if (res.code === 200) {
          ElMessage.success("文件上传成功")
          router.push("/dataset/detail/" + props.datasetId);
        } else {
          ElMessage.error("文件上传失败")
        }
      })
      .finally(() => buttonLoading.value = false);
  }
  // 上传doc后添加对应doc的分段
  await handleDocSplit();
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
  ]
});


</script>

<style scoped>
.para-card-container {
  height: 500px; /* 替换为你希望的固定高度 */
  overflow-y: auto;
}
</style>
