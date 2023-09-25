export interface AppDatasetVO {
  /**
   *
   */
  id: string | number;

  /**
   * 应用id
   */
  appId: string | number;

  /**
   * 数据集id
   */
  datasetId: string | number;

}

export interface AppDatasetForm extends BaseEntity {
  /**
   *
   */
  id?: string | number;

  /**
   * 应用id
   */
  appId?: string | number;

  /**
   * 数据集id
   */
  datasetId?: string | number;

}

export interface AppDatasetQuery extends PageQuery {

  /**
   * 应用id
   */
  appId?: string | number;

  /**
   * 数据集id
   */
  datasetId?: string | number;

    /**
     * 日期范围参数
     */
    params?: any;
}



