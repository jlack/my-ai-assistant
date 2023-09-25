<template>
  <div class="app-container">

    <el-row :gutter="10">
      <el-col :md="6" class="mt5">
        <el-card class="box-card link-card" style="height: 100%" @click="handleAdd" shadow="hover">
          <div class="card-header">
            <el-text tag="b">创建应用</el-text>
            <el-button style="float: right; " @click.stop="handleAdd"
                       icon="Plus" plain></el-button>
          </div>
        </el-card>
      </el-col>
      <el-col :md="6" class="mt5" v-for="item in appList">
        <el-card class="box-card link-card" @click="handleDetail(item)" shadow="hover">
          <template #header>
            <div class="card-header">
              <el-image style="width: 20px; height: 20px" :src="cardLogo" fit="fill"/>
              <el-text tag="b">{{ item.appName }}</el-text>
              <el-dropdown style="float: right; ">
                    <span class="el-dropdown-link">
                      <el-icon class="el-icon--right">
                        <More/>
                      </el-icon>
                    </span>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item @click.stop="handleUpdate(item)" icon="Setting">设置</el-dropdown-item>
                    <el-dropdown-item @click.stop="handleDelete(item)" icon="Delete">删除</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </div>
          </template>
          <el-text class="mt20 mx-1" truncated>{{ item.appDesc }}</el-text>
          <div class="mt10">
            <el-text>对话型</el-text>
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
    <!-- 添加或修改构建应用对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
      <el-form ref="infoFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="应用名" prop="appName">
          <el-input v-model="form.appName" placeholder="请输入应用名"/>
        </el-form-item>
        <el-form-item label="应用描述" prop="appDesc">
          <el-input v-model="form.appDesc" placeholder="请输入应用描述"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button :loading="buttonLoading" type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup name="App" lang="ts">
import {listApp, getApp, delApp, addApp, updateApp} from '@/api/witdock/app/index';
import {AppVO, AppQuery, AppForm} from '@/api/witdock/app/type';
import {More} from "@element-plus/icons-vue";

const {proxy} = getCurrentInstance() as ComponentInternalInstance;
const router = useRouter();
const appList = ref<AppVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);
const cardLogo = "/src/assets/logo/logo.png";
const queryFormRef = ref<ElFormInstance>();
const infoFormRef = ref<ElFormInstance>();

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const initFormData: AppForm = {
  id: undefined,
  appName: undefined,
  appDesc: undefined,
  isDeleted: undefined
}
const data = reactive<PageData<AppForm, AppQuery>>({
  form: {...initFormData},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    appName: undefined,
    appDesc: undefined,
    isDeleted: undefined,
    params: {}
  },
  rules: {
    id: [
      {required: true, message: "不能为空", trigger: "blur"}
    ],
    appName: [
      {required: true, message: "应用名不能为空", trigger: "blur"}
    ],
    appDesc: [
      {required: true, message: "应用描述不能为空", trigger: "blur"}
    ],
  }
});

const {queryParams, form, rules} = toRefs(data);

/** 查询构建应用列表 */
const getList = async () => {
  loading.value = true;
  const res = await listApp(queryParams.value);
  appList.value = res.rows;
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
  infoFormRef.value?.resetFields();
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
const handleSelectionChange = (selection: AppVO[]) => {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = "开始创建一个新应用";
}

const handleDetail = (row?: AppVO) => {
  router.push("/app/detail/" + row?.id)
}
/** 修改按钮操作 */
const handleUpdate = async (row?: AppVO) => {
  reset();
  const _id = row?.id || ids.value[0]
  const res = await getApp(_id);
  Object.assign(form.value, res.data);
  dialog.visible = true;
  dialog.title = "WebApp 设置";
}

/** 提交按钮 */
const submitForm = () => {
  infoFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateApp(form.value).finally(() => buttonLoading.value = false);
      } else {
        await addApp(form.value).finally(() => buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess("修改成功");
      dialog.visible = false;
      await getList();
    }
  });
}

/** 删除按钮操作 */
const handleDelete = async (row?: AppVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除构建应用编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
  await delApp(_ids);
  proxy?.$modal.msgSuccess("删除成功");
  await getList();
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download('system/info/export', {
    ...queryParams.value
  }, `info_${new Date().getTime()}.xlsx`)
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
