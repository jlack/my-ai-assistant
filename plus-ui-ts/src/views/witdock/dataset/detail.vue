<template>
  <div class="app-container">
    <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
      <el-tab-pane label="文档" name="first">
        <el-row :gutter="10">
          <el-col :span="20">
            <el-input
              style="width: 220px"
              v-model="queryParams.docName"
              class="w-50 m-2"
              @keyup.enter="handleQuery"
              placeholder="搜索文件名"
              clearable
              :prefix-icon="Search"
            />
            <el-button type="primary" @click="handleQuery">搜索</el-button>
            <el-button icon="Refresh" @click="resetQuery">重置</el-button>
          </el-col>

          <el-col :span="4">
            <file-upload :show-file-list="false"
                         :isShowTip="false"
                         :buttonText="'添加文件'"
                         :fileType="['txt','html','md','pdf','xlsx','csv','docx']"
                         v-model="ossIds"
                         :showFileList="false"
                         @addDoc="addDocInfo"
            />
          </el-col>
        </el-row>

        <el-table v-loading="loading" :data="docList">
          <el-table-column type="selection" width="55" align="center"/>
          <el-table-column label="id" align="center" prop="id" v-if="true"/>
          <el-table-column label="数据集id" align="center" prop="datasetId"/>
          <el-table-column label="文档名称" align="center" prop="docName"/>
          <!--          <el-table-column label="对象存储id" align="center" prop="ossId" />-->
          <el-table-column label="字符数" align="center" prop="charNum"/>
          <el-table-column label="状态" align="center" prop="status">
            <template #default="scope">
              <dict-tag :options="witdock_doc_status" :value="scope.row.status" />
            </template>
          </el-table-column>
          <el-table-column label="启用" align="center" prop="status">
            <template #default="scope">
              <el-switch
                v-model="scope.row.status"
                :disabled="scope.row.status === 'archived'"
                active-value="active"
                inactive-value="inactive"
                @change="updateDoc({id: scope.row.id, status: scope.row.status})"
              />
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template #default="scope">
              <el-tooltip content="归档" placement="top">
                <el-button v-if="scope.row.status !== 'archived'" link type="primary" icon="Delete" @click="handleArchive(scope.row)"
                           v-hasPermi="['witdoc:doc:remove']">归档
                </el-button>
                <el-button v-if="scope.row.status === 'archived'" link type="primary" icon="RefreshLeft" @click="handleReArchive(scope.row)"
                           v-hasPermi="['witdoc:doc:remove']">取消归档
                </el-button>
              </el-tooltip>
            </template>
          </el-table-column>
        </el-table>

        <pagination
          v-show="total>0"
          :total="total"
          v-model:page="queryParams.pageNum"
          v-model:limit="queryParams.pageSize"
          @pagination="getList"
        />
      </el-tab-pane>
      <el-tab-pane label="命中测试" name="second"></el-tab-pane>
      <el-tab-pane label="设置" name="third">
        <el-text tag="b" size="large">数据集设置</el-text>
        <br>
        <el-text>在这里您可以修改数据集的工作方式以及其他设置</el-text>
        <el-form class="mt20" ref="datasetFormRef" :model="datasetInfo" label-width="200px">
          <el-form-item label="数据集名称" prop="datasetName">
            <el-input v-model="datasetInfo.datasetName" placeholder="请输入数据集名称"/>
          </el-form-item>
          <el-form-item label="数据集描述" prop="datasetDesc">
            <el-input v-model="datasetInfo.datasetDesc" placeholder="请输入数据集描述"/>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" :loading="buttonLoading" @click="submitForm">保存</el-button>
          </el-form-item>
        </el-form>

      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup lang="ts">
import {DocForm, DocQuery, DocVO} from "@/api/witdock/datasetDoc/type";

import {ref} from 'vue'
import {Search} from '@element-plus/icons-vue'
import {addDoc, listDoc, listDocByDatasetId, updateDoc} from "@/api/witdock/datasetDoc/api";
import {TabsPaneContext} from "element-plus";
import {DatasetVO} from "@/api/witdock/dataset/types";
import {delDataset, getDataset, updateDataset} from "@/api/witdock/dataset";

const {proxy} = getCurrentInstance() as ComponentInternalInstance;
const { witdock_doc_status } = toRefs<any>(proxy?.useDict("witdock_doc_status"));

const datasetId = useRoute().params.id as string;
const datasetInfo = ref({});
const activeName = ref('first');
const docList = ref<DocVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const total = ref(0);
const ossIds = ref<String>(undefined);

const initFormData: DocForm = {
  id: undefined,
  datasetId: undefined,
  docName: undefined,
  ossId: undefined,
  charNum: undefined,
  status: undefined
}
const data = reactive<PageData<DocForm, DocQuery>>({
  datasetFormRef: {...initFormData},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    datasetId: undefined,
    docName: undefined,
    ossId: undefined,
    charNum: undefined,
    status: undefined,
    params: {}
  },
  rules: {
    id: [
      {required: true, message: "不能为空", trigger: "blur"}
    ],
    datasetId: [
      {required: true, message: "数据集id不能为空", trigger: "blur"}
    ],
    docName: [
      {required: true, message: "文档名称不能为空", trigger: "blur"}
    ],
    ossId: [
      {required: true, message: "对象存储id不能为空", trigger: "blur"}
    ],
    charNum: [
      {required: true, message: "字符数不能为空", trigger: "blur"}
    ],
    status: [
      {required: true, message: "状态不能为空", trigger: "change"}
    ],
  }
});

const {queryParams, datasetFormRef, rules} = toRefs(data);

/** 查询数据集文档列表 */
const getList = async () => {
  loading.value = true;
  queryParams.value.datasetId = datasetId;
  console.log(typeof queryParams.value.datasetId + queryParams.value.datasetId)
  const res = await listDoc(queryParams.value);
  docList.value = res.rows;
  total.value = res.total;
  loading.value = false;
}

const addDocInfo = (ossId: number): void => {
  console.log("call addDocInfo")
  addDoc({ossId: ossId, datasetId: datasetId} as DocForm).then((res) => {
    if (res.code === 200) {
      ElMessage.success("添加文件成功");
      getList();
    }
  })
}


const handleClick = (tab: TabsPaneContext, event: Event) => {
  console.log(tab, event)
}

/** 归档按钮操作 */
const handleArchive = async (row?: DatasetVO) => {
  const id = row?.id;
  await proxy?.$modal.confirm('是否确认将数据集编号为"' + id + '"的数据项归档？').finally(() => loading.value = false);
  await updateDoc({
    id: id,
    status: 'archived'
  })
  proxy?.$modal.msgSuccess("归档成功");
  await getList();
}

/** 取消归档按钮操作 */
const handleReArchive = async (row?: DatasetVO) => {
  const id = row?.id;
  await proxy?.$modal.confirm('是否确认将数据集编号为"' + id + '"的数据项取消归档？').finally(() => loading.value = false);
  await updateDoc({
    id: id,
    status: 'inactive'
  })
  proxy?.$modal.msgSuccess("取消归档成功");
  await getList();
}

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.value.pageNum = 1;
  getList();
}

/** 搜索按钮操作 */
const resetQuery = () => {
  queryParams.value.docName = undefined;
  queryParams.value.pageNum = 1;
  getList();
}


/** 提交按钮 */
const submitForm = () => {
  datasetFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      await updateDataset(datasetInfo.value).finally(() => buttonLoading.value = false);
      proxy?.$modal.msgSuccess("修改成功");
      getDataset(datasetId).then((res) => {
        datasetInfo.value = res.data;
      });
    }
  });
}

const computedStatus = computed((statusStr) => statusStr === 'active')


onMounted(() => {
  getList();
  getDataset(datasetId).then((res) => {
    datasetInfo.value = res.data;
  });
});
</script>
<style>

</style>
