package watch.dependency

import kotlinx.coroutines.delay
import watch.dependency.Debug.Disabled
import kotlin.time.Duration

class DependencyAwait(
	private val mavenRepository: MavenRepository,
	private val notifier: Notifier,
	private val checkInterval: Duration,
	private val debug: Debug = Disabled,
) {
	suspend fun await(
    coordinate: MavenCoordinate,
    version: String
  ) {
		while (true) {
			debug.log { "Fetching metadata for $coordinate..." }
			val versions = mavenRepository.versions(coordinate)
			debug.log { "$coordinate $versions" }

			if (version in versions) {
				break
			}

			debug.log { "Sleeping $checkInterval..." }
      delay(checkInterval)
		}

		notifier.notify(coordinate, version)
	}
}
