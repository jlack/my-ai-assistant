import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { DatasetVO, DatasetForm, DatasetQuery } from '@/api/witdock/dataset/types';

/**
 * 查询数据集列表
 * @param query
 * @returns {*}
 */

export const listDataset = (query?: DatasetQuery): AxiosPromise<DatasetVO[]> => {
  return request({
    url: '/witdock/dataset/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询数据集详细
 * @param id
 */
export const getDataset = (id: string | number): AxiosPromise<DatasetVO> => {
  return request({
    url: '/witdock/dataset/' + id,
    method: 'get'
  });
};

/**
 * 新增数据集
 * @param data
 */
export const addDataset = (data: DatasetForm) => {
  return request({
    url: '/witdock/dataset',
    method: 'post',
    data: data
  });
};

/**
 * 修改数据集
 * @param data
 */
export const updateDataset = (data: DatasetForm) => {
  return request({
    url: '/witdock/dataset',
    method: 'put',
    data: data
  });
};

/**
 * 删除数据集
 * @param id
 */
export const delDataset = (id: string | number | Array<string | number>) => {
  return request({
    url: '/witdock/dataset/' + id,
    method: 'delete'
  });
};
