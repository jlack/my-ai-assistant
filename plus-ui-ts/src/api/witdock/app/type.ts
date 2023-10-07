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
   * 应用Code
   */
  code: string;

  /**
   * 启用webapp
   */
  enableSite: boolean;

  /**
   * 启用服务API
   */
  enableApi: boolean;

  datasetIds: string[]

  /**
   * 开场白
   */
  prolog: string;

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
   * 应用Code
   */
  code?: string;

  /**
   * 启用webapp
   */
  enableSite?: boolean;

  /**
   * 启用服务API
   */
  enableApi?: boolean;

  /**
   * 开场白
   */
  prolog?: string;

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
   * 应用Code
   */
  code?: string;

  /**
   * 启用webapp
   */
  enableSite?: boolean;

  /**
   * 启用服务API
   */
  enableApi?: boolean;

  /**
   * 日期范围参数
   */
  params?: any;
}



