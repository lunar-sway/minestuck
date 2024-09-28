# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/).

## [Unreleased 1.20.4]

### Added

- Block Teleporter
- Built in compatability for Better Combat mod

### Changed
- Gave Underlings and Carapacian Pawns the Attack Speed attribute. This influences how fast their attack animations play
- Underlings, Carapacian Pawns, and Consorts now adjust their walk animation speed according to their Movement Speed attribute
- Structure Block Registry Processor can now be used for data generated structures through a processor_list
- Adjusted the hitbox size for Lotus Flowers
- The Lotus Time Capsule now spawns a Lotus Flower when placed
- Lotus Time Capsule blocks now remove any Lotus Flowers above them when destroyed

### Fixed
- The Umbral Infiltrator no longer jitters when thrown by a void player
- Manually-spawned Lotus Flowers can no longer be hurt or killed by any damage source
- Lotus Flowers no longer push entities inside of them away
- Ogres and Basilisks no longer slide around when idling

### Contributors for this release

- Dweblenod, sipherNil, gtzc, Cibernet

## [Unreleased]

## [1.20.1-1.12.1.0] - 2024-09-08

### Added

- Predetermined captcha codes can now be set through a data file (`data/<namespace>/minestuck/captcha_codes.json`)

### Changed

- Silicone Caulk Bucket alchemy recipe was changed from Water Bucket && Quartz to Water Bucket && Amethyst Shard, to avoid conflict with Prismarine Shard alchemy recipe
- Frost Trees will now drop saplings
- Saplings are now pottable
- Aspect slabs can now be used in crafting recipes
- Uranium button now made with 1 raw uranium instead of 1 uranium block

### Fixed

- Fix player gutter multiplier, which wasn't being saved
- Space sapling texture baseline lowered, space and frost sapling sides trimmed
- Fixed crash involving null targets with animated attacks
- Changed aspect-wood bookshelf recipes to match their book drop rate
- Added missing pressure plates to tag
- Activating the hashmap modus from chat now gives the correct message

### Contributors for this release

- Akisephila, kirderf1, glubtier, ThalliumSulfate, Dweblenod

## [1.20.1-1.12.0.1] - 2024-04-15

### Fixed

- More logging for grist cost generator to help identify bottlenecks
- Perfectly generic and cindered signs work again
- Fixed execution permissions for the `minestuck:command` dialogue trigger
- Add missing wood-related recipes
- Updated holopad collision shape
- The block model for the holopad with card now renders with cutout

### Contributors for this release

- kirderf1, glubtier, Dweblenod

## [1.20.1-1.12.0.0] - 2024-04-13

### Added

- Spawn egg items for main Underling types, Consorts, and Carapacians
- Stairs, slabs, walls, fences, fence gates, doors, trapdoors, buttons, and pressure plates to land-specific blocks
- Carved Logs, Carved Wooden Leaf, and Treated and Lacquered variants of Carved blocks
- Tree Stump feature in Forest Lands
- Frog Ruins feature in Frog Lands
- Various Unfinished and Carved features in Wood Lands
- Transportalizer codes can now be manually set instead of having one randomly assigned (can only be done once per transportalizer)
- New NBT tag on transportalizers that allows them to be locked, permanently preventing the viewing or editing of its id or destination
- Transportalizer NBT data will now be saved when mined, even without silk touch, and can be viewed on the item's tooltip
- New advancements for Echeladder milestones, using an Intellibeam Laserstation, getting a max tier weapon, and buying every item from a consort merchant
- New computer Themes: Astral Charts, Lifdoff, LOWAS, Minestuck, SBURB 10, Scourging Heat, Skaianet Black, Skaianet Green, Skaianet White, Spirograph, ~ATH
- Carved Bush
- Uncarved Wood Cruxite, Uranium, Iron, Redstone, and Emerald ores
- Silicone Caulk fluid, used in Wood lands
- Cindered woodset and Trees in Heat lands
- Igneous Stone, Magmatic Igneous Stone, Pumice Stone, Singed Grass and Foliage, and Igneous Spike, that generate in Heat lands.
- Sandy Grass, Dead Foliage, Tall Sandy Grass and Tall Dead Bush that generate in Sand lands.
- Coffee Claws
- TV Antenna
- Molten Amber fluid, used in Heat lands
- Cruxite block set
- Black Stone Cruxite, Uranium, Gold, Redstone, and Quartz ores
- Alchemy recipes for all Minestuck fluids
- Magmatic Black Stone Bricks
- Cast Iron Frame, Sheet, and Tile
- Sulfur Pools and Cast Iron features in Heat Lands
- Wooden Lamp and Heat Lamp blocks, used in Wood and Heat lands respectively
- Chocolatey Cake - the most scrumptious cake in the entire world!
- New dialogue system that uses a gui screen, replacing the chat-based system
- Dialogue in this new system is data-driven, and can be defined or tweaked through datapacks
- New dialogue
- Carapacian pawns now have dialogue
- New structure in rain lands
- Signs and hanging signs for all wood types
- Shade and glowing block sets updated
- Doors and trapdoors added
- All aspect block sets updated
- Minor Aspect trees added

### Changed

- Minor changes to what is shown in the data checker
- Duplicate titles are now allowed in the same session,
  and consequently the session size limit has been removed
- Pre-entry players no longer play a part in grist gutter capacity,
  and they no longer receive grist from the gutter
- Wooden Cactus is now only placeable on the Wood Terrain Blocks tag instead of Sand
- Carved Planks are now the surface block of Wood Lands instead of Treated Planks
- Carved Planks no longer need to be mined with a Stone Axe or better
- Transportalizers now stack to one instead of 64
- Prismarine armor remodel and retexture
- Iron Lass Armor has now been remodeled, and retextured; with additional new animations.
- Computer Themes can now be added and rendered via resource pack
- Default computer Theme for crockertop is now Crocker
- Computer Theme is now chosen from a list of available Themes in separate GUI
- Changes to computer Theme texture Crocker
- Computer programs now always use Theme specific texture for arrow buttons
- Updated textures of Treated Planks, Chipboard, Wood Shavings, and Wooden Cactus by Vinnelli and Riotmode
- Updated textures of Treated Bookshelf and Treated Ladder to fit updated Treated Planks
- Oil Buckets no longer cost 8 Tar and 8 Shale, now cost 16 Shale
- Renamed Oil to Shale Oil
- Pumord is now alchemized with Pumice Stone instead of any regular Stone
- Minor change to land terrain height
- Black Sand now generates in Heat lands
- Updated Textures of Cast Iron blocks
- Steel Beams are now alchemized with Iron Blocks instead of Cast Iron Blocks
- Existing consort dialogue have been ported to the new dialogue system with some tweaks
- New format for the data file `grist_cost_generation_recipes.json`

### Removed

- Removed "skaianetCheck" config option
- Uncarved Wood no longer has a tooltip

### Fixed

- Fixes some niche issues with connections
- `/sburbpredefine` now gives an error when applied to a player that already has a land
- Fixed missing data sync for land types after using `/debuglands`
- Added command success message to `/debuglands`
- Fixed player owner id not loading for mini alchemiter and grist widget
- Double slabs will now drop two slabs when broken
- Sneaking now prevents trajectory blocks from moving the player
- Blue dirt can now have the shovel used on it
- Minestuck fences will now connect with vanilla fences appropriately
- All walls will now connect with each other properly
- Fixed name typos for some chess castle blocks
- Area effect blocks now save their effect as a string id instead of an int id
- Improved grammar on some dialogues
- Error handling when teleporting entities during entry

### Contributors for this release

- rose_bushes_, hadean, glubtier, ScarabOasis, Dweblenod, Boxfox, DORO, Akisephila, Vinnelli, Riotmode, pavizi, sipherNil, kirderf1

## [1.20.1-1.11.2.1] - 2024-03-10

### Fixed

- Metal Boats no longer crash the game when dispensed
- Block Pressure Plates no longer cycle power states endlessly when players are crouched on them
- Fixed underling texture layer that on hurt wasn't tinted red with the rest of the underling
- Fixed fall damage for minestuck fluids
- Fluids no longer break gates and return nodes
- Advanced color selection screen no longer tints buttons
- Round the displayed max health value on the echeladder screen
- Prevent one method of captchaloguing stacks with stack size larger than max stack size
- Stopped editmode cursor from spinning

### Contributors for this release

- hadean, Dweblenod, kirderf1, glubtier

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
