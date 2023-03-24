# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/).

## [Unreleased]

### Added

- Combination recipes and grist costs for items introduced in minecraft 1.19

### Changed

- Update to minecraft 1.19.2
- Minestuck now uses a biome modifier to add ores to the overworld (minestuck:overworld_ores)
- The cruxite dowel emerging from a cruxtruder is now a separate block from the regular placed dowel (minestuck:emerging_cruxite_dowel)
- Cruxite dowel emerging from a cruxtruder now breaks if the pipe below breaks
- The cruxtruder now drops any held raw cruxite when destroyed
- The totem lathe card slot block will now drop held cards when destroyed
- The cruxite dowel block in the totem lathe now drops the dowel when destroyed
- The totem lathe rod now also updates if the connected dowel is destroyed
- Tweaked frequency of some land worldgen features and passive mobs
- Flora lands now uses mud block instead of coarse dirt
- The GO-button on machines behaves slightly differently now

### Fixed

- Fix metal boats not using the correct texture
- More gui text has translation keys now, making them translatable by a resource pack
- Fixed GO-button in small machines showing the wrong text
- Fixed some cases of cruxtruder data changes not being saved
- Fixed mini alchemiter comparator value only getting updated when the machine is running
- Fixed mini punch designix behaviour when punching an unpunched card

## [1.18.2-1.9.2.0] - 2023-03-13

### Added

- New weapon "Shadowrazor"

### Changed

- Update metal boat entity textures
- Update textures for items, blocks and guis

### Fixed

- Reapply stone tablet crash fix that was mistakenly reverted during the mc1.16->mc1.18 move
- Fix some player decoy behavior when entering editmode while flying
- Machine guis with a GO-button can now be started with the keypad enter key
