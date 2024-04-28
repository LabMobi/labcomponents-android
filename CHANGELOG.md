# Changelog for Mobi Lab Components

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/). This project does not use semantic versioning.

## 0.0.14 [UNRELEASED]

### Added

- ..

### Changed

- ..

### Deprecated

- ..

### Removed

- ..

### Fixed 

- ..

### Security

- ..

## 0.0.13 - 2024.04.28

### Changed

- Added more granular font attributes to different TextAppearance styles. More values to define in the beginning but makes the styles reusable. Additionally, the entire typography styles file can be copy-pasted into projects to remove dependencies on the underlying design system's default values.

## 0.0.12 - 2024.02.05

### Fixed

- LabTextField cursor error color override not resetting itself after error state is cleared.


## 0.0.11 - 2024.02.02

### Changed

- LabTextField.`setCursorColorOverride` replaced with `setCursorColor` and `setCursorErrorColor` from MaterialTextInputLayout.
- Updated Material dependency to version 1.11.0

## 0.0.10 - 2023.10.18

### Changed

- Button .Big styles are now default styles with no extra ending. For example. Widget.Lab.Button.Filled is now the same as Widget.Lab.Button.Filled used to be.
- Added button .Small styles to represent the old default styles. 

## 0.0.9 - 2023.10.17

### Added

- Added LabTextField class that extends TextInputLayout.
- Added LabToolbar to provide additional functionality for MaterialToolbar
- Added a base Lab theme with our colors and text styles. 
- Added an extensive Color system based on Figma Design Template.
- Added LabButton class that extends MaterialButton.
- Made the project source code publish on  GitHub.
- Enabled publishing to Maven Central
