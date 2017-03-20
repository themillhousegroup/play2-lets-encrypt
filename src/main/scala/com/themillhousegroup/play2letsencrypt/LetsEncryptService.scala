package com.themillhousegroup.play2letsencrypt

import javax.inject.{ Inject, Singleton }

import com.themillhousegroup.mondrian.TypedMongoService
import play.api.libs.json.Json
import play.modules.reactivemongo.ReactiveMongoApi
import com.themillhousegroup.play2letsencrypt.LetsEncryptChallengeResponsePairJson._
import play.api.Configuration

import scala.concurrent.Future

@Singleton
class LetsEncryptService @Inject() (
  val reactiveMongoApi: ReactiveMongoApi,
  val configuration: Configuration)
    extends TypedMongoService[LetsEncryptChallengeResponsePair](configuration.getString("letsencrypt.mongo.collection.name").getOrElse("letsEncryptPairs")) {

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
