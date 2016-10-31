# Play URL Path Base Internationalization (I18n)
**!! THIS IS UNDER DEVELOPMENT !!**

This is a request handler for play framework Scala. It handles the request path for all play default router to use internationalized path as [the language tag](https://www.w3.org/International/articles/language-tags/), and resets `Accept-Language`.
```
http://example.com
http://example.com/en-US
```

**If you find any weird behavior or bugs please report or issue me :) I always welcome your pull requests!**

## Installation

#### Dependency
Add those resolver and dependency to `build.sbt`:<br>
**Currently it only supports Play Framework 2.5.x**
```scala
// Adding resolver
resolvers += "EJISAN" at "https://ejisan.github.io/repo/"
// Adding dependency
libraryDependencies += "com.ejisan" %% "play-i18n-url" % "1.0.0-SNAPSHOT"
```

## Uses
1. Add the request handler (`conf/application.conf`)<br>
```
play.http {
  requestHandler = "ejisan.play.i18n.UrlI18nRequestHandler"
}
```

2. Add the application languages (`conf/application.conf`)<br>
The format of the languages must fallow Language Tag Syntax which is defined in [RFC5646](http://www.rfc-editor.org/rfc/rfc5646.txt).
```
play.i18n {
  # The application languages
  langs = [ "en-US", "es-US", "ja-JP", "en-JP" ]
  ...
```

3. Your application is ready!<br>
`UrlI18nRequestHandler` will automatically add language tag at the head of the requested path and set to `Accept-Language`. If the language tag in the requested path is case insensitive, `UrlI18nRequestHandler` replaces it as right case and redirects it.
```
# http://example.com/en-US or http://example.com/ja-JP etc
GET     /                     controllers.Controller.index
# http://example.com/en-US/page1 or http://example.com/ja-JP/page1 etc
GET     /page1                controllers.Controller.page1
```

### Sird router with Non-Internationalized Routing
Check about [sird router](https://www.playframework.com/documentation/2.5.x/ScalaSirdRouter)
```scala
import javax.inject.Inject
import play.api.http.{ HttpErrorHandler, HttpConfiguration }
import play.api.i18n.DefaultLangs
import play.api.mvc._
import play.api.routing._
import play.api.routing.sird._
import ejisan.play.i18n.{ UrlI18nRequestHandler, ScalaSirdRouterSupport }

class MyUrlI18nRequestHandler @Inject()(
  langs: DefaultLangs,
  defaultRouter: Router,
  errorHandler: HttpErrorHandler,
  httpConfiguration: HttpConfiguration)
  extends UrlI18nRequestHandler(langs, defaultRouter, errorHandler, httpConfiguration)
  with ScalaSirdRouterSupport {
  // Using a sird router
  def router: Router = Router.from {
    case GET(p"/") => Action {
      Results.Ok("Hello Play!")
    }
  }
}
```
