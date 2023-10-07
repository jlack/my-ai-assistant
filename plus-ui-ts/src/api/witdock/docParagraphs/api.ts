import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import { DocParagraphsVO, DocParagraphsForm, DocParagraphsQuery } from '@/api/witdock/docParagraphs/types';

/**
 * 查询文档段落表列表
 * @param query
 * @returns {*}
 */

export const listDocParagraphs = (query?: DocParagraphsQuery): AxiosPromise<DocParagraphsVO[]> => {
  return request({
    url: '/witdock/docParagraphs/list',
    method: 'get',
    params: query
  });
};

/**
 * 查询文档段落表详细
 * @param id
 */
export const getDocParagraphs = (id: string | number): AxiosPromise<DocParagraphsVO> => {
  return request({
    url: '/witdock/docParagraphs/' + id,
    method: 'get'
  });
};

/**
 * 新增文档段落表
 * @param data
 */
export const addDocParagraphs = (data: DocParagraphsForm) => {
  return request({
    url: '/witdock/docParagraphs',
    method: 'post',
    data: data
  });
};

/**
 * 修改文档段落表
 * @param data
 */
export const updateDocParagraphs = (data: DocParagraphsForm) => {
  return request({
    url: '/witdock/docParagraphs',
    method: 'put',
    data: data
  });
};

/**
 * 删除文档段落表
 * @param id
 */
export const delDocParagraphs = (id: string | number | Array<string | number>) => {
  return request({
    url: '/witdock/docParagraphs/' + id,
    method: 'delete'
  });
};
