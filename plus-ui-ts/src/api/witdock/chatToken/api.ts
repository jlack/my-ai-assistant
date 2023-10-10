import {AppDatasetQuery, AppDatasetVO} from "@/api/witdock/appDataset/types";
import {AxiosPromise} from "axios";
import request from "@/utils/request";

/**
 * 请求新的token
 */
export const getChatToken = (query?: AppDatasetQuery): AxiosPromise<AppDatasetVO[]> => {
  return request({
    url: '/witdock/appDataset/list',
    method: 'get',
    params: query
  });
};

