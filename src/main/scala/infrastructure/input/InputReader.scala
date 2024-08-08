package infrastructure.input

import scala.io.{BufferedSource, Source}
import cats.effect.{Resource, Sync}

object InputReader {
  def read[F[_]: Sync](filePath: String): F[List[String]] = {
    val acquire: F[BufferedSource]         = Sync[F].delay(Source.fromFile(filePath))
    val release: BufferedSource => F[Unit] = source => Sync[F].delay(source.close())

    Resource.make(acquire)(release).use { source =>
      Sync[F].delay(source.getLines().toList)
    }
  }
}
