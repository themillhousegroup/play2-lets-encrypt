package com.themillhousegroup.play2letsencrypt

import javax.inject.Inject

import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.json.Json
import play.api.mvc.{ Action, Controller }
import com.themillhousegroup.play2letsencrypt.LetsEncryptChallengeResponsePairJson._

class LetsEncryptController @Inject() (val letsEncryptService: LetsEncryptService) extends Controller {

  def respondTo(challenge: String) = Action.async {

    letsEncryptService.findResponseFor(challenge).map { maybeResponseText =>
      maybeResponseText.fold {
        NotFound(s"No response found for challenge $challenge")
      } { r =>
        Ok(r)
      }
    }
  }

  def insertPair(challenge: String, response: String) = Action.async {

    letsEncryptService.savePair(challenge, response).map { maybeSavedPair =>
      maybeSavedPair.fold {
        NotFound("Wasn't able to save that pair :-(")
      } { r =>
        Ok(Json.toJson(r))
      }
    }
  }
}
