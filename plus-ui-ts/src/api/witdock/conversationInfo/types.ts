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
   * 是否删除
   */
  isDeleted?: boolean;

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
}



