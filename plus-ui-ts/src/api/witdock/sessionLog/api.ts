import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { SessionLogVO, SessionLogForm, SessionLogQuery } from '@/api/witdock/sessionLog/types';

/**
 * 查询会话日志表列表
 * @param query
 * @returns {*}
 */

export const listSessionLog = (query?: SessionLogQuery): AxiosPromise<SessionLogVO[]> => {
  return request({
    url: '/witdock/sessionLog/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询会话日志表详细
 * @param id
 */
export const getSessionLog = (id: string | number): AxiosPromise<SessionLogVO> => {
  return request({
    url: '/witdock/sessionLog/' + id,
    method: 'get'
  });
};

/**
 * 新增会话日志表
 * @param data
 */
export const addSessionLog = (data: SessionLogForm) => {
  return request({
    url: '/witdock/sessionLog',
    method: 'post',
    data: data
  });
};

/**
 * 修改会话日志表
 * @param data
 */
export const updateSessionLog = (data: SessionLogForm) => {
  return request({
    url: '/witdock/sessionLog',
    method: 'put',
    data: data
  });
};

/**
 * 删除会话日志表
 * @param id
 */
export const delSessionLog = (id: string | number | Array<string | number>) => {
  return request({
    url: '/witdock/sessionLog/' + id,
    method: 'delete'
  });
};
