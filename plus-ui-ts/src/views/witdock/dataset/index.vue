<template>
  <div class="app-container">
    <el-row :gutter="10">
      <el-col :md="6" class="mt5">
        <el-card class="box-card link-card" style="height: 100%" @click="handleAdd" shadow="hover">
          <div class="card-header">
            <el-text tag="b">创建数据集</el-text>
            <el-button style="float: right; " @click.stop="handleAdd"
                       icon="Plus" plain></el-button>
          </div>
          <el-text class="mt20 mx-1" truncated>导入自己的文本数据或通过xxxxxxx</el-text>
        </el-card>
      </el-col>
      <el-col :md="6" class="mt5" v-for="item in datasetList">
        <el-card class="box-card link-card" @click="handleUpdate(item)" shadow="hover">
          <template #header>
            <div class="card-header">
              <el-image style="width: 20px; height: 20px" :src="cardLogo" fit="fill"/>
              <el-text tag="b">{{ item.datasetName }}</el-text>
              <el-button style="float: right; " @click.stop="handleDelete(item)"
                         icon="Delete" plain></el-button>
            </div>
          </template>
          <el-text class="mt20 mx-1" truncated>{{ item.datasetDesc }}</el-text>
          <div class="mt10">
            <el-text class="mt20 mx-1">{{item.docNum}} 文档</el-text>
            <el-divider direction="vertical" />
            <el-text>{{ item.charNum }} 字符</el-text>
            <el-divider direction="vertical" border-style="dashed" />
            <el-text>1 关联应用</el-text>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <pagination
      v-show="total>0"
      :total="total"
      v-model:page="queryParams.pageNum"
      v-model:limit="queryParams.pageSize"
      @pagination="getList"
    />
  </div>
</template>

<script setup name="Dataset" lang="ts">
import {listDataset, getDataset, delDataset, addDataset, updateDataset} from '@/api/witdock/dataset';
import {DatasetVO, DatasetQuery, DatasetForm} from '@/api/witdock/dataset/types';
import {useRouter} from "vue-router";

const {proxy} = getCurrentInstance() as ComponentInternalInstance;

const router = useRouter();
const datasetList = ref<DatasetVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);

const cardLogo = "/src/assets/logo/logo.png";

const queryFormRef = ref<ElFormInstance>();
const datasetFormRef = ref<ElFormInstance>();

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const initFormData: DatasetForm = {
  id: undefined,
  datasetName: undefined,
  datasetDesc: undefined,
  visiblePermission: undefined,
  isDeleted: undefined
}
const data = reactive<PageData<DatasetForm, DatasetQuery>>({
  form: {...initFormData},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    datasetName: undefined,
    datasetDesc: undefined,
    visiblePermission: undefined,
    isDeleted: undefined,
    params: {}
  },
  rules: {
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
      {required: true, message: "me,all不能为空", trigger: "blur"}
    ],
    isDeleted: [
      {required: true, message: "是否删除不能为空", trigger: "blur"}
    ]
  }
});

const {queryParams, form, rules} = toRefs(data);

/** 查询数据集列表 */
const getList = async () => {
  loading.value = true;
  const res = await listDataset(queryParams.value);
  datasetList.value = res.rows;
  total.value = res.total;
  loading.value = false;
}

/** 取消按钮 */
const cancel = () => {
  reset();
  dialog.visible = false;
}

/** 表单重置 */
const reset = () => {
  form.value = {...initFormData};
  datasetFormRef.value?.resetFields();
}

/** 搜索按钮操作 */
const handleQuery = () => {
  queryParams.value.pageNum = 1;
  getList();
}

/** 重置按钮操作 */
const resetQuery = () => {
  queryFormRef.value?.resetFields();
  handleQuery();
}

/** 多选框选中数据 */
const handleSelectionChange = (selection: DatasetVO[]) => {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
const handleAdd = () => {
  // reset();
  // dialog.visible = true;
  // dialog.title = "添加数据集";
  router.push("/dataset/add");
}

/** 修改按钮操作 */
const handleUpdate = async (row?: DatasetVO) => {
  // reset();
  // const _id = row?.id || ids.value[0]
  const _id = row?.id
  router.push("dataset/detail/" + _id)
  // const res = await getDataset(_id);
  // Object.assign(form.value, res.data);
  // dialog.visible = true;
  // dialog.title = "修改数据集";
}

/** 提交按钮 */
const submitForm = () => {
  datasetFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateDataset(form.value).finally(() => buttonLoading.value = false);
      } else {
        await addDataset(form.value).finally(() => buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess("修改成功");
      dialog.visible = false;
      await getList();
    }
  });
}

/** 删除按钮操作 */
const handleDelete = async (row?: DatasetVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除数据集编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
  await delDataset(_ids);
  proxy?.$modal.msgSuccess("删除成功");
  await getList();
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download('witdock/dataset/export', {
    ...queryParams.value
  }, `dataset_${new Date().getTime()}.xlsx`)
}

onMounted(() => {
  getList();
});
</script>

<style>
.link-card {
  cursor: pointer; /* 将鼠标样式设置为手指形状 */
}

.card-header {
  display: flex;
  justify-content: space-between;
  //align-items: center;
}
</style>
