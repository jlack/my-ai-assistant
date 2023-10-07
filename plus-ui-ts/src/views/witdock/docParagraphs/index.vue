<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div class="search" v-show="showSearch">
        <el-form :model="queryParams" ref="queryFormRef" :inline="true" label-width="68px">
          <el-form-item label="文档id" prop="docId">
            <el-input v-model="queryParams.docId" placeholder="请输入文档id" clearable style="width: 240px" @keyup.enter="handleQuery" />
          </el-form-item>
          <el-form-item label="段落编号" prop="sno">
            <el-input v-model="queryParams.sno" placeholder="请输入段落编号" clearable style="width: 240px" @keyup.enter="handleQuery" />
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-input v-model="queryParams.status" placeholder="请输入状态" clearable style="width: 240px" @keyup.enter="handleQuery" />
          </el-form-item>
          <el-form-item label="字数" prop="charNum">
            <el-input v-model="queryParams.charNum" placeholder="请输入字数" clearable style="width: 240px" @keyup.enter="handleQuery" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="Search" @click="handleQuery">搜索</el-button>
            <el-button icon="Refresh" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>
      </div>
    </transition>

    <el-card shadow="never">
      <template #header>
        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['witdock:docParagraphs:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()" v-hasPermi="['witdock:docParagraphs:edit']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()" v-hasPermi="['witdock:docParagraphs:remove']">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['witdock:docParagraphs:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" :data="docParagraphsList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="id" align="center" prop="id" v-if="true" />
        <el-table-column label="文档id" align="center" prop="docId" />
        <el-table-column label="段落编号" align="center" prop="sno" />
        <el-table-column label="状态" align="center" prop="status" />
        <el-table-column label="段落内容" align="center" prop="content" />
        <el-table-column label="字数" align="center" prop="charNum" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['witdock:docParagraphs:edit']"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['witdock:docParagraphs:remove']"></el-button>
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
    </el-card>
    <!-- 添加或修改文档段落表对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
      <el-form ref="docParagraphsFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="文档id" prop="docId">
          <el-input v-model="form.docId" placeholder="请输入文档id" />
        </el-form-item>
        <el-form-item label="段落编号" prop="sno">
          <el-input v-model="form.sno" placeholder="请输入段落编号" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-input v-model="form.status" placeholder="请输入状态" />
        </el-form-item>
        <el-form-item label="段落内容">
          <editor v-model="form.content" :min-height="192"/>
        </el-form-item>
        <el-form-item label="字数" prop="charNum">
          <el-input v-model="form.charNum" placeholder="请输入字数" />
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

<script setup name="DocParagraphs" lang="ts">
import { listDocParagraphs, getDocParagraphs, delDocParagraphs, addDocParagraphs, updateDocParagraphs } from '@/api/witdock/docParagraphs/api';
import { DocParagraphsVO, DocParagraphsQuery, DocParagraphsForm } from '@/api/witdock/docParagraphs/types';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;

const docParagraphsList = ref<DocParagraphsVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);

const queryFormRef = ref<ElFormInstance>();
const docParagraphsFormRef = ref<ElFormInstance>();

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const initFormData: DocParagraphsForm = {
  id: undefined,
  docId: undefined,
  sno: undefined,
  status: undefined,
  content: undefined,
  charNum: undefined,
}
const data = reactive<PageData<DocParagraphsForm, DocParagraphsQuery>>({
  form: {...initFormData},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    docId: undefined,
    sno: undefined,
    status: undefined,
    content: undefined,
    charNum: undefined,
    params: {
    }
  },
  rules: {
    id: [
      { required: true, message: "id不能为空", trigger: "blur" }
    ],
    docId: [
      { required: true, message: "文档id不能为空", trigger: "blur" }
    ],
    sno: [
      { required: true, message: "段落编号不能为空", trigger: "blur" }
    ],
    status: [
      { required: true, message: "状态不能为空", trigger: "blur" }
    ],
    content: [
      { required: true, message: "段落内容不能为空", trigger: "blur" }
    ],
    charNum: [
      { required: true, message: "字数不能为空", trigger: "blur" }
    ],
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询文档段落表列表 */
const getList = async () => {
  loading.value = true;
  const res = await listDocParagraphs(queryParams.value);
  docParagraphsList.value = res.rows;
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
  docParagraphsFormRef.value?.resetFields();
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
const handleSelectionChange = (selection: DocParagraphsVO[]) => {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = "添加文档段落表";
}

/** 修改按钮操作 */
const handleUpdate = async (row?: DocParagraphsVO) => {
  reset();
  const _id = row?.id || ids.value[0]
  const res = await getDocParagraphs(_id);
  Object.assign(form.value, res.data);
  dialog.visible = true;
  dialog.title = "修改文档段落表";
}

/** 提交按钮 */
const submitForm = () => {
  docParagraphsFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateDocParagraphs(form.value).finally(() =>  buttonLoading.value = false);
      } else {
        await addDocParagraphs(form.value).finally(() =>  buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess("修改成功");
      dialog.visible = false;
      await getList();
    }
  });
}

/** 删除按钮操作 */
const handleDelete = async (row?: DocParagraphsVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除文档段落表编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
  await delDocParagraphs(_ids);
  proxy?.$modal.msgSuccess("删除成功");
  await getList();
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download('witdock/docParagraphs/export', {
    ...queryParams.value
  }, `docParagraphs_${new Date().getTime()}.xlsx`)
}

onMounted(() => {
  getList();
});
</script>
