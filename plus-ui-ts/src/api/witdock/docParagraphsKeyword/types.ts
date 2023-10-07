export interface DocParagraphsKeywordVO {
  /**
   * 段落id
   */
  paragraphsId: string | number;

  /**
   * 关键字
   */
  keyword: string;

}

export interface DocParagraphsKeywordForm extends BaseEntity {
  /**
   * 段落id
   */
  paragraphsId?: string | number;

  /**
   * 关键字
   */
  keyword?: string;

}

export interface DocParagraphsKeywordQuery extends PageQuery {

  /**
   * 段落id
   */
  paragraphsId?: string | number;

  /**
   * 关键字
   */
  keyword?: string;

  /**
   * 日期范围参数
   */
  params?: any;
}



