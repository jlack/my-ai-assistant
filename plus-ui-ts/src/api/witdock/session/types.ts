export interface SessionVO {
  /**
   *
   */
  id: string | number;

  /**
   * 应用id
   */
  appId: string | number;

  /**
   * 会话标题
   */
  sessionTitle: string;

  /**
   * 发起用户
   */
  userId: string | number;

  /**
   * 是否置顶
   */
  topping: boolean;

}

export interface SessionForm extends BaseEntity {
  /**
   *
   */
  id?: string | number;

  /**
   * 应用id
   */
  appId?: string | number;

  /**
   * 会话标题
   */
  sessionTitle?: string;

  /**
   * 发起用户
   */
  userId?: string | number;

  /**
   * 是否置顶
   */
  topping?: boolean;

}

export interface SessionQuery extends PageQuery {

  /**
   * 应用id
   */
  appId?: string | number;

  /**
   * 会话标题
   */
  sessionTitle?: string;

  /**
   * 发起用户
   */
  userId?: string | number;

  /**
   * 是否置顶
   */
  topping?: boolean;

  /**
   * 日期范围参数
   */
  params?: any;
}



