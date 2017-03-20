package com.themillhousegroup.play2letsencrypt

import com.themillhousegroup.mondrian.{ MongoEntity, MongoId, MongoJson }
import play.api.libs.json.Json

case class LetsEncryptChallengeResponsePair(
  _id: Option[MongoId],
  challenge: String,
  response: String) extends MongoEntity

object LetsEncryptChallengeResponsePairJson extends MongoJson {
  implicit val lecrpConverter = Json.format[LetsEncryptChallengeResponsePair]
}
