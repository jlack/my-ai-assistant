import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { InfoVO, InfoForm, InfoQuery } from '@/api/witdock/appInfo/types';

/**
 * 查询构建应用列表
 * @param query
 * @returns {*}
 */

export const listInfo = (query?: InfoQuery): AxiosPromise<InfoVO[]> => {
  return request({
    url: '/system/info/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询构建应用详细
 * @param id
 */
export const getInfo = (id: string | number): AxiosPromise<InfoVO> => {
  return request({
    url: '/system/info/' + id,
    method: 'get'
  });
};

/**
 * 新增构建应用
 * @param data
 */
export const addInfo = (data: InfoForm) => {
  return request({
    url: '/system/info',
    method: 'post',
    data: data
  });
};

/**
 * 修改构建应用
 * @param data
 */
export const updateInfo = (data: InfoForm) => {
  return request({
    url: '/system/info',
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
    url: '/system/info/' + id,
    method: 'delete'
  });
};
