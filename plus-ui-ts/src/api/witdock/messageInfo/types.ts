export interface MessageInfoVO {
  /**
   *
   */
  id: string | number;

  /**
   * 对话id
   */
  conversationId: string | number;

  /**
   * 提问内容
   */
  query: string;

  /**
   * 回答内容
   */
  answer: string;

  /**
   * 回答时间
   */
  reDatetime: string;

  /**
   * 花费token
   */
  msgToken: number;

  /**
   * 本地客户端生成的id
   */
  msgLocalId: string;

  /**
   * 流式回答文本内容
   */
  streamText: string;
}

export interface MessageInfoForm extends BaseEntity {
  /**
   *
   */
  id?: string | number;

  /**
   * 对话id
   */
  conversationId?: string | number;

  /**
   * 提问内容
   */
  query?: string;

  /**
   * 回答内容
   */
  answer?: string;

  /**
   * 回答时间
   */
  reDatetime?: string;

  /**
   * 花费token
   */
  msgToken?: number;

  /**
   * 本地客户端生成的id
   */
  msgLocalId?: string;
}

export interface MessageInfoQuery extends PageQuery {

  /**
   * 对话id
   */
  conversationId?: string | number;

  /**
   * 提问内容
   */
  query?: string;

  /**
   * 回答内容
   */
  answer?: string;

  /**
   * 回答时间
   */
  reDatetime?: string;

  /**
   * 花费token
   */
  msgToken?: number;

  /**
   * 日期范围参数
   */
  params?: any;
}



