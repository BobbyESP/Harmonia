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

**You are free to:**

- 📤 **Share** - copy and redistribute the material in any medium or format

The licensor cannot revoke these freedoms as long as you follow the license terms.

**Under the following terms:**

- ©️ **Attribution** - You must give appropriate credit , provide a link to the license, and indicate if changes were
  made. You may do so in any reasonable manner, but not in any way that suggests the licensor endorses you or your use.
- 🚫 **NonCommercial** - You may not use the material for commercial purposes.
- 🚫 **NoDerivatives** - If you remix, transform, or build upon the material, you may not distribute the modified
  material.
- 🚫 **No additional restrictions** - You may not apply legal terms or technological measures that legally restrict
  others from doing anything the license permits.
