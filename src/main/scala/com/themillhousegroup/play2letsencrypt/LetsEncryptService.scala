package com.themillhousegroup.play2letsencrypt

import javax.inject.Inject

import com.themillhousegroup.mondrian.TypedMongoService
import play.api.libs.json.Json
import play.modules.reactivemongo.ReactiveMongoApi
import com.themillhousegroup.play2letsencrypt.LetsEncryptChallengeResponsePairJson._

import scala.concurrent.Future

class LetsEncryptService @Inject() (val reactiveMongoApi: ReactiveMongoApi)
    extends TypedMongoService[LetsEncryptChallengeResponsePair]("letsEncryptPairs") {

  def findByChallenge(challenge: String): Future[Option[LetsEncryptChallengeResponsePair]] = {
    findOne(Json.obj("challenge" -> challenge))
  }

  def findResponseFor(challenge: String): Future[Option[String]] = {
    findByChallenge(challenge).map { maybePair =>
      maybePair.map(_.response)
    }
  }

  def savePair(challenge: String, responseText: String): Future[Option[LetsEncryptChallengeResponsePair]] = {

    findByChallenge(challenge).flatMap { maybeExistingPairWithThisChallenge =>
      val whatToSave = maybeExistingPairWithThisChallenge.fold {
        // Brand new:
        new LetsEncryptChallengeResponsePair(None, challenge, responseText)
      } { existingPair =>
        existingPair.copy(response = responseText)
      }

      saveAndPopulate(whatToSave)
    }
  }

}
