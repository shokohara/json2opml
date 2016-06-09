package controllers

import play.api.libs.json.Json
import play.api.mvc._

import scala.util.Properties

case class OuterOutline(text: String, title: String)

object OuterOutline {
  implicit val format = Json.format[OuterOutline]
}

case class Outline(text: String, xmlUrl: String, htmlUrl: String)

object Outline {
  implicit val format = Json.format[Outline]
}

case class OPMLRequest(title: String, outline: OuterOutline, outlines: List[Outline])

object OPMLRequest {
  implicit val format = Json.format[OPMLRequest]
}

class Application extends Controller {

  def index = Action(parse.json[OPMLRequest]) { request =>
    println(opml2string(request.body))
    Ok(opml2string(request.body))
  }

  def opml2string(x: OPMLRequest) =
    s"""<?xml version="1.0" encoding="UTF-8"?>
        |  <opml version="1.0">
        |    <head>
        |      <title>${x.title}</title>
        |    </head>
        |    <body>
        |      <outline text="${x.outline.text}" title="${x.outline.title}">
        |""".stripMargin +
      x.outlines.map(o =>s"""        <outline type="rss" text="${o.text}" title="${o.text}" xmlUrl="${o.xmlUrl}" htmlUrl="${o.htmlUrl}"/>""").mkString(Properties.lineSeparator) +
      """
        |      </outline>
        |    </body>
        |  </opml>
        |</xml>""".stripMargin
}
