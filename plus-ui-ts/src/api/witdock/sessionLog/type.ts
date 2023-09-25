export interface LogVO {
  /**
   *
   */
  id: string | number;

  /**
   * 对话id
   */
  sessionId: string | number;

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

}

export interface LogForm extends BaseEntity {
  /**
   *
   */
  id?: string | number;

  /**
   * 对话id
   */
  sessionId?: string | number;

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

}

export interface LogQuery extends PageQuery {

  /**
   * 对话id
   */
  sessionId?: string | number;

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



