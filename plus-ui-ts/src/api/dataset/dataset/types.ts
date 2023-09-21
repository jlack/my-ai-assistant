export interface DatasetVO {
  /**
   * 
   */
  datasetName: string;

  /**
   * 
   */
  datasetDesc: string;

  /**
   * 
   */
  visiblePermission: string;

}

export interface DatasetForm extends BaseEntity {
  /**
   * 
   */
  datasetName?: string;

  /**
   * 
   */
  datasetDesc?: string;

  /**
   * 
   */
  visiblePermission?: string;

}

export interface DatasetQuery extends PageQuery {

  /**
   * 
   */
  datasetName?: string;

  /**
   * 
   */
  datasetDesc?: string;

  /**
   * 
   */
  visiblePermission?: string;

    /**
     * 日期范围参数
     */
    params?: any;
}



