import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { SessionVO, SessionForm, SessionQuery } from '@/api/witdock/session/types';

/**
 * 查询会话列表
 * @param query
 * @returns {*}
 */

export const listSession = (query?: SessionQuery): AxiosPromise<SessionVO[]> => {
  return request({
    url: '/witdock/session/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询会话详细
 * @param id
 */
export const getSession = (id: string | number): AxiosPromise<SessionVO> => {
  return request({
    url: '/witdock/session/' + id,
    method: 'get'
  });
};

/**
 * 新增会话
 * @param data
 */
export const addSession = (data: SessionForm) => {
  return request({
    url: '/witdock/session',
    method: 'post',
    data: data
  });
};

/**
 * 修改会话
 * @param data
 */
export const updateSession = (data: SessionForm) => {
  return request({
    url: '/witdock/session',
    method: 'put',
    data: data
  });
};

/**
 * 删除会话
 * @param id
 */
export const delSession = (id: string | number | Array<string | number>) => {
  return request({
    url: '/witdock/session/' + id,
    method: 'delete'
  });
};
