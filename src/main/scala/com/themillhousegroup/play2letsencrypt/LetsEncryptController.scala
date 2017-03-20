package com.themillhousegroup.play2letsencrypt

import javax.inject.Inject

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import play.api.mvc.{ Action, Controller, Result }
import com.themillhousegroup.play2letsencrypt.LetsEncryptChallengeResponsePairJson._
import com.themillhousegroup.sses.{ PasswordProtected, UsernamePasswordProtected }
import play.api.Configuration

import scala.concurrent.Future

class LetsEncryptController @Inject() (val letsEncryptService: LetsEncryptService,
    val configuration: Configuration) extends Controller {

  lazy val endpointUsername = configuration.getString("letsencrypt.endpoint.username").getOrElse("l3ts3ncrypt")
  lazy val endpointPassword = configuration.getString("letsencrypt.endpoint.password").getOrElse("l3ts3ncrypt")

  def respondTo(challenge: String) = Action.async {

    letsEncryptService.findResponseFor(challenge).map { maybeResponseText =>
      maybeResponseText.fold {
        NotFound(s"No response found for challenge $challenge")
      } { r =>
        Ok(r)
      }
    }
  }

  def insertPairNoSecurity(challenge: String, response: String) = Action.async {
    insertPair(challenge, response)
  }

  def insertPairRequiringPassword(challenge: String, response: String) = PasswordProtected(endpointPassword).async {

    insertPair(challenge, response)
  }

  def insertPairRequiringUsernamePassword(challenge: String, response: String) = UsernamePasswordProtected(endpointUsername, endpointPassword).async {

    insertPair(challenge, response)
  }

  private def insertPair(challenge: String, response: String): Future[Result] = {

    letsEncryptService.savePair(challenge, response).map { maybeSavedPair =>
      maybeSavedPair.fold {
        NotFound("Wasn't able to save that pair :-(")
      } { r =>
        Ok(Json.toJson(r))
      }
    }
  }
}
