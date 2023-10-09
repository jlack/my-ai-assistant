export interface DocParagraphsVO {
  /**
   * id
   */
  id: string | number;

  /**
   * 文档id
   */
  docId: string | number;

  /**
   * 段落编号
   */
  sno: number;

  /**
   * 状态（active,inactive）可用、禁用
   */
  status: string;

  /**
   * 段落内容
   */
  content: string;

  /**
   * 字数
   */
  charNum: number;

}

export interface DocParagraphsForm extends BaseEntity {
  /**
   * id
   */
  id?: string | number;

  /**
   * 文档id
   */
  docId?: string | number;

  /**
   * 段落编号
   */
  sno?: number;

  /**
   * 状态（active,inactive）可用、禁用
   */
  status?: string;

  /**
   * 段落内容
   */
  content?: string;

  /**
   * 字数
   */
  charNum?: number;

}

export interface DocParagraphsQuery extends PageQuery {

  /**
   * 文档id
   */
  docId?: string | number;

  /**
   * 段落编号
   */
  sno?: number;

  /**
   * 状态（active,inactive）可用、禁用
   */
  status?: string;

  /**
   * 段落内容
   */
  content?: string;

  /**
   * 字数
   */
  charNum?: number;

  /**
   * 日期范围参数
   */
  params?: any;
}

/** 激活状态 */
export enum ParaStatus {
  /** 激活 */
  ACTIVE = 'active',
  /** 未激活 */
  INACTIVE = 'inactive'
}
