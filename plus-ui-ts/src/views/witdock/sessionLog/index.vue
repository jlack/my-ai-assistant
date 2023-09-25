<template>
  <div class="p-2">
    <transition :enter-active-class="proxy?.animate.searchAnimate.enter" :leave-active-class="proxy?.animate.searchAnimate.leave">
      <div class="search" v-show="showSearch">
        <el-form :model="queryParams" ref="queryFormRef" :inline="true" label-width="68px">
          <el-form-item label="对话id" prop="sessionId">
            <el-input v-model="queryParams.sessionId" placeholder="请输入对话id" clearable style="width: 240px" @keyup.enter="handleQuery" />
          </el-form-item>
          <el-form-item label="提问内容" prop="query">
            <el-input v-model="queryParams.query" placeholder="请输入提问内容" clearable style="width: 240px" @keyup.enter="handleQuery" />
          </el-form-item>
          <el-form-item label="回答内容" prop="answer">
            <el-input v-model="queryParams.answer" placeholder="请输入回答内容" clearable style="width: 240px" @keyup.enter="handleQuery" />
          </el-form-item>
          <el-form-item label="回答时间" prop="reDatetime">
            <el-date-picker clearable
                            v-model="queryParams.reDatetime"
                            type="date"
                            value-format="YYYY-MM-DD"
                            placeholder="请选择回答时间"
            />
          </el-form-item>
          <el-form-item label="花费token" prop="msgToken">
            <el-input v-model="queryParams.msgToken" placeholder="请输入花费token" clearable style="width: 240px" @keyup.enter="handleQuery" />
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
            <el-button type="primary" plain icon="Plus" @click="handleAdd" v-hasPermi="['system:log:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="Edit" :disabled="single" @click="handleUpdate()" v-hasPermi="['system:log:edit']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="Delete" :disabled="multiple" @click="handleDelete()" v-hasPermi="['system:log:remove']">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="Download" @click="handleExport" v-hasPermi="['system:log:export']">导出</el-button>
          </el-col>
          <right-toolbar v-model:showSearch="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>
      </template>

      <el-table v-loading="loading" :data="logList" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="" align="center" prop="id" v-if="true" />
        <el-table-column label="对话id" align="center" prop="sessionId" />
        <el-table-column label="提问内容" align="center" prop="query" />
        <el-table-column label="回答内容" align="center" prop="answer" />
        <el-table-column label="回答时间" align="center" prop="reDatetime" width="180">
          <template #default="scope">
            <span>{{ parseTime(scope.row.reDatetime, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="花费token" align="center" prop="msgToken" />
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template #default="scope">
            <el-tooltip content="修改" placement="top">
              <el-button link type="primary" icon="Edit" @click="handleUpdate(scope.row)" v-hasPermi="['system:log:edit']"></el-button>
            </el-tooltip>
            <el-tooltip content="删除" placement="top">
              <el-button link type="primary" icon="Delete" @click="handleDelete(scope.row)" v-hasPermi="['system:log:remove']"></el-button>
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
    <!-- 添加或修改会话日志表对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="500px" append-to-body>
      <el-form ref="logFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="对话id" prop="sessionId">
          <el-input v-model="form.sessionId" placeholder="请输入对话id" />
        </el-form-item>
        <el-form-item label="提问内容" prop="query">
          <el-input v-model="form.query" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="回答内容" prop="answer">
          <el-input v-model="form.answer" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="回答时间" prop="reDatetime">
          <el-date-picker clearable
                          v-model="form.reDatetime"
                          type="datetime"
                          value-format="YYYY-MM-DD HH:mm:ss"
                          placeholder="请选择回答时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="花费token" prop="msgToken">
          <el-input v-model="form.msgToken" placeholder="请输入花费token" />
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

<script setup name="Log" lang="ts">
import { listLog, getLog, delLog, addLog, updateLog } from '@/api/witdock/sessionLog/api';

import { LogVO, LogQuery, LogForm } from '@/api/witdock/sessionLog/type';

const { proxy } = getCurrentInstance() as ComponentInternalInstance;

const logList = ref<LogVO[]>([]);
const buttonLoading = ref(false);
const loading = ref(true);
const showSearch = ref(true);
const ids = ref<Array<string | number>>([]);
const single = ref(true);
const multiple = ref(true);
const total = ref(0);

const queryFormRef = ref<ElFormInstance>();
const logFormRef = ref<ElFormInstance>();

const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const initFormData: LogForm = {
  id: undefined,
  sessionId: undefined,
  query: undefined,
  answer: undefined,
  reDatetime: undefined,
  msgToken: undefined,
}
const data = reactive<PageData<LogForm, LogQuery>>({
  form: {...initFormData},
  queryParams: {
    pageNum: 1,
    pageSize: 10,
    sessionId: undefined,
    query: undefined,
    answer: undefined,
    reDatetime: undefined,
    msgToken: undefined,
    params: {
    }
  },
  rules: {
    id: [
      { required: true, message: "不能为空", trigger: "blur" }
    ],
    sessionId: [
      { required: true, message: "对话id不能为空", trigger: "blur" }
    ],
    query: [
      { required: true, message: "提问内容不能为空", trigger: "blur" }
    ],
    answer: [
      { required: true, message: "回答内容不能为空", trigger: "blur" }
    ],
    reDatetime: [
      { required: true, message: "回答时间不能为空", trigger: "blur" }
    ],
    msgToken: [
      { required: true, message: "花费token不能为空", trigger: "blur" }
    ],
  }
});

const { queryParams, form, rules } = toRefs(data);

/** 查询会话日志表列表 */
const getList = async () => {
  loading.value = true;
  const res = await listLog(queryParams.value);
  logList.value = res.rows;
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
  logFormRef.value?.resetFields();
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
const handleSelectionChange = (selection: LogVO[]) => {
  ids.value = selection.map(item => item.id);
  single.value = selection.length != 1;
  multiple.value = !selection.length;
}

/** 新增按钮操作 */
const handleAdd = () => {
  reset();
  dialog.visible = true;
  dialog.title = "添加会话日志表";
}

/** 修改按钮操作 */
const handleUpdate = async (row?: LogVO) => {
  reset();
  const _id = row?.id || ids.value[0]
  const res = await getLog(_id);
  Object.assign(form.value, res.data);
  dialog.visible = true;
  dialog.title = "修改会话日志表";
}

/** 提交按钮 */
const submitForm = () => {
  logFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      buttonLoading.value = true;
      if (form.value.id) {
        await updateLog(form.value).finally(() =>  buttonLoading.value = false);
      } else {
        await addLog(form.value).finally(() =>  buttonLoading.value = false);
      }
      proxy?.$modal.msgSuccess("修改成功");
      dialog.visible = false;
      await getList();
    }
  });
}

/** 删除按钮操作 */
const handleDelete = async (row?: LogVO) => {
  const _ids = row?.id || ids.value;
  await proxy?.$modal.confirm('是否确认删除会话日志表编号为"' + _ids + '"的数据项？').finally(() => loading.value = false);
  await delLog(_ids);
  proxy?.$modal.msgSuccess("删除成功");
  await getList();
}

/** 导出按钮操作 */
const handleExport = () => {
  proxy?.download('system/log/export', {
    ...queryParams.value
  }, `log_${new Date().getTime()}.xlsx`)
}

onMounted(() => {
  getList();
});
</script>
