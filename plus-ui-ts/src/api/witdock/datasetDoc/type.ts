export interface AddDocsBo {

  datasetId: string | number;

  ossIds: string | undefined;
}

export interface DocVO {
  /**
   * 文档id
   */
  id: string | number;

  /**
   * 数据集id
   */
  datasetId: string | number;

  /**
   * 文档名称
   */
  docName: string;

  /**
   * 对象存储id
   */
  ossId: string | number | object;

  /**
   * 字符数
   */
  charNum: number;

  /**
   * 状态（active,inactive,archived）可用、禁用、归档
   */
  status: string;

  fileSize: number | string

  createTime: string

  createByName: string
  updateTime: string
  updateByName: string
}

export interface DocForm extends BaseEntity {
  /**
   *
   */
  id?: string | number;

  /**
   * 数据集id
   */
  datasetId?: string | number;

  /**
   * 文档名称
   */
  docName?: string;

  /**
   * 对象存储id
   */
  ossId?: string | number | object;

  /**
   * 字符数
   */
  charNum?: number;

  /**
   * 状态（active,inactive,archived）可用、禁用、归档
   */
  status?: string;

  ossVo?: any;
}

export interface DocQuery extends PageQuery {

  /**
   * 数据集id
   */
  datasetId?: string | number;

  /**
   * 文档名称
   */
  docName?: string;

  /**
   * 对象存储id
   */
  ossId?: string | number;

  /**
   * 字符数
   */
  charNum?: number;

  /**
   * 状态（active,inactive,archived）可用、禁用、归档
   */
  status?: string;

  /**
   * 日期范围参数
   */
  params?: any;
}

/** 状态 */
export enum DocStatus {
  /** 激活 */
  ACTIVE = 'active',
  /** 未激活 */
  INACTIVE = 'inactive',
  /** 归档 */
  ARCHIVED = 'archived',
}

