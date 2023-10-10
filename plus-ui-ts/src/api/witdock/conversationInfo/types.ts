export interface ConversationInfoVO {
  /**
   *
   */
  id: string | number;

  /**
   * 应用id
   */
  appId: string | number;

  /**
   * 会话token
   */
  chatToken: string;

  /**
   * 会话标题
   */
  conversationTitle: string;

  /**
   * 发起用户
   */
  userId: string | number;

  /**
   * 是否置顶
   */
  topping: boolean;

}

export interface ConversationInfoForm extends BaseEntity {
  /**
   *
   */
  id?: string | number;

  /**
   * 应用id
   */
  appId?: string | number;

  /**
   * 会话token
   */
  chatToken?: string ;

  /**
   * 会话标题
   */
  conversationTitle?: string;

  /**
   * 发起用户
   */
  userId?: string | number;

  /**
   * 是否置顶
   */
  topping?: boolean;

}

export interface ConversationInfoQuery extends PageQuery {

  /**
   * 应用id
   */
  appId?: string | number;


  /**
   * 会话标题
   */
  conversationTitle?: string;

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

  /**
   * 查找结果排序 desc/asc
   */
  isAsc: string | undefined;

  /**
   * 按照哪一个字段的值进行排序
   */
  orderByColumn: string | undefined;
}

export enum ConversationConstants {
  /** 查询会话数量 */
  QuerySize = 20

}
