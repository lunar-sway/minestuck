# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/).

## [Unreleased 4/13]

### Added

- Animations for the new alchemy equipment, consorts, underlings, and pawn models
- Horse clock, the animated clock that is also horse themed
- Sound effect for cruxtruder as well as bite, ground slam, and several limb based attacks
- Ground slam attack for Ogres
- Fireball/tail whip attacks for Basilisks (and fire immunity)
- New wood block set and tree features called Shadewood which generate in Shade land Rough biomes
- Added shade stone rock blobs to Shade lands and to the Rough biomes
- Bookshelves for various minestuck wood types
- Ladders for various minestuck wood types
- 66 new weapons (8 with alternate form)
- Mirror block
- Cryptid photo item (alchemy ingredient)
- Skaianet Denier block that prevents Entry
- Large cake blocks that are used to make large cake Features in Cake lands
- Tag support for terrain land types and title land types
- Grist costs for additional common Forge metal ingot/ore item tags
- Grist Collector block
- New expandable tooltips for puzzle related blocks
- Added suspicious chiseled mycelium bricks
- 2 Sburb Editmode tools (Revise and Recycle)
- Atheneum tab which allows you to select basic building blocks and deploy their punched cards by holding sneak key
- Sburb Cursor entity that appears when using Editmode tools

### Changed

- Remodels of alchemy equipment, consorts, underlings, and pawns
- Changed various speed/knockback resistance/health/damage attributes for underlings and consorts
- Redstone clock is now muffled by the vibration occluding blocktag instead of just wool
- Reduced spawn cap of underlings and consorts
- Changes to underling and consort hitbox sizes
- Increased number of hitboxes for Basilisk
- Imps now stop attacking players on their own when they reach rung 15 instead of rung 18
- Increased frequency of vitality gel drops from underlings
- Changed Cat Claws recipe to Makeshift Claws && Iron Ingot
- Updated Cat Claws texture
- Changed Jousting Lance recipe to Wooden Lance && Iron Ingot
- Changed Cigarette Lance recipe to Regilance && Eightball
- Rebalanced Cigarette Lance Grist Cost and Damage
- Added alchemy recipe for Ancient Debris: Steel Beam && Raw Uranium
- Added Grist Cost to Eightball and Dice items
- Changed Applescab Scalemate's recipe to Red Wool && Dragon Breath
- Added alchemy recipes for every Scalemate
- Changed what dyes can be used to change Scalemate color in the offhand to reflect recipe additions
- Changed Pinesnort Scalemate's in-game name from Pinesnout to Pinesnort
- Fixed large item models' item frame displays
- Updated item texture for Mini Wizard Statue
- The "minestuck:land_type" loot condition now uses tags instead of land type groups
- Some consort dialogue conditions now uses tags instead of land type groups
- New way to determine which land types that can get picked at random,
defined using data files "data/\<namespace\>/minestuck/terrain_land_types.json" and "data/\<namespace\>/minestuck/title_land_types.json"
- Grist mining tools (currently only Grist N Mine) now only extract grist at 50% efficiency
- Minor tweaks to tooltips of some existing puzzle related blocks
- Land vegetation now has a partial random XZ offset the same way. Hitboxes are more appropriate
- Fixed blocks in the LOGS tag that Minestuck added not being given an automatic grist cost
- Modified land terrain generation settings to have more variation
- Grist selector screen now exits to the previous screen when pressing esc
- Editmode players will now break the entire multiblock when directly breaking one of their individual blocks.

### Removed

- Remove json grist cost for treated planks (There is already a grist cost for the planks item tag which will determine the cost instead)

### Fixed

- Restore land sky rendering
- Removed sunset fog coloration in lands
- Fixed resizing issue with grist selector screen
- Fixed some light problems with entry, possibly fixed other behaviors with entry

### Contributors for this release

- kirderf1, Dweblenod, Akisephila, rose_bushes_, Hadean, Emma "Dilemma", Carnie, Riotmode, sipherNil, blankMushroom, Caldw3ll, Amiwyn, LunaticCat, Doro

## [Unreleased]

## [1.19.2-1.9.3.0] - 2023-03-24

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

### Removed

- Removed config options "generateCruxiteOre" and "generateUraniumOre".
For the overworld, these ores can now instead be disabled by overriding the new biome modifier with a datapack.

### Fixed

- Fix metal boats not using the correct texture
- More gui text has translation keys now, making them translatable by a resource pack
- Fixed GO-button in small machines showing the wrong text
- Fixed some cases of cruxtruder data changes not being saved
- Fixed mini alchemiter comparator value only getting updated when the machine is running
- Fixed mini punch designix behaviour when punching an unpunched card

### Contributors for this release

- kirderf1, Dweblenod, LunaticCat

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

### Contributors for this release

- kirderf1, Dweblenod, Akisephila, rose_bushes_
