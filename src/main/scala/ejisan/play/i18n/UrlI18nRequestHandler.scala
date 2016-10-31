package ejisan.play.i18n

import javax.inject.Inject
import play.api.routing.Router
import play.api.http._
import play.api.mvc._
import play.api.i18n.{ DefaultLangs, Lang }

class UrlI18nRequestHandler(
  langs: DefaultLangs,
  router: Router,
  errorHandler: HttpErrorHandler,
  httpConfiguration: HttpConfiguration,
  filters: EssentialFilter*
) extends DefaultHttpRequestHandler(router, errorHandler, httpConfiguration, filters: _*) {
  @Inject
  def this(langs: DefaultLangs, router: Router, errorHandler: HttpErrorHandler, httpConfiguration: HttpConfiguration, filters: HttpFilters) =
    this(langs, router, errorHandler, httpConfiguration, filters.filters: _*)

  def getHeadOfPath(request: RequestHeader): Option[String] =
    request.path.tail.split('/').headOption

  def getLangFromRequest(request: RequestHeader): Option[(Lang, String)] =
    getHeadOfPath(request)
      .flatMap(p => Lang.get(p).map((_, p)))
      .filter(l => langs.availables.exists(_.code == l._1.code))

  def setAcceptLanguage(request: RequestHeader, lang: Lang): RequestHeader =
    request.copy(headers = request.headers.replace(HeaderNames.ACCEPT_LANGUAGE -> lang.code))

  override def handlerForRequest(request: RequestHeader): (RequestHeader, Handler) =
    super.handlerForRequest(
      getLangFromRequest(request).map(l => setAcceptLanguage(request, l._1)).getOrElse(request))

  override def routeRequest(request: RequestHeader): Option[Handler] =
    getLangFromRequest(request) match {
      case Some((lang, path)) =>
        val langPath = "/" + lang.code
        if (path == lang.code) {
          router.withPrefix(langPath).routes.lift(request)
        } else Some {
          Action(BodyParsers.parse.empty) { req =>
            val r = langPath + request.path.stripPrefix(s"/$path")
            Results.MovedPermanently((new Call(HttpVerbs.GET, r)).absoluteURL()(req)) }
        }
      case None => Some {
        Action.async(BodyParsers.parse.empty)(errorHandler.onClientError(_, Status.NOT_FOUND)) }
    }
}
