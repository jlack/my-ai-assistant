import request from '@/utils/request';
import {AxiosPromise} from 'axios';
import {AppVO, AppForm, AppQuery} from '@/api/witdock/app/type';

/**
 * 查询构建应用列表
 * @param query
 * @returns {*}
 */

export const listApp = (query?: AppQuery): AxiosPromise<AppVO[]> => {
  return request({
    url: '/witdock/app/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询构建应用详细
 * @param id
 */
export const getApp = (id: string | number): AxiosPromise<AppVO> => {
  return request({
    url: '/witdock/app/' + id,
    method: 'get'
  });
};


/**
 * 添加数据集到APP
 */
export const addDatasetToApp = (data: {}) => {
  return request({
    url: '/witdock/app/addDatasetToApp',
    method: 'post',
    data: data
  });
}


export const getConfig = (appId: number) => {
  return request({
    url: '/witdock/app/getConfig/' + appId,
    method: 'get',
  });
}

/**
 * 新增构建应用
 * @param data
 */
export const addApp = (data: AppForm) => {
  return request({
    url: '/witdock/app',
    method: 'post',
    data: data
  });
};

/**
 * 重置URL
 */
export const resetCode = (id: string | number) => {
  let data = {
    id: id
  }
  return request({
    url: '/witdock/app/resetCode',
    method: 'put',
    data: data
  });
}

/**
 * 修改构建应用
 * @param data
 */
export const updateApp = (data: AppForm) => {
  return request({
    url: '/witdock/app',
    method: 'put',
    data: data
  });
};

/**
 * 删除构建应用
 * @param id
 */
export const delApp = (id: string | number | Array<string | number>) => {
  return request({
    url: '/witdock/app/' + id,
    method: 'delete'
  });
};
