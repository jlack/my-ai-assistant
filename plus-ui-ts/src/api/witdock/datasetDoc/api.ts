import request from '@/utils/request';
import { AxiosPromise } from 'axios';
import {DocVO, DocForm, DocQuery, AddDocsBo} from '@/api/witdock/datasetDoc/type';
import {addSplitedParasBo} from "@/api/witdock/docParagraphs/types";

/**
 * 查询数据集文档列表
 * @param query
 * @returns {*}
 */

export const listDoc = (query?: DocQuery): AxiosPromise<DocVO[]> => {
  return request({
    url: '/witdock/doc/list',
    method: 'get',
    params: query
  });
};

export const listDocByDatasetId = (query?: DocQuery): AxiosPromise<DocVO[]> => {
  return request({
    url: '/witdock/doc/listDocByDatasetId',
    method: 'get',
    params: query
  });
};

/**
 * 查询数据集文档详细
 * @param id
 */
export const getDoc = (id: string | number): AxiosPromise<DocVO> => {
  return request({
    url: '/witdock/doc/' + id,
    method: 'get'
  });
};

/**
 * 新增数据集文档
 * @param data
 */
export const addDoc = (data: DocForm) => {
  return request({
    url: '/witdock/doc',
    method: 'post',
    data: data
  });
};

/**
 * 新增多个数据集文档
 * @param data
 */
export const addDocs = (data: AddDocsBo) => {
  return request({
    url: '/witdock/doc/addDocs',
    method: 'post',
    data: data
  });
};

/**
 * 修改数据集文档
 * @param data
 */
export const updateDoc = (data: DocForm) => {
  return request({
    url: '/witdock/doc',
    method: 'put',
    data: data
  });
};

/**
 * 删除数据集文档
 * @param id
 */
export const delDoc = (id: string | number | Array<string | number>) => {
  return request({
    url: '/witdock/doc/' + id,
    method: 'delete'
  });
};

export const getDocSegResult = (data: addSplitedParasBo) => {
  if (!data.maxSegmentSizeInTokens || data.maxSegmentSizeInTokens === 0) {
    data.maxSegmentSizeInTokens = 400;
  }
  return request({
    url: '/witdock/docParagraphs/getDocSegResult',
    method: 'post',
    data: data
  });
}

export const splitDocToPara = (data: addSplitedParasBo) => {
  if (!data.maxSegmentSizeInTokens || data.maxSegmentSizeInTokens === 0) {
    data.maxSegmentSizeInTokens = 500;
  }
  return request({
    url: '/witdock/docParagraphs/splitDocToPara',
    method: 'post',
    data: data
  });
}
