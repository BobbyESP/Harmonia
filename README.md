# Harmonia

![License](https://img.shields.io/static/v1?label=license&message=CC-BY-NC-ND-4.0&color=blue)

A modern and beautiful music player for Android.

## Project modules

**Note:** `ui` package in `app` module is excluded from the Git now, because it's not ready yet.

- [app](app/README.md)
- [media](media/README.md)
- [ui](ui/README.md)

## Libraries

### Third-party

- [Glide](https://github.com/bumptech/glide)
- [GridPad](https://github.com/touchlane/gridpad-android)

### From [Kyant](https://github.com/Kyant0)

- [m3color](https://github.com/Kyant0/m3color)
- [taglib](https://github.com/Kyant0/taglib)

## Contributing

### Code style

Kotlin code style is enforced by `ktlint` plugin with **Android mode**.

### Extra checks

- Run `./gradlew :app:releaseComposeCompilerHtmlReport :ui:releaseComposeCompilerHtmlReport --rerun-tasks` to check
  stability for Compose. You may disable configuration cache of Gradle temporarily.

## License

This project is licensed under the [CC BY-NC-ND 4.0](LICENSE.md) (Attribution-NonCommercial-NoDerivs 4.0 International).
