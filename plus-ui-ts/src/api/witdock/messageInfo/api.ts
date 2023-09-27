import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { MessageInfoVO, MessageInfoForm, MessageInfoQuery } from '@/api/witdock/messageInfo/types';

/**
 * 查询对话消息列表
 * @param query
 * @returns {*}
 */

export const listMessageInfo = (query?: MessageInfoQuery): AxiosPromise<MessageInfoVO[]> => {
  return request({
    url: '/witdock/messageInfo/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询对话消息详细
 * @param id
 */
export const getMessageInfo = (id: string | number): AxiosPromise<MessageInfoVO> => {
  return request({
    url: '/witdock/messageInfo/' + id,
    method: 'get'
  });
};

/**
 * 新增对话消息
 * @param data
 */
export const addMessageInfo = (data: MessageInfoForm) => {
  return request({
    url: '/witdock/messageInfo',
    method: 'post',
    data: data
  });
};

/**
 * 修改对话消息
 * @param data
 */
export const updateMessageInfo = (data: MessageInfoForm) => {
  return request({
    url: '/witdock/messageInfo',
    method: 'put',
    data: data
  });
};

/**
 * 删除对话消息
 * @param id
 */
export const delMessageInfo = (id: string | number | Array<string | number>) => {
  return request({
    url: '/witdock/messageInfo/' + id,
    method: 'delete'
  });
};
