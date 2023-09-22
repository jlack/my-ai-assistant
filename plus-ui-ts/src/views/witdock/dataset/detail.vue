<template>
  <div class="app-container">
    <div>编辑id {{ datasetId }}</div>
    <el-tabs v-model="activeName" class="demo-tabs" @tab-click="handleClick">
      <el-tab-pane label="文档" name="first">
        <el-row :gutter="10">
          {{queryParams}}
          <el-col :span="20">
            <el-input
              style="width: 220px"
              v-model="queryParams.docName"
              class="w-50 m-2"
              placeholder="搜索文件名"
              :prefix-icon="Search"
            />
            <el-button type="primary" @click="handleQuery">搜索</el-button>
          </el-col>

          <el-col :span="4">
            <file-upload :show-file-list="false">添加文件</file-upload>
          </el-col>
        </el-row>


        <el-table v-loading="loading" :data="docList">
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column label="id" align="center" prop="id" v-if="true" />
          <el-table-column label="数据集id" align="center" prop="datasetId" />
          <el-table-column label="文档名称" align="center" prop="docName" />
<!--          <el-table-column label="对象存储id" align="center" prop="ossId" />-->
          <el-table-column label="字符数" align="center" prop="charNum" />
          <el-table-column label="状态" align="center" prop="status" />
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template #default="scope">
              <el-tooltip content="删除" placement="top">
                <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['witdoc:doc:remove']"></el-button>
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
import {DocForm, DocQuery, DocVO} from "@/api/witdock/datasetDoc/type";

const datasetId = useRoute().params.id as string;
import {ref} from 'vue'
import {Search} from '@element-plus/icons-vue'
import {listDoc, listDocByDatasetId} from "@/api/witdock/datasetDoc/api";
import {TabsPaneContext} from "element-plus";

const datasetInfo = ref({});
const activeName = ref('first');
const docList = ref<DocVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const total = ref(0);

const initFormData: DocForm = {
  id: undefined,
  datasetId: undefined,
  docName: undefined,
  ossId: undefined,
  charNum: undefined,
  status: undefined
}
const data = reactive<PageData<DocForm, DocQuery>>({
  form: {...initFormData},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    datasetId: undefined,
    docName: undefined,
    ossId: undefined,
    charNum: undefined,
    status: undefined,
    params: {
    }
  },
  rules: {
    id: [
      { required: true, message: "不能为空", trigger: "blur" }
    ],
    datasetId: [
      { required: true, message: "数据集id不能为空", trigger: "blur" }
    ],
    docName: [
      { required: true, message: "文档名称不能为空", trigger: "blur" }
    ],
    ossId: [
      { required: true, message: "对象存储id不能为空", trigger: "blur" }
    ],
    charNum: [
      { required: true, message: "字符数不能为空", trigger: "blur" }
    ],
    status: [
      { required: true, message: "状态不能为空", trigger: "change" }
    ],
  }
});

const { queryParams, form, rules } = toRefs(data);

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

const handleClick = (tab: TabsPaneContext, event: Event) => {
  console.log(tab, event)
}

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.value.pageNum = 1;
  getList();
}

onMounted(() => {
  getList();
});
</script>
<style>

</style>
