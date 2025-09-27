# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/).

## [Unreleased]

### Added

- Added music to the other dimensions in the Medium

### Contributors for this release

- heartsremedy, Dweblenod

## [1.21.1-1.13.1.1] - 2025-09-14

### Added

- Translated key for item tags used in JEI

### Fixed

- Fix Captcharoid Cameras crashing when used on blocks that do not have item counterparts
- Empty Sylladex button being invisible
- Fix Echeladder screen not displaying before Entry
- Fix Prismarine and Iron Lass armors crashing when enchanted
- Fix error saving consort merchant inventory

### Contributors for this release

- medsal15, Dweblenod, kirderf1

## [1.21.1-1.13.1.0] - 2025-07-26

### Added

- New rook themed towers on the Battlefield
- Increased number of blocks used in land specific structure block palettes
- Land Type Extensions now support structure sets.
- New Cathedral Structure on the Battlefield
- Added tooltip to rungs which have gated conditions

### Changed

- Stack and Queue modi have more distinct recipes
- Farmine break blacklist is now controlled by the `minestuck:farmine_break_blacklist` block tag, instead of being hardcoded
- Disabled Skaian Castle structure
- Moved `prospit_wfc_demo` dimension and related wfc examples to `additional_resources` datapack
- Disabled `wfc_performance_test` command

### Fixed

- Fix some items having 0 grist cost
- Fix weapons having the wrong durability
- Fix unbreakable katana and crowbar being breakable
- Fix emerald weapons dealing no damage
- Fix armor stacking/durability/toughness
- Fix farmine only mining a 3x3x3 cube around the block mined
- Fix carved tablets crashing the game when placed down
- Fix issues with frog entity/item rendering/loading/tooltips
- Fix cassette player not playing correct music
- Fix dance_stab_dance and emissary_of_dance being labelled as each other
- Fix cassette playing weapons not having a menu for storing cassette
- Fix weapons not being enchantable
- Add back song description for cassette items
- Fix hover area for some tooltip in the echeladder screen
- Removed Refined Storage compatibility feature that was not compatible with Refined Storage 2
- Fix entry breaking certain Create contraptions
- Improve land skybox performance

### Removed

- Removed `minestuck:non_mirrored` recipe type
- Removed `preEntryRungLimit` server config
- Minestuck tridents no longer get unusable trident enchantments

### Contributors for this release

- medsal15, Dweblenod, TangleKat, kirderf1, ambiguousChronology, ThalliumSulfate

## [1.20.1-1.12.1.1] - 2025-04-18

### Fixed

- Fix crash with Geckolib 4.7.1+ mentioning giclops.animation.json
- Color selection button is available in the SBURB Client program before connection again instead of after
- Cruxite button now uses raw cruxite instead of cruxite block, which resolves a conflict with storage block breakdown
- The block model for the holopad with card now renders with cutout
- Carved totems in alchemiter model now have correct uv mapping

### Contributors for this release

- kirderf1, blankMushroom

## [1.21.1-1.13.0.1] - 2025-04-14

### Fixed

- Fix multiplayer crash with grist cache screen

### Contributors for this release

- kirderf1

## [1.21.1-1.13.0.0] - 2025-04-13

- Updated to run with Minecraft 1.21 / Neoforge 21.1

### Added

- Added Moon Cake
- New consort and carapacian dialogue
- Added 2 tracks by triagegremling: october and endless chasm
- Added 2 tracks by Caldw3ll: the note desolation plays and desolate strife
- New Disk Manager utility in Settings computer program for ejecting disks
- Computer interaction sound effects
- New computer related advancement
- Message when entering edit mode to remind players of the exit keybind
- Computer programs now have a tooltip when hovered over in computer gui
- Data files for echeladder rungs and rung effects
- Echeladder now provides fall damage resistance
- Some work-in-progress content not accessible through normal gameplay:
  - Prospit and Derse dimensions and block sets
  - Veil dimension and meteoric stone block
  - Dream moon structure generation system with demo dimension

### Changed

- Updated land skybox rendering
- Most vanilla enemy mobs now provide Echeladder exp
- Echeladder exp is provided when gaining the advancements for obtaining a legendary weapon/obtaining a netherite hoe/entering the Nether/entering the End
- There is no longer a threshold under which certain experience sources will stop contributing. After the incoming exp has been modified for the given rung, there is a 1 in 2/exp chance that 1 exp will be added.
- Echeladder related attributes now scale linearly instead of exponentially, with minor tweaks to values on each rung
- Tweaks to Echeladder GUI
- Knitting needle crafting recipe now produces one item instead of two
- Updated coarse end stone texture

### Fixed

- Wall Signs now mineable with axe
- Non-color data will now be preserved on dowels going through a totem lathe
- Item stack data will now be preserved on disks inserted into a computer
- Fix disk insertion and computer screen for computer easter egg
- Rung progress bar is now white when it would otherwise be too dark to see progress clearly

### Removed

- Removed "aspectEffects" config option as it is redundant to modifying rungs.json

### Contributors for this release

- Cibernet, Dweblenod, kirderf1, glubtier, blankMushroom, hadean, ThalliumSulfate, triagegremlin, Caldw3ll, heartsremedy, rose_bushes_, v_sabitron

## [1.20.4-1.12.2.1] - 2025-03-16

### Fixed

- Shade stone no longer has texture of shade stone cruxite ore
- Carved totems in alchemiter model now have correct uv mapping
- Removed collision from buttons
- Fixed many blocks not having a correct tool
- Color selection button is available in the SBURB Client program before connection again instead of after
- Fixed untranslated key when using /gutter show command
- Fixed widget rendering in Color Selection screen

### Contributors for this release

- Dweblenod, blankMushroom, kirderf1

## [1.20.4-1.12.2.0] - 2024-10-06

- Updated to run with Minecraft 1.20.4 / Neoforge 20.4

### Added

- Block Teleporter
- Built in compatability for Better Combat mod
- Cindered ladder and bookshelf
- Shadewood ladder and bookshelf
- Parrots will mimic imps, ogres, basilisks, and liches

### Changed

- Gave Underlings and Carapacian Pawns the Attack Speed attribute. This influences how fast their attack animations play
- Underlings, Carapacian Pawns, and Consorts now adjust their walk animation speed according to their Movement Speed attribute
- Structure Block Registry Processor can now be used for data generated structures through a processor_list
- Adjusted the hitbox size for Lotus Flowers
- The Lotus Time Capsule now spawns a Lotus Flower when placed
- Lotus Time Capsule blocks now remove any Lotus Flowers above them when destroyed
- Many organic blocks are now compostable
- Many wood-based blocks can now be used as fuel
- Cruxite ore textures tweaked
- Localized potion name for the status message that appears when right-clicking an Area Effect Block with a potion
- Localized entity type for the status message that appears when right-clicking a Summoner Block with a Spawn Egg

### Fixed

- The Umbral Infiltrator no longer jitters when thrown by a void player
- Manually-spawned Lotus Flowers can no longer be hurt or killed by any damage source
- Lotus Flowers no longer push entities inside of them away
- Ogres and Basilisks no longer slide around when idling
- Cruxite button now uses raw cruxite instead of cruxite block, which resolves a conflict with storage block breakdown
- Bookshelves for terrain woods can now be crafted, resolving a conflict with the vanilla bookshelf recipe
- Fixed right-clicking on an Area Effect Block with a Potion not properly updating its settings
- Fixed right-clicking on a Summoner Block with a Spawn Egg not properly updating its settings
- Updated holopad collision shape
- The block model for the holopad with card now renders with cutout

### Contributors for this release

- Dweblenod, sipherNil, gtzc, Cibernet, glubtier, rose_bushes_, kirderf1

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

- kirderf1, Dweblenod, Akisephila, rose_bushes_, Hadean, Emma "Dilemma", Carnie, Riotmode, sipherNil, blankMushroom, Caldw3ll, Amiwyn, LunaticCat, DORO, SarahK

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

- Riotmode, rose_bushes_, Dweblenod, kirderf1, DORO, Akisephila, blankMushroom

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

## [1.18.2-1.9.0.0] - 2022-12-27

### Added

- Grist toast notifications
- New hats
- Added Music Sword
- Added cassettes for additional vanilla tracks
- Added Green Stone Brick Embedded Ladder, which is now used in frog temples
- Villager trades for minestuck-related items

### Changed

- Sburb code is now written down in parts
- Sburb disks are now obtained using the new disk burner program
- Add functionality to Mine and Grist
- Blank disks can now be crafted
- Lotus flower now uses a loot table
- Tweak overworld loot slightly

### Fixed

- Fix compatibility issue with TerraBlender
- Fixed item for large machines not being used up in survival,
  and made other small improvements to placing large machines
- Add missing usage message to oW THE EDGE
- Fix frog temple generation performance issue

### Contributors for this release

- kirderf1, Dweblenod, Riotmode, LunaticCat, Caldw3ll

## [1.18.2-1.8.1.2] - 2022-11-15

### Fixed

- Fix transportalizer bug where they stop working as destination after being unloaded
- Fix item deletion bug in the captcha deck screen
- Fix captchalogue dupe bug
- Fix up focus on transportalizer text box
- Fix horn position in the lich model

### Contributors for this release

- kirderf1

## [1.18.2-1.8.1.1] - 2022-11-01

### Fixed

- Fix basilisk-related crash
- Fix geckolib-related crash
- Bumped up minimum forge version

### Contributors for this release

- kirderf1

## [1.18.2-1.8.1.0] - 2022-10-30

- Updated to Minecraft 1.18.2
- Minestuck has a new dependency: Infiniverse

### Added

- New music track "Lillypad Rain" that plays in frogs and clockwork lands
- New music tracks "Sickest Fires", "Rancorous Gambligant", "What Goes Up" that plays in all lands
- New music track "Rise Up" that may be used in the future
- Sound effects for magic effects and lotus flower loot spawn
- Combination and grist cost recipes for new vanilla items

### Changed

- Starting moduses are now defined through datapacks
- Land and skaia world gen has been revamped somewhat to fit the new worldgen system
- World height has been increased for lands
- Ore generation in land and the overworld has been adapted to the new world heights
- Minestuck versions of iron/gold ore will drop raw iron/gold
- New ore textures
- Grist and vitality gel no longer have fire immunity
- A number of other technical entites now have fire immunity
- Changed sound and other properties of some block groups
- Changed system behind grist layer generation
- Ignore fog density in custom fluids for spectators
- Changed strawberry stem block drops to match that of vanilla stem blocks
- Grist type secondary drops are now handled by grist type data tags
- Grist layer spawn categories are now handled by grist type data tags
- Petrified flora now uses block tag `minestuck:petrified_flora_placeable` for placeable blocks
- Kundler surprise egg now uses a new data loot table `minestuck:kundler_suprises` for its drop
- Frogs now uses item tag `minestuck:bugs` for lure/tempt behavior instead of a hardcoded list of items
- Hungry consort dialogue now matches items against a new item tag `minestuck:consort_snacks` instead of a hardcoded list of items
- Changed registry id of `mossy_coarse_stone` block to `mossy_coarse_stone_bricks`
- Changed registry id of `castle_brick` blocks to `chess_brick`
- Changed registry id of `stone_slab` to `stone_tablet`
- Renamed `minestuck:portable_block_replacable` block tag to `minestuck:portable_block_replaceable`
- Renamed `minestuck:land_aspect` loot condition to `minestuck:land_type`
- Changed conditions used for spawning mobs using summoner block
- Block property change for gate and return node blocks: the "main" block is now a different block and not just a different state

### Fixed

- Fixed poster being wrongly rotated when loading
- Fixed flowing light water texture animation
- Added missing model for cassette "Far" inside cassette player
- Strawberry stem now properly uses cutout render type
- Fix some display problems with stone tablets
- Lava blocks in "ocean rundowns" in heat lands now properly flow on generation
- Fixed redstone power related block updating for a few puzzle blocks
- Fix some interactions with the totem lathe

### Removed

- Removed wyrm entity
- Removed vein blocks
- "startingModusTypes" config option was replaced by new datapack file

### Contributors for this release

- kirderf1, Amiwyn, Dweblenod, sipherNil, Caldw3ll, FrostyMac, DORO

## [1.16.5-1.8.0.1] - 2022-07-28

### Fixed

- Patched issue with infinite loop when checking whether all players in a session have entered and completed connections

### Contributors for this release

- kirderf1

## [1.16.5-1.8.0.0] - 2022-07-17

### Added

- Toggler block: toggles a specific blockstate that many puzzle blocks have been modified to possess or been given (if they are new), can be toggled itself to instead shut off powered redstone blocks
- Remote Comparator block: checks for blocks at a specific distance and gives off redstone if it matches the type that is directly behind it, can be toggled to additionally matches blockstates
- Structure Core block: records data to specific structures and can permanently shut down specific blocks functionality (summoners and area effect blocks) if otherwise set to read data from said structures, intended for actual use in the Tiered Dungeons update
- Pushable Block: can be pushed in a horizontal direction if right clicked, imagined with sokoban/zelda/portal style puzzles in mind, can absorb platform blocks but cannot be pushed into their way, destroys small plants and any blocks listed in the block tag "pushable_block_replacable"
- Block Pressure Plate: will give off redstone if a player(who is not crouching) or a block with a full faced bottom side are directly above it

### Changed

- Implemented new land structure blocks into their respective terrain land components, expanding and fixing components of structure block registries for said terrain lands in the process (fungi lands now have mycelium stone as their ground material)
- Increased functionality for the Sendificator for it to now be able to teleport items to coordinates in the same dimension after being set by a player, using uranium as fuel similar to the uranium cooker
- Area effect block can be shut down by the structure core when in READ_AND_WIPE mode
- Area effect block now works as an offset, allowing it to change the area of effect upon rotation
- Area effect GUI now also has a way to set the effect type/amplifier and change whether all mobs or just players are affected
- Item Magnet can now be machine toggled to push entities away instead of pulling them closer. Can also move the new pushable blocks upwards (turning them into a falling block entity)
- Platform block/platform generator now use new blockstate properties to reduce the amount of block updates they caused, with the platform blocks being able to check whether they are being supported and the generators able to more reliable create and remove the platform blocks, including a block tag for blocks that should absorb the platforms
- Platform block now has the ability to replace blocks like grass/fire/liquid
- Summoner now has a gui for improved capacity to set the summoned entity among other small utility
- The area at which Remote Observer can observe is now variable.
- Remote Observer now has a blacklist entity type tag for entities it shouldnt be able to detect
- Remote Observer type `IS_ENTITY_IN_WATER` has been split into a type for if an entity is submerged in water and a type for it the player is wet(in bubble column/water/rain)
- Remote Observer can now check for sprinting + entities on ground
- Stat storer alchemy recipe now uses computer parts instead of comparator
- If a Toggler block is in Discharge mode and interacts with a Stat storer, it will also reset the active stat type value to 0, increasing its capacity for automation
- Variable Solid Switch now has an alchemy recipe of Solid Switch || Comparator
- Redstone Clock now has an alchemy recipe of Clock && Comparator
- Fragile Stone alchemy recipe now uses && instead of ||
- Fragile stone now has a stonecutting recipe from stone
- Retractable Spikes now has an alchemy recipe of Spikes && Sticky Piston
- Wireless Redstone Transmitter now works as an offset, allowing it to power different receivers depending on rotation
- Wireless Redstone Transmitter now uses computer parts instead of quartz in its crafting recipe
- Expanded the list of blocks that the Rotator can rotate (now includes repeaters and comparators for advanced redstone)
- Rotator now has a crafting recipe
- Updated some preexisting block textures related to terrain lands
- Renamed "Mossy Coarse Stone" to "Mossy Coarse Stone Bricks"

### Fixed

- Fixed issue with the redstone clock tick speed resetting every time it was unloaded

### Contributors for this release

- Badadamadaba, Dweblenod, SipherNil, Riotmode, kirderf1

## [1.16.5-1.7.0.3] - 2022-05-14

### Fixed

- Fix client-side editmode crash related to item tooltips
- Fix sylladex not immediately syncing when switching modus

### Contributors for this release

- kirderf1

## [1.16.5-1.7.0.2] - 2022-04-21

### Added

- New combination recipes and grist costs

### Changes

- Allow crouch interaction for more puzzle blocks

### Fixed

- Fix stone tablet placement crash
- Fix some data sync problems with puzzle blocks
- Fix some title generation issues
- Fix data checker render issue

### Contributors for this release

- kirderf1, Dweblenod

## [1.16.5-1.7.0.1] - 2022-04-15

### Fixed

- Fix server crash from remote observers
- Fix server crash from daggers
- Fix an interaction with area effect blocks outside of single player
- Added command argument serialization, which fixed some logged warnings and possibly some under-the-hood improvements

### Removed

- Remove loot entry from `minestuck:chests/medium_basic` that didn't serve a purpose and caused log warnings

### Contributors for this release

- kirderf1
