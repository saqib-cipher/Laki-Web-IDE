# Laki Web IDE

`Laki Web IDE` is an Android application derived from the Sketchware Pro codebase that focuses on building websites (HTML/CSS/JavaScript) using a visual, drag-and-drop editor. The IDE exposes ready-made HTML/CSS blocks and components so you can compose pages without hand-writing markup — just drag, drop, and configure.

## Project overview

- Root project name: `Laki Web IDE`
- Android application ID: `laki.webide`
- Namespace: `laki.webide`
- Minimum SDK: 26
- Target SDK: 34
- Compile SDK: 36
- Kotlin JVM target: 17

## Key features (web-focused)

- Drag-and-drop HTML builder with pre-made tags and components
- Visual CSS block editor (apply styles without writing CSS by hand)
- JavaScript snippet blocks and behaviour wiring via visual logic
- Live embedded preview of pages inside the IDE
- Export project as a ZIP containing HTML/CSS/JS or as a single-file HTML
- Publish/export options (download, FTP/SFTP, or package for Android WebView)

## Requirements

- Java 17
- Android SDK with API level 36
- Gradle wrapper included in the repository (`gradlew` / `gradlew.bat`)
- Optional: `SKETCHUB_API_KEY` environment variable for remote features

## Setup

1. Clone the repository or open this folder in Android Studio.
2. Install the Android SDK and ensure `sdkmanager` is configured.
3. (Optional) Set the `SKETCHUB_API_KEY` environment variable if you use external APIs:

	 - PowerShell (Windows):
		 ```powershell
		 $env:SKETCHUB_API_KEY = 'your_api_key_here'
		 ```

	 - macOS / Linux:
		 ```bash
		 export SKETCHUB_API_KEY='your_api_key_here'
		 ```

## Typical workflow (build a website)

1. Create a new project in the IDE and pick a page template.
2. Drag HTML components (containers, headers, buttons, images, forms) onto the canvas.
3. Style components using the CSS block editor (predefined style blocks or custom rules).
4. Add interactive behavior with JavaScript blocks or custom script snippets.
5. Preview changes live inside the embedded preview pane.
6. Export when ready.

## Export & publish options

- Export as a ZIP (recommended): downloads `index.html`, `styles.css`, `app.js`, and assets in a single ZIP.
- Export as single-file HTML: embeds CSS and JS into one standalone file for easy sharing.
- Publish to a server: upload exported files via FTP/SFTP (credentials handled by the app).
- Package as Android WebView APK: creates a minimal WebView wrapper APK from the exported site (debug-signed).

Quick export commands (developer only):

```bash
./gradlew assembleDebug      # build the Android app
./gradlew clean              # clean build artifacts
```

## Live preview behavior

- The IDE provides an embedded WebView-based live preview that updates as you change the page.
- You can also open the exported HTML in an external browser for final testing.

## Usage tips

- Use templates to bootstrap responsive layouts.
- Keep assets under the project's `assets/` folder to ensure they package with exports.
- For complex behavior, attach custom JavaScript snippets to events via the visual blocks.

## Notes

- Debug signing: project includes a debug signing configuration referencing `testkey.keystore` (parent directory).
- Build config injects `SKETCHUB_API_KEY` into `BuildConfig.SKETCHUB_API_KEY` when provided.
- Version info in `app/build.gradle`: `versionName = "LatikeshMarathe"`, `versionCode = 1`.

## License & attribution

- See `LICENSE.md` for official license details.
- Note from maintainer: you may use this project, but please do not rename the project. Preserve attribution and follow the original/legacy license terms.

## Screenshots / Demo

- Add screenshots to `docs/` or link to a hosted demo here. (TODO)

## Contributing

- Please open issues or pull requests for bug fixes and improvements. If you contribute, keep the original project name and attribution intact.

If you'd like, I can also add an example export walkthrough, a sample template, or a minimal demo screenshot — tell me which and I'll add it.