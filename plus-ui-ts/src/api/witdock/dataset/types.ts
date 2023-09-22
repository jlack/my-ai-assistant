export interface DatasetVO {
  /**
   *
   */
  id: string | number;

  /**
   * 数据集名称
   */
  datasetName: string;

  /**
   * 数据集描述
   */
  datasetDesc: string;

  /**
   * me\all
   */
  visiblePermission: string;

  /**
   * 是否删除
   */
  isDeleted: number;

  /**
   * 文档数量
   */
  docNum: number;

  /**
   * 字符数量
   */
  charNum: number;

}

export interface DatasetForm extends BaseEntity {
  /**
   *
   */
  id?: string | number;

  /**
   * 数据集名称
   */
  datasetName?: string;

  /**
   * 数据集描述
   */
  datasetDesc?: string;

  /**
   * me\all
   */
  visiblePermission?: string;

  /**
   * 是否删除
   */
  isDeleted?: number;

}

export interface DatasetQuery extends PageQuery {

  /**
   * 数据集名称
   */
  datasetName?: string;

  /**
   * 数据集描述
   */
  datasetDesc?: string;

  /**
   * me\all
   */
  visiblePermission?: string;

  /**
   * 是否删除
   */
  isDeleted?: number;

  /**
   * 日期范围参数
   */
  params?: any;
}



