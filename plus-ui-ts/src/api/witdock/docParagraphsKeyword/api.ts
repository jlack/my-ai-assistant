import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { DocParagraphsKeywordVO, DocParagraphsKeywordForm, DocParagraphsKeywordQuery } from '@/api/witdock/docParagraphsKeyword/types';

/**
 * 查询段落关键字表列表
 * @param query
 * @returns {*}
 */

export const listDocParagraphsKeyword = (query?: DocParagraphsKeywordQuery): AxiosPromise<DocParagraphsKeywordVO[]> => {
  return request({
    url: '/witdock/docParagraphsKeyword/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询段落关键字表详细
 * @param paragraphsId
 */
export const getDocParagraphsKeyword = (paragraphsId: string | number): AxiosPromise<DocParagraphsKeywordVO> => {
  return request({
    url: '/witdock/docParagraphsKeyword/' + paragraphsId,
    method: 'get'
  });
};

/**
 * 新增段落关键字表
 * @param data
 */
export const addDocParagraphsKeyword = (data: DocParagraphsKeywordForm) => {
  return request({
    url: '/witdock/docParagraphsKeyword',
    method: 'post',
    data: data
  });
};

/**
 * 修改段落关键字表
 * @param data
 */
export const updateDocParagraphsKeyword = (data: DocParagraphsKeywordForm) => {
  return request({
    url: '/witdock/docParagraphsKeyword',
    method: 'put',
    data: data
  });
};

/**
 * 删除段落关键字表
 * @param paragraphsId
 */
export const delDocParagraphsKeyword = (paragraphsId: string | number | Array<string | number>) => {
  return request({
    url: '/witdock/docParagraphsKeyword/' + paragraphsId,
    method: 'delete'
  });
};
