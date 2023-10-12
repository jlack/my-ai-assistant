import {addSplitedParasBo} from "@/api/witdock/docParagraphs/types";
import request from "@/utils/request";

export const getOssListInfo = (ossIds: string | number) => {
  return request({
    url: '/witdock/docParagraphs/getOssListInfo/' + ossIds
  });
}
