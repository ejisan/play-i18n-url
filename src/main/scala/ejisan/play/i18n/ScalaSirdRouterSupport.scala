package ejisan.play.i18n

import play.api.http.DefaultHttpRequestHandler
import play.api.routing.Router
import play.api.mvc.{ RequestHeader, Handler }

trait ScalaSirdRouterSupport extends DefaultHttpRequestHandler { _: UrlI18nRequestHandler =>
  def router: Router

  override def routeRequest(request: RequestHeader): Option[Handler] = {
    val handler = router.handlerFor(request)
    if (handler.isEmpty) super.routeRequest(request)
    else handler
  }
}
