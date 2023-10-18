<template>
  <div class="app-container">
    <el-row :gutter="50">
      <el-col :span="18">
        <el-row justify="space-between">
          <el-col :span="12">
            <el-button size="large" type="primary" plain @click="handleBack">
              <el-icon>
                <Back/>
              </el-icon>
            </el-button>
            <span style="font-size: 180%" class="ml20">{{ currDoc.docName }}</span>
          </el-col>
          <el-col :span="12">
            <el-switch
              size="large"
              :disabled="currDoc.status === DocStatus.ARCHIVED"
              v-bind:value="docActive(currDoc)"
              class="ml20"
              inline-prompt
              style="float: right;--el-switch-on-color: #5d90ef; --el-switch-off-color: #dddfea"
              active-text="已激活"
              inactive-text="禁用中"
              @change="handleDocActive"
            />
            <el-button style="float: right" size="large" type="warning" plain
                       @click="handleArchive(DocStatus.ARCHIVED)" v-if="currDoc.status !== DocStatus.ARCHIVED">
              <el-icon>
                <Delete />
              </el-icon>
              归 档
            </el-button>
            <el-button style="float: right;" size="large" type="info" plain @click="handleArchive(DocStatus.INACTIVE)" v-else>
              <el-icon>
                <RefreshLeft />
              </el-icon>
              取消归档
            </el-button>
            <el-button type="primary" plain size="large" style="float: right" class="mr20"
                       @click="() => {dialog.visible = true; dialog.title = '新增段落';}">
              <el-icon>
                <Plus />
              </el-icon>
              添加分段
            </el-button>
          </el-col>
        </el-row>
        <el-divider class="mr5"/>
        <div class="row-container">
          <span style="font-size: 120%">段落数: {{ total }}</span>
          <el-form :model="queryParams" ref="queryFormRef" :inline="true" label-width="68px">
            <el-form-item label="状态" prop="status">
              <el-select placeholder="段落状态" v-model="queryParams.status" clearable style="width: 100px">
                <el-option v-for="dict in witdock_para_status" :key="dict.value" :label="dict.label"
                           :value="dict.value"/>
              </el-select>
            </el-form-item>
            <el-form-item label="内容" prop="content">
              <el-input v-model="queryParams.content" placeholder="请输入段落内容" clearable style="width: 200px"
                        @keyup.enter="handleQuery"/>
            </el-form-item>
            <el-form-item>
              <el-button :loading="queryLoading" type="primary" icon="Search" @click="handleQuery">搜索</el-button>
              <el-button :loadinng="resetLoading" icon="Refresh" @click="resetQuery">重置</el-button>
            </el-form-item>
          </el-form>
        </div>

        <!-- 段落信息cards -->
        <el-row :gutter="10">
          <el-col :md="8" class="mt5" v-for="para in paraList" :key="para.id">
            <el-card class="box-card link-card" @click="handleUpdate(para)" shadow="hover"
                     @mouseover="setCardHover(para.id, true)" @mouseleave="setCardHover(para.id, false)">
              <template #header>
                <div class="card-header">
                  <el-tag size="large">{{ '#' + para.sno }}</el-tag>

                  <span v-show="isCardHover(para.id)" class="ml20">{{ '字符数:' + para.charNum }}</span>
                  <el-button type="warning" v-show="isCardHover(para.id)" class="ml20"
                             @click.stop="handleDelete(para)" icon="Delete" plain/>
                  <span @click.stop="">
                    <el-switch
                      v-bind:value="paraActive(para)"
                      class="ml-2"
                      inline-prompt
                      style="float: right;--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949"
                      active-text="已激活"
                      inactive-text="禁用中"
                      @change="handleParaActive(para)"
                    />
                  </span>
                </div>
              </template>
              <el-text class="mt20 mx-1" truncated>{{ para.content }}</el-text>
            </el-card>
          </el-col>
        </el-row>

        <pagination
          v-show="total>0"
          :total="total"
          v-model:page="queryParams.pageNum"
          v-model:limit="queryParams.pageSize"
          @pagination="getParaList"
        />
      </el-col>

      <el-col :span="6">
        <el-descriptions title="文件信息" :column="1" :border="true">
          <el-descriptions-item label="文件名:">{{ currDoc.docName }}</el-descriptions-item>
          <el-descriptions-item label="文件大小:">
            <template v-if="currDoc.fileSize > 1024">
              {{ (currDoc.fileSize / 1024).toFixed(1) + ' KB' }}
            </template>
            <template v-else>
              {{ currDoc.fileSize + ' Byte' }}
            </template>
          </el-descriptions-item>
          <el-descriptions-item label="字符数:">{{ currDoc.charNum }}</el-descriptions-item>
          <el-descriptions-item label="上传时间:">{{ currDoc.createTime }}</el-descriptions-item>
          <el-descriptions-item label="上传用户:">{{ currDoc.createByName }}</el-descriptions-item>
          <el-descriptions-item label="修改时间:">{{ currDoc.updateTime }}</el-descriptions-item>
          <el-descriptions-item label="修改用户:">{{ currDoc.updateByName }}</el-descriptions-item>
          <el-descriptions-item label="状态:">
            <dict-tag :options="witdock_doc_status" :value="currDoc.status" />
          </el-descriptions-item>
        </el-descriptions>
      </el-col>
    </el-row>

    <!-- 添加或修改文档段落表对话框 -->
    <el-dialog :title="dialog.title" v-model="dialog.visible" width="660px" append-to-body :close-on-click-modal="false">
      <el-form ref="docParagraphsFormRef" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="段落内容">
          <el-input type="textarea" :rows="6" v-model="form.content" :min-height="192"/>
        </el-form-item>
        <el-form-item label="状态" v-if="dialog.title === '段落详情'">
          <el-radio-group v-model="form.status">
            <el-radio v-for="dict in witdock_para_status" :key="dict.value" :label="dict.value">
              {{ dict.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <el-tag v-if="dialog.title === '段落详情'" class="mr5 mt5" v-for="keyword in keywordList" :key="keyword.id">{{keyword.keyword}}</el-tag>
      <template #footer>
        <div class="dialog-footer">
          <el-button :loading="addLoading" type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import {
  addDocParagraphs,
  delDocParagraphs, getDocParagraphs,
  listDocParagraphs,
  updateDocParagraphs
} from "@/api/witdock/docParagraphs/api";
import {DocParagraphsForm, DocParagraphsQuery, DocParagraphsVO, ParaStatus} from "@/api/witdock/docParagraphs/types";
import {DocStatus, DocVO} from "@/api/witdock/datasetDoc/type";
import {getDoc, updateDoc} from "@/api/witdock/datasetDoc/api";
import {DocParagraphsKeywordVO} from "@/api/witdock/docParagraphsKeyword/types";
import {listDocParagraphsKeyword} from "@/api/witdock/docParagraphsKeyword/api";
import {showResultMsg} from "@/utils/message";


const router = useRouter();
const isArchived = ref(false);
const dialog = reactive<DialogOption>({
  visible: false,
  title: ''
});

const total = ref(0);
const {proxy} = getCurrentInstance() as ComponentInternalInstance;
const {witdock_para_status} = toRefs<any>(proxy?.useDict("witdock_para_status"));
const {witdock_doc_status} = toRefs<any>(proxy?.useDict("witdock_doc_status"));
const queryFormRef = ref<ElFormInstance>();
const currDocId = String(useRoute().params.id);
const cardHover = ref<{ [key: string]: boolean }>({});
const currDoc = ref<DocVO>({
  id: '',
  datasetId: '',
  docName: '',
  ossId: '',
  charNum: 0,
  status: '',
  fileSize: 0,
  createTime: '',
  createByName: '',
  updateTime: '',
  updateByName: ''
});

const initFormData: DocParagraphsForm = {
  id: undefined,
  docId: undefined,
  sno: undefined,
  status: undefined,
  content: undefined,
  charNum: undefined,
}
const paraList = ref<DocParagraphsVO[]>([]);
const queryLoading = ref(false);
const resetLoading = ref(false);
const queryParams = ref<DocParagraphsQuery>({
  pageNum: 1,
  pageSize: 12,
  docId: undefined,
  sno: undefined,
  status: undefined,
  content: undefined,
  charNum: undefined,
  params: {}
})
const keywordList = ref<DocParagraphsKeywordVO[]>([]);
const form = ref({...initFormData});
const addLoading = ref(false);
const rules = ref({
    content: [
    { required: true, message: "段落内容不能为空", trigger: "blur" }
  ]
})

const docParagraphsFormRef = ref<ElFormInstance>();

/** 提交按钮 */
const submitForm = () => {
  docParagraphsFormRef.value?.validate(async (valid: boolean) => {
    if (valid) {
      addLoading.value = true;
      let res;
      if (form.value.id) {
        let charNum = form.value.content?.length;
        res = await updateDocParagraphs({...form.value, charNum: charNum}).finally(() =>  addLoading.value = false);
        showResultMsg(res, '修改');
      } else {
        res = await addDocParagraphs({...form.value, docId: currDocId}).finally(() =>  addLoading.value = false);
        showResultMsg(res, '新增');
      }

      cancel();
      await init();
    }
  });
}

function cancel() {
  form.value = {...initFormData};
  docParagraphsFormRef.value?.resetFields();
  dialog.visible = false;
}

function handleBack() {
  router.push("/dataset/detail/" + currDoc.value.datasetId);
}

/** 查询文档段落表列表 */
const getParaList = async () => {
  const res = await listDocParagraphs({...queryParams.value, docId: currDocId});
  paraList.value = res.rows;
  total.value = res.total;
}

const paraActive = computed(() => (para: { status: ParaStatus; }) => {
  return para.status === ParaStatus.ACTIVE;
})

const docActive = computed(() => (para: { status: DocStatus; }) => {
  return para.status === DocStatus.ACTIVE;
})

async function handleUpdate(para: DocParagraphsVO) {
  dialog.visible = true;
  dialog.title = '段落详情';
  form.value = para;
  let keywordRes = await listDocParagraphsKeyword({paragraphsId: para.id, pageNum: 1, pageSize: 1000});
  keywordList.value = keywordRes.rows;
}

async function handleDelete(para: DocParagraphsVO) {
  let res = await delDocParagraphs(para.id);
  showResultMsg(res, '删除');
  await init();
}

async function handleParaActive(para: DocParagraphsVO) {
  let status = para.status === ParaStatus.ACTIVE ? ParaStatus.INACTIVE : ParaStatus.ACTIVE;
  let res = await updateDocParagraphs({id: para.id, status: status});
  showResultMsg(res);
  await init();
  // form.value.status = status;
}

async function handleDocActive() {
  let status = currDoc.value.status === DocStatus.ACTIVE ? DocStatus.INACTIVE : DocStatus.ACTIVE;
  let res = await updateDoc({id: currDocId, status: status})
  if (res.code === 200)
    currDoc.value.status = status;
  showResultMsg(res);
}

/** 归档按钮操作 */
async function handleArchive(status: DocStatus){
  if (status === DocStatus.ARCHIVED) {
    await proxy?.$modal.confirm('是否确认将名为"' + currDoc.value.docName + '"的文档归档？');
    let res = await updateDoc({id: currDocId, status: status})
    showResultMsg(res, '归档');
  } else {
    await proxy?.$modal.confirm('是否确认将名为"' + currDoc.value.docName + '"的文档取消归档？');
    let res = await updateDoc({id: currDocId, status: status})
    showResultMsg(res, '取消归档');
  }

  await init();
}

function setCardHover(cardId: string | number, value: boolean) {
  cardHover.value[cardId] = value;
}

function isCardHover(cardId: string | number) {
  return cardHover.value[cardId] || false;
}

async function handleQuery() {
  queryParams.value.pageNum = 1;
  queryLoading.value = true;
  await getParaList();
  queryLoading.value = false;
}

/** 重置按钮操作 */
const resetQuery = () => {
  queryFormRef.value?.resetFields();
  handleQuery();
}

async function init() {
  await getParaList();
  let res = await getDoc(currDocId);
  currDoc.value = res.data;
}

init();
</script>

<style scoped>
.row-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

</style>
