# Changelog for [Unseaworthy](https://github.com/murphy-slaw/unseaworthy)

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.2.0] 2024-10-07

### Added

- New configuration option - minimum water depth to sink boats. This allows you to permit boats to operate in shallower 
waters of tagged biomes.

## [1.1.2] 2024-07-07

### Changed

- Made biomes that sink boats configurable via biome tag
- Switched from mucking around in EntityData to using the Cardinal Components API for extra boat entity data

## [1.1.1] 2024-07-04

### Fixed

- So, uh, funny story. I made this mod to make boats less useful so that players would build the ships from the Small
Ships mod. It turns out, those ships inherit from the vanilla Boat class, so they got all the behavior that I gave to
boats. Which is to say, they sank.

This release uses entity tags to limit the "sink in oceans" behavior to vanilla boats and chest boats.

## [1.1.0] 2024-07-03

### Added

- Now has a configuration screen under ModMenu
- Boat destruction time is configurable
- Boat destruction % is configurable
- Fate of wrecked boats can be set to one of three modes
    - SINK: boat is pulled underwater and ejects passengers
    - BREAK: boat is destroyed and drops its item entity
    - DESTROY: boat is destroyed and drops a random amount of planks and sticks.

## [1.0.0] 2024-06-27

Initial public release.

### Features

- Boats floating on water in ocean biomes will shake and bounce as if on rough seas.
- 5 seconds later they will break apart into planks and sticks.
- That's all.
