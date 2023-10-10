import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { ConversationInfoVO, ConversationInfoForm, ConversationInfoQuery } from '@/api/witdock/conversationInfo/types';

/**
 * 查询会话列表
 * @param query
 * @returns {*}
 */

export const listConversationInfo = (query?: ConversationInfoQuery): AxiosPromise<ConversationInfoVO[]> => {
  return request({
    url: '/witdock/conversationInfo/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询非游客创建的会话列表
 * @param query
 * @returns {*}
 */

export const listUserConversationInfo = (query?: ConversationInfoQuery): AxiosPromise<ConversationInfoVO[]> => {
  return request({
    url: '/witdock/conversationInfo/listUserConversationInfo',
    method: 'get',
    params: query
  });
};

/**
 * 查询游客创建的会话列表
 * @param query
 * @returns {*}
 */

export const listVisitorConversationInfo = (query?: ConversationInfoQuery): AxiosPromise<ConversationInfoVO[]> => {
  return request({
    url: '/witdock/conversationInfo/listVisitorConversationInfo',
    method: 'get',
    params: query
  });
};

/**
 * 查询会话详细
 * @param id
 */
export const getConversationInfo = (id: string | number): AxiosPromise<ConversationInfoVO> => {
  return request({
    url: '/witdock/conversationInfo/' + id,
    method: 'get'
  });
};

/**
 * 新增会话
 * @param data
 */
export const addConversationInfo = (data: ConversationInfoForm) => {
  return request({
    url: '/witdock/conversationInfo',
    method: 'post',
    data: data
  });
};

/**
 * 修改会话
 * @param data
 */
export const updateConversationInfo = (data: ConversationInfoForm) => {
  return request({
    url: '/witdock/conversationInfo',
    method: 'put',
    data: data
  });
};

/**
 * 删除会话
 * @param id
 */
export const delConversationInfo = (id: string | number | Array<string | number>) => {
  return request({
    url: '/witdock/conversationInfo/' + id,
    method: 'delete'
  });
};


/**
 * 依据token查出ConversationList
 * @param chatToken
 */
export const genChatToken = () => {
  return request({
    url: '/witdock/conversationInfo/genChatToken',
    method: 'get'
  });
}
