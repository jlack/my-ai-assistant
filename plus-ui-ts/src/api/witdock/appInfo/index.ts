import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { appVO, appForm, appQuery } from '@/api/witdock/appInfo/type';

/**
 * 查询构建应用列表
 * @param query
 * @returns {*}
 */

export const listInfo = (query?: appQuery): AxiosPromise<appVO[]> => {
  return request({
    url: '/witdock/info/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询构建应用详细
 * @param id
 */
export const getInfo = (id: string | number): AxiosPromise<appVO> => {
  return request({
    url: '/witdock/info/' + id,
    method: 'get'
  });
};

/**
 * 新增构建应用
 * @param data
 */
export const addInfo = (data: appForm) => {
  return request({
    url: '/witdock/info',
    method: 'post',
    data: data
  });
};

/**
 * 修改构建应用
 * @param data
 */
export const updateInfo = (data: appForm) => {
  return request({
    url: '/witdock/info',
    method: 'put',
    data: data
  });
};

/**
 * 删除构建应用
 * @param id
 */
export const delInfo = (id: string | number | Array<string | number>) => {
  return request({
    url: '/witdock/info/' + id,
    method: 'delete'
  });
};
