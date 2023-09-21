export interface InfoVO {
  /**
   *
   */
  id: string | number;

  /**
   * 应用名
   */
  appName: string;

  /**
   * 应用描述
   */
  appDesc: string;

  /**
   * 是否删除
   */
  isDeleted: number;

}

export interface InfoForm extends BaseEntity {
  /**
   *
   */
  id?: string | number;

  /**
   * 应用名
   */
  appName?: string;

  /**
   * 应用描述
   */
  appDesc?: string;

  /**
   * 是否删除
   */
  isDeleted?: number;

}

export interface InfoQuery extends PageQuery {

  /**
   * 应用名
   */
  appName?: string;

  /**
   * 应用描述
   */
  appDesc?: string;

  /**
   * 是否删除
   */
  isDeleted?: number;

  /**
   * 日期范围参数
   */
  params?: any;
}



