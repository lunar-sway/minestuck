# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/).

## [Unreleased upcoming-content]

### Added

- Spawn egg items for main Underling types, Consorts, and Carapacians
- Stairs, slabs, walls, fences, fence gates, doors, trapdoors, buttons, and pressure plates to land-specific blocks
- Tree Stump feature in Forest Lands
- Frog Ruins feature in Frog Lands
- Transportalizer codes can now be manually set instead of having one randomly assigned (can only be done once per transportalizer)
- New NBT tag on transportalizers that allows them to be locked, permanently preventing the viewing or editing of its id or destination
- Transportalizer NBT data will now be saved when mined, even without silk touch, and can be viewed on the item's tooltip
- Added Carved Bush.

### Changed

- Transportalizers now stack to one instead of 64
- Prismarine armor remodel and retexture
- Iron Lass Armor has now been remodeled, and retextured; with additional new animations.


### Fixed

- Double slabs will now drop two slabs when broken

### Contributors for this release

- rose_bushes_, hadean, glubtier, ScarabOasis, Dweblenod, Boxfox, DORO

## [Unreleased]

### Fixed

- Metal Boats no longer crash the game when dispensed
- Block Pressure Plates no longer cycle power states endlessly when players are crouched on them

### Contributors for this release

- hadean, Dweblenod

## [1.20.1-1.11.2.0] - 2024-01-13

### Added

- Grist cost added for stripped bamboo block group, suspicious blocks, sniffer egg, pink petals, torchflower, torchflower seed, pitcher plant, pitcher pod, relic music disc
- Uncarved wood block group now has a stonecutting recipe and grist cost
- `/rung get` command, which output a player's echeladder rung
- Pogo lance weapon
- New consort dialogue
- Sopor Stupor Effect
- Sopor Slime Pie food item
- Land type extensions for datapacks, which can be used to add worldgen features, carvers and mob spawn to lands
- New editmode settings screen, which can be used to teleport between different areas where editmode can function
- Behaviour for buckets and barbasol bombs when used in dispensers
- Minestuck fluids now have sound effects in situations reminiscent of water
- Light water now acts as a light source and has underwater fog appear closer
- All Minestuck fluids besides ender fluid now support boats
- All Minestuck fluids besides oil now extinguish on fire entities, entities touching oil while on fire extends fire duration
- Brain juice now forms an infinite source, it also now cannot be drowned in
- Blood and brain juice now hydrate crops
- Minestuck fluids now have custom tick rates
- Blood/brain juice/light water now have underwater particles
- Minestuck fluids now have improved mod compatibility
- The Iron Lass armor set has the ability to glide like an elytra and boost with the sneak key

### Changed

- Bamboo item now costs 1 build grist instead of 2 (bamboo plank blocks now similar in cost to traditional planks)
- Fine china axe now uses decorated pot in recipe instead of flower pot
- Changed `/setrung` command into `/rung set`
- Minor changes and polish to the temple scanner
- The area around which an editmode player can move has been made more flexible, each computer can act as a source to extend reach
- Entities now move through and sink in Minestuck fluids at rates specific to the fluid
- The texture for oil and blood are now more transparent than they were in previous versions
- Underwater fog appears closer for oil/blood/water colors/light water

### Fixed

- Fixed multiple sources of crashes involving fake players
- Creative Shock effect is no longer cured by drinking milk
- Right-clicking an item outside the atheneum menu no longer crashes the client
- Suspicion effect now correctly stops the affected mob from summoning zombie reinforcements
- Respawning mid-entry no longer spawns you in your land
- Fixed a performance issue with land gate placement
- Minestuck fluids now prevent fall damage
- Minestuck fluids no longer have a breaking sound effect and now have bucket/extinguish sound effects
- Minestuck fluids now use their translatable name
- All Minestuck fluids besides ender fluid now have transparency
- Water colors underwater fog is a rave again

### Contributors for this release

- Dweblenod, Zepsun, hadean, pavizi, kirderf1, DORO, sipherNil, Vinnelli

## [1.19.2-1.11.0.2] - 2023-11-16

### Fixed

- Fixed multiple sources of crashes involving fake players

### Contributors for this release

- Dweblenod

## [1.20.1-1.11.1.0] - 2023-10-22

### Added

- Two new computer Themes: Joy and SBURB 95
- Various new weapons including 10 keys, 6 batons, 1 wand, 2 clubs, 3 swords, 1 chainsaw, and 6 staffs. Notably includes the key/batonkind Denizen weapon, Yaldabaoth's Key-ton
- New effects: Boss Buster and an effect used to spawn particles on hit
- Native sulfur block for use in Heat lands
- Particle Accelerator item
- Boss Mob entity tag
- Consort plushes
- Mutini plush

### Changed

- New textures and slightly shorter models for transportalizers
- Old Computer now starts with SBURB 95 Theme
- Updated textures of bookshelves and ladders
- Retextured green stone blocks
- Iron Lass armor is now alchemized with the Particle Accelerator instead of Energy Core
- New texture for wizard staff
- Temple Scanner now displays direction on the item

### Fixed

- Restored the ocean rundown worldgen feature used in heat lands
- Slightly adjusted damage of Home By Midnight to give more value to alchemizing it over its ingredients

### Contributors for this release

- Vinnelli, hadean, Vivian Zane, sipherNil, SurrealDude, Emma "Dilemma", Scarab Oasis, rose_bushes_, Dweblenod, kirderf1

## [1.19.2-1.11.0.1] - 2023-10-21

### Fixed

- Fix a crash from fake players injuring underlings
- Fix a crash caused by librarian villagers in the overworld

### Contributors for this release

- kirderf1, Dweblenod

## [1.19.2-1.11.0.0] - 2023-07-22

### Added

- New operator command that can make a player enter the medium without using an artifact
- Translation keys/lang file entries for entry failure messages
- Union Buster Sword
- Suspicion mob effect (affected mobs will be forced away from other affected mobs)
- Uncarved Wood Variants (Polished uncarved Wood, Carved Heavy Planks, Carved Knotted Wood, Carved Planks)
- Expanded number of Minecraft/Forge item/block tags that Minestuck items/blocks are in
- Power hub block
- Fire, Water, and Wizard Staffs
- Rubik's Mace, mACE of Clubs, and Home Grown Mace Weapons.
- Anthvil block
- Captcha codes for all registered items (put in tooltip if it is legible)
- Intellibeam Laserstation and accompanying UNREADABLE Item Tag for gating certain item captcha codes via "legibility"
- GUI for Punch Designix for manually inputting and punching captcha codes

### Changed

- Changed textures for Rainbow Logs, Rainbow Leaves, Rainbow Planks, and Rainbow Saplings
- Changed Rainbow Sapling to use a simpler growth process without using colored state properties
- Some land worldgen features are now replacable by datapacks
- Tree features for our saplings are now replacable by datapacks
- Uncarved Wood Texture
- Hubtop combination recipe now uses power hub
- Right-clicking a Punch Designix with a captchalogue card now sets the Designix's stored captcha code if the captcha code is, or has been made, legible
- Mini Punch Designix now requires captcha cards whose codes are, or have been made, legible

### Removed

- Removed color blockstates and models for the Rainbow Sapling

### Fixed

- Fixed issue with sburb connections and /debuglands
- Grist cost generation optimisation
- Shadewood planks are now in planks tag and can now be used for crafting

### Contributors for this release

- kirderf1, hadean, Estellairon, Riotmode, Carnie, Akisephila, Zepsun, Vinnelli, Vivian Zane, Dweblenod, rose_bushes_, DORO, blankMushroom

### Idea Contributions

- gummyYummy

## [1.19.2-1.10.3.1] - 2023-06-19

### Fixed

- Grist costs did not scale with the item count when created with alchemiters or consumed with grist widgets

### Contributors for this release

- kirderf1

## [1.19.2-1.10.3.0] - 2023-06-12

### Added

- Cinnamon Sword
- Alchemiter splash particles
- Alchemiter, Transportalizer, Totem Lathe sound effects
- New growing animation for lotus flower

### Changed

- Lotus flowers now regrow after 5 minutes instead of 10 by default
- Lotus flower has been remodeled and given changes to existing animations
- Font used for on-screen message when Entering is now the newly created "Land of Copyleft and Freedom"

### Fixed

- Basilisks can now despawn again
- Clarity issues with gutter related items
- /tpz command now works with TRANSportalizers
- /tpz command code argument is no longer case sensitive

### Contributors for this release

- Vivian-Zane, rose_bushes_, DORO, Riotmode(SpoiledMysterymeat), Akisephila, Dweblenod

## [1.19.2-1.10.2.0] - 2023-04-20

### Added

- Ogre left hand punch

### Fixed

- Fixes a basilisk duplication bug with transportalizers
- Some cassettes missing a grist cost
- Cassette "5" and "Otherside" recipe were swap

### Contributors for this release

- Caldw3ll, kirderf1, LunaticCat

## [1.19.2-1.10.1.0] - 2023-04-16

### Added

- Added 75 more blocks to the default atheneum list.
- Config option for delaying entry from when the land dimension is generated, which can help with performance.
Note that this option is found in the common config and not the world config.
- Particle effect while entering
- Transportalizer particle

### Changed

- Changed how underling attack hit-detection works to use cones instead of spheres.

### Fixed

- Fixed a crash with TerraBlender
- Fixed crash with invalid transportalizer data
- Connections with missing land dimensions gets partially reset to before entry at server start
- Fixed editmode players not being able to destroy machines
- Messages for more cases where entry may fail
- Spawn height for re-entry is now the top motion-blocking block
- Fixed entering editmode sometimes placing the player at the wrong height
- Prevent an overflowed grist gutter from negatively influencing ability to pick up grist
- Fixed grist cache view in editmode using the limit of the wrong player
- Cache limit will now be accounted for more often client-side
- Clearer and more accurate grist toasts when at cache capacity
- Underling attacks will no longer hit the player if the attack is not facing them.

### Contributors for this release

- kirderf1, Caldw3ll, Dweblenod, Riotmode

## [1.19.2-1.10.0.1] - 2023-04-13

### Fixed

- Fix client-side crash on player death

### Contributors for this release

- kirderf1

## [1.19.2-1.10.0.0] - 2023-04-13

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
- Grist gutter, which is separate for each session
- Gutter-related items that can increase the capacity of the grist gutter
- New command `/grist_gutter show` to display the content and size of the gutter
- Cassettes for "5" and "Otherside"

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
defined using data files `data/<namespace>/minestuck/terrain_land_types.json` and `data/<namespace>/minestuck/title_land_types.json`
- Grist mining tools (currently only Grist N Mine) now only extract grist at 50% efficiency
- Minor tweaks to tooltips of some existing puzzle related blocks
- Land vegetation now has a partial random XZ offset the same way. Hitboxes are more appropriate
- Fixed blocks in the LOGS tag that Minestuck added not being given an automatic grist cost
- Modified land terrain generation settings to have more variation
- Grist selector screen now exits to the previous screen when pressing esc
- Editmode players will now break the entire multiblock when directly breaking one of their individual blocks
- Player grist cache now has an upper limit that depends on the echeladder rung of the player

### Removed

- Remove json grist cost for treated planks (There is already a grist cost for the planks item tag which will determine the cost instead)

### Fixed

- Restore land sky rendering
- Removed sunset fog coloration in lands
- Fixed resizing issue with grist selector screen
- Fixed some light problems with entry, possibly fixed other behaviors with entry
- End leaves burn in a more reasonable way now
- Fix gap between grist toasts

### Contributors for this release

- kirderf1, Dweblenod, Akisephila, rose_bushes_, Hadean, Emma "Dilemma", Carnie, Riotmode, sipherNil, blankMushroom, Caldw3ll, Amiwyn, LunaticCat, Doro, SarahK

## [1.19.2-1.9.3.0] - 2023-03-24

### Added

- Combination recipes and grist costs for items introduced in minecraft 1.19
- New interface for the computers with a desktop home screen
- Advanced color selector with support for colors other than the original sixteen
- Settings program and a simple theming system to change the color of the desktop

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

## [1.18.2-1.9.1.0] - 2023-02-19

### Added

- Added moss carpet patches/rooted azalea trees/lush cave vegetation to Flora lands
- Added carved caves/moss carpet patches/berry bush patches/mossy forest rocks/hanging roots and lush cave vegetation to cave ceilings/rooted dirt deposits to Forest lands
- Added carved caves/iceburgs/pools of ice/ice spikes/more snow and ice deposits underground to Frost lands
- Added nether fungus patches to Fungi lands
- Added nether style carved caves/larger black stone deposits underground to Heat lands
- Added dripstone features and various rock deposits to Rock Lands
- Added vanilla monster dungeons to Monster Lands
- Added mycelium cruxite and uranium ore blocks

### Changed

- Removed discs of leaves from Wood Lands
- Other tweaks to generation of features in Lands
- Extend computer block state properties to represent when a computer has disks inserted
- Computer model updates for most computer variants
- Updated ice shard texture
- Updated pink stone coal ore texture

### Fixed

- /debuglands now require permission level 2 to be used
- Item capability data is no longer lost when the item is put in a card
- Skylight in lands is now determined as intended
- Pumpkins(what pumpkins?) no longer generates overtop oceans in Silence Lands
- Bucket structures no longer sometimes spawns with non-source fluid blocks

### Contributors for this release

- Riotmode, rose_bushes_, Dweblenod, kirderf1, Doro, Akisephila, blankMushroom

## [1.18.2-1.9.0.2] - 2023-01-09

### Fixed

- Fix crash with Mine and Grist
- Fix and tweak some computer interactions

### Contributors for this release

- kirderf1

## [1.18.2-1.9.0.1] - 2022-12-28

### Fixed

- Fix crash on player death

### Contributors for this release

- kirderf1
