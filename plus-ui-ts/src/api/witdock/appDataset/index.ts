import request from '@/utils/request';
import {AxiosPromise} from 'axios';
import {AppDatasetVO, AppDatasetForm, AppDatasetQuery} from '@/api/witdock/appDataset/types';

/**
 * 查询应用数据集列表
 * @param query
 * @returns {*}
 */

export const listAppDataset = (query?: AppDatasetQuery): AxiosPromise<AppDatasetVO[]> => {
  return request({
    url: '/witdock/appDataset/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询应用数据集详细
 * @param id
 */
export const getAppDataset = (id: string | number): AxiosPromise<AppDatasetVO> => {
  return request({
    url: '/witdock/appDataset/' + id,
    method: 'get'
  });
};

/**
 * 新增应用数据集
 * @param data
 */
export const addAppDataset = (data: AppDatasetForm) => {
  return request({
    url: '/witdock/appDataset',
    method: 'post',
    data: data
  });
};

/**
 * 修改应用数据集
 * @param data
 */
export const updateAppDataset = (data: AppDatasetForm) => {
  return request({
    url: '/witdock/appDataset',
    method: 'put',
    data: data
  });
};

/**
 * 删除应用数据集
 * @param id
 */
export const delAppDataset = (id: string | number | Array<string | number>) => {
  return request({
    url: '/witdock/appDataset/' + id,
    method: 'delete'
  });
};

/**
 * 删除应用数据集
 * @param id
 */
export const delAppDatasetByBothId = (data: AppDatasetForm) => {
  return request({
    url: '/witdock/appDataset/delAppDatasetByBothId/' + data.appId + "/" + data.datasetId,
    method: 'delete'
  });
};
