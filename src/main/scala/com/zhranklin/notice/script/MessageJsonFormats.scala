package com.zhranklin.notice.script

import org.json4s.{DefaultFormats, ShortTypeHints, TypeHints}

trait MessageJsonFormats {
  implicit val format = new DefaultFormats {
    override val typeHintFieldName = "type"
    override val typeHints: TypeHints = ShortTypeHints(List(
      classOf[listsources],
      classOf[err],
      classOf[succ],
      classOf[getnews],
      classOf[fetch_test],
      classOf[getpictures],
      classOf[create_source],
      classOf[fetchnews]
    ))
  }
}
