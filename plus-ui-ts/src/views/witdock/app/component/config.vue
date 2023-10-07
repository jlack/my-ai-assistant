<template>
  <div style="width: 50%">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>上下文</span>
        <el-link style="float: right; padding: 3px 0; color: #409EFF" :underline="false" @click="handleAddDataset">
          <el-icon class="el-icon--right"><Plus /></el-icon>增加
        </el-link>
      </div>
      <el-row :gutter="10">
        <el-col :span="12" v-for="item in dataSetList">
          <el-card class="box-card" shadow="never">
            {{ item.datasetName }}
            <el-link style="float: right; color: #409EFF" :underline="false" @click="handleUnRef(appId, item.id)">
              <el-icon class="el-icon--right"><Delete /></el-icon>
            </el-link>
          </el-card>
        </el-col>
      </el-row>
    </el-card>
    <el-card class="box-card mt20">
      <div slot="header" class="clearfix">
        <span>对话开场白</span>
        <el-link style="float: right; padding: 3px 0; color: #409EFF" :underline="false" @click="editProlog=true">
          <el-icon class="el-icon--right"><Edit /></el-icon>编辑
        </el-link>
      </div>
      <div v-if="editProlog">
        <el-input type="textarea" v-model="app.prolog"></el-input>
        <div class="mt10 mb10" style="float: right">
          <el-button @click="editProlog=!editProlog">取消</el-button>
          <el-button @click="saveProlog" type="primary">确定</el-button>
        </div>
      </div>
      <el-text v-else-if="app.prolog">{{ app.prolog }}</el-text>
      <el-text v-else>在对话型应用中，让AI主动说第一段话可以拉近与用户间的距离</el-text>
    </el-card>


    <el-dialog
      v-model="dialogVisible"
      title="选择引用数据集"
      width="50%"
    >
      <el-table
        ref="multipleTableRef"
        :data="dialogDataSetList"
        v-loading="dialogTableLoading"
        style="width: 100%"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55"/>
        <el-table-column prop="datasetName" label="数据集名称"/>
        <el-table-column prop="charNum" label="字符数" width="80"/>
        <el-table-column prop="docNum" label="文档数" width="80"/>
      </el-table>
      <template #footer>
      <span class="dialog-footer">
        <el-text style="float: left">{{ multipleSelection.length }}个数据集被选中</el-text>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleAddDatasetToApp">
          确定
        </el-button>
      </span>
      </template>
    </el-dialog>
  </div>
</template>
<script setup lang="ts">
import {listDataset} from "@/api/witdock/dataset";
import {addDatasetToApp, getConfig, updateApp} from "@/api/witdock/app";
import {DatasetVO} from "@/api/witdock/dataset/types";
import {AppForm, AppVO} from "@/api/witdock/app/type";
import {ElTable} from "element-plus";
import {delAppDataset, delAppDatasetByBothId} from "@/api/witdock/appDataset";

const {proxy} = getCurrentInstance() as ComponentInternalInstance;
const route = useRoute();
const appId = route.params.id;

const props = defineProps({
  id: {
    type: String,
    required: true
  }
})

const multipleSelection = ref<DatasetVO[]>([])
const dialogDataSetList = ref<DatasetVO[]>([]);
const dataSetList = ref<DatasetVO[]>([]);

const editProlog = ref(false)
const dialogVisible = ref(false)
const dialogTableLoading = ref(false)
const multipleTableRef = ref<InstanceType<typeof ElTable>>()
const app = ref(<AppVO>{});

const init = async () => {
  const res = await getConfig(props.id)
  app.value = res.data
  dataSetList.value = res.data.datasetInfoVoList
}

const saveProlog = async () => {
  let appForm: AppForm
  appForm = {
    id: props.id,
    prolog: app.value.prolog
  }
  await updateApp(appForm)
  proxy?.$modal.msgSuccess("开场白保存成功")
  editProlog.value = false
}

const handleAddDatasetToApp = async () => {
  let datasetIds: (string | number)[] = []
  multipleSelection.value.forEach((dataset: DatasetVO) => {
    datasetIds.push(dataset.id)
  })
  const res = await addDatasetToApp({"id": props.id, "datasetIds": datasetIds})
  proxy?.$modal.msgSuccess("操作成功");
  dialogVisible.value = false
  init()
}
const handleAddDataset = () => {
  dialogVisible.value = true
  fetchDataset()
}

// 解除app对dataset的引用
const handleUnRef = (appId: any, datasetId: any) => {
  delAppDatasetByBothId({ appId: appId, datasetId: datasetId })
    .then((res) => {
    if (res.code === 200) {
      ElMessage.success('解除数据集上下文成功');
      // fetchDataset();
      init();
    } else {
      ElMessage.warning('解除数据集上下文失败')
    }
  })
}
const fetchDataset = async () => {
  try {
    dialogDataSetList.value = []
    dialogTableLoading.value = true
    await listDataset().then(res => {
      dialogDataSetList.value = res.rows
      //坑点，nextTick函数保证DOM元素渲染完成后再进行表格的选中操作
      nextTick(() => {
        dialogDataSetList.value.forEach(row => {
          if ((app.value.datasetIds as string[]).includes(row.id as string)) {
            multipleTableRef.value!.toggleRowSelection(row, true)
          }
        })
      })
    })
    dialogTableLoading.value = false
  } catch (e) {
    dialogTableLoading.value = false
  }
}

const handleSelectionChange = (val: DatasetVO[]) => {
  multipleSelection.value = val
}
init()
</script>
<style scoped lang="scss">

</style>
