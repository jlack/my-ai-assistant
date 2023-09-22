export interface AppVO {
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
   * 逻辑删除
   */
  isDeleted: number;

  /**
   * 应用Code
   */
  code: string;

  /**
   * 启用webapp
   */
  enableSite: number;

  /**
   * 启用服务API
   */
  enableApi: number;

}

export interface AppForm extends BaseEntity {
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
   * 逻辑删除
   */
  isDeleted?: number;

  /**
   * 应用Code
   */
  code?: string;

  /**
   * 启用webapp
   */
  enableSite?: number;

  /**
   * 启用服务API
   */
  enableApi?: number;

}

export interface AppQuery extends PageQuery {

  /**
   * 应用名
   */
  appName?: string;

  /**
   * 应用描述
   */
  appDesc?: string;

  /**
   * 逻辑删除
   */
  isDeleted?: number;

  /**
   * 应用Code
   */
  code?: string;

  /**
   * 启用webapp
   */
  enableSite?: number;

  /**
   * 启用服务API
   */
  enableApi?: number;

  /**
   * 日期范围参数
   */
  params?: any;
}



