import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { appVO, appForm, appQuery } from '@/api/witdock/app/type';

/**
 * 查询构建应用列表
 * @param query
 * @returns {*}
 */

export const listApp = (query?: appQuery): AxiosPromise<appVO[]> => {
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
export const getApp = (id: string | number): AxiosPromise<appVO> => {
  return request({
    url: '/witdock/app/' + id,
    method: 'get'
  });
};

/**
 * 新增构建应用
 * @param data
 */
export const addApp = (data: appForm) => {
  return request({
    url: '/witdock/app',
    method: 'post',
    data: data
  });
};

/**
 * 修改构建应用
 * @param data
 */
export const updateApp = (data: appForm) => {
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
