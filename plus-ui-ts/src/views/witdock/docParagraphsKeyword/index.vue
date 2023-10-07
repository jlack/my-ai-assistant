<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div class="search" v-show="showSearch">
        <el-form :model="queryParams" ref="queryFormRef" :inline="true" label-width="68px">
          <el-form-item label="段落id" prop="paragraphsId">
            <el-input v-model="queryParams.paragraphsId" placeholder="请输入段落id" clearable style="width: 240px" @keyup.enter="handleQuery" />
          </el-form-item>
          <el-form-item label="关键字" prop="keyword">
            <el-input v-model="queryParams.keyword" placeholder="请输入关键字" clearable style="width: 240px" @keyup.enter="handleQuery" />
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
            <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['witdock:docParagraphsKeyword:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()" v-hasPermi="['witdock:docParagraphsKeyword:edit']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()" v-hasPermi="['witdock:docParagraphsKeyword:remove']">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['witdock:docParagraphsKeyword:export']">导出</el-button>
          </el-col>
          <el-button @click="testAdd">增</el-button>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" :data="docParagraphsKeywordList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="段落id" align="center" prop="paragraphsId" />
        <el-table-column label="关键字" align="center" prop="keyword" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['witdock:docParagraphsKeyword:edit']"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['witdock:docParagraphsKeyword:remove']"></el-button>
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
    <!-- 添加或修改段落关键字表对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
      <el-form ref="docParagraphsKeywordFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="段落id" prop="paragraphsId">
          <el-input v-model="form.paragraphsId" placeholder="请输入段落id" />
        </el-form-item>
        <el-form-item label="关键字" prop="keyword">
          <el-input v-model="form.keyword" placeholder="请输入关键字" />
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

<script setup name="DocParagraphsKeyword" lang="ts">
import { listDocParagraphsKeyword, getDocParagraphsKeyword, delDocParagraphsKeyword, addDocParagraphsKeyword, updateDocParagraphsKeyword } from '@/api/witdock/docParagraphsKeyword/api';
import { DocParagraphsKeywordVO, DocParagraphsKeywordQuery, DocParagraphsKeywordForm } from '@/api/witdock/docParagraphsKeyword/types';
import {func} from "vue-types";

const { proxy } = getCurrentInstance() as ComponentInternalInstance;

const docParagraphsKeywordList = ref<DocParagraphsKeywordVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);

const queryFormRef = ref<ElFormInstance>();
const docParagraphsKeywordFormRef = ref<ElFormInstance>();

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const initFormData: DocParagraphsKeywordForm = {
  paragraphsId: undefined,
  keyword: undefined,
}
const data = reactive<PageData<DocParagraphsKeywordForm, DocParagraphsKeywordQuery>>({
  form: {...initFormData},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    paragraphsId: undefined,
    keyword: undefined,
    params: {
    }
  },
  rules: {
    paragraphsId: [
      { required: true, message: "段落id不能为空", trigger: "blur" }
    ],
    keyword: [
      { required: true, message: "关键字不能为空", trigger: "blur" }
    ],
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询段落关键字表列表 */
const getList = async () => {
  loading.value = true;
  const res = await listDocParagraphsKeyword(queryParams.value);
  docParagraphsKeywordList.value = res.rows;
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
  docParagraphsKeywordFormRef.value?.resetFields();
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
const handleSelectionChange = (selection: DocParagraphsKeywordVO[]) => {
  ids.value = selection.map(item => item.paragraphsId);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = "添加段落关键字表";
}

/** 修改按钮操作 */
const handleUpdate = async (row?: DocParagraphsKeywordVO) => {
  reset();
  const _paragraphsId = row?.paragraphsId || ids.value[0]
  const res = await getDocParagraphsKeyword(_paragraphsId);
  Object.assign(form.value, res.data);
  dialog.visible = true;
  dialog.title = "修改段落关键字表";
}

/** 提交按钮 */
const submitForm = () => {
  docParagraphsKeywordFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.paragraphsId) {
        await updateDocParagraphsKeyword(form.value).finally(() =>  buttonLoading.value = false);
      } else {
        await addDocParagraphsKeyword(form.value).finally(() =>  buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess("修改成功");
      dialog.visible = false;
      await getList();
    }
  });
}

/** 删除按钮操作 */
const handleDelete = async (row?: DocParagraphsKeywordVO) => {
  const _paragraphsIds = row?.paragraphsId || ids.value;
  await proxy?.$modal.confirm('是否确认删除段落关键字表编号为"' + _paragraphsIds + '"的数据项？').finally(() => loading.value = false);
  await delDocParagraphsKeyword(_paragraphsIds);
  proxy?.$modal.msgSuccess("删除成功");
  await getList();
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download('witdock/docParagraphsKeyword/export', {
    ...queryParams.value
  }, `docParagraphsKeyword_${new Date().getTime()}.xlsx`)
}

function testAdd() {
  addDocParagraphsKeyword({paragraphsId: 1234, keyword: '1234Keyword'})
}

onMounted(() => {
  getList();
});
</script>
