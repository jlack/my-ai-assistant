import { ElMessage } from 'element-plus';

export function showResulFulltMsg(res: any, successInfo: string, failureInfo: string) {
  if (res.code === 200) {
    ElMessage.success(successInfo);
  } else {
    ElMessage.warning(failureInfo);
  }
}

export function showResultMsg(res: any, title: string = '操作') {
  if (res.code === 200) {
    ElMessage.success(title + '成功');
  } else {
    ElMessage.warning(title + '失败');
  }
}
