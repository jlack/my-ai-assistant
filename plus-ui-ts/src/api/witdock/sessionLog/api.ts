import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { LogVO, LogForm, LogQuery } from '@/api/system/log/types';

/**
 * 查询会话日志表列表
 * @param query
 * @returns {*}
 */

export const listLog = (query?: LogQuery): AxiosPromise<LogVO[]> => {
  return request({
    url: '/system/log/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询会话日志表详细
 * @param id
 */
export const getLog = (id: string | number): AxiosPromise<LogVO> => {
  return request({
    url: '/system/log/' + id,
    method: 'get'
  });
};

/**
 * 新增会话日志表
 * @param data
 */
export const addLog = (data: LogForm) => {
  return request({
    url: '/system/log',
    method: 'post',
    data: data
  });
};

/**
 * 修改会话日志表
 * @param data
 */
export const updateLog = (data: LogForm) => {
  return request({
    url: '/system/log',
    method: 'put',
    data: data
  });
};

/**
 * 删除会话日志表
 * @param id
 */
export const delLog = (id: string | number | Array<string | number>) => {
  return request({
    url: '/system/log/' + id,
    method: 'delete'
  });
};
